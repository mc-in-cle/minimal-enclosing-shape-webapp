package world.info.minorcline.mes;

/**
* Represents a finite, integer coordinate grid.
* Offers static methods to translate lists of coordinates.
* 
* @author M.C. September 2018
**/

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.lang.Math;

public class Grid{
    
    private int xMax, yMax;

    public Grid(int xMax, int yMax){
        //if (xMax < 0 || yMax < 0) throw new Exception ("Cannot create Grid with negative maximum bounds.");
        this.xMax = xMax;
        this.yMax = yMax;
    }

    public Grid (Point max){
        this(max.x(), max.y());
    }

    public Point getBounds(){
        return new Point(xMax, yMax);
    }
    
    public int getXBound(){
        return xMax;
    }

    public int getYBound(){
        return yMax;
    }
    
     public boolean isInBounds(Point c){
         return isInBounds(c.x(), c.y()); 
     }

     public boolean isInBounds(int x, int y){
        return x <= xMax && y <= yMax && y >= 0 && x >= 0;
     }

    //Return all Coordinates in the grid with taxicab distance "distance" from Coordinate center.
    //That's all points where |delta x| + |delta y| == distance.
    //Excludes any coordinates that are out of bounds.
    public List<Point> getNearbyCoordinatesWithinBounds(Point center, int distance){
        List<Point> nears = new ArrayList<>();
        if (!isInBounds(center)) return nears;

        int x = center.x(), y = center.y();
        int d = distance;
        for (int i = 0; i < d; i++){
            List<Point> pts = new ArrayList<>();
            pts.add(new Point(x + i - d, y + i));
            pts.add(new Point(x + i, y + d - i));
            pts.add(new Point(x + d - i, y - i));
            pts.add(new Point(x - i, y + i - d));
            for (Point c : pts){
                if (isInBounds(c)) 
                nears.add(c);
            }
        }
        return nears;
    }
    
    //the bottom left corner is the point (X, Y) such that for all coordinates,
    //c.x() >= X && c.y() >= Y.
    //If there are no points provided, returns null.
    public static Point locateBottomLeftCorner(List<Point> points){
        return locateBottomLeftCorner(points.iterator());
    }


    private static Point locateBottomLeftCorner(Iterator<Point> itr){
        if (!itr.hasNext()) return null;
        
        Point first = itr.next();
        int x = first.x();
        int y = first.y();
        while (itr.hasNext()){
            Point c = itr.next();
            if (c.x() < x) x = c.x();
            if (c.y() < y) y = c.y();
        }
        return new Point(x,y);
    }

    //the top right corner is the point (X, Y) such that for all coordinates,
    //c.x() <= X && c.y() <= Y.
    //If there are no points provided, returns null.
    public static Point locateTopRightCorner(List<Point> points){
        return locateTopRightCorner(points.iterator());
    }

    /*
    public static Coordinate locateTopRightCorner(PointSet pointset){
        return locateTopRightCorner(pointset.iterator());
    }
    */

    private static Point locateTopRightCorner(Iterator<Point> itr){
        if (!itr.hasNext()) return null;
        
        Point first = itr.next();
        int x = first.x();
        int y = first.y();
        while (itr.hasNext()){
            Point c = itr.next();
            if (c.x() > x) x = c.x();
            if (c.y() > y) y = c.y();
        }
        return new Point(x,y);
    }

    //Returns a new list where all the points in the parameter list 
    //are shifted so that 'origin' is now the bottom left corner.
    public static List<Point> shiftPoints(List<Point> coords, Point origin){
    	if (coords.isEmpty())
    		return coords;
        Point bottomLeftCorner = locateBottomLeftCorner(coords);
        int shiftx = origin.x() - bottomLeftCorner.x();
        int shifty = origin.y() - bottomLeftCorner.y();
        
        //if the corner is already at the origin, done
        if (shiftx == 0 && shifty == 0) return coords;

        //otherwise make a new list with coordinates shifted to the origin
        List<Point> shiftedList = new ArrayList<>();
        for (Point c: coords){
            shiftedList.add(new Point(c.x() + shiftx, c.y() + shifty));
        }
        return shiftedList;
    }

    //iterates through the coordinates from start to finish, not including start or finish.
    //travels along the x-axis first if xFirst = true, otherwise travels along the y-axis first.
    //Accepts negative coordinates
    public static Iterator<Point> getTraveller(Point start, Point finish, boolean xFirst){
        return new Iterator<Point>(){
            private int x = start.x(), y = start.y();
            private boolean goingX = xFirst;
            private int distToEnd = Math.abs(start.x() - finish.x()) + Math.abs(start.y() - finish.y());

            public boolean hasNext(){
                return distToEnd > 1;
            }

            public Point next(){
                //if (!hasNext()) throw new NoSuchElementException();
                if (goingX && x == finish.x()) goingX = false;
                else if (!goingX && y == finish.y()) goingX = true;

                if (goingX){
                    if (start.x() < finish.x()) x++;
                    else x--;
                    distToEnd--;
                }
                else {
                    if (start.y() < finish.y()) y++;
                    else y--;
                    distToEnd--;
                }
                return new Point(x,y);
            }
        };
    }

}