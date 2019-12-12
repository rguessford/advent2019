import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class day12 {
    public static void main(String[] args) {
        ArrayList<Triord> moons = new ArrayList<>();
        moons.add(new Triord(-10,-10,-13));
        moons.add(new Triord(5,5,-9));
        moons.add(new Triord(3,8,-16));
        moons.add(new Triord(1,3,-3));

        ArrayList<Triord> velocities = new ArrayList<>();
        velocities.add(new Triord(0,0,0));
        velocities.add(new Triord(0,0,0));
        velocities.add(new Triord(0,0,0));
        velocities.add(new Triord(0,0,0));
        
        boolean periodXfound = false;
        boolean periodYfound = false;
        boolean periodZfound = false;

        int perx = -1;
        int pery = -1;
        int perz = -1;

        HashSet<List<Integer>> positionsx = new HashSet<>();
        HashSet<List<Integer>> positionsy = new HashSet<>();
        HashSet<List<Integer>> positionsz = new HashSet<>();
        
        for (int steps = 0; perx < 0 || pery < 0 || perz < 0; steps++) {
            for (int i = 0; i < moons.size(); i++) {
                Triord a = moons.get(i);
                Triord v = velocities.get(i);
                for (int j = 0; j < moons.size(); j++) {
                    if (i!=j){
                        Triord b = moons.get(j);
                        if (a._x < b._x){
                            v._x++;
                        } else if (a._x > b._x){
                            v._x--;
                        }
                        if (a._y < b._y){
                            v._y++;
                        } else if (a._y > b._y){
                            v._y--;
                        }
                        if (a._z < b._z){
                            v._z++;
                        } else if (a._z > b._z){
                            v._z--;
                        }
                    }
                }
            }
            for (int i = 0; i < moons.size(); i++) {
                Triord a = moons.get(i);
                Triord v = velocities.get(i);

                a._x += v._x;
                a._y += v._y;
                a._z += v._z;
            }
            if(perx < 0){
                List<Integer> pos = new ArrayList<>();
                for (int i = 0; i < moons.size(); i++) {
                    pos.add(moons.get(i)._x);
                    pos.add(velocities.get(i)._x);
                }
                if (positionsx.contains(pos)){
                    System.out.println("Found x duplicate after " + steps + " steps");
                    perx = steps;
                }
                positionsx.add(pos);
            }
            if(pery < 0){
                List<Integer> pos = new ArrayList<>();
                for (int i = 0; i < moons.size(); i++) {
                    pos.add(moons.get(i)._y);
                    pos.add(velocities.get(i)._y);
                }
                if (positionsy.contains(pos)){
                    System.out.println("Found y duplicate after " + steps + " steps");
                    pery = steps;
                }
                positionsy.add(pos);
            }
            if(perz < 0){
                List<Integer> pos = new ArrayList<>();
                for (int i = 0; i < moons.size(); i++) {
                    pos.add(moons.get(i)._z);
                    pos.add(velocities.get(i)._z);
                }
                if (positionsz.contains(pos)){
                    System.out.println("Found z duplicate after " + steps + " steps");
                    perz = steps;
                }
                positionsz.add(pos);
            }

        }
        //part 1
//        int totalEnergy = 0;
//        for (int i = 0; i < moons.size(); i++) {
//            int pot = moons.get(i).energy();
//            int kin = velocities.get(i).energy();
//            totalEnergy += pot * kin;
//        }
//        System.out.println(totalEnergy);
        //part2
        System.out.println(LCM(LCM(perx, pery), perz));
    }


    public static long GCF(long a, long b) {
        if (b == 0) {
            return a;
        } else {
            return (GCF(b, a % b));
        }
    }

    public static long LCM(long a, long b) {
        return (a * b) / GCF(a, b);
    }

    public static class Triord {
        int _x;
        int _y;
        int _z;

        public Triord(int x, int y, int z){
            _x = x;
            _y = y;
            _z = z;
        }

        public int energy(){
            return Math.abs(_x) + Math.abs(_y) + Math.abs(_z);
        }

        public boolean equals(Object b){
            if (!(b instanceof Triord)){
                return false;
            }
            Triord p;
            try {
                p = (Triord) b;
            } catch (ClassCastException e){
                return false;
            }
            return _x == p._x && _y == p._y && _z == p._z;
        }
        public int hashCode() {
            return Objects.hash(_x,_y,_z);
        }

    }
}
