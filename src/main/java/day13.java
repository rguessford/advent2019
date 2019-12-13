import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class day13 {
    public static final int OPCODE = 3;
    public static final int PARAMETER_MODE_1 = 2;
    public static final int PARAMETER_MODE_2 = 1;
    public static final int PARAMETER_MODE_3 = 0;

    public static final int POSITION_MODE = 0;
    public static final int IMMEDIATE_MODE = 1;
    public static final int RELATIVE_MODE = 2;

    enum OutputMode{
        X, Y, TILE
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BufferedReader reader;
        long[] memory;
        long[][] screen = new long[22][40];
        long xRegister = -1;
        long yRegister = -1;
        OutputMode mode = OutputMode.X;
        long score = 0;
        try {
            reader = new BufferedReader(new FileReader("src/main/res/day13"));
            memory = Arrays.stream(reader.readLine().split(",")).mapToLong(Long::parseLong).toArray();
            memory = Arrays.copyOf(memory, memory.length +1000);
            long relativeBase=0;

            for (int executionPoint = 0; ; ) {
                long[] instruction = new long[]{memory[executionPoint]/10000, (memory[executionPoint]/1000)%10, (memory[executionPoint]/100) % 10, memory[executionPoint]%100};

                if(instruction[OPCODE] == 1) {
                    //add
                    long a = 0;
                    long b = 0;
                    if(instruction[PARAMETER_MODE_1] == POSITION_MODE) {
                        a = memory[(int) memory[executionPoint + 1]];
                    } else if (instruction[PARAMETER_MODE_1] == IMMEDIATE_MODE){
                        a = memory[executionPoint + 1];
                    } else if (instruction[PARAMETER_MODE_1] == RELATIVE_MODE) {
                        a = memory[(int) memory[executionPoint + 1] + (int) relativeBase];
                    }
                    if(instruction[PARAMETER_MODE_2] == POSITION_MODE) {
                        b = memory[(int) memory[executionPoint + 2]];
                    } else if (instruction[PARAMETER_MODE_2] == IMMEDIATE_MODE){
                        b = memory[executionPoint + 2];
                    } else if (instruction[PARAMETER_MODE_2] == RELATIVE_MODE) {
                        b = memory[(int) memory[executionPoint + 2] + (int) relativeBase];
                    }
                    if(instruction[PARAMETER_MODE_3] == POSITION_MODE) {
                        memory[(int) memory[executionPoint + 3]] = a + b;
                    } else if (instruction[PARAMETER_MODE_3] == RELATIVE_MODE) {
                        memory[(int) memory[executionPoint + 3] + (int) relativeBase] = a + b;
                    }
                    executionPoint +=4;
                } else if (instruction[OPCODE] == 2) {
                    //multiply
                    long a = 0;
                    long b = 0;
                    if(instruction[PARAMETER_MODE_1] == POSITION_MODE) {
                        a = memory[(int) memory[executionPoint + 1]];
                    } else if (instruction[PARAMETER_MODE_1] == IMMEDIATE_MODE){
                        a = memory[executionPoint + 1];
                    } else if (instruction[PARAMETER_MODE_1] == RELATIVE_MODE) {
                        a = memory[(int) memory[executionPoint + 1] + (int) relativeBase];
                    }
                    if(instruction[PARAMETER_MODE_2] == POSITION_MODE) {
                        b = memory[(int) memory[executionPoint + 2]];
                    } else if (instruction[PARAMETER_MODE_2] == IMMEDIATE_MODE){
                        b = memory[executionPoint + 2];
                    } else if (instruction[PARAMETER_MODE_2] == RELATIVE_MODE) {
                        b = memory[(int) memory[executionPoint + 2] + (int) relativeBase];
                    }
                    if(instruction[PARAMETER_MODE_3] == POSITION_MODE) {
                        memory[(int) memory[executionPoint + 3]] = a * b;
                    } else if (instruction[PARAMETER_MODE_3] == RELATIVE_MODE) {
                        memory[(int) memory[executionPoint + 3] + (int) relativeBase] = a * b;
                    }
                    executionPoint+=4;
                } else if (instruction[OPCODE] == 3) {
                    //input
                    int paddlex = -1;
                    int ballx = -1;
                    int blocksleft = 0;
                    for (int i = 0; i < screen.length; i++) {
                        for (int j = 0; j < screen[0].length; j++) {
                            if (screen[i][j] == 2) {
                                blocksleft++;
                            }
                            if (screen[i][j] == 3) {
                                paddlex = j;
                            }
                            if (screen[i][j] == 4) {
                                ballx = j;
                            }
                        }
                    }
                    System.out.println("blocks" + blocksleft);
                    System.out.println(score);
                    long in = 0;
                    if(ballx < paddlex){
                        in = -1;
                    }
                    if(ballx > paddlex){
                        in = 1;
                    }


                    if(instruction[PARAMETER_MODE_1] == POSITION_MODE) {
                        memory[(int) memory[executionPoint + 1]] = in;
                    } else if (instruction[PARAMETER_MODE_1] == RELATIVE_MODE) {
                        memory[(int) memory[executionPoint + 1] + (int) relativeBase] = in;
                    }
                    executionPoint+=2;
                } else if (instruction[OPCODE] == 4) {
                    //output
                    long a = 0;
                    if(instruction[PARAMETER_MODE_1] == POSITION_MODE) {
                        a = memory[(int) memory[executionPoint + 1]];
                    } else if (instruction[PARAMETER_MODE_1] == IMMEDIATE_MODE){
                        a = memory[executionPoint + 1];
                    } else if (instruction[PARAMETER_MODE_1] == RELATIVE_MODE) {
                        a = memory[(int) memory[executionPoint + 1] + (int) relativeBase];
                    }
                    switch (mode){
                        case X:
                            xRegister = a;
                            mode = OutputMode.Y;
                            break;
                        case Y:
                            yRegister = a;
                            mode = OutputMode.TILE;
                            break;
                        case TILE:
                            if(xRegister == -1 && yRegister == 0){
                                score = a;
                            } else {
                                screen[(int) yRegister][(int) xRegister] = a;
                            }
                            mode = OutputMode.X;
                            break;
                    }
                    executionPoint+=2;
                } else if (instruction[OPCODE] == 5) {
                    //jump if param 1 != 0
                    long test = 0;
                    if (instruction[PARAMETER_MODE_1] == POSITION_MODE) {
                        test = memory[(int) memory[executionPoint + 1]];
                    } else if (instruction[PARAMETER_MODE_1] == IMMEDIATE_MODE) {
                        test = memory[executionPoint + 1];
                    } else if (instruction[PARAMETER_MODE_1] == RELATIVE_MODE) {
                        test = memory[(int) memory[executionPoint + 1] + (int) relativeBase];
                    }
                    if(test == 0) {
                        executionPoint += 3;
                    } else if (instruction[PARAMETER_MODE_2] == POSITION_MODE) {
                        executionPoint = (int) memory[(int) memory[executionPoint + 2]];
                    } else if (instruction[PARAMETER_MODE_2] == IMMEDIATE_MODE) {
                        executionPoint = (int) memory[executionPoint + 2];
                    } else if (instruction[PARAMETER_MODE_2] == RELATIVE_MODE) {
                        executionPoint = (int) memory[(int) memory[executionPoint + 2] + (int) relativeBase];
                    }
                } else if (instruction[OPCODE] == 6) {
                    //jump if param 1 == 0
                    long test = 0;
                    if (instruction[PARAMETER_MODE_1] == POSITION_MODE) {
                        test = memory[(int) memory[executionPoint + 1]];
                    } else if (instruction[PARAMETER_MODE_1] == IMMEDIATE_MODE) {
                        test = memory[executionPoint + 1];
                    }  else if (instruction[PARAMETER_MODE_1] == RELATIVE_MODE) {
                        test = memory[(int) memory[executionPoint + 1] + (int) relativeBase];
                    }
                    if(test == 0) {
                        if (instruction[PARAMETER_MODE_2] == POSITION_MODE) {
                            executionPoint = (int) memory[(int) memory[executionPoint + 2]];
                        } else if (instruction[PARAMETER_MODE_2] == IMMEDIATE_MODE) {
                            executionPoint = (int) memory[executionPoint + 2];
                        } else if (instruction[PARAMETER_MODE_2] == RELATIVE_MODE) {
                            executionPoint = (int) memory[(int) memory[executionPoint + 2] + (int) relativeBase];
                        }
                    } else { executionPoint += 3;}
                } else if (instruction[OPCODE] == 7) {
                    //less than
                    long a = 0;
                    long b = 0;
                    if(instruction[PARAMETER_MODE_1] == POSITION_MODE) {
                        a = memory[(int) memory[executionPoint + 1]];
                    } else if (instruction[PARAMETER_MODE_1] == IMMEDIATE_MODE){
                        a = memory[executionPoint + 1];
                    } else if (instruction[PARAMETER_MODE_1] == RELATIVE_MODE) {
                        a = memory[(int) memory[executionPoint + 1] + (int) relativeBase];
                    }
                    if(instruction[PARAMETER_MODE_2] == POSITION_MODE) {
                        b = memory[(int) memory[executionPoint + 2]];
                    } else if (instruction[PARAMETER_MODE_2] == IMMEDIATE_MODE){
                        b = memory[executionPoint + 2];
                    } else if (instruction[PARAMETER_MODE_2] == RELATIVE_MODE) {
                        b = memory[(int) memory[executionPoint + 2] + (int) relativeBase];
                    }
                    if(instruction[PARAMETER_MODE_3] == POSITION_MODE) {
                        memory[(int) memory[executionPoint + 3]] = a < b ? 1 : 0;
                    } else if (instruction[PARAMETER_MODE_3] == RELATIVE_MODE) {
                        memory[(int) memory[executionPoint + 3] + (int) relativeBase] = a < b ? 1 : 0;
                    }
                    executionPoint+=4;
                } else if (instruction[OPCODE] == 8) {
                    long a = 0;
                    long b = 0;
                    if(instruction[PARAMETER_MODE_1] == POSITION_MODE) {
                        a = memory[(int) memory[executionPoint + 1]];
                    } else if (instruction[PARAMETER_MODE_1] == IMMEDIATE_MODE){
                        a = memory[executionPoint + 1];
                    } else if (instruction[PARAMETER_MODE_1] == RELATIVE_MODE) {
                        a = memory[(int) memory[executionPoint + 1] + (int) relativeBase];
                    }
                    if(instruction[PARAMETER_MODE_2] == POSITION_MODE) {
                        b = memory[(int) memory[executionPoint + 2]];
                    } else if (instruction[PARAMETER_MODE_2] == IMMEDIATE_MODE){
                        b = memory[executionPoint + 2];
                    } else if (instruction[PARAMETER_MODE_2] == RELATIVE_MODE) {
                        b = memory[(int) memory[executionPoint + 2] + (int) relativeBase];
                    }
                    if(instruction[PARAMETER_MODE_3] == POSITION_MODE) {
                        memory[(int) memory[executionPoint + 3]] = a == b ? 1 : 0;
                    } else if (instruction[PARAMETER_MODE_3] == RELATIVE_MODE) {
                        memory[(int) memory[executionPoint + 3] + (int) relativeBase] = a == b ? 1 : 0;
                    }
                    executionPoint+=4;
                } else if (instruction[OPCODE] == 9) {
                    //adjust relative base
                    if(instruction[PARAMETER_MODE_1] == POSITION_MODE) {
                        relativeBase += memory[(int) memory[executionPoint + 1]];
                    } else if (instruction[PARAMETER_MODE_1] == IMMEDIATE_MODE){
                        relativeBase += memory[executionPoint + 1];
                    } else if (instruction[PARAMETER_MODE_1] == RELATIVE_MODE){
                        relativeBase +=memory[(int) memory[executionPoint + 1] + (int) relativeBase];
                    }
                    executionPoint +=2;

                } else if (instruction[OPCODE] == 99) break;
            }
//            //part1
//            int counter = 0;
//            for (int i = 0; i < screen.length; i++) {
//                for (int j = 0; j < screen.length; j++) {
//                    if(screen[i][j] == 2) counter++;
//                }
//            }
//            System.out.println(counter);
            System.out.println(score);
            reader.close();
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }
}