# Docu - Windows
1. Download
2. Setup path: https://stackoverflow.com/questions/7709041/javac-is-not-recognized-as-an-internal-or-external-command-operable-program-or
    * Make sure its the x86 path (for 32 bit)
3. Test via:
    1. Go to src folder
    1. Run compile_and_run.bat
    
# Next Steps
1. Try with data and build own test system 
2. Think of application that can be done via compile etc(maybe cli think that asks what todo)


https://www.norsys.com/netica-j/docs/NeticaJ_Man.pdf
* Rework file structure to utilize one point only - problem with file structure 

## General Structure Idea  
1. Extract all possible nodes + values from CSV (or build per hand)
    * rethink how the net shall look like (especially what kind of values each node could have!)
2. Build nodes with values 
3. Connect nodes correctly (Find out how!)
4. Learn CPTs 
5. Save Network
6. Display network
7. Give possibility to enter input

## Current Workflow
1. Build Utils function within normal java setup under 'src_test/'
2. Copy into One-file solution for Netica
3. Compile and run


