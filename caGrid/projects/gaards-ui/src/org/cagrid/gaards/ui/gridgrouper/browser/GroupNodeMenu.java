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
package org.cagrid.gaards.ui.gridgrouper.browser;

import gov.nih.nci.cagrid.common.FaultUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.gaards.ui.gridgrouper.tree.GridGrouperBaseTreeNode;
import org.cagrid.gaards.ui.gridgrouper.tree.GridGrouperTree;
import org.cagrid.gaards.ui.gridgrouper.tree.GroupTreeNode;
import org.cagrid.grape.utils.ErrorDialog;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 */
public class GroupNodeMenu extends GridGrouperTreeNodeMenu {
	private static Log log = LogFactory.getLog(GroupNodeMenu.class);
	
	private static final long serialVersionUID = 1L;

	public GroupNodeMenu(GroupManagementBrowser browser, GridGrouperTree tree) {
		super(browser, tree);
	}


	private GroupTreeNode getGroupNode() {
		return (GroupTreeNode) getGridGrouperTree().getCurrentNode();
	}


	public void removeNode() {
		GroupTreeNode node = getGroupNode();

		int id = getBrowser().getProgress().startEvent("Removing the group.... ");
		try {
			node.getGroup().delete();
			getBrowser().getContentManager().removeGroup(node);
			((GridGrouperBaseTreeNode) node.getParent()).refresh();
			getBrowser().getProgress().stopEvent(id, "Successfully removed the group !!!");
		} catch (Exception e) {
			getBrowser().getProgress().stopEvent(id, "Error removing the group !!!");
			ErrorDialog.showError(e);
			FaultUtil.logFault(log, e);
		}
	}

}
