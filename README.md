# Docu - Windows
1. Download
2. Setup path: https://stackoverflow.com/a/8518438
    * Make sure its the x86 path (for 32 bit). Thus you will need a 32 bit JDK. 
3. Test via:
    1. Go to src folder
    1. Run compile_and_run.bat
    

# Netica Docu
https://www.norsys.com/netica-j/docs/NeticaJ_Man.pdf
* Rework file structure to utilize one point only - problem with file structure 

## General Structure Idea  
1. Extract all possible nodes + values from CSV (or build per hand)
    * rethink how the net shall look like (especially what kind of values each node could have!)
2. Build nodes with values 
3. Connect nodes correctly 
4. Learn CPTs 
5. Save Network
6. Display network
7. Give possibility to enter input

* Build correct hierarchy 

## Current Workflow
1. Build Utils function within normal java setup under 'src_test/'
2. Copy into One-file solution for Netica
3. Compile and run


## CSV File Assumptions
* First lines contains column headers 
* Last column contains evaluation information (e.g. Assigned product A/B)
* Values separated by semicolon 
* No usage of quotes 
* "Empty" values are marked with "n.a."
* Number values minimum are 0


## Docu
* You have to surround every Netica code with try-catch statements 
* For state of Nodes: If not a string -> N indicates pure Number, R indicates Range
* Only Int Numbers and ranges possible due to netica seeing a "." or "," as state separator 
* "-" is a invalid character thus it will be replaced by "bis"


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