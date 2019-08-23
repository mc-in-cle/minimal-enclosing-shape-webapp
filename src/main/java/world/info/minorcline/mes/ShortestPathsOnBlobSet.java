package world.info.minorcline.mes;

/**
 * A special class.
 * The method BlobSet.getShortestPathsOnBlobSet returns an instance of this class
 * that consists of, for each blob x and y in a BlobSet B, all shortest paths between x and y
 * such that there is no blob z in B which obstructs the path.
 * 
 * @author M.C. September 2018
 */

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ShortestPathsOnBlobSet implements Iterable<Path>{

    List<Path> list;
    Map<PointSet, ShortestPathsInfo> shortestPathsMap;
    //Maps a blob to an object representing information about the shortest paths from this blob to other blobs.

    public ShortestPathsOnBlobSet(){
        list = new ArrayList<Path>();
        shortestPathsMap = new HashMap<PointSet, ShortestPathsInfo>();
    }

    public void addPath(PointSet from, PointSet to, Path path){
        list.add(path);
        addShortestPath(from, path);
        addShortestPath(to, path.reverse());
    }

   //If 'node' does not have any shortest path information, add it
   //Otherwise, attempt to add 'path' to the existing information about 'node'.
   //Returns true if path was added, or false if it was rejected because it was not
   //a shortest path for 'node'.
   private boolean addShortestPath(PointSet node, Path path){
        if (!shortestPathsMap.containsKey(node)){
            shortestPathsMap.put(node, new ShortestPathsInfo(node, path));
            return true;
        }
        else
            return shortestPathsMap.get(node).putShortestPath(path);
   }

   //if there is no information available for this blob, return empty iterator
   public Iterator<Path> iterateShortestPathsFrom(PointSet blob){
       if (!shortestPathsMap.containsKey(blob)) {
    	   List<Path> list = new ArrayList<>();
    	   return list.iterator();
       }
       else return shortestPathsMap.get(blob).iterator();
   }

    public Iterator<Path> iterator(){
       return list.iterator();
    }
    
}