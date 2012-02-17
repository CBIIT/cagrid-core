package gov.nih.nci.cagrid.fqp.service;

import gov.nih.nci.cagrid.fqp.service.globus.resource.FederatedQueryProcessorResource;
import  gov.nih.nci.cagrid.fqp.service.FederatedQueryProcessorConfiguration;

import java.rmi.RemoteException;

import javax.naming.InitialContext;
import javax.xml.namespace.QName;

import org.apache.axis.MessageContext;
import org.globus.wsrf.Constants;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.ResourceContextException;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceHome;
import org.globus.wsrf.ResourceProperty;
import org.globus.wsrf.ResourcePropertySet;


/** 
 * DO NOT EDIT:  This class is autogenerated!
 *
 * Provides some simple accessors for the Impl.
 * 
 * @created by Introduce Toolkit version 1.5
 * 
 */
public abstract class FederatedQueryProcessorImplBase {
	
	public FederatedQueryProcessorImplBase() throws RemoteException {
	
	}
	
	public FederatedQueryProcessorConfiguration getConfiguration() throws Exception {
		return FederatedQueryProcessorConfiguration.getConfiguration();
	}
	
	
	public gov.nih.nci.cagrid.fqp.service.globus.resource.FederatedQueryProcessorResourceHome getResourceHome() throws Exception {
		ResourceHome resource = getResourceHome("home");
		return (gov.nih.nci.cagrid.fqp.service.globus.resource.FederatedQueryProcessorResourceHome)resource;
	}

	
	
	
	public ResourceHome getCaGridEnumerationResourceHome() throws Exception {
		ResourceHome resource = getResourceHome("caGridEnumerationHome");
		return resource;
	}
	
	public gov.nih.nci.cagrid.fqp.results.service.globus.resource.FederatedQueryResultsResourceHome getFederatedQueryResultsResourceHome() throws Exception {
		ResourceHome resource = getResourceHome("federatedQueryResultsHome");
		return (gov.nih.nci.cagrid.fqp.results.service.globus.resource.FederatedQueryResultsResourceHome)resource;
	}
	
	public gov.nih.nci.cagrid.fqp.resultsretrieval.service.globus.resource.FederatedQueryResultsRetrievalResourceHome getFederatedQueryResultsRetrievalResourceHome() throws Exception {
		ResourceHome resource = getResourceHome("federatedQueryResultsRetrievalHome");
		return (gov.nih.nci.cagrid.fqp.resultsretrieval.service.globus.resource.FederatedQueryResultsRetrievalResourceHome)resource;
	}
	
	
	protected ResourceHome getResourceHome(String resourceKey) throws Exception {
		MessageContext ctx = MessageContext.getCurrentContext();

		ResourceHome resourceHome = null;
		
		String servicePath = ctx.getTargetService();

		String jndiName = Constants.JNDI_SERVICES_BASE_NAME + servicePath + "/" + resourceKey;
		try {
			javax.naming.Context initialContext = new InitialContext();
			resourceHome = (ResourceHome) initialContext.lookup(jndiName);
		} catch (Exception e) {
			throw new Exception("Unable to instantiate resource home. : " + resourceKey, e);
		}

		return resourceHome;
	}


}

