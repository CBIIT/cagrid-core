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
package org.cagrid.gridgrouper.test.system.steps;

import gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier;
import gov.nih.nci.cagrid.gridgrouper.client.GridGrouperClient;
import gov.nih.nci.cagrid.gridgrouper.stubs.types.GridGrouperRuntimeFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.types.StemNotFoundFault;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.rmi.RemoteException;
import java.util.HashSet;

import org.apache.axis.types.URI.MalformedURIException;

public class GrouperCheckStemsStep extends Step {
	private String endpoint;
	private String path;
	private String[] children;

	public GrouperCheckStemsStep(String path, String[] children, String endpoint) {
		super();

		this.endpoint = endpoint;
		this.path = path;
		this.children = children;
	}

	@Override
	public void runStep() throws GridGrouperRuntimeFault, StemNotFoundFault, RemoteException, MalformedURIException {
		System.out.println("GrouperCheckStemsStep " + this.path);

		GridGrouperClient grouper = new GridGrouperClient(this.endpoint);

		// get child stems
		StemDescriptor[] stems = grouper.getChildStems(new StemIdentifier(null, this.path));
		if (stems == null && this.children.length == 0) {
			return;
		}
		assertEquals(this.children.length, stems.length);
		HashSet<String> stemSet = new HashSet<String>(stems.length);
		for (StemDescriptor stem : stems) {
			stemSet.add(stem.getName());
		}

		// check child stems
		String path = this.path;
		if (!path.equals("")) {
			path = path + ":";
		}
		for (String child : this.children) {
			assertTrue(stemSet.contains(path + child));
		}
	}
}
