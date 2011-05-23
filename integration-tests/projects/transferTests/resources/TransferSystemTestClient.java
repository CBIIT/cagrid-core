package org.cagrid.transfer.system.test.client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.AxisClient;
import org.apache.axis.client.Stub;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

import org.oasis.wsrf.properties.GetResourcePropertyResponse;

import org.globus.common.CoGProperties;
import org.globus.gsi.GlobusCredential;

import org.cagrid.transfer.system.test.stubs.TransferSystemTestPortType;
import org.cagrid.transfer.system.test.stubs.service.TransferSystemTestServiceAddressingLocator;
import org.cagrid.transfer.system.test.common.TransferSystemTestI;

import gov.nih.nci.cagrid.common.security.ProxyUtil;
import gov.nih.nci.cagrid.introduce.security.client.ServiceSecurityClient;


/**
 * This class is autogenerated, DO NOT EDIT GENERATED GRID SERVICE ACCESS
 * METHODS. This client is generated automatically by Introduce to provide a
 * clean unwrapped API to the service. On construction the class instance will
 * contact the remote service and retrieve it's security metadata description
 * which it will use to configure the Stub specifically for each method call.
 * 
 * @created by Introduce Toolkit version 1.2
 */
public class TransferSystemTestClient extends TransferSystemTestClientBase implements TransferSystemTestI {

    public TransferSystemTestClient(String url) throws MalformedURIException, RemoteException {
        this(url, null);
    }


    public TransferSystemTestClient(String url, GlobusCredential proxy) throws MalformedURIException, RemoteException {
        super(url, proxy);
    }


    public TransferSystemTestClient(EndpointReferenceType epr) throws MalformedURIException, RemoteException {
        this(epr, null);
    }


    public TransferSystemTestClient(EndpointReferenceType epr, GlobusCredential proxy) throws MalformedURIException,
        RemoteException {
        super(epr, proxy);
    }


    public static void usage() {
        System.out.println(TransferSystemTestClient.class.getName() + " -url <service url>");
    }


    public static void main(String[] args) {
        System.out.println("Running the Grid Service Client");
        try {
            if (!(args.length < 2)) {
                if (args[0].equals("-url")) {
                	org.globus.common.CoGProperties properties = org.globus.common.CoGProperties.getDefault();
            		properties.setCaCertLocations(".");
            		org.globus.common.CoGProperties.setDefault(properties);
                	
                    GlobusCredential creds = null;
                    GlobusCredential anothercreds = null;
                    try {
                        creds = ProxyUtil.loadProxy("user.proxy");
                        anothercreds = ProxyUtil.loadProxy("user2.proxy");
                        System.out.println("Using proxy with id= " + creds.getIdentity() + " and lifetime "
                            + creds.getTimeLeft());
                    } catch (Exception e1) {
                        System.out.println("No proxy file loaded so running with no credentials");
                    }
                    TransferSystemTestClient client = new TransferSystemTestClient(args[1], creds);
                    // place client calls here if you want to use this main as a
                    // test....
                    System.out.println("Creating transfer context");
                    org.cagrid.transfer.context.stubs.types.TransferServiceContextReference tref = client
                        .createTransferMethodStep();

                    System.out.println("retrieving transfer descriptor");
                    org.cagrid.transfer.context.client.TransferServiceContextClient tclient = new org.cagrid.transfer.context.client.TransferServiceContextClient(
                        tref.getEndpointReference(), creds);

                    System.out.println("getting handle to data transfer descriptor");
                    org.cagrid.transfer.descriptor.DataTransferDescriptor desc = tclient.getDataTransferDescriptor();

                    System.out.println("writing data to service at: " + desc.getUrl());
                    String testString = "Testing the TrasferService";
                    org.cagrid.transfer.context.client.helper.TransferClientHelper.putData(new ByteArrayInputStream(
                        testString.getBytes()), testString.getBytes().length, desc, creds);

                    System.out.println("setting status to Staged");
                    tclient.setStatus(org.cagrid.transfer.descriptor.Status.Staged);

                    if (anothercreds != null) {
                        System.out.println("try to read with bad creds");
                        try {
                            java.io.InputStream istream = org.cagrid.transfer.context.client.helper.TransferClientHelper
                                .getData(tclient.getDataTransferDescriptor(), anothercreds);
                            java.io.InputStreamReader reader = new java.io.InputStreamReader(istream);
                            java.lang.StringBuffer str = new java.lang.StringBuffer();
                            char[] buff = new char[8192];
                            int len = 0;
                            while ((len = reader.read(buff)) != -1) {
                                str.append(buff, 0, len);
                            }
                            reader.close();
                            System.out.println("Should not have been able to read with another users credentials");
                            System.exit(1);
                        } catch (Exception e) {
                            System.out.println("Not able to read the data with bad creds as expected.");
                        }
                    }

                    System.out.println("reading data from service");
                    java.io.InputStream istream = org.cagrid.transfer.context.client.helper.TransferClientHelper
                        .getData(tclient.getDataTransferDescriptor(), creds);
                    java.io.InputStreamReader reader = new java.io.InputStreamReader(istream);
                    java.lang.StringBuffer str = new java.lang.StringBuffer();
                    char[] buff = new char[8192];
                    int len = 0;
                    while ((len = reader.read(buff)) != -1) {
                        str.append(buff, 0, len);
                    }
                    reader.close();

                    System.out.println("comparing data sent to data recieved");
                    if (str.toString().equals(testString)) {
                        System.out.println("dataOK");
                    } else {
                        System.out.println("data recieved does not match data sent");
                        System.out.println("Sent:");
                        System.out.println(testString);
                        System.out.println("Recieved:");
                        System.out.println(str.toString());
                        System.exit(1);
                    }

                    System.out.println("destroying the resource");
                    tclient.destroy();

                    System.out.println("try to receive that data again after destroyed");
                    try {
                        istream = org.cagrid.transfer.context.client.helper.TransferClientHelper.getData(desc, creds);
                        reader = new java.io.InputStreamReader(istream);
                        str = new java.lang.StringBuffer();
                        len = 0;
                        while ((len = reader.read(buff)) != -1) {
                            str.append(buff, 0, len);
                        }
                        reader.close();
                        System.out.println("read data again and should not have neen able to");
                        System.exit(1);
                    } catch (Exception e) {
                        System.out.println("data was successfully removed");
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


    public org.cagrid.transfer.context.stubs.types.TransferServiceContextReference createTransferMethodStep()
        throws RemoteException {
        synchronized (portTypeMutex) {
            configureStubSecurity((Stub) portType, "createTransferMethodStep");
            org.cagrid.transfer.system.test.stubs.CreateTransferMethodStepRequest params = new org.cagrid.transfer.system.test.stubs.CreateTransferMethodStepRequest();
            org.cagrid.transfer.system.test.stubs.CreateTransferMethodStepResponse boxedResult = portType
                .createTransferMethodStep(params);
            return boxedResult.getTransferServiceContextReference();
        }
    }

}
