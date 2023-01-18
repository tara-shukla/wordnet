import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class WordNet {
    private Digraph digraph; // wordnet DAG
    private HashMap<Integer, String> synsetList; // keeps track of ID and synset
    private HashMap<String, Stack<Integer>> nouns; // connects nouns to hypernyms
    private ShortestCommonAncestor sca; // sca for two WordNet nouns

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null) {
            throw new IllegalArgumentException("Synsets file cannot be null");
        }
        if (hypernyms == null) {
            throw new IllegalArgumentException("Hypernyms file cannot be null");
        }

        synsetList = new HashMap<Integer, String>();
        nouns = new HashMap<String, Stack<Integer>>();

        // call helper functions to build 'synsetList' and 'nouns', construct digraph
        synsetBuilder(synsets);
        digraph = new Digraph(synsetList.size());
        hypernymBuilder(hypernyms);

        sca = new ShortestCommonAncestor(digraph);
    }

    // read synsets file and builds the 'synsetList' and 'nouns' hashMap
    private void synsetBuilder(String synsets) {
        In input1 = new In(synsets);
        // read each synset from the file, line by line
        while (!input1.isEmpty()) {
            String synset = input1.readLine(); // read the synset information
            String[] line = synset.split(","); // find ID and synset separately

            int synsetID = Integer.parseInt(line[0]);
            // pair synsetID to the synset
            synsetList.put(synsetID, line[1]);

            String[] synsetNouns = line[1].split(" ");
            // add each noun in every synset to the 'nouns' hashmap
            for (String a : synsetNouns) {
                // checks for duplicate keys
                if (!nouns.containsKey(a)) {
                    Stack<Integer> hypernymList = new Stack<Integer>();
                    hypernymList.push(synsetID); // add this noun's synsetID
                    // pair the noun to its ID and hypernyms
                    nouns.put(a, hypernymList);

                }
                // append it to the currently stored stack/queue
                else {
                    nouns.get(a).push(synsetID);
                }
            }
        }
    }

    // read hypernyms file and construct wordnet digraph
    private void hypernymBuilder(String hypernyms) {
        In input2 = new In(hypernyms);
        // create digraph with V = total # of nouns
        while (!input2.isEmpty()) {
            String[] line = input2.readLine().split(",");
            for (int i = 1; i < line.length; i++) {
                digraph.addEdge(Integer.parseInt(line[0]), Integer.parseInt(line[i]));
            }

        }
    }

    // the set of all WordNet nouns
    public Iterable<String> nouns() {
        return this.nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("Word cannot be null");
        }
        return this.nouns.containsKey(word);
    }

    // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {
        if (!isNoun(noun1)) {
            throw new IllegalArgumentException("Noun1 is not a wordNet noun");
        }
        if (!isNoun(noun2)) {
            throw new IllegalArgumentException("Noun2 is not a wordNet noun");
        }

        // calculate SCA synsetID
        int scaID = sca.ancestorSubset(nouns.get(noun1), nouns.get(noun2));

        return synsetList.get(scaID);
    }

    // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {
        if (!isNoun(noun1)) {
            throw new IllegalArgumentException("Noun1 is not a wordNet noun");
        }
        if (!isNoun(noun2)) {
            throw new IllegalArgumentException("Noun2 is not a wordNet noun");
        }

        int dist = sca.lengthSubset(nouns.get(noun1), nouns.get(noun2));
        return dist;
    }


    // unit testing (required)
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        StdOut.println(wordnet.nouns());
        // args[0] = synsets6.txt, args[1] = hypernyms6TwoAncestors.txt
        // should print: [a, b, c, d, e, f]

        StdOut.println(wordnet.isNoun("hoop")); // true
        StdOut.println(wordnet.isNoun("lounging_jacket")); // false

        StdOut.println(wordnet.sca("b", "f")); //
        StdOut.println(wordnet.sca("b", "c")); //

        StdOut.println(wordnet.distance("hoop", "lounging_jacket"));


    }
}
