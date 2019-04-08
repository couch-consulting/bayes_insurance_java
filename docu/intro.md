## Netica Docu
* https://www.norsys.com/netica-j/docs/NeticaJ_Man.pdf
* HTML FIles under "NeticaJ_WIn/NeticaJ_504/docs/javadocs"
* Free Version of netic only suppports 15 nodes (our initial solution had 17). Thus we are not using made up nodes and only the given 14. 
## Content
* Warum Netica, Warum java etc.... + warum so gebaut 
* Eigene test bewertungen mit input data (an 4 beispielen - wenig zu viel test daten)

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
* Workflow grafik

* decided against synthetic node because we could only use one extra node and learning a new node is not working correctly 
* using Counting Learning as cpt learn algo (see netica_J manual)
* example data is almost everytime b (not enough a)
* n.a mapped to lowest range for numbers



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


