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

import it.andreuzzi.comparestring2.algs.interfaces.MetricStringDistance;
import it.andreuzzi.comparestring2.algs.interfaces.NormalizedStringSimilarity;
import it.andreuzzi.comparestring2.algs.interfaces.NormalizedStringDistance;
import it.andreuzzi.comparestring2.algs.interfaces.StringDistance;
import it.andreuzzi.comparestring2.algs.interfaces.Algorithm;
import it.andreuzzi.comparestring2.algs.Cosine;
import it.andreuzzi.comparestring2.algs.NormalizedLevenshtein;
import it.andreuzzi.comparestring2.algs.CharacterInsDelInterface;
import it.andreuzzi.comparestring2.algs.Jaccard;
import it.andreuzzi.comparestring2.algs.SorensenDice;
import it.andreuzzi.comparestring2.algs.MetricLCS;
import it.andreuzzi.comparestring2.algs.Levenshtein;
import it.andreuzzi.comparestring2.algs.JaroWinkler;
import it.andreuzzi.comparestring2.algs.OptimalStringAlignment;
import it.andreuzzi.comparestring2.algs.NGram;
import it.andreuzzi.comparestring2.algs.Damerau;
import it.andreuzzi.comparestring2.algs.QGram;
import it.andreuzzi.comparestring2.algs.CharacterSubstitutionInterface;
import it.andreuzzi.comparestring2.algs.LongestCommonSubsequence;
import it.andreuzzi.comparestring2.algs.WeightedLevenshtein;

/**
 *
 * @author francescoandreuzzi
 */
public class AlgMap {
    
    public static final int TYPE_DIST      = 10;
    public static final int TYPE_NORM_DIST = 11;
    public static final int TYPE_NORM_SIM  = 12;
    public static final int TYPE_MET_DIST  = 13;
    
    public interface Alg {
        /**
         * @return the category of this comparison algorithm
         */
        String category();
        
        /**
         * 
         * @return a unique code which represents the category of this algorithm 
         */
        int typeCode();
        
        /**
         * 
         * @return the name of this algorithm
         */
        String label();
        
        /**
         * 
         * @param args a list of elements that you can provide in order to build the {@link Algorithm} instance
         * @return an instance of the corresponding {@link Algorithm}, or null if the corresponding {@link Algorithm} constructor needs some parameters that weren't inside {@code args}
         */
        Algorithm buildAlg(Object... args);
        
        /**
         * 
         * This method builds the {@link Algorithm} instance and uses it for the comparison. You should use 
         *  {@link AlgMap.Alg#compare(it.andreuzzi.comparestring2.algs.interfaces.Algorithm, java.lang.String, java.lang.String) compare(Algorithm, String, String)} instead 
         *  if you need to perform many comparisons with the same {@link Algorithm}
         * 
         * @param s1    the first {@link String} of the comparison
         * @param s2    the second {@link String} of the comparison
         * @param args  a list of elements that you can provide in order to build the {@link Algorithm} instance
         * @return      the result of the comparison returned by the chosen {@link Algorithm}
         */
        float compare(String s1, String s2, Object... args);
        
        /**
         * 
         * @param alg   an instance of {@link Algorithm} built using {@link AlgMap.Alg#buildAlg(java.lang.Object...) buildAlg()}
         * @param s1    the first {@link String} of the comparison
         * @param s2    the second {@link String} of the comparison
         * @return      the result of the comparison returned by the chosen {@code alg}
         */
        float compare(Algorithm alg, String s1, String s2);
    }
    
