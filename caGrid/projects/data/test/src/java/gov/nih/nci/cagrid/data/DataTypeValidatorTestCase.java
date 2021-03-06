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

import gov.nih.nci.cagrid.data.cql.validation.DataTypeValidator;
import gov.nih.nci.cagrid.data.cql.validation.DomainConformanceException;

import java.math.BigDecimal;
import java.util.Date;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class DataTypeValidatorTestCase extends TestCase {
    
    public DataTypeValidatorTestCase(String name) {
        super(name);
    }
    
    
    private void checkValid(String value, String datatype) {
        try {
            DataTypeValidator.validate(value, datatype);
        } catch (DomainConformanceException ex) {
            ex.printStackTrace();
            fail("Value " + value + " did not validate as " + datatype + ": " + ex.getMessage());
        }
    }
    
    
    private void checkInvalid(String value, String datatype) {
        try {
            DataTypeValidator.validate(value, datatype);
            fail("Value " + value + " should not have passed validation as " + datatype);
        } catch (DomainConformanceException ex) {
            // expected
        } catch (Exception ex) {
            // not expected
            ex.printStackTrace();
            fail("Unexpected exception caught: " + ex.getMessage());
        }
    }
    
    
    public void testValidateString() {
        checkValid("hello", String.class.getName());
    }
    
    
    public void testValidateInteger() {
        String goodVal = String.valueOf(Integer.MAX_VALUE - 1);
        String badVal1 = goodVal + "00"; // overflow
        String badVal2 = "abcd";
        checkValid(goodVal, Integer.class.getName());
        checkInvalid(badVal1, Integer.class.getName());
        checkInvalid(badVal2, Integer.class.getName());
    }
    
    
    public void testValidateLong() {
        String goodVal = String.valueOf(Long.MAX_VALUE - 1);
        String badVal1 = goodVal + "00"; // overflow
        String badVal2 = "abc";
        checkValid(goodVal, Long.class.getName());
        checkInvalid(badVal1, Long.class.getName());
        checkInvalid(badVal2, Long.class.getName());
    }
    
    
    public void testValidateDate() {
        String[] good = new String[] {
            "1/2/34 1:23 AM",
            "01:23:45",
            "21:21:21",
            "2005-09-03",
            "2009-02-27T23:45:12"
        };
        String[] bad = new String[] {
            "15/15/34 1:23 AM",
            "10/33/34 1:23 AM",
            "1/2/1234 1:23",
            "1/2/1234 31:23 AM",
            "1/2/1234",
            "24:23",
            "21:65",
            "09-03-2005",
            "9-3-2005"
        };
        for (String g : good) {
            checkValid(g, Date.class.getName());
        }
        for (String b : bad) {
            checkInvalid(b, Date.class.getName());
        }
    }
    
    
    public void testValidateBoolean() {
        // the way Boolean.valueOf() works means anything not null is "valid"
        String[] good = new String[] {
            "true", "True", "TRUE", "TrUe",
            "false", "False", "FALSE", "FaLsE"
        };
        for (String s : good) {
            checkValid(s, Boolean.class.getName());
        }
    }
    
    
    public void testValidateCharacter() {
        String[] good = new String[] {
            "1", "a", "A", "@"
        };
        String[] bad = new String[] {
            "!@#", "hello", "123"
        };
        for (String s : good) {
            checkValid(s, Character.class.getName());
        }
        for (String s : bad) {
            checkInvalid(s, Character.class.getName());
        }
    }
    
    
    public void testValidateDouble() {
        String goodVal = BigDecimal.valueOf(Double.MAX_VALUE).toPlainString();
        // String badVal1 = goodVal + "00"; // overflow -> "Infinity", not a failure
        String badVal2 = "abcd";
        checkValid(goodVal, Double.class.getName());
        // checkInvalid(badVal1, Double.class.getName());
        checkInvalid(badVal2, Double.class.getName());
    }
    
    
    public void testValidateFloat() {
        String goodVal = BigDecimal.valueOf(Float.MAX_VALUE).toPlainString();
        // String badVal1 = goodVal + "00"; // overflow -> "Infinity", not a failure
        String badVal2 = "abcd";
        checkValid(goodVal, Float.class.getName());
        // checkInvalid(badVal1, Float.class.getName());
        checkInvalid(badVal2, Float.class.getName());
    }
    

    public static void main(String[] args) {
        TestRunner runner = new TestRunner();
        TestResult result = runner.doRun(new TestSuite(DataTypeValidatorTestCase.class));
        System.exit(result.errorCount() + result.failureCount());
    }

}
