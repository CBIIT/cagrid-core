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
package org.cagrid.installer.tasks.service;

import java.io.File;
import java.util.Map;
import java.util.Properties;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.tasks.AntExecutionTask;
import org.cagrid.installer.tasks.BasicTask;
import org.cagrid.installer.util.InstallerUtils;


/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class DeployServiceTask extends BasicTask {

    protected String serviceName;


    /**
     * @param name
     * @param description
     */
    public DeployServiceTask(String name, String description, String serviceName) {
        super(name, description);
        this.serviceName = serviceName;
    }


    /*
     * (non-Javadoc)
     * @see org.cagrid.installer.tasks.BasicTask#internalExecute(java.util.Map)
     */
    @Override
    protected Object internalExecute(CaGridInstallerModel model) throws Exception {
        Map<String, String> env = InstallerUtils.getEnvironment(model);
        env.put("GLOBUS_LOCATION", model.getProperty(Constants.GLOBUS_HOME));
        env.put("CATALINA_HOME", model.getProperty(Constants.TOMCAT_HOME));
        env.put("JBOSS_HOME", model.getProperty(Constants.JBOSS_HOME));        
        
        Properties sysProps = InstallerUtils.getProxyProperties();
        if (model.isSet(Constants.JBOSS_HOME) &&
            model.getProperty(Constants.CONTAINER_TYPE).equals(Constants.CONTAINER_TYPE_JBOSS) ) {
            sysProps.put("env.JBOSS_HOME", model.getProperty(Constants.JBOSS_HOME) );
        }
        if (model.isSet(Constants.TOMCAT_HOME) &&
            model.getProperty(Constants.CONTAINER_TYPE).equals(Constants.CONTAINER_TYPE_TOMCAT) ) {
            sysProps.put("env.CATALINA_HOME", model.getProperty(Constants.TOMCAT_HOME) );
        }

        return runAntTask(model, env, sysProps);

    }


    protected Object runAntTask(CaGridInstallerModel model, Map<String, String> env, Properties sysProps)
        throws Exception {
        String antTarget = "";
        if (model.isTomcatContainer()) {
            antTarget = getDeployTomcatTarget();
        } else if (model.isJBossContainer()) {
            antTarget = getDeployJBossTarget();
        }
        AntExecutionTask antTask = new AntExecutionTask("", "", getBuildFilePath(model), antTarget, env, sysProps);
        Object result = antTask.execute(model);

        return result;
    }


    protected String getDeployTomcatTarget() {
        return "deployTomcat";
    }

    protected String getDeployJBossTarget() {
        return "deployJBoss";
    }


    protected String getBuildFilePath(CaGridInstallerModel model) {
        return model.getProperty(Constants.CAGRID_HOME) + File.separator 
            + "projects" + File.separator + this.serviceName 
            + File.separator + "build.xml";
    }

}
