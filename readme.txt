Programming Assignment 6: WordNet


/* *****************************************************************************
 *  Describe concisely the data structure(s) you used to store the
 *  information in synsets.txt. Why did you make this choice?
 **************************************************************************** */

We used a hashmap <Integer, String> to store information in synsets.txt.
We chose this because it provides a way to connect the synsetID to its synset.


/* *****************************************************************************
 *  Describe concisely the data structure(s) you used to store the
 *  information in hypernyms.txt. Why did you make this choice?
 **************************************************************************** */

We used another hashmap for this but changed the key, value pairings to
<String, Stack<Integer>>. We used this so that we could have a data structure
that would easily return the list of nouns as an Iterable<String>. Furthermore,
the hashmap lets us keep track of each noun and the synsetIDs of its hypernyms.



/* *****************************************************************************
 *  Describe concisely the algorithm you use in the constructor of
 *  ShortestCommonAncestor to check if the digraph is a rooted DAG.
 *  What is the order of growth of the worst-case running times of
 *  your algorithm? Express your answer as a function of the
 *  number of vertices V and the number of edges E in the digraph.
 *  (Do not use other parameters.) Use Big Theta notation to simplify
 *  your answer.
 **************************************************************************** */

Description:

We used DirectedCycle.hascycle() to check for cycles.
We iterated through all the vertices and checked its outdegree; if its outdegree
was greater than 0, it couldn't be a root; otherwise, incremented roots. Then,
we checked if there was one root and if all other vertices weren't roots, meaning
it is singly rooted.

root check: proportional to V = Θ(V)
has cycle: Θ(E + V)

root check + cycle check: E+2V = Θ(E+V)
Order of growth of running time: Θ(E + V)


/* *****************************************************************************
 *  Describe concisely your algorithm to compute the shortest common ancestor
 *  in ShortestCommonAncestor. For each method, give the order of growth of
 *  the best- and worst-case running times. Express your answers as functions
 *  of the number of vertices V and the number of edges E in the digraph.
 *  (Do not use other parameters.) Use Big Theta notation to simplify your
 *  answers.
 *
 *  If you use hashing, assume the uniform hashing assumption so that put()
 *  and get() take constant time per operation.
 *
 *  Be careful! If you use a BreadthFirstDirectedPaths object, don't forget
 *  to count the time needed to initialize the marked[], edgeTo[], and
 *  distTo[] arrays.
 **************************************************************************** */

Description:

For SCA, we put the vertices v and w, or all the vertices from each subset, into
separate queuees, and run two concurrent bfs on those queues. While loading new
vertices in the queue, we checked if any of them were already in the other
subset's marked. If so, we checked corner cases to verify that is the SCA.


                                 running time
method                  best case            worst case
--------------------------------------------------------
length()                Θ(1)                    Θ(V)

ancestor()                 Θ(1)                 Θ(V)

lengthSubset()              Θ(1)                Θ(V)

ancestorSubset()            Θ(1)                   Θ(V)




/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
n/a
