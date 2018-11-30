package edu.sdsu.cs;

import edu.sdsu.cs.datastructures.DirectedGraph;
import edu.sdsu.cs.datastructures.IGraph;

import java.io.*;
import java.util.List;
import java.util.Scanner;

/**
 * Program 3
 * Michael Kemper
 * <p>
 * <p>
 * Juan Pina-Sanz
 * cssc0835*
 */

public class App {
    private static File currFile;

    public static void main(String args[]) {
        System.out.println("CS310: Program 3\nThe Graph and Shortest Path");
//        System.out.println("The Graph and Shortest Path");

        if (args.length == 0) {
            File defaultFile = new File("layout.csv");
            if (readFile(defaultFile)) {
                currFile = new File("layout.csv");
            }
        } else {
            File testFile = new File(args[0]);
            String path = args[0];
            if (readFile(testFile)) {
                currFile = new File(path);
            }
        }

        IGraph graph = new DirectedGraph<>();
        buildGraph(currFile, graph);
        ((DirectedGraph) graph).printAdjList();
        System.out.println(graph.toString());
        Scanner scan = new Scanner(System.in);
        System.out.println("Select a Start and End Location:\n");
        String start = "";
        String end = "";

        System.out.println("Start: \n");
        start = scan.nextLine();
        System.out.println("End: \n");
        end = scan.nextLine();
        if (!validateVertices(graph, start, end)) {
            System.out.println("Start: \n");
            start = scan.nextLine();
            System.out.println("End: \n");
            end = scan.nextLine();
        } else if (validateVertices(graph, start, end) && !graph.isConnected
                (start, end)) {
            System.out.println(start + " and " + end + " are not connected. " +
                    "\n Try again: ");
            System.out.println("Start: \n");
            start = scan.nextLine();
            System.out.println("End: \n");
            end = scan.nextLine();
        } else if (validateVertices(graph, start, end) && graph.isConnected
                (start, end)) {
            printShortestPath(graph, start, end);
            scan.close();
        }

        System.exit(0);
    }

    private static boolean readFile(File csv) {
        try {
            if (!csv.canRead() || isCSV(csv)) {
                throw new IOException();
            }
            if (csv.canRead()) {
                return true;
            }
        } catch (IOException e) {
            System.out.println("Unable to read: "
                    + csv.getName() + "\n" + "Verify the file exists, is" +
                    " accessible, and meets syntax requirements");
            System.exit(0);
        }
        return false;
    }

    private static boolean isCSV(File file) {
        String name = file.getName();
        String ext = name.substring(name.lastIndexOf('.') + 1);
        if (ext != "csv") {
            return false;
        } else {
            return true;
        }
    }

    private static void buildGraph(File file, IGraph graph) {

        BufferedReader csvReader;
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

    private static void printShortestPath(IGraph graph, String start,
                                          String end) {
        List shortestPath = graph.shortestPath(start, end);
        String path = "";
        int distance = -1;
        for (Object vertex : shortestPath) {
            path += " -> " + vertex.toString();
            distance += 1;
        }

        System.out.println("The shortest path: " + path + "\nDistance: "
                + distance);
    }
}