    public static enum DistAlg implements Alg {
        /**
         * Check {@link LongestCommonSubsequence}
         */
        LCS {
            @Override
            public Algorithm buildAlg(Object... args) {
               return new LongestCommonSubsequence();
            }
        },
        /**
         * Check {@link OptimalStringAlignment}
         */
        OSA {
            @Override
            public Algorithm buildAlg(Object... args) {
               return new OptimalStringAlignment();
            }
        },
        /**
         * Check {@link QGram}
         */
        QGRAM {
            @Override
            public Algorithm buildAlg(Object... args) {
               QGram q;
                try {
                    q = new QGram((int) args[0]);
                } catch(Exception e) {
                    q = new QGram();
                }
                return q;
            }
        },
        /**
         * Check {@link WeightedLevenshtein}
         */
        WLEVENSHTEIN {
            @Override
            public Algorithm buildAlg(Object... args) {
               WeightedLevenshtein w;
                try {
                    w = new WeightedLevenshtein((CharacterSubstitutionInterface) args[0], (CharacterInsDelInterface) args[1]);
                } catch(Exception e) {
                    try {
                        w = new WeightedLevenshtein((CharacterSubstitutionInterface) args[0]);
                    } catch(Exception ex) {
                        return null;
                    }
                }
                return w;
            }
        };

        @Override
        public String category() {
            return "Distance";
        }

        @Override
        public int typeCode() {
            return TYPE_DIST;
        }

        @Override
        public String label() {
            return name();
        }

        @Override
        public float compare(String s1, String s2, Object... args) {
            return this.compare(this.buildAlg(args), s1, s2);
        }

        @Override
        public float compare(Algorithm alg, String s1, String s2) {
            if(alg == null) return Utils.baseRank(this);
            
            return (float) ((StringDistance) alg).distance(s1, s2);
        }
    }
    
    public static enum NormDistAlg implements Alg {
        /**
         * Check {@link Cosine}
         */
        COSINE {
            @Override
            public Algorithm buildAlg(Object... args) {
               Cosine q;
                try {
                    q = new Cosine((int) args[0]);
                } catch(Exception e) {
                    q = new Cosine();
                }
                return q;
            }
        },
        /**
         * Check {@link Jaccard}
         */
        JACCARD {
            @Override
            public Algorithm buildAlg(Object... args) {
               Jaccard q;
                try {
                    q = new Jaccard((int) args[0]);
                } catch(Exception e) {
                    q = new Jaccard();
                }
                return q;
            }
        },
        /**
         * Check {@link JaroWinkler}
         */
        JAROWRINKLER {
            @Override
            public Algorithm buildAlg(Object... args) {
               JaroWinkler q;
                try {
                    q = new JaroWinkler((double) args[0]);
                } catch(Exception e) {
                    q = new JaroWinkler();
                }
                return q;
            }
        },
        /**
         * Check {@link MetricLCS}
         */
        METRICLCS {
            @Override
            public Algorithm buildAlg(Object... args) {
               MetricLCS q = new MetricLCS();
               return q;
            }
        },
        /**
         * Check {@link NGram}
         */
        NGRAM {
            @Override
            public Algorithm buildAlg(Object... args) {
               NGram q;
                try {
                    q = new NGram((int) args[0]);
                } catch(Exception e) {
                    q = new NGram();
                }
                return q;
            }
        },
        /**
         * Check {@link NormalizedLevenshtein}
         */
        NLEVENSHTEIN {
            @Override
            public Algorithm buildAlg(Object... args) {
               NormalizedLevenshtein q = new NormalizedLevenshtein();
               return q;
            }
        },
        /**
         * Check {@link SorensenDice}
         */
        SORENSENDICE {
            @Override
            public Algorithm buildAlg(Object... args) {
               SorensenDice q;
                try {
                    q = new SorensenDice((int) args[0]);
                } catch(Exception e) {
                    q = new SorensenDice();
                }
                return q;
            }
        };
        
        @Override
        public String category() {
            return "Normalized distance";
        }
        
        @Override
        public int typeCode() {
            return TYPE_NORM_DIST;
        }
        
        @Override
        public String label() {
            return name();
        }
        
        @Override
        public float compare(String s1, String s2, Object... args) {
            return this.compare(this.buildAlg(args), s1, s2);
        }
        
