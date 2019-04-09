import norsys.netica.*;
import norsys.netica.gui.*;
import javax.swing.*;

/**
 * Application to display a single Netica net.
 * Usage: java DrawNet <filePath>
 * Example: java  DrawNet ChestClinic.dne
 *
 * Copyright (C) 1992-2007 by Norsys Software Corp.
 * The software in this file may be copied, modified, and/or included in 
 * derivative works without charge or obligation.
 * This file contains example software only, and Norsys makes no warranty that 
 * it is suitable for any particular purpose, or without defects.
 */
class DrawNet extends JFrame{

    public DrawNet( String netName ) throws Exception {
	Net net = new Net( new Streamer( netName ) );
	net.compile();  //optional
	NetPanel netPanel = new NetPanel( net, NodePanel.NODE_STYLE_AUTO_SELECT );
	getContentPane().add( new JScrollPane(netPanel ) ); //adds the NetPanel to ourself
	setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	setSize( 800, 500 ); // or supply getPreferredSize();
	setVisible(true);
    }

    public static void main( String[] args ) {
	try {
	    Environ env = new Environ( null );
	    DrawNet dn = new DrawNet( args.length == 0 ? "ChestClinic_WithVisuals.dne" : args[0] );
	}
	catch ( Exception e ) {
	    e.printStackTrace();
	}
    }
}
