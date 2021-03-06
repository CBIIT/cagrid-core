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
package org.cagrid.introduce.test.system;

import gov.nih.nci.cagrid.introduce.test.NotificationTestCaseInfo;
import gov.nih.nci.cagrid.introduce.test.steps.AddBookResourcePropertyStep;
import gov.nih.nci.cagrid.introduce.test.steps.AddBookstoreSchemaStep;
import gov.nih.nci.cagrid.introduce.test.steps.AddNotificationMethodImplStep;
import gov.nih.nci.cagrid.introduce.test.steps.AddSetBookMethodStep;
import gov.nih.nci.cagrid.introduce.test.steps.CreateSkeletonStep;
import gov.nih.nci.cagrid.introduce.test.steps.RemoveSkeletonStep;
import gov.nih.nci.cagrid.introduce.test.steps.ValidateWSDLStep;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DeployServiceStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DestroyContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StartContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StopContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.UnpackContainerStep;
import gov.nih.nci.cagrid.testing.system.haste.Step;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.util.Vector;

import org.cagrid.introduce.test.system.steps.InvokeClientStep;


public class NotificationTest extends Story {
    
    private ServiceContainer container;
    private NotificationTestCaseInfo tci;

    public NotificationTest() {
        this.setName("Introduce Notification System Test");
    }


    public String getName() {
        return "Introduce Notification System Test";
    }


    public String getDescription() {
        return "Testing the Introduce Notification support";
    }


    @Override
    protected Vector<Step> steps() {
        Vector<Step> steps = new Vector<Step>();
        try {
            steps.add(new UnpackContainerStep(container));
            steps.add(new CreateSkeletonStep(tci, true));
            steps.add(new AddBookstoreSchemaStep(tci,false));
            steps.add(new AddBookResourcePropertyStep(tci,false));
            steps.add(new AddSetBookMethodStep(tci,false));
            steps.add(new AddNotificationMethodImplStep(tci,true));
            steps.add(new ValidateWSDLStep(tci,false));
            steps.add(new DeployServiceStep(container,tci.getDir()));
            steps.add(new StartContainerStep(container));
            steps.add(new InvokeClientStep(container,tci));
            
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        return steps;
    }
    

    protected boolean storySetUp() throws Throwable {
        // init the container
        try {
            container = ServiceContainerFactory.createContainer(
                ServiceContainerType.TOMCAT_6_CONTAINER);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Failed to create container: " + ex.getMessage());
        }
        
        tci = new NotificationTestCaseInfo();
        
        RemoveSkeletonStep step1 = new RemoveSkeletonStep(tci);
        try {
            step1.runStep();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }


    protected void storyTearDown() throws Throwable {
        RemoveSkeletonStep step1 = new RemoveSkeletonStep(tci);
        try {
            step1.runStep();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        StopContainerStep step2 = new StopContainerStep(container);
        try {
            step2.runStep();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DestroyContainerStep step3 = new DestroyContainerStep(container);
        try {
            step3.runStep();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
