package com.barrenlandanalysis.impl;

import com.barrenlandanalysis.service.*;
import com.barrenlandanalysis.domain.*;
import com.barrenlandanalysis.constants.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author megha
 *
 */
public class BarrenLandServiceImpl implements BarrenLandService{


    private static FarmCoordinate[][] farm = new FarmCoordinate[Constants.WIDTH][Constants.HEIGHT];
    
    /**
     * Function to print input instructions
     */
    @Override
    public void printInstructions() {
    	System.out.println("You are given a set of rectangles that contain the barren land. "
				+ "These rectangles are defined in a string, which consists of four integers"
				+ " separated by single spaces, with no additional spaces in the string. "
				+ "The first two integers are the coordinates of the bottom left corner in the given rectangle, "
				+ "and the last two integers are the coordinates of the top right corner.");
		System.out.println("Example input: 48 192 351 207, 48 392 351 407,"
				+ "120 52 135 547, 260 52 275 547");

    }
    
     /**
      * Function to handle any input errors
      */
	 private void handleError() {
		 System.out.println("ERROR  : Invalid coordinates for barren land.");
		 printInstructions();
     }

    /**
     * Wrapper function to take the input and call multiple helpers
     * @param barrenRectangleArray
     * @return String which Outputs all the fertile land area in square meters, 
     * sorted from smallest area to greatest, separated by a space.
     */
	@Override
    public String findFertileLand(String[] barrenLandRectangleArray) {
    	
    	if(barrenLandRectangleArray == null || barrenLandRectangleArray.length == 0) {
    		handleError();
    	}
    	
        List<Integer> fertileLand = new ArrayList<Integer>();

        findFertileLand(barrenLandRectangleArray, fertileLand);
        
    	StringBuilder fertileAreas = new StringBuilder();              
		for(Integer f : fertileLand){
		  fertileAreas.append(f.toString()).append(Constants.DELIMITER);
		}
		
		return fertileAreas.toString().trim();

    }
    
    /**
     * Populate a farm with marked with barren lands and sorts the list of calculated fertile areas
     * @param barrenRectangleArray
     * @param fertileLandList
     */ 
    private void findFertileLand(String[] barrenRectangleArray, 
            List<Integer> fertileLandList) {
    	
          List<FarmCoordinate> totalBarrenLand = getBarrenLandCoordinates(barrenRectangleArray);

          //build a farm with barren land 
          markBarrenLand(totalBarrenLand);

          fertileLandList = getFertileAreasList(fertileLandList, 0, 0);

          Collections.sort(fertileLandList);

    }
    
    /**
     * Populate a barren land rectangle with all the coordinates in that space
     * @param barrenRectangleArray
     * @return List of coordinates in barren land rectangle
     */
    private List<FarmCoordinate> getBarrenLandCoordinates(String[] barrenRectangleArray) {

    	List<FarmCoordinate> totalBarrenLand = new ArrayList<FarmCoordinate>();
    	
    	if(barrenRectangleArray.length == 0) {
			handleError();
		}
    	
    	FarmCoordinate coord ;
       
    	for(String barrenRectangle: barrenRectangleArray) {
			
			String [] coordinates = barrenRectangle.trim().split(Constants.DELIMITER);
			
			if (coordinates.length != Constants.COORDINATE_COUNT_PER_STRING) {
				handleError();
			}
			
			int bottomLeftX=0,bottomLeftY=0,topRightX=0,topRightY=0;
			
			try {
					bottomLeftX = Integer.parseInt(coordinates[Constants.BOTTOM_LEFT_X_INDEX]);
					bottomLeftY = Integer.parseInt(coordinates[Constants.BOTTOM_LEFT_Y_INDEX]);
					topRightX = Integer.parseInt(coordinates[Constants.TOP_RIGHT_X_INDEX]);
					topRightY = Integer.parseInt(coordinates[Constants.TOP_RIGHT_Y_INDEX]);
					
					checkCoordinates(bottomLeftX,bottomLeftY,topRightX,topRightY);
			}
			
			catch(IllegalArgumentException ex) {
				handleError();
			}
			
			for(int x = bottomLeftX; x <= topRightX ; x++) {
				for (int y = bottomLeftY ; y <= topRightY ; y++) {
					 coord = new FarmCoordinate(x, y);
					 totalBarrenLand.add(coord);
					
				}
			}
			
    	}
    	
    	return totalBarrenLand;
    }
   
