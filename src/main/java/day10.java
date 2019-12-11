import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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
            HashMap<Coord, ArrayList<Coord>> targetList = new HashMap<>();
            int sanitycounter = 0;
            //obtained from part 1
            final int laserInstallationX = 28;
            final int laserInstallationY = 29;
            for (int i = 0; i < field[0].length ; i++) {
                for (int j = 0; j < field.length; j++) {
                    //i == x j == y

                    if(field[j][i] == '.') continue;
                    int visibleAsteroidCount = 0;
                    for (int k = 0; k < field[0].length; k++) {
                        for (int l = 0; l < field.length; l++) {
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
                    }


                    //part 2 stuff
                    if(!(i == laserInstallationX && j == laserInstallationY)){
                        int rise = laserInstallationY - j;
                        int run = laserInstallationX - i;
                        int gcd = gcd(Math.abs(rise), Math.abs(run));
                        gcd = gcd == 0 ? 1 : gcd;
                        rise /= gcd;
                        run /= gcd;
                        int x = laserInstallationX;
                        int y = laserInstallationY;
                        x -= run;
                        y -= rise;
                        while(x >= 0 && x < field[0].length && y >= 0 && y < field.length){
                            if(field[y][x] == '#'){
                                Coord vector = new Coord(run, rise);
                                Coord asteroid = new Coord(x, y);
                                ArrayList<Coord> targets = targetList.getOrDefault(vector, new ArrayList<>());
                                if(!targets.contains(asteroid)) {
                                    targets.add(new Coord(x, y));
                                    targetList.put(vector, targets);
                                    sanitycounter++;
                                }

                            }
                            x-= run;
                            y-= rise;
                        }
                    }
                }
            }

            System.out.println(mostAsteroidsVisible);
            System.out.println(laserInstallationX+ ","+ laserInstallationY);
            ArrayList<Map.Entry<Coord, ArrayList<Coord>>> targetArray = new ArrayList<>(targetList.entrySet());
            targetArray.sort(new coordAngleComparator());
            int beginIndex = -1;
            for (int i = 0; i < targetArray.size(); i++) {
                if(targetArray.get(i).getKey().equals(new Coord(0,1))){
                    beginIndex = i;
                }
            }
            int asteroidsDestroyed = 0;
            Coord lasteroidRemoved = null;
            while (asteroidsDestroyed != 200){
                ArrayList<Coord> targets = targetArray.get(beginIndex).getValue();
                if(targets.size() ==0){
                    continue;
                }
                lasteroidRemoved = targets.remove(0);
                asteroidsDestroyed++;

                beginIndex--;
                if (beginIndex <0) beginIndex = targetArray.size()-1;
            }
            System.out.println(lasteroidRemoved._x + " " + lasteroidRemoved._y);

            reader.close();
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public static class coordAngleComparator implements Comparator<Map.Entry<Coord, ArrayList<Coord>>>{

        @Override
        public int compare(Map.Entry<Coord, ArrayList<Coord>> o1, Map.Entry<Coord, ArrayList<Coord>> o2) {
            double angdiff = o1.getKey().angle() - o2.getKey().angle();
            return angdiff == 0 ? 0 : angdiff > 0 ? 1: -1;
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