/**
 * An extension of norsys.netica.Net containing assorted convenience methods.
 * Users are welcome to cut-and-paste methods from this class into their
 * own extensions of norsys.netica.Net.
 *   WARNING: The methods in this class could change, so do not rely on them
 * always being here in future versions of Netica-J.  They might be promoted 
 * to the base class, revised, or removed entirely.
 */
package norsys.neticaEx;

import norsys.netica.*;
import java.util.*;

public class NetEx extends Net implements java.io.Serializable{

  //------------------------------------------------------------ constructors
  public NetEx() throws NeticaException {
      super();
  }

  public NetEx( Environ env ) throws NeticaException { 
      super (env);
  }

  public NetEx( Streamer inStream ) throws NeticaException { 
      super (inStream);
  }

  public NetEx( Net net, 
		String newName, 
		Environ newEnv, 
		String control ) throws NeticaException {
      super (net, newName, newEnv, control);
  }
  //------------------------------------------------------------
    

  /** 
   *  Make a copy of a net, giving it the name, newName.
   *  @param net      the net to duplicate
   *  @param newName  the name of the new net
   */
  public static Net duplicate(Net net, String newName) throws NeticaException {
    Net newNet = new Net (net.getEnviron());
    newNet.setName (newName);
    NodeList newNodes = newNet.copyNodes (net.getNodes());
       
    NodeList elimOrder = net.getElimOrder();
    NodeList newOrder =NodeListEx.mapNodeList (elimOrder, newNet);
    newNet.setElimOrder (newOrder);

    newNet.setAutoUpdate (net.getAutoUpdate());
    newNet.setTitle (net.getTitle());
    newNet.setComment (net.getComment());
    newNet.user().setReference (net.user().getReference()); // If desired
    return newNet;
  }

 
  /** 
   *  A convenience routine based on Node.enterFinding( String stateName ).
   *  @param the name of the node in this net whose finding you wish to set.
   *  @param stateName the name of the state that this node is believed to be in.
   */
  public void enterFinding( String nodeName, String stateName ) throws NeticaException {
      Node node = getNode ( nodeName);
      node.finding().enterState (stateName);
  }

  /** 
   *  Find a net in the given Environ by it's name. Comparison must be an exact match and 
   *  is case sensitive.  Returns the first net that matches, or null, if there was no match.
   *  @param  name The name of the net sought.
   *  @param  env  The Environ to search within.
   *  @returns The net with that name, or null, if not found.
   */
  public static Net getNetNamed (String name, Environ env) throws NeticaException { 
      synchronized (env) {
	  Vector nets = Net.getAllNets( env );
	  Enumeration enum2 = nets.elements();
	  while (enum2.hasMoreElements()) {
	      Net net = (Net) enum2.nextElement();
	      if (net.getName().equals (name)) {
		  return net;
	      }
	  }
      }
      return null;
  }

  /** 
   *  Find a node by name.  Like getNode(String nodeName), except that
   *  throws a NeticaException if the name doesn't exist.
   *  @param  name The name of the node sought.
   *  @returns The node with that name.
   */
  public Node getExistingNode (String nodeName) throws NeticaException{
      Node node = getNode (nodeName);
      if (node == null){
	  throw new NeticaException ("There is no node named " + nodeName +
				     " in net " + getName());
      }
      return node;
  }

  /** 
   *  Returns a list of all the nodes in this net, with the exception 
   *  of those nodes that are of kind: Node.CONSTANT_NODE. 
   */
  public NodeList getNonConstantNodes () throws NeticaException{
      NodeList nodes = getNodes();
      NodeList nc_nodes = new NodeList (this);

      Enumeration enum2 = nodes.elements();
      while (enum2.hasMoreElements()) {
	  Node node = (Node) enum2.nextElement();
	  if (node.getKind() != Node.CONSTANT_NODE) {
	      nc_nodes.add (node);
	  }
      }
      return nc_nodes;
  }

  /** 
   *  Returns a list of all the nodes in the given Nodeset. 
   */
  public static NodeList getNodeset ( Net net, String nodesetName ) throws NeticaException{
      NodeList nodes = net.getNodes();
      NodeList ns_nodes = new NodeList (net);

      Enumeration enum2 = nodes.elements();
      while (enum2.hasMoreElements()) {
	  Node node = (Node) enum2.nextElement();
	  if (node.isInNodeset(nodesetName)) {
	      ns_nodes.add (node);
	  }
      }
      return ns_nodes;
  }


}

