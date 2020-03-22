package com.bc.util;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @(#)Util.java   11-Apr-2015 07:02:12
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

/**
 * @author   chinomso bassey ikwuagwu
 */
public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String [] args) {

        System.out.println("Printing command line arguments:\n" + java.util.Arrays.toString(args));
    }
}
