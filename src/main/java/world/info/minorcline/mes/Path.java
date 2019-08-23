
/**
 * A simple class that wraps a list of Points.
 * Used with MinimalEnclosingShape and its helper classes.
 * 
 * @author M.C. September 2018
 */

package world.info.minorcline.mes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Path implements Iterable<Point>{
    private List<Point> pointList;

    public Path(){
        pointList = new ArrayList<Point>();
    }

    public Path(PointSet p){
        for (Point c : p)
            pointList.add(c);
    }

    public Path(List<Point> pointList){
        this.pointList.addAll(pointList);
    }

    public void addPoint(Point c){
        pointList.add(c);
    }
    
    public void addPoint(int i, int j) {
    	pointList.add(new Point(i, j));
    }
    
    public Iterator<Point> iterator(){
        return pointList.iterator();
    }

    public int length(){
        return pointList.size();
    }
    
    public boolean hasPoint(Point p) {
    	for (Point point : pointList) {
    		if (point.equals(p)) return true;
    	}
    	return false;
    }
    
    public boolean hasPoint(int x, int y) {
    	return hasPoint(new Point(x,y));
    }

    public Path reverse(){
    	Path reversed = new Path();
    	for (int i = length() - 1; i >= 0; i--) {
    		reversed.addPoint(pointList.get(i));
    	}
    	return reversed;
    }
    
    public boolean equals(Object o) {
    	if (o instanceof Path) {
    		if ( ((Path)o).length() != this.length())
    			return false;
    		Iterator<Point> other = ((Path)o).iterator();
    		Iterator<Point> selfItr = this.iterator();
    		while(other.hasNext()) {
    			if (! (other.next().equals(selfItr.next())))
    				return false;
    		}
    		return true;
    	}
    	return false;
    }
    
    public int hashCode() {
    	int hash = 0;
    	for (Point p : this) {
    		hash += p.hashCode();
    	}
    	hash = hash % (11*17);
    	return hash;
    }
    
    
}