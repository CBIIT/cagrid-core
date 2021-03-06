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
package org.cagrid.cds.test.steps;

import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.util.List;

import org.cagrid.cds.test.util.GridCredential;
import org.cagrid.gaards.dorian.client.LocalAdministrationClient;
import org.cagrid.gaards.dorian.idp.Application;
import org.cagrid.gaards.dorian.idp.LocalUser;
import org.cagrid.gaards.dorian.idp.LocalUserFilter;
import org.cagrid.gaards.dorian.idp.LocalUserStatus;
import org.globus.gsi.GlobusCredential;


/**
 * This step approves a user application by finding the user in dorian and
 * marking the account status as active.
 * 
 * @author Patrick McConnell
 */
public class DorianApproveRegistrationStep extends Step {
    private String serviceURL;
    private Application application;
    private GridCredential credential;


    public DorianApproveRegistrationStep(Application application, String serviceURL, GridCredential credential) {
        super();
        this.application = application;
        this.serviceURL = serviceURL;
        this.credential = credential;
    }


    @Override
    public void runStep() throws Throwable {
        GlobusCredential proxy = null;
        if (credential != null) {
            proxy = credential.getCredential();
        }
        LocalAdministrationClient client = new LocalAdministrationClient(this.serviceURL, proxy);

        // find users
        LocalUserFilter filter = new LocalUserFilter();
        filter.setUserId(this.application.getUserId());
        filter.setStatus(LocalUserStatus.Pending);
        List<LocalUser> users = client.findUsers(filter);
        assertNotNull(users);
        assertTrue(users.size() > 0);

        // find user
        LocalUser user = findUser(users);
        assertNotNull(user);

        // accept application
        user.setStatus(LocalUserStatus.Active);
        client.updateUser(user);
    }


    private LocalUser findUser(List<LocalUser> users) {
        for (LocalUser user : users) {
            if (user.getUserId().equals(application.getUserId())) {
                return user;
            }
        }
        return null;
    }
}
