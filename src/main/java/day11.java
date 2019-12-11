import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class day11 {
    public static final int OPCODE = 3;
    public static final int PARAMETER_MODE_1 = 2;
    public static final int PARAMETER_MODE_2 = 1;
    public static final int PARAMETER_MODE_3 = 0;

    public static final int POSITION_MODE = 0;
    public static final int IMMEDIATE_MODE = 1;
    public static final int RELATIVE_MODE = 2;

    enum Direction {
        UP,DOWN,LEFT,RIGHT
    }
    enum OutputMode {
        PAINT,MOVE
    }
    public static void main(String[] args) {
        BufferedReader reader;
        long[] memory;
        Integer[][] panel = new Integer[100][];
        for (int i = 0; i < panel.length ; i++) {
            panel[i] = new Integer[100];
        }
        panel[50][50] = 1;
        try {
            reader = new BufferedReader(new FileReader("src/main/res/day11"));
            memory = Arrays.stream(reader.readLine().split(",")).mapToLong(Long::parseLong).toArray();
            memory = Arrays.copyOf(memory, memory.length + 1000);
            PaintBot bot = new PaintBot(memory, 50,50);
            bot.run(panel);
            int paintedPanels = 0;
            for (int i = 0; i < panel.length; i++) {
                for (int j = 0; j <panel.length; j++) {
                    if (panel[j][i] != null) paintedPanels++;
                    System.out.print(panel[j][i] == null ? 0 : panel[j][i]);
                }
                System.out.println();
            }
            System.out.println(paintedPanels);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static class PaintBot{
        long[] memory;
        int xpos;
        int ypos;
        OutputMode mode;
        Direction direction;

        PaintBot(long[] program, int x, int y){
            memory = program;
            xpos = x;
            ypos = y;
            mode = OutputMode.PAINT;
            direction = Direction.UP;
        }

        public void run(Integer[][] panel){
            long relativeBase = 0;
                for (int executionPoint = 0; ; ) {
                long[] instruction = new long[]{memory[executionPoint] / 10000, (memory[executionPoint] / 1000) % 10, (memory[executionPoint] / 100) % 10, memory[executionPoint] % 100};

                if (instruction[OPCODE] == 1) {
                    //add
                    long a = 0;
                    long b = 0;
                    if (instruction[PARAMETER_MODE_1] == POSITION_MODE) {
                        a = memory[(int) memory[executionPoint + 1]];
                    } else if (instruction[PARAMETER_MODE_1] == IMMEDIATE_MODE) {
                        a = memory[executionPoint + 1];
                    } else if (instruction[PARAMETER_MODE_1] == RELATIVE_MODE) {
                        a = memory[(int) memory[executionPoint + 1] + (int) relativeBase];
                    }
                    if (instruction[PARAMETER_MODE_2] == POSITION_MODE) {
                        b = memory[(int) memory[executionPoint + 2]];
                    } else if (instruction[PARAMETER_MODE_2] == IMMEDIATE_MODE) {
                        b = memory[executionPoint + 2];
                    } else if (instruction[PARAMETER_MODE_2] == RELATIVE_MODE) {
                        b = memory[(int) memory[executionPoint + 2] + (int) relativeBase];
                    }
                    if (instruction[PARAMETER_MODE_3] == POSITION_MODE) {
                        memory[(int) memory[executionPoint + 3]] = a + b;
                    } else if (instruction[PARAMETER_MODE_3] == RELATIVE_MODE) {
                        memory[(int) memory[executionPoint + 3] + (int) relativeBase] = a + b;
                    }
                    executionPoint += 4;
                } else if (instruction[OPCODE] == 2) {
                    //multiply
                    long a = 0;
                    long b = 0;
                    if (instruction[PARAMETER_MODE_1] == POSITION_MODE) {
                        a = memory[(int) memory[executionPoint + 1]];
                    } else if (instruction[PARAMETER_MODE_1] == IMMEDIATE_MODE) {
                        a = memory[executionPoint + 1];
                    } else if (instruction[PARAMETER_MODE_1] == RELATIVE_MODE) {
                        a = memory[(int) memory[executionPoint + 1] + (int) relativeBase];
                    }
                    if (instruction[PARAMETER_MODE_2] == POSITION_MODE) {
                        b = memory[(int) memory[executionPoint + 2]];
                    } else if (instruction[PARAMETER_MODE_2] == IMMEDIATE_MODE) {
                        b = memory[executionPoint + 2];
                    } else if (instruction[PARAMETER_MODE_2] == RELATIVE_MODE) {
                        b = memory[(int) memory[executionPoint + 2] + (int) relativeBase];
                    }
                    if (instruction[PARAMETER_MODE_3] == POSITION_MODE) {
                        memory[(int) memory[executionPoint + 3]] = a * b;
                    } else if (instruction[PARAMETER_MODE_3] == RELATIVE_MODE) {
                        memory[(int) memory[executionPoint + 3] + (int) relativeBase] = a * b;
                    }
                    executionPoint += 4;
                } else if (instruction[OPCODE] == 3) {
                    //input
                    long in;
                    if(panel[ypos][xpos]==null){
                        in = 0;
                    } else{
                        in = panel[ypos][xpos];
                    }

                    if (instruction[PARAMETER_MODE_1] == POSITION_MODE) {
                        memory[(int) memory[executionPoint + 1]] = in;
                    } else if (instruction[PARAMETER_MODE_1] == RELATIVE_MODE) {
                        memory[(int) memory[executionPoint + 1] + (int) relativeBase] = in;
                    }
                    executionPoint += 2;
                } else if (instruction[OPCODE] == 4) {
                    //output
                    Long a = 0L;
                    if (instruction[PARAMETER_MODE_1] == POSITION_MODE) {
                        a = memory[(int) memory[executionPoint + 1]];
                    } else if (instruction[PARAMETER_MODE_1] == IMMEDIATE_MODE) {
                        a = memory[executionPoint + 1];
                    } else if (instruction[PARAMETER_MODE_1] == RELATIVE_MODE) {
                        a = memory[(int) memory[executionPoint + 1] + (int) relativeBase];
                    }
                    if(mode == OutputMode.PAINT){
                        panel[ypos][xpos] = a.intValue();
                        mode = OutputMode.MOVE;
                    } else {
                        //0 = left 1 == right
                        switch (direction){
                            case UP:
                                if(a == 0){
                                    direction = Direction.LEFT;
                                    xpos--;
                                } else{
                                    direction = Direction.RIGHT;
                                    xpos++;
                                }
                                break;
                            case DOWN:
                                if(a == 0){
                                    direction = Direction.RIGHT;
                                    xpos++;
                                } else {
                                    direction = Direction.LEFT;
                                    xpos--;
                                }
                                break;
                            case LEFT:
                                if(a == 0){
                                    direction = Direction.DOWN;
                                    ypos--;
                                } else {
                                    direction = Direction.UP;
                                    ypos++;
                                }
                                break;
                            case RIGHT:
                                if(a == 0){
                                    direction = Direction.UP;
                                    ypos++;
                                } else {
                                    direction = Direction.DOWN;
                                    ypos--;
                                }
                                break;
                        }
                        mode = OutputMode.PAINT;
                    }
                    executionPoint += 2;
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
                    if (test == 0) {
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
                    } else if (instruction[PARAMETER_MODE_1] == RELATIVE_MODE) {
                        test = memory[(int) memory[executionPoint + 1] + (int) relativeBase];
                    }
                    if (test == 0) {
                        if (instruction[PARAMETER_MODE_2] == POSITION_MODE) {
                            executionPoint = (int) memory[(int) memory[executionPoint + 2]];
                        } else if (instruction[PARAMETER_MODE_2] == IMMEDIATE_MODE) {
                            executionPoint = (int) memory[executionPoint + 2];
                        } else if (instruction[PARAMETER_MODE_2] == RELATIVE_MODE) {
                            executionPoint = (int) memory[(int) memory[executionPoint + 2] + (int) relativeBase];
                        }
                    } else {
                        executionPoint += 3;
                    }
                } else if (instruction[OPCODE] == 7) {
                    //less than
                    long a = 0;
                    long b = 0;
                    if (instruction[PARAMETER_MODE_1] == POSITION_MODE) {
                        a = memory[(int) memory[executionPoint + 1]];
                    } else if (instruction[PARAMETER_MODE_1] == IMMEDIATE_MODE) {
                        a = memory[executionPoint + 1];
                    } else if (instruction[PARAMETER_MODE_1] == RELATIVE_MODE) {
                        a = memory[(int) memory[executionPoint + 1] + (int) relativeBase];
                    }
                    if (instruction[PARAMETER_MODE_2] == POSITION_MODE) {
                        b = memory[(int) memory[executionPoint + 2]];
                    } else if (instruction[PARAMETER_MODE_2] == IMMEDIATE_MODE) {
                        b = memory[executionPoint + 2];
                    } else if (instruction[PARAMETER_MODE_2] == RELATIVE_MODE) {
                        b = memory[(int) memory[executionPoint + 2] + (int) relativeBase];
                    }
                    if (instruction[PARAMETER_MODE_3] == POSITION_MODE) {
                        memory[(int) memory[executionPoint + 3]] = a < b ? 1 : 0;
                    } else if (instruction[PARAMETER_MODE_3] == RELATIVE_MODE) {
                        memory[(int) memory[executionPoint + 3] + (int) relativeBase] = a < b ? 1 : 0;
                    }
                    executionPoint += 4;
                } else if (instruction[OPCODE] == 8) {
                    long a = 0;
                    long b = 0;
                    if (instruction[PARAMETER_MODE_1] == POSITION_MODE) {
                        a = memory[(int) memory[executionPoint + 1]];
                    } else if (instruction[PARAMETER_MODE_1] == IMMEDIATE_MODE) {
                        a = memory[executionPoint + 1];
                    } else if (instruction[PARAMETER_MODE_1] == RELATIVE_MODE) {
                        a = memory[(int) memory[executionPoint + 1] + (int) relativeBase];
                    }
                    if (instruction[PARAMETER_MODE_2] == POSITION_MODE) {
                        b = memory[(int) memory[executionPoint + 2]];
                    } else if (instruction[PARAMETER_MODE_2] == IMMEDIATE_MODE) {
                        b = memory[executionPoint + 2];
                    } else if (instruction[PARAMETER_MODE_2] == RELATIVE_MODE) {
                        b = memory[(int) memory[executionPoint + 2] + (int) relativeBase];
                    }
                    if (instruction[PARAMETER_MODE_3] == POSITION_MODE) {
                        memory[(int) memory[executionPoint + 3]] = a == b ? 1 : 0;
                    } else if (instruction[PARAMETER_MODE_3] == RELATIVE_MODE) {
                        memory[(int) memory[executionPoint + 3] + (int) relativeBase] = a == b ? 1 : 0;
                    }
                    executionPoint += 4;
                } else if (instruction[OPCODE] == 9) {
                    //adjust relative base
                    if (instruction[PARAMETER_MODE_1] == POSITION_MODE) {
                        relativeBase += memory[(int) memory[executionPoint + 1]];
                    } else if (instruction[PARAMETER_MODE_1] == IMMEDIATE_MODE) {
                        relativeBase += memory[executionPoint + 1];
                    } else if (instruction[PARAMETER_MODE_1] == RELATIVE_MODE) {
                        relativeBase += memory[(int) memory[executionPoint + 1] + (int) relativeBase];
                    }
                    executionPoint += 2;

                } else if (instruction[OPCODE] == 99) break;
            }
        }
    }
}
