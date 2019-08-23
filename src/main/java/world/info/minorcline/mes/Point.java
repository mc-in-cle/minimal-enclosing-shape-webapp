package world.info.minorcline.mes;

//immutable object representing an integer x-y coordinate

public class Point{
    private int x;
    private int y;
    private int hash;
    
    public Point(int a, int b){
        x = a;
        y = b;
    }

    public int x() { return x; }

    public int y() { return y; }

    public boolean equals(Object o){
    	if (o instanceof Point) {
    		return x == ((Point)o).x() && y == ((Point)o).y();
    	}
    	else return false;
    }

    public String toString(){
        return "(" + x + ", " + y + ")";
    }
    
    public int hashCode() {
        return x ^ y;
    }


  
}