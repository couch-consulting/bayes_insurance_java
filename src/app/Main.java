/*
 * Main.java
 *
 * Test Example use of Netica-J to build a Bayes net and use it for inference.
 * Used for bayes_insurance
 * Stupid One-File strucutre because Netica build does not allow a different strucutre (as far as we know)
 */

// Import Netica

import norsys.netica.*;
import norsys.neticaEx.aliases.Node;

// Import for Java
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.rmi.UnexpectedException;


// Import for Main
import java.nio.file.Paths;

// Import for NetBuild
import java.util.Collections;

/*
Main Clas
 */
class Main {

    private static final String projectRootPath = new File(System.getProperty("user.dir")).getParentFile().getAbsolutePath();

    public static void main(String[] args) {
        // Build paths
        String inputFullPath = Paths.get(projectRootPath, "data", "versicherung_a.csv").toString();
        String connectionFullPath = Paths.get(projectRootPath, "data", "netConnections.csv").toString();
        String outputpath = Paths.get(projectRootPath, "data").toString();

        try {
            // Get data from csv files
            List<List<String>> csvdata = CSVUtils.parseCSVFile(inputFullPath);
            List<List<String>> connectionData = CSVUtils.parseCSVFile(connectionFullPath);

            System.out.println("Building network...");
            //build network
            NeticaNetBuilder.build_net(connectionData, csvdata, outputpath);
            System.out.println("Done!");


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}


class NeticaNetBuilder {
    private static List<Node> nodesList = new ArrayList<>();
    private static List<String> nodes = new ArrayList<>();
    private static List<List<String>> possibilities = new ArrayList<>();
    private static List<List<String>> newPossibilities = new ArrayList<>();
    private static Map<String, Integer> modifiedFlagMap = new HashMap<>();

    public static void build_net(List<List<String>> connectionData, List<List<String>> csvdata, String outputpath) {
        try {
            // Build paths
            String outputpathNetFile = Paths.get(outputpath, "BayesInsurance.dne").toString();
            String outputpathCaseFile = Paths.get(outputpath, "insuranceCaseFile.txt").toString();

            // Init
            Net net = NeticaUtils.initNetica("BayesInsurance");

            // Get Nodes
            nodes = Utils.extractNodes(csvdata);
            // Get possibilities for all Nodes
            possibilities = Utils.extractPossibilities(csvdata);

            System.out.println("Setup Nodes and Links...");
            // Build nodes
            build_nodes(net);
            //Add Links
            add_links(net, connectionData);

            System.out.println("Learning CPTs...");
            // Build Cases from inputData
            List<List<String>> cases = buildCaseinputData(newPossibilities, modifiedFlagMap, csvdata);
            // Build case File
            NeticaUtils.writeCaseFile(nodes, cases, outputpathCaseFile);

            //learn CPTs
            learn_cpts(net, outputpathCaseFile);


            System.out.println("Saving Network into a File...");
            NeticaUtils.writeNetIntoFile(net, outputpathNetFile);
            NeticaUtils.deconstructNeticaNet(net);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Fill a given network with newly build nodes. Modif input possiblites to be a range for numbers only.
     * Every Input will be transformed to a valid strucutre (String with alphabetical first letter)
     *
     * @param net a Network of the Netica API
     */
    private static void build_nodes(Net net) {
        try {
            // Build nodes
            int index = 0;
            int listLength = nodes.size() - 1;

            for (String node : nodes) {

                // List to caputre modifed possibilities
                List<String> tmpNewPosi = new ArrayList<>();

                // Build string from array
                StringBuilder builder = new StringBuilder();
                boolean first = true;
                int modification = -1;
                // Check if possibilities are  a set of numbers and if they are a set, transfrom into 3 Ranges instead
                List<String> currentPossibilites = new ArrayList<>(possibilities.get(index));
                if (Utils.possibilitiesAreNummbers(currentPossibilites)) {
                    currentPossibilites = Utils.getRangesFromPossibilities(currentPossibilites);
                    modification = 0;
                }


                // Modif possibilites

                for (String value : currentPossibilites) {
                    // Eliminate illegal char "-"
                    if (value.contains("-")) {
                        // For a number replace to "bis" and check if string is a number
                        if (value.replace("-", "").trim().matches("-?\\d+(\\.\\d+)?")) {
                            value = "R" + value.replace("-", "bis").trim();
                            if (modification != 0) {
                                modification = 1;
                            }
                        } else {
                            value = value.replace("-", "").trim();
                            modification = 2;
                        }
                    } else if (value.matches("-?\\d+(\\.\\d+)?")) {
                        // Check if String is a nubmer and add "N" infront if that is the case
                        value = "N" + value;
                        modification = 3;

                    } else if (value.contains(" ")) {
                        value = value.replace(" ", "").trim();
                        modification = 4;
                    }

                    //Only append "," if it is not the first value
                    if (first) {
                        builder.append(value);
                        tmpNewPosi.add(value);
                        first = false;
                    } else {
                        builder.append("," + value);
                        tmpNewPosi.add(value);
                    }
                }

                // Save modification for later
                if (modification != -1) {
                    modifiedFlagMap.put(node, modification);
                }
                // Save to new possibilites
                newPossibilities.add(tmpNewPosi);


                // Build One String for Netica API
                String tmpPossib = builder.toString();
                //Create node and add to node list
                Node tmpNode = new Node(node, tmpPossib, net);
                nodesList.add(tmpNode);


                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A auotmatic link builder which utilizes data which represent the connections to add the links between nodes of the given net
     *
     * @param net:            A netica Network filled with nodes
     * @param connectionData: A List of String Lists filled accordingly to documentation to represent connections.
     */
    private static void add_links(Net net, List<List<String>> connectionData) {
        try {
            for (List<String> nodeConnections : connectionData) {

                // Get target node and pointer nodes
                String targetNode = nodeConnections.get(0);
                int listLength = nodeConnections.size();
                List<String> pointerNodes = nodeConnections.subList(1, listLength);

                // Add Links from each pointer node to target node
                for (String pointerNode : pointerNodes) {
                    // Can not be refactored to only get the target node once because of Netica Node vs aliases.Node error!
                    net.getNode(targetNode).addLink(net.getNode(pointerNode));
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * Learns CPTs for the Network
     *
     * @param net:            Netica net
     * @param pathToCasefile: Path to case file
     */
    private static void learn_cpts(Net net, String pathToCasefile) {
        try {

            // Remove CPTables of nodes in net, so new ones can be learned.
            // Not really needed right now but done anyway
            NodeList nodes = net.getNodes();
            int numNodes = nodes.size();
            for (int n = 0; n < numNodes; n++) {
                Node node = (Node) nodes.get(n);
                node.deleteTables();
            }

            // Read in the case file
            Streamer caseFile = new Streamer(pathToCasefile);
            // Learn the case file
            net.reviseCPTsByCaseFile(caseFile, nodes, 1.0);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * Transforms the given csv data into case file format (e.g. the modifed format of values we use for the net)
     *
     * @param newPossibilities: Modifed possibilities
     * @param modifiedFlagMap:  Map with flags for nodes where action needs to be taken
     * @param csvdata:          Raw csv input data
     * @throws UnexpectedException
     * @return: Cases data
     */
    public static List<List<String>> buildCaseinputData(List<List<String>> newPossibilities, Map<String, Integer> modifiedFlagMap, List<List<String>> csvdata) throws UnexpectedException {
        // Get Nodes again
        List<String> nodes = Utils.extractNodes(csvdata);

        // Translate Data
        List<List<String>> inputData = new ArrayList<>(csvdata);
        inputData.remove(0);
        int index = 0;
        for (String node : nodes) {
            Integer checkValueTest = modifiedFlagMap.get(node);
            if (checkValueTest != null) {
                int checkValue = modifiedFlagMap.get(node);
                // Change Input data for this node
                for (List<String> inputLine : inputData) {
                    String newInputData = "Unexpected Error";
                    String currValue = inputLine.get(index);
                    if (checkValue == 0) {
                        // 0: "R" at start and "-" replace with "bis" for a set of numbers
                        List<String> ranges = newPossibilities.get(index);
                        String inRange = null;
                        if (currValue.equals("n.a.")) {
                            // Build for empty field

                            boolean start = true;
                            int tmpLowestEnd = 0;
                            // Find lowest Range
                            for (String range : ranges) {
                                // Get values
                                String tmpRange = new String(range.substring(1));
                                String[] numbers = tmpRange.split(Pattern.quote("bis"));
                                int lowerEnd = Integer.parseInt(numbers[0]);

                                // Set tmp Lowest initally. This has to be done because it is possible that the lowest value is not zero. Thus it has to be one of the range vars initaliy.
                                if (start == true) {
                                    start = false;

                                    tmpLowestEnd = lowerEnd;
                                }

                                // Get loweset range
                                if (lowerEnd <= tmpLowestEnd) {
                                    inRange = range;
                                    tmpLowestEnd = lowerEnd;
                                }
                            }


                        } else {
                            // Build for actual number
                            for (String range : ranges) {
                                // Get values
                                String tmpRange = new String(range.substring(1));

                                String[] numbers = tmpRange.split(Pattern.quote("bis"));
                                int lowerEnd = Integer.parseInt(numbers[0]);
                                int upperEnd = Integer.parseInt(numbers[1]);
                                // Check if in range
                                int intCurrValue = Integer.parseInt(currValue);
                                if ((lowerEnd <= intCurrValue) && (intCurrValue < upperEnd)) {
                                    inRange = range;
                                }
                            }
                        }

                        // Check range value
                        if (inRange == null) {
                            throw new UnexpectedException("Range is an unexpected value. Range is null.");
                        }
                        newInputData = inRange;
                    } else if (checkValue == 1) {
                        // 1: "R" at start and "-" replace with "bis" for strings
                        newInputData = "R" + currValue.replace("-", "bis").trim();
                    } else if (checkValue == 2) {
                        // 2: "-" replaced with ""
                        newInputData = currValue.replace("-", "").trim();
                    } else if (checkValue == 3) {
                        // 3: "N" at start
                        newInputData = "N" + currValue;
                    } else if (checkValue == 4) {
                        // 4: whitepsace within the string
                        newInputData = currValue.replace(" ", "").trim();
                    } else {
                        throw new UnexpectedException("CheckValue is an unexpected value" + checkValue);
                    }

                    // Replace Input Data with fitting input typ for modifed possibilities
                    inputLine.set(index, newInputData);

                }

            }
            index++;
        }
        return inputData;
    }

}

/*
  CVS Reader Class
 Original source Taken from: https://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
 Slightly modified to return a ist of String Lists from the csv file where each string list equals one line
  */
class CSVUtils {

    // Default vars adpated to our csv file format mentioned in the documentation
    private static final char DEFAULT_SEPARATOR = ';';
    private static final char DEFAULT_QUOTE = '"';

    /**
     * Parses a CSVFile into a List of String lists
     *
     * @param csvFile: Path to csvFile
     * @return CSV data as a list of string list whereby each string list equals one row
     * @throws FileNotFoundException
     */
    public static List<List<String>> parseCSVFile(String csvFile) throws FileNotFoundException {
        List<List<String>> lines = new ArrayList<>();

        // Build a List of String Lists from the csv file (Each string list equals one line)
        try (Scanner scanner = new Scanner(new File(csvFile))) {
            while (scanner.hasNext()) {
                List<String> line = parseLine(scanner.nextLine());
                lines.add(line);
            }
        }


        return lines;
    }

    /**
     * Original Function - No changes
     */
    public static List<String> parseLine(String cvsLine) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

    /**
     * Original Function - No changes
     */
    public static List<String> parseLine(String cvsLine, char separators, char customQuote) {

        List<String> result = new ArrayList<>();

        //if empty, return!
        if (cvsLine == null && cvsLine.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }


}


/*
  Utils Class
  Contains several non-class-specific utility functions
 */
class Utils {

    /**
     * Gets first line of csv file, aka column headers!
     *
     * @param data: parsed raw csv data
     * @return List of string that simply are the names of nodes/column headers
     */
    public static List<String> extractNodes(List<List<String>> data) {
        return data.get(0);
    }

    /**
     * This function shall create a list of possible values each node can become for the given input data
     * Data will be returned structured similar to original csv file.
     *
     * @param csvdata - contains parsed csv data as a list of string lists
     * @return List of possibilities (possibilities as a string list)
     */
    public static List<List<String>> extractPossibilities(List<List<String>> csvdata) {

        List<List<String>> possibilites = new ArrayList<>();
        // Create Copy of List without First Entry (Names of columns)
        List<List<String>> inputData = new ArrayList<>(csvdata);
        inputData.remove(0);

        // Loop through each node and get all possibilities (aka possible values this node could have)
        int index = 0;
        for (String node : extractNodes(csvdata)) {
            List<String> tmplist = extractNodePossibilities(index, inputData);
            possibilites.add(tmplist);
            index++;
        }

        return possibilites;
    }

    /**
     * This function shall loop through all input data for a give node (index) and return all distinct values that are present
     *
     * @param index:     Index for current node (basically the column)
     * @param inputData: The parsed csv data without the first list
     * @return a List of strings with all distinct values that were given in the csv file for this column
     */
    public static List<String> extractNodePossibilities(int index, List<List<String>> inputData) {

        List<String> nodePossibilities = new ArrayList<>();
        for (List<String> line : inputData) {
            String entry = line.get(index);

            // Add distinct (check if entry string is within the list already) (compare strings timed and case insensitive
            // Also filter out "n.a." statements
            if (!entry.trim().equalsIgnoreCase("n.a.") && !nodePossibilities.stream().anyMatch(str -> str.trim().equalsIgnoreCase(entry.trim()))) {
                nodePossibilities.add(entry);
            }

            //TODO think about refactoring number range creation to here or own function

        }


        return nodePossibilities;


    }

    /**
     * A custom made function to calculate all important Box Plot values that exit and utilitze them to construct ranges!
     * Math is based on 4th Semester Descriptive Statistics - Box Plot
     * Result: 3 Ranges
     * Constraints: Has to be more than 4 values
     */
    public static List<String> getRangesFromPossibilities(List<String> numberPossibilities) {
        //Convert to double list instead of string list
        List<Double> numbers = new ArrayList<>();
        for (String possibility : numberPossibilities) {
            if (possibility.equalsIgnoreCase("n.a.")) {
                continue;
            }
            numbers.add(Double.parseDouble(possibility));
        }
        Collections.sort(numbers);
        int listLength = numbers.size();

        // Get Max and Min Value
        double max = numbers.get(listLength - 1);
        double min = numbers.get(0);

        /* Median and Mean not used for ranges but could be useful to filter out bad ranges
        // Calculate Median
        double median = Utils.getMedian(numbers);

        // Calculate Mean
        double sumOfNumbers = 0;
        for (Double number : numbers) {
            sumOfNumbers += number;
        }
        double mean = sumOfNumbers / listLength;
        */

        // Get halves
        List<Double> upperHalve;
        List<Double> lowerHalve;
        if (listLength % 2 == 0) {
            upperHalve = numbers.subList(listLength / 2, listLength);
            lowerHalve = numbers.subList(0, listLength / 2);
        } else {
            upperHalve = numbers.subList(listLength / 2 + 1, listLength);
            lowerHalve = numbers.subList(0, listLength / 2);
        }
        // Get lower and upper quartile
        double lowerQ = Utils.getMedian(lowerHalve);
        double upperQ = Utils.getMedian(upperHalve);

        // Get Lower and upper limit
        double lowerLimit = lowerQ - 1.5 * (upperQ - lowerQ);
        if (lowerLimit < 0) {
            lowerLimit = 0;
        }

        double upperLimit = upperQ + 1.5 * (upperQ - lowerQ);


        // Get 3 Ranges
        String lowerRange;
        // Math rounding
        int minI = (int) Math.round(min);
        int lowerQI = (int) Math.round(lowerQ);
        int lowerLimitI = (int) Math.round(lowerLimit);
        int upperQI = (int) Math.round(upperQ);
        int upperLimitI = (int) Math.round(upperLimit);
        int maxI = (int) Math.round(max);

        if (minI < lowerLimitI) {
            lowerRange = minI + "-" + lowerQI;
        } else {
            lowerRange = lowerLimitI + "-" + lowerQI;
        }

        String upperRange;
        if (maxI > upperLimitI) {
            upperRange = upperQI + "-" + maxI + 1;
        } else {
            upperRange = upperQI + "-" + upperLimitI;
        }

        String midRange = lowerQI + "-" + upperQI;

        List<String> results = new ArrayList<>();
        results.add(lowerRange);
        results.add(midRange);
        results.add(upperRange);
        return results;


    }

    /**
     * Tests if each String in the String list can be transformed to a number
     *
     * @param possibilities List of String
     * @return True or False
     */
    public static boolean possibilitiesAreNummbers(List<String> possibilities) {
        boolean result = true;
        for (String value : possibilities) {
            if (!value.matches("-?\\d+(\\.\\d+)?")) {
                result = false;
            }
        }
        return result;
    }

    /**
     * Short function to calculate the median of a list of numbers
     *
     * @param numbers List of numbers (doubles)
     * @return A median of the list (double)
     */
    public static double getMedian(List<Double> numbers) {
        double median;
        int listLength = numbers.size();
        if (listLength % 2 == 0) {
            // If length is odd, take numbers around the middle
            // e.g. if length 14 -> 6 and 7
            // this transferred into the list index will be 5 and 6
            median = (numbers.get(listLength / 2 - 1) + numbers.get(listLength / 2)) / 2;
        } else {
            median = numbers.get(listLength / 2);
        }

        return median;
    }


}

/*
Netica Utils Code
 */
class NeticaUtils {

    /**
     * Init Netica and a Netica Net
     *
     * @param netName: Name for the Network
     * @return Netica Net
     */
    public static Net initNetica(String netName) {
        Net net = null;
        try {
            // Init
            Node.setConstructorClass("norsys.neticaEx.aliases.Node");
            Environ env = new Environ(null);
            net = new Net();
            net.setName(netName);
            return net;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return net;

    }

    /**
     * Write a netica net into a file
     *
     * @param net:        Netica Net
     * @param outputpath: Path for outputh file
     */
    public static void writeNetIntoFile(Net net, String outputpath) {
        try {
            // Write Net into File
            Streamer stream = new Streamer(outputpath);
            net.write(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deconstruct Netica Net
     *
     * @param net: A netica net
     */
    public static void deconstructNeticaNet(Net net) {
        try {
            net.finalize();  // free resources immediately and safely;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Builds a Netica Case file
     * <p>
     * From Netica:
     * The case file is an ascii text file with each case on one row, and the first row being the list of nodes as column headings.
     * Each entry is separated by a comma, space or tab.
     *
     * @param nodes:      header line node data
     * @param cases:      CSV file data transformed for case file format and modifed accordingly to possibilities
     * @param outputpath: Path and file name + ending where to write the file
     */
    public static void writeCaseFile(List<String> nodes, List<List<String>> cases, String outputpath) {

        //Build header lines
        StringBuilder nodesBuilder = new StringBuilder();
        nodesBuilder.append("IDnum");
        for (String node : nodes) {
            nodesBuilder.append("," + node);
        }
        String headerLine = nodesBuilder.toString();


        // Write Casefile
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputpath), StandardCharsets.UTF_8))) {
            // Write header line
            writer.append(headerLine + "\n");

            // Write data
            int index = 0;
            for (List<String> inputLine : cases) {

                // Build line data
                StringBuilder lineBuilder = new StringBuilder();
                lineBuilder.append(index);
                for (String element : inputLine) {
                    lineBuilder.append("," + element);
                }
                String buildInputLine = lineBuilder.toString();
                // Append at end
                writer.append(buildInputLine + "\n");
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
