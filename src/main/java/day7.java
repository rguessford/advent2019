import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class day7 {
    public static final int OPCODE = 3;
    public static final int PARAMETER_MODE_1 = 2;
    public static final int PARAMETER_MODE_2 = 1;
    public static final int PARAMETER_MODE_3 = 0;

    public static final int POSITION_MODE = 0;
    public static final int IMMEDIATE_MODE = 1;

    public static void main(String[] args) {
        BufferedReader reader;
        int[] program;
        try {
            reader = new BufferedReader(new FileReader("src/main/res/day7"));
            program = Arrays.stream(reader.readLine().split(",")).mapToInt(Integer::parseInt).toArray();

            int [] phaseSettings = new int[] {5,6,7,8,9};
            int maxOutput = -1;
            while (true) {
                int i = phaseSettings.length - 1;
                try {
                    while (phaseSettings[i - 1] >= phaseSettings[i]) {
                        i -= 1;
                    }
                } catch (ArrayIndexOutOfBoundsException e){
                    break;
                }
                int j = phaseSettings.length;
                while (phaseSettings[j - 1] <= phaseSettings[i - 1]) {
                    j -= 1;
                }
                //swap the largest digit with the next largest digit
                int temp = phaseSettings[i - 1];
                phaseSettings[i - 1] = phaseSettings[j - 1];
                phaseSettings[j - 1] = temp;

                i++;
                j = phaseSettings.length;
                while (i < j) {
                    temp = phaseSettings[i - 1];
                    phaseSettings[i - 1] = phaseSettings[j - 1];
                    phaseSettings[j - 1] = temp;
                    i++;
                    j--;
                }
                ArrayList<Amplifier> amplifiers = new ArrayList<>();
                for (int phaseSetting : phaseSettings) {
                    amplifiers.add(new Amplifier(Arrays.copyOf(program, program.length), phaseSetting));
                }
                int ampIndex = 0;
                int input = 0;
                while(!amplifiers.get(4).halted){
                    input = amplifiers.get(ampIndex).feedInput(input);
                    ampIndex++;
                    if(ampIndex == 5) ampIndex = 0;
                }
                maxOutput = Math.max(input, maxOutput);
            }

            System.out.println(maxOutput);

            reader.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static class Amplifier{
            int executionPoint = 0;
            int outputRegister;
            boolean halted = false;
            int[] program;
            public Amplifier(int[] program_, int phaseSetting_){
                program = program_;
                feedInput(phaseSetting_);
            }

            public int feedInput(int input){
                boolean paused = false;
                while (true) {
                    int[] instruction = new int[]{program[executionPoint]/10000, (program[executionPoint]/1000)%10, (program[executionPoint]/100) % 10, program[executionPoint]%100};

                    if(instruction[OPCODE] == 1) {
                        //add
                        int a = 0;
                        int b = 0;
                        if(instruction[PARAMETER_MODE_1] == POSITION_MODE) {
                            a = program[program[executionPoint + 1]];
                        } else if (instruction[PARAMETER_MODE_1] == IMMEDIATE_MODE){
                            a = program[executionPoint + 1];
                        }
                        if(instruction[PARAMETER_MODE_2] == POSITION_MODE) {
                            b = program[program[executionPoint + 2]];
                        } else if (instruction[PARAMETER_MODE_2] == IMMEDIATE_MODE){
                            b = program[executionPoint + 2];
                        }
                        program[program[executionPoint+3]] = a + b;
                        executionPoint +=4;
                    } else if (instruction[OPCODE] == 2) {
                        //multiply
                        int a = 0;
                        int b = 0;
                        if(instruction[PARAMETER_MODE_1] == POSITION_MODE) {
                            a = program[program[executionPoint + 1]];
                        } else if (instruction[PARAMETER_MODE_1] == IMMEDIATE_MODE){
                            a = program[executionPoint + 1];
                        }
                        if(instruction[PARAMETER_MODE_2] == POSITION_MODE) {
                            b = program[program[executionPoint + 2]];
                        } else if (instruction[PARAMETER_MODE_2] == IMMEDIATE_MODE){
                            b = program[executionPoint + 2];
                        }
                        program[program[executionPoint+3]] = a * b;
                        executionPoint+=4;
                    } else if (instruction[OPCODE] == 3) {
                        //input
                        if(paused){
                            break;
                        }
                        program[program[executionPoint+1]] = input;
                        executionPoint+=2;
                        paused=true;
                    } else if (instruction[OPCODE] == 4) {
                        //output
                        int a = 0;
                        if(instruction[PARAMETER_MODE_1] == POSITION_MODE) {
                            a = program[program[executionPoint + 1]];
                        } else if (instruction[PARAMETER_MODE_1] == IMMEDIATE_MODE){
                            a = program[executionPoint + 1];
                        }
                        outputRegister = a;
                        paused = true;
                        executionPoint +=2;
                    } else if (instruction[OPCODE] == 5) {
                        //jump if param 1 != 0
                        int test = 0;
                        if (instruction[PARAMETER_MODE_1] == POSITION_MODE) {
                            test = program[program[executionPoint + 1]];
                        } else if (instruction[PARAMETER_MODE_1] == IMMEDIATE_MODE) {
                            test = program[executionPoint + 1];
                        }
                        if(test == 0) {
                            executionPoint += 3;
                        } else if (instruction[PARAMETER_MODE_2] == POSITION_MODE) {
                            executionPoint = program[program[executionPoint + 2]];
                        } else if (instruction[PARAMETER_MODE_2] == IMMEDIATE_MODE) {
                            executionPoint = program[executionPoint + 2];
                        }
                    } else if (instruction[OPCODE] == 6) {
                        //jump if param 1 == 0
                        int test = 0;
                        if (instruction[PARAMETER_MODE_1] == POSITION_MODE) {
                            test = program[program[executionPoint + 1]];
                        } else if (instruction[PARAMETER_MODE_1] == IMMEDIATE_MODE) {
                            test = program[executionPoint + 1];
                        }
                        if(test == 0) {
                            if (instruction[PARAMETER_MODE_2] == POSITION_MODE) {
                                executionPoint = program[program[executionPoint + 2]];
                            } else if (instruction[PARAMETER_MODE_2] == IMMEDIATE_MODE) {
                                executionPoint = program[executionPoint + 2];
                            }
                        } else { executionPoint += 3;}
                    } else if (instruction[OPCODE] == 7) {
                        int a = 0;
                        int b = 0;
                        if(instruction[PARAMETER_MODE_1] == POSITION_MODE) {
                            a = program[program[executionPoint + 1]];
                        } else if (instruction[PARAMETER_MODE_1] == IMMEDIATE_MODE){
                            a = program[executionPoint + 1];
                        }
                        if(instruction[PARAMETER_MODE_2] == POSITION_MODE) {
                            b = program[program[executionPoint + 2]];
                        } else if (instruction[PARAMETER_MODE_2] == IMMEDIATE_MODE){
                            b = program[executionPoint + 2];
                        }
                        program[program[executionPoint+3]] = a < b ? 1 : 0;
                        executionPoint+=4;
                    } else if (instruction[OPCODE] == 8) {
                        int a = 0;
                        int b = 0;
                        if(instruction[PARAMETER_MODE_1] == POSITION_MODE) {
                            a = program[program[executionPoint + 1]];
                        } else if (instruction[PARAMETER_MODE_1] == IMMEDIATE_MODE){
                            a = program[executionPoint + 1];
                        }
                        if(instruction[PARAMETER_MODE_2] == POSITION_MODE) {
                            b = program[program[executionPoint + 2]];
                        } else if (instruction[PARAMETER_MODE_2] == IMMEDIATE_MODE){
                            b = program[executionPoint + 2];
                        }
                        program[program[executionPoint+3]] = a == b ? 1 : 0;
                        executionPoint+=4;
                    } else if(instruction[OPCODE] == 99){
                        halted = true;
                        break;
                    }
                }
                return outputRegister;
            }
    }

}
