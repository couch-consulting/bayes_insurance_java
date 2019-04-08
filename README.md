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
* Free Version of netic only suppports 15 nodes (our initial solution had 17). Thus we are not using made up nodes and only the given 14. 

# General project content 
1. Extract all possible nodes + values (possibilities) from CSV Input file [DONE]
2. Build nodes with possibilities [DONE]
    * possibilities will be normalized (to String) and sets of numbers will be transformed into ranges
3. Connect nodes correctly [DONE]
    * Node connection setup has to be given as Input file 
4. Learn CPTs [DONE]
    * Map Input to ranges and normalized possibilities (via CaseFile)
5. Save Network [DONE]
    * Basic Write to File
6. Display network [FAKE DONE]
    * This functionality is given by a Netviewer from Netica itself. 
    * This can only be done afterwards 
7. Give possibility to enter input and get prediction [DONE]
    * Given CSV File which is a subset of the original dataset 

## Next Todos
* Write up connection setup
* Think about: Build correct hierarchy (not one file)
    * Create another file structure where it is not a one-file solution and easier to read/inspect
    



* Create complete documentation 
    * Warum Netica, Warum java etc.... + warum so gebaut 
    * Eigene test bewertungen mit input data (an 4 beispielen - wenig zu viel test daten)
    * Doku Read.me in leserlich 

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
6. Now you can see the Network. Move the Boxe (drag and drop) accordingly to create a better view of the network.

## Docu
* Network file itself can not be pushed to Source control because it is to big (ca. 270 MB) but the code will create the same network file everytime
* You have to surround every Netica code with try-catch statements 
* For state of Nodes: If not a string -> N indicates pure Number, R indicates Range
* Only Int Numbers and ranges possible due to netica seeing a "." or "," as state separator 
* "-" is a invalid character thus it will be replaced by "bis"
* Any input/output files is stored in the "data/" directory. 
* Code is stored in the "src/" directory. 
* Ranges are excluding upper limit and including lower limit
* System out println used instead of logger on purpose 
* decided against synthetic node because we could only use one extra node and learning a new node is not working correctly 
* using Counting Learning as cpt learn algo (see netica_J manual)
* example data is almost everytime b (not enough a)

### Input CSV File Assumptions
* First lines contains column headers 
* Last column contains evaluation information (e.g. Assigned product A/B). Only two evaluation results exist (e.g. A and B)
* Values separated by semicolon 
* No usage of quotes 
* "Empty" numeric values are marked with "n.a.", non numeric vales can not be empty
* Number values minimum are 0
* no empty lines

### "netConnections" CSV File
* First element is target, rest of row are elements that point to the first element
* Everything is string and separated by ";"
* Synthetic nodes are given
* Every string uses non utf-8 chars (e.g no "ö" etc)
* Every pointerNode of a target node has to be unique
* no trailing ";"
* different names for different net results with these connections on all data


#### Input Test data csv
* CSV not cli and additionally the Netviewer can work as  a GUI to enter cases etc.
* CSV with same assumption as input csv file 
* n.a for some
* Here, empty non numeric values can be makred with just no text between the ";" or at the end
* no empty lines
* must have same words for headers and attributes as normal input data
* any var can be left out for a case line and it will still try to classify it
    * A controll tarif answer does not to have to give but gives better statistics if it is the case
* an all empty line will return default setup that the cpts learned (can also be see that way in the netviewr) 

### Learning/Training
 (for connection tests)
* Learning on data: everyting
* test on data: everything
* In our test the ratio for a was always worse than the b ratio because the input data just has not enough data on cases with result A
    ````
    189 of 199 cases are correct! Ratio: 94.97488 %
    For cases with result B: 173 of 177 cases are correct! Ratio: 97.74011 %
    For cases with result A: 16 of 22 cases are correct! Ratio: 72.72727 %
    88.94472 % of all cases are with the result B
    11.055277 % of all cases are with the result A
    ````
    Here you can see that our test data has not enough A cases!
* for reality data should be: used with k-folding or other input data!


### Data nodes:
* n.a mapped to lowest range for numbers



