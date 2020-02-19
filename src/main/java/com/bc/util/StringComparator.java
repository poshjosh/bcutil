package com.bc.util;

/**
 * @author Chinomso Bassey Ikwuagwu on Jan 29, 2019 3:44:10 PM
 */
public interface StringComparator extends Similarity<String> {

    /**
     * Compares both String arguments. 
     * @param lhs
     * @param rhs
     * @param tolerance A tolerance of 0.1 means only 10% of characters may be
     * mismatched. A tolerance of 0 means <code>lhs.equals(rhs)</code> to compare.
     * @return 
     */
    boolean compare(final String lhs, final String rhs, float tolerance);

    int compare(char lhs, char rhs);
}
