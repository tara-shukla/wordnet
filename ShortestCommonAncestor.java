import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class ShortestCommonAncestor {

    private Digraph G; // copy of given G
    private int length; // shortest path between v and w
    private int sca; // shortest common ancestor of v and w

    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("Given digraph is null");
        }
        // check if G is a rooted DAG
        if (!isAcyclicRooted(G)) {
            throw new IllegalArgumentException("G must be a rooted DAG!");
        }
        length = Integer.MAX_VALUE;
        this.G = new Digraph(G);
    }

    // check that digraph is rooted and vertices are connected
    private boolean isAcyclicRooted(Digraph digraph) {
        DirectedCycle cycle = new DirectedCycle(digraph);
        if (cycle.hasCycle()) return false;

        int verts = digraph.V();
        int roots = 0;
        int notRoots = 0;
        for (int i = 0; i < verts; i++) {
            if (digraph.outdegree(i) != 0) notRoots++;
            else roots++;
        }

        if (notRoots == verts - 1 && roots == 1) return true;
        return false;
    }

    // adapted from Digraph.java (algs4.cs.princeton.edu)
    // throws IllegalArgumentException if vertex does not exist
    private void validate(int v) {
        if (v < 0 || v >= G.V()) {
            throw new IllegalArgumentException("v is not in the digraph");
        }
    }

    // throws IllegalArgumentException if a vertex in the subset does not exist
    private void validate(Iterable<Integer> verts) {
        if (verts == null) {
            throw new IllegalArgumentException("subset can't be null!");
        }
        int count = 0;
        for (Integer v : verts) {
            if (v == null) {
                throw new IllegalArgumentException("vertices in subset can't be null");
            }
            if (v < 0 || v >= G.V()) {
                throw new IllegalArgumentException("v is not in the digraph");
            }
            count++;
        }
        if (count == 0) throw new IllegalArgumentException("subset can't be empty!");
    }

    // calls bfs for two distinct vertices
    private void regularBFS(int v, int w) {
        Queue<Integer> vQ = new Queue<Integer>();
        Queue<Integer> wQ = new Queue<Integer>();

        // enqueue v and w to their respective queues
        vQ.enqueue(v);
        wQ.enqueue(w);

        bfs(vQ, wQ);
    }

    // calls bfs for two subsets
    private void subsetBFS(Iterable<Integer> vSet, Iterable<Integer> wSet) {
        Queue<Integer> vQ = new Queue<Integer>();
        Queue<Integer> wQ = new Queue<Integer>();
        // enqueue all elements of subsets to respective queues
        for (int i : vSet) {
            vQ.enqueue(i);
        }
        for (int i : wSet) {
            wQ.enqueue(i);
        }
        bfs(vQ, wQ);
    }

    // implements bfs
    private void bfs(Queue<Integer> vQ, Queue<Integer> wQ) {

        // keeps track of V-accessed path
        boolean[] markedV = new boolean[G.V()];
        // keeps track of W-accessed path
        boolean[] markedW = new boolean[G.V()];

        // records distance to v
        int[] distToV = new int[G.V()];
        // records distance to w
        int[] distToW = new int[G.V()];


        // marks ints in queue as marked, distance = 0
        for (int i : vQ) {
            markedV[i] = true;
            distToV[i] = 0;
        }
        for (int i : wQ) {
            markedW[i] = true;
            distToW[i] = 0;
        }

        // while either queue has elements, alternately check if the reachable
        // vertices from v and w collide; else, enqueue more adj vertices
        while (!vQ.isEmpty() || !wQ.isEmpty()) {
            if (!vQ.isEmpty()) {
                // dequeue vQ and check if marked by W's side
                int current = vQ.dequeue();
                if (markedW[current]) {
                    // if so, update sca and length
                    int distance = distToV[current] + distToW[current];
                    if (distance < length) {
                        sca = current;
                        length = distance;
                    }

                    // corner case catch
                    for (int adj : G.adj(current)) {
                        int newD = distToW[adj] + 1 + distToV[current];
                        if (markedW[adj] && newD < length) {
                            sca = adj;
                            length = newD;
                        }
                    }

                }
                // else enqueue current's adjacent vertices
                else {
                    for (int adj : G.adj(current)) {
                        if (!markedV[adj]) {
                            vQ.enqueue(adj);
                            markedV[adj] = true;
                            distToV[adj] = distToV[current] + 1;
                        }

                    }
                }
            }
            if (!wQ.isEmpty()) {
                // dequeue wQ and check if marked by V's side
                int current = wQ.dequeue();
                if (markedV[current]) {
                    // if so, update sca and length
                    int distance = distToV[current] + distToW[current];

                    if (distance < length) {
                        sca = current;
                        length = distance;
                    }

                    // corner case catch
                    for (int adj : G.adj(current)) {
                        int newD = distToV[adj] + 1 + distToW[current];
                        if (markedV[adj] && newD < length) {
                            sca = adj;
                            length = newD;

                        }
                    }
                }

                // else enqueue current's adjacents
                else {
                    for (int adj : G.adj(current)) {
                        if (!markedW[adj]) {
                            wQ.enqueue(adj);
                            markedW[adj] = true;
                            distToW[adj] = distToW[current] + 1;
                        }
                    }
                }
            }
        }
    }


    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        validate(v);
        validate(w);

        length = Integer.MAX_VALUE;
        sca = -1;
        regularBFS(v, w);
        return length;
    }


    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        validate(v);
        validate(w);

        length = Integer.MAX_VALUE;
        sca = -1;
        regularBFS(v, w);
        return sca;
    }

    // length of shortest ancestral path of vertex subsets A and B
    public int lengthSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        validate(subsetA);
        validate(subsetB);

        length = Integer.MAX_VALUE;
        sca = -1;
        subsetBFS(subsetA, subsetB);
        return length;
    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestorSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        validate(subsetA);
        validate(subsetB);

        length = Integer.MAX_VALUE;
        sca = -1;
        subsetBFS(subsetA, subsetB);
        return sca;
    }

    // unit testing (required)
    // adapted from assignment description test client
    public static void main(String[] args) {
        In readFile = new In(args[0]);
        Digraph G = new Digraph(readFile);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);

        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);

            // integer subset testing
            List<Integer> vsub = new ArrayList<>();
            vsub.add(v);
            List<Integer> wsub = new ArrayList<>();
            wsub.add(w);
            int subsetL = sca.lengthSubset(vsub, wsub);
            int subsetAnc = sca.ancestorSubset(vsub, wsub);
            StdOut.println("Subsets:");
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }

    }

}
