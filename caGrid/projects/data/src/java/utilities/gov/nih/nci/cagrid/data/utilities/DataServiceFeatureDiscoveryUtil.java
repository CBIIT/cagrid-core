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
package gov.nih.nci.cagrid.data.utilities;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.EnumerationMethodConstants;
import gov.nih.nci.cagrid.data.MetadataConstants;
import gov.nih.nci.cagrid.data.TransferMethodConstants;
import gov.nih.nci.cagrid.data.utilities.validation.WSDLUtils;
import gov.nih.nci.cagrid.metadata.ResourcePropertyHelper;
import gov.nih.nci.cagrid.metadata.exceptions.InvalidResourcePropertyException;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;

import javax.wsdl.Definition;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.xml.namespace.QName;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.dataservice.cql.support.QueryLanguageSupport;
import org.cagrid.dataservice.metadata.instancecount.DataServiceInstanceCounts;
import org.cagrid.dataservice.metadata.instancecount.InstanceCount;
import org.globus.wsrf.utils.XmlUtils;
import org.w3c.dom.Element;

/**
 * DataServiceFeatureDiscoveryUtil
 * Discover what features a data service supports
 * 
 * @author David
 *
 */
public class DataServiceFeatureDiscoveryUtil {
    
    private static Log LOG = LogFactory.getLog(DataServiceFeatureDiscoveryUtil.class);

