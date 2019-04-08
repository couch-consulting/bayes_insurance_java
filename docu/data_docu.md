# Data documentation
This is a documentation about assumptions and constraints of all input files

* Any input file is stored in the "data/" directory or its subdirectories. 

## Trainings Data - CSV file (.csv) 
The trainings data csv file (in this project "verischerung_a.csv") contains information about all possible nodes and values.
This data is used for getting all nodes and possibilities and building a case file for training the CPTs. 

* Current example given under "data/verischerung_a.csv"

### Assumptions
* First line contains the column headers (nodes of the network) 
* Last column contains evaluation information (e.g. assigned product A/B). 
* Only two evaluation results exist (e.g. A and B)
* Values are separated by semicolon 
* No usage of quotes but raw string instead  
* "Empty" numeric values are marked with "n.a.", non numeric vales should not be empty.
    * The could be empty but this would be not beneficial. 
* The minimum a numeric value could attain is zero.
* No empty lines and each line has "content" (e.g. semicolons) for each node

## Test data - CSV file (.csv)
This test data csv file supplies data that is used for the classification test. It is similar to the trainings data csv file but has further assumptions.

* Current example given under "data/versicherung_a_classify.csv"

### Assumptions
* First line contains the column headers (nodes of the network) 
* Last column contains evaluation information (e.g. assigned product A/B). 
    * This can be empty but if a control value is given, statistics will be created for correct/false classifications.
* Only two evaluation results exist (e.g. A and B)
* Values are separated by semicolon 
* No usage of quotes but raw string instead  
* "Empty" numeric values are marked with "n.a." and can be empty, non numeric vales can be empty.
    * An all empty line will return the default CPT table of the evaluation node as result (e.g. the actual distribution).
    * Missing values are not used for the classification (as intended) but a result can still be produced. 
* The minimum a numeric value could attain is zero.
* No empty lines and each line has "content" (e.g. semicolons) for each node


## Network Links - CSV file (.csv)
This file describes the links between nodes that shall be implemented for the network. This provides a non hard coded possibility to setup the links between the nodes. 

* Our default connection Setup is give under "data/connectionSetup/netConnections.csv"
* Other connection setups are given as well in this directory. The name contains its statistics. 
    * Here the ending has to be read like this: "O" indicates the overall correct classification ratio, "B" the ratio for correctness of B and "A" for the correctness of A.

### Assumptions
* First element is the target node for a link, rest of the row are elements that point to this target node (first element of row)
* Everything is a string and separated by ";"
* Every string uses non utf-8 chars (e.g no "ö" etc)
* Every pointerNode of a target node is unique
* no trailing ";"
* No empty lines


## Using different csv files/path or names for files
1. In the src/app/Main.java file edit the string in the main function of the Main class for each path according to your needs.
2. Create your own file and replace currently existing files (same path/same name)   