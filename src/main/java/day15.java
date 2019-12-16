import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class day15 {
    private static final int OPCODE = 3;
    private static final int PARAMETER_MODE_1 = 2;
    private static final int PARAMETER_MODE_2 = 1;
    private static final int PARAMETER_MODE_3 = 0;

    private static final int POSITION_MODE = 0;
    private static final int IMMEDIATE_MODE = 1;
    private static final int RELATIVE_MODE = 2;

    private static final int STATUS_WALL = 0;
    private static final int STATUS_MOVED = 1;
    private static final int STATUS_FINISH = 2;

    private static final int MAP_UNEXPLORED = 0;
    private static final int MAP_WALL = 1;
    private static final int MAP_FLOOR = 2;
    private static final int MAP_FINISH = 3;
    private static final int MAP_OXYGENATED = 4;

    enum Direction {
        NORTH(1, 2), SOUTH(2, 1), WEST(3, 4), EAST(4, 3);

        int dirCode;
        int oppositeDirCode;

        Direction(int dirCode_, int oppositeDirCode_) {
            dirCode = dirCode_;
            oppositeDirCode = oppositeDirCode_;
        }
    }

    public static void main(String[] args) {
        BufferedReader reader;
        long[] memory;
        try {
            reader = new BufferedReader(new FileReader("src/main/res/day15"));
            memory = Arrays.stream(reader.readLine().split(",")).mapToLong(Long::parseLong).toArray();
            memory = Arrays.copyOf(memory, memory.length + 1000);

            IntcodeRobot repairBot = new IntcodeRobot(memory);
            int[][] map = new int[41][41];
            int posx = 21;
            int posy = 21;
            map[20][20] = MAP_FLOOR;
            int statusCode = 0;

            ArrayList<Direction> pathStack = new ArrayList<>();

            while (true) {
                if (map[posy-1][posx] == MAP_UNEXPLORED) {
                    statusCode = repairBot.feedInput(Direction.NORTH.dirCode);

                    if (statusCode == STATUS_WALL) {
                        map[posy - 1][posx] = MAP_WALL;
                    } else if (statusCode == STATUS_MOVED) {
                        map[posy - 1][posx] = MAP_FLOOR;
                        posy -= 1;
                        pathStack.add(0, Direction.NORTH);
                    } else if (statusCode == STATUS_FINISH) {
                        map[posy - 1][posx] = MAP_FINISH;
                        posy -= 1;
                        pathStack.add(0, Direction.NORTH);
                        System.out.println(pathStack.size());
                    }
                } else if (map[posy][posx + 1] == MAP_UNEXPLORED) {
                    statusCode = repairBot.feedInput(Direction.EAST.dirCode);

                    if (statusCode == STATUS_WALL) {
                        map[posy][posx + 1] = MAP_WALL;
                    } else if (statusCode == STATUS_MOVED) {
                        map[posy][posx + 1] = MAP_FLOOR;
                        posx += 1;
                        pathStack.add(0, Direction.EAST);
                    } else if (statusCode == STATUS_FINISH) {
                        map[posy][posx + 1] = MAP_FINISH;
                        posx += 1;
                        pathStack.add(0, Direction.EAST);
                        System.out.println(pathStack.size());
                    }
                } else if (map[posy + 1][posx] == MAP_UNEXPLORED) {
                    statusCode = repairBot.feedInput(Direction.SOUTH.dirCode);

                    if (statusCode == STATUS_WALL) {
                        map[posy + 1][posx] = MAP_WALL;
                    } else if (statusCode == STATUS_MOVED) {
                        map[posy + 1][posx] = MAP_FLOOR;
                        posy += 1;
                        pathStack.add(0, Direction.SOUTH);
                    } else if (statusCode == STATUS_FINISH) {
                        map[posy + 1][posx] = MAP_FINISH;
                        posy += 1;
                        pathStack.add(0, Direction.SOUTH);
                        System.out.println(pathStack.size());
                    }
                } else if (map[posy][posx - 1] == MAP_UNEXPLORED) {
                    statusCode = repairBot.feedInput(Direction.WEST.dirCode);

                    if (statusCode == STATUS_WALL) {
                        map[posy][posx - 1] = MAP_WALL;
                    } else if (statusCode == STATUS_MOVED) {
                        map[posy][posx - 1] = MAP_FLOOR;
                        posx -= 1;
                        pathStack.add(0, Direction.WEST);
                    } else if (statusCode == STATUS_FINISH) {
                        map[posy][posx - 1] = MAP_FINISH;
                        posx -= 1;
                        pathStack.add(0, Direction.WEST);
                        System.out.println(pathStack.size());
                    }
                } else {
                    try {
                        Direction lastMove = pathStack.remove(0);
                        repairBot.feedInput(lastMove.oppositeDirCode);
                        switch (lastMove){
                            case NORTH:
                                posy +=1;
                                break;
                            case SOUTH:
                                posy -=1;
                                break;
                            case WEST:
                                posx +=1;
                                break;
                            case EAST:
                                posx -=1;
                                break;
                        }
                    } catch (Exception e){
                        break;
                    }
                }
            }

            HashSet<Coord> toFill = new HashSet<>();
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map.length; j++) {
                    if (map[i][j] == 3){
                        toFill.add(new Coord(j, i));
                    }
                }
            }

            HashSet<Coord> fromFill;
            int minutes = -1;
            while (toFill.size() > 0){
                fromFill = toFill;
                toFill = new HashSet<>();
                for (Coord from: fromFill) {
                    map[from._y][from._x] = MAP_OXYGENATED;
                    if(map[from._y-1][from._x] == 2){
                        toFill.add(new Coord(from._x,from._y-1));
                    }
                    if(map[from._y][from._x+1] == 2){
                        toFill.add(new Coord(from._x+1,from._y));
                    }
                    if(map[from._y+1][from._x] == 2){
                        toFill.add(new Coord(from._x,from._y+1));
                    }
                    if(map[from._y][from._x-1] == 2){
                        toFill.add(new Coord(from._x-1,from._y));
                    }
                }
                minutes++;
            }
            System.out.println(minutes);
            reader.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static class IntcodeRobot {
        long[] memory;
        long outputRegister;
        int executionPoint = 0;
        long relativeBase = 0;
        boolean halted = false;

        IntcodeRobot(long[] program) {
            memory = program;
        }

        public int feedInput(int input) {
            boolean paused = false;
            while (true) {
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
                    if (paused) break;
                    if (instruction[PARAMETER_MODE_1] == POSITION_MODE) {
                        memory[(int) memory[executionPoint + 1]] = input;
                    } else if (instruction[PARAMETER_MODE_1] == RELATIVE_MODE) {
                        memory[(int) memory[executionPoint + 1] + (int) relativeBase] = input;
                    }
                    executionPoint += 2;
                    paused = true;
                } else if (instruction[OPCODE] == 4) {
                    //output
                    long a = 0;
                    if (instruction[PARAMETER_MODE_1] == POSITION_MODE) {
                        a = memory[(int) memory[executionPoint + 1]];
                    } else if (instruction[PARAMETER_MODE_1] == IMMEDIATE_MODE) {
                        a = memory[executionPoint + 1];
                    } else if (instruction[PARAMETER_MODE_1] == RELATIVE_MODE) {
                        a = memory[(int) memory[executionPoint + 1] + (int) relativeBase];
                    }
                    outputRegister = a;
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

                } else if (instruction[OPCODE] == 99) {
                    halted = true;
                    break;
                }
            }
            return (int) outputRegister;
        }
    }

    public static class Coord{
        int _x;
        int _y;
        public Coord(int x, int y){
            _x = x;
            _y = y;
        }

        public double angle(){
            return Math.atan2(_x,_y);
        }

        public boolean equals(Object b){
            if (!(b instanceof Coord)){
                return false;
            }
            Coord p;
            try {
                p = (Coord) b;
            } catch (ClassCastException e){
                return false;
            }
            return _x == p._x && _y == p._y;
        }
        public int hashCode() {
            return Objects.hash(_x,_y);
        }

    }
}
