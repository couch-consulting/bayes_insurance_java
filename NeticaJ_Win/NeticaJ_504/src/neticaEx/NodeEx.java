/*
 * An extension of norsys.netica.Node containing assorted convenience methods.
 * Users are welcome to cut-and-paste methods from this class into their
 * own extensions of norsys.netica.Node. 
 *   WARNING: The methods in this class could change, so do not rely on them
 * always being here in future versions of Netica-J.  They might be promoted 
 * to the base class, revised, or removed entirely.
 */
package norsys.neticaEx;

import norsys.netica.*;

public class NodeEx extends Node{

  //------------------------------------------------------------ constructors
  public NodeEx (String name, int num_states, Net parentNet) throws NeticaException {
      super (name, num_states, parentNet);
  }

  public NodeEx (String name, String stateNames, Net parentNet) throws NeticaException {
      super (name, stateNames, parentNet);
  }

  public NodeEx () throws NeticaException{
      super();
  }

  //------------------------------------------------------------

  /** 
   *  Handy method to absorb a single node.
   *  @param node the node to be removed from its net
   */
  public static void absorb  (Node node) throws NeticaException {
      Net net = node.getNet();
      NodeList nodes = new NodeList (net);
      nodes.add (node);
      net.absorb (nodes);
  }

  /** 
   *  Make a copy of the current node and place it in newNet.
   *  @param newNet  the net where the newly constructed node will be placed.
   */
  public Node duplicate (Net newNet) throws NeticaException {
      NodeList nodes = new NodeList (getNet());
      nodes.add (this);
      NodeList newNodes = newNet.copyNodes (nodes);
      return (Node) newNodes.get (0);
  }

  /** 
   *  Make a copy of this node in its own net
   */
  public Node duplicate() throws NeticaException {
      return duplicate (getNet());
  }

  /** 
   *  A convenience version of Node.setCPTable(String, float[]), suited to nodes with two states.
   *  @param parentStateNames a comma and/or space delimited string of the parentState names, one per parent
   *  @param prob1 the probability of us being in our first state, given our parents are in the states given in 'parentStateNames'
   *  @param prob2 likewise, the probability of us being in our second state
   */
  public void setCPTable (String parentStateNames, double prob1, double prob2) 
    throws NeticaException 
  {
    float[] cpTable = new float[2];
    cpTable[0] = (float) prob1;
    cpTable[1] = (float) prob2;
    setCPTable (parentStateNames, cpTable);
  }

  /** 
   *  A convenience version of Node.setCPTable(String, float[]), suited to nodes with three states.
   *  @param parentStateNames a comma and/or space delimited string of the parentState names, one per parent
   *  @param prob1 the probability of us being in our first state, given our parents are in the states given in 'parentStateNames'
   *  @param prob2 likewise, the probability of us being in our second state
   *  @param prob3 likewise, the probability of us being in our third state
   */
  public void setCPTable (String parentStateNames, double prob1, double prob2, double prob3) 
    throws NeticaException 
  {
    float[] cpTable = new float[3];
    cpTable[0] = (float) prob1;
    cpTable[1] = (float) prob2;
    cpTable[2] = (float) prob3;
    setCPTable (parentStateNames, cpTable);
  }

  /** 
   *  A convenience version of Node.setCPTable(float[]), suited to nodes with two states and no parents.
   *  @param prob1 the probability of us being in our first state
   *  @param prob2 the probability of us being in our second state
   */
  public void setCPTable (double prob1, double prob2) throws NeticaException {
    setCPTable (null, prob1, prob2);
  }

  /** 
   *  A convenience version of Node.setCPTable(float[]), suited to nodes with three states and no parents.
   *  @param prob1 the probability of us being in our first state
   *  @param prob2 the probability of us being in our second state
   *  @param prob3 the probability of us being in our third state
   */
  public void setCPTable (double prob1, double prob2, double prob3) throws NeticaException {
    setCPTable (null, prob1, prob2, prob3);
  }

  /** 
   *  A convenience version of Node.setCPTable(String, float[]), suited to nodes with two parents and two states.
   *  @param parent1StateName the name of parent1
   *  @param parent2StateName the name of parent2
   *  @param prob1 the probability of us being in our first state, given our two parents are in the states given
   *  @param prob2 likewise, the probability of us being in our second state
   *  
   */
  public void setCPTable (String parent1StateName, String parent2StateName, double prob1, double prob2) 
    throws NeticaException 
  {
    float[] cpTable = new float[2];
    cpTable[0] = (float) prob1;
    cpTable[1] = (float) prob2;
    setCPTable (parent1StateName + "," + parent2StateName, cpTable); 
  }

