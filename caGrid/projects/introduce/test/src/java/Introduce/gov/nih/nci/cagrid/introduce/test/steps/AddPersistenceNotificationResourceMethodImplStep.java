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
package gov.nih.nci.cagrid.introduce.test.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.test.util.SourceUtils;

import java.io.File;


public class AddPersistenceNotificationResourceMethodImplStep extends BaseStep {
    private TestCaseInfo tci;
    private TestCaseInfo tci2;
    private String methodName;


    public AddPersistenceNotificationResourceMethodImplStep(TestCaseInfo tci, TestCaseInfo tci2, boolean build) throws Exception {
        super(tci.getDir(), build);
        this.tci = tci;
        this.tci2 = tci2;
    }


    public void runStep() throws Throwable {
        System.out.println("Adding a simple methods implementation.");

        File inFileClient = new File(Utils.decodeUrl(this.getClass().getResource("/gold/persistentnotification/" + tci.getName() + "SetClient.java")));
        File outFileClient = new File(tci.getDir() + File.separator + "src" + File.separator + tci.getPackageDir()
            + File.separator + "client" + File.separator + tci.getName() + "Client.java");

        Utils.copyFile(inFileClient, outFileClient);
        
        inFileClient = new File(Utils.decodeUrl(this.getClass().getResource("/gold/persistentnotification/" + tci2.getName() + "Client.java")));
        outFileClient = new File(tci2.getDir() + File.separator + "src" + File.separator + tci2.getPackageDir()
            + File.separator + "client" + File.separator + tci2.getName() + "Client.java");

        Utils.copyFile(inFileClient, outFileClient);
        

        File inFileImpl = new File(Utils.decodeUrl(this.getClass().getResource("/gold/persistentnotification/" + tci2.getName() + "Impl.java")));
        File outFileImpl = new File(tci2.getDir() + File.separator + "src" + File.separator + tci2.getPackageDir() + File.separator + "service" + File.separator  + tci2.getName() + "Impl.java");
        
        SourceUtils.modifyImpl(inFileImpl, outFileImpl, "setBook");
        
        inFileImpl = new File(Utils.decodeUrl(this.getClass().getResource("/gold/persistentnotification/" + tci2.getName() + "Impl.java")));
        outFileImpl = new File(tci2.getDir() + File.separator + "src" + File.separator + tci2.getPackageDir() + File.separator + "service" + File.separator  + tci2.getName() + "Impl.java");
        
        SourceUtils.modifyImpl(inFileImpl, outFileImpl, "getBook");

        buildStep();
    }

}
