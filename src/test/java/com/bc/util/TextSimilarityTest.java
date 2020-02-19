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

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Josh
 */
public class TextSimilarityTest {
    
    private final boolean greedy = false;
    
    public TextSimilarityTest() { }

    /**
     * Test similarity similarity method, similarity class TextSimilarity.
     */
    @Test
    public void testOf() {
        System.out.println("of");

        final Map<String[], Integer> params = new LinkedHashMap<>();
        params.put(new String[]{"ABCDE", "ABCD"}, greedy ? 4 : 4);
        params.put(new String[]{"abcde", "ABCD"}, greedy ? 4 : 4);
        params.put(new String[]{"BCDE", "ABCDE"}, greedy ? 4 : 0);
        params.put(new String[]{"bcde", "ABCDE"}, greedy ? 4 : 0);
        params.put(new String[]{null, null}, 0);
        params.put(new String[]{"ABCDE", null}, 0);
        params.put(new String[]{null, "ABCDE"}, 0);
        params.put(new String[]{"ABCDE", null}, 0);
        
        params.put(new String[]{"A whole life", "A role strife"}, greedy ? 7 : 1);
        params.put(new String[]{"A whole life", "Cool Knife"}, greedy ? 5 : 0);
        
        params.put(new String[]{"A whole life", "life whole A"}, greedy ? 10 : 0);

        final TextSimilarity instance = new TextSimilarity(greedy);

        for(Map.Entry<String[], Integer> entry : params.entrySet()) {
            final String [] args = entry.getKey();
            final Integer expResult = entry.getValue();
            final Integer result = instance.similarity(args[0], args[1]);
            System.out.println("Expected: " + expResult + ", found: " + result +
                    " similarity for: " + Arrays.toString(args));
            assertEquals(expResult, result, 0.0);
        }
    }
}
