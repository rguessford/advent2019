import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class day14 {
    public static void main(String[] args) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("src/main/res/day14"));
            HashMap<ReactionFactor, HashSet<ReactionFactor>> reactions = new HashMap<>();
            while (reader.ready())  {
                String rxn = reader.readLine();
                String[] splitRxn = rxn.split(" => ");
                String product = splitRxn[1];
                String[] splitProduct = product.split(" ");

                HashSet<ReactionFactor> reactantSet = new HashSet<>();
                String reactants = splitRxn[0];
                String[] splitReactants = reactants.split(", ");
                for (int i = 0; i < splitReactants.length; i++) {
                    String reactant = splitReactants[i];
                    String[] splitReactant = reactant.split(" ");
                    reactantSet.add(new ReactionFactor(splitReactant[1], Integer.parseInt(splitReactant[0])));
                }
                reactions.put(new ReactionFactor(splitProduct[1], Integer.parseInt(splitProduct[0])), reactantSet);
            }

            HashMap<String, Long> toMake;
            Map<String, Long> excessRollback = new HashMap<>();
            Map<String, Long> excess = new HashMap<>();
            long ore = 1000000000000L;
            long fuel=0;
            long amount = 10000000000L;
            boolean rollback = false;
            while (true) {
                HashMap<String, Long> willRequire = new HashMap<>();
                willRequire.put("FUEL", amount);
                if (rollback){
                    excess = excessRollback;
                    rollback = false;
                }
                excessRollback = clone(excess);
                do {
                    toMake = willRequire;
                    willRequire = new HashMap<>();
                    willRequire.put("ORE", toMake.getOrDefault("ORE", 0L));
                    for (Iterator<String> chems = toMake.keySet().iterator(); chems.hasNext(); ) {
                        String chem = chems.next();
                        if (chem.equals("ORE")) {
                            continue;
                        }
                        long qtyNeed = toMake.get(chem);

                        //find rxn that produces the chemical
                        Map.Entry<ReactionFactor, HashSet<ReactionFactor>> reactionOfConcern = null;
                        for (Map.Entry<ReactionFactor, HashSet<ReactionFactor>> reaction : reactions.entrySet()) {
                            if (reaction.getKey().equals(chem)) reactionOfConcern = reaction;
                        }

                        //find no. of rxns needed to produce req'd qty
                        ReactionFactor product = reactionOfConcern.getKey();
                        long qtyProduced = product._quantity;

                        long excessOfChemAvailable = excess.getOrDefault(chem, 0L);
                        if (excessOfChemAvailable >= qtyNeed) {
                            excessOfChemAvailable -= qtyNeed;
                            excess.put(chem, excessOfChemAvailable);
                            qtyNeed = 0;
                        } else {
                            qtyNeed -= excessOfChemAvailable;
                            excess.remove(chem);
                        }

                        long rxnsRequired = Math.round(Math.ceil((double) qtyNeed / (double) qtyProduced));

                        //track excess?
                        if (qtyProduced * rxnsRequired > qtyNeed) {
                            excess.put(chem, qtyProduced * rxnsRequired - qtyNeed);
                        }

                        //add reagents to willRequire
                        for (ReactionFactor reactant : reactionOfConcern.getValue()) {
                            long neededQty = willRequire.getOrDefault(reactant._symbol, 0L);
                            neededQty += reactant._quantity * rxnsRequired;
                            willRequire.put(reactant._symbol, neededQty);
                        }
                    }
                } while (willRequire.size() > 1);

                ore -= willRequire.get("ORE");
                if(ore < 0){
                    ore += willRequire.get("ORE");
                    amount = amount / 10;
                    rollback = true;
                    if(amount == 0) break;
                } else {
                    fuel+= amount;
                    System.out.println(fuel);
                }

            }
            System.out.println(fuel);
            reader.close();
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }
    public static<K,V> Map<K,V> clone(Map<K,V> original) {
        return original.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue));
    }

    public static class ReactionFactor {
        int _quantity;
        String _symbol;
        ReactionFactor(String symbol, int q) {
            _symbol = symbol;
            _quantity = q;
        }

        public boolean equals(Object b) {
            if ((b instanceof String)) {
                return _symbol.equals(b);
            } else if(b instanceof ReactionFactor) {
                return this._symbol.equals(((ReactionFactor) b)._symbol);
            } else return false;
        }
        public int hashCode() {
            return _symbol.hashCode();
        }
    }
}