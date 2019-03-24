/**
 * A convenience class, for naming purposes, that simply wraps a NetEx
 * but preserves the same name as norsys.netica.Net
 */
package norsys.neticaEx.aliases;

import norsys.neticaEx.*;
import norsys.netica.*;

public class Net extends NetEx {

    //------------------------------------------------------------ constructors
    public Net() throws NeticaException {
	super();
    }
    
    public Net (Environ env) throws NeticaException { 
	super (env);
    }
    
    public Net (Streamer inStream) throws NeticaException { 
	super (inStream);
    }
}
