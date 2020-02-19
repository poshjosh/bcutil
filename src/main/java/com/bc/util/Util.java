package com.bc.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
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
 * @version  2.0
 * @since    2.0
 */
public class Util {

  private transient static final Logger LOG = Logger.getLogger(Util.class.getName());

  public final static <E> List<E> asList(E [] array) {
    final List<E> output;
      switch (array.length) {
          case 0:
              output = Collections.EMPTY_LIST;
              break;
          case 1:
              output = Collections.singletonList(array[0]);
              break;
          default:
              output = Arrays.asList(array);
              break;
      }
    return output;
  }
    
  public final static <E> List<E> unmodifiableList(Collection<E> collection) {
    final List<E> output;
    if(collection.isEmpty()) {
        output = Collections.EMPTY_LIST;
    }else if(collection.size() == 1) {
        output = Collections.singletonList(collection.iterator().next());
    }else{
        final List list = collection instanceof List ? (List)collection : new ArrayList(collection);
        output = Collections.unmodifiableList(list);
    }
    return output;
  }

  public final static long availableMemory() {
    final Runtime runtime = Runtime.getRuntime();
    final long heapMemory = runtime.totalMemory(); // current heap allocated to the VM process
    final long freeHeapMemory = runtime.freeMemory(); // out of the current heap, how much is free
    final long maxHeapMemory = runtime.maxMemory(); // Max heap VM can use e.g. Xmx setting
    final long usedHeapMemory = heapMemory - freeHeapMemory; // how much of the current heap the VM is using
    final long availableHeapMemory = maxHeapMemory - usedHeapMemory; // available memory i.e. Maximum heap size minus the current amount used
    return availableHeapMemory;
  }  

  public final static long usedMemory(long bookmarkMemory) {
    return bookmarkMemory - availableMemory();
  }
  
  public final static String removeNonBasicMultilingualPlaneChars(String test) {
      
    StringBuilder sb = new StringBuilder(test.length());
    
    for (int ii = 0; ii < test.length(); ) {
          
       int codePoint = test.codePointAt(ii);
       
       if (codePoint > 0xFFFF) {
         ii += Character.charCount(codePoint);
       }else {
         sb.appendCodePoint(codePoint);
         ii++;
       }
    }
    
    return sb.toString();
  }

  /**
     * @param path The path for which a relative path will be returned
     * @param basePath The base path of the path for which a relative path will be returned
     * @return A relative path 
     * @throws IllegalArgumentException If the second method argument 
     * (<code>path</code>) does not start with the first (<code>basePath</code>).
     */
    public static String getRelativePath(String basePath, String path) throws IllegalArgumentException {

        path = normalize(path);
        basePath = normalize(basePath);
        
        int index = path.indexOf(basePath);
        
        if(index < 0) throw new IllegalArgumentException("Input must start with: "+basePath+", Found: "+path);

        String output = path.substring(index + basePath.length());
//Logger.getLogger(this.getClass().getName()).info("Input: "+absolutePath+
//        "\nOutput: "+output);
        return output;
    }
    
    private static String normalize(String path) {
        return path.replace('\\', '/').trim();
    }

    /**
     * @param url 
     * @return The base URL of the method argument or <code>null</code> if the 
     * method argument is a malformed URL
     */
    public static String getBaseURL(String url) {
        return UrlUtil.getBaseURL(url);
    }
    
    public static String getBaseURL(URL url) {
        return UrlUtil.getBaseURL(url);
    }

    /**
     * @param path The path whose file name extension will be returned
     * @return The file name extension of the input path. <br/><br/>
     * For a path of <tt>/folder/folder/file.xyz</tt> returns <tt>xyz</tt>
     */
    public static String getExtension(String path) {
        
//        This doesn't work for filenames with ':'. e.g http://
//        String ext = Paths.get(path).getFileName().toString();

        final int len = path.length();

        final int b = path.lastIndexOf('?', len-1);
        
        final int a = path.lastIndexOf('.', len-1); 
        
        String ext = a == -1 ? null : path.substring(a + 1, (b == -1 ? len : b) );
        
        return ext;
    }
    
    /**
     * @param size The returned int will be of range: 0 - <tt>size</tt>
     * @return  a pseudorandom <code>int</code> greater than or equal 
     * to <code>0</code> and less than the input<code>size</code>.
     * @see https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java?rq=1
     */
    public static int randomInt(int size) {
//        final double numbr = random(size);
//        return (int)Math.floor(numbr);
        final int randomNum = ThreadLocalRandom.current().nextInt(size);
        return randomNum;
    }
    
    /**
     * Returns a <code>double</code> value with a positive sign, greater 
     * than or equal to <code>0</code> and less than input <code>size</code>. 
     * Returned values are chosen pseudo-randomly with (approximately) 
     * uniform distribution from that range. 
     * 
     * <p>When this method is first called, it creates a single new
     * pseudorandom-number generator, exactly as if by the expression
     * <blockquote><pre>new java.util.Random</pre></blockquote> This
     * new pseudorandom-number generator is used thereafter for all
     * calls to this method and is used nowhere else.
     * 
     * <p>This method is properly synchronized to allow correct use by
     * more than one thread. However, if many threads need to generate
     * pseudorandom numbers at a great rate, it may reduce contention
     * for each thread to have its own pseudorandom-number generator.
     *  
     * @param size The returned double will be of range: 0 - <tt>size</tt>
     * @return  a pseudorandom <code>double</code> greater than or equal 
     * to <code>0</code> and less than the input<code>size</code>.
     * @see https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java?rq=1
     */
    public static double random(double size) {
//        double random = Math.random();
//        double numbr = (random * size);
//        return numbr;
        final double randomDouble = ThreadLocalRandom.current().nextDouble(size);
        return randomDouble;
    }
    
