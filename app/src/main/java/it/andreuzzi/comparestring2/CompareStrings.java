/*
 * The MIT License
 *
 * Copyright 2019 francescoandreuzzi.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package it.andreuzzi.comparestring2;

import it.andreuzzi.comparestring2.algs.interfaces.Algorithm;
import java.util.Arrays;
import java.util.Iterator;

/**
 *
 * @author francescoandreuzzi
 */
public class CompareStrings {
    private static final CustomComparator<CompareItem> comparator = new CustomComparator<>();
    
    /**
     * 
     * @param s1          the first {@link String} of the comparison
     * @param s2          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param size        the exact number of elements in {@code s2}
     * @param splitters   refer to {@link CompareObjects#checkSplits checkSplits}
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            a sorted array of {@link CompareItem} containing information about the comparison of each element with {@code s1}. 
     *                      The array is sorted with respect of the sorting order given by the chosen {@link Algorithm}
     * @see               CompareItem
     */
    private static CompareItem[] buildComparePack(String s1, Iterable<String> s2, int size, String[] splitters, Algorithm algInstance, AlgMap.Alg alg) {
        final String ss1 = Utils.normalize(s1);
        
        CompareItem[] toReturn = new CompareItem[size];
        Iterator<String> it = s2.iterator();
        
        int counter = 0;
        while(it.hasNext()) {
            float result;
            
            String st = Utils.normalize(it.next());
            
            if(splitters != null) {
                result = Utils.checkSplits(ss1, st, splitters, algInstance, alg);
            } else {
                result = Utils.compare(ss1, st, algInstance, alg);
            }
            
            toReturn[counter++] = new CompareItem(new StringableStringWrapper(st), result);
        }
        
        comparator.biggerIsBetter = Utils.biggerIsBetter(alg);
        Arrays.sort(toReturn, comparator);
        
        return toReturn;
    }
    
//    ----------------------------------------------------------------------------------------------------------------
    
    /**
     * 
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code String[]} object whose elements will be compared to {@code s1}
     * @param alg         an instance  of {@link AlgMap.Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            the single best match of {@code s1} with each element of {@code ss}, found using the given {@link AlgMap.Alg} {@code alg}
     */
    public static String bestMatch(String s1, String[] ss, AlgMap.Alg alg, Object... args) {
        return bestMatch(s1, ss, null, alg, args);
    }
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code String[]} object whose elements will be compared to {@code s1}
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link AlgMap.Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            the single best match of {@code s1} with each element of {@code ss}, found using the given {@link AlgMap.Alg}
     */
    public static String bestMatch(String s1, String[] ss, Algorithm algInstance, AlgMap.Alg alg) {
        return bestMatch(s1, ss, null, algInstance, alg);
    }
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code String[]} object whose elements will be compared to {@code s1}
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param alg         an instance  of {@link AlgMap.Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            the single best match of {@code s1} with each element of {@code ss}, found using the given {@link AlgMap.Alg}
     */
    public static String bestMatch(String s1, String[] ss, String[] splitters, AlgMap.Alg alg, Object... args) {
        Algorithm algInstance = alg.buildAlg(args);
        return bestMatch(s1, ss, splitters, algInstance, alg);
    }
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code String[]} object whose elements will be compared to {@code s1}
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link AlgMap.Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            the single best match of {@code s1} with each element of {@code ss}, found using the given {@link AlgMap.Alg}
     */
    public static String bestMatch(String s1, String[] ss, String[] splitters, Algorithm algInstance, AlgMap.Alg alg) {
        String[] matches = topNmatches(s1, ss, 1, splitters, algInstance, alg);
        if(matches == null || matches.length == 0) return null;
        return matches[0];
    }
    
