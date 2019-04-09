package netviewer;

import norsys.netica.*;
import norsys.netica.gui.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*; 
import javax.swing.filechooser.*;

/**
 * Application to allow selection of Netica net files from a directory
 * and then display them using a variety of display styles.
 * Usage: java NetViewer <dirName>
 * Example: java NetViewer .
 *
 * Copyright (C) 1992-2007 by Norsys Software Corp.
 * The software in this file may be copied, modified, and/or included in 
 * derivative works without charge or obligation.
 * This file contains example software only, and Norsys makes no warranty that 
 * it is suitable for any particular purpose, or without defects.
 */
class NetViewer extends JFrame{

    static NetPanel curNetPanel = null;
    static File dirFile; //the start directory for listing nets
    Color bgColor = Color.white;
    Color menuBgColor = new Color( 255, 250, 220 ); //cream
    static int defaultNodeStyle = NodePanel.NODE_STYLE_AUTO_SELECT;
    static int defaultLinkPolicy = NetPanel.LINK_POLICY_BELOW;
    NetAndControlsPanel nacp = null;
    static Frame frame;

    public NetViewer() throws Exception {
	frame = (Frame) this;
	
        JMenuBar menuBar = new JMenuBar();
	menuBar.setBackground( menuBgColor );
       
	//-------------------- File Menu
	JMenu fileMenu = (JMenu) menuBar.add(new JMenu("File"));
	fileMenu.setBackground( null );

	JMenuItem openItem = (JMenuItem) fileMenu.add(new JMenuItem("Open..."));
	openItem.setBackground( menuBgColor );
	openItem.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) { showOpenDialog(); }});

	JMenuItem printItem = (JMenuItem) fileMenu.add(new JMenuItem("Print"));
	printItem.setBackground( menuBgColor );
	printItem.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent ae) { 
		if ( curNetPanel != null ) {
		    Toolkit tk = Toolkit.getDefaultToolkit();
		    String netName = "";
		    try {
			netName = curNetPanel.getNet().getTitle();
			if ( (netName == null) ||
			     (netName.equals("") ) ) netName = curNetPanel.getNet().getName();
		    }
		    catch (Exception e) {}
                    PageAttributes pa = new PageAttributes();
		    if ( curNetPanel.getWidth() > curNetPanel.getHeight() )
			 pa.setOrientationRequested( PageAttributes.OrientationRequestedType.LANDSCAPE );
		    else pa.setOrientationRequested( PageAttributes.OrientationRequestedType.PORTRAIT  );
		    tk.getPrintJob( frame, netName, null, pa );
		}
	    }});

	JMenuItem exitItem = (JMenuItem) fileMenu.add(new JMenuItem("Exit"));
	exitItem.setBackground( menuBgColor );
	exitItem.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) { System.exit(0); }});

	//-------------------- Help Menu
        JMenu helpMenu  = (JMenu) menuBar.add(new JMenu("Help"));
	helpMenu.setBackground( null );

        JMenuItem helpItem = (JMenuItem) helpMenu.add(new JMenuItem("Help"));
	helpItem.setBackground( menuBgColor );
        helpItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { showHelpDialog(); }});

        JMenuItem aboutItem = (JMenuItem) helpMenu.add(new JMenuItem("About"));
	aboutItem.setBackground( menuBgColor );
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { showAboutDialog(); }});

        getContentPane().add(menuBar, BorderLayout.NORTH);
	getContentPane().setBackground( bgColor );
	//getContentPane().setOpaque( false );

	setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	setSize( 800, 600 );
	// experimental: RepaintManager.currentManager(getContentPane()).setDoubleBufferingEnabled(false);
	setVisible(true);
    }

    public static void main( String[] args ) {
	String dirName = ".";
	if (args.length > 0) dirName = args[0];

	//-- choose some of your preferred defaults
	NetPanel.defaultLinkPolicy = NetPanel.LINK_POLICY_BELOW;
	NodePanel.probabilitiesFormat = NodePanel.PROBABILITY_FORMAT_2; //eg., 0.01 
	//NodePanel.probabilitiesFormat = NodePanel.PROBABILITY_FORMAT_4; //eg., 0.01 

	try {
	    dirFile = new File(dirName);
	    if ( (dirFile == null)  ||
		 !dirFile.isDirectory()  ) {
                System.err.println("Usage: java NetViewer <netDirectory>");
		return;
	    }
	    Environ env = new Environ( null );
	    NetViewer dn = new NetViewer();
	}
	catch ( Exception e ) {
	    e.printStackTrace();
	}
    }

    void loadNet( String netPath ) throws Exception {
	if (curNetPanel != null ) { //-- remove old
	    getContentPane().remove( curNetPanel );
	}

	Net net = new Net( new Streamer( netPath ) );
	try {
	    net.compile();
	}
	catch (NeticaException ne) {
	    String msg = "The net, " + netPath + 
		", could not be compiled, because " + 
		ne.getMessage();
	    System.out.println( msg );
	    JOptionPane.showMessageDialog(this, msg );
	}
	curNetPanel = new NetPanel( net, defaultNodeStyle );
	//-- setup listeners on all the net's nodes
	curNetPanel.addListenerToAllComponents( new MyMouseInputAdapter() );
	//curNetPanel.addEventListenerToAllComponents( new MyMouseMotionAdapter() );
    
	loadPanel( curNetPanel );
	System.out.println("New net that was loaded : "+ netPath);
    }

    void loadPanel( NetPanel np ){
	if (np != null) {
	    if (nacp != null) remove(nacp); //remove old component
	    nacp = new NetAndControlsPanel( np );
	    getContentPane().add( nacp, BorderLayout.CENTER );
	    getContentPane().validate();
	    repaint();
	}
    }

    void showAboutDialog(){
    }
    void showHelpDialog(){
    }
    void showOpenDialog(){

	JFileChooser chooser = new JFileChooser(dirFile);        
	//chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

	NeticaFilter filter = new NeticaFilter();
	chooser.setFileFilter(filter);
	//int returnVal = chooser.showOpenDialog(null);
	int returnVal = chooser.showOpenDialog(this);
	if(returnVal == JFileChooser.APPROVE_OPTION) {
	    System.out.println("\nYou chose to open this file: " +
			       chooser.getSelectedFile().getName());
	    try{
		loadNet( chooser.getSelectedFile().getAbsolutePath() );
	    }catch (Exception e) {
		JOptionPane.showMessageDialog(this, e.getMessage());
		 e.printStackTrace();
		loadPanel( curNetPanel );//restore previous
	    }
	}
    }
}
//Java 1.4 required for this
class NeticaFilter extends javax.swing.filechooser.FileFilter {
    String description;