  /** 
   *  A convenience version of Node.getCPTable(int[], float[]), where we do not want to reuse the float array, 
   *  rather we simply want one given to us (which we might subsequently reuse).
   *  @param parentStates the array of parent states, one per parent
   */
  public float[] getCPTable (int[] parentStates)throws NeticaException {
      return getCPTable (parentStates, null);
  }

  /** 
   *  A convenience version of Node.getCPTable(String, float[]), where we do not want to reuse the float array, 
   *  rather we simply want one given to us (which we might subsequently reuse).
   *  @param parentStates the comma/space delimited string of parent state-names, one name per parent
   */
  public float[] getCPTable (String parentStateNames) throws NeticaException {
      return getCPTable (parentStateNames, null);
  }

  /** 
   *  A convenience version of Node.getCPTable( float[]), where we do not want to reuse the float array, 
   *  rather we simply want one given to us (which we might subsequently reuse).
   */
  public float[] getCPTable ()throws NeticaException {
      return getCPTable ((float[]) null);
  }

  /** 
   *  This routine is useful when we are not sure whether node
   *  already has a finding, but, when it does, we just want to override it.
   *  @param stateIndex the index of the state we are now claiming this node is in
   */
  public void changeFinding (int stateIndex) throws NeticaException {
    int saved = getNet().getAutoUpdate();        
    getNet().setAutoUpdate (0);
    finding().clear();
    finding().enterState (stateIndex);
    getNet().setAutoUpdate (saved);
  }

  /** 
   *  Same as changeFinding(int stateIndex) above, only where the state is 
   *  given by its stateName rather than stateIndex.
   *  @ param stateName the name of the state we are now claiming this node is in
   */
  public void changeFinding (String stateName) throws NeticaException {
    changeFinding (state (stateName).getIndex());
  }

  /** 
   *  This routine is useful when we are not sure whether a real valued node
   *  already has a finding, but, when it does, we just want to override it.
   *  @param value the new value we believe this node has
   */
  public void changeValue (double value) throws NeticaException {
    getNet().setAutoUpdate (0);
    finding().clear();
    finding().enterReal (value);
    getNet().setAutoUpdate (Net.BELIEF_UPDATE);
  }

  /** 
   *  Same as enterFindingNot(int stateIndex), only where the state is 
   *  given by its stateName rather than stateIndex.
   *  @deprecated.  Use Value.enterStateNot (String stateName) instead.
   *  @ param stateName the name of the state we are now claiming this node is NOT in
   */
  public void enterFindingNot (String stateName) throws NeticaException {
    finding().enterStateNot (state(stateName).getIndex());
  }

  /** 
   *  Return the first link index of the the given parentNode to us, or -1, 
   *  if the given parentNode is not really a parent.
   *  Warning: if this node has more than one link from the parent Node to this node,
   *  this will just return the index of the first one. 
   *   @ param parentNode the node, believed to be a parent node, whose index we seek
   */
  public int getParentIndex (Node parentNode) throws NeticaException {
      NodeList list = getParents();
      for (int i=0; i<list.size(); i++) {
	  Node parNode = (Node) list.get (i);
	  if (parNode.equals (parentNode)) {
	      return i;
	  }
      }
      return -1;
  }

  /** 
   *  Same as getParentIndex(Node parentNode), only where the parent is
   *  given by its name, rather than by its Node object.
   *  Warning: if this node has more than one link from the parent Node to this node, this
   *  will just return the index of the first one.
   *   @ param parentName the name of the node, believed to be a parent node, whose index we seek
   */
  public int getParentIndex (String parentName) throws NeticaException {
      Node parent = (Node) getNet().getNode (parentName);
      return getParentIndex (parent);
  }

  /** 
   *  Removes the single link from node 'parent' to node 'child'.
   *  If there is no link from 'parent' to 'child', or more than one, it generates an error.
   *  @param parent the parent node
   *  @param child  the child node
   */
  public static void deleteLink (Node parent, Node child) throws NeticaException {
      NodeList parents = child.getParents();
      int index = parents.indexOf (parent);
      child.deleteLink (index);
  }

  /** 
   *  Removes any and all links from node 'parent' to ourself.
   *  @param parent the parent node
   */
  public void deleteLinks (Node parent) throws NeticaException {
      NodeList list = getParents();
      for (int i=0; i<list.size(); i++) {
	  Node parNode = (Node) list.get (i);
	  if (parNode.equals (parent)) {
	      deleteLink (i);
	  }
      }
  }

  /**
   *  Removes all links entering node child
   *  See the Node.deleteLink() comment for explanation
   *  @param child the child node
   */
  public static void deleteLinksEntering (Node child) throws NeticaException{
      NodeList parents = child.getParents();
      int numParents = parents.size();
      for (int pn = 0;  pn < numParents;  ++pn) {
	  child.deleteLink (0);
      }
  }

  /** 
   *  Replace one parent link with a new link.  A convenience version of
   *  switchParent(int, Node)
   *  @param parentNode the node we no longer want as a parent
   *  @param newParentNode the new node we want as a parent
   */
  public void switchParent (Node parentNode, Node newParentNode) throws NeticaException {
    int parentLinkIndex = getParentIndex (parentNode);
    switchParent (parentLinkIndex, newParentNode);
  }

  /** 
   *  @returns a string of all our state names in index order, each separated by comma.
   */
  public String getStateNames() throws NeticaException {
      String[] stateNamesArray = getStateNamesArray();
      StringBuffer buf = new StringBuffer();
      for (int i=0; i<stateNamesArray.length; i++) {
	  if (i>0) buf.append (',');
	  buf.append (state (i).getName());
      }
      return buf.toString();
  }

  /** 
   *  @eturns the state names of this node (in index order) in a String array.
   */
  public String[] getStateNamesArray() throws NeticaException {
    int numberStates = getNumStates();
    String[] stateNames = new String[ numberStates ];
    for (int i=0; i<numberStates; i++) {
      stateNames[i] = state(i).getName();
    }
    return stateNames;
  }

  /** 
   *  Gives the passed node a uniform conditional probability distribution
   *  (i.e. all the probabilities the same).
   *  @param node the node to modify
   */
  public static void makeProbsUniform (Node node) throws NeticaException{
      int numStates  = node.getNumStates();
      int numParents = node.getParents().size();
      float[] uniform = new float[numStates];
      int[]   pstates = new int[numParents];
      for (int st = 0;  st < numStates;   ++st)  uniform[st] = 1.0F / numStates;
      for (int pn = 0;  pn < numParents;  ++pn)  pstates[pn] = State.EVERY_STATE;
      node.setCPTable (pstates, uniform);
  }

  /** 
   * Sets all the conditional probabilities of a given node based on a 2-D array of probabilities.
   * You could use this function in combination with getNodeAllProbs (see below)
   * to temporarily save probability tables.
   * The 1st dimension of the array passed must be the size of cartesian product
   * of the states of each parent of node, and must be in "odometer order".
   * See NodeListEx.nextStates() for an explanation of the odometer order of 
   * all possible parent states. 
   *
   *  @param node  is the node whose CPT we are setting
   *  @param probs is an array of probabilities, the first dimension being of size equal to 
   *	           the size of cartesian product of the states of each parent of node. 
   *               The second dimension is the number of states of node.
   *               i.e., float[][] probs = new float[(int) NodeListEx.sizeCartesianProduct (node.getParents())]
   *                                                [node.getNumberStates()];
   */
  public static void setNodeAllProbs (Node node, float[][] probs) throws NeticaException{
      NodeList parents = node.getParents();
      int[] parentStates = new int[parents.size()]; //all initially zero
      int probSet = 0;
      while (true){
          node.setCPTable (parentStates, probs[probSet++]);
          if (NodeListEx.nextStates (parentStates, parents))  break;
      }
  }

  /** 
   *  Returns all the conditional probabilities of a given node as a 2-D array.
   *  You may supply an array to re-use.  If it is null, however, then one will be supplied.
   *  @param node  is the node whose CPT we are retrieving
   *  @param probs is an array of probabilities, the first dimension being of size equal to 
   *	           the size of cartesian product of the states of each parent of node. 
   *               The second dimension is the number of states of node.
   *               i.e., float[][] probs = new float[(int) NodeListEx.sizeCartesianProduct (node.getParents())]
   *                                                [node.getNumberStates()];
   *               It may be null, in which case an array will be allocated for you.
   */
  public static float[][] getNodeAllProbs (Node node, float[][] probs) throws NeticaException {
      NodeList parents = node.getParents();
      int numStates  = node.getNumStates();
      if (probs == null) {
	  probs = new float[(int) NodeListEx.sizeCartesianProduct (node.getParents())][numStates];
      }
      int[] parentStates = new int[parents.size()];  //initially all zero
      int probSet = 0;
      while (true){
          float[] vecp = node.getCPTable (parentStates, null);
          if (vecp == null)  break;
          for (int st = 0;  st < numStates;  ++st)  probs[probSet][st] = vecp[st];
          if (NodeListEx.nextStates (parentStates, parents)) 
              break;
          ++probSet;
      }
      return probs;
  }

