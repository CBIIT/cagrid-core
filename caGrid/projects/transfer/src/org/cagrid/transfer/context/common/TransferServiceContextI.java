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
package org.cagrid.transfer.context.common;

import java.rmi.RemoteException;

/** 
 * This class is autogenerated, DO NOT EDIT.
 * 
 * This interface represents the API which is accessable on the grid service from the client. 
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public interface TransferServiceContextI {

  public org.oasis.wsrf.lifetime.DestroyResponse destroy(org.oasis.wsrf.lifetime.Destroy params) throws RemoteException ;

  public org.oasis.wsrf.lifetime.SetTerminationTimeResponse setTerminationTime(org.oasis.wsrf.lifetime.SetTerminationTime params) throws RemoteException ;

  /**
   * Returns the descriptor which can be used by the TransferClientHelper to download or upload the data.
   *
   */
  public org.cagrid.transfer.descriptor.DataTransferDescriptor getDataTransferDescriptor() throws RemoteException ;

  public org.oasis.wsn.SubscribeResponse subscribe(org.oasis.wsn.Subscribe params) throws RemoteException ;

  /**
   * Get the status of the data.
   *
   */
  public org.cagrid.transfer.descriptor.Status getStatus() throws RemoteException ;

  /**
   * Set the status of the transfer data.
   *
   * @param status
   */
  public void setStatus(org.cagrid.transfer.descriptor.Status status) throws RemoteException ;

}

