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
package gov.nih.nci.cagrid.introduce.portal.modification.services.servicetree;

import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


public class ResourcesButtonPanel extends ServiceContextsOptionsPanel {

	private JButton addServiceButton = null;


	/**
	 * This method initializes
	 */
	public ResourcesButtonPanel(ServicesJTree tree) {
		super(tree);
		initialize();
	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridx = 0;
		this.setLayout(new GridBagLayout());
		this.add(getAddServiceButton(), gridBagConstraints);
	}


	/**
	 * This method initializes addServiceButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddServiceButton() {
		if (addServiceButton == null) {
			addServiceButton = new JButton();
			addServiceButton.setText("Modify Resource Properties");
			addServiceButton.setIcon(IntroduceLookAndFeel.getModifyResourcePropertiesIcon());
			addServiceButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					DefaultMutableTreeNode tnode = ResourcesButtonPanel.this.getTree().getCurrentNode();
					if (tnode instanceof ResourcePropertiesTypeTreeNode) {
						ResourcePropertiesPopUpMenu.modifyResourceProperties((ResourcePropertiesTypeTreeNode) tnode);
					}
					((DefaultTreeModel) getTree().getModel()).nodeStructureChanged(tnode);
					((DefaultTreeModel) getTree().getModel()).nodeChanged(tnode);
				}

			});
		}
		return addServiceButton;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
