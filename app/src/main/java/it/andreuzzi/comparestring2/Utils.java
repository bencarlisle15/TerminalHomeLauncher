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

import java.lang.reflect.Array;
import it.andreuzzi.comparestring2.AlgMap.Alg;
import it.andreuzzi.comparestring2.algs.interfaces.Algorithm;
import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * This class contains some useful methods for the {@link CompareObjects} class
 *
 * @author francescoandreuzzi
 */
public class Utils {

    /**
     * @param <T>       the class of the returned array
     * @param c         the class that will be used to cast the returned elements
     * @param items     a sorted array of {@link CompareItem}
     * @param n         the length of the returned array
     * @return          an array of {@code T} containing the first {@code n} elements of {@code items}
     */
    public static <T> T[] gather(Class<T> c, CompareItem[] items, int n) {
        int length = Math.min(n, items.length - 1);
        if(length < 0) length = 0;
        T[] array = (T[]) Array.newInstance(c, length);
        for(int i = 0; i < length; i++) {
            array[i] = (T) items[i].o;
        }
        
        return array;
    }

    /**
     * 
     * @param items             a sorted array of {@link CompareItem}
     * @param deadline          the deadline
     * @param biggerIsBetter    refer to {@link Utils#biggerIsBetter(it.andreuzzi.comparestring2.AlgMap.Alg) biggerIsBetter(Alg)}
     * @return                  the index of the first element before the first element whose {@code r} value is {@code > deadline} if 
     *                              {@code biggerIsBetter} is false, the index of the first element before the first element whose {@code r} value is {@code < deadline} if 
     *                              {@code biggerIsBetter} is true
     */
    public static int firstBeyondDeadline(CompareItem[] items, double deadline, boolean biggerIsBetter) {
        boolean check = false;
        int c = 0;
        for(; c < items.length; c++) {
            boolean case1 = biggerIsBetter && items[c].r < deadline;
            boolean case2 = !biggerIsBetter && items[c].r > deadline;
            
            check = case1 || case2;
            if(check) break;
        }
        
        return c - (check ? 1 : 0);
    }
    
    public static void log(Object o) {
        System.out.println(o != null ? o.toString() : "null");
    }
    
    /**
     * 
     * @param alg   the chosen algorithm
     * @return      return {@code true} if {@code alg} belongs to {@link AlgMap.NormSimAlg}. {@code false} otherwise
     */
    public static boolean biggerIsBetter(Alg alg) {
        return alg instanceof AlgMap.NormSimAlg;
    }
    
    public static float baseRank(Alg alg) {
        return biggerIsBetter(alg) ? Float.MIN_VALUE : Float.MAX_VALUE;
    }
    
    /**
     * This method affects the overall performance of the comparison. You should choose carefully your {@code splitters}.
     * 
     * @param s1          the first {@link String} of the comparison
     * @param s2          the second {@link String} of the comparison
     * @param splitters   each element of {@code splitters} will be used to perform a call to {@code s2.split(element)}. Each element of the returned array
     *                      will be compared to {@code s1} using the provided algorithm. Then the value of the single best match (the biggest or the smallest, depending on {@code alg}) 
     *                      will be returned.
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance  of {@link Alg}. Check {@link AlgMap} 
     * @return            the value of the best match
     */
    public static float checkSplits(String s1, String s2, String[] splitters, Algorithm algInstance, AlgMap.Alg alg) {
        boolean biggerIsBetter = Utils.biggerIsBetter(alg);
        float result = Utils.baseRank(alg);
        
        for(String q : splitters) {
            String[] split = s2.split(Pattern.quote(q));
            for(int i = 1; i < split.length; i++) {
                float r = compare(s1, split[i], algInstance, alg);
                result = biggerIsBetter ? Math.max(result, r) : Math.min(result, r);
            }
        }
        
//        test the whole word
        float r = compare(s1, s2, algInstance, alg);
        result = biggerIsBetter ? Math.max(result, r) : Math.min(result, r);
        
        return result;
    }
    
    private static final Pattern accentPattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    private static final String EMPTYSTRING = "";
    
    /**
     * 
     * @param s the {@link String} you want to normalize
     * @return  {@code s}, lowercase, without accents, or similar weird characters
     */
    public static String normalize(String s) {
        String decomposed = Normalizer.normalize(s, Normalizer.Form.NFD);
        return accentPattern.matcher(decomposed).replaceAll(EMPTYSTRING).toLowerCase();
    }
    
    /**
     * 
     * @param s1          the {@link String} string of the comparison
     * @param s2          the {@link String} string of the comparison
     * @param algInstance an instance of {@link Algorithm} that will be used to perform the comparison
     * @param alg         an instance of {@link Alg}. It must refer to the same algorithm referred by {@code algInstance}. Check {@link AlgMap} 
     * @return            the distance/similarity between {@code s1} and {@code s2}
     * @see               Algorithm
    */
    public static float compare(String s1, String s2, Algorithm algInstance, Alg alg) {
        return alg.compare(algInstance, s1, s2);
    }
    
    /**
     * 
     * @param s1          the first {@link String} of the comparison
     * @param s2          the second {@link String} of the comparison
     * @param alg         an instance of {@link Alg}. Check {@link AlgMap} 
     * @param args        a list of Object that will be used to build an instance of {@link Algorithm}
     * @return            the distance/similarity between {@code s1} and {@code s2}
     * @see               Alg
    */
    public static float compare(String s1, String s2, Alg alg, Object... args) {
        return alg.compare(s1, s2, args);
    }
}