    //Accept all directories and all neta, dne, and dnet extension files
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = getExtension(f.getName());
        if (extension != null) {
            if (extension.equals("neta") ||
                extension.equals("dne")  ||
                extension.equals("dnet")   ) {
                    return true;
            } else {
                return false;
            }
        }

        return false;
    }

    public String getDescription() {
        return ".dne, .dnet, .neta : Netica format nets";
    }
    public String getExtension(String fileName) {
	int pos = fileName.lastIndexOf('.');
	if (pos<0) 
	    return "";
        else 
	    return  fileName.substring(pos+1);
    }
}

class NetAndControlsPanel extends JPanel {

    NetAndControlsPanel( NetPanel np) {
	setLayout( new BorderLayout() );
	setBackground( new Color( 240, 255, 240 ) );
	setBorder( BorderFactory.createLoweredBevelBorder() );
	JPanel controlsPanel = new JPanel( new BorderLayout() );
	controlsPanel.add( new StylesChooser(np), BorderLayout.NORTH );
	controlsPanel.add( new LinkChooser(np), BorderLayout.SOUTH );
	add( controlsPanel, BorderLayout.NORTH );
	add( new JScrollPane(np), BorderLayout.CENTER );
    }
}

/*  NodePanel.
    public static final int  NODE_STYLE_AUTO_SELECT = 0; // Netica-J chooses an appropriate style 
    public static final int  NODE_STYLE_LABELED_BOX = 1;
    public static final int  NODE_STYLE_CIRCLE      = 2;
    public static final int  NODE_STYLE_ABSENT      = 3;
    public static final int  NODE_STYLE_BELIEF_BARS = 4;
    public static final int  NODE_STYLE_TEXT        = 5;
*/
class StylesChooser extends JPanel implements ItemListener {
    NetPanel np;
    Checkbox cb[] = new Checkbox[6];

    public StylesChooser( NetPanel np ) {
	this.np = np;
	setBackground( new Color( 240, 255, 240 ) );
	setBorder( BorderFactory.createRaisedBevelBorder() );
	CheckboxGroup cbg = new CheckboxGroup();
	add( new JLabel("Node Styles: ") );

	String titles[] = { "Auto Select", "Belief Bars", "Labeled Box", "Circle", "Text", "Hidden" };
	for (int i=0; i< cb.length; i++) {
	    cb[i] = new Checkbox( titles[i], cbg, (i==NetViewer.defaultNodeStyle) );
	    cb[i].addItemListener(this);
	    cb[i].setBackground( null ); // take parent's bg
	    add( cb[i] );
	}
    }

