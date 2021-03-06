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
package gov.nih.nci.cagrid.wsenum;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.wsenum.utils.ConcurrenPersistantObjectEnumIterator;

import java.io.StringWriter;

import javax.xml.soap.SOAPElement;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.globus.ws.enumeration.IterationConstraints;
import org.globus.ws.enumeration.IterationResult;
import org.projectmobius.bookstore.Book;

/** 
 *  ConcurrentEnumIterTestCase
 *  Test case to test the persistent (complicated) enumeration iterator
 *  which uses java.util.concurrent
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Nov 3, 2006 
 * @version $Id: ConcurrentEnumIterTestCase.java,v 1.7 2008-11-13 16:03:16 dervin Exp $ 
 */
public class ConcurrentEnumIterTestCase extends CompleteEnumIteratorBaseTest {
	
	public ConcurrentEnumIterTestCase() {
		super(ConcurrenPersistantObjectEnumIterator.class.getName());
	}
	
    
    public void testCharLimitExceded2() {
        // ask for all the results, but only enough chars for the first element
        int charCount = -1;
        StringWriter writer = new StringWriter();
        try {
            Utils.serializeObject(getObjectList().get(0), 
                TestingConstants.BOOK_QNAME, writer);
            // trim() because serializeObject() appends a newline
            String text = writer.getBuffer().toString().trim();
            charCount = (text.length() * 2) - 1;
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error determining object char count: " + ex.getMessage());
        }
        IterationConstraints cons = new IterationConstraints(
            getObjectList().size(), charCount, null);
        IterationResult result = getEnumIterator().next(cons);
        SOAPElement[] rawResults = result.getItems();
        assertTrue("Enumeration did not return results", rawResults != null);
        assertFalse("Enumeration returned all results", 
            rawResults.length == getObjectList().size());
        assertEquals("Unexpected number of results returned", 1, rawResults.length);
        // verify content
        Book original = null;
        Book returned = null;
        try {
            original = (Book) deserializeDocumentString(
                writer.getBuffer().toString(), Book.class);
            returned = (Book) deserializeDocumentString(
                rawResults[0].toString(), Book.class);
        } catch (Exception ex) {
            fail("Error deserializing objects: " + ex.getMessage());
        }
        boolean equal = original.getAuthor().equals(returned.getAuthor()) 
            && original.getISBN().equals(returned.getISBN());
        assertTrue("Expected book and returned book do not match", equal);
        
        // ask for results again, should get the next object
        writer = new StringWriter();
        try {
            Utils.serializeObject(getObjectList().get(1), 
                TestingConstants.BOOK_QNAME, writer);
            // trim() because serializeObject() appends a newline
            String text = writer.getBuffer().toString().trim();
            charCount = (text.length() * 2) - 1;
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error determining object char count: " + ex.getMessage());
        }
        cons = new IterationConstraints(getObjectList().size(), charCount, null);
        result = getEnumIterator().next(cons);
        rawResults = result.getItems();
        assertTrue("Enumeration did not return results", rawResults != null);
        assertFalse("Enumeration returned all results", 
            rawResults.length == getObjectList().size());
        assertEquals("Unexpected number of results returned", 1, rawResults.length);
        // verify content
        try {
            original = (Book) deserializeDocumentString(
                writer.getBuffer().toString(), Book.class);
            returned = (Book) deserializeDocumentString(
                rawResults[0].toString(), Book.class);
        } catch (Exception ex) {
            fail("Error deserializing objects: " + ex.getMessage());
        }
        equal = original.getAuthor().equals(returned.getAuthor()) 
            && original.getISBN().equals(returned.getISBN());
        assertTrue("Expected book and returned book do not match", equal);
    }
    
    
    public static void main(String[] args) {
        TestRunner runner = new TestRunner();
        TestResult result = runner.doRun(new TestSuite(ConcurrentEnumIterTestCase.class));
        System.exit(result.errorCount() + result.failureCount());
    }
}
