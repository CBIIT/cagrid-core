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
package org.cagrid.gaards.cds.service;

import gov.nih.nci.cagrid.common.FaultUtil;
import junit.framework.TestCase;

import org.cagrid.gaards.cds.testutils.Utils;

public class PropertyManagerTest extends TestCase {

	public void testPropertyMangager() {
		PropertyManager pm = null;
		try {
			pm = Utils.getPropertyManager();
			pm.clearAllProperties();
			pm = Utils.getPropertyManager();
			assertEquals(PropertyManager.CURRENT_VERSION, pm.getVersion());
			assertNull(pm.getKeyManager());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		} finally {
			try {
				pm.clearAllProperties();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		Utils.getDatabase().createDatabaseIfNeeded();
	}
}
