import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class day2 {
    public static void main(String[] args) {
        BufferedReader reader;
        int[] initialMemory;
        try {
            reader = new BufferedReader(new FileReader("src/main/res/day2"));
            initialMemory = Arrays.stream(reader.readLine().split(",")).mapToInt(Integer::parseInt).toArray();
            for (int noun = 0; noun < 100; noun++) {
                for (int verb = 0; verb < 100; verb++) {
                    int[] memory = initialMemory.clone();
                    memory[1] = noun;
                    memory[2] = verb;
                    for (int executionPoint = 0; memory[executionPoint] != 99; executionPoint +=4) {
                        int a = memory[memory[executionPoint+1]];
                        int b = memory[memory[executionPoint+2]];
                        if(memory[executionPoint] == 1){
                            memory[memory[executionPoint+3]] = a + b;
                        } else if (memory[executionPoint] == 2) {
                            memory[memory[executionPoint+3]] = a * b;
                        }
                    }
                    if(memory[0] == 19690720) System.out.println(noun + " " + verb);
                }
            }

            reader.close();
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
