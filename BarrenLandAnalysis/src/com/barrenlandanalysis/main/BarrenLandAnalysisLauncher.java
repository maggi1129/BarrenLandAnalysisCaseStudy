package com.barrenlandanalysis.main;

import com.barrenlandanalysis.impl.BarrenLandServiceImpl;
import com.barrenlandanalysis.service.BarrenLandService;
import java.util.*;  

/**
 * @author megha
 *
 */
public class BarrenLandAnalysisLauncher {
	
	  public static void main(String[] args) {
	        //String[] STDIN = {"0 292 399 307"};
		   //String[] STDIN = {"48 192 351 207", "48 392 351 407", "120 52 135 547", "260 52 275 547"};
		
		   BarrenLandService barrenService = new BarrenLandServiceImpl();
		 
		   barrenService.printInstructions();
		   
		   Scanner sc = new Scanner(System.in);  
		   System.out.print("Enter the coordinates: ");  
		   String input = sc.nextLine();
		   String[] STDIN = input.split(",");
		   sc.close();     
		
		   long startTime = System.currentTimeMillis();

		   System.out.println("Fetching the results: ");
		   		   
		   String STDOUT = barrenService.findFertileLand(STDIN);
		   
		   long elapsedTime = System.currentTimeMillis() - startTime;
		   
		   System.out.println(STDOUT);
		   
		   System.out.println("Time taken in seconds: "+elapsedTime/(1000));

	    }


}
