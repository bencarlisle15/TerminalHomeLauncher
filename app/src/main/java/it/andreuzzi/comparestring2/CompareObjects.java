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

import java.util.Arrays;
import java.util.Iterator;

import it.andreuzzi.comparestring2.AlgMap.Alg;
import it.andreuzzi.comparestring2.algs.interfaces.Algorithm;
import com.bencarlisle15.terminalhomelauncher.managers.AppsManager;

/**
 * 
 * Contains the methods used to compare {@link StringableObject}
 *
 * @author francescoandreuzzi
 */
public class CompareObjects {
    
    private static final CustomComparator<CompareItem> comparator = new CustomComparator<>();
    
    /**
     * 
     * @param s1          the first {@link String} of the comparison
     * @param s2          an {@link Iterable<T>} object whose elements will be compared to {@code s1}
     * @param size        the exact number of elements in {@code s2}
     * @param splitters   refer to {@link CompareObjects#checkSplits checkSplits}
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            a sorted array of {@link CompareItem} containing information about the comparison of each element with {@code s1}. 
     *                      The array is sorted with respect of the sorting order given by the chosen {@link Algorithm}
     * @see               CompareItem
     */
    private static <T extends StringableObject> CompareItem[] buildComparePack(String s1, Iterable<T> s2, int size, String[] splitters, Algorithm algInstance, Alg alg) {
        final String ss1 = Utils.normalize(s1);
        
        CompareItem[] toReturn = new CompareItem[size];
        Iterator<? extends StringableObject> it = s2.iterator();
        
        int counter = 0;
        while(it.hasNext()) {
            float result;
            
            StringableObject t = it.next();
            String st = Utils.normalize(t.getLowercaseString());
            
            if(splitters != null) {
                result = Utils.checkSplits(ss1, st, splitters, algInstance, alg);
            } else {
                result = Utils.compare(ss1, st, algInstance, alg);
            }

            toReturn[counter++] = new CompareItem(t, result);
        }
        
        comparator.biggerIsBetter = Utils.biggerIsBetter(alg);
        Arrays.sort(toReturn, comparator);
        
        return toReturn;
    }
    
//    ----------------------------------------------------------------------------------------------------------------
    
    /**
     * 
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code T[]} object whose elements will be compared to {@code s1}
     * @param alg         an instance  of {@link Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            the single best match of {@code s1} with each element of {@code ss}, found using the given {@link Alg} {@code alg}
     */
    public static <T extends StringableObject> T bestMatch(Class<T> clazz, String s1, T[] ss, Alg alg, Object... args) {
        return bestMatch(clazz, s1,ss, null, alg, args);
    }
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code T[]} object whose elements will be compared to {@code s1}
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            the single best match of {@code s1} with each element of {@code ss}, found using the given {@link Alg} {@code alg}
     */
    public static <T extends StringableObject> T bestMatch(Class<T> clazz, String s1, T[] ss, Algorithm algInstance, Alg alg) {
        return bestMatch(clazz, s1,ss, null, algInstance, alg);
    }
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code T[]} object whose elements will be compared to {@code s1}
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param alg         an instance  of {@link Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            the single best match of {@code s1} with each element of {@code ss}, found using the given {@link Alg} {@code alg}
     */
    public static <T extends StringableObject> T bestMatch(Class<T> clazz, String s1, T[] ss, String[] splitters, Alg alg, Object... args) {
        Algorithm algInstance = alg.buildAlg(args);
        return bestMatch(clazz, s1,ss, splitters, algInstance, alg);
    }
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code T[]} object whose elements will be compared to {@code s1}
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            the single best match of {@code s1} with each element of {@code ss}, found using the given {@link Alg} {@code alg}
     */
    public static <T extends StringableObject> T bestMatch(Class<T> clazz, String s1, T[] ss, String[] splitters, Algorithm algInstance, Alg alg) {
        T[] matches = topNmatches(clazz, s1,ss, 1, splitters, algInstance, alg);
        if(matches == null || matches.length == 0) return null;
        return matches[0];
    }
    
