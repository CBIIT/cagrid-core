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
package gov.nih.nci.cagrid.data.creation;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.CqlSchemaConstants;
import gov.nih.nci.cagrid.data.EnumerationMethodConstants;
import gov.nih.nci.cagrid.data.QueryMethodConstants;
import gov.nih.nci.cagrid.data.enumeration.service.globus.Cql2EnumerationDataServiceProviderImpl;
import gov.nih.nci.cagrid.data.enumeration.service.globus.EnumerationDataServiceProviderImpl;
import gov.nih.nci.cagrid.data.enumeration.stubs.EnumerationDataServicePortType;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptions;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptionsException;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeImportInformation;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeProviderInformation;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionException;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.wsenum.common.WsEnumConstants;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.xml.namespace.QName;

import org.cagrid.dataservice.enumeration.stubs.Cql2EnumerationDataServicePortType;


/**
 * WsEnumerationFeatureCreator 
 * Adds the components needed for the WS-Enumeration
 * feature of data services
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Aug 22, 2006
 * @version $Id: WsEnumerationFeatureCreator.java,v 1.4 2009-02-26 17:06:10 dervin Exp $
 */
public class WsEnumerationFeatureCreator extends FeatureCreator {
	public static final String WS_ENUM_EXTENSION_NAME = "cagrid_wsEnum";

	public WsEnumerationFeatureCreator(ServiceInformation info, ServiceType mainService) {
		super(info, mainService);
	}


	public void addFeature() throws CreationExtensionException {
	    if (!featureAlreadyCreated()) {
	        installWsEnumExtension();
	        copySchemas();
	        addEnumerationQueryMethods();
        }
	}


	private void installWsEnumExtension() throws CreationExtensionException {
		// verify the ws-enum extension is installed
		if (!wsEnumExtensionInstalled()) {
			throw new CreationExtensionException("The required extension " + WS_ENUM_EXTENSION_NAME
				+ " was not found to be installed.  Please install it and try creating your service again");
		}

		if (!wsEnumExtensionUsed()) {
			System.out.println("Adding the WS-Enumeration extension to the service");
			// add the ws Enumeration extension to the service model
            ExtensionTools.addExtensionToService(getServiceInformation(), WS_ENUM_EXTENSION_NAME);
            // edit the introduce.properties file to add the enum extension to the list
            Properties introduceProps = new Properties();
            File propsFile = new File(getServiceInformation().getBaseDirectory(), IntroduceConstants.INTRODUCE_PROPERTIES_FILE);
            try {
                FileInputStream fis = new FileInputStream(propsFile);
                introduceProps.load(fis);
                fis.close();
                String extensionsProperty = introduceProps.getProperty(IntroduceConstants.INTRODUCE_SKELETON_EXTENSIONS);
                extensionsProperty += "," + WS_ENUM_EXTENSION_NAME;
                introduceProps.setProperty(IntroduceConstants.INTRODUCE_SKELETON_EXTENSIONS, extensionsProperty);
                FileOutputStream fos = new FileOutputStream(propsFile);
                introduceProps.store(fos, "Edited by " + WsEnumerationFeatureCreator.class.getName());
                fos.close();
            } catch (IOException ex) {
                throw new CreationExtensionException("Error editing " 
                    + propsFile.getAbsolutePath() + " : " + ex.getMessage(), ex);
            }
		}
	}


