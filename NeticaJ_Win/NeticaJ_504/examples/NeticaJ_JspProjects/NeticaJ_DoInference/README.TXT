How to deploy and run the doInference web application under Apache's Tomcat app server
--------------------------------------------------------------------------------------

1.  Copy the four files in the WEB-INF\lib_copyToSharedFolder\  folder (these are the the NeticaJ and Netica DLL and jar files) to the TOMCAT_HOME\shared\lib\ folder.  You can also use the common\lib\ folder, but shared\lib\ is technically the correct location since tomcat does not need the Netica libraries.  Note that moving these files to the shared\lib\ folder is an important step, for if you were only to have these files available in the WEB_INF\lib\ folder, doInference will still run, but whenever tomcat reloads/redeploys the application, NeticaJ's JNI libraries will be loaded into a 2nd classloader, and this will result in the nasty "already loaded in another classloader" jspException being thrown.  This is because, a JNI library is only permitted to be loaded once within a JVM.
2. Ensure that the directory with Netica.DLL is available on the System path.  For example, in the startup.bat or "Control Panel->System->Advanced->Environment Variables"  define PATH to be %TOMCAT_HOME%\shared\lib\;%PATH%;
3.  Start the tomcat server (TOMCAT_HOME\bin\startup.bat).
4.  Start the tomcat web application manager and deploy doInference.  To do this, choose a context path, e.g., "/Netica" (note that the forward slash is required)  and specify this directory (...\examples\jspProjects\NeticaJ_DoInference) as the location of the web application.
5.  Point your web browser to http://localhost:8080/Netica/doInference.jsp and the doInference.jsp page should compile and run automatically.  
6.  Using the Web Applicaton.  Click on the radio buttons to enter medical symptoms and predisposition findings. When you click "Submit", Netica will compute the probabilities of the three diseases, given the state of knowledge of your patient. When you click "New Patient Reset" all findings are cleared.  

Additional Notes
----------------
Note that you can have any number of users simultaneously accessing/running the doInference web application.

To read/learn more about the classic ChestClinic Bayesian Network, see http://www.norsys.com/netlibrary/index.htm.


Developer Notes
---------------
A batch file, compile.bat, is supplied for compiling DoInferenceBean.java.  This is the main "back-end", presentation-independent, bean that connects to Netica, reads in the Bayesian Network, and handles requests from the presentation-layer.  Whenever you recompile DoInferenceBean, you need to re-deploy the web application.

doInference.jsp is typically compiled dynamically by your jsp engine (e.g., Tomcat), so you can just edit it in the webapps/Netica folder, if you desire, and it will be automatically re-deployed.  This typically makes developing the presentation-layer a fair bit faster than developing the "back-end" beans.