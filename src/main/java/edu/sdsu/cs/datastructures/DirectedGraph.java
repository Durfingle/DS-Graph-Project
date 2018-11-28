package edu.sdsu.cs.datastructures;

import java.util.*;

public class DirectedGraph<V> implements IGraph<V> {

    Map<V, LinkedList<V>> listOfItems = new TreeMap();

    public DirectedGraph() {

    }

    @Override
    public void add(V vertexName) {
        listOfItems.put((V) vertexName, new LinkedList<V>());
    }

    @Override
    public void connect(V start, V destination) {
        try {
            LinkedList<V> updatedGraph = listOfItems.get((V) start);
            boolean tmp = listOfItems.containsKey((V) destination);
            if(!tmp)
                throw new NoSuchElementException();
            updatedGraph.add((V) destination);

            listOfItems.put((V) start, updatedGraph);
        }
        catch (Exception e) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void clear() {
        listOfItems.clear();
    }

    @Override
    public boolean contains(V label) {
        if (listOfItems.containsKey(label)) {
            return true;
        }
        return false;
    }

    @Override
    public void disconnect(V start, V destination) {
        try {
            LinkedList<V> updatedGraph = listOfItems.get((V) start);
            boolean tmp = updatedGraph.contains((V) destination);
            if(!tmp)
                throw new NoSuchElementException();
            updatedGraph.remove((V) destination);
            listOfItems.put((V) start, updatedGraph);
        }
        catch (Exception e) {
            throw new NoSuchElementException();
        }
    }

    private Set<V> checkVisited(List<V> graph, Set<V> visitedItems) {
        Set<V> finalVisitedItems = new HashSet<>(visitedItems);
        for(V item: graph) {
            if (!finalVisitedItems.contains(item)) {
                finalVisitedItems.add(item);
                finalVisitedItems.addAll(checkVisited(listOfItems.get(item),finalVisitedItems));
            }
        }
        return finalVisitedItems;
    }

    @Override
    public boolean isConnected(V start, V destination) {
        List<V> updatedGraph = listOfItems.get((V) start);
        Set<V> visitedItems = new HashSet<>();
        visitedItems = checkVisited(updatedGraph,visitedItems);
        return visitedItems.contains((V) destination);
    }

    @Override
    public Iterable<V> neighbors(V vertexName) {
        try {
            List<V> neighborList = listOfItems.get((V) vertexName);
            boolean tmp = listOfItems.containsKey((V) vertexName);
            if(!tmp)
                throw new NoSuchElementException();
            return neighborList;
        }
        catch (Exception e) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void remove(V vertexName) {
        if(!listOfItems.containsKey(vertexName)) {
            throw new NoSuchElementException();
        }
        listOfItems.remove((V) vertexName);
        for(V key: listOfItems.keySet()) {
            LinkedList<V> newList = listOfItems.get(key);
            if(newList.contains(vertexName)) {
                newList.remove(vertexName);
                listOfItems.put(key, newList);
            }
        }
    }

    public void printList(List<V> list) {
        for(V item: list) {
            System.out.print(" | " + item + " | ");
        }
        System.out.println();
    }

    public LinkedList<V> findShortestWithQueueNew(V itemToFind, V startingItem) {
        Set<V> visited = new HashSet<>();
        LinkedList<V> queue = new LinkedList<>();
        Map<V, Integer> distanceMap = new TreeMap<>();
        Map<V, LinkedList<V>> pathMap = new TreeMap<>();
        queue.add(startingItem);
        visited.add(startingItem);
        distanceMap.put(startingItem, 0);
        LinkedList<V> tmpStartList = new LinkedList<>();
        tmpStartList.add(startingItem);
        pathMap.put(startingItem, tmpStartList);
        boolean done = false;
        while(!queue.isEmpty() && !done) {
            V currentItem = queue.poll();
            for(V item: listOfItems.get(currentItem)) {
                if(!visited.contains(item)) {
                    LinkedList<V> tmpNewList = new LinkedList<>(pathMap.get(currentItem));
                    visited.add(item);
                    queue.add(item);
                    tmpNewList.add(item);
                    pathMap.put(item, tmpNewList);
                    distanceMap.put(item, distanceMap.get(currentItem) + 1);
//                  System.out.println(item.toString() + " - Distance: " + distanceMap.get(item));
                    if(item.equals(itemToFind)) {
                        return(pathMap.get(item));
                    }
                }
            }
        }
        throw new NoSuchElementException();
    }



    public void printAdjList() {
        for(V key: listOfItems.keySet()) {
           List<V> currentList = listOfItems.get(key);
            System.out.print("Vertex: " + key + " --> ");
            for(V item: currentList) {
               System.out.print(" | " + item + " | ");
           }
            System.out.println();

        }
    }


    @Override
    public List shortestPath(V start, V destination) {
        return findShortestWithQueueNew(destination, start);
    }

    @Override
    public int size() {
        return listOfItems.keySet().size();
    }

    @Override
    public Iterable<V> vertices() {
        List<V> verticesList = new LinkedList<>();
        for(V item: listOfItems.keySet()) {
            verticesList.add(item);
        }
        return verticesList;
    }

    @Override
    public IGraph<V> connectedGraph(V origin) {
        List<V> updatedGraph = listOfItems.get((V) origin);
        Set<V> visitedItems = new HashSet<>();
        visitedItems.add(origin);
        visitedItems = checkVisited(updatedGraph,visitedItems);
        IGraph<V> directedGraph = new DirectedGraph<>();
        for(V item: visitedItems) {
            directedGraph.add((V) item);
        }
        for(V item: visitedItems) {
            for(V connectedItem: listOfItems.get((V)item)) {
                directedGraph.connect(item, connectedItem);
            }
        }
        return directedGraph;
    }

    public static void main( String[] args )
    {
        DirectedGraph<Integer> dg = new DirectedGraph();
        dg.add(0);
        dg.add(1);
        dg.add(2);
        dg.add(3);
        dg.add(4);
        dg.add(5);
        dg.add(6);
        dg.add(7);
        dg.add(8);

        dg.connect(0,5);
        dg.connect(0,3);
        dg.connect(3,4);
        dg.connect(4,1);
        dg.connect(1,2);
        dg.connect(2,1);
        dg.connect(2,3);
        dg.connect(2,0);
        dg.connect(5,6);
        dg.connect(6,7);
        dg.connect(7,8);
        dg.connect(8,2);

        dg.printAdjList();

//        dg.connect(0,1);
//        dg.connect(1,2);
//        dg.connect(2,3);
//        dg.connect(3,4);



        //List<Integer> tmpUpdatedGraph = dg.listOfItems.get(4);
        //Map<Integer,Integer> map = dg.initEmptyDistanceMap();
        //Map<Integer,Integer> finalMap = dg.findShortest(4, 4, 0, map);
        //System.out.println("Final distance: " + finalMap.get(4));

        dg.findShortestWithQueueNew(2,0);

    }
}
