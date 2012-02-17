package gov.nih.nci.cagrid.fqp.resultsretrieval.service.globus;


import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import javax.security.auth.Subject;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;

import gov.nih.nci.cagrid.introduce.servicetools.security.AuthorizationExtension;
import org.globus.wsrf.impl.security.authorization.exceptions.AuthorizationException;
import org.globus.wsrf.impl.security.authorization.exceptions.CloseException;
import org.globus.wsrf.impl.security.authorization.exceptions.InitializeException;
import org.globus.wsrf.impl.security.authorization.exceptions.InvalidPolicyException;
import org.globus.wsrf.security.authorization.PDP;
import org.globus.wsrf.security.authorization.PDPConfig;
import org.globus.wsrf.config.ContainerConfig;
import org.w3c.dom.Node;


/** 
 * DO NOT EDIT:  This class is autogenerated!
 *
 * This is a PDP for use with the globus authorization callout.
 * This class will have a authorize method for each method on this grid service.
 * The method is responsible for making any authorization callouts required to satisfy the 
 * authorization requirements placed on each method call.  Each method will either return
 * upon a successful authorization or will throw an exception upon a failed authorization.
 * 
 * @created by Introduce Toolkit version 1.5
 * 
 */
public class FederatedQueryResultsRetrievalAuthorization implements PDP {

	public static final String SERVICE_NAMESPACE = "http://fqp.cagrid.nci.nih.gov/FederatedQueryProcessor/ResultsRetrieval";
	
	Map authorizationClassMap = new HashMap();
	
	
	public FederatedQueryResultsRetrievalAuthorization() {
	}
	
	protected String getServiceNamespace(){
		return SERVICE_NAMESPACE;
	}
	
	public static String getCallerIdentity() {
		String caller = org.globus.wsrf.security.SecurityManager.getManager().getCaller();
		if ((caller == null) || (caller.equals("<anonymous>"))) {
			return null;
		} else {
			return caller;
		}
	}
					
	public void authorizeGetServiceSecurityMetadata(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   				
	public void authorizeDestroy(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   				
	public void authorizeSetTerminationTime(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   				
	public void authorizeSubscribe(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   				
	public void authorizeGetMultipleResourceProperties(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   				
	public void authorizeGetResourceProperty(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   				
	public void authorizeQueryResourceProperties(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   				
	public void authorizeIsProcessingComplete(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   				
	public void authorizeGetExecutionStatus(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   				
	public void authorizeGetAggregateResults(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   				
	public void authorizeGetResults(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   				
	public void authorizeEnumerate(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   				
	public void authorizeTransfer(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException {
		
	}
	   
	
	public boolean isPermitted(Subject peerSubject, MessageContext context, QName operation)
		throws AuthorizationException {
		
		if(!operation.getNamespaceURI().equals(getServiceNamespace())){
		  return false;
		}
		if(operation.getLocalPart().equals("getServiceSecurityMetadata")){
			authorizeGetServiceSecurityMetadata(peerSubject, context, operation);
			return true;
		} else if(operation.getLocalPart().equals("destroy")){
			authorizeDestroy(peerSubject, context, operation);
			return true;
		} else if(operation.getLocalPart().equals("setTerminationTime")){
			authorizeSetTerminationTime(peerSubject, context, operation);
			return true;
		} else if(operation.getLocalPart().equals("subscribe")){
			authorizeSubscribe(peerSubject, context, operation);
			return true;
		} else if(operation.getLocalPart().equals("getMultipleResourceProperties")){
			authorizeGetMultipleResourceProperties(peerSubject, context, operation);
			return true;
		} else if(operation.getLocalPart().equals("getResourceProperty")){
			authorizeGetResourceProperty(peerSubject, context, operation);
			return true;
		} else if(operation.getLocalPart().equals("queryResourceProperties")){
			authorizeQueryResourceProperties(peerSubject, context, operation);
			return true;
		} else if(operation.getLocalPart().equals("isProcessingComplete")){
			authorizeIsProcessingComplete(peerSubject, context, operation);
			return true;
		} else if(operation.getLocalPart().equals("getExecutionStatus")){
			authorizeGetExecutionStatus(peerSubject, context, operation);
			return true;
		} else if(operation.getLocalPart().equals("getAggregateResults")){
			authorizeGetAggregateResults(peerSubject, context, operation);
			return true;
		} else if(operation.getLocalPart().equals("getResults")){
			authorizeGetResults(peerSubject, context, operation);
			return true;
		} else if(operation.getLocalPart().equals("enumerate")){
			authorizeEnumerate(peerSubject, context, operation);
			return true;
		} else if(operation.getLocalPart().equals("transfer")){
			authorizeTransfer(peerSubject, context, operation);
			return true;
		} 		
		return false;
	}
	

	public Node getPolicy(Node query) throws InvalidPolicyException {
		return null;
	}


	public String[] getPolicyNames() {
		return null;
	}


	public Node setPolicy(Node policy) throws InvalidPolicyException {
		return null;
	}


	public void close() throws CloseException {


	}


	public void initialize(PDPConfig config, String name, String id) throws InitializeException {
    	try{
    		String serviceName = (String)config.getProperty(name, "serviceName");
    	    String etcPath = ContainerConfig.getBaseDirectory() + File.separator + (String)config.getProperty(name, "etcDirectoryPath");

    	
    	} catch (Exception e){
        	throw new InitializeException(e.getMessage(),e);
		}
	}
	
	
}
