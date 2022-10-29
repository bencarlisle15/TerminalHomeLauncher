package it.andreuzzi.comparestring2.test;

import it.andreuzzi.comparestring2.AlgMap;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;
import it.andreuzzi.comparestring2.AlgMap.Alg;
import it.andreuzzi.comparestring2.algs.interfaces.Algorithm;

/**
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

/*
 * @author francescoandreuzzi
 */
public class Test {

    public static final int DISTANCE_ALG        = 1;
    public static final int NORM_DISTANCE_ALG   = 2;
    public static final int NORM_SIMILARITY_ALG = 4;
    public static final int METRIC_DISTANCE_ALG = 8;
    public static final int ALL_ALG             = 15;

    public static final int SORTMODE_ALG_CATEGORY = 10;
    public static final int SORTMODE_ALG_TIME     = 11;
    public static final int SORTMODE_ALG_RESULT   = 12;
    public static final int SORTMODE_ALG          = 13;

    public static void main(String[] args) {
      List<List<TestResult>> results = makeTests("au", new String[] {"vault", "ajustes", "authenticator"});
      printResults(results, System.out, SORTMODE_ALG);
      
      System.out.println("\n");
      
      results = makeTests("aut", new String[] {"vault", "ajustes", "authenticator"});
      printResults(results, System.out, SORTMODE_ALG);
      
      System.out.println("\n");
      
      results = makeTests("auth", new String[] {"vault", "ajustes", "authenticator"});
      printResults(results, System.out, SORTMODE_ALG);
    }

//    --------------------------------------------------------

    public static Alg[] parseAlgs(int on) {
        List<Alg> algs = new ArrayList<>();
        if((on & DISTANCE_ALG) == DISTANCE_ALG) algs.addAll(Arrays.asList(AlgMap.DistAlg.values()));
        if((on & NORM_DISTANCE_ALG) == NORM_DISTANCE_ALG) algs.addAll(Arrays.asList(AlgMap.NormDistAlg.values()));
        if((on & NORM_SIMILARITY_ALG) == NORM_SIMILARITY_ALG) algs.addAll(Arrays.asList(AlgMap.NormSimAlg.values()));
        if((on & METRIC_DISTANCE_ALG) == METRIC_DISTANCE_ALG) algs.addAll(Arrays.asList(AlgMap.MetricDistAlg.values()));

        return algs.toArray(new Alg[0]);
    }

//    --------------------------------------------------------

    public static List<TestResult> makeTest(String s1, String s2) {
        return makeTest(s1, s2, null, ALL_ALG);
    }

    public static List<TestResult> makeTest(String s1, String s2, Alg alg) {
        return makeTest(s1, s2, new Alg[] {alg});
    }

    public static List<TestResult> makeTest(String s1, String s2, Alg alg, Map<String, Object> args) {
        return makeTest(s1, s2, new Alg[] {alg}, args);
    }

    public static List<TestResult> makeTest(String s1, String s2, Alg[] algs) {
        return makeTest(s1, s2, algs, null);
    }

    public static List<TestResult> makeTest(String s1, String s2, Alg[] algs, Map<String, Object> args) {
        List<TestResult> results = new ArrayList<>();

        Consumer<Alg> c = (Alg t) -> {
            Object[] os = null;
            if(args != null) {
                os = new Object[2];

                os[0] = args.get(t.label() + "0");
                os[1] = args.get(t.label() + "1");
            }

            results.add(performTest(s1, s2, t, os));
        };

        Stream.of(algs).forEach(c);

        return results;
    }

    public static List<TestResult> makeTest(String s1, String s2, int on) {
        return makeTest(s1, s2, null, on);
    }

    public static List<TestResult> makeTest(String s1, String s2, Map<String, Object> args, int on) {
        return makeTest(s1, s2, parseAlgs(on), args);
    }

//    --------------------------------------------------------

    public static List<List<TestResult>> makeTests(String s1, String[] s2) {
        return makeTests(s1, s2, null, ALL_ALG);
    }
    
    public static List<List<TestResult>> makeTests(String s1, String[] s2, Map<String, Object> args) {
        return makeTests(s1, s2, args, ALL_ALG);
    }

    public static List<List<TestResult>> makeTests(String s1, String[] s2, Alg alg) {
        return makeTests(s1, s2, new Alg[] {alg});
    }

    public static List<List<TestResult>> makeTests(String s1, String[] s2, Alg alg, Map<String, Object> args) {
        return makeTests(s1, s2, new Alg[] {alg}, args);
    }

    public static List<List<TestResult>> makeTests(String s1, String[] s2, Alg[] algs) {
        return makeTests(s1, s2, algs, null);
    }

