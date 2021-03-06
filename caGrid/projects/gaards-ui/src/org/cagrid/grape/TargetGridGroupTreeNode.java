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
package org.cagrid.grape;

import javax.swing.ImageIcon;

import org.cagrid.grape.configuration.Grid;
import org.cagrid.grape.model.ConfigurationGroup;

public class TargetGridGroupTreeNode extends TargetGridBaseTreeNode {

	private ConfigurationGroup group;

	public TargetGridGroupTreeNode(ConfigurationWindow window,
			ConfigurationTree tree, ConfigurationGroup group, Grid grid) throws Exception {
		super(window, tree, grid);
		this.group = group;
		this.processConfigurationDescriptors(group
				.getConfigurationDescriptors());
		this.setDisplayPanel(new ConfigurationDisplayPanel(group.getName()));
	}

	public ImageIcon getIcon() {
		return LookAndFeel.getConfigurationGroupIcon();
	}


	public String toString() {
		return group.getName();
	}
}
