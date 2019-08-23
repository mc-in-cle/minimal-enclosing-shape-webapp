package world.info.minorcline.mes;

/**
* Represents a finite, integer coordinate grid with points on it.
* 
* @author M.C. September 2018
**/

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class PointSet extends Grid implements Iterable<Point>, Cloneable{

    private List<Point> listOfPoints; //a list, but also implemented as a set
    private boolean[][] arrayOfPoints;

    
    public PointSet(int xMax, int yMax){
        super(xMax,yMax);
        arrayOfPoints = new boolean[xMax+1][yMax+1];
        listOfPoints = new LinkedList<Point>();
    }

    public PointSet(Point max){
        super(max.x(),max.y());
        arrayOfPoints = new boolean[max.x()+1][max.y()+1];
        listOfPoints = new LinkedList<Point>();
    }

    public PointSet clone(){
        PointSet p = new PointSet(getXBound(), getYBound());
        p.addAllPoints(this);
        return p;
    }

    public Iterator<Point> iterator(){
       return listOfPoints.iterator();
    }

    public List<Point> pointsList(){
        //Coordinates are immutable, so producing a shallow copy of the list is sufficient
        //to protect data.
        List<Point> clone = new LinkedList<Point>();
        clone.addAll(listOfPoints);
    	return clone;
    }

    public boolean hasPoint(Point c){
        return hasPoint(c.x(), c.y());
    }

    public boolean hasPoint(int x, int y){
        if (!isInBounds(x,y)) return false;
        return arrayOfPoints[x][y];
    }

    public int numberOfPoints(){
        return listOfPoints.size();
    }

    public boolean hasAllPoints(PointSet p){
        for (Point c : p ){
            if (!hasPoint(c)) return false;
        }
        return true;
    }
    
    public boolean hasAllPoints(List<Point> points) {
    	for (Point c : points ){
            if (!hasPoint(c)) return false;
        }
        return true;
    }

    //Ignores points that are not within the Grid's boundaries.
    public void addPoint(Point c){
        if (isInBounds(c) && !arrayOfPoints[c.x()][c.y()]){
            arrayOfPoints[c.x()][c.y()] = true;
            listOfPoints.add(c);
        }
    }
    
    public void addPoint(int x, int y) {
    	addPoint(new Point(x,y));
    }
    
    public void addAllPoints(List<Point> points){
        for (Point c : points){
            addPoint(c);
        }
    }

    public void addAllPoints(PointSet p){
        for (Point c : p){
            addPoint(c);
        }
    }


    //Ignores points that are not within the Grid's boundaries.
    public void removePoint(Point c){
        if (isInBounds(c) && arrayOfPoints[c.x()][c.y()]){
            arrayOfPoints[c.x()][c.y()] = false;
            listOfPoints.remove(c);
        }
    }
    
    public void removePoint(int x, int y){
       removePoint(new Point(x,y));
    }
    
    public void removeAllPoints(List<Point> points){
        for (Point c : points){
            removePoint(c);
        }
    }

    public void removeAllPoints(PointSet p){
        for (Point c : p){
            removePoint(c);
        }
    }
    
    public boolean equals(Object o) {
    	if (o instanceof PointSet) {
    		if (!((PointSet)o).getBounds().equals(this.getBounds()))
    			return false;
    		
    		if (((PointSet)o).numberOfPoints() != this.numberOfPoints())
    			return false;
    		
    		for (Point p : (PointSet)o) {
    			if (!this.hasPoint(p)) {
    				return false;
    			}
    		}
    		return true;
    	
    	} else
    		return false;
    }
    
    public int hashCode() {
    	int hash = this.getBounds().hashCode();
    	for (Point p : this) {
    		hash += p.hashCode();
    	}
    	hash = hash % (13*17);
    	return hash;
    }
    
    public void display() {
    	for (int i = arrayOfPoints.length - 1; i >= 0; i--) {
    		for (int j = 0; j < arrayOfPoints[i].length; j++) {
    			if (!arrayOfPoints[i][j])
    				System.out.print(".");
    			else 
    				System.out.print("X");
    		}
    		System.out.println();
    	}
    	System.out.println();
    }

}