  /** 
   *  For discrete or discretized nodes that are a deterministic function of their
   *  parents' states, this method can be used to set the function table in one
   *  call, rather than repeatedly calling setStateFuncTable().
   *  The array passed must be the size of cartesian product of the states of each 
   *  parent of node. It contains state indices of this state, each entry representing
   *  the state that this node is in when the parents are in the corresponding index'th
   *  odometer configuration.  See NodeListEx.nextStates() for an explanation of 
   *  the odometer order of all possible parent states. 
   *  The following line is useful for declaring value[]:
   *  int[] value = new int[ NodeListEx.sizeCartesianProduct (node.getParents()) ];
   *
   *  @param node      the node of interest
   *  @param value     the array of states of this node, one entry per permutation
   *                   of parent states in "odometer order".
   */
    
    public static void setNodeFuncState (Node node, int[] value) throws NeticaException{
	NodeList parents = node.getParents();
	int[] parentStates = new int[parents.size()]; //all initially zero
	int probSet = 0;
	while (true){
	    node.setStateFuncTable(parentStates, value[probSet++]);
	    if (NodeListEx.nextStates (parentStates, parents))  break;
	}
    }

  /** 
   *  For continuous nodes that are a deterministic function of their
   *  parents' states, this method can be used to set the function table in one
   *  call, rather than repeatedly calling setRealFuncTable().
   *  The array passed must be the size of cartesian product of the states of each 
   *  parent of node. It contains the values of this node when the parents are 
   *  in the corresponding index'th odometer configuration.  
   *  See NodeListEx.nextStates() for an explanation of  the odometer order 
   *  of all possible parent states. 
   *  The following line is useful for declaring value[]:
   *  double[] value = new double[ NodeListEx.sizeCartesianProduct (node.getParents()) ];
   *
   *  @param node      the node of interest
   *  @param value     the array of values of this node, one entry per permutation
   *                   of parent states in "odometer order".
   */
    
    public static void setNodeFuncReal (Node node, double[] value) throws NeticaException{
	NodeList parents = node.getParents();
	int[] parentStates = new int[parents.size()]; //all initially zero
	int probSet = 0;
	while (true){
	    node.setRealFuncTable(parentStates, value[probSet++]);
	    if (NodeListEx.nextStates (parentStates, parents))  break;
	}
    }

  /** 
   *  Determines whether a link entering a node is disconnected.
   *  @param linkIndex the index number of the link in question
   *  @param node      the node of interest
   *  @returns true, iff the link is disconnected.
   */
  public static boolean isLinkDisconnected (int linkIndex, Node node) throws NeticaException{
      NodeList parents = node.getParents();
      Node parent = (Node) parents.get (linkIndex);
      return (parent.getKind() == Node.DISCONNECTED_NODE);
  }

  //------------------------------------------------------------
  //------------------------------------------------------------
  //--  Do not include these in the NodeEx that is distributed
  //------------------------------------------------------------
  //------------------------------------------------------------

  /** 
   *  A convenience method passing parentStates as a comma or space delimited string.
   */
  public int[] getStateFuncTable (String parentStatesStr, int[] stateTable)throws NeticaException {
      return getStateFuncTable (parentStatesToIndexes (parentStatesStr), stateTable);
  }
  public int[] getStateFuncTable (String parentStatesStr) throws NeticaException {
      return getStateFuncTable (parentStatesToIndexes (parentStatesStr), null);
  }

  /** 
   *  A convenience method passing parentStates as a comma or space delimited string.
   */
  public double[] getRealFuncTable (String parentStatesStr, double[] realTable) throws NeticaException {
      return getRealFuncTable (parentStatesToIndexes (parentStatesStr), realTable);
  }
  public double[] getRealFuncTable (String parentStatesStr )throws NeticaException {
      return getRealFuncTable (parentStatesToIndexes (parentStatesStr), null);
  }

  /** 
   *  A convenience method passing parentStates as a comma or space delimited string.
   */
  public double[] getExperTable (String parentStatesStr, double[] experTable) throws NeticaException {
      return getExperTable (parentStatesToIndexes (parentStatesStr), experTable);
  }
  public double[] getExperTable (String parentStatesStr) throws NeticaException {
      return getExperTable (parentStatesToIndexes (parentStatesStr), null);
  }

  /** 
   *  A convenience method passing parentStates as a comma or space delimited string.
   */
  public void setStateFuncTable (String parentStatesStr, int funcState ) throws NeticaException {
      setStateFuncTable (parentStatesToIndexes (parentStatesStr), funcState);
  }

  /** 
   *  A convenience method passing parentStates as a comma or space delimited string.
   */
  public void setRealFuncTable (String parentStatesStr, double funcValue) throws NeticaException {
      setRealFuncTable (parentStatesToIndexes (parentStatesStr), funcValue);
  }

  /** 
   *  A convenience method passing parentStates as a comma or space delimited string.
   */
  public void setExperTable (String parentStatesStr, double experience) throws NeticaException {
      setExperTable  (parentStatesToIndexes (parentStatesStr), experience);
  }

}
