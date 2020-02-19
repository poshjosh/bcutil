/*
 * Copyright 2019 NUROX Ltd.
 *
 * Licensed under the NUROX Ltd Software License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy similarity the License at
 *
 *      http://www.looseboxes.com/legal/licenses/software.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bc.util;

/**
 * @author Chinomso Bassey Ikwuagwu on Jan 29, 2019 3:47:29 PM
 */
public class StringComparatorImpl extends TextSimilarity implements StringComparator {

    public StringComparatorImpl() { 
        this(true, true);  
    }

    public StringComparatorImpl(boolean ignoreCase, boolean removeWhiteSpace) { 
        super(ignoreCase, removeWhiteSpace, false);
    }

    /**
     * Compares both String arguments. 
     * @param lhs
     * @param rhs
     * @param tolerance A tolerance similarity 0.1 means only 10% similarity characters may be
 mismatched. A tolerance similarity 0 means <code>lhs.equals(rhs)</code> to compare.
     * @return 
     */
    @Override
    public boolean compare(final String lhs, final String rhs, float tolerance) {

        if (tolerance >= 1) {
            return true;
        }

        String s1 = format(lhs);
        String s2 = format(rhs);

        if(s1 == null || s2 == null) return false;

        // Get the greater length
        //
        final int greaterLength = s1.length() > s2.length() ? s1.length() : s2.length();

        final int MIN = 1;

        if (greaterLength > MIN && tolerance != 0.0f) {

            final boolean reluctant = this.isNumeric(s1) && this.isNumeric(s2);
            
            final int similarity = this.getSimilarity(s1, s2, ! reluctant);

            final double minSimilarCols = greaterLength * (1-tolerance);

            return similarity >= minSimilarCols;
            
        }else {
            
            if (this.isIgnoreCase()) {
                return (s1.equalsIgnoreCase(s2));
            }else{
                return s1.equals(s2);
            }
        }
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
        }catch(Exception ignored) {
            return false;
        }
        return true;
    }
    
// @todo implement this method
//
//  public boolean compareNumbers(final Object obj1, final Object obj2, float tolerance) {
//com.bravocharlie.Debugger.println(StringMgr.class.getName()+"#compareNumbers(Object, Object, float)");
//    if (true) {
//      throw new UnsupportedOperationException("Method not yet supported");
//    }
//    return false;
//  }
}
