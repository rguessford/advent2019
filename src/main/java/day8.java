import sun.plugin.javascript.navig4.Layer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class day8 {

    static final int LAYER_WIDTH = 25;
    static final int LAYER_HEIGHT = 6;

    public static void main(String[] args) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("src/main/res/day8"));
            String image = reader.readLine();
            ArrayList<int[][]> layers = new ArrayList<>(image.length()/LAYER_WIDTH*LAYER_HEIGHT);

            while(image.length() != 0) {
                int[][] layer = new int[LAYER_HEIGHT][];

                for (int i = 0; i < LAYER_HEIGHT; i++) {
                    int[] intRow = new int[LAYER_WIDTH];
                    String stringRow = image.substring(0,LAYER_WIDTH);
                    image = image.substring(LAYER_WIDTH);

                    for (int j = 0; j < stringRow.length(); j++) {
                        intRow[j] = Character.getNumericValue(stringRow.charAt(j));
                    }
                    layer[i] = intRow;
                }
                layers.add(layer);
            }

            //Part 1
            //find layer with least zeroes
            int leastZeroes = 9999999;
            int[] digitCountsOfLayerOfConcern = null;

            for(int[][] layer: layers){
                int[] counts = layerDigitCount(layer);

                if(counts[0] < leastZeroes){
                    digitCountsOfLayerOfConcern = counts;
                    leastZeroes = counts[0];
                }
            }
            try{
                System.out.println(digitCountsOfLayerOfConcern[1] * digitCountsOfLayerOfConcern[2]);
            } catch (NullPointerException e){
                System.err.println("something went wrong");
            }

            //Part 2
            int[][] decodedImage = new int[LAYER_HEIGHT][LAYER_WIDTH];
            for (int i = 0; i < LAYER_HEIGHT; i++) {
                for (int j = 0; j < LAYER_WIDTH; j++) {
                    for (int k = 0; k < layers.size(); k++) {
                        int pixel = layers.get(k)[i][j];

                        if(pixel == 2){
                            continue;
                        } else {
                            decodedImage[i][j] = pixel;
                            break;
                        }
                    }
                }
            }

            for (int i = 0; i < LAYER_HEIGHT; i++) {
                for (int j = 0; j < LAYER_WIDTH; j++) {
                    System.out.print(decodedImage[i][j]);
                }
                System.out.print('\n');
            }
            reader.close();
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static int[] layerDigitCount(int[][] layer){
        int[] digitCounts = new int[10];

        for (int i = 0; i < LAYER_HEIGHT; i++) {
            for (int j = 0; j < LAYER_WIDTH; j++) {
                digitCounts[layer[i][j]]++;
            }
        }

        return digitCounts;
    }
}
