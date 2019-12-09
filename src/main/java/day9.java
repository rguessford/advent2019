import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class day9 {
    public static final int OPCODE = 3;
    public static final int PARAMETER_MODE_1 = 2;
    public static final int PARAMETER_MODE_2 = 1;
    public static final int PARAMETER_MODE_3 = 0;

    public static final int PARAMETER_1 = 1;
    public static final int PARAMETER_2 = 2;
    public static final int PARAMETER_3 = 3;

    public static final int POSITION_MODE = 0;
    public static final int IMMEDIATE_MODE = 1;
    public static final int RELATIVE_MODE = 2;


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BufferedReader reader;
        long[] memory;
        try {
            reader = new BufferedReader(new FileReader("src/main/res/day9"));
            memory = Arrays.stream(reader.readLine().split(",")).mapToLong(Long::parseLong).toArray();
            memory = Arrays.copyOf(memory, memory.length +1000);
            long relativeBase=0;

            for (int executionPoint = 0; ; ) {
                long[] instruction = new long[]{memory[executionPoint]/10000, (memory[executionPoint]/1000)%10, (memory[executionPoint]/100) % 10, memory[executionPoint]%100};

                if(instruction[OPCODE] == 1) {
                    //add
                    long a = readParameter(memory, (int) relativeBase, executionPoint, instruction, PARAMETER_1);
                    long b = readParameter(memory, (int) relativeBase, executionPoint, instruction, PARAMETER_2);
                    writeValue(memory, (int) relativeBase, executionPoint, instruction, PARAMETER_3, a+b);
                    executionPoint +=4;
                } else if (instruction[OPCODE] == 2) {
                    //multiply
                    long a = readParameter(memory, (int) relativeBase, executionPoint, instruction, PARAMETER_1);
                    long b = readParameter(memory, (int) relativeBase, executionPoint, instruction, PARAMETER_2);
                    writeValue(memory, (int) relativeBase, executionPoint, instruction, PARAMETER_3, a*b);
                    executionPoint+=4;
                } else if (instruction[OPCODE] == 3) {
                    //input
                    long in = scanner.nextInt();
                    writeValue(memory, (int) relativeBase, executionPoint, instruction, PARAMETER_1, in);
                    executionPoint+=2;
                } else if (instruction[OPCODE] == 4) {
                    //output
                    long a = readParameter(memory, (int) relativeBase, executionPoint, instruction, PARAMETER_1);
                    System.out.println(a);
                    executionPoint+=2;
                } else if (instruction[OPCODE] == 5) {
                    //jump if param 1 != 0
                    long test = readParameter(memory, (int) relativeBase, executionPoint, instruction, PARAMETER_1);
                    if(test == 0) {
                        executionPoint += 3;
                    } else {
                        executionPoint = (int) readParameter(memory, (int) relativeBase, executionPoint, instruction, PARAMETER_2);
                    }
                } else if (instruction[OPCODE] == 6) {
                    //jump if param 1 == 0
                    long test = readParameter(memory, (int) relativeBase, executionPoint, instruction, PARAMETER_1);
                    if(test != 0) {
                        executionPoint += 3;
                    } else {
                        executionPoint = (int) readParameter(memory, (int) relativeBase, executionPoint, instruction, PARAMETER_2);
                    }
                } else if (instruction[OPCODE] == 7) {
                    //less than
                    long a = readParameter(memory, (int) relativeBase, executionPoint, instruction, PARAMETER_1);
                    long b = readParameter(memory, (int) relativeBase, executionPoint, instruction, PARAMETER_2);
                    writeValue(memory, (int) relativeBase, executionPoint, instruction, PARAMETER_3, a < b ? 1 : 0);
                    executionPoint+=4;
                } else if (instruction[OPCODE] == 8) {
                    //equals
                    long a = readParameter(memory, (int) relativeBase, executionPoint, instruction, PARAMETER_1);
                    long b = readParameter(memory, (int) relativeBase, executionPoint, instruction, PARAMETER_2);
                    writeValue(memory, (int) relativeBase, executionPoint, instruction, PARAMETER_3, a == b ? 1 : 0);
                    executionPoint+=4;
                } else if (instruction[OPCODE] == 9) {
                    //adjust relative base
                    relativeBase = readParameter(memory, (int) relativeBase, executionPoint, instruction, PARAMETER_1);
                    executionPoint +=2;

                } else if (instruction[OPCODE] == 99) break;
            }

            reader.close();
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void writeValue(long[] memory, int relativeBase, int executionPoint, long[] instruction, int addressParameter, long value) {
        int parameterMode = -1;
        if (addressParameter == PARAMETER_1) parameterMode = PARAMETER_MODE_1;
        if (addressParameter == PARAMETER_2) parameterMode = PARAMETER_MODE_2;
        if (addressParameter == PARAMETER_3) parameterMode = PARAMETER_MODE_3;

        if (instruction[parameterMode] == POSITION_MODE) {
            memory[(int) memory[executionPoint + addressParameter]] = value;
        } else if (instruction[parameterMode] == RELATIVE_MODE) {
            memory[(int) memory[executionPoint + addressParameter] + relativeBase] = value;
        }
    }

    private static long readParameter(long[] memory, int relativeBase, int executionPoint, long[] instruction, int parameter) {
        int parameterMode = -1;
        if (parameter == PARAMETER_1) parameterMode = PARAMETER_MODE_1;
        if (parameter == PARAMETER_2) parameterMode = PARAMETER_MODE_2;
        long value = 0;
        if (instruction[parameterMode] == POSITION_MODE) {
            value = memory[(int) memory[executionPoint + parameter]];
        } else if (instruction[parameterMode] == IMMEDIATE_MODE) {
            value = memory[executionPoint + parameter];
        } else if (instruction[parameterMode] == RELATIVE_MODE) {
            value = memory[(int) memory[executionPoint + parameter] + relativeBase];
        }
        return value;
    }
}
