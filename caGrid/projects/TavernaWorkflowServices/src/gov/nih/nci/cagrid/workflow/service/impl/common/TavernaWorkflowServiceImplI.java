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
package gov.nih.nci.cagrid.workflow.service.impl.common;

import java.rmi.RemoteException;

/** 
 * This class is autogenerated, DO NOT EDIT.
 * 
 * This interface represents the API which is accessable on the grid service from the client. 
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public interface TavernaWorkflowServiceImplI {

  public org.oasis.wsrf.lifetime.DestroyResponse destroy(org.oasis.wsrf.lifetime.Destroy params) throws RemoteException ;

  public org.oasis.wsrf.lifetime.SetTerminationTimeResponse setTerminationTime(org.oasis.wsrf.lifetime.SetTerminationTime params) throws RemoteException ;

  /**
   * Cancels the execution of a workflow
   *
   * @throws CannotCancelWorkflowFault
   *	
   */
  public void cancel() throws RemoteException, gov.nih.nci.cagrid.workflow.service.impl.stubs.types.CannotCancelWorkflowFault ;

  /**
   * Gives a detailed status of the workflow
   *
   * @throws WorkflowException
   *	
   */
  public workflowmanagementfactoryservice.WorkflowStatusEventType[] getDetailedStatus() throws RemoteException, gov.nih.nci.cagrid.workflow.service.impl.stubs.types.WorkflowException ;

  /**
   * Returns status of the workflow
   *
   * @throws WorkflowException
   *	
   */
  public workflowmanagementfactoryservice.WorkflowStatusType getStatus() throws RemoteException, gov.nih.nci.cagrid.workflow.service.impl.stubs.types.WorkflowException ;

  /**
   * Returns the output of the workflow
   *
   * @throws WorkflowException
   *	
   */
  public workflowmanagementfactoryservice.WorkflowOutputType getWorkflowOutput() throws RemoteException, gov.nih.nci.cagrid.workflow.service.impl.stubs.types.WorkflowException ;

  /**
   * Pause the workflow execution
   *
   * @throws WorkflowException
   *	
   * @throws CannotPauseWorkflowFault
   *	
   */
  public workflowmanagementfactoryservice.WorkflowStatusType pause() throws RemoteException, gov.nih.nci.cagrid.workflow.service.impl.stubs.types.WorkflowException, gov.nih.nci.cagrid.workflow.service.impl.stubs.types.CannotPauseWorkflowFault ;

  /**
   * Resumes a paused workflow
   *
   * @throws WorkflowException
   *	
   * @throws CannotResumeWorkflowFault
   *	
   */
  public workflowmanagementfactoryservice.WorkflowStatusType resume() throws RemoteException, gov.nih.nci.cagrid.workflow.service.impl.stubs.types.WorkflowException, gov.nih.nci.cagrid.workflow.service.impl.stubs.types.CannotResumeWorkflowFault ;

  /**
   * starts the workflow (This will be deprecated in future releases. A new operations startWorkflow will supersede this operation)
   *
   * @param startInputElement
   * @throws CannotStartWorkflowFault
   *	
   */
  public workflowmanagementfactoryservice.WorkflowStatusType start(workflowmanagementfactoryservice.StartInputType startInputElement) throws RemoteException, gov.nih.nci.cagrid.workflow.service.impl.stubs.types.CannotStartWorkflowFault ;

  public org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse getMultipleResourceProperties(org.oasis.wsrf.properties.GetMultipleResourceProperties_Element params) throws RemoteException ;

  public org.oasis.wsrf.properties.GetResourcePropertyResponse getResourceProperty(javax.xml.namespace.QName params) throws RemoteException ;

  public org.oasis.wsrf.properties.QueryResourcePropertiesResponse queryResourceProperties(org.oasis.wsrf.properties.QueryResourceProperties_Element params) throws RemoteException ;

  public org.oasis.wsn.SubscribeResponse subscribe(org.oasis.wsn.Subscribe params) throws RemoteException ;

  /**
   * Get the delegated credential from CDS using the EPR
   *
   * @param delegatedCredentialReference
   * @throws CannotSetCredential
   *	Unable to get or set the client's delegated credential
   */
  public void setDelegatedCredential(org.cagrid.gaards.cds.delegated.stubs.types.DelegatedCredentialReference delegatedCredentialReference) throws RemoteException, gov.nih.nci.cagrid.workflow.service.impl.stubs.types.CannotSetCredential ;

  /**
   * Uses caTransfer service to transfer large dataset to the Workflow Service.
   *
   * @param filename
   *	name of the file that the client wants to upload.
   */
  public org.cagrid.transfer.context.stubs.types.TransferServiceContextReference putInputData(java.lang.String filename) throws RemoteException ;

  /**
   * Uses caTransfer to transfer the output back to the client.  Used for large files.
   *
   */
  public org.cagrid.transfer.context.stubs.types.TransferServiceContextReference getOutputData() throws RemoteException ;

  /**
   * starts the workflow
   *
   * @param startInputElement
   * @throws CannotStartWorkflowFault
   *	
   */
  public workflowmanagementfactoryservice.WorkflowStatusType startWorkflow(workflowmanagementfactoryservice.StartInputType startInputElement) throws RemoteException, gov.nih.nci.cagrid.workflow.service.impl.stubs.types.CannotStartWorkflowFault ;

}