//    --------------------------------------------------------
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code String[]} object whose elements will be compared to {@code s1}
     * @param n           the length of the returned array object
     * @param alg         an instance of {@link AlgMap.Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            a sorted {@code String[]} of length {@code n}. The sorting operation is based on the results given by the algorithm {@code alg}
     */
    public static String[] topNmatches(String s1, String[] ss, int n, AlgMap.Alg alg, Object... args) {
        return topNmatches(s1, ss, n, null, alg, args);
    }
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code String[]} object whose elements will be compared to {@code s1}
     * @param n           the length of the returned array object
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link AlgMap.Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            a sorted {@code String[]} of length {@code n}. The sorting operation is based on the results given by the algorithm {@code alg}
     */
    public static String[] topNmatches(String s1, String[] ss, int n, Algorithm algInstance, AlgMap.Alg alg) {
        return topNmatches(s1, ss.length, Arrays.asList(ss), n, null, algInstance, alg);
    }
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code String[]} object whose elements will be compared to {@code s1}
     * @param n           the length of the returned array object
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param alg         an instance  of {@link AlgMap.Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            a sorted {@code String[]} of length {@code n}. The sorting operation is based on the ranks given by the algorithm {@code alg}
     */
    public static String[] topNmatches(String s1, String[] ss, int n, String[] splitters, AlgMap.Alg alg, Object... args) {
        Algorithm algInstance = alg.buildAlg(args);
        return topNmatches(s1, ss, n, splitters, algInstance, alg);
    }
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code String[]} object whose elements will be compared to {@code s1}
     * @param n           the length of the returned array object
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link AlgMap.Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            a sorted {@code String[]} of length {@code n}. The sorting operation is based on the results given by the algorithm {@code alg}
     */
    public static String[] topNmatches(String s1, String[] ss, int n, String[] splitters, Algorithm algInstance, AlgMap.Alg alg) {
        return topNmatches(s1, ss.length, Arrays.asList(ss), n, splitters, algInstance, alg);
    }
    
//    --------------------------------------------------------
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code String[]} object whose elements will be compared to {@code s1}
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param alg         an instance  of {@link AlgMap.Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            a sorted {@code String[]} containing only elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static String[] withDeadline(String s1, String[] ss, float deadline, AlgMap.Alg alg, Object... args) {
        return withDeadline(s1, ss, deadline, null, alg, args);
    }
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code String[]} object whose elements will be compared to {@code s1}
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link AlgMap.Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            a sorted {@code String[]} containing only elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static String[] withDeadline(String s1, String[] ss, float deadline, Algorithm algInstance, AlgMap.Alg alg) {
        return withDeadline(s1, ss.length, Arrays.asList(ss), deadline, null, algInstance, alg);
    }
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code String[]} object whose elements will be compared to {@code s1}
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param alg         an instance  of {@link AlgMap.Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            a sorted {@code String[]} containing only elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static String[] withDeadline(String s1, String[] ss, float deadline, String[] splitters, AlgMap.Alg alg, Object... args) {
        Algorithm algInstance = alg.buildAlg(args);
        return withDeadline(s1, ss, deadline, splitters, algInstance, alg);
    }
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code String[]} object whose elements will be compared to {@code s1}
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link AlgMap.Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            a sorted {@code String[]} containing only elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static String[] withDeadline(String s1, String[] ss, float deadline, String[] splitters, Algorithm algInstance, AlgMap.Alg alg) {
        return withDeadline(s1, ss.length, Arrays.asList(ss), deadline, splitters, algInstance, alg);
    }
    
