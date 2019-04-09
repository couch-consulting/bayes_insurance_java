/**
 * A convenience class, for naming purposes, that simply wraps a NodeEx
 * but preserves the same name as norsys.netica.Node
 */
package norsys.neticaEx.aliases;

import norsys.neticaEx.*;
import norsys.netica.*;

public class Node extends NodeEx {

    //------------------------------------------------------------ constructors
    public Node (String name, int num_states, norsys.netica.Net parentNet) throws NeticaException {
	super( name, num_states, parentNet );
    }
    
    public Node (String name, String stateNames, norsys.netica.Net parentNet) throws NeticaException {
	super( name, stateNames, parentNet );
    }
    
    public Node() throws NeticaException {}; 
}
