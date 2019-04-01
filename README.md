# Docu Getting started - Windows
1. Clone/Download
2. Setup path: https://stackoverflow.com/a/8518438
    * Make sure its the x86 path (for 32 bit). Thus you will need a 32 bit JDK. 
3. Test via:
    1. Go to Netica Demo folder
    2. Run compile.bat
    3. Run run.bat
    
## Netica Docu
* https://www.norsys.com/netica-j/docs/NeticaJ_Man.pdf
* HTML FIles under "NeticaJ_WIn/NeticaJ_504/docs/javadocs"


# General TODOS and project plan   
1. Extract all possible nodes + values (possibilities) from CSV Input file [DONE]
2. Build nodes with possibilities [DONE]
    * possibilities will be normalized (to String) and sets of numbers will be transformed into ranges
3. Connect nodes correctly [DONE]
    * Node connection setup has to be given as Input file 
4. Learn CPTs 
    * Map Input to ranges and normalized possibilities 
5. Save Network [DONE]
    * Basic Write to File
6. Display network [FAKE DONE]
    * This functionality is given by a Netviewer from Netica itself. 
    * This can only be done afterwards 
7. Give possibility to enter input 
    * CSV file or CLI?

## Next Todos
* Build correct hierarchy 
* Create another file structure where it is not a one-file solution and easier to read/inspect
* Save Network in a better fashion (if possible)
* Create complete documentation 
* Maybe create tests
* GO through code, refactor if possible and comment if additional is needed 
* See if our solution fits to task completion 
* Display Network in normal Workflow/App but not afterwards


## Current Workflow
1. Build Utils function within normal java setup under 'src_test/' (or directly within the one-file java class)
2. Copy into One-file solution for Netica
3. Run compile_and_run_app.bat

### Netviewer
1. Go to "src/netviewer"
2. Run compile_and_run_netviewer.bat

## Docu
* You have to surround every Netica code with try-catch statements 
* For state of Nodes: If not a string -> N indicates pure Number, R indicates Range
* Only Int Numbers and ranges possible due to netica seeing a "." or "," as state separator 
* "-" is a invalid character thus it will be replaced by "bis"
* Any input/output files is stored in the "data/" directory. 
* Code is stored in the "src/" directory. 

### Input CSV File Assumptions
* First lines contains column headers 
* Last column contains evaluation information (e.g. Assigned product A/B)
* Values separated by semicolon 
* No usage of quotes 
* "Empty" values are marked with "n.a."
* Number values minimum are 0

### "netConnections" CSV File
* First element is target, rest of row are elements that point to the first element

### NetViewer Workflow
* Sadly the Netviewer from netica is not working properly. Thus the following has to be done to see the net correctly.
1. Start the Netviewer (USe it in full screen)
2. Click on "File" -> "Open" and select the correct net file (".dne") and click open again
3. In the row "Node Styles" select "Circle"
    * A circle object should appear in the left top corner 
4. Drag and drop one of the circles to the middle/bottom left of the screen 
5. In the row "Node styles" select "Auto Select"
6. Now you can see the Network. Move the Boxe (drag and drop) accordingly to create a better view of the network. 