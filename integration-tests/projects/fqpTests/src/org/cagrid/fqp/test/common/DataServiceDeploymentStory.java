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
package org.cagrid.fqp.test.common;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DeployServiceStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.SetIndexRegistrationStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StartContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.UnpackContainerStep;
import gov.nih.nci.cagrid.testing.system.haste.Step;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.cagrid.fqp.test.common.steps.DeleteDirectoryStep;
import org.cagrid.fqp.test.common.steps.DisableCql2QueryProcessorStep;
import org.cagrid.fqp.test.common.steps.UnzipServiceStep;

/** 
 *  DataServiceDeploymentStory
 *  Deploys a data service to a local service container and starts it up
 * 
 * @author David Ervin
 * 
 * @created Jul 9, 2008 11:46:02 AM
 * @version $Id: DataServiceDeploymentStory.java,v 1.9 2009-05-04 20:13:59 dervin Exp $ 
 */
public class DataServiceDeploymentStory extends Story implements ServiceContainerSource {
    
    private File dataServiceZip;
    private File temp;
    
    private ServiceContainer dataServiceContainer;
    private boolean secureDeployment;
    private boolean disableCql2;
    private boolean complete;
    
    /**
     * @param dataServiceZip
     *      The zip file containing the data service to be deployed
     * @param secureDeployment
     *      Flag indicates if the service should be deployed to a secure container
     * @param disableCql2
     *      Flag indicates that CQL 2 should be disabled on the service
     */
    public DataServiceDeploymentStory(File dataServiceZip, boolean secureDeployment, boolean disableCql2) {
        this.dataServiceZip = dataServiceZip;
        this.secureDeployment = secureDeployment;
        this.disableCql2 = disableCql2;
        this.complete = false;
    }
    
    
    public String getName() {
        return (secureDeployment ? "Secure " : "") + "Data Service " + (disableCql2 ? "Without CQL 2 " : "") + "Deployment";
        
    }
    

    public String getDescription() {
        return "Deploys a " + (secureDeployment ? "secure " : "") 
            + "data service " + (disableCql2 ? " without CQL 2 " : "") 
            + "to a local service container and starts it up";
    }
    
    
    public boolean storySetUp() {
        try {
            ServiceContainerType containerType = secureDeployment ?
                ServiceContainerType.SECURE_TOMCAT_6_CONTAINER : ServiceContainerType.TOMCAT_6_CONTAINER;
            dataServiceContainer = ServiceContainerFactory.createContainer(containerType);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        try {
            temp = File.createTempFile("FQPTestDataService", "Temp", 
                new File(System.getProperty("java.io.tmpdir")));
            temp.delete();
            temp.mkdirs();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        if (!temp.isDirectory() || !temp.canWrite()) {
            System.err.println("Failed to create writable temp directory: " + temp.getAbsolutePath());
            return false;
        }
        return true;
    }
    
    
    /**
     * Overridden to run tests, and on successful completion set 
     * the 'complete' flag to true so the service container can be returned
     */
    protected void runTest() throws Throwable {
        super.runTest();
        complete = true;
    }


    protected Vector<?> steps() {
        Vector<Step> steps = new Vector<Step>();
        steps.add(new UnpackContainerStep(dataServiceContainer));
        steps.add(new UnzipServiceStep(dataServiceZip, temp));
        if (disableCql2) {
            steps.add(new DisableCql2QueryProcessorStep(temp.getAbsolutePath()));
        }
        List<String> args = Arrays.asList(new String[] {
            "-Dno.deployment.validation=true", "-Dperform.index.service.registration=false"
        });
        steps.add(new SetIndexRegistrationStep(temp.getAbsolutePath(), false));
        steps.add(new DeployServiceStep(dataServiceContainer, temp.getAbsolutePath(), args));
        steps.add(new DeleteDirectoryStep(temp));
        steps.add(new StartContainerStep(dataServiceContainer));
        return steps;
    }
    
    
    public ServiceContainer getServiceContainer() {
        if (dataServiceContainer == null || !complete) {
            throw new IllegalStateException(
                "Deployment Story has not completed to create a working service container!");
        }
        return dataServiceContainer;
    }
}
