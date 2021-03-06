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

import java.lang.reflect.Constructor;

import javax.swing.ImageIcon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.grape.model.ConfigurationDescriptor;
import org.cagrid.grape.model.ConfigurationEditor;
import org.cagrid.grape.model.ConfigurationEditors;


public class TargetGridDescriptorTreeNode extends ConfigurationDescriptorTreeNode {
	private static Log log = LogFactory.getLog(TargetGridDescriptorTreeNode.class);
	
	private String configurationName = null;

	public TargetGridDescriptorTreeNode(ConfigurationWindow window, ConfigurationTree tree,
		ConfigurationDescriptor des, String configurationName) throws Exception {
		super(window, tree, des, configurationName);
		this.configurationName = configurationName;
		if (des.getConfigurationPanel() == null) {
			this.setDisplayPanel(new ConfigurationDisplayPanel(des.getDisplayName()));
		} else {
			try {
				Class[] types = new Class[2];
				types[0] = ConfigurationDescriptorTreeNode.class;
				types[1] = Object.class;
				Constructor c = Class.forName(des.getConfigurationPanel()).getConstructor(types);
				Object[] args = new Object[2];
				args[0] = this;
				args[1] = GAARDSApplication.getContext().getConfigurationManager().getConfigurationObjectByConfiguration(des.getSystemName(), configurationName);
				this.setDisplayPanel((ConfigurationBasePanel) c.newInstance(args));
			} catch (Exception e) {
				this.setDisplayPanel(new ConfigurationDisplayPanel(des.getDisplayName()));
				log.error("An error occurred using the panel " + des.getConfigurationPanel()
					+ " for editing the preference " + des.getDisplayName());
				log.error(e.getMessage(), e);
			}
		}
		this.processConfigurationEditors(des.getConfigurationEditors());
	}


	protected void processConfigurationEditors(ConfigurationEditors list) throws Exception {

		if (list != null) {
			ConfigurationEditor[] des = list.getConfigurationEditor();
			if (des != null) {
				for (int i = 0; i < des.length; i++) {
					this.processConfigurationEditor(des[i]);
				}

			}
		}
	}


	protected void processConfigurationEditor(ConfigurationEditor editor) throws Exception {
		if (editor != null) {
			ConfigurationEditorTreeNode node = new ConfigurationEditorTreeNode(getConfigurationWindow(), getTree(),
				editor, GAARDSApplication.getContext().getConfigurationManager().getConfigurationObjectByConfiguration(getSystemName(), configurationName));
			this.add(node);
		}
	}


	public void addConfigurationEditor(ConfigurationEditor editor, Object object) throws Exception {
		ConfigurationEditorTreeNode node = new ConfigurationEditorTreeNode(getConfigurationWindow(), getTree(), editor,
			object);
		this.add(node);
		getTree().reload();
	}


	public ImageIcon getIcon() {
		return LookAndFeel.getConfigurationPropertyIcon();
	}


	public String toString() {
		return getDisplayName();
	}

}
