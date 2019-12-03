import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class day1 {
    public static void main(String[] args) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("src/main/res/day1"));
            int accumulator = 0;
            while (reader.ready()){
                accumulator+= fuelReqForMass(0, Integer.valueOf(reader.readLine()));
            }
            System.out.println(accumulator);
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }

    }
    public static int fuelReqForMass(int total, int mass){
        int result = mass/3;
        result -= 2;

        if(result > 0) {
            total += result;
            total = fuelReqForMass(total, result);
        }
        return total;
    }


}