//    --------------------------------------------------------
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code T[]} object whose elements will be compared to {@code s1}
     * @param n           the length of the returned array object
     * @param alg         an instance  of {@link Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            a sorted array of {@code T} of length {@code n}. The sorting operation is based on the results given by the algorithm {@code alg}
     */
    public static <T extends StringableObject> T[] topNmatches(Class<T> clazz, String s1, T[] ss, int n, Alg alg, Object... args) {
        return topNmatches(clazz, s1,ss, n, null, alg, args);
    }
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code T[]} object whose elements will be compared to {@code s1}
     * @param n           the length of the returned array object
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            a sorted array of {@code T} of length {@code n}. The sorting operation is based on the results given by the algorithm {@code alg}
     */
    public static <T extends StringableObject> T[] topNmatches(Class<T> clazz, String s1, T[] ss, int n, Algorithm algInstance, Alg alg) {
        return topNmatches(clazz, s1,ss.length, Arrays.asList(ss), n, null, algInstance, alg);
    }
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code T[]} object whose elements will be compared to {@code s1}
     * @param n           the length of the returned array object
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param alg         an instance  of {@link Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            a sorted array of {@code T} of length {@code n}. The sorting operation is based on the ranks given by the algorithm {@code alg}
     */
    public static <T extends StringableObject> T[] topNmatches(Class<T> clazz, String s1, T[] ss, int n, String[] splitters, Alg alg, Object... args) {
        Algorithm algInstance = alg.buildAlg(args);
        return topNmatches(clazz, s1,ss, n, splitters, algInstance, alg);
    }
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code T[]} object whose elements will be compared to {@code s1}
     * @param n           the length of the returned array object
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            a sorted array of {@code T} of length {@code n}. The sorting operation is based on the results given by the algorithm {@code alg}
     */
    public static <T extends StringableObject> T[] topNmatches(Class<T> clazz, String s1, T[] ss, int n, String[] splitters, Algorithm algInstance, Alg alg) {
        return topNmatches(clazz, s1,ss.length, Arrays.asList(ss), n, splitters, algInstance, alg);
    }
    
//    --------------------------------------------------------
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code T[]} object whose elements will be compared to {@code s1}
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param alg         an instance  of {@link Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            a sorted array of {@code T} containing only elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static <T extends StringableObject> T[] withDeadline(Class<T> clazz, String s1, T[] ss, float deadline, Alg alg, Object... args) {
        return withDeadline(clazz, s1,ss, deadline, null, alg, args);
    }
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code T[]} object whose elements will be compared to {@code s1}
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            a sorted array of {@code T} containing only elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static <T extends StringableObject> T[] withDeadline(Class<T> clazz, String s1, T[] ss, float deadline, Algorithm algInstance, Alg alg) {
        return withDeadline(clazz, s1,ss.length, Arrays.asList(ss), deadline, null, algInstance, alg);
    }
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code T[]} object whose elements will be compared to {@code s1}
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param alg         an instance  of {@link Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            a sorted array of {@code T} containing only elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static <T extends StringableObject> T[] withDeadline(Class<T> clazz, String s1, T[] ss, float deadline, String[] splitters, Alg alg, Object... args) {
        Algorithm algInstance = alg.buildAlg(args);
        return withDeadline(clazz, s1,ss, deadline, splitters, algInstance, alg);
    }
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code T[]} object whose elements will be compared to {@code s1}
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            a sorted array of {@code T} containing only elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static <T extends StringableObject> T[] withDeadline(Class<T> clazz, String s1, T[] ss, float deadline, String[] splitters, Algorithm algInstance, Alg alg) {
        return withDeadline(clazz, s1,ss.length, Arrays.asList(ss), deadline, splitters, algInstance, alg);
    }
    
