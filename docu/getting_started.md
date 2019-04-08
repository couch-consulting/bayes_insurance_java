# Docu Getting started - Windows
1. Clone/Download
2. Setup path: https://stackoverflow.com/a/8518438
    * Make sure its the x86 path (for 32 bit). Thus you will need a 32 bit JDK. 
3. Test via:
    1. Go to Netica Demo folder
    2. Run compile.bat
    3. Run run.bat
 

## Current Workflow
1. Build Utils function within normal java setup under 'src_test/' (or directly within the one-file java class)
2. Copy into One-file solution for Netica
3. Run compile_and_run_app.bat



### Netviewer
1. Go to "src/netviewer"
2. Run compile_and_run_netviewer.bat

* allows for own case stuff and look into cpts

#### NetViewer Workflow
* Sadly the Netviewer from netica is not working properly. Thus the following has to be done to see the net correctly. (Saved default not centered )
1. Start the Netviewer (USe it in full screen)
2. Click on "File" -> "Open" and select the correct net file (".dne") and click open again
3. In the row "Node Styles" select "Circle"
    * A circle object should appear in the left top corner 
4. Drag and drop one of the circles to the middle/bottom left of the screen 
5. In the row "Node styles" select "Auto Select"
6. Now you can see the Network. Move the Boxe (drag and drop) accordingly to cr