import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class day0 {
    public static void main(String[] args) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("src/main/res/day0"));


            reader.close();
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }
}