//    --------------------------------------------------------

    /**
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code String[]} object whose elements will be compared to {@code s1}
     * @param n           the maximum number of elements of the returned array
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param alg         an instance  of {@link AlgMap.Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            a sorted {@code String[]} containing at most {@code n} elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static String[] topMatchesWithDeadline(String s1, String[] ss, int n, float deadline, AlgMap.Alg alg, Object... args) {
        return topMatchesWithDeadline(s1, ss, n, deadline, null, alg, args);
    }
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code String[]} object whose elements will be compared to {@code s1}
     * @param n           the maximum number of elements of the returned array
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link AlgMap.Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            a sorted {@code String[]} containing at most {@code n} elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static String[] topMatchesWithDeadline(String s1, String[] ss, int n, float deadline, Algorithm algInstance, AlgMap.Alg alg) {
        return topMatchesWithDeadline(s1, ss.length, Arrays.asList(ss), n, deadline, null, algInstance, alg);
    }
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code String[]} object whose elements will be compared to {@code s1}
     * @param n           the maximum number of elements of the returned array
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param alg         an instance  of {@link AlgMap.Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            a sorted {@code String[]} containing at most {@code n} elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static String[] topMatchesWithDeadline(String s1, String[] ss, int n, float deadline, String[] splitters, AlgMap.Alg alg, Object... args) {
        Algorithm algInstance = alg.buildAlg(args);
        return topMatchesWithDeadline(s1, ss, n, deadline, splitters, algInstance, alg);
    }
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code String[]} object whose elements will be compared to {@code s1}
     * @param n           the maximum number of elements of the returned array
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link AlgMap.Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            a sorted {@code String[]} containing at most {@code n} elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static String[] topMatchesWithDeadline(String s1, String[] ss, int n, float deadline, String[] splitters, Algorithm algInstance, AlgMap.Alg alg) {
        return topMatchesWithDeadline(s1, ss.length, Arrays.asList(ss), n, deadline, splitters, algInstance, alg);
    }
    
//    ----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param alg         an instance  of {@link AlgMap.Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            the best match with {@code s1} within {@code ss}, found using the given {@link AlgMap.Alg} {@code alg}
     */
    public static String bestMatch(String s1, int size, Iterable<String> ss, AlgMap.Alg alg, Object... args) {
        return bestMatch(s1, size, ss, null, alg, args);
    }
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link AlgMap.Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            the best match with {@code s1} within {@code ss}, found using the given {@link AlgMap.Alg} {@code alg}
     */
    public static String bestMatch(String s1, int size, Iterable<String> ss, Algorithm algInstance, AlgMap.Alg alg) {
        return bestMatch(s1, size, ss, null, algInstance, alg);
    }
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param alg         an instance  of {@link AlgMap.Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            the best match with {@code s1} within {@code ss}, found using the given {@link AlgMap.Alg} {@code alg}
     */
    public static String bestMatch(String s1, int size, Iterable<String> ss, String[] splitters, AlgMap.Alg alg, Object... args) {
        Algorithm algInstance = alg.buildAlg(args);
        return bestMatch(s1, size, ss, splitters, algInstance, alg);
    }
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link AlgMap.Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            the best match with {@code s1} within {@code ss}, found using the given {@link AlgMap.Alg} {@code alg}
     */
    public static String bestMatch(String s1, int size, Iterable<String> ss, String[] splitters, Algorithm algInstance, AlgMap.Alg alg) {
        String[] matches = topNmatches(s1, size, ss, 1, splitters, algInstance, alg);
        if(matches == null || matches.length == 0) return null;
        return matches[0];
    }
    
//    --------------------------------------------------------
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param n           the length of the returned array object
     * @param alg         an instance  of {@link AlgMap.Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            a sorted {@code String[]} of length {@code n}. The sorting operation is based on the ranks given by the algorithm {@code alg}
     */
    public static String[] topNmatches(String s1, int size, Iterable<String> ss, int n, AlgMap.Alg alg, Object... args) {
        return topNmatches(s1, size, ss, n, null, alg, args);
    }
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param n           the length of the returned array object
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link AlgMap.Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            a sorted {@code String[]} of length {@code n}. The sorting operation is based on the ranks given by the algorithm {@code alg}
     */
    public static String[] topNmatches(String s1, int size, Iterable<String> ss, int n, Algorithm algInstance, AlgMap.Alg alg) {
        return topNmatches(s1, size, ss, n, null, algInstance, alg);
    }
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param n           the length of the returned array object
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param alg         an instance  of {@link AlgMap.Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            a sorted {@code String[]} of length {@code n}. The sorting operation is based on the ranks given by the algorithm {@code alg}
     */
    public static String[] topNmatches(String s1, int size, Iterable<String> ss, int n, String[] splitters, AlgMap.Alg alg, Object... args) {
        Algorithm algInstance = alg.buildAlg(args);
        return topNmatches(s1, size, ss, n, splitters, algInstance, alg);
    }
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param n           the length of the returned array object
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link AlgMap.Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            a sorted {@code String[]} of length {@code n}. The sorting operation is based on the ranks given by the algorithm {@code alg}
     */
    public static String[] topNmatches(String s1, int size, Iterable<String> ss, int n, String[] splitters, Algorithm algInstance, AlgMap.Alg alg) {
        CompareItem[] items = buildComparePack(s1, ss, size, splitters, algInstance, alg);
        return Utils.gather(String.class, items, n);
    }
    
//    --------------------------------------------------------
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param alg         an instance  of {@link AlgMap.Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            a sorted {@code String[]} containing only elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static String[] withDeadline(String s1, int size, Iterable<String> ss, float deadline, AlgMap.Alg alg, Object... args) {
        return withDeadline(s1, size, ss, deadline, null, alg, args);
    }
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link AlgMap.Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            a sorted {@code String[]} containing only elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static String[] withDeadline(String s1, int size, Iterable<String> ss, float deadline, Algorithm algInstance, AlgMap.Alg alg) {
        return withDeadline(s1, size, ss, deadline, null, algInstance, alg);
    }
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param alg         an instance  of {@link AlgMap.Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            a sorted {@code String[]} containing only elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static String[] withDeadline(String s1, int size, Iterable<String> ss, float deadline, String[] splitters, AlgMap.Alg alg, Object... args) {
        Algorithm algInstance = alg.buildAlg(args);
        return withDeadline(s1, size, ss, deadline, splitters, algInstance, alg);
    }
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link AlgMap.Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            a sorted {@code String[]} containing only elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static String[] withDeadline(String s1, int size, Iterable<String> ss, float deadline, String[] splitters, Algorithm algInstance, AlgMap.Alg alg) {
        CompareItem[] items = buildComparePack(s1, ss, size, splitters, algInstance, alg);
        int cutIndex = Utils.firstBeyondDeadline(items, deadline, Utils.biggerIsBetter(alg));
        return Utils.gather(String.class, items, cutIndex);
    }
    
//    --------------------------------------------------------    
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param n           the maximum number of elements of the returned array
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param alg         an instance  of {@link AlgMap.Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            a sorted {@code String[]} containing at most {@code n} elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static String[] topMatchesWithDeadline(String s1, int size, Iterable<String> ss, int n, float deadline, AlgMap.Alg alg, Object... args) {
        return topMatchesWithDeadline(s1, size, ss, n, deadline, null, alg, args);
    }
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param n           the maximum number of elements of the returned array
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link AlgMap.Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            a sorted {@code String[]} containing at most {@code n} elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static String[] topMatchesWithDeadline(String s1, int size, Iterable<String> ss, int n, float deadline, Algorithm algInstance, AlgMap.Alg alg) {
        return topMatchesWithDeadline(s1, size, ss, n, deadline, null, algInstance, alg);
    }
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param n           the maximum number of elements of the returned array
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param alg         an instance  of {@link AlgMap.Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            a sorted {@code String[]} containing at most {@code n} elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static String[] topMatchesWithDeadline(String s1, int size, Iterable<String> ss, int n, float deadline, String[] splitters, AlgMap.Alg alg, Object... args) {
        Algorithm algInstance = alg.buildAlg(args);
        return topMatchesWithDeadline(s1, size, ss, n, deadline, splitters, algInstance, alg);
    }
    
    /**
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param n           the maximum number of elements of the returned array
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link AlgMap.Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            a sorted {@code String[]} containing at most {@code n} elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static String[] topMatchesWithDeadline(String s1, int size, Iterable<String> ss, int n, float deadline, String[] splitters, Algorithm algInstance, AlgMap.Alg alg) {
        CompareItem[] items = buildComparePack(s1, ss, size, splitters, algInstance, alg);
        int cutIndex = Utils.firstBeyondDeadline(items, deadline, Utils.biggerIsBetter(alg));
        return Utils.gather(String.class, items, Math.min(cutIndex, n));
    }
}