    public static List<List<TestResult>> makeTests(String s1, String[] s2, Alg[] algs, Map<String, Object> args) {
        List<List<TestResult>> results = new ArrayList<>();

        Consumer<String> c = (String s) -> {
            List<TestResult> rs = new ArrayList<>();

            for(Alg a : algs) {
                Object[] os = null;
                if(args != null) {
                    Object o0 = args.get(a.label() + "0");
                    Object o1 = args.get(a.label() + "1");
                    
                    if(o0 != null && o1 != null) os = new Object[] {o0, o1};
                    else if(o0 != null) os = new Object[] {o0};
                }

                rs.add(performTest(s1, s, a, os));
            }

            results.add(rs);
        };

        Stream.of(s2).forEach(c);

        return results;
    }

    public static List<List<TestResult>> makeTests(String s1, String[] s2, int on) {
        return makeTests(s1, s2, null, on);
    }

    public static List<List<TestResult>> makeTests(String s1, String[] s2, Map<String, Object> args, int on) {
        return makeTests(s1, s2, parseAlgs(on), args);
    }

//    --------------------------------------------------------

    public static TestResult performTest(String s1, String s2, Alg alg, Object... args) {
        return performTest(s1, s2, alg.buildAlg(args), alg);
    }

    public static TestResult performTest(String s1, String s2, Algorithm algInstance, Alg alg) {
        long nt = System.nanoTime();
        double result = alg.compare(algInstance, s1, s2);
        nt = System.nanoTime() - nt;

        return new TestResult(alg, s1, s2, result, nt);
    }

//    --------------------------------------------------------

    public static void printResult(List<TestResult> result, PrintStream stream) {
        printResult(result, stream, SORTMODE_ALG_CATEGORY);
    }

    public static void printResult(List<TestResult> result, PrintStream stream, int sortMode) {
        printResult(result, stream, sortMode, true);
    }

    public static void printResult(List<TestResult> result, PrintStream stream, int sortMode, boolean descendent) {
        result.sort(Comparators.getComparator(sortMode, descendent));

        result.forEach(r -> {
            try {
                stream.write((r.toString() + "\n").getBytes());
            } catch (IOException ex) {
                System.out.println("exc");
            }
        });
    }

//    --------------------------------------------------------

    public static void printResults(List<List<TestResult>> results, PrintStream stream) {
        printResults(results, stream, SORTMODE_ALG_CATEGORY);
    }

    public static void printResults(List<List<TestResult>> results, PrintStream stream, int sortMode) {
        printResults(results, stream, sortMode, true);
    }

    public static void printResults(List<List<TestResult>> results, PrintStream stream, int sortMode, boolean descendent) {
        if(sortMode == SORTMODE_ALG) {
            for(int i = 0; i < results.get(0).size(); i++) {
                
                try {
                    stream.write(String.format("---------- %s -----------\n", results.get(0).get(i).alg.label()).getBytes());
                } catch (IOException ex) {
                    System.out.println("exc");
                }
                
                final int temp = i;
                results.forEach((l) -> {
                    try {
                        stream.write((l.get(temp).toString() + "\n").getBytes());
                    } catch (IOException ex) {
                        System.out.println("exc");
                    }
                });
            } 
        } else {
            results.forEach((r) -> {
                try {
                    stream.write(String.format("---------- %s -----------\n", r.get(0).s2).getBytes());
                    printResult(r, stream, sortMode, descendent);
                } catch (IOException ex) {
                    System.out.println("exc");
                }
            });
        }
    }

    private static class Comparators {
        static final Comparator<TestResult> algCategory = Comparator.comparingInt((TestResult o) -> o.alg.typeCode());
        static final Comparator<TestResult> algResultDescendent = (TestResult o1, TestResult o2) -> (int) (o2.result - o1.result);
        static final Comparator<TestResult> algTimeDescendent = (TestResult o1, TestResult o2) -> (int) (o2.time - o1.time);
        static final Comparator<TestResult> algResultAscendent = (TestResult o1, TestResult o2) -> (int) (o1.result - o2.result);
        static final Comparator<TestResult> algTimeAscendent = (TestResult o1, TestResult o2) -> (int) (o1.time - o2.time);

        public static Comparator<TestResult> getComparator(int sortMode, boolean descendent) {
            switch (sortMode) {
                case SORTMODE_ALG_CATEGORY:
                    return algCategory;
                case SORTMODE_ALG_RESULT:
                    return descendent ? algResultDescendent : algResultAscendent;
                case SORTMODE_ALG_TIME:
                    return descendent ? algTimeDescendent : algTimeAscendent;
                default:
                    return null;
            }
        }
    }
}
