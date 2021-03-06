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
package org.cagrid.data.test.system;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.XMLUtilities;
import gov.nih.nci.cagrid.data.MetadataConstants;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;
import gov.nih.nci.cagrid.metadata.ResourcePropertyHelper;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.namespace.QName;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.cagrid.cql2.extensionsupport.SupportedExtensions;
import org.cagrid.dataservice.cql.support.Cql2SupportType;
import org.cagrid.dataservice.cql.support.QueryLanguageSupport;
import org.cagrid.dataservice.cql.support.QueryLanguageSupportCQL2Support;
import org.globus.wsrf.utils.XmlUtils;
import org.w3c.dom.Element;

public class CheckCql2QueryLanguageSupportResourcePropertyStep extends Step {
    
    private ServiceContainer container = null;
    private TestCaseInfo dataServiceInfo = null;
    
    private boolean expectedCql2Support = false;
    private SupportedExtensions expectedSupportedExtensions = null;

    public CheckCql2QueryLanguageSupportResourcePropertyStep(ServiceContainer container, TestCaseInfo dataServiceInfo) {
        this(container, dataServiceInfo, false, null);
    }
    
    
    public CheckCql2QueryLanguageSupportResourcePropertyStep(ServiceContainer container, TestCaseInfo dataServiceInfo,
        boolean expectedCql2Support, SupportedExtensions expectedSupportedExtensions) {
        this.container = container;
        this.dataServiceInfo = dataServiceInfo;
        this.expectedCql2Support = expectedCql2Support;
        this.expectedSupportedExtensions = expectedSupportedExtensions;
    }


    public void runStep() throws Throwable {
        QueryLanguageSupport languageSupport = getQueryLanguageSupportResourceProperty();
        // ensure there is some mention of CQL 2 support
        QueryLanguageSupportCQL2Support cql2Support = languageSupport.getCQL2Support();
        Cql2SupportType notSupported = languageSupport.getCQL2NotSupported();
        assertTrue("No CQL 2 support was defined in the resource property", 
            !(cql2Support == null && notSupported == null));
        assertTrue("CQL 2 support metadata inconsistent: claims both supported AND not supported", 
            !(cql2Support != null && notSupported != null));
        if (expectedCql2Support) {
            SupportedExtensions supportedExtensions = cql2Support.getSupportedExtensions();
            compareExtensions(supportedExtensions);
        } else {
            assertEquals("CQL 2 should not be supported by " + dataServiceInfo.getName() + 
                " and the resource property should explicitly state this", 
                Cql2SupportType.ImplementationNotProvided, notSupported);
        }
    }
    
    
    private void compareExtensions(SupportedExtensions found) {
        if (found != null && expectedSupportedExtensions == null) {
            fail("Found supported extensions data on CQL 2 support resource property, but expected none");
        }
        if (found == null && expectedSupportedExtensions != null) {
            fail("Did not find supported extensions data on CQL 2 support resource property, but data was expected");
        }
        testExtensionsMatch(expectedSupportedExtensions.getAttributeExtension(), found.getAttributeExtension(), "attribute");
        testExtensionsMatch(expectedSupportedExtensions.getModifierExtension(), found.getModifierExtension(), "modifier");
        testExtensionsMatch(expectedSupportedExtensions.getObjectExtension(), found.getObjectExtension(), "object");
        testExtensionsMatch(expectedSupportedExtensions.getResultExtension(), found.getResultExtension(), "results");
    }
    
    
    private void testExtensionsMatch(QName[] expected, QName[] found, String type) {
        Comparator<QName> qnameSorter = new Comparator<QName>() {
            public int compare(QName o1, QName o2) {
                return o1.toString().compareTo(o2.toString());
            }
        };
        Set<QName> expectedSet = new TreeSet<QName>(qnameSorter);
        Set<QName> foundSet = new TreeSet<QName>(qnameSorter);
        if (expected != null) {
            Collections.addAll(expectedSet, expected);
        }
        if (found != null) {
            Collections.addAll(foundSet, found);
        }
        StringBuffer expectedString = new StringBuffer();
        for (Iterator<QName> iter = expectedSet.iterator(); iter.hasNext(); ) {
            expectedString.append(iter.next().toString());
            if (iter.hasNext()) {
                expectedString.append(", ");
            }
        }
        StringBuffer foundString = new StringBuffer();
        for (Iterator<QName> iter = foundSet.iterator(); iter.hasNext(); ) {
            foundString.append(iter.next().toString());
            if (iter.hasNext()) {
                foundString.append(", ");
            }
        }
        assertEquals("CQL 2 " + type + " extensions in metadata did not match expected", expectedString.toString(), foundString.toString());
    }
    
    
    private QueryLanguageSupport getQueryLanguageSupportResourceProperty() {
        EndpointReferenceType epr = null;
        try {
            epr = container.getServiceEPR("cagrid/" + dataServiceInfo.getName());
        } catch (MalformedURIException ex) {
            ex.printStackTrace();
            fail("Error obtaining service EPR: " + ex.getMessage());
        }
        Element resourceProperty = null;
        try {
            resourceProperty = ResourcePropertyHelper.getResourceProperty(
                epr, MetadataConstants.QUERY_LANGUAGE_SUPPORT_QNAME);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error reading resource property: " + ex.getMessage());
        }
        QueryLanguageSupport support = null;
        try {
            resourceProperty = ResourcePropertyHelper.getResourceProperty(
                epr, MetadataConstants.QUERY_LANGUAGE_SUPPORT_QNAME);
            assertNotNull("CQL 2 query support resource property was null", resourceProperty);
            // deserialize the resource property
            String propertyAsString = XmlUtils.toString(resourceProperty);
            System.out.println(XMLUtilities.formatXML(propertyAsString));
            InputStream wsddStream = getClientConfigWsdd();
            support = Utils.deserializeObject(
                new StringReader(propertyAsString), 
                QueryLanguageSupport.class, wsddStream);
            wsddStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error deserializing query language support document: " + ex.getMessage());
        }
        return support;
    }
    
    
    private InputStream getClientConfigWsdd() throws FileNotFoundException {
        File clientConfig = new File(dataServiceInfo.getDir() 
            + File.separator + "src" 
            + File.separator + dataServiceInfo.getPackageDir() 
            + File.separator + "client" + File.separator + "client-config.wsdd");
        assertTrue("Client config " + clientConfig.getAbsolutePath() 
            + " does not exist", clientConfig.exists());
        return new FileInputStream(clientConfig);
    }
}
