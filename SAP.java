import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public final class SAP {
    private final Digraph graph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();

        // defensive copy of G
        this.graph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validateVertex(v);
        validateVertex(w);

        SAPSearch sapSearch = new SAPSearch(graph, v, w);
        return sapSearch.getLength();
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validateVertex(v);
        validateVertex(w);

        SAPSearch sapSearch = new SAPSearch(graph, v, w);
        return sapSearch.getSCA();
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();

        for (Integer i : v) {
            if (i == null) throw new IllegalArgumentException();
        }
        for (Integer j : w) {
            if (j == null) throw new IllegalArgumentException();
        }

        validateVertices(v);
        validateVertices(w);

        SAPSearch sapSearch = new SAPSearch(graph, v, w);
        return sapSearch.getLength();
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();

        for (Integer i : v) {
            if (i == null) throw new IllegalArgumentException();
        }
        for (Integer j : w) {
            if (j == null) throw new IllegalArgumentException();
        }
        validateVertices(v);
        validateVertices(w);

        SAPSearch sapSearch = new SAPSearch(graph, v, w);
        return sapSearch.getSCA();
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= graph.V())
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (graph.V() - 1));
    }

    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int V = graph.V();
        for (int v : vertices) {
            if (v < 0 || v >= V) {
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
            }
        }
    }


    private class SAPSearch {
        private int sca;
        private int length;

        private SAPSearch(Digraph G, int v, int w) {
            BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
            BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

            search(bfsV, bfsW);


        }

        private SAPSearch(Digraph G, Iterable<Integer> v, Iterable<Integer> w) {
            BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
            BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

            search(bfsV, bfsW);
        }

        private void search(BreadthFirstDirectedPaths bfsV, BreadthFirstDirectedPaths bfsW) {
            int scaCopy = -1;
            int lengthCopy = -1;
            int minDistance = Integer.MAX_VALUE;

            for (int i = 0; i < graph.V(); i++) {
                if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                    int totalDistance = bfsV.distTo(i) + bfsW.distTo(i);
                    if (totalDistance < minDistance) {
                        minDistance = totalDistance;
                        scaCopy = i;
                        lengthCopy = minDistance;
                    }
                }
            }
            sca = scaCopy;
            length = lengthCopy;
        }


        private int getSCA() {
            return sca;
        }

        private int getLength() {
            return length;
        }
    }


    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);

        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }


}