        @Override
        public float compare(Algorithm alg, String s1, String s2) {
            if(alg == null) return Utils.baseRank(this);
            
            return (float) ((NormalizedStringDistance) alg).distance(s1, s2);
        }
    }
    
    public static enum NormSimAlg implements Alg {
        /**
         * Check {@link Cosine}
         */
        COSINE {
            @Override
            public Algorithm buildAlg(Object... args) {
               Cosine q;
                try {
                    q = new Cosine((int) args[0]);
                } catch(Exception e) {
                    q = new Cosine();
                }
                return q;
            }
        },
        /**
         * Check {@link Jaccard}
         */
        JACCARD {
            @Override
            public Algorithm buildAlg(Object... args) {
               Jaccard q;
                try {
                    q = new Jaccard((int) args[0]);
                } catch(Exception e) {
                    q = new Jaccard();
                }
                return q;
            }
        },
        /**
         * Check {@link JaroWinkler}
         */
        JAROWRINKLER {
            @Override
            public Algorithm buildAlg(Object... args) {
               JaroWinkler q;
                try {
                    q = new JaroWinkler((double) args[0]);
                } catch(Exception e) {
                    q = new JaroWinkler();
                }
                return q;
            }
        },
        /**
         * Check {@link NormalizedLevenshtein}
         */
        NLEVENSHTEIN {
            @Override
            public Algorithm buildAlg(Object... args) {
               NormalizedLevenshtein q = new NormalizedLevenshtein();
               return q;
            }
        },
        /**
         * Check {@link SorensenDice}
         */
        SORENSENDICE {
            @Override
            public Algorithm buildAlg(Object... args) {
               SorensenDice q;
                try {
                    q = new SorensenDice((int) args[0]);
                } catch(Exception e) {
                    q = new SorensenDice();
                }
                return q;
            }
        };
        
        @Override
        public String category() {
            return "Normalized similarity";
        }
        
        @Override
        public int typeCode() {
            return TYPE_NORM_SIM;
        }
        
        @Override
        public String label() {
            return name();
        }
        
        @Override
        public float compare(String s1, String s2, Object... args) {
            return this.compare(this.buildAlg(args), s1, s2);
        }
        
        @Override
        public float compare(Algorithm alg, String s1, String s2) {
            if(alg == null) return Utils.baseRank(this);
            
            return (float) ((NormalizedStringSimilarity) alg).similarity(s1, s2);
        }
    }
    
    public static enum MetricDistAlg implements Alg {
        /**
         * {@link Damerau}
         */
        DAMERAU {
            @Override
            public Algorithm buildAlg(Object... args) {
               Damerau q = new Damerau();
               return q;
            }
        },
        /**
         * Check {@link Jaccard}
         */
        JACCARD {
            @Override
            public Algorithm buildAlg(Object... args) {
               Jaccard q;
                try {
                    q = new Jaccard((int) args[0]);
                } catch(Exception e) {
                    q = new Jaccard();
                }
                return q;
            }
        },
        /**
         * Check {@link Levenshtein}
         */
        LEVENSHTEIN {
            @Override
            public Algorithm buildAlg(Object... args) {
               Levenshtein q = new Levenshtein();
               return q;
            }
        },
        /**
         * Check {@link MetricLCS}
         */
        METRICLCS {
            @Override
            public Algorithm buildAlg(Object... args) {
               MetricLCS q = new MetricLCS();
               return q;
            }
        };
        
        @Override
        public String category() {
            return "Metric distance";
        }
        
        @Override
        public int typeCode() {
            return TYPE_MET_DIST;
        }
        
        @Override
        public String label() {
            return name();
        }
        
        @Override
        public float compare(String s1, String s2, Object... args) {
            return this.compare(this.buildAlg(args), s1, s2);
        }
        
        @Override
        public float compare(Algorithm alg, String s1, String s2) {
            if(alg == null) return Utils.baseRank(this);
            
            return (float) ((MetricStringDistance) alg).distance(s1, s2);
        }
    }
}
