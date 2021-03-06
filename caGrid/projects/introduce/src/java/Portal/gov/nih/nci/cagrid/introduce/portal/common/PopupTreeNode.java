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
package gov.nih.nci.cagrid.introduce.portal.common;

import javax.swing.JPopupMenu;

/** 
 *  PopupTreeNode
 *  TODO:DOCUMENT ME
 * 
 * @author David Ervin
 * 
 * @created May 3, 2007 4:25:16 PM
 * @version $Id: PopupTreeNode.java,v 1.1 2007-05-04 13:48:45 dervin Exp $ 
 */
public interface PopupTreeNode {
    
    public JPopupMenu getPopUpMenu();
}
