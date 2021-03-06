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
package gov.nih.nci.cagrid.data;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.cql2.validation.walker.Cql2Walker;
import gov.nih.nci.cagrid.data.cql2.validation.walker.Cql2WalkerStructureValidationHandler;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.cagrid.cql.utilities.CQL2SerializationUtil;
import org.cagrid.cql2.CQLQuery;

public class Cql2StructureValidatorTestCase extends TestCase {
    
    public static final String TEST_DOCS_LOCATION = "ext" + File.separator + "dependencies" + File.separator + "test" + File.separator + "docs";
    public static final String CQL2_EXAMPLES_LOCATION = TEST_DOCS_LOCATION + File.separator + "cql2Examples";
    
    private Cql2Walker validator = null;
    
    public Cql2StructureValidatorTestCase(String name) {
        super(name);
    }
    
    
    public void setUp() {
        validator = new Cql2Walker();
        validator.addListener(new Cql2WalkerStructureValidationHandler());
    }
    
    
    public void testExampleQueries() {
        File docsDir = new File(CQL2_EXAMPLES_LOCATION);
        File[] queryDocs = docsDir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isFile() 
                    && pathname.getName().toLowerCase().endsWith(".xml");   
                }
            });
        Comparator<File> fileSorter = new Comparator<File>() {
            public int compare(File f1, File f2) {
                return f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase());
            }
        };
        Arrays.sort(queryDocs, fileSorter);
        for (File queryDocument : queryDocs) {
            try {
                String text = Utils.fileToStringBuffer(queryDocument).toString();
                CQLQuery query = CQL2SerializationUtil.deserializeCql2Query(text);
                validator.walkCql(query);
            } catch (Exception ex) {
                ex.printStackTrace();
                fail("Error validating CQL 2 query (" + queryDocument.getName() + "): " + ex.getMessage());
            }
        }
    }
    

    public static void main(String[] args) {
        TestRunner runner = new TestRunner();
        TestResult result = runner.doRun(new TestSuite(Cql2StructureValidatorTestCase.class));
        System.exit(result.errorCount() + result.failureCount());
    }

}
