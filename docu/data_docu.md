* all possible data things
* Any input/output files is stored in the "data/" directory. 

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
