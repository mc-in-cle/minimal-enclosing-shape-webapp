package world.info.minorcline.mes;

/**
* Generates random flocculated pixels on a grid.
* Created for demonstrated Minimal Enclosing Shape.
* @author M.C. November 2018
**/

import java.util.Queue;
import java.util.List;
import java.util.Collections;
import java.util.LinkedList;

public class FlocculatedPointGenerator{
    private PointSet area;
    private double density;
    private double floc;
    
    public FlocculatedPointGenerator(PointSet bounds, double density, double flocculation){
        area = bounds;
        this.density = density;
        this.floc = flocculation;
    }

    public void generate(){
        PointSet calculated = new PointSet(area.getBounds());
        Queue<Point> toExplore = new LinkedList<>();
        toExplore.offer(getRandomPoint());

        while (!toExplore.isEmpty()){
            Point node = toExplore.poll();
            List<Point> neighbors = area.getNearbyCoordinatesWithinBounds(node, 1);
            Collections.shuffle(neighbors);
            
            for (Point p : neighbors){
                if (! calculated.hasPoint(p)){
                    double r = Math.random();
                    if (r < (area.hasPoint(node) ? floc : density)){
                        area.addPoint(p);
                    } else {
                        area.removePoint(p);
                    }
                    calculated.addPoint(p);
                    toExplore.offer(p);
                }
            }
        }
    }

    private Point getRandomPoint(){
        int x = (int)(Math.random() * area.getXBound());
        int y = (int)(Math.random() * area.getYBound());
        return new Point(x,y);
    }
    
    public PointSet getPointSet() {
    	return area;
    }
    
    public List<Point> getPointList(){
    	return area.pointsList();
    }

}