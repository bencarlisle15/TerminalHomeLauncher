/*
 * =============================================================================
 *
 *   Copyright (c) 2014-2017, The UNBESCAPE team (http://www.unbescape.org)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 * =============================================================================
 */
package com.bencarlisle15.terminalhomelauncher.tuils.html_escape;

import java.util.Arrays;

/**
 * <p>
 * This class initializes the  structure.
 * </p>
 *
 * @author Daniel Fern&aacute;ndez
 * @since 1.0.0
 */
final class Html4EscapeSymbolsInitializer {


    static HtmlEscapeSymbols initializeHtml4() {

        final HtmlEscapeSymbols.References html4References = new HtmlEscapeSymbols.References();

        /*
         * -----------------------------------------------------------------
         *   HTML4 NAMED CHARACTER REFERENCES (CHARACTER ENTITY REFERENCES)
         *   See: http://www.w3.org/TR/html4/sgml/entities.html
         * -----------------------------------------------------------------
         */

        /* HTML NCRs FOR MARKUP-SIGNIFICANT CHARACTERS */
        // (Note HTML 4 does not include &apos; as a valid NCR)
        html4References.addReference('"', "&quot;");
        html4References.addReference('&', "&amp;");
        html4References.addReference('<', "&lt;");
        html4References.addReference('>', "&gt;");
        /* HTML NCRs FOR ISO-8859-1 CHARACTERS */
        html4References.addReference('\u00A0', "&nbsp;");
        html4References.addReference('¡', "&iexcl;");
        html4References.addReference('¢', "&cent;");
        html4References.addReference('£', "&pound;");
        html4References.addReference('¤', "&curren;");
        html4References.addReference('¥', "&yen;");
        html4References.addReference('¦', "&brvbar;");
        html4References.addReference('§', "&sect;");
        html4References.addReference('¨', "&uml;");
        html4References.addReference('©', "&copy;");
        html4References.addReference('ª', "&ordf;");
        html4References.addReference('«', "&laquo;");
        html4References.addReference('¬', "&not;");
        html4References.addReference('\u00AD', "&shy;");
        html4References.addReference('®', "&reg;");
        html4References.addReference('¯', "&macr;");
        html4References.addReference('°', "&deg;");
        html4References.addReference('±', "&plusmn;");
        html4References.addReference('²', "&sup2;");
        html4References.addReference('³', "&sup3;");
        html4References.addReference('´', "&acute;");
        html4References.addReference('µ', "&micro;");
        html4References.addReference('¶', "&para;");
        html4References.addReference('·', "&middot;");
        html4References.addReference('¸', "&cedil;");
        html4References.addReference('¹', "&sup1;");
        html4References.addReference('º', "&ordm;");
        html4References.addReference('»', "&raquo;");
        html4References.addReference('¼', "&frac14;");
        html4References.addReference('½', "&frac12;");
        html4References.addReference('¾', "&frac34;");
        html4References.addReference('¿', "&iquest;");
        html4References.addReference('À', "&Agrave;");
        html4References.addReference('Á', "&Aacute;");
        html4References.addReference('Â', "&Acirc;");
        html4References.addReference('Ã', "&Atilde;");
        html4References.addReference('Ä', "&Auml;");
        html4References.addReference('Å', "&Aring;");
        html4References.addReference('Æ', "&AElig;");
        html4References.addReference('Ç', "&Ccedil;");
        html4References.addReference('È', "&Egrave;");
        html4References.addReference('É', "&Eacute;");
        html4References.addReference('Ê', "&Ecirc;");
        html4References.addReference('Ë', "&Euml;");
        html4References.addReference('Ì', "&Igrave;");
        html4References.addReference('Í', "&Iacute;");
        html4References.addReference('Î', "&Icirc;");
        html4References.addReference('Ï', "&Iuml;");
        html4References.addReference('Ð', "&ETH;");
        html4References.addReference('Ñ', "&Ntilde;");
        html4References.addReference('Ò', "&Ograve;");
        html4References.addReference('Ó', "&Oacute;");
        html4References.addReference('Ô', "&Ocirc;");
        html4References.addReference('Õ', "&Otilde;");
        html4References.addReference('Ö', "&Ouml;");
        html4References.addReference('×', "&times;");
        html4References.addReference('Ø', "&Oslash;");
        html4References.addReference('Ù', "&Ugrave;");
        html4References.addReference('Ú', "&Uacute;");
        html4References.addReference('Û', "&Ucirc;");
        html4References.addReference('Ü', "&Uuml;");
        html4References.addReference('Ý', "&Yacute;");
        html4References.addReference('Þ', "&THORN;");
        html4References.addReference('ß', "&szlig;");
        html4References.addReference('à', "&agrave;");
        html4References.addReference('á', "&aacute;");
        html4References.addReference('â', "&acirc;");
        html4References.addReference('ã', "&atilde;");
        html4References.addReference('ä', "&auml;");
        html4References.addReference('å', "&aring;");
        html4References.addReference('æ', "&aelig;");
        html4References.addReference('ç', "&ccedil;");
        html4References.addReference('è', "&egrave;");
        html4References.addReference('é', "&eacute;");
        html4References.addReference('ê', "&ecirc;");
        html4References.addReference('ë', "&euml;");
        html4References.addReference('ì', "&igrave;");
        html4References.addReference('í', "&iacute;");
        html4References.addReference('î', "&icirc;");
        html4References.addReference('ï', "&iuml;");
        html4References.addReference('ð', "&eth;");
        html4References.addReference('ñ', "&ntilde;");
        html4References.addReference('ò', "&ograve;");
        html4References.addReference('ó', "&oacute;");
        html4References.addReference('ô', "&ocirc;");
        html4References.addReference('õ', "&otilde;");
        html4References.addReference('ö', "&ouml;");
        html4References.addReference('÷', "&divide;");
        html4References.addReference('ø', "&oslash;");
        html4References.addReference('ù', "&ugrave;");
        html4References.addReference('ú', "&uacute;");
        html4References.addReference('û', "&ucirc;");
        html4References.addReference('ü', "&uuml;");
        html4References.addReference('ý', "&yacute;");
        html4References.addReference('þ', "&thorn;");
        html4References.addReference('ÿ', "&yuml;");
        /* HTML NCRs FOR SYMBOLS, MATHEMATICAL SYMBOLS AND GREEK LETTERS */
        /* - Greek */
        html4References.addReference('ƒ', "&fnof;");
        html4References.addReference('Α', "&Alpha;");
        html4References.addReference('Β', "&Beta;");
        html4References.addReference('Γ', "&Gamma;");
        html4References.addReference('Δ', "&Delta;");
        html4References.addReference('Ε', "&Epsilon;");
        html4References.addReference('Ζ', "&Zeta;");
        html4References.addReference('Η', "&Eta;");
        html4References.addReference('Θ', "&Theta;");
        html4References.addReference('Ι', "&Iota;");
        html4References.addReference('Κ', "&Kappa;");
        html4References.addReference('Λ', "&Lambda;");
        html4References.addReference('Μ', "&Mu;");
        html4References.addReference('Ν', "&Nu;");
        html4References.addReference('Ξ', "&Xi;");
        html4References.addReference('Ο', "&Omicron;");
        html4References.addReference('Π', "&Pi;");
        html4References.addReference('Ρ', "&Rho;");
        html4References.addReference('Σ', "&Sigma;");
        html4References.addReference('Τ', "&Tau;");
        html4References.addReference('Υ', "&Upsilon;");
        html4References.addReference('Φ', "&Phi;");
        html4References.addReference('Χ', "&Chi;");
        html4References.addReference('Ψ', "&Psi;");
        html4References.addReference('Ω', "&Omega;");
        html4References.addReference('α', "&alpha;");
        html4References.addReference('β', "&beta;");
        html4References.addReference('γ', "&gamma;");
        html4References.addReference('δ', "&delta;");
        html4References.addReference('ε', "&epsilon;");
        html4References.addReference('ζ', "&zeta;");
        html4References.addReference('η', "&eta;");
        html4References.addReference('θ', "&theta;");
        html4References.addReference('ι', "&iota;");
        html4References.addReference('κ', "&kappa;");
        html4References.addReference('λ', "&lambda;");
        html4References.addReference('μ', "&mu;");
        html4References.addReference('ν', "&nu;");
        html4References.addReference('ξ', "&xi;");
        html4References.addReference('ο', "&omicron;");
        html4References.addReference('π', "&pi;");
        html4References.addReference('ρ', "&rho;");
        html4References.addReference('ς', "&sigmaf;");
        html4References.addReference('σ', "&sigma;");
        html4References.addReference('τ', "&tau;");
        html4References.addReference('υ', "&upsilon;");
        html4References.addReference('φ', "&phi;");
        html4References.addReference('χ', "&chi;");
        html4References.addReference('ψ', "&psi;");
        html4References.addReference('ω', "&omega;");
        html4References.addReference('ϑ', "&thetasym;");
        html4References.addReference('ϒ', "&upsih;");
        html4References.addReference('ϖ', "&piv;");
        /* - General punctuation */
        html4References.addReference('•', "&bull;");
        html4References.addReference('…', "&hellip;");
        html4References.addReference('′', "&prime;");
        html4References.addReference('″', "&Prime;");
        html4References.addReference('‾', "&oline;");
        html4References.addReference('⁄', "&frasl;");
        /* - Letter-like symbols */
        html4References.addReference('℘', "&weierp;");
        html4References.addReference('ℑ', "&image;");
        html4References.addReference('ℜ', "&real;");
        html4References.addReference('™', "&trade;");
        html4References.addReference('ℵ', "&alefsym;");
        /* - Arrows */
        html4References.addReference('←', "&larr;");
        html4References.addReference('↑', "&uarr;");
        html4References.addReference('→', "&rarr;");
        html4References.addReference('↓', "&darr;");
        html4References.addReference('↔', "&harr;");
        html4References.addReference('↵', "&crarr;");
        html4References.addReference('⇐', "&lArr;");
        html4References.addReference('⇑', "&uArr;");
        html4References.addReference('⇒', "&rArr;");
        html4References.addReference('⇓', "&dArr;");
        html4References.addReference('⇔', "&hArr;");
        /* - Mathematical operators */
        html4References.addReference('∀', "&forall;");
        html4References.addReference('∂', "&part;");
        html4References.addReference('∃', "&exist;");
        html4References.addReference('∅', "&empty;");
        html4References.addReference('∇', "&nabla;");
        html4References.addReference('∈', "&isin;");
        html4References.addReference('∉', "&notin;");
        html4References.addReference('∋', "&ni;");
        html4References.addReference('∏', "&prod;");
        html4References.addReference('∑', "&sum;");
        html4References.addReference('−', "&minus;");
        html4References.addReference('∗', "&lowast;");
        html4References.addReference('√', "&radic;");
        html4References.addReference('∝', "&prop;");
        html4References.addReference('∞', "&infin;");
        html4References.addReference('∠', "&ang;");
        html4References.addReference('∧', "&and;");
        html4References.addReference('∨', "&or;");
        html4References.addReference('∩', "&cap;");
        html4References.addReference('∪', "&cup;");
        html4References.addReference('∫', "&int;");
        html4References.addReference('∴', "&there4;");
        html4References.addReference('∼', "&sim;");
        html4References.addReference('≅', "&cong;");
        html4References.addReference('≈', "&asymp;");
        html4References.addReference('≠', "&ne;");
        html4References.addReference('≡', "&equiv;");
        html4References.addReference('≤', "&le;");
        html4References.addReference('≥', "&ge;");
        html4References.addReference('⊂', "&sub;");
        html4References.addReference('⊃', "&sup;");
        html4References.addReference('⊄', "&nsub;");
        html4References.addReference('⊆', "&sube;");
        html4References.addReference('⊇', "&supe;");
        html4References.addReference('⊕', "&oplus;");
        html4References.addReference('⊗', "&otimes;");
        html4References.addReference('⊥', "&perp;");
        html4References.addReference('⋅', "&sdot;");
        /* - Miscellaneous technical */
        html4References.addReference('⌈', "&lceil;");
        html4References.addReference('⌉', "&rceil;");
        html4References.addReference('⌊', "&lfloor;");
        html4References.addReference('⌋', "&rfloor;");
        html4References.addReference('〈', "&lang;");
        html4References.addReference('〉', "&rang;");
        /* - Geometric shapes */
        html4References.addReference('◊', "&loz;");
        html4References.addReference('♠', "&spades;");
        html4References.addReference('♣', "&clubs;");
        html4References.addReference('♥', "&hearts;");
        html4References.addReference('♦', "&diams;");
        /* HTML NCRs FOR INTERNATIONALIZATION CHARACTERS */
        /* - Latin Extended-A */
        html4References.addReference('Œ', "&OElig;");
        html4References.addReference('œ', "&oelig;");
        html4References.addReference('Š', "&Scaron;");
        html4References.addReference('š', "&scaron;");
        html4References.addReference('Ÿ', "&Yuml;");
        /* - Spacing modifier letters */
        html4References.addReference('ˆ', "&circ;");
        html4References.addReference('˜', "&tilde;");
        /* - General punctuation */
        html4References.addReference('\u2002', "&ensp;");
        html4References.addReference('\u2003', "&emsp;");
        html4References.addReference('\u2009', "&thinsp;");
        html4References.addReference('\u200C', "&zwnj;");
        html4References.addReference('\u200D', "&zwj;");
        html4References.addReference('\u200E', "&lrm;");
        html4References.addReference('\u200F', "&rlm;");
        html4References.addReference('–', "&ndash;");
        html4References.addReference('—', "&mdash;");
        html4References.addReference('‘', "&lsquo;");
        html4References.addReference('’', "&rsquo;");
        html4References.addReference('‚', "&sbquo;");
        html4References.addReference('“', "&ldquo;");
        html4References.addReference('”', "&rdquo;");
        html4References.addReference('„', "&bdquo;");
        html4References.addReference('†', "&dagger;");
        html4References.addReference('‡', "&Dagger;");
        html4References.addReference('‰', "&permil;");
        html4References.addReference('‹', "&lsaquo;");
        html4References.addReference('›', "&rsaquo;");
        html4References.addReference('€', "&euro;");


        /*
         * Initialization of escape levels.
         * Defined levels :
         *
         *    - Level 0 : Only markup-significant characters except the apostrophe (')
         *    - Level 1 : Only markup-significant characters (including the apostrophe)
         *    - Level 2 : Markup-significant characters plus all non-ASCII
         *    - Level 3 : All non-alphanumeric characters
         *    - Level 4 : All characters
         */
        final byte[] escapeLevels = new byte[0x7f + 2];
        Arrays.fill(escapeLevels, (byte) 3);
        for (char c = 'A'; c <= 'Z'; c++) {
            escapeLevels[c] = 4;
        }
        for (char c = 'a'; c <= 'z'; c++) {
            escapeLevels[c] = 4;
        }
        for (char c = '0'; c <= '9'; c++) {
            escapeLevels[c] = 4;
        }
        escapeLevels['\''] = 1;
        escapeLevels['"'] = 0;
        escapeLevels['<'] = 0;
        escapeLevels['>'] = 0;
        escapeLevels['&'] = 0;
        escapeLevels[0x7f + 1] = 2;


        return new HtmlEscapeSymbols(html4References, escapeLevels);

    }


    private Html4EscapeSymbolsInitializer() {
        super();
    }

}

