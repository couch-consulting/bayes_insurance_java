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

// Import for CSVUtils
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        String fullPath = Paths.get(projectRootPath, "data", "versicherung_a.csv").toString();

        try {
            List<List<String>> csvdata = CSVUtils.parseCSVFile(fullPath);

            // Get Nodes
            List<String> nodes = Utils.extractNodes(csvdata);
            // Get possibilities for all Nodes
            List<List<String>> possibilities = Utils.extractPossibilities(csvdata);

            //build network
            NeticaNetBuilder.build_net(nodes, possibilities);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}


class NeticaNetBuilder {
    private static final String projectRootPath = new File(System.getProperty("user.dir")).getParentFile().getAbsolutePath();
    private static List<Node> nodesList = new ArrayList<>();
    private static List<Node> finalNodesList = new ArrayList<>();
    private static List<String> nodes = new ArrayList<>();
    private static List<List<String>> possibilities = new ArrayList<>();

    public static void build_net(List<String> tmpNodes, List<List<String>> tmpPossibilities) {
        try {

            // Init
            Node.setConstructorClass("norsys.neticaEx.aliases.Node");
            Environ env = new Environ(null);
            Net net = new Net();
            net.setName("BayesInsurance");
            nodes = tmpNodes;
            possibilities = tmpPossibilities;


            // Build nodes
            build_nodes(net);
            //Add Links
            add_links();


            System.out.println(nodesList);
            System.out.println(finalNodesList);


            /*

            // Set Title for Nodes
            visitAsia.setTitle("Visit to Asia");
            cancer.setTitle("Lung Cancer");
            tbOrCa.setTitle("Tuberculosis or Cancer");

            // Set Title for Node possiblities
            visitAsia.state("visit").setTitle("Visited Asia within the last 3 years");

            // Add Links between nodes
            tuberculosis.addLink(visitAsia); // link from visitAsia to tuberculosis
            cancer.addLink(smoking);
            tbOrCa.addLink(tuberculosis);
            tbOrCa.addLink(cancer);
            xRay.addLink(tbOrCa);
            dyspnea.addLink(tbOrCa);
            bronchitis.addLink(smoking);
            dyspnea.addLink(bronchitis);

            // Build CPTs
            */


            // Write Net into File
            String outputpath = Paths.get(projectRootPath, "data", "BayesInsurance.dne").toString();
            Streamer stream = new Streamer(outputpath);
            net.write(stream);

            net.finalize();  // free resources immediately and safely; not strictly necessary, but a good habit
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void build_nodes(Net net) {
        try {
            // Build nodes
            int index = 0;
            int listLength = nodes.size() - 1;
            for (String node : nodes) {

                // Build Final nodes for each possibility of final node
                if (index == listLength) {
                    for (String finalePossibility : possibilities.get(index)) {
                        //Format String
                        finalePossibility = finalePossibility.replace("-", "").trim();
                        // Check if String is a nubmer
                        if (finalePossibility.matches("-?\\d+(\\.\\d+)?")) {
                            finalePossibility = "N" + finalePossibility;
                        }
                        Node tmpNode = new Node(node + finalePossibility, finalePossibility, net);
                        finalNodesList.add(tmpNode);
                    }

                } else {
                    // Build string from array
                    StringBuilder builder = new StringBuilder();
                    boolean first = true;

                    // Check if possibilities are  a set of numbers and if they are a set, transfrom into 3 Ranges instead
                    List<String> currentPossibilites = new ArrayList<>(possibilities.get(index));
                    if (possibilitiesAreNummbers(currentPossibilites)){
                        currentPossibilites = getRangesFromPossibilities(currentPossibilites);
                    }

                    for (String value : currentPossibilites) {
                        //TODO find out why "-" is an illegal character
                        // Eliminate illegal char "-"
                        if (value.contains("-")) {
                            // For a number replace to "bis"
                            if (value.replace("-", "").trim().matches("-?\\d+(\\.\\d+)?")) {
                                value = "R" + value.replace("-", "bis").trim();
                            } else {
                                value = value.replace("-", "").trim();
                            }
                        }

                        // Check if String is a nubmer
                        if (value.matches("-?\\d+(\\.\\d+)?")) {
                            value = "R" + value;
                        }

                        //Only append "," if it is not the first value
                        if (first) {
                            builder.append(value);
                            first = false;
                        } else {
                            builder.append("," + value);
                        }
                    }
                    String tmpPossib = builder.toString();
                    System.out.println(tmpPossib);


                    //Cread node and add to node list
                    Node tmpNode = new Node(node, tmpPossib, net);
                    nodesList.add(tmpNode);

                }


                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests if each String in the String list can be transformed to a number
     * @param possibilities List of String
     * @return True or False
     */
    private static boolean possibilitiesAreNummbers (List<String> possibilities){
        boolean result = true;
        for (String value : possibilities) {
            if(!value.matches("-?\\d+(\\.\\d+)?")){
                result = false;
            }
        }
        return result;
    }


    private static void add_links() {
        try {

            // Build Links (Every node will link to both final nodes)
            for (Node finalNode : finalNodesList) {
                for (Node node : nodesList) {
                    finalNode.addLink(node);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A custom made function to calculate all important Box Plot values that exit and utilitze them to construct ranges!
     * Math is based on 4th Semester Descriptive Statistics - Box Plot
     * Result: 3 Ranges
     * Constraints: Has to be more than 4 values
     */
    private static List<String> getRangesFromPossibilities(List<String> numberPossibilities) {
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
        double median = getMedian(numbers);

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
        double lowerQ = getMedian(lowerHalve);
        double upperQ = getMedian(upperHalve);

        // Get Lower and upper limit
        double lowerLimit = lowerQ - 1.5 * (upperQ - lowerQ);
        if (lowerLimit < 0){
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
        if (maxI > upperLimitI){
            upperRange = upperQI + "-" + maxI;
        }else {
            upperRange = upperQI + "-" + upperLimitI;
        }

        String midRange =  lowerQI + "-" + upperQI;

        List<String> results = new ArrayList<>();
        results.add(lowerRange);
        results.add(midRange);
        results.add(upperRange);
        return results;




    }

    private static double getMedian(List<Double> numbers) {
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
  CVS Reader Class
 Original source Taken from: https://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
 Slightly modified to return a ist of String Lists from the csv file where each string list equals one line
 */
class CSVUtils {

    private static final char DEFAULT_SEPARATOR = ';';
    private static final char DEFAULT_QUOTE = '"';

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

    public static List<String> parseLine(String cvsLine) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

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
  Contains several non-specific utility functions
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

            //TODO Think about additional feature extraction for just numbers and not categories

        }


        return nodePossibilities;


    }
}


/*
Netica Demo Code - stored for later
 */
class NeticaUtils {


}
