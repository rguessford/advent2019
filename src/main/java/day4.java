public class day4 {
    public static void main(String[] args) {
        int counter = 0;
        for (int i = 134792; i < 675811; i++) {
            boolean valid = false;
            int j = i;
            int digitToCount = j%10;
            int digitCounter = 0;
            while(j > 0){
                int digit = j%10;
                if(digit == digitToCount){
                    digitCounter++;
                } else {
                    if (digitCounter == 2){
                        valid = true;
                    }
                    digitToCount = digit;
                    digitCounter = 1;
                }
                j = j/10;
                if(j == 0 && digitCounter == 2){
                    valid = true;
                }
                if(j%10 > digit) {
                    valid = false;
                    break;
                }
            }
            if (valid){
                counter++;
            }
        }
        System.out.println(counter);
    }
}

