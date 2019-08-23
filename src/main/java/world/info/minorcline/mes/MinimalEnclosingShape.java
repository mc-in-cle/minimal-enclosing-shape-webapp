package world.info.minorcline.mes;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MinimalEnclosingShape{
	
	private List<Point> inputList;
	private PointSet pointset;
	private PointSet shapeset;
	private Point origin;
	private Point endCorner;
	private boolean debug = false;
	private BlobSet blobs;
	

	public MinimalEnclosingShape(List<Point> points) {
		inputList = points;
		origin = Grid.locateBottomLeftCorner(inputList);
		
		//shift the points so the coordinates are non-negative and can be used with Grid class.
		//(unshift points prior to returning results to user)
		List<Point> shifted = Grid.shiftPoints(inputList, new Point(0,0));
        endCorner = Grid.locateTopRightCorner(shifted);
        if (endCorner == null)
        	endCorner = new Point(0,0);
        pointset = new PointSet(endCorner);
        pointset.addAllPoints(shifted);
	}
	
	public void setDebugOn() {
		debug = true;
	}
	
	public void setDebugOff() {
		debug = false;
	}

	public void displayInput() {
		pointset.display();
	}
	
	public void displayResult() {
		shapeset.display();
	}

	public void findShape(){
		if (pointset.numberOfPoints() == 0) {
			shapeset = new PointSet(0,0);
			return;
		}
        createBlobs();
        if (debug) blobs.display();

        int itr = 0;
        while(blobs.numberOfBlobs() > 1) {
        	itr++;
        	if (debug && itr > 10000) {
                System.out.println("Loop failed to resolve!");
                System.exit(1);
            }
        	growShape();
        }
        if (debug) System.out.println("Algorithm completed in " + itr + " steps.");
        
        shapeset = blobs.iterator().next();
    }
	
	public PointSet getResultPointSet() {
		return shapeset;
	}
	
	public List<Point> getResultList(){
		return Grid.shiftPoints(shapeset.pointsList(), origin);
	}

    //This method is an algorithm that produces the compact enclosing shape.
    private void growShape(){
        ShortestPathsOnBlobSet pathsModel = blobs.createShortestPathsModel();
        short[][] crossPaths = tabulateCrossPaths(pathsModel);
       
        for (PointSet blob : blobs){
            int maxRating = 0;
            Path bestPath = new Path();
            Iterator<Path> shortestPathsItr = pathsModel.iterateShortestPathsFrom(blob);
            while (shortestPathsItr.hasNext()){
                Path path = shortestPathsItr.next();
                int rating = getRating(path, crossPaths);
                if (rating > maxRating){
                    rating = maxRating;
                    bestPath = path;
                }
            }
            if (bestPath.length() > 0) {
            	blobs.expandByOne(blob, bestPath.iterator().next());
            }
        }
        blobs.mergeAdjacentBlobs();
        if (debug) blobs.display();
    }

    
    private short[][] tabulateCrossPaths(ShortestPathsOnBlobSet model){
    	short[][] crossPaths = new short[blobs.getBounds().x() + 1][blobs.getBounds().y() + 1];
        for (Path p : model){
            for (Point c : p){
                crossPaths[c.x()][c.y()]++;
            }
        }
        return crossPaths;
    }
    
    private int getRating(Path path, short[][] crossPaths) {
    	int total = 0;
    	for (Point c : path) {
    		try {
    			total += crossPaths[c.x()][c.y()];
    		} catch(ArrayIndexOutOfBoundsException e) {}
    	}
    	return total;
    }

    private void createBlobs(){
        blobs = new BlobSet(pointset.getBounds());

        //make a copy of the points
        PointSet unexploredPoints = pointset.clone();

        //Examine all points
        //When a point is added to a blob, remove it from the set of unexplored points
        while (unexploredPoints.numberOfPoints() != 0){
            Point nextPoint = unexploredPoints.iterator().next();
            PointSet nextBlob = growBlobFromAdjacents(nextPoint);
            blobs.addBlob(nextBlob);
            unexploredPoints.removeAllPoints(nextBlob);
        }
    }

    //Perform depth-first search to build a PointSet that consists of all Points
    //in pointset that are contiguously adjacent to 'center'.
    //if pointset does not contain the point 'center', return an empty PointSet.
    private PointSet growBlobFromAdjacents(Point center){
    	if (!pointset.hasPoint(center))
    		return new PointSet(pointset.getBounds());
    	
    	PointSet blob = new PointSet(pointset.getBounds());
    	PointSet explored = new PointSet(pointset.getBounds());
    	
    	Deque<Point> stack = new LinkedList<>();
    	stack.push(center);
    	
    	while(!stack.isEmpty()) {
    		Point node = stack.pop();
    		if (pointset.hasPoint(node)) {
    			blob.addPoint(node);
    			List<Point> neighbors = pointset.getNearbyCoordinatesWithinBounds(node,1);
    			
    			for (Point p : neighbors) {
    				if (!explored.hasPoint(p))
    					stack.push(p);
    			}
    			
    			explored.addAllPoints(neighbors);
    			explored.addPoint(node);
    		}
    	}
        return blob;
    }


	
}
