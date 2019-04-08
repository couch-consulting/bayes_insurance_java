# Getting started 
This documentation shall provide a short overview of how to get the program started within this project.

* The application builds the network, does classification for a set of data and saves the network.
* The Netviewer allows you to view, examined and play with the network in an application format.
    * This is a default Netica app and could also be replaced by the actual Netica App. It has similar features. 

## Installation Windows
1. Download/Clone/Copy this project (with all its files)
2. Setup your Java path correctly to include a 32 bit Version of the JDK
    * See for help: https://stackoverflow.com/a/8518438
    * Make sure its the x86 path (for 32 bit).
    * This has to be done for the Netica Java version which needs this 32 bit JDK.  

## Start the Application
1. Go to the "/src" folder
2. Execute "compile_and_run_app.bat" in CMD or Powershell. 
    * This will compile the Java code with Netica for your system and run it afterwards. 


## Netviewer
1. Go to "src/netviewer"
2. Run compile_and_run_netviewer.bat

### NetViewer Workaround 
Sadly the Netviewer from netica is not working properly. Thus the following has to be done to see the net correctly. 

* Problem: Default saved netica network wont be displayed in the center of the app

1. Start the Netviewer (Use it in full screen)
2. Click on "File" -> "Open" and select the correct net file (".dne") and click open again
3. In the row "Node Styles" select "Circle"
    * A circle object should appear in the left top corner 
4. Drag and drop one of the circles to the middle/bottom right of the screen 
5. In the row "Node styles" select "Auto Select"
6. Now you can see the Network. Move the Boxe (drag and drop) accordingly to build yourself a better view of the network. 


* CSV not cli and additionally the Netviewer can work as a GUI to enter cases etc.
