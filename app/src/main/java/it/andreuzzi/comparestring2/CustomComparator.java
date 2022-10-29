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
import java.util.Comparator;

/**
 *              This class manages the two cases (biggerIsBetter = true, false). Moreover, if the ranks are the same, the method {@link Comparable#compareTo(java.lang.Object) compareTo} will
 *                  be used (if available)
 * 
 * @author      francescoandreuzzi
 * @param       <T> the type of objects that may be compared
 */
public class CustomComparator <T extends CompareItem> implements Comparator<T> {
    
    public boolean biggerIsBetter;

    @Override
    public int compare(T o1, T o2) {
        if(o1.r == o2.r) {
            try {
                return o1.o.getString().compareTo(o2.o.getString());
            } catch(Exception exc) {
                exc.printStackTrace();
            }
            
            return o1.o.getLowercaseString().compareTo(o2.o.getLowercaseString());
	} else if(biggerIsBetter) {
            return (int) Math.signum(o2.r - o1.r);
        } else {
            return (int) Math.signum(o1.r - o2.r);
        }
    }
    
}
