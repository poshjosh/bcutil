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

import java.util.HashSet;
import java.util.Set;

/**
 * @author Chinomso Bassey Ikwuagwu on Mar 13, 2019 4:22:22 PM
 */
public class TextSimilarity implements Similarity<String> {
    
    private final boolean ignoreCase;
    private final boolean removeWhiteSpace;
    private final boolean greedy;

    public TextSimilarity() {
        this(false);
    }
    
    public TextSimilarity(boolean greedy) {
        this(true, true, greedy);
    }
    
    public TextSimilarity(boolean ignoreCase, boolean removeWhiteSpace, boolean greedy) {
        this.ignoreCase = ignoreCase;
        this.removeWhiteSpace = removeWhiteSpace;
        this.greedy = greedy;
    }

    @Override
    public int similarity(String s1, String s2) {
        final boolean hasS1 = ! this.isNullOrEmpty(s1);
        final boolean hasS2 = ! this.isNullOrEmpty(s2);
        if(hasS1 && hasS2) {
            return getSimilarity(this.format(s1), this.format(s2), greedy);
        }else{
            return 0;
        }
    }
    
    private boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    protected String format(String s) {
        return s == null ? null : (!removeWhiteSpace) ? s : s.replaceAll("\\s", "");
    }

    public int getSimilarity(String s1, String s2, boolean greedy) {
        final int result;
        if (! greedy) {
          result = countSimilarAdjacentIndices(s1, s2);
        }else {
          result = getSimilarIndices(s1, s2).size();
        }
        return result;
    }

    public Set<Integer> getSimilarIndices (String s1, String s2) {

        final HashSet<Integer> s = new HashSet<>();

        for (int i = 0; i < s1.length(); i++) {

            for (int j = 0; j < s2.length(); j++) {

                if (compare(s1.charAt(i), s2.charAt(j)) == 0) {
                    if(s.add(j)) { 
                        break;
                    }
                }
            }
        }
        return s;
    }

    public Set<Integer> getSimilarAdjacentIndices(String s1, String s2){

        final int lesserLength = s1.length() < s2.length() ? s1.length() : s2.length();

        final HashSet<Integer> result = new HashSet<>(lesserLength);

        for (int i = 0; i < lesserLength; i++) {
            if(compare(s1.charAt(i), s2.charAt(i)) == 0) {
                result.add(i);
            }
        }

        return result;
    }

    public int countSimilarAdjacentIndices(String s1, String s2){

        final int lesserLength = s1.length() < s2.length() ? s1.length() : s2.length();

        int result = 0;

        for (int i = 0; i < lesserLength; i++) {
            if(compare(s1.charAt(i), s2.charAt(i)) == 0) {
                ++result;
            }
        }

        return result;
    }

    public int compare(char c1, char c2) {
        if(ignoreCase) {
            final boolean c1state = Character.isUpperCase(c1);
            final boolean c2state = Character.isUpperCase(c2);
            if(c1state != c2state) {
                c1 = Character.toUpperCase(c1);
                c2 = Character.toUpperCase(c2);
            }
        }
  //    if(c1 > c2) {
  //        return -1;
  //    }else if(c1 == c2) {
  //        return 0;
  //    }else {
  //        return 1;
  //    }
        return Character.compare(c1, c2);
    }

    public final boolean isGreedy() {
        return greedy;
    }

    public final boolean isIgnoreCase() {
        return ignoreCase;
    }

    public final boolean isRemoveWhiteSpace() {
        return removeWhiteSpace;
    }
}