	private void addEnumerationQueryMethods() {
		CommonTools.addMethod(getMainService(), getCql1EnumerationQueryMethod());
		CommonTools.addMethod(getMainService(), getCql2EnumerationQueryMethod());
	}
	
	
	public static MethodType getCql1EnumerationQueryMethod() {
	    // the enumerationQuery method
        MethodType enumerateMethod = new MethodType();
        enumerateMethod.setName(EnumerationMethodConstants.ENUMERATION_QUERY_METHOD_NAME);
        enumerateMethod.setDescription(EnumerationMethodConstants.ENUMERATION_QUERY_METHOD_DESCRIPTION);
        enumerateMethod.setIsImported(true);
        enumerateMethod.setIsProvided(true);
        // input
        MethodTypeInputs enumInputs = new MethodTypeInputs();
        MethodTypeInputsInput queryParam = new MethodTypeInputsInput();
        queryParam.setName(QueryMethodConstants.QUERY_METHOD_PARAMETER_NAME);
        queryParam.setIsArray(false);
        queryParam.setQName(CqlSchemaConstants.CQL_QUERY_QNAME);
        queryParam.setDescription(QueryMethodConstants.QUERY_METHOD_PARAMETER_DESCRIPTION);
        enumInputs.setInput(new MethodTypeInputsInput[]{queryParam});
        enumerateMethod.setInputs(enumInputs);
        // output
        MethodTypeOutput enumOutput = new MethodTypeOutput();
        enumOutput.setIsArray(false);
        enumOutput.setQName(EnumerationMethodConstants.ENUMERATION_QUERY_METHOD_OUTPUT_TYPE);
        enumOutput.setDescription(EnumerationMethodConstants.ENUMERATION_QUERY_METHOD_OUTPUT_DESCRIPTION);
        enumerateMethod.setOutput(enumOutput);
        // import info
        MethodTypeImportInformation enumImport = new MethodTypeImportInformation();
        enumImport.setPortTypeName(EnumerationDataServicePortType.class.getSimpleName());
        enumImport.setWsdlFile("EnumerationDataService.wsdl");
        enumImport.setInputMessage(new QName(EnumerationMethodConstants.ENUMERATION_DATA_SERVICE_NAMESPACE, "EnumerationQueryRequest"));
        enumImport.setOutputMessage(new QName(EnumerationMethodConstants.ENUMERATION_DATA_SERVICE_NAMESPACE, "EnumerationQueryResponse"));
        enumImport.setNamespace(EnumerationMethodConstants.ENUMERATION_DATA_SERVICE_NAMESPACE);
        enumImport.setPackageName(EnumerationMethodConstants.ENUMERATION_DATA_SERVICE_PACKAGE);
        enumerateMethod.setImportInformation(enumImport);
        // provider info
        MethodTypeProviderInformation enumProvider = new MethodTypeProviderInformation();
        enumProvider.setProviderClass(EnumerationDataServiceProviderImpl.class.getName());
        enumerateMethod.setProviderInformation(enumProvider);
        // exceptions
        MethodTypeExceptions methodExceptions = new MethodTypeExceptions();
        MethodTypeExceptionsException qpException = new MethodTypeExceptionsException(
            QueryMethodConstants.QUERY_PROCESSING_EXCEPTION_DESCRIPTION,
            QueryMethodConstants.QUERY_PROCESSING_EXCEPTION_NAME, 
            QueryMethodConstants.QUERY_PROCESSING_EXCEPTION_QNAME);
        MethodTypeExceptionsException mqException = new MethodTypeExceptionsException(
            QueryMethodConstants.MALFORMED_QUERY_EXCEPTION_DESCRIPTION,
            QueryMethodConstants.MALFORMED_QUERY_EXCEPTION_NAME, 
            QueryMethodConstants.MALFORMED_QUERY_EXCEPTION_QNAME);
        methodExceptions.setException(new MethodTypeExceptionsException[]{qpException, mqException});
        enumerateMethod.setExceptions(methodExceptions);
        return enumerateMethod;
	}
	
	
	public static MethodType getCql2EnumerationQueryMethod() {
        // the executeEnumerationQuery method
        MethodType enumerateMethod = new MethodType();
        enumerateMethod.setName(EnumerationMethodConstants.CQL2_ENUMERATION_QUERY_METHOD_NAME);
        enumerateMethod.setDescription(EnumerationMethodConstants.CQL2_ENUMERATION_QUERY_METHOD_DESCRIPTION);
        enumerateMethod.setIsImported(true);
        enumerateMethod.setIsProvided(true);
        // input
        MethodTypeInputs enumInputs = new MethodTypeInputs();
        MethodTypeInputsInput queryParam = new MethodTypeInputsInput();
        queryParam.setName(QueryMethodConstants.QUERY_METHOD_PARAMETER_NAME);
        queryParam.setIsArray(false);
        queryParam.setQName(CqlSchemaConstants.CQL2_QUERY_QNAME);
        queryParam.setDescription(QueryMethodConstants.CQL2_QUERY_METHOD_PARAMETER_DESCRIPTION);
        enumInputs.setInput(new MethodTypeInputsInput[]{queryParam});
        enumerateMethod.setInputs(enumInputs);
        // output
        MethodTypeOutput enumOutput = new MethodTypeOutput();
        enumOutput.setIsArray(false);
        enumOutput.setQName(EnumerationMethodConstants.ENUMERATION_QUERY_METHOD_OUTPUT_TYPE);
        enumOutput.setDescription(EnumerationMethodConstants.ENUMERATION_QUERY_METHOD_OUTPUT_DESCRIPTION);
        enumerateMethod.setOutput(enumOutput);
        // import info
        MethodTypeImportInformation enumImport = new MethodTypeImportInformation();
        enumImport.setPortTypeName(Cql2EnumerationDataServicePortType.class.getSimpleName());
        enumImport.setWsdlFile("Cql2EnumerationDataService.wsdl");
        enumImport.setInputMessage(new QName(EnumerationMethodConstants.CQL2_ENUMERATION_DATA_SERVICE_NAMESPACE, "ExecuteEnumerationQueryRequest"));
        enumImport.setOutputMessage(new QName(EnumerationMethodConstants.CQL2_ENUMERATION_DATA_SERVICE_NAMESPACE, "ExecuteEnumerationQueryResponse"));
        enumImport.setNamespace(EnumerationMethodConstants.CQL2_ENUMERATION_DATA_SERVICE_NAMESPACE);
        enumImport.setPackageName(EnumerationMethodConstants.CQL2_ENUMERATION_DATA_SERVICE_PACKAGE);
        enumerateMethod.setImportInformation(enumImport);
        // provider info
        MethodTypeProviderInformation enumProvider = new MethodTypeProviderInformation();
        enumProvider.setProviderClass(Cql2EnumerationDataServiceProviderImpl.class.getName());
        enumerateMethod.setProviderInformation(enumProvider);
        // exceptions
        MethodTypeExceptions methodExceptions = new MethodTypeExceptions();
        MethodTypeExceptionsException qpException = new MethodTypeExceptionsException(
            QueryMethodConstants.QUERY_PROCESSING_EXCEPTION_DESCRIPTION,
            QueryMethodConstants.QUERY_PROCESSING_EXCEPTION_NAME, 
            QueryMethodConstants.QUERY_PROCESSING_EXCEPTION_QNAME);
        MethodTypeExceptionsException mqException = new MethodTypeExceptionsException(
            QueryMethodConstants.MALFORMED_QUERY_EXCEPTION_DESCRIPTION,
            QueryMethodConstants.MALFORMED_QUERY_EXCEPTION_NAME, 
            QueryMethodConstants.MALFORMED_QUERY_EXCEPTION_QNAME);
        methodExceptions.setException(new MethodTypeExceptionsException[]{qpException, mqException});
        enumerateMethod.setExceptions(methodExceptions);
        return enumerateMethod;
    }


