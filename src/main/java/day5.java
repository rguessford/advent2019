import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class day5 {
    public static final int OPCODE = 3;
    public static final int PARAMETER_MODE_1 = 2;
    public static final int PARAMETER_MODE_2 = 1;
    public static final int PARAMETER_MODE_3 = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BufferedReader reader;
        int[] memory;
        try {
            reader = new BufferedReader(new FileReader("src/main/res/day5"));
            memory = Arrays.stream(reader.readLine().split(",")).mapToInt(Integer::parseInt).toArray();

            for (int executionPoint = 0; ; ) {
                int[] instruction = new int[]{memory[executionPoint]/10000, (memory[executionPoint]/1000)%10, (memory[executionPoint]/100) % 10, memory[executionPoint]%100};
                if(instruction[OPCODE] == 99) break;
                if(instruction[OPCODE] == 1) {
                    //add
                    int a = 0;
                    int b = 0;
                    if(instruction[PARAMETER_MODE_1] == 0) {
                        a = memory[memory[executionPoint + 1]];
                    } else if (instruction[PARAMETER_MODE_1] == 1){
                        a = memory[executionPoint + 1];
                    }
                    if(instruction[PARAMETER_MODE_2] == 0) {
                        b = memory[memory[executionPoint + 2]];
                    } else if (instruction[PARAMETER_MODE_2] == 1){
                        b = memory[executionPoint + 2];
                    }
                    memory[memory[executionPoint+3]] = a + b;
                    executionPoint +=4;
                } else if (instruction[OPCODE] == 2) {
                    //multiply
                    int a = 0;
                    int b = 0;
                    if(instruction[PARAMETER_MODE_1] == 0) {
                        a = memory[memory[executionPoint + 1]];
                    } else if (instruction[PARAMETER_MODE_1] == 1){
                        a = memory[executionPoint + 1];
                    }
                    if(instruction[PARAMETER_MODE_2] == 0) {
                        b = memory[memory[executionPoint + 2]];
                    } else if (instruction[PARAMETER_MODE_2] == 1){
                        b = memory[executionPoint + 2];
                    }
                    memory[memory[executionPoint+3]] = a * b;
                    executionPoint+=4;
                } else if (instruction[OPCODE] == 3) {
                    //input
                    int in = scanner.nextInt();
                    memory[memory[executionPoint+1]] = in;
                    executionPoint+=2;
                } else if (instruction[OPCODE] == 4) {
                    //output
                    int a = 0;
                    if(instruction[PARAMETER_MODE_1] == 0) {
                        a = memory[memory[executionPoint + 1]];
                    } else if (instruction[PARAMETER_MODE_1] == 1){
                        a = memory[executionPoint + 1];
                    }
                    System.out.println(a);
                    executionPoint+=2;
                } else if (instruction[OPCODE] == 5) {
                    //jump if param 1 != 0
                    int test = 0;
                    if (instruction[PARAMETER_MODE_1] == 0) {
                        test = memory[memory[executionPoint + 1]];
                    } else if (instruction[PARAMETER_MODE_1] == 1) {
                        test = memory[executionPoint + 1];
                    }
                    if(test == 0) {
                        executionPoint += 3;
                    } else if (instruction[PARAMETER_MODE_2] == 0) {
                        executionPoint = memory[memory[executionPoint + 2]];
                    } else if (instruction[PARAMETER_MODE_2] == 1) {
                        executionPoint = memory[executionPoint + 2];
                    }
                } else if (instruction[OPCODE] == 6) {
                    //jump if param 1 == 0
                    int test = 0;
                    if (instruction[PARAMETER_MODE_1] == 0) {
                        test = memory[memory[executionPoint + 1]];
                    } else if (instruction[PARAMETER_MODE_1] == 1) {
                        test = memory[executionPoint + 1];
                    }
                    if(test == 0) {
                        if (instruction[PARAMETER_MODE_2] == 0) {
                            executionPoint = memory[memory[executionPoint + 2]];
                        } else if (instruction[PARAMETER_MODE_2] == 1) {
                            executionPoint = memory[executionPoint + 2];
                        }
                    } else { executionPoint += 3;}
                } else if (instruction[OPCODE] == 7) {
                    int a = 0;
                    int b = 0;
                    if(instruction[PARAMETER_MODE_1] == 0) {
                        a = memory[memory[executionPoint + 1]];
                    } else if (instruction[PARAMETER_MODE_1] == 1){
                        a = memory[executionPoint + 1];
                    }
                    if(instruction[PARAMETER_MODE_2] == 0) {
                        b = memory[memory[executionPoint + 2]];
                    } else if (instruction[PARAMETER_MODE_2] == 1){
                        b = memory[executionPoint + 2];
                    }
                    memory[memory[executionPoint+3]] = a < b ? 1 : 0;
                    executionPoint+=4;
                } else if (instruction[OPCODE] == 8) {
                    int a = 0;
                    int b = 0;
                    if(instruction[PARAMETER_MODE_1] == 0) {
                        a = memory[memory[executionPoint + 1]];
                    } else if (instruction[PARAMETER_MODE_1] == 1){
                        a = memory[executionPoint + 1];
                    }
                    if(instruction[PARAMETER_MODE_2] == 0) {
                        b = memory[memory[executionPoint + 2]];
                    } else if (instruction[PARAMETER_MODE_2] == 1){
                        b = memory[executionPoint + 2];
                    }
                    memory[memory[executionPoint+3]] = a == b ? 1 : 0;
                    executionPoint+=4;
                }
            }

            reader.close();
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
