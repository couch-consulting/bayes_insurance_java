/* 
 *  MakeDecision.java
 *
 *  Example use of Netica-J to build a decision net and choose an optimal decision with it.
 *
 * Copyright (C) 1992-2007 by Norsys Software Corp.
 * The software in this file may be copied, modified, and/or included in 
 * derivative works without charge or obligation.
 * This file contains example software only, and Norsys makes no warranty that 
 * it is suitable for any particular purpose, or without defects.
 */
import norsys.netica.*;
import norsys.neticaEx.aliases.Node;
     
public class MakeDecision {

  public static void main (String[] args){
    try {
      Node.setConstructorClass ("norsys.neticaEx.aliases.Node");
      Environ env = new Environ (null);
      
      Net net = new Net();
      net.setName ("Umbrella");
      
      Node weather      = new Node ("Weather",     "sunshine,rain", net);
      Node forecast     = new Node ("Forecast",    "sunny,cloudy,rainy", net);
      Node umbrella     = new Node ("Umbrella",    "take_umbrella, dont_take_umbrella", net);
      Node satisfaction = new Node ("Satisfaction", 0, net);
      
      umbrella.setKind (Node.DECISION_NODE);
      satisfaction.setKind (Node.UTILITY_NODE);
      
      forecast.addLink (weather);
      umbrella.addLink (forecast);
      satisfaction.addLink (weather);
      satisfaction.addLink (umbrella);
      
      weather.setCPTable (0.7, 0.3);
	
      //                               forecast
      //                   weather   | sunny  cloudy  rainy
      forecast.setCPTable ("sunshine", 0.7,   0.2,    0.1);
      forecast.setCPTable ("rain",     0.15,  0.25,   0.6);
      
      //                             weather    umbrella
      satisfaction.setRealFuncTable ("sunshine, take_umbrella",       20.0);
      satisfaction.setRealFuncTable ("sunshine, dont_take_umbrella", 100.0);
      satisfaction.setRealFuncTable ("rain,     take_umbrella",       70.0);
      satisfaction.setRealFuncTable ("rain,     dont_take_umbrella",   0.0);
      
      net.compile();
      
      //--- 1st type of usage:  To get the expected utilities, given the current findings
      
      forecast.finding().enterState ("sunny");
      float[] utils = umbrella.getExpectedUtils();  // returns expected utilities, given current findings

      System.out.print   ("If the forecast is sunny,  ");
      System.out.println ("the expected utility of " + umbrella.state(0) + " is " + utils[0] + 
			  ", of "                    + umbrella.state(1) + " is " + utils[1] );
      
      net.retractFindings();
      forecast.finding().enterState ("cloudy");
      utils = umbrella.getExpectedUtils();
      
      System.out.print   ("If the forecast is cloudy, ");
      System.out.println ("the expected utility of " + umbrella.state(0) + " is " + utils[0] + 
			  ", of "                    + umbrella.state(1) + " is " + utils[1] + "\n");

      //--- 2nd type of usage:  To get the optimal decision table
      
      net.retractFindings();
      umbrella.getExpectedUtils();  // causes Netica to recompute decision tables, given current findings
                                      // (which in this case are no findings)

      for (int fs = 0;  fs < forecast.getNumStates();  ++fs){
	int[] parStates = new int[1];
	parStates[0] = fs;  // forecast is the parent of umbrella
	int[] decision = umbrella.getStateFuncTable (parStates, null);
	System.out.println ("If the forecast is "     + forecast.state (fs) +
			    ",\tthe best decision is " + umbrella.state (decision[0]));
      }

      net.finalize();  // free resources immediately and safely; not strictly necessary, but a good habit
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