//    --------------------------------------------------------

    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code T[]} object whose elements will be compared to {@code s1}
     * @param n           the maximum number of elements of the returned array
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param alg         an instance  of {@link Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            a sorted array of {@code T} containing at most {@code n} elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static <T extends StringableObject> T[] topMatchesWithDeadline(Class<T> clazz, String s1, T[] ss, int n, float deadline, Alg alg, Object... args) {
        return topMatchesWithDeadline(clazz, s1,ss, n, deadline, null, alg, args);
    }
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code T[]} object whose elements will be compared to {@code s1}
     * @param n           the maximum number of elements of the returned array
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            a sorted array of {@code T} containing at most {@code n} elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static <T extends StringableObject> T[] topMatchesWithDeadline(Class<T> clazz, String s1, T[] ss, int n, float deadline, Algorithm algInstance, Alg alg) {
        return topMatchesWithDeadline(clazz, s1,ss.length, Arrays.asList(ss), n, deadline, null, algInstance, alg);
    }
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code T[]} object whose elements will be compared to {@code s1}
     * @param n           the maximum number of elements of the returned array
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param alg         an instance  of {@link Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            a sorted array of {@code T} containing at most {@code n} elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static <T extends StringableObject> T[] topMatchesWithDeadline(Class<T> clazz, String s1, T[] ss, int n, float deadline, String[] splitters, Alg alg, Object... args) {
        Algorithm algInstance = alg.buildAlg(args);
        return topMatchesWithDeadline(clazz, s1,ss, n, deadline, splitters, algInstance, alg);
    }
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param ss          the {@code T[]} object whose elements will be compared to {@code s1}
     * @param n           the maximum number of elements of the returned array
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            a sorted array of {@code T} containing at most {@code n} elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static <T extends StringableObject> T[] topMatchesWithDeadline(Class<T> clazz, String s1, T[] ss, int n, float deadline, String[] splitters, Algorithm algInstance, Alg alg) {
        return topMatchesWithDeadline(clazz, s1,ss.length, Arrays.asList(ss), n, deadline, splitters, algInstance, alg);
    }
    
//    ----------------------------------------------------------------------------------------------------------------
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param alg         an instance  of {@link Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            the best match with {@code s1} within {@code ss}, found using the given {@link Alg} {@code alg}
     */
    public static <T extends StringableObject> T bestMatch(Class<T> clazz, String s1, int size, Iterable<T> ss, Alg alg, Object... args) {
        return bestMatch(clazz, s1,size, ss, null, alg, args);
    }
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            the best match with {@code s1} within {@code ss}, found using the given {@link Alg} {@code alg}
     */
    public static <T extends StringableObject> T bestMatch(Class<T> clazz, String s1, int size, Iterable<T> ss, Algorithm algInstance, Alg alg) {
        return bestMatch(clazz, s1,size, ss, null, algInstance, alg);
    }
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param alg         an instance  of {@link Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            the best match with {@code s1} within {@code ss}, found using the given {@link Alg} {@code alg}
     */
    public static <T extends StringableObject> T bestMatch(Class<T> clazz, String s1, int size, Iterable<T> ss, String[] splitters, Alg alg, Object... args) {
        Algorithm algInstance = alg.buildAlg(args);
        return bestMatch(clazz, s1,size, ss, splitters, algInstance, alg);
    }
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            the best match with {@code s1} within {@code ss}, found using the given {@link Alg} {@code alg}
     */
    public static <T extends StringableObject> T bestMatch(Class<T> clazz, String s1, int size, Iterable<T> ss, String[] splitters, Algorithm algInstance, Alg alg) {
        T[] matches = topNmatches(clazz, s1,size, ss, 1, splitters, algInstance, alg);
        if(matches == null || matches.length == 0) return null;
        return matches[0];
    }
    
//    --------------------------------------------------------
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param n           the length of the returned array object
     * @param alg         an instance  of {@link Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            a sorted array of {@code T} of length {@code n}. The sorting operation is based on the ranks given by the algorithm {@code alg}
     */
    public static <T extends StringableObject> T[] topNmatches(Class<T> clazz, String s1, int size, Iterable<T> ss, int n, Alg alg, Object... args) {
        return topNmatches(clazz, s1,size, ss, n, null, alg, args);
    }
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param n           the length of the returned array object
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            a sorted array of {@code T} of length {@code n}. The sorting operation is based on the ranks given by the algorithm {@code alg}
     */
    public static <T extends StringableObject> T[] topNmatches(Class<T> clazz, String s1, int size, Iterable<T> ss, int n, Algorithm algInstance, Alg alg) {
        return topNmatches(clazz, s1,size, ss, n, null, algInstance, alg);
    }
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param n           the length of the returned array object
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param alg         an instance  of {@link Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            a sorted array of {@code T} of length {@code n}. The sorting operation is based on the ranks given by the algorithm {@code alg}
     */
    public static <T extends StringableObject> T[] topNmatches(Class<T> clazz, String s1, int size, Iterable<T> ss, int n, String[] splitters, Alg alg, Object... args) {
        Algorithm algInstance = alg.buildAlg(args);
        return topNmatches(clazz, s1,size, ss, n, splitters, algInstance, alg);
    }
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param n           the length of the returned array object
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            a sorted array of {@code T} of length {@code n}. The sorting operation is based on the ranks given by the algorithm {@code alg}
     */
    public static <T extends StringableObject> T[] topNmatches(Class<T> clazz, String s1, int size, Iterable<T> ss, int n, String[] splitters, Algorithm algInstance, Alg alg) {
        CompareItem[] items = buildComparePack(s1,ss, size, splitters, algInstance, alg);
        return Utils.gather(clazz, items, n);
    }
    
//    --------------------------------------------------------
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param alg         an instance  of {@link Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            a sorted array of {@code T} containing only elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static <T extends StringableObject> T[] withDeadline(Class<T> clazz, String s1, int size, Iterable<T> ss, float deadline, Alg alg, Object... args) {
        return withDeadline(clazz, s1,size, ss, deadline, null, alg, args);
    }
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            a sorted array of {@code T} containing only elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static <T extends StringableObject> T[] withDeadline(Class<T> clazz, String s1, int size, Iterable<T> ss, float deadline, Algorithm algInstance, Alg alg) {
        return withDeadline(clazz, s1, size, ss, deadline, null, algInstance, alg);
    }
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param alg         an instance  of {@link Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            a sorted array of {@code T} containing only elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static <T extends StringableObject> T[] withDeadline(Class<T> clazz, String s1, int size, Iterable<T> ss, float deadline, String[] splitters, Alg alg, Object... args) {
        Algorithm algInstance = alg.buildAlg(args);
        return withDeadline(clazz, s1, size, ss, deadline, splitters, algInstance, alg);
    }
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            a sorted array of {@code T} containing only elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static <T extends StringableObject> T[] withDeadline(Class<T> clazz, String s1, int size, Iterable<T> ss, float deadline, String[] splitters, Algorithm algInstance, Alg alg) {
        CompareItem[] items = buildComparePack(s1,ss, size, splitters, algInstance, alg);
        int cutIndex = Utils.firstBeyondDeadline(items, deadline, Utils.biggerIsBetter(alg));
        return Utils.gather(clazz, items, cutIndex);
    }
    
//    --------------------------------------------------------    
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param n           the maximum number of elements of the returned array
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param alg         an instance  of {@link Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            a sorted array of {@code T} containing at most {@code n} elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static <T extends StringableObject> T[] topMatchesWithDeadline(Class<T> clazz, String s1, int size, Iterable<T> ss, int n, float deadline, Alg alg, Object... args) {
        return topMatchesWithDeadline(clazz, s1, size, ss, n, deadline, null, alg, args);
    }
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param n           the maximum number of elements of the returned array
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            a sorted array of {@code T} containing at most {@code n} elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static <T extends StringableObject> T[] topMatchesWithDeadline(Class<T> clazz, String s1, int size, Iterable<T> ss, int n, float deadline, Algorithm algInstance, Alg alg) {
        return topMatchesWithDeadline(clazz, s1, size, ss, n, deadline, null, algInstance, alg);
    }
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param n           the maximum number of elements of the returned array
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param alg         an instance  of {@link Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that can be used to build an instance of {@link Algorithm}
     * @return            a sorted array of {@code T} containing at most {@code n} elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static <T extends StringableObject> T[] topMatchesWithDeadline(Class<T> clazz, String s1, int size, Iterable<T> ss, int n, float deadline, String[] splitters, Alg alg, Object... args) {
        Algorithm algInstance = alg.buildAlg(args);
        return topMatchesWithDeadline(clazz, s1, size, ss, n, deadline, splitters, algInstance, alg);
    }
    
    /**
     * @param <T>         the class of {@code ss}
     * @param clazz       the class of the returned array
     * @param s1          the first {@link String} of the comparison
     * @param size        the number of elements in {@code ss}
     * @param ss          an {@link Iterable} object whose elements will be compared to {@code s1}
     * @param n           the maximum number of elements of the returned array
     * @param deadline    the min/max rank of the elements in the array which is returned
     * @param splitters   refer to {@link Utils#checkSplits checkSplits}
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            a sorted array of {@code T} containing at most {@code n} elements with {@code rank >= deadline} if 
     *                      {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code true}, only elements with
     *                      {@code rank <= deadline} if {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) Utils.biggerIsBetter(alg)} is {@code false}
     */
    public static <T extends StringableObject> T[] topMatchesWithDeadline(Class<T> clazz, String s1, int size, Iterable<T> ss, int n, float deadline, String[] splitters, Algorithm algInstance, Alg alg) {
        CompareItem[] items = buildComparePack(s1,ss, size, splitters, algInstance, alg);
        int cutIndex = Utils.firstBeyondDeadline(items, deadline, Utils.biggerIsBetter(alg));
        return Utils.gather(clazz, items, Math.min(cutIndex, n));
    }

    public static AppsManager.LaunchInfo[] topAppMatchesWithDeadline(Class<AppsManager.LaunchInfo> clazz, String s1, int size, Iterable<AppsManager.LaunchInfo> ss, int n, float deadline, String[] splitters, Algorithm algInstance, Alg alg) {
        CompareItem[] items = buildAppComparePack(s1,ss, size, splitters, algInstance, alg);
        int cutIndex = Utils.firstBeyondDeadline(items, deadline, Utils.biggerIsBetter(alg));
        return Utils.gather(clazz, items, Math.min(cutIndex, n));
    }

    private static CompareItem[] buildAppComparePack(String s1, Iterable<AppsManager.LaunchInfo> s2, int size, String[] splitters, Algorithm algInstance, Alg alg) {
        final String ss1 = Utils.normalize(s1);

        CompareItem[] toReturn = new CompareItem[size];
        Iterator<AppsManager.LaunchInfo> it = s2.iterator();

        int counter = 0;
        float maxTimes = 1;
        while(it.hasNext()) {
            float result;

            AppsManager.LaunchInfo t = it.next();
            maxTimes = Math.max(maxTimes, t.launchedTimes);
            String st = Utils.normalize(t.getLowercaseString());

            if(splitters != null) {
                result = Utils.checkSplits(ss1, st, splitters, algInstance, alg);
            } else {
                result = Utils.compare(ss1, st, algInstance, alg);
            }
            result += 0;

            toReturn[counter++] = new CompareItem(t, result);
        }
        it = s2.iterator();
        counter = 0;
        float similarityWeight = 0.7f;
        float launchWeight = 0.3f;
        while (it.hasNext()) {
            AppsManager.LaunchInfo t = it.next();
            float launchValue = (t.launchedTimes / maxTimes) + 0.45f;
            launchValue = Math.min(launchValue, 1);
            toReturn[counter].r = toReturn[counter].r * similarityWeight + launchValue * launchWeight;
            counter++;
        }

        comparator.biggerIsBetter = Utils.biggerIsBetter(alg);
        Arrays.sort(toReturn, comparator);

        return toReturn;
    }
    
}
