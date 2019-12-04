import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class day3 {
    public static void main(String[] args) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("src/main/res/day3"));
            String[] wire1Path = reader.readLine().split(",");
            String[] wire2Path = reader.readLine().split(",");
            ArrayList<Point> wire1 = generateWireSegments(wire1Path);
            ArrayList<Point> wire2 = generateWireSegments(wire2Path);
            reader.close();
            ArrayList<Intersection> intersections = new ArrayList<>();
            for (int i = 0; i < wire1.size()-1; i++) {
                Point p1 = wire1.get(i);
                Point q1 = wire1.get(i+1);
                boolean wire1SegIsVertical = p1.x == q1.x;
                for (int j = 0; j < wire2.size()-1; j++) {
                    Point p2 = wire2.get(j);
                    Point q2 = wire2.get(j+1);

                    if(wire1SegIsVertical & p2.x == q2.x);
                    else{
                        if (wire1SegIsVertical){
                            //vertical projection
                            if(p2.y > p1.y && p2.y < q1.y || p2.y < p1.y && p2.y > q1.y){
                                //horizontal projection
                                if(p1.x > p2.x && p1.x < q2.x || p1.x < p2.x && p1.x > q2.x){
                                    intersections.add(new Intersection(new Point(p1.x, p2.y, 0), p1.dist + Math.abs(p1.y - p2.y),p2.dist + Math.abs(p2.x - p1.x)));
                                }
                            }
                        } else { //wireseg2 vertical
                            if(p1.y > p2.y && p1.y < q2.y || p1.y < p2.y && p1.y > q2.y){
                                //horizontal projection
                                if(p2.x > p1.x && p2.x < q1.x || p2.x < p1.x && p2.x > q1.x){
                                    intersections.add(new Intersection(new Point(p2.x, p1.y, 0), p1.dist + Math.abs(p1.x - p2.x),p2.dist + Math.abs(p2.y - p1.y)));
                                }
                            }
                        }
                    }
                }
            }
            int min = 99999999;
            for(Intersection i : intersections){
                int dist = Math.abs(i.point.x) + Math.abs(i.point.y);
                min = Math.min(min, dist);
            }
            System.out.println("manhattan dist to closest intersection: " + min);
            min = 99999999;
            for(Intersection i : intersections){
                min = Math.min(min, i.length);
            }
            System.out.println("shortest distance along wires to first intersection " + min);
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }
    public static ArrayList<Point> generateWireSegments(String[] wirePath){
        int curx = 0;
        int cury = 0;
        int wireLength = 0;
        ArrayList<Point> wire = new ArrayList<>();
        wire.add(new Point(0,0,0));
        for(String instruction : wirePath){
            int magnitude = Integer.valueOf(instruction.substring(1));
            switch (instruction.charAt(0)){
                case 'U':
                    cury += magnitude;
                    break;
                case 'D':
                    cury -= magnitude;
                    break;
                case 'R':
                    curx += magnitude;
                    break;
                case 'L':
                    curx -= magnitude;
                    break;
            }
            wireLength += magnitude;
            wire.add(new Point(curx, cury, wireLength));
        }
        return wire;
    }
    public static class Intersection{
        Point point;
        int length;
        public Intersection(Point p, int a, int b){
            point = p;
            length = a+b;
        }
    }
    public static class Point{
        int x;
        int y;
        int dist;
        public Point(int x_, int y_, int dist_){
            x = x_;
            y = y_;
            dist = dist_;
        }
    }
}