    private DataServiceFeatureDiscoveryUtil() {
        
    }
    
    
    public static DataServiceInstanceCounts getInstanceCounts(EndpointReferenceType epr) throws Exception {
        DataServiceInstanceCounts counts = null;
        Element resourceProperty = null;
        try {
            resourceProperty = ResourcePropertyHelper.getResourceProperty(epr, 
                MetadataConstants.DATA_INSTANCE_QNAME);
            if (resourceProperty != null) {
                counts = Utils.deserializeObject(
                    new StringReader(XmlUtils.toString(resourceProperty)), DataServiceInstanceCounts.class);
            }
        } catch (InvalidResourcePropertyException ex) {
            // expected for caGrid 1.4 and earlier data services
            if (LOG.isTraceEnabled()) {
                LOG.trace("Resource property not found - service "
                    + epr.getAddress().toString() + " is probably older than caGrid 1.4");
                LOG.trace(ex);
            }
        }
        return counts;
    }
    
    
    public static long getDataInstanceCount(EndpointReferenceType epr, String classname) throws Exception {
        long count = -1;
        DataServiceInstanceCounts counts = getInstanceCounts(epr);
        if (counts == null) {
            throw new Exception("Could not obtain count information from the service "
                + epr.getAddress().toString());
        }
        InstanceCount[] countArray = counts.getInstanceCount();
        if (countArray != null) {
            for (InstanceCount ic : countArray) {
                if (ic.getDomainClassName().equals(classname)) {
                    count = ic.getInstances();
                    break;
                }
            }
        }
        return count;
    }
    
    
    public static boolean serviceSupportsCql2(EndpointReferenceType epr) throws Exception {
        Element resourceProperty = null;
        try {
            resourceProperty = ResourcePropertyHelper.getResourceProperty(
                epr, MetadataConstants.QUERY_LANGUAGE_SUPPORT_QNAME);
            if (resourceProperty != null) {
                // deserialize the resource property
                QueryLanguageSupport support = Utils.deserializeObject(
                    new StringReader(XmlUtils.toString(resourceProperty)), QueryLanguageSupport.class);
                return support.getCQL2NotSupported() == null && support.getCQL2Support() != null;
            }
        } catch (InvalidResourcePropertyException ex) {
            // expected for caGrid 1.3 and earlier data services
            if (LOG.isTraceEnabled()) {
                LOG.trace("Resource property not found - assuming no CQL 2 support for " 
                    + epr.getAddress().toString());
                LOG.trace(ex);
            }
        }
        return false;
    }
    
    
    public static Operation getOperation(EndpointReferenceType epr,
        QName inputMessage, QName outputMessage, String operationName) throws WSDLException {
        String wsdlLocation = WSDLUtils.getWSDLLocation(epr);
        LOG.debug("Loading WSDL from " + wsdlLocation);
        Definition wsdlDef = WSDLUtils.parseServiceWSDL(wsdlLocation);
        Operation foundOperation = null;
        Iterator<?> serviceIter = wsdlDef.getServices().values().iterator();
        while (serviceIter.hasNext() && foundOperation == null) {
            Service service = (Service) serviceIter.next();
            Iterator<?> portIter = service.getPorts().values().iterator();
            while (portIter.hasNext() && foundOperation == null) {
                Port port = (Port) portIter.next();
                PortType portType = port.getBinding().getPortType();
                Iterator<?> opIter = portType.getOperations().iterator();
                while (opIter.hasNext() && foundOperation == null) {
                    Operation op = (Operation) opIter.next();
                    if (op.getName().equals(operationName)) {
                        LOG.debug("Found operation " + operationName + ", checking input / output messages");
                        Message input = op.getInput().getMessage();
                        Message output = op.getOutput().getMessage();
                        if (input.getQName().equals(inputMessage) &&
                            output.getQName().equals(outputMessage)) {
                            foundOperation = op;
                            LOG.debug("Input / output messages match");
                            break;
                        }
                    }
                }
            }
        }
        return foundOperation;
    }
    
    
    public static boolean serviceHasCql2TransferOperation(EndpointReferenceType epr) throws WSDLException {
        Operation transferOp = getOperation(epr, 
            TransferMethodConstants.CQL2_TRANSFER_QUERY_METHOD_INPUT_MESSAGE,
            TransferMethodConstants.CQL2_TRANSFER_QUERY_METHOD_OUTPUT_MESSAGE,
            TransferMethodConstants.CQL2_TRANSFER_QUERY_METHOD_NAME);
        return transferOp != null;
    }
    
    
    public static boolean serviceHasCql1TransferOperation(EndpointReferenceType epr) throws WSDLException {
        Operation transferOp = getOperation(epr,
            TransferMethodConstants.TRANSFER_QUERY_METHOD_INPUT_MESSAGE,
            TransferMethodConstants.TRANSFER_QUERY_METHOD_OUTPUT_MESSAGE, 
            TransferMethodConstants.TRANSFER_QUERY_METHOD_NAME);
        return transferOp != null;
    }
    
    
    public static boolean serviceHasCql2EnumerationOperation(EndpointReferenceType epr) throws WSDLException {
        Operation enumerationOp = getOperation(epr, 
            EnumerationMethodConstants.CQL2_ENUMERATION_QUERY_INPUT_MESSAGE,
            EnumerationMethodConstants.CQL2_ENUMERATION_QUERY_OUTPUT_MESSAGE,
            EnumerationMethodConstants.CQL2_ENUMERATION_QUERY_METHOD_NAME);
        return enumerationOp != null;
    }
    
    
    public static boolean serviceHasCql1EnumerationOperation(EndpointReferenceType epr) throws WSDLException {
        Operation enumerationOp = getOperation(epr, 
            EnumerationMethodConstants.ENUMERATION_QUERY_INPUT_MESSAGE,
            EnumerationMethodConstants.ENUMERATION_QUERY_OUTPUT_MESSAGE,
            EnumerationMethodConstants.ENUMERATION_QUERY_METHOD_NAME);
        return enumerationOp != null;
    }
    
    
    public static void main(String[] args) {
        try {
            EndpointReferenceType epr = new EndpointReferenceType(new Address(
                "http://localhost:8080/wsrf/services/cagrid/HelloWorld"));
            System.out.println("Has cql2? " + serviceSupportsCql2(epr));
            System.out.println("Has enum? " + serviceHasCql2EnumerationOperation(epr));
            String classname = "my.awsome.classes.name2";
            System.out.println("Instance counts: " + getDataInstanceCount(epr, classname));
            DataServiceInstanceCounts counts = getInstanceCounts(epr);
            StringWriter writer = new StringWriter();
            Utils.serializeObject(counts, MetadataConstants.DATA_INSTANCE_QNAME, writer);
            System.out.println(writer.getBuffer().toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
