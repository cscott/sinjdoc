// DocErrorReporter.java, created Tue Mar 18 18:18:02 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc;

/**
 * The <code>DocErrorReporter</code> interface provides error, warning, and
 * notice printing.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 * @see com.sun.javadoc.DocErrorReporter
 */
public interface DocErrorReporter {
    /**
     * Print error message and increment error count.
     * @parameter msg the message to print.
     */
    public void printError(String msg);
    /**
     * Print error message and increment error count.
     * @parameter pos the position item where the error occurs.
     * @parameter msg the message to print.
     */
    public void printError(SourcePosition pos, String msg);
    /**
     * Print warning message and increment warning count.
     * @parameter msg the message to print.
     */
    public void printWarning(String msg);
    /**
     * Print warning message and increment warning count.
     * @parameter pos the position item where the warning occurs.
     * @parameter msg the message to print.
     */
    public void printWarning(SourcePosition pos, String msg);
    /**
     * Print a message.
     * @parameter msg the message to print.
     */
    public void printNotice(String msg);
    /**
     * Print a message.
     * @parameter pos the position item where the message occurs.
     * @parameter msg the message to print.
     */
    public void printNotice(SourcePosition pos, String msg);
}
