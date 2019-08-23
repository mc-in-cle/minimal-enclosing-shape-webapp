
/**
 * Consists of non-overlapping PointSets, ie 'blobs', co-mingling on a common
 * non-negative integer grid.
 * 
 * Used for MinimalEnclosingShape.
 * 
 * @author M.C. September 2018
 */


package world.info.minorcline.mes;

import java.lang.Iterable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BlobSet extends Grid implements Iterable<PointSet>{

    private List<PointSet> blobs;
    private PointSet[][] lookup;
    //an array such that (x,y) is a pointer to the blob that contains (x,y),
    // or null if it is not in a blob.
    
    public BlobSet(int xMax, int yMax){
        super(xMax,yMax);
        blobs = new ArrayList<PointSet>();
        lookup = new PointSet[xMax + 1][yMax + 1];
    }

    public BlobSet(Point max){
    	super(max.x(),max.y());
        blobs = new ArrayList<PointSet>();
        lookup = new PointSet[max.x() + 1][max.y() + 1];
    }

    public int numberOfBlobs() {
    	return blobs.size();
    }
    
    public Iterator<PointSet> iterator(){
        return blobs.iterator();
    }

    //If one of the points in the list is already in a different blob, that point is ignored,
    //since blobs cannot overlap.
    //Points that are out of bounds are ignored.
    //If no valid points are provided, no blob is added.
    public void addBlob(List<Point> points){
        addBlob(points.iterator());
    }
    
    //The PointSet that is added to this BlobSet will have the same bounds
    //as this BlobSet, irrespective of the bounds of the param 'points'.
    public void addBlob(PointSet points){
        addBlob(points.iterator());
    }
    
    private void addBlob(Iterator<Point> pointsItr){
        PointSet newBlob = new PointSet(getXBound(), getYBound());
        while (pointsItr.hasNext()){
            Point c = pointsItr.next();
            if (isInBounds(c) && lookup[c.x()][c.y()] == null){
               newBlob.addPoint(c);
               lookup[c.x()][c.y()] = newBlob;
            }
        }
        if (newBlob.numberOfPoints() != 0) blobs.add(newBlob);
    }
    
    public boolean hasPoint(Point c){
        return isInBounds(c) && lookup[c.x()][c.y()] != null;
    }
    
    public boolean hasPoint(int x, int y) {
    	return hasPoint(new Point(x,y));
    }
    
    public boolean hasAllPoints(PointSet p) {
    	for (Point point : p) {
    		if (!this.hasPoint(point)) return false;
    	}
    	return true;
    }
    
    public boolean hasAllPoints(List<Point> list) {
    	for (Point point : list) {
    		if (!this.hasPoint(point)) return false;
    	}
    	return true;
    }
    
    public boolean hasBlob(PointSet p) {
    	for (PointSet blob : blobs) {
    		if (p.equals(blob))
    			return true;
    	}
    	return false;
    }
    
    //This method will do nothing if nextPoint is in another blob,
    //since blobs must not overlap.
    public void expandByOne(PointSet blob, Point nextPoint){
    	if (!this.hasBlob(blob)) return;
        
        if (!isInBounds(nextPoint)) return;
       
        if (this.hasPoint(nextPoint)) return;
        
        blob.addPoint(nextPoint);
        lookup[nextPoint.x()][nextPoint.y()] = blob;
    }
    

    //Identifies any blobs that are adjacent (distance 1) to one another and merges them.
    public void mergeAdjacentBlobs(){
    	List<PointSet> removeList = new ArrayList<>();
    	
        for (int i = 0; i < blobs.size(); i++){
        	PointSet blob = blobs.get(i);
            Set<Point> neighbors = new HashSet<>();
            for (Point c : blob){
                List<Point> pointNeighbors = getNearbyCoordinatesWithinBounds(c, 1);
                neighbors.addAll(pointNeighbors);
            }
            for (Point c : neighbors){
                PointSet adjacentBlob = lookup[c.x()][c.y()];
                if (adjacentBlob != null && adjacentBlob != blob){
                    adjacentBlob.addAllPoints(blob);
                    removeList.add(blob);
                    for (Point c2 : blob) lookup[c2.x()][c2.y()] = adjacentBlob;
                }
            }
        }
        for (PointSet p : removeList)
        	blobs.remove(p);
    }

    public ShortestPathsOnBlobSet createShortestPathsModel(){
        ShortestPathsOnBlobSet model = new ShortestPathsOnBlobSet();
        for (int i = 0; i < blobs.size(); i++){
            for (int j = i + 1; j < blobs.size(); j++){
                PointSet blob1 = blobs.get(i), blob2 = blobs.get(j);
                List<Path> paths = shortestPathsBetween(blob1, blob2);
                for (Path p : paths)
                    model.addPath(blob1, blob2, p);
            }
        }
        return model;
    }

    //Produces a list of all the shortest paths between blob1 and blob2
    //If all the direct paths between blob1 and blob2 are blocked by a some other blob(s),
    //the list returned will be empty.
    public List<Path> shortestPathsBetween(PointSet blob1, PointSet blob2){
        if (!blobs.contains(blob1) || !blobs.contains(blob2)) return null;

        int minPathLength = Integer.MAX_VALUE;
        List<Path> shortestPaths = new ArrayList<>();

        for (Point start : blob1){
            for (Point finish : blob2){
                //There are two possible paths: travelling on the x-axis, then the y; or the y-axis first and then the x.
                //Look at both. However, if the two points lie in a line, the paths would be the same. Only look at one.
                Iterator<Point> xPathItr = Grid.getTraveller(start, finish, true);
                Iterator<Point> yPathItr = Grid.getTraveller(start, finish, false);
                boolean inline = start.x() == finish.x() || start.y() == finish.y();
                
                minPathLength = discoverShortPath(xPathItr, minPathLength, shortestPaths);
                if(!inline)
                	minPathLength = discoverShortPath(yPathItr, minPathLength, shortestPaths);
                	
            }
        }
        return shortestPaths;
    }
    
    //Calls createClearPathNoLongerThan(itr, minPathLength) and checks the result.
    //If a path is found that is shorter than minPathLength, the method clears shortestPaths and adds
    //the new path.
    //This method MODIFIES the parameter shortestPaths.
    //Returns minPathLength, or the length of the path discovered by itr if it is shorter than minPathLength.
    private int discoverShortPath(Iterator<Point> itr, int minPathLength, List<Path> shortestPaths) {
    	
    	Path shortPath = createClearPathNoLongerThan(itr, minPathLength);
    	if (shortPath == null)
    		return minPathLength;
    	
    	if (shortPath.length() < minPathLength) {
    		shortestPaths.clear();
    	}
    	
    	shortestPaths.add(shortPath);
    	return shortPath.length();
    }
    
    
    //if this path is longer than the minimum path length, it is too long, ignore this path.
    //If there is any blob in the way of the path, ignore this path.
    //Return null if the iterator can't create a valid path.
    private Path createClearPathNoLongerThan(Iterator<Point> itr, int maxPermissibleLength){
        Path p = new Path();
        while(itr.hasNext()){
            Point nextPt = itr.next();
            if (p.length() == maxPermissibleLength || this.hasPoint(nextPt)) {
            	return null;
            }
            
            p.addPoint(nextPt);
        }
        return p;
    }
    
    
    public void display() {
    	Map<PointSet, Character> alphaMap = new HashMap<>();
    	for (int i = 0; i < blobs.size(); i++) {
    		char val = (char)('A' + i);
    		alphaMap.put(blobs.get(i), val);
    	}
    	
    	for (int i = lookup.length - 1; i >= 0; i--) {
    		for (int j = 0; j < lookup[i].length; j++) {
    			if (lookup[i][j] == null) 
    				System.out.print(".");
    			else 
    				System.out.print(alphaMap.get(lookup[i][j]).charValue());
    		}
    		System.out.println();
    	}
    	System.out.println();
    }
    

}