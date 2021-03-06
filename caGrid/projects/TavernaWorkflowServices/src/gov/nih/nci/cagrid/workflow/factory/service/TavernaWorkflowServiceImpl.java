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
package gov.nih.nci.cagrid.workflow.factory.service;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.workflow.factory.stubs.types.WorkflowException;
import gov.nih.nci.cagrid.workflow.service.impl.service.globus.resource.TavernaWorkflowServiceImplResource;
import gov.nih.nci.cagrid.workflow.service.impl.service.globus.resource.TavernaWorkflowServiceImplResourceHome;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.Constants;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.container.ServiceHost;
import org.globus.wsrf.utils.AddressingUtils;

import workflowmanagementfactoryservice.WMSInputType;
import workflowmanagementfactoryservice.WMSOutputType;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.naming.Context;
import javax.naming.InitialContext;

/** 
 * TODO:I am the service side implementation class.  IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class TavernaWorkflowServiceImpl extends TavernaWorkflowServiceImplBase {

	
	private static TavernaWorkflowServiceConfiguration config = null;
	ExecutorService threadExecutor = null;
	private int concurrent = 2;
	
	public TavernaWorkflowServiceImpl() throws RemoteException {		
		super();
		try{
			config = TavernaWorkflowServiceConfiguration.getConfiguration();
			concurrent = Integer.parseInt(config.getConcurrentWorkflows());
			threadExecutor = Executors.newFixedThreadPool(concurrent);
			System.out.println("Concurrent workflows allowed: " + concurrent);

		}catch (Exception e1) {
			
			e1.printStackTrace();
			throw new RemoteException(e1.getLocalizedMessage());
		}
	}

  public workflowmanagementfactoryservice.WMSOutputType createWorkflow(workflowmanagementfactoryservice.WMSInputType wMSInputElement) throws RemoteException, gov.nih.nci.cagrid.workflow.factory.stubs.types.WorkflowException {

	  TavernaWorkflowServiceImplResourceHome home = null;
		ResourceKey key = null;
		int TERM_TIME = 180;
		try {
			System.out.println("Creating a resource for the workflow..");
			Context ctx = new InitialContext();
			String lookupString = Constants.JNDI_SERVICES_BASE_NAME +
			"cagrid/TavernaWorkflowServiceImpl"+ "/home";
			home = (TavernaWorkflowServiceImplResourceHome) ctx.lookup(lookupString);

			key = home.createResource();

			//Create a resource on the Impl Service.
			TavernaWorkflowServiceImplResource workflowResource = home.getResource(key);

			EndpointReferenceType epr = AddressingUtils.createEndpointReference(ServiceHost
					.getBaseURL() + "cagrid/TavernaWorkflowServiceImpl", key);
			
			//If the Client sends a Termination time, use it. Otherwise use the default 180min.
			Calendar termTime = wMSInputElement.getTerminationTime();
			
			if(termTime == null){
				termTime = Calendar.getInstance();
		        termTime.add(Calendar.MINUTE, TERM_TIME);
			}
			workflowResource.setTerminationTime(termTime);
	        
			workflowResource.createWorkflow(wMSInputElement, threadExecutor);
			WMSOutputType wMSOutputElement = new WMSOutputType();
			wMSOutputElement.setWorkflowEPR(epr);
			return wMSOutputElement;
			
		} catch (Exception e1) {
			
			e1.printStackTrace();
			throw new RemoteException(e1.getLocalizedMessage());
		}
	}

}

