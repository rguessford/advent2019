import com.sun.deploy.security.SandboxSecurity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class day6 {

    public static void main(String[] args) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("src/main/res/day6"));
            HashMap<String, HashSet<String>> systems = new HashMap<>();
            while (reader.ready()){
                String[] system = reader.readLine().split("\\)");
                HashSet<String> orbits = systems.getOrDefault(system[0], new HashSet<String>());
                orbits.add(system[1]);
                systems.put(system[0], orbits);
                orbits = systems.getOrDefault(system[1], new HashSet<String>());
                systems.put(system[1], orbits);
            }
            HashMap<String, Integer> orbitCounts = new HashMap<>();
            String root = null;

            for(Map.Entry<String, HashSet<String>> entry : systems.entrySet()){
                boolean isOrbiting = false;
                for(Map.Entry<String, HashSet<String>> entry2 : systems.entrySet()){
                    if(entry2.getValue().contains(entry.getKey())){
                        isOrbiting = true;
                        break;
                    }
                }
                if(!isOrbiting) root = entry.getKey();
            }
            HashSet<String> YOUpath = new HashSet<>();
            HashSet<String> SANpath = new HashSet<>();

            String node = "YOU";
            while(!node.equals("COM")){
                for(Map.Entry<String, HashSet<String>> entry : systems.entrySet()){
                    if(entry.getValue().contains(node)){
                        YOUpath.add(entry.getKey());
                        node = entry.getKey();
                    }
                }
            }

            node = "SAN";
            while(!node.equals("COM")){
                for(Map.Entry<String, HashSet<String>> entry : systems.entrySet()){
                    if(entry.getValue().contains(node)){
                        SANpath.add(entry.getKey());
                        node = entry.getKey();
                    }
                }
            }
            // PART 1
            orbitCounts.put(root, countChildren(systems, orbitCounts, root));
            int accumulator = 0;
            for(Integer count: orbitCounts.values()){
                accumulator += count;
            }
            System.out.println(accumulator);

            // PART 2
            Iterator it = YOUpath.iterator();
            while(it.hasNext()){
                String planet = (String)it.next();
                if(SANpath.contains(planet)){
                    it.remove();
                    SANpath.remove(planet);
                }
            }

            System.out.println(SANpath.size() + YOUpath.size());

            reader.close();
        } catch(IOException e) {
            java.lang.System.err.println(e.getMessage());
        }
    }

    public static int countChildren(HashMap<String, HashSet<String>> systems, HashMap<String, Integer> orbitCounts, String node) {
        HashSet<String> satellites = systems.get(node);
        if (satellites.size() == 0){
            orbitCounts.put(node, 0);
            return 0;
        }
        for(String satellite : satellites){
            int count = orbitCounts.getOrDefault(node, 0);
            count += countChildren(systems,orbitCounts, satellite)+1;
            orbitCounts.put(node, count);
        }
        return orbitCounts.get(node);
    }
}
