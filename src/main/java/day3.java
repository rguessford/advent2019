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
            ArrayList<Segment> wire1 = generateWireSegments(wire1Path);
            ArrayList<Segment> wire2 = generateWireSegments(wire2Path);
            reader.close();
            ArrayList<Intersection> intersections = new ArrayList<>();
            for(Segment seg1:wire1){
                for(Segment seg2:wire2){

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
    public static ArrayList<Segment> generateWireSegments(String[] wirePath){
        int curx = 0;
        int cury = 0;
        int wireLength = 0;
        ArrayList<Segment> wire = new ArrayList<>();
        for(String instruction : wirePath){
            int nextx = curx;
            int nexty = cury;
            int magnitude = Integer.valueOf(instruction.substring(1));
            switch (instruction.charAt(0)){
                case 'U':
                    nexty += magnitude;
                    break;
                case 'D':
                    nexty -= magnitude;
                    break;
                case 'R':
                    nextx += magnitude;
                    break;
                case 'L':
                    nextx -= magnitude;
                    break;
            }
            wire.add(new Segment(new Point(curx, cury), new Point(nextx, nexty), wireLength));
            curx = nextx;
            cury = nexty;
            wireLength += magnitude;
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
        public Point(int x_, int y_){
            x = x_;
            y = y_;
        }
    }
    public static class Segment{
        Point a;
        Point b;
        //length of wire at point a
        int wireLength;

        public boolean isVertical(){
            return a.x == b.x;
        }
        public Segment(Point a_, Point b_, int wireLength_){
            a = a_;
            b = b_;
            wireLength = wireLength_;
        }

    }
}
