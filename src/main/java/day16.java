import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class day16 {
    public static void main(String[] args) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("src/main/res/day16"));
            String input = reader.readLine();
            String inputPt2 = String.join("", Collections.nCopies(10000,input));
            int offset = Integer.parseInt(input.substring(0,7));
            inputPt2 = inputPt2.substring(offset);

            //part 1
            int signalSize = input.length();
            ArrayList<Integer> currentSignal = new ArrayList<>(signalSize);
            int[] pattern = new int[]{0,1,0,-1};
            for (int i = 0; i < signalSize; i++) {
                currentSignal.add(Character.getNumericValue(input.charAt(i)));
            }
            ArrayList<Integer> nextSignal = new ArrayList<>();
            int passes = 100;
            for (int i = 0; i < passes; i++) {
                nextSignal = new ArrayList<>();
                for (int outputIndex = 0; outputIndex < currentSignal.size(); outputIndex++) {
                    ArrayList<Integer> products = new ArrayList<>();
                    int factorPatternScale = outputIndex+1;
                    for (int inputIndex = 0; inputIndex < currentSignal.size(); inputIndex++) {
                        int patternFactor = ((inputIndex+1)/factorPatternScale) % 4;
                        products.add(currentSignal.get(inputIndex) * pattern[patternFactor]);
                    }
                    int sum = products.stream().reduce(0, (a, b) -> a + b);
                    nextSignal.add(Math.abs(sum)%10);
                }
                currentSignal = nextSignal;
            }
            for (Integer aNextSignal : nextSignal) {
                System.out.print(aNextSignal);
            }
            System.out.println();

            int signalLength2 = inputPt2.length();
            int[] currentSignalPt2 = new int[signalLength2];
            for (int i = 0; i < signalLength2; i++) {
                currentSignalPt2[i]= Character.getNumericValue(inputPt2.charAt(i));
            }
            passes = 100;

            for (int i = 0; i < passes; i++) {
                for (int j = signalLength2-1; j > 0; j--) {
                    currentSignalPt2[j-1] += currentSignalPt2[j];
                    currentSignalPt2[j-1] = currentSignalPt2[j-1]%10;
                }

            }
            for (Integer aNextSignal : currentSignalPt2) {
                System.out.print(aNextSignal);
            }
            System.out.println();

            reader.close();
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }
}