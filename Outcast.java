import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private WordNet wordnet; // wordnet obj in which to find outcast

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        String outcast = null;
        int currentD = 0;
        int maxD = 0;
        // calculate the distance from each noun to all the others
        for (int i = 0; i < nouns.length; i++) {
            for (int j = 0; j < nouns.length; j++) {
                currentD += wordnet.distance(nouns[i], nouns[j]);
            }
            // update maxD if this noun holds the new greatest distance
            if (currentD > maxD) {
                maxD = currentD;
                outcast = nouns[i];
            }
            // reset currentD to 0 before next iteration
            currentD = 0;
        }

        // return noun with max distance
        return outcast;
    }

    // test client from assignment test client
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
