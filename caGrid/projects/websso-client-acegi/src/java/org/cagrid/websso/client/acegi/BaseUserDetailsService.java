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
package org.cagrid.websso.client.acegi;

import java.util.Map;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.websso.common.WebSSOConstants;
import org.cagrid.websso.common.WebSSOClientHelper;
import org.springframework.dao.DataAccessException;

public abstract class BaseUserDetailsService implements UserDetailsService {

	private final Log log = LogFactory.getLog(getClass());
	
	/**
	 * Defines an interface for implementations that wish to retrieve user information from
	 * database using CSM or Data Access Service
	 * @param userName
	 * @return
	 */
	abstract protected WebSSOUser loadUserByGridId(String gridId);

	public UserDetails loadUserByUsername(String casUserId)
			throws UsernameNotFoundException, DataAccessException {
		Map<String, String> userAttributesMap = WebSSOClientHelper.getUserAttributes(casUserId);
		String gridId = getUserIdFromGridIdentity(userAttributesMap.get(WebSSOConstants.CAGRID_SSO_GRID_IDENTITY));
		WebSSOUser user = loadUserByGridId(gridId);
		loadSessionAttributes(userAttributesMap, user);
		log.info("User Info "+user);
		return user;
	}
	
	/**
	 *	load grid user information retrieved from WebSSO server 
	 */
	private void loadSessionAttributes(Map<String,String> userAttributesMap,WebSSOUser user){
		user.setFirstName(userAttributesMap.get(WebSSOConstants.CAGRID_SSO_FIRST_NAME));
		user.setGridId(userAttributesMap.get(WebSSOConstants.CAGRID_SSO_GRID_IDENTITY));
		user.setLastName(userAttributesMap.get(WebSSOConstants.CAGRID_SSO_LAST_NAME));
		user.setDelegatedEPR(userAttributesMap.get(WebSSOConstants.CAGRID_SSO_DELEGATION_SERVICE_EPR));
		user.setEmailId(userAttributesMap.get(WebSSOConstants.CAGRID_SSO_EMAIL_ID));		
	}
	
	private String getUserIdFromGridIdentity(String gridIdentity) {
		String[] sections = gridIdentity.split("=");
		return sections[sections.length - 1];
	}
}
