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
package org.cagrid.introduce.test.system.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.test.steps.BaseStep;
import gov.nih.nci.cagrid.introduce.test.util.SourceUtils;

import java.io.File;


public class AddSecurityMethodsImplStep extends BaseStep {
	private TestCaseInfo tci;

	public AddSecurityMethodsImplStep(TestCaseInfo tci, boolean build) throws Exception {
		super(tci.getDir(),build);
		this.tci = tci;
	}


	public void runStep() throws Throwable {
		System.out.println("Adding methods implementation.");

		File inFileClient = new File(Utils.decodeUrl(this.getClass().getResource("/gold/security/" + "SecurityTestsClient.java")));
		File outFileClient = new File(tci.getDir() + File.separator + "src" + File.separator + tci.getPackageDir() + File.separator + "client" + File.separator  + tci.getName() + "Client.java");
		
		SourceUtils.modifyImpl(inFileClient, outFileClient, "main");
		
		File inFileImpl = new File(Utils.decodeUrl(this.getClass().getResource("/gold/security/" + "SecurityTestsImpl.java")));
		File outFileImpl = new File(tci.getDir() + File.separator + "src" + File.separator + tci.getPackageDir() + File.separator + "service" + File.separator  + tci.getName() + "Impl.java");
		
		SourceUtils.modifyImpl(inFileImpl, outFileImpl, "anonPrefered");
	    SourceUtils.modifyImpl(inFileImpl, outFileImpl, "anonNotPrefered");
			
		buildStep();
	}

}
