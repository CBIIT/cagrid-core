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
package org.cagrid.gaards.dorian.test.system.steps;

import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.security.cert.X509Certificate;
import java.util.List;

import org.cagrid.gaards.dorian.client.GridAdministrationClient;
import org.cagrid.gaards.dorian.federation.TrustedIdP;
import org.cagrid.gaards.dorian.federation.TrustedIdPStatus;
import org.cagrid.gaards.pki.CertUtil;


public class VerifyTrustedIdPStep extends Step {

    private String serviceURL;
    private GridCredentialRequestStep admin;
    private String name;
    private String displayName;
    private X509Certificate certificate;
    private TrustedIdPStatus status;
    private String authenticationServiceURL;
    private String authenticationServiceIdentity;
    private String userPolicyClass;
    private boolean publish;


    public VerifyTrustedIdPStep(String serviceURL, GridCredentialRequestStep admin, String name) {
        this.serviceURL = serviceURL;
        this.admin = admin;
        this.name = name;
    }


    public void setPublish(boolean publish) {
        this.publish = publish;
    }


    public boolean isPublish() {
        return publish;
    }


    public void runStep() throws Throwable {
        GridAdministrationClient client = new GridAdministrationClient(serviceURL, this.admin.getGridCredential());
        List<TrustedIdP> idps = client.getTrustedIdPs();
        boolean found = false;
        for (int i = 0; i < idps.size(); i++) {
            TrustedIdP idp = idps.get(i);
            if (idp.getName().endsWith(this.name)) {
                found = true;
                if (getDisplayName() != null) {
                    assertEquals(getDisplayName(), idp.getDisplayName());
                }
                if (getCertificate() != null) {
                    assertEquals("IdP Certificate did not match expected", getCertificate(), CertUtil.loadCertificate(idp.getIdPCertificate()));
                }

                if (getStatus() != null) {
                    assertEquals("IdP Status did not match expectec", getStatus(), idp.getStatus());
                }

                if (getAuthenticationServiceURL() != null) {
                    assertEquals("IdP URL did not match expected", getAuthenticationServiceURL(), idp.getAuthenticationServiceURL());
                }

                if (getAuthenticationServiceIdentity() != null) {
                    assertEquals("IdP Identity did not match expected", getAuthenticationServiceIdentity(), idp.getAuthenticationServiceIdentity());
                }

                if (getUserPolicyClass() != null) {
                    assertEquals("IdP Policy class did not match expected", getUserPolicyClass(), idp.getUserPolicyClass());
                }
                assertEquals(isPublish(), client.getPublish(idp));
            }
        }
        if (!found) {
            fail("The identity provider " + name
                + " was not found as a trusted identity provider when it was expected to be.");
        }
    }


    public String getDisplayName() {
        return displayName;
    }


    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    public X509Certificate getCertificate() {
        return certificate;
    }


    public void setCertificate(X509Certificate certificate) {
        this.certificate = certificate;
    }


    public TrustedIdPStatus getStatus() {
        return status;
    }


    public void setStatus(TrustedIdPStatus status) {
        this.status = status;
    }


    public String getAuthenticationServiceURL() {
        return authenticationServiceURL;
    }


    public void setAuthenticationServiceURL(String authenticationServiceURL) {
        this.authenticationServiceURL = authenticationServiceURL;
    }


    public String getAuthenticationServiceIdentity() {
        return authenticationServiceIdentity;
    }


    public void setAuthenticationServiceIdentity(String authenticationServiceIdentity) {
        this.authenticationServiceIdentity = authenticationServiceIdentity;
    }


    public String getUserPolicyClass() {
        return userPolicyClass;
    }


    public void setUserPolicyClass(String userPolicyClass) {
        this.userPolicyClass = userPolicyClass;
    }
}
