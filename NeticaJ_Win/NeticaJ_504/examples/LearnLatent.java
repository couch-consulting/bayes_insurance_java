/* 
 *  LearnLatent.java
 *
 *  Example use of Netica-J for learning a latent (hidden) variable
 *  using the EM Learning technique.
 *
 * Copyright (C) 1992-2007 by Norsys Software Corp.
 * The software in this file may be copied, modified, and/or included in 
 * derivative works without charge or obligation.
 * This file contains example software only, and Norsys makes no warranty that 
 * it is suitable for any particular purpose, or without defects.
 */

import java.io.File;
import norsys.netica.*;

public class LearnLatent {

  public static void main(String[] args) {
    System.out.println( "Running Netica-J LearnLatent example..." );

    try {
	Environ env = new Environ (null);

	/* Build the net */
	Net      net      = new Net (env);
	net.setName ("Learned_Latent");

	Node     r        = new Node ("R", 2, net);
	Node     s        = new Node ("S", 2, net);
	Node     t        = new Node ("T", 2, net);
	r.state(0).setName ("false");
	r.state(1).setName ("true");
	s.state(0).setName ("false");
	s.state(1).setName ("true");
	t.state(0).setName ("false");
	t.state(1).setName ("true");

	Node     a        = new Node ("A", 2, net); // the latent node; trying 2 states
	//Node   a        = new Node ("A", 3, net); // the latent node; trying 3 states

	r.addLink (a);
	s.addLink (a);
	t.addLink (a);

	NodeList nodes    = net.getNodes();
	int      numNodes = nodes.size();

	// Read in the case file into a caseset

	Caseset cases = new Caseset ();
	Streamer caseFile = new Streamer ("Data Files/LearnLatent.cas");
	cases.addCases ( caseFile, 1.0, null);
	Learner learner = new Learner (Learner.EM_LEARNING);
	learner.setMaxIterations (200);
	
	learner.learnCPTs ( nodes, cases, 1.0);

	net.write (new Streamer ("Data Files/Learned_Latent.dne"));
	
	// the folowing are not strictly necessary, but a good habit
	learner.finalize();
	cases.finalize();
	net.finalize();   
    }
    catch (Exception e) {
	e.printStackTrace();
    }
  }
}

	/* ==============================================================
	 * This alternate way can replace the net.reviseCPTsByCaseFile
	 * line above, if you need to filter or adjust individual cases.
	 
	long[] casePosn = new long[1];
	casePosn[0] = Net.FIRST_CASE;
	while (true) {
	    net.retractFindings();
	    net.readCase (casePosn, caseFile, nodes, null, null);
	    if (casePosn[0] == Net.NO_MORE_CASES) break;
	    
	    net.reviseCPTsByFindings (nodes, 1.0);
	    casePosn[0] = Net.NEXT_CASE;
	}
	  ============================================================== */
	