    public void itemStateChanged(ItemEvent ie) {
	Object obj = ie.getItem();
	if ( (obj instanceof String) &&
	     (ie.getStateChange() == ItemEvent.SELECTED) ) {
	    String s = (String) obj;
	    try {
		NodeList selectedNodes = np.getNodesWithDisplayMode( NodePanel.DISPLAY_MODE_HILITED );
		if ( selectedNodes.size() == 0 ) selectedNodes = null;

		if ( s.equals( "Auto Select" ) ) np.setNodeStyle ( selectedNodes, NodePanel.NODE_STYLE_AUTO_SELECT ); else
		if ( s.equals( "Belief Bars" ) ) np.setNodeStyle ( selectedNodes, NodePanel.NODE_STYLE_BELIEF_BARS );  else
		if ( s.equals( "Labeled Box" ) ) np.setNodeStyle ( selectedNodes, NodePanel.NODE_STYLE_LABELED_BOX ); else
	        if ( s.equals( "Circle"      ) ) np.setNodeStyle ( selectedNodes, NodePanel.NODE_STYLE_CIRCLE );      else
	        if ( s.equals( "Text"        ) ) np.setNodeStyle ( selectedNodes, NodePanel.NODE_STYLE_TEXT );        else
	        if ( s.equals( "Hidden"      ) ) np.setNodeStyle ( selectedNodes, NodePanel.NODE_STYLE_ABSENT );      else
		  JOptionPane.showMessageDialog(this, "Invalid Style: " + s );
	    }
	    catch (Exception e) {
		JOptionPane.showMessageDialog(this, e.getMessage() );
	    }
	}
    }

}

class LinkChooser extends JPanel implements ItemListener {
    NetPanel np;
    public LinkChooser( NetPanel np ) {
	this.np = np;
	setBackground( new Color( 255, 240, 240 ) );
	setBorder( BorderFactory.createRaisedBevelBorder() );
	CheckboxGroup cbg = new CheckboxGroup();
	add(new JLabel("Links: "));

	Checkbox cb[] = new Checkbox[3];
	String titles[] = { "None", "Below", "Above" };
	for (int i=0; i< cb.length; i++) {
	    cb[i] = new Checkbox( titles[i], cbg, (i==NetViewer.defaultLinkPolicy) );
	    cb[i].addItemListener(this);
	    cb[i].setBackground( null ); // take parent's bg
	    add( cb[i] );
	}
	cb[2].setEnabled( false );// since "above" is not working yet.
    }

    public void itemStateChanged(ItemEvent ie) {
	Object obj = ie.getItem();
	if ( (obj instanceof String) &&
	     (ie.getStateChange() == ItemEvent.SELECTED) ) {
	    String s = (String) obj;
	    if ( s.equals( "None"  ) ) np.setLinkPolicy( NetPanel.LINK_POLICY_NONE  ); else
	    if ( s.equals( "Below" ) ) np.setLinkPolicy( NetPanel.LINK_POLICY_BELOW ); else
	    if ( s.equals( "Above" ) ) np.setLinkPolicy( NetPanel.LINK_POLICY_ABOVE ); else
		JOptionPane.showMessageDialog(this, "Invalid Link Policy: " + s );
	}
    }
}

//-----------------------------------------------------
class MyMouseInputAdapter extends MouseInputAdapter {
    Point pressPt;
    boolean dragged;
    
