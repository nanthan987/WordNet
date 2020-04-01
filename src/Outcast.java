import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public final class Outcast {
    private final WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        if (wordnet == null) throw new IllegalArgumentException();

        this.wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        if (nouns == null) throw new IllegalArgumentException();

        int maxDistance = 0;
        String outcast = null;
        for (String noun : nouns) {
            int distance = 0;
            for (String otherNoun : nouns) {
                if (noun.equals(otherNoun)) continue;
                distance += wordNet.distance(noun, otherNoun);
            }
            if (distance > maxDistance) {
                maxDistance = distance;
                outcast = noun;
            }
        }
        return outcast;

    }

    // test client
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