	private void copySchemas() throws CreationExtensionException {
		// copy over the EnumerationQuery.wsdl file
		String schemaDir = getServiceSchemaDir();
		File dataExtensionSchemaDir = new File(ExtensionsLoader.getInstance().getExtensionsDir(),
		    "data" + File.separator + "schema" + File.separator + "Data");
		
		File[] enumerationWsdls = dataExtensionSchemaDir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                String name = pathname.toString();
                return name.endsWith("EnumerationDataService.wsdl");
            }
        });
        try {
            for (File wsdl : enumerationWsdls) {
                Utils.copyFile(wsdl, new File(schemaDir, wsdl.getName()));
            }
        } catch (Exception ex) {
			throw new CreationExtensionException("Error copying data service enumeration schemas: " + ex.getMessage(), ex);
		}
	}


	private boolean wsEnumExtensionInstalled() {
		List<?> extensionDescriptors = ExtensionsLoader.getInstance().getServiceExtensions();
		for (int i = 0; i < extensionDescriptors.size(); i++) {
			ServiceExtensionDescriptionType ex = (ServiceExtensionDescriptionType) extensionDescriptors.get(i);
			if (ex.getName().equals(WS_ENUM_EXTENSION_NAME)) {
				return true;
			}
		}
		return false;
	}


	private boolean wsEnumExtensionUsed() {
		ServiceDescription desc = getServiceInformation().getServiceDescriptor();
		if ((desc.getExtensions() != null) && (desc.getExtensions().getExtension() != null)) {
			for (int i = 0; i < desc.getExtensions().getExtension().length; i++) {
				if (desc.getExtensions().getExtension(i).getName().equals(WS_ENUM_EXTENSION_NAME)) {
					return true;
				}
			}
		}
		return false;
	}


	private String getServiceSchemaDir() {
	    return getServiceInformation().getBaseDirectory().getAbsolutePath() + File.separator 
            + "schema" + File.separator + getMainService().getName();
	}
    
    
    private boolean featureAlreadyCreated() {
        // does the service context exist?
        ServiceType service = CommonTools.getService(
            getServiceInformation().getServices(), WsEnumConstants.CAGRID_ENUMERATION_SERVICE_NAME);
        if (service != null) {
            return WsEnumConstants.CAGRID_ENUMERATION_SERVICE_PACKAGE.equals(service.getPackageName())
                && WsEnumConstants.CAGRID_ENUMERATION_SERVICE_NAMESPACE.equals(service.getNamespace());
        }
        return false;
    }
}
