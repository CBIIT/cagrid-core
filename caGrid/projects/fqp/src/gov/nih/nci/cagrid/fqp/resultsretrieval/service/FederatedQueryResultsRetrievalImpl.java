package gov.nih.nci.cagrid.fqp.resultsretrieval.service;

import java.rmi.RemoteException;

/** 
 * TODO:I am the service side implementation class.  IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.4
 * 
 */
public class FederatedQueryResultsRetrievalImpl extends FederatedQueryResultsRetrievalImplBase {

	
	public FederatedQueryResultsRetrievalImpl() throws RemoteException {
		super();
	}
	
  public boolean isProcessingComplete() throws RemoteException {
    //TODO: Implement this autogenerated method
    throw new RemoteException("Not yet implemented");
  }

  public org.cagrid.fqp.results.metadata.FederatedQueryExecutionStatus getExecutionStatus() throws RemoteException {
    //TODO: Implement this autogenerated method
    throw new RemoteException("Not yet implemented");
  }

  public org.cagrid.cql2.results.CQLQueryResults getAggregateResults() throws RemoteException, gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault, gov.nih.nci.cagrid.fqp.results.stubs.types.ProcessingNotCompleteFault, gov.nih.nci.cagrid.fqp.results.stubs.types.InternalErrorFault {
    //TODO: Implement this autogenerated method
    throw new RemoteException("Not yet implemented");
  }

  public org.cagrid.data.dcql.results.DCQLQueryResultsCollection getResults() throws RemoteException, gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault, gov.nih.nci.cagrid.fqp.results.stubs.types.ProcessingNotCompleteFault, gov.nih.nci.cagrid.fqp.results.stubs.types.InternalErrorFault {
    //TODO: Implement this autogenerated method
    throw new RemoteException("Not yet implemented");
  }

  public gov.nih.nci.cagrid.enumeration.stubs.response.EnumerationResponseContainer enumerate() throws RemoteException, gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault, gov.nih.nci.cagrid.fqp.results.stubs.types.ProcessingNotCompleteFault, gov.nih.nci.cagrid.fqp.results.stubs.types.InternalErrorFault {
    //TODO: Implement this autogenerated method
    throw new RemoteException("Not yet implemented");
  }

  public org.cagrid.transfer.context.stubs.types.TransferServiceContextReference transfer() throws RemoteException, gov.nih.nci.cagrid.fqp.stubs.types.FederatedQueryProcessingFault, gov.nih.nci.cagrid.fqp.results.stubs.types.ProcessingNotCompleteFault, gov.nih.nci.cagrid.fqp.results.stubs.types.InternalErrorFault {
    //TODO: Implement this autogenerated method
    throw new RemoteException("Not yet implemented");
  }

}
