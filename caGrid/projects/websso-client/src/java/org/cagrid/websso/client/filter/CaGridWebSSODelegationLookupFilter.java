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
package org.cagrid.websso.client.filter;

import gov.nih.nci.cagrid.common.FaultUtil;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.websso.common.WebSSOClientException;
import org.cagrid.websso.common.WebSSOConstants;
import org.cagrid.websso.common.WebSSOClientHelper;
import org.globus.gsi.GlobusCredential;
import org.springframework.util.Assert;


public class CaGridWebSSODelegationLookupFilter implements Filter {

	private static final String CERTIFICATE_FILE_PATH = "certificate-file-path";
	private static final String KEY_FILE_PATH = "key-file-path";
	private final Log log = LogFactory.getLog(getClass());

	private String certificateFilePath = null;
	private String keyFilePath = null;
	
	public void destroy() {
		// do nothing
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest)request).getSession();
		Boolean isGridCredentialLoaded = (Boolean) session.getAttribute(WebSSOConstants.IS_GRID_CREDENTIAL_LOADED);
		if (null == isGridCredentialLoaded
				|| isGridCredentialLoaded == Boolean.FALSE) {
			Boolean isSessionLoaded = (Boolean) session.getAttribute(WebSSOConstants.IS_SESSION_ATTRIBUTES_LOADED);
			if (null == isSessionLoaded || isSessionLoaded == Boolean.FALSE) {
				throw new ServletException(
						"WebSSO Attributes are not loaded in the Session");
			} else {
				String delegationEPR = (String)session.getAttribute(WebSSOConstants.CAGRID_SSO_DELEGATION_SERVICE_EPR);
				GlobusCredential userCredential;
				try {
					userCredential = WebSSOClientHelper.getUserCredential(delegationEPR,certificateFilePath,keyFilePath);
				} catch (WebSSOClientException e) {
					log.info(FaultUtil.printFaultToString(e));
					throw new ServletException(e);
				}
				session.setAttribute(WebSSOConstants.CAGRID_SSO_GRID_CREDENTIAL,userCredential);
				session.setAttribute(WebSSOConstants.IS_GRID_CREDENTIAL_LOADED,Boolean.TRUE);
			}
		}
		filterChain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.certificateFilePath = filterConfig.getInitParameter(CERTIFICATE_FILE_PATH);
		this.keyFilePath = filterConfig.getInitParameter(KEY_FILE_PATH);
		Assert.notNull(this.certificateFilePath,"Please spcify host certificate in web.xml");
		Assert.notNull(this.keyFilePath,"Please spcify host key in web.xml");
	}
}
