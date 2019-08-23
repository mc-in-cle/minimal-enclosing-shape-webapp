package world.info.minorcline.mes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A special class.
 * Used with ShortestPathsOnBlobSet to encapsulate information that is relevant
 * to a particular blob.
 * 
 * @author M.C. September 2018
 */

public class ShortestPathsInfo implements Iterable<Path>{
    private PointSet node; //the blob that this information relates to
    private List<Path> shortestPathsList;
    //a list of the shortest paths between 'node' and other blobs in some BlobSet
    //such that the lengths of these paths are all the same and all minimal
    private int shortestPathLength; //the length of the paths in shortestPathsList

    //there is no default constructor. This object cannot be instantiated
    //without indicating at least one shortest path.
    public ShortestPathsInfo(PointSet node, Path path){
        this.node = node;
        shortestPathsList = new ArrayList<Path>();
        shortestPathsList.add(path);
        shortestPathLength = path.length();
    }

    public int getShortestPathLength(){ 
        return shortestPathLength;
    }
    
    public boolean isNode(PointSet p) {
    	return p.equals(node);
    }

    //compares p to the path information in the model.
    //if the length of p is the same as shortestPathLength, add p to shortestPathsList.
    //if the length of p is longer, do nothing with it.
    //if the length of p is shorter, replace shortestPathsList with a new list consisting of p,
    //and update shortestPathLength such that it is equal to p.length.
    //Returns true if p was added to the model, or false if p was not added because p.length > shortestPathLength.
    public boolean putShortestPath(Path p){   	
        int length = p.length();
        if (length == 0) return false;
        if (length > shortestPathLength) return false;

        if (length == shortestPathLength)
            shortestPathsList.add(p);

        if (length < shortestPathLength){
            shortestPathsList.clear();
            shortestPathsList.add(p);
            shortestPathLength = p.length();
        }
        return true;
    }

    public Iterator<Path> iterator(){
        return shortestPathsList.iterator();
    }

}