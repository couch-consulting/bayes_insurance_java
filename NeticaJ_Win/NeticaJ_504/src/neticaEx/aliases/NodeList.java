/**
 * A convenience class, for naming purposes, that simply wraps a NodeListEx
 * but preserves the same name as norsys.netica.NodeList
 */
package norsys.neticaEx.aliases;

import norsys.neticaEx.*;
import norsys.netica.*;

public class NodeList extends NodeListEx {

    //------------------------------------------------------------ constructors
    public NodeList (Net parentNet) throws NeticaException {
	super (parentNet);
    }
    public NodeList (NodeList nodes) throws NeticaException {
	super (nodes);
    }
}
