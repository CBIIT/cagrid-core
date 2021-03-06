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
package gov.nih.nci.cagrid.data.utilities;


import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.common.DataServiceI;
import gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType;
import gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Iterator;

/** 
 *  DataServiceHandle
 *  Wrapper implementation over a data service instance to return iterators
 *  over the CQL Result Set's objects
 *  
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * @deprecated As of caGrid 1.4, CQL 2 is the preferred query language.  http://cagrid.org/display/dataservices/CQL+2
 * 
 * @created Jun 15, 2006 
 * @version $Id$ 
 */
public class DataServiceHandle implements DataServiceIterator {

	private DataServiceI service;
	private InputStream wsddStream;
	
	public DataServiceHandle(DataServiceI dataService) {
		this(dataService, (InputStream) null);
	}
	
	
	public DataServiceHandle(DataServiceI dataService, String wsddFilename) 
		throws FileNotFoundException {
		this(dataService, new FileInputStream(wsddFilename));
	}
	
	
	public DataServiceHandle(DataServiceI dataService, InputStream wsddStream) {
		this.service = dataService;
		this.wsddStream = wsddStream;
	}
	
	
	public Iterator query(CQLQuery cqlQuery) 
		throws MalformedQueryExceptionType, QueryProcessingExceptionType, RemoteException {
		CQLQueryResults results = service.query(cqlQuery);
		if (wsddStream == null) {
			return new CQLQueryResultsIterator(results);
		} else {
			return new CQLQueryResultsIterator(results, wsddStream);
		}
	}
}
