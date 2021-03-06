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

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.File;

public class CopyConfigurationStep extends Step {

	private File serviceDir;
	private File configuration;
	private File properties;

	public CopyConfigurationStep(File serviceDir, File configuration,
			File properties) {
		this.serviceDir = serviceDir;
		this.configuration = configuration;
		this.properties = properties;
	}

	public void runStep() throws Throwable {
		if (this.configuration != null) {
			Utils.copyFile(this.configuration, new File(this.serviceDir
					.getAbsolutePath()
					+ File.separator
					+ "etc"
					+ File.separator
					+ "dorian-configuration.xml"));
		}
		if (this.properties != null) {
			Utils.copyFile(this.properties, new File(this.serviceDir
					.getAbsolutePath()
					+ File.separator
					+ "etc"
					+ File.separator
					+ "dorian.properties"));
		}
	}

}
