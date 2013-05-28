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
package gov.nih.nci.cagrid.introduce.upgrade.model;

import gov.nih.nci.cagrid.introduce.upgrade.common.IntroduceUpgradeStatus;
import gov.nih.nci.cagrid.introduce.upgrade.common.ModelUpgraderBase;


public class Model_1_3__1_4_Upgrader extends ModelUpgraderBase {

    public Model_1_3__1_4_Upgrader(IntroduceUpgradeStatus status, String servicePath) {
        super(status, servicePath, "1.3", "1.4");
    }


    protected void upgrade() throws Exception {
       
    }
}