    public static boolean removeNulls(Collection c) {
    
        if(c == null || c.isEmpty()) {
            return false;
        }
        
        // Collection.remove(null) will only remove the first null value
        // So we use Collection.removeAll(Collection c);
        //
        boolean success = c.removeAll(Collections.singleton(null)); 

        return success;
    }

    /**
     * @return true if the ExecutorService terminated correctly, false otherwise
     * @see com.bc.process.ProcessManager#shutdownAndAwaitTermination(java.util.concurrent.ExecutorService, long, java.util.concurrent.TimeUnit, java.util.List) 
     * @see java.util.concurrent.ExecutorService
     */
    public static boolean shutdownAndAwaitTermination(
            ExecutorService pool, long timeout, TimeUnit unit) {
        
        return shutdownAndAwaitTermination(pool, timeout, unit, null);
    }
    
    /**
     * Shuts down an ExecutorService in two phases, first by calling 
     * shutdown to reject incoming tasks, and then calling shutdownNow,
     * if necessary, to cancel any lingering tasks.
     * <br/><br/>
     * <b>Note:</b> This method was culled from the ExecutorService documentation
     * @return true if the ExecutorService terminated correctly, false otherwise
     * @see java.util.concurrent.ExecutorService
     */
    public static boolean shutdownAndAwaitTermination(
            ExecutorService pool, long timeout, 
            TimeUnit unit, List<Runnable> addInterruptedHere)  {

        if(!pool.isShutdown()) {
            pool.shutdown(); // Disable new tasks from being submitted
        }

        return awaitTermination(pool, timeout, unit, addInterruptedHere);
    }

    /**
     * <b>Note:</b> This method was culled from the ExecutorService documentation
     * @return true if the ExecutorService terminated correctly, false otherwise
     * @see java.util.concurrent.ExecutorService
     */
    public static boolean awaitTermination(
            ExecutorService pool, long timeout, TimeUnit unit)  {
        
        return awaitTermination(pool, timeout, unit, null);
    }
    /**
     * <b>Note:</b> This method was culled from the ExecutorService documentation
     * @return true if the ExecutorService terminated correctly, false otherwise
     * @see java.util.concurrent.ExecutorService
     */
    public static boolean awaitTermination(
            ExecutorService pool, long timeout, 
            TimeUnit unit, List<Runnable> addInterruptedHere)  {
        
        try {
            
            return terminate(pool, timeout, unit, addInterruptedHere);
            
        } catch (InterruptedException ie) {
            final String message = "Interrupted while awaiting termination of ExecutorService:: " + pool;
            if(LOG.isLoggable(Level.FINE)) {
                LOG.log(Level.WARNING, message, ie);
            }else{
                LOG.warning(message);
            }
        }
         
        return true;
    }

    /**
     * <b>Note:</b> This method was culled from the ExecutorService documentation
     * @return true if the ExecutorService terminated correctly, false otherwise
     * @see java.util.concurrent.ExecutorService
     * @throws java.lang.InterruptedException
     */
    public static boolean terminate(
            ExecutorService pool, long timeout, TimeUnit unit) 
            throws InterruptedException {
        
        return terminate(pool, timeout, unit, null);
    }
    
    /**
     * <b>Note:</b> This method was culled from the ExecutorService documentation
     * @return true if the ExecutorService terminated correctly, false otherwise
     * @see java.util.concurrent.ExecutorService
     * @throws java.lang.InterruptedException
     */
    public static boolean terminate(
            ExecutorService pool, long timeout, 
            TimeUnit unit, List<Runnable> addInterruptedHere) 
            throws InterruptedException {
        
        if(timeout <= 0) {
            timeout = 1;
            unit = TimeUnit.NANOSECONDS;
        }

        try {
             
            // Wait a while for existing tasks to terminate
            if ( ! pool.awaitTermination(timeout, unit)) {
                 
                List interrupted = pool.shutdownNow(); // Cancel currently executing tasks
                
                if(addInterruptedHere != null && 
                        (interrupted != null && !interrupted.isEmpty())) {
                    addInterruptedHere.addAll(interrupted);
                }
               
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(1000, TimeUnit.MILLISECONDS)) { 
                    return false;
                }    
             }
        } catch (InterruptedException ie) {
            
            // (Re-)Cancel if current thread also interrupted
            List interrupted = pool.shutdownNow();
            
            if(addInterruptedHere != null && 
                    (interrupted != null && !interrupted.isEmpty())) {
                addInterruptedHere.addAll(interrupted);
            }
            
            // Preserve interrupt status
            Thread.currentThread().interrupt();
            
            throw ie;
        }
         
        return true;
    }
}
