import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author nanthan987 - Gowrienanthan Balarupan
 */

public final class WordNet {

    private final Digraph graph;
    private final SAP sap;
    private final HashMap<String, List<Integer>> nouns;     // mapping from noun to synsets(vertices)
    private final ArrayList<String> synsetList;             // list of all synsets

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException("Argument is null");

        In synsetStream = new In(synsets);

        int lastVertex = 0;
        ArrayList<String> synsetsListTemp = new ArrayList<>();  // temporary list of all synsets

        while (!synsetStream.isEmpty()) {
            String[] line = synsetStream.readLine().split(",");
            lastVertex = Integer.parseInt(line[0]);
            synsetsListTemp.add(line[1]);
        }

        synsetList = synsetsListTemp;

        this.nouns = initializeNouns(lastVertex + 1, synsetsListTemp);

        this.graph = initializeGraph(lastVertex + 1, hypernyms);

        this.sap = new SAP(graph);
    }

    // Initialize WordNet digraph
    private Digraph initializeGraph(int graphSize, String hypernyms) {
        // create new input stream
        In hypernymStream = new In(hypernyms);

        Digraph graphTemp = new Digraph(graphSize);
        // initialize new input stream and use it to fill digraph
        while (!hypernymStream.isEmpty()) {
            String[] edge = hypernymStream.readLine().split(",");
            for (int i = 1; i < edge.length; i++) {
                graphTemp.addEdge(Integer.parseInt(edge[0]), Integer.parseInt(edge[i]));
            }
        }

        // makes sure digraph is a Directed Acyclic Graph
        DirectedCycle detectCycle = new DirectedCycle(graphTemp);
        if (detectCycle.hasCycle() || !isRooted(graphTemp))
            throw new IllegalArgumentException("Digraph is not a Rooted DAG");

        return graphTemp;
    }

    // Initialize {@code noun} instance variable
    private HashMap<String, List<Integer>> initializeNouns(int graphSize, ArrayList<String> synsets) {

        HashMap<String, List<Integer>> nounsTemp = new HashMap<>(); // mapping from noun to every synset it belongs to
        SET<String> nounSet = new SET<>();                          // to check if a noun has already been added to the hashmap


        for (int i = 0; i < graphSize; i++) {
            String synset = synsets.get(i);

            ArrayList<String> nounsInSynset = new ArrayList<>(Arrays.asList(synset.split(" ")));

            for (String noun : nounsInSynset) {

                if (!nounSet.contains(noun)) {
                    nounSet.add(noun);
                    nounsTemp.put(noun, new ArrayList<>(Arrays.asList(i)));
                } else {
                    List<Integer> copy = nounsTemp.get(noun);
                    copy.add(i);
                    nounsTemp.put(noun, copy);
                }
            }
        }

        return nounsTemp;
    }

    // checks if digraph has only one root
    private boolean isRooted(Digraph G) {
        int roots = 0;
        for (int i = 0; i < G.V(); i++) {
            if (G.outdegree(i) == 0) {
                roots++;
                if (roots > 1) return false;
            }
        }
        return roots == 1;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return nouns.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null && nounB == null) throw new IllegalArgumentException();
        if (!isNoun(nounA) && !isNoun(nounB)) throw new IllegalArgumentException();

        List<Integer> a = nouns.get(nounA);
        List<Integer> b = nouns.get(nounB);

        return sap.length(a, b);

    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null && nounB == null) throw new IllegalArgumentException();
        if (!isNoun(nounA) && !isNoun(nounB)) throw new IllegalArgumentException();

        List<Integer> a = nouns.get(nounA);
        List<Integer> b = nouns.get(nounB);


        int ancestor = sap.ancestor(a, b);
        if (ancestor != -1) {
            return synsetList.get(ancestor);
        } else return null;


    }

    // do unit testing of this class
    public static void main(String[] args) {
        String synsets = args[0];
        String hypernyms = args[1];

        WordNet wordNet = new WordNet(synsets, hypernyms);

        StdOut.println(wordNet.sap("Hemofil", "antihaemophilic_globulin"));
        StdOut.println(wordNet.distance("Apollo", "Zeus"));

    }

}
