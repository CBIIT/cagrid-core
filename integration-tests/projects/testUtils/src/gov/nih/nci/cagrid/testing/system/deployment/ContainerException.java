/**
*============================================================================
*  Copyright The Ohio State University Research Foundation, The University of Chicago - 
*	Argonne National Laboratory, Emory University, SemanticBits LLC, and 
*	Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.testing.system.deployment;


/** 
 *  ContainerException
 *  Exception to be thrown when operations on a web service container fail
 * 
 * @author David Ervin
 * 
 * @created Oct 12, 2007 10:06:26 AM
 * @version $Id: ContainerException.java,v 1.1 2008-05-14 17:17:42 hastings Exp $ 
 */
public class ContainerException extends Exception {

    public ContainerException(Exception cause) {
        super(cause);
    }
    
    
    public ContainerException(String message) {
        super(message);
    }
    
    
    public ContainerException(String message, Exception cause) {
        super(message, cause);
    }
}
