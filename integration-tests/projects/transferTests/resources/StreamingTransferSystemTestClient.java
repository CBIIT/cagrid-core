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

import org.cagrid.transfer.context.client.helper.TransferClientHelper;
import org.cagrid.transfer.descriptor.Status;
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
                        .createStreamingTransferMethodStep();

                    System.out.println("retrieving transfer descriptor");
                    org.cagrid.transfer.context.client.TransferServiceContextClient tclient = new org.cagrid.transfer.context.client.TransferServiceContextClient(
                        tref.getEndpointReference(), creds);

                    System.out.println("getting handle to data transfer descriptor");
                    org.cagrid.transfer.descriptor.DataTransferDescriptor desc = tclient.getDataTransferDescriptor();
                    
                    
                    while(!tclient.getStatus().equals(Status.Staging));
                    InputStream is = TransferClientHelper.getData(tclient.getDataTransferDescriptor(),creds);
                    int bytesRead = 0;
                    int mbRead = 0;
                    while(is.read()!=-1){
                        bytesRead++;
                        if(bytesRead%1024==0){
                            System.out.print("." + ++mbRead);
                        }
                    }
                    System.out.println("READ: " + bytesRead);
              
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


    public org.cagrid.transfer.context.stubs.types.TransferServiceContextReference createStreamingTransferMethodStep()
        throws RemoteException {
        synchronized (portTypeMutex) {
            configureStubSecurity((Stub) portType, "createStreamingTransferMethodStep");
            org.cagrid.transfer.system.test.stubs.CreateStreamingTransferMethodStepRequest params = new org.cagrid.transfer.system.test.stubs.CreateStreamingTransferMethodStepRequest();
            org.cagrid.transfer.system.test.stubs.CreateStreamingTransferMethodStepResponse boxedResult = portType
                .createStreamingTransferMethodStep(params);
            return boxedResult.getTransferServiceContextReference();
        }
    }

}
