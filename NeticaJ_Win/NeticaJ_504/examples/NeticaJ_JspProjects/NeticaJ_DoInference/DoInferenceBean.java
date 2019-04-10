
package norsys.netica.jspprojects.doinference;


import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import norsys.netica.*;

public class DoInferenceBean {

  public Node visitAsia;
  public Node smoking;
  public Node xRay;
  public Node dyspnea;
  public Node tuberculosis;
  public Node cancer;
  public Node bronchitis;

  public String visitAsiaRadio;
  public String smokingRadio;
  public String xRayRadio;
  public String dyspneaRadio;

  public static Environ env;
  private Net net;
  private String date;
  private String logBuf = "";

  public DoInferenceBean () {
    SimpleDateFormat sdf = new SimpleDateFormat( "MMM d, yyyy" );
    this.date = sdf.format( (new GregorianCalendar()).getTime() );
  }

  public String getDate () {
    return this.date;
  }

  public void resetForNewPatient(){
      visitAsiaRadio = "unk";
      smokingRadio   = "unk";
      xRayRadio      = "unk";
      dyspneaRadio   = "unk";
  }

    private void loadChestClinicNet() throws Exception{
	//-- If you know the exact path, you can do something like the following
	//net = new Net (new Streamer ("C:\\Apps\\apache-tomcat-5.5.20\\webapps\\Netica\\ChestClinic.dne"));

	//-- Or, if you want to keep things relative to your website, which is to be recommended, then do...
	URL url = this.getClass().getClassLoader().getResource("ChestClinic.dne"); // Resource in WEB-INF/classes dir
	InputStream is = new FileInputStream(url.getFile());
	net = new Net( new Streamer ( is, "ChestClinic.dne", env ) );

	visitAsia    = net.getNode( "VisitAsia");
	smoking      = net.getNode( "Smoking"  );
	xRay         = net.getNode( "XRay"     );
        dyspnea      = net.getNode( "Dyspnea"  );
	tuberculosis = net.getNode( "Tuberculosis" );
	cancer       = net.getNode( "Cancer"       );
	bronchitis   = net.getNode( "Bronchitis"   );

	net.compile();
    }

  public String initSession (){
      resetForNewPatient();
	String returnMsg = null;
    try {
	env = new Environ (null);
	loadChestClinicNet();
    }
    catch (Exception e) {
	returnMsg = e.getMessage();
	e.printStackTrace();
    }
    return returnMsg;
  }

  public void processRequest () {
      


  }

    public String getLog() { return logBuf; }

    public void log( String msg ){
	msg = msg.replaceAll( "\n", "<br>\n" );
	if (msg != null) {
	    logBuf += msg + "<br>";
	}
    }
}
