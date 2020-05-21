package it.andreuzzi.comparestring2.algs.interfaces;

/**
 *
 * @author Thibault Debatty
 */
public interface StringSimilarity extends Algorithm {
    /**
     * Compute and return a measure of similarity between 2 strings.
     * @param s1
     * @param s2
     * @return similarity (0 means both strings are completely different)
     */
    double similarity(String s1, String s2);
}
