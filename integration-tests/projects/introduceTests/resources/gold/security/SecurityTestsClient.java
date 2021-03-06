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
package org.cagrid.introduce.security.tests.client;

import java.io.InputStream;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.AxisClient;
import org.apache.axis.client.Stub;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

import org.oasis.wsrf.properties.GetResourcePropertyResponse;

import org.globus.gsi.GlobusCredential;

import org.cagrid.introduce.security.tests.stubs.SecurityTestsPortType;
import org.cagrid.introduce.security.tests.stubs.service.SecurityTestsServiceAddressingLocator;
import org.cagrid.introduce.security.tests.common.SecurityTestsI;

import gov.nih.nci.cagrid.common.security.ProxyUtil;
import gov.nih.nci.cagrid.introduce.security.client.ServiceSecurityClient;


/**
 * This class is autogenerated, DO NOT EDIT GENERATED GRID SERVICE ACCESS
 * METHODS. This client is generated automatically by Introduce to provide a
 * clean unwrapped API to the service. On construction the class instance will
 * contact the remote service and retrieve it's security metadata description
 * which it will use to configure the Stub specifically for each method call.
 * 
 * @created by Introduce Toolkit version 1.3
 */
public class SecurityTestsClient extends SecurityTestsClientBase implements SecurityTestsI {

    public SecurityTestsClient(String url) throws MalformedURIException, RemoteException {
        this(url, null);
    }


    public SecurityTestsClient(String url, GlobusCredential proxy) throws MalformedURIException, RemoteException {
        super(url, proxy);
    }


    public SecurityTestsClient(EndpointReferenceType epr) throws MalformedURIException, RemoteException {
        this(epr, null);
    }


    public SecurityTestsClient(EndpointReferenceType epr, GlobusCredential proxy) throws MalformedURIException,
        RemoteException {
        super(epr, proxy);
    }


    public static void usage() {
        System.out.println(SecurityTestsClient.class.getName() + " -url <service url>");
    }