    public void mouseClicked(MouseEvent me) {
	Component comp = me.getComponent();
	//System.out.println( "Mouse clicked on " + comp.getClass().getName() ); 
	try { 
	    if (comp instanceof JLabel) comp = comp.getParent(); //pass on to containing NodePanel
	    
	    if (comp instanceof NodePanel ) {
		NodePanel np = (NodePanel) comp;
		//System.out.println( "Mouse clicked on NodePanel for node " + np.getNode().getName() ); 
	    
		if ( me.paramString().indexOf( "button=1" ) >= 0 ) { //-- left button was clicked
		    if (np.getDisplayMode() == NodePanel.DISPLAY_MODE_NORMAL ) {
			np.setDisplayMode( NodePanel.DISPLAY_MODE_HILITED );
		    } else
		    if (np.getDisplayMode() == NodePanel.DISPLAY_MODE_HILITED ) {
			np.setDisplayMode( NodePanel.DISPLAY_MODE_NORMAL );
		    }
		}
		else
		if ( (me.paramString().indexOf( "button=2" ) >= 0 ) ||
		     (me.paramString().indexOf( "button=3" ) >= 0 )   ){ //-- middle or right button was clicked
		    //... handle right/middle button click as desired; for now, just log event to console
		    //System.out.println( "Right mouse clicked on " + comp.getClass().getName() ); 
		   }
	    }
	    else if (comp instanceof NodePanel_BeliefBarsRow) {
		NodePanel_BeliefBarsRow npbbr = (NodePanel_BeliefBarsRow) comp;
		/*
		System.out.println( "Mouse clicked on BBR for state " + 
				    npbbr.getState().getIndex() + 
				    " of node " + npbbr.getState().getNode().getName() );
		*/
		int indexOfStateClicked = npbbr.getState().getIndex();
		Value finding = npbbr.getState().getNode().finding();
		if ( finding.getState() == indexOfStateClicked) {
		    //-- clicking on already selected state
		    finding.clear();
		}
		else {
		    finding.setState( indexOfStateClicked );
		}
		try{
		    NetViewer.curNetPanel.getNet().compile();
		}catch (Exception e) {
		    JOptionPane.showMessageDialog( comp, e.getMessage() );
		}
		NetViewer.curNetPanel.refreshDataDisplayed();
	    } 
	    else if (comp instanceof LinkGraphic) {
		LinkGraphic link = (LinkGraphic) comp;
		System.out.println( "Mouse clicked on link: " + link + ", at point " + me.getPoint() );
		if ( link.getDisplayMode() == LinkGraphic.DISPLAY_MODE_NORMAL ) {
		    link.setDisplayMode( LinkGraphic.DISPLAY_MODE_HILITED );
		    //link.setLineWidth( 3 ); // basic stroke, which will work fine, but...
		    // ... you can go wild too!! 
		    float[] dash = {10.0F, 5.0F};
		    link.setStroke( new BasicStroke( 3.0F,
						     BasicStroke.CAP_ROUND,
						     BasicStroke.JOIN_ROUND,
						     1.0F,
						     dash,
						     0.0F ) );
		     //just for fun, if it is Button #2 or #3, move the link's endpoint
		    if ( me.getButton() != MouseEvent.BUTTON1 ) {
			Point start = link.getStartPoint();
			link.setEndPoint( me.getPoint() );
		    }
		    NetViewer.curNetPanel.repaint();
		}
		else {
		    link.setDisplayMode( LinkGraphic.DISPLAY_MODE_NORMAL );
		    //link.setLineWidth( 1 ); // for use with basic strokes only. However, if you are playing
		    // with custom Strokes, as above, then you will likely want to reset to a totally new BasicStroke.
		    link.setStroke( new BasicStroke( 1.0F ) );
		    NetViewer.curNetPanel.refreshDataDisplayed(); //NB this resets Link coords to match underlying Nodes
		}
	    }
	}
	catch (Exception e2) {
		JOptionPane.showMessageDialog( comp, e2.getMessage() );
		//e2.printStackTrace(); //TBD
	}
    }

    public void mousePressed(MouseEvent me) {
	pressPt   = me.getPoint();  //in component coods
	System.err.println( "Mouse pressed  at " + pressPt ); 
	Component comp = me.getComponent();
	if (comp instanceof LinkGraphic) {
	    System.err.println( "Mouse pressed on link " + ((LinkGraphic) comp) ); 
	}
    }

    public void mouseReleased(MouseEvent me) {
	System.err.println( "Mouse released at " + me.getPoint() ); 
	try { 
	    if ( dragged ) { //end of a drag operation, so update link-arrows
		NetViewer.curNetPanel.refreshDataDisplayed();
	    }
	}
	catch (Exception e) {
	    e.printStackTrace();  //TBchanged
	}
	pressPt = null;
	dragged = false;
    }

    public void mouseDragged(MouseEvent me) {
	Component comp = me.getComponent();
	if (comp instanceof JLabel) comp = comp.getParent(); //pass on to containing NodePanel
	Point pt = me.getPoint();; 
	//System.err.println( "Mouse dragged at " + pt + ", on object "+ comp.getClass().getName());
	dragged = true;
	try { 
	    if (comp instanceof NodePanel ) {
		NodePanel np = (NodePanel) comp;
		//System.out.println( "Mouse dragged on NodePanel for node " + np.getNode().getName() );
		Point compLoc2   = comp.getLocationOnScreen();
		np.moveBy(pt.x - pressPt.x, 
			  pt.y - pressPt.y);
	    }
	}
	catch (Exception e) {
		e.printStackTrace();  //TBchanged
	}
    }
    
    /*
    public void mouseMoved(MouseEvent e){
    }
    */ 
}