    /**
     * Initializes the farm with the barren land marked
     * @param totalBarrenLand
     */
    private void markBarrenLand(List<FarmCoordinate> totalBarrenLand) {
    	FarmCoordinate co;
    	for (int y = 0; y < Constants.HEIGHT; y++) {
            for (int x = 0; x < Constants.WIDTH; x++) {
                co = new FarmCoordinate(x, y);
                //for each coordinate, if it's present in the totalBarrenLand list
                for (FarmCoordinate c : totalBarrenLand) {
                    if (c.getX() == x && c.getY() == y) {
                        co.setBarren(true);
                        co.setVisited(true);
                        break;
                    } else {
                        co.setBarren(false);
                    }
                }
                farm[x][y] = co;
            }
        }
    }
    /**
     * Check through the farm, find first unvisited point, flood fill the fertile area directly connected to that point, and return the total area
     * @param land
     * @param xVal
     * @param yVal
     * @return List of area of each fertile land plot
     */
    private List<Integer> getFertileAreasList(List<Integer> land, int xVal, int yVal) {
    	
        for (int y = yVal; y < Constants.HEIGHT; y++) {
            for (int x = xVal; x < Constants.WIDTH; x++) {
                FarmCoordinate tile = farm[x][y];
                if (!tile.isVisited()) {
                    int totalFertileArea = visitNeighbours(farm, x, y);
                    land.add(totalFertileArea);
                    getFertileAreasList(land, x, y);
                }
            }
        }
        return land;

    }

    /**
     * Visit all coordinates in a fertile land space and find the area
     * @param farm
     * @param x
     * @param y
     * @return area of the current fertile land 
     */
    private int visitNeighbours(FarmCoordinate[][] farm, int x, int y) {
        
    	// Counter for farm coordinates visited
    	int count = 0; 

        Queue<FarmCoordinate> queue = new LinkedList<FarmCoordinate>();
        queue.offer(new FarmCoordinate(x, y));

        while (!queue.isEmpty()) {
            FarmCoordinate c = queue.poll();

        //If Coordinate c is unvisited, visit it, increase count by 1, and add neighbors to the queue;
            if(!isCoordinateVisited(farm, c)) {     
                count += 1;
                    if ( c.getY() -1 >= 0 && !farm[c.getX()][c.getY() - 1].isVisited() ) {
                        queue.offer(new FarmCoordinate(c.getX(), c.getY() - 1));
                    }
                    if (c.getY() +1 < Constants.HEIGHT && !farm[c.getX()][c.getY() + 1].isVisited()) {
                        queue.offer(new FarmCoordinate(c.getX(), c.getY() + 1));
                    }
                    if ( c.getX() -1 >= 0 && !farm[c.getX() - 1][c.getY()].isVisited()) {
                        queue.offer(new FarmCoordinate(c.getX() - 1, c.getY()));
                    }
                    if (c.getX() +1 < Constants.WIDTH && !farm[c.getX() + 1][c.getY()].isVisited()) {
                        queue.offer(new FarmCoordinate(c.getX() + 1, c.getY()));
                    }
            }

        }
        return count;
    }

    /**
     * Check if coordinate has been visited already - if not, switch visited to true
     * @param farm
     * @param c
     * @return boolean value representing whether coordinate c has been visited or not
     */
    private boolean isCoordinateVisited(FarmCoordinate[][] farm, FarmCoordinate c) {

        // Check for boundary conditions
        if (c.getX() < 0 || c.getY() < 0 || c.getX() >= Constants.WIDTH || c.getY() >= Constants.HEIGHT) {
            return false;
        }

        FarmCoordinate coordinateToCheck = farm[c.getX()][c.getY()];

        if (coordinateToCheck.isVisited()) {
            return true;
        }

        coordinateToCheck.setVisited(true);

        return false;
    }
    
    
    /**
	 * Input Checks for coordinates
	 * @param bottomLeftX
	 * @param bottomLeftY
	 * @param topRightX
	 * @param topRightY
	 */
	private void checkCoordinates(int bottomLeftX,int bottomLeftY,int topRightX,int topRightY) {
		
		if (bottomLeftX < 0 || bottomLeftY < 0 || topRightX < 0 || topRightY < 0) {
			
			throw new IllegalArgumentException();
		}
		
		if(bottomLeftX >= topRightX || bottomLeftY >= topRightY) {
			throw new IllegalArgumentException();
		}
		
		if (bottomLeftX > Constants.WIDTH-1 || topRightX > Constants.WIDTH-1 || bottomLeftY > Constants.HEIGHT-1 || topRightY > Constants.HEIGHT-1 ) {
			throw new IllegalArgumentException();
		}
	}
    
}