    public static void main(String[] args) {
        System.out.println("Running the Grid Service Client");
        try {
            if (!(args.length < 2)) {
                if (args[0].equals("-url")) {
                    org.globus.common.CoGProperties properties = org.globus.common.CoGProperties.getDefault();
                    properties.setCaCertLocations(".");
                    org.globus.common.CoGProperties.setDefault(properties);

                    org.globus.gsi.GlobusCredential creds = null;
                    try {
                        creds = gov.nih.nci.cagrid.common.security.ProxyUtil.loadProxy("user.proxy");
                        System.out.println("Using proxy with id= " + creds.getIdentity() + " and lifetime "
                            + creds.getTimeLeft());
                    } catch (Exception e1) {
                        System.out.println("No proxy file loaded so running with no credentials");
                    }

                    // GlobusCredential creds =
                    // GlobusCredential.getDefaultCredential();

                    String credsString = creds.getIdentity();

                    SecurityTestsClient credsclient = new SecurityTestsClient(args[1], creds);
                    SecurityTestsClient nocredsclient = new SecurityTestsClient(args[1]);
                    String result = null;

                    result = credsclient.anonPrefered();
                    if (result!=null) {
                        throw new Exception("Authorization configuration error");
                    }

                    credsclient.setAnonymousPrefered(false);
                    result = credsclient.anonPrefered();
                    if (!result.equals(credsString)) {
                        throw new Exception("Authorization configuration error");
                    }

                    credsclient.setAnonymousPrefered(true);
                    result = credsclient.anonPrefered();
                    if (result!=null) {
                        throw new Exception("Authorization configuration error");
                    }

                    result = credsclient.anonNotPrefered();
                    if (!result.equals(credsString)) {
                        throw new Exception("Authorization configuration error");
                    }

                    credsclient.setAnonymousPrefered(false);
                    result = credsclient.anonNotPrefered();
                    if (!result.equals(credsString)) {
                        throw new Exception("Authorization configuration error");
                    }

                    credsclient.setAnonymousPrefered(true);
                    result = credsclient.anonNotPrefered();
                    if (!result.equals(credsString)) {
                        throw new Exception("Authorization configuration error");
                    }

                    result = nocredsclient.anonPrefered();
                    if (result!=null) {
                        throw new Exception("Authorization configuration error");
                    }

                    boolean failed = false;
                    try {
                        nocredsclient.setAnonymousPrefered(false);
                        result = nocredsclient.anonPrefered();
                    } catch (Exception e) {
                        failed = true;
                    }
                    if (!failed) {
                        throw new Exception("Authorization configuration error");
                    }

                    nocredsclient.setAnonymousPrefered(true);
                    result = nocredsclient.anonPrefered();
                    if (result!=null) {
                        throw new Exception("Authorization configuration error");
                    }

                    failed = false;
                    try {
                        result = nocredsclient.anonNotPrefered();
                    } catch (Exception e) {
                        failed = true;
                    }
                    if (!failed) {
                        throw new Exception("Authorization configuration error");
                    }

                    failed = false;
                    try {
                        nocredsclient.setAnonymousPrefered(false);
                        result = nocredsclient.anonNotPrefered();
                    } catch (Exception e) {
                        failed = true;
                    }
                    if (!failed) {
                        throw new Exception("Authorization configuration error");
                    }

                    failed = false;
                    try {
                        nocredsclient.setAnonymousPrefered(true);
                        result = nocredsclient.anonNotPrefered();
                    } catch (Exception e) {
                        failed = true;
                    }
                    if (!failed) {
                        throw new Exception("Authorization configuration error");
                    }

                } else {
                    usage();
                    System.exit(1);
                }
            } else {
                usage();
                System.exit(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


    public org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse getMultipleResourceProperties(
        org.oasis.wsrf.properties.GetMultipleResourceProperties_Element params) throws RemoteException {
        synchronized (portTypeMutex) {
            configureStubSecurity((Stub) portType, "getMultipleResourceProperties");
            return portType.getMultipleResourceProperties(params);
        }
    }


    public org.oasis.wsrf.properties.GetResourcePropertyResponse getResourceProperty(javax.xml.namespace.QName params)
        throws RemoteException {
        synchronized (portTypeMutex) {
            configureStubSecurity((Stub) portType, "getResourceProperty");
            return portType.getResourceProperty(params);
        }
    }


    public org.oasis.wsrf.properties.QueryResourcePropertiesResponse queryResourceProperties(
        org.oasis.wsrf.properties.QueryResourceProperties_Element params) throws RemoteException {
        synchronized (portTypeMutex) {
            configureStubSecurity((Stub) portType, "queryResourceProperties");
            return portType.queryResourceProperties(params);
        }
    }


    public java.lang.String anonPrefered() throws RemoteException {
        synchronized (portTypeMutex) {
            configureStubSecurity((Stub) portType, "anonPrefered");
            org.cagrid.introduce.security.tests.stubs.AnonPreferedRequest params = new org.cagrid.introduce.security.tests.stubs.AnonPreferedRequest();
            org.cagrid.introduce.security.tests.stubs.AnonPreferedResponse boxedResult = portType.anonPrefered(params);
            return boxedResult.getResponse();
        }
    }


    public java.lang.String anonNotPrefered() throws RemoteException {
        synchronized (portTypeMutex) {
            configureStubSecurity((Stub) portType, "anonNotPrefered");
            org.cagrid.introduce.security.tests.stubs.AnonNotPreferedRequest params = new org.cagrid.introduce.security.tests.stubs.AnonNotPreferedRequest();
            org.cagrid.introduce.security.tests.stubs.AnonNotPreferedResponse boxedResult = portType
                .anonNotPrefered(params);
            return boxedResult.getResponse();
        }
    }

}
