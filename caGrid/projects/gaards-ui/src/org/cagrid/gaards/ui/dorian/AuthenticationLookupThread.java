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
package org.cagrid.gaards.ui.dorian;

import gov.nih.nci.cagrid.common.Runner;
import gov.nih.nci.cagrid.common.RunnerGroup;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.gaards.dorian.client.GridUserClient;
import org.cagrid.gaards.dorian.federation.TrustedIdentityProvider;
import org.cagrid.grape.GAARDSApplication;
import org.cagrid.grape.GridApplication;
import org.cagrid.grape.configuration.ServiceConfiguration;
import org.cagrid.grape.configuration.ServiceDescriptor;
import org.cagrid.grape.configuration.Services;

public class AuthenticationLookupThread extends Runner {
	private static Log log = LogFactory.getLog(AuthenticationLookupThread.class);

	private DorianHandle handle;

	private List<AuthenticationServiceHandle> authenticationServices;

	public AuthenticationLookupThread(DorianHandle handle) {
		this.handle = handle;
	}

	public void execute() {
		this.authenticationServices = new ArrayList<AuthenticationServiceHandle>();
		try {
			GridUserClient client = this.handle.getUserClient();
			List<TrustedIdentityProvider> idps = client
					.getTrustedIdentityProviders();
			if (idps != null) {
				for (int i = 0; i < idps.size(); i++) {
					ServiceDescriptor des = new ServiceDescriptor();
					TrustedIdentityProvider idp = idps.get(i);
					String displayName = idp.getName();
					if (idp.getDisplayName() != null) {
						displayName = idp.getDisplayName();
					}
					if (idp.getAuthenticationServiceURL() != null) {
						des.setDisplayName(displayName);
						des.setServiceURL(idp.getAuthenticationServiceURL());
						des.setServiceIdentity(idp
								.getAuthenticationServiceIdentity());
						AuthenticationServiceHandle as = new AuthenticationServiceHandle(
								des);
						authenticationServices.add(as);
					}
				}
			}
				
			ServiceConfiguration conf = (ServiceConfiguration) GAARDSApplication
				.getContext().getConfigurationManager()
				.getActiveConfigurationObject(DorianUIConstants.AUTHENTICATION_SERVICE_CONF);
			Services s = conf.getServices();
			if (s != null) {
				ServiceDescriptor[] list = s.getServiceDescriptor();
				if (list != null) {
					for (int i = 0; i < list.length; i++) {
						authenticationServices.add(new AuthenticationServiceHandle(list[i]));
					}
				}
			}
			
			if ((authenticationServices != null)
					&& (authenticationServices.size() > 0)) {
				RunnerGroup grp = new RunnerGroup();
				for (int i = 0; i < this.authenticationServices.size(); i++) {
					grp.add(new AuthenticationProfilesLookupThread(
							this.authenticationServices.get(i)));
				}
				GridApplication.getContext().getApplication()
						.getThreadManager().executeGroupInBackground(grp);
			}

		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}

		this.handle.setAuthenticationServices(this.authenticationServices);
	}

	public List<AuthenticationServiceHandle> getAuthenticationServices() {
		return authenticationServices;
	}
}
