import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class day10 {
    public static void main(String[] args) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("src/main/res/day10"));
            ArrayList<String> rawField = new ArrayList<>();
            while(reader.ready()){
                rawField.add(reader.readLine());
            }
            char[][] field = new char[rawField.size()][rawField.get(0).length()];
            for (int i = 0; i < rawField.size(); i++) {
                field[i] = rawField.get(i).toCharArray();
            }

            int mostAsteroidsVisible = 0;
            int laserInstallationX = 0;
            int laserInstallationY = 0;
            for (int i = 0; i < field.length ; i++) {
                for (int j = 0; j < field[0].length; j++) {
                    //i == x j == y
                    if(field[j][i] == '.') continue;
                    int visibleAsteroidCount = 0;
                    for (int k = 0; k < field.length; k++) {
                        for (int l = 0; l < field[0].length; l++) {
                            //k==x l==y
                            if(field[l][k] == '.'){ continue; }
                            if(k==i && l==j) continue;

                            int rise = l - j;
                            int run = k - i;
                            int gcd = gcd(Math.abs(rise), Math.abs(run));
                            rise /= gcd;
                            run /= gcd;
                            int x = k;
                            int y = l;
                            boolean visible = true;
                            x -= run;
                            y -= rise;
                            while(x != i || y != j){
                                if (field[y][x] == '#') visible = false;
                                x -= run;
                                y -= rise;
                            }
                            if(visible) visibleAsteroidCount++;
                        }
                    }


                    if(visibleAsteroidCount > mostAsteroidsVisible){
                        mostAsteroidsVisible = visibleAsteroidCount;
                        laserInstallationX = i;
                        laserInstallationY = j;
                    }
                }
            }

            int destroyedAsteroids = 0;
            int x = laserInstallationX;
            int y = 0;
            HashSet<Point> checkedReducedVectors = new HashSet<>();

            System.out.println(mostAsteroidsVisible);

            reader.close();
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    public static class Point{
        int _x;
        int _y;
        public Point(int x, int y){
            _x = x;
            _y = y;
        }
        public boolean equals(Object b){
            if (!(b instanceof Point)){
                return false;
            }
            Point p;
            try {
                p = (Point) b;
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