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
            NeticaUtils.build_net(nodes, possibilities);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

/*
Netica Demo Code
 */
class NeticaUtils {
    private static final String projectRootPath = new File(System.getProperty("user.dir")).getParentFile().getAbsolutePath();


    public static void build_net(List<String> nodes, List<List<String>> possibilities) {
        try {


            // Init
            Node.setConstructorClass("norsys.neticaEx.aliases.Node");
            Environ env = new Environ(null);
            Net net = new Net();
            net.setName("BayesInsurance");

            // Build nodes
            List<Node> nodesList = new ArrayList<>();
            List<Node> finalNodesList = new ArrayList<>();

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

                    index++;
                    continue;
                }

                // Build string from array
                StringBuilder builder = new StringBuilder();
                for (String value : possibilities.get(index)) {
                    //TODO find out why "-" is an illegal character
                    value = value.replace("-", "").trim();

                    // Check if String is a nubmer
                    if (value.matches("-?\\d+(\\.\\d+)?")) {
                        value = "N" + value;
                    }

                    builder.append("," + value);
                }
                String tmpPossib = builder.toString();

                Node tmpNode = new Node(node, tmpPossib, net);


                nodesList.add(tmpNode);
                index++;
            }

            System.out.println(nodesList);
            System.out.println(finalNodesList);

            // Build Links (Every node will link to both final nodes)
            for (Node finalNode : finalNodesList) {
                for (Node node : nodesList) {
                    finalNode.addLink(node);
                }
            }

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

