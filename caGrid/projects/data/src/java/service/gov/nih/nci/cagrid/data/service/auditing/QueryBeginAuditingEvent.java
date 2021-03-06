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
package gov.nih.nci.cagrid.data.service.auditing;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;

/** 
 *  QueryBeginAuditingEvent
 *  Auditing event fired when a call to the query method is first made
 * 
 * @author David Ervin
 * 
 * @created May 17, 2007 11:11:05 AM
 * @version $Id: QueryBeginAuditingEvent.java,v 1.2 2007-05-18 16:04:01 dervin Exp $ 
 */
public class QueryBeginAuditingEvent extends BaseAuditingEvent {

    public QueryBeginAuditingEvent(CQLQuery query, String callerId) {
        super(query, callerId);
    }
}
