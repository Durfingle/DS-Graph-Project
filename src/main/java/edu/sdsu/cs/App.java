package edu.sdsu.cs;

import edu.sdsu.cs.datastructures.DirectedGraph;
import edu.sdsu.cs.datastructures.IGraph;

import java.io.*;
import java.util.Scanner;

/**
 * Michael Kemper
 *
 *
 * Juan Pina-Sanz
 * cssc0835*
 */

public class App {
    private static File currFile;

    public static void main(String args[]) {
        System.out.println("CS310: Program 3");
        System.out.println("The Graph and Shortest Path");
        System.out.println("Please enter the file directory of the CSV you " +
                "want to graph: \n");

        Scanner scan = new Scanner(System.in);
        String input;
        input = scan.nextLine();
        if (readFile(input)) {
            currFile = new File(input);
        }
        IGraph graph = new DirectedGraph<>();
        buildGraph(currFile, graph);
        System.out.println(graph.toString());

        System.out.println("Select a Start and End Location:\n");
        String start = "";
        String end = "";
        while (!validateVertices(graph, start, end)) {
            System.out.println("Start: \n");
            start = scan.nextLine();
            System.out.println("End: \n");
            end = scan.nextLine();
        }

        scan.close();
    }

    private static boolean readFile(String path) {
        try {
            File csv = new File(path);
            if (!csv.canRead()) {
                throw new IOException("Unable to read: "
                        + csv.getName() + "\n" + "Verify the file exists, is" +
                        " accessible, and meets syntax requirements");
            }
            if (csv.canRead()) {
                return true;
            }
        } catch (IOException e) {
            System.exit(0);
        }
        return false;
    }

    private boolean readFile(File csv) {
        try {
            if (!csv.canRead()) {
                throw new IOException("Unable to read: "
                        + csv.getName() + "\n" + "Verify the file exists, is" +
                        " accessible, and meets syntax requirements");
            }
            if (csv.canRead()) {
                return true;
            }
        } catch (IOException e) {
            System.exit(0);
        }
        return false;
    }

    /**
     * build graph based on the input line. Read value with delimiter ","
     * if the value is "value" + ","
     *
     * @param graph
     */
    private static void buildGraph(File file, IGraph graph) {

        BufferedReader csvReader = null;
        try {
            String currLine = "";
            csvReader = new BufferedReader(new FileReader(file));

            while (currLine != null) {
                currLine = csvReader.readLine();
                String[] lineLst = currLine.split(",");

                for (String str : lineLst) {
                    if (!graph.contains(str)) {
                        graph.add(str);
                    }
                }
                if (lineLst.length > 1) {
                    graph.connect(lineLst[0], lineLst[1]);
                }

            }
        } catch (Exception e) {
        }
    }

    private static boolean validateVertices(IGraph graph, String start,
                                            String end) {

        if (graph.contains(start) && graph.contains(end)) {
            return true;
        } else if (!graph.contains(start) && graph.contains(end)) {
            System.out.println(start + " Not Found, Try Again");
            return false;
        } else if (!graph.contains(end) && graph.contains(start)) {
            System.out.println(end + " Not Found, Try Again");
            return false;
        } else if (!graph.contains(start) && graph.contains(end)) {
            System.out.println(start + " and " + end + " Not Found Try Again");
            return false;
        }
        return false;
    }


}