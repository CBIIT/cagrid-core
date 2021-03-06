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
package org.cagrid.transfer.context.service;

import java.rmi.RemoteException;

import org.cagrid.transfer.context.service.globus.resource.TransferServiceContextResource;

/**
 * TODO:I am the service side implementation class. IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.2
 */
public class TransferServiceContextImpl extends TransferServiceContextImplBase {

    public TransferServiceContextImpl() throws RemoteException {
        super();
    }

  public org.cagrid.transfer.descriptor.DataTransferDescriptor getDataTransferDescriptor() throws RemoteException {
        TransferServiceContextResource resource = null;
        try {
            resource = getResourceHome().getAddressedResource();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error locating resource", e);
        }
        org.cagrid.transfer.descriptor.DataTransferDescriptor dataDesc = new org.cagrid.transfer.descriptor.DataTransferDescriptor();
        try {
            org.apache.axis.MessageContext ctx = org.apache.axis.MessageContext.getCurrentContext();
            String transportURL = (String) ctx.getProperty(org.apache.axis.MessageContext.TRANS_URL);
            transportURL = transportURL.substring(0, transportURL.lastIndexOf('/'));// cut
            // service
            // name
            transportURL = transportURL.substring(0, transportURL.lastIndexOf('/'));// cut
            // cagrid
            // etc.
            transportURL = transportURL.substring(0, transportURL.lastIndexOf('/'));// cut
            // services
            transportURL = transportURL.substring(0, transportURL.lastIndexOf('/'));// cut
            // wsrf
            dataDesc.setUrl(transportURL + getConfiguration().getTransferServletPathName() + "?id=" + resource.getID());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Cannot create retrieve URL for transferResource.", e);
        }

        dataDesc.setDataDescriptor(resource.getDataStorageDescriptor().getDataDescriptor());
        return dataDesc;
       
    }

  public org.cagrid.transfer.descriptor.Status getStatus() throws RemoteException {
        TransferServiceContextResource resource = null;
        try {
            resource = getResourceHome().getAddressedResource();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error locating resource", e);
        }
        return resource.getDataStorageDescriptor().getStatus();
    }

  public void setStatus(org.cagrid.transfer.descriptor.Status status) throws RemoteException {
        TransferServiceContextResource resource = null;
        try {
            resource = getResourceHome().getAddressedResource();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error locating resource: " + e.getMessage(), e);
        }
        resource.setStatus(status);

    }

}
