/* 
 *  SmulateCases.java
 *
 *  Example use of Netica-J for generating random cases that follow
 *  the probability distribution given by a Bayes net.
 *
 * Copyright (C) 1992-2007 by Norsys Software Corp.
 * The software in this file may be copied, modified, and/or included in 
 * derivative works without charge or obligation.
 * This file contains example software only, and Norsys makes no warranty that 
 * it is suitable for any particular purpose, or without defects.
 */

import java.io.File;
import norsys.netica.*;

public class SimulateCases {

  public static void main(String[] args) {
    int numCases = 200;
    System.out.println( "Creating " + numCases + " random cases..." );

    try {
	Environ env = new Environ (null);

	// Read in the net created by the BuildNet.java example program
	Net        net = new Net (new Streamer ("Data Files/ChestClinic.dne"));
	NodeList nodes = net.getNodes();
	
	(new File("Data Files/ChestClinic.cas")).delete(); // since it may exist from a previous run and we do not wish to append
	Streamer caseFile = new Streamer ("Data Files/ChestClinic.cas");

	net.compile();  

	for (int n=0; n < numCases; n++) {
	    net.retractFindings();
	    int res = net.generateRandomCase (nodes, 0, 20, null);
	    if (res >= 0) {
		net.writeFindings (caseFile, nodes,  n, -1.0);
	    }
	}

	net.finalize();   // not strictly necessary, but a good habit
    }
    catch (Exception e) {
	e.printStackTrace();
    }
  }
}

