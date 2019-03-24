/* 
 *  ClassifyData.java
 *
 *  Example use of Netica-J for doing Naive Bayesian Classification.
 *
 * Copyright (C) 1992-2007 by Norsys Software Corp.
 * The software in this file may be copied, modified, and/or included in 
 * derivative works without charge or obligation.
 * This file contains example software only, and Norsys makes no warranty that 
 * it is suitable for any particular purpose, or without defects.
 */
import norsys.netica.*;
     
public class ClassifyData {

  public static void main (String[] args){
    try {
	Environ env = new Environ (null);
	env.setCaseFileDelimChar (','); //because file BreastCancer.cas is comma-delimited

	Net net = new Net (new Streamer ("Data Files/BreastCancer.dne"));

	Node malignancy               = net.getNode ("Malignancy");
	Node clumpThickness           = net.getNode ("ClumpThickness");
	Node uniformityOfCellSize     = net.getNode ("UniformityOfCellSize");
	Node uniformityOfCellShape    = net.getNode ("UniformityOfCellShape");
	Node marginalAdhesion         = net.getNode ("MarginalAdhesion");
	Node singleEpithelialCellSize = net.getNode ("SingleEpithelialCellSize");
	Node bareNuclei               = net.getNode ("BareNuclei");
	Node blandChromatin           = net.getNode ("BlandChromatin");
	Node normalNucleoli           = net.getNode ("NormalNucleoli");
	Node mitoses                  = net.getNode ("Mitoses");

	//-- Clear any CPT data
	NodeList nodes = net.getNodes();
	for (int n=0; n<nodes.size(); n++) {
	    nodes.getNode(n).deleteTables();
	}

	//-- Train net based on database of Wisconsin breast cancer study obtained 
	//-- from Dr. William H. Wolberg, University of Wisconsin Hospitals, Madison, Wisconsin, USA;
	//-- Dataset archived at: ftp://ftp.ics.uci.edu/pub/machine-learning-databases/breast-cancer-wisconsin/
        //-- Citation: William H. Wolberg and O.L. Mangasarian: "Multisurface method of 
        //-- pattern separation for medical diagnosis applied to breast cytology", 
        //-- Proceedings of the National Academy of Sciences, U.S.A., Volume 87, 
        //-- December 1990, pp 9193-9196.


	net.reviseCPTsByCaseFile (new Streamer ("Data Files/BreastCancer.cas"), nodes, 1.0);

	//-- Now perform a classification for a particular case:  10,3,3,2,2,10,4,1,2

	// clear current findings, calculated from learning
	
	for (int n=0; n<nodes.size(); n++) {
	    nodes.getNode(n).finding().clear();
	}

	clumpThickness.finding().enterReal (10.0);
	uniformityOfCellSize.finding().enterReal (3.0);
	uniformityOfCellShape.finding().enterReal (3.0);
	marginalAdhesion.finding().enterReal (2.0);
	singleEpithelialCellSize.finding().enterReal (2.0);
	bareNuclei.finding().enterReal (10.0);
	blandChromatin.finding().enterReal (4.0);
	normalNucleoli.finding().enterReal (1.0);
	mitoses.finding().enterReal (2.0);

	net.compile ();

	float belief = malignancy.getBelief("Malignant");       
	System.out.println ("\nThe probability of this cell being malignant is " + belief);

	net.finalize ();   // not strictly necessary, but a good habit
    }
    catch (Exception e) {
	e.printStackTrace();
    }
  }
}
