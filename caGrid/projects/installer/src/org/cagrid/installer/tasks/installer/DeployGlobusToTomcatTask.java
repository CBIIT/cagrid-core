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
/**
 * 
 */
package org.cagrid.installer.tasks.installer;

import java.io.File;
import java.util.Map;
import java.util.Properties;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.tasks.AntExecutionTask;


/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class DeployGlobusToTomcatTask extends CaGridInstallerAntTask {

    /**
	 * 
	 */
    public DeployGlobusToTomcatTask(String name, String description) {
        super(name, description, null);
    }


    public String getBuildFilePath() {
        return new File("scripts/tomcat/build.xml").getAbsolutePath();
    }


    protected Object runAntTask(CaGridInstallerModel model, String buildFile, String target, Map<String, String> env,
        Properties sysProps) throws Exception {
        
        if ( model.isSet(Constants.TOMCAT_HOME) ) {
            sysProps.put("tomcat.home", model.getProperty(Constants.TOMCAT_HOME));
        }

        boolean secure = model.isTrue(Constants.USE_SECURE_CONTAINER);

        setStepCount(1);
        if (!secure) {
            new AntExecutionTask("", "", getBuildFilePath(), "globus-deploy-tomcat", env, sysProps).execute(model);
        } else {
            new AntExecutionTask("", "", getBuildFilePath(), "globus-deploy-secure-tomcat", env, sysProps)
                .execute(model);
        }

        return null;
    }

}
