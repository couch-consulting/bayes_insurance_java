/*
 * An extension of norsys.netica.NodeList containing assorted convenience methods.
 * Users are welcome to cut-and-paste methods from this class into their
 * own extensions of norsys.netica.NodeList.
 *   WARNING: The methods in this class could change, so do not rely on them
 * always being here in future versions of Netica-J.  They might be promoted 
 * to the base class, revised, or removed entirely.
 */
package norsys.neticaEx;

import norsys.netica.*;

public class NodeListEx extends NodeList{

  //------------------------------------------------------------ constructors
  public NodeListEx (Net parentNet) throws NeticaException {
    super (parentNet);
  }
  public NodeListEx (NodeList nodes) throws NeticaException {
    super (nodes);
  }
  //------------------------------------------------------------

  /** 
   *  return a NodeList whose nodes come from destNet and whose nodes have the same names
   *  and order as those within ourself.
   *  @param destNet the net to use for selecting nodes to include in the output NodeList
   */
  public static NodeList mapNodeList (NodeList nodes, Net destNet) throws NeticaException {
      if (nodes == null) return null;
      NodeList newNodes = new NodeList (destNet);
      for (int n=0; n<nodes.size(); n++) {
	  Node oldNode = (Node) nodes.get (n);
	  Node newNode = destNet.getNode (oldNode.getName());
	  newNodes.add (newNode);
      }

      return newNodes;
  }

  /** 
   *  This  cycles through all possible configurations (i.e. elements of the cartesian
   *  product) of states, odometer style, with the last state changing fastest.
   *  states is a list of node states, one for each node of nodeList.
   *  It returns 'true' when all the configurations have been examined (i.e., when it
   *  "rolls over" to all zeros again).
   *  Don't forget to initialize states before calling it the first time (usually 
   *    to all zeros).
   *  @param states  A set of state indices, one per node in nodeList, and in the same 
   *                 order as the nodes in nodeList.
   *  @param nodeList  A set of nodes, whose states are to be cycled through.
   */
  public static boolean nextStates (int[] states, NodeList nodeList) throws NeticaException{
      int n;
      for (n = nodeList.size() - 1;  n >= 0;  n--){
	  Node node = (Node) nodeList.get(n);
	  if (++states[n] < node.getNumStates())
	      return false;
	  states[n] = 0;
      }
      return true;
  }

  /** 
   *  Returns the size of the cartesian product of the states of the given nodes in nodeList,
   *  or 0 if one the nodes is continuous and not discretized.
   *  Returns Double.MAX_VALUE if the size is greater than Double.MAX_VALUE (this type 
   *  of overflow is not uncommon, since the values returned can be very large). 
   *  @param nodeList  the set of nodes each of whose number of states will be multiplied together
   *                   to form the cartesian product
   */
  public static double sizeCartesianProduct(NodeList nodeList) throws NeticaException{
    double size = 1.0;
    for (int n=0;  n < nodeList.size();  ++n){
      Node node = (Node) nodeList.get(n);
      int numStates = node.getNumStates();
      if (numStates == 0)  return 0.0;
      if ((numStates + 1.0) > (Double.MAX_VALUE / size)) return Double.MAX_VALUE; //Maybe better to just trap the numeric overflow exception???
      size *= numStates;
    }
    return size;
  }

  /** 
   *  Removes all the nodes in nodeList from their net, and deletes them.
   *  @param nodeList  the nodes to delete
   */
    //Brent, this would work even if the nodeList were illegal, in the sense of having nodes from diff nets. Allow this?
  public static void deleteNodes (NodeList nodeList) throws NeticaException {
      while (nodeList.size() > 0) {
	  Node node = (Node) nodeList.remove (0);
	  node.delete();
      }
  }

  /** 
   *  Returns the index of the node identified by name within our list of nodes,
   *  or -1, if it doesn't appear.
   *  @param name  the name of the node whose index we seek
   *  @returns the index of the node with that name, or -1 if there isn't one
   */
  public int findNodeNamed (String name) throws NeticaException {
      if (size()== 0)  return -1;
      Net net = getNet();
      Node node = net.getNode (name);
      if (node == null)  return -1;
      return indexOf (node);
  }

  /** 
   *  This transfers nodes from the net they are in to newNet,
   *  and returns a new list of the new nodes in the same order as they
   *  appeared in nodes.  
   *
   *  In the process, each node in nodes is deleted, and a new one created,
   *  so don't try and use any of the old nodes (if you do, a NeticaException
   *  will be thrown.)
   *  @param nodes  the nodes to be deleted
   *  @param newNet  the net to receive copies of the nodes to be deleted
   */
  public static NodeList transferNodes (NodeList nodes, Net newNet) throws NeticaException{
      NodeList newNodes = newNet.copyNodes (nodes);
      while (nodes.size() > 0){
	  Node node = (Node) nodes.remove(0);
	  node.delete();
      }
      return newNodes;
  }
}
