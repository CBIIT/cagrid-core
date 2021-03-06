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
package org.cagrid.grape.table;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreeCellEditor;


/**
 * 
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @created Oct 14, 2004
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GrapeTableCellEditor implements TableCellEditor, TreeCellEditor, Serializable {

    protected EventListenerList listenerList = new EventListenerList();

    transient protected ChangeEvent changeEvent = null;

    protected JComponent editorComponent = null;

    protected JComponent container = null; // Can be tree or table


    public GrapeTableCellEditor() {
        super();
    }


    public Component getComponent() {
        return editorComponent;
    }


    public Object getCellEditorValue() {
        return editorComponent;
    }


    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }


    public boolean shouldSelectCell(EventObject anEvent) {
        if (editorComponent != null && anEvent instanceof MouseEvent
            && ((MouseEvent) anEvent).getID() == MouseEvent.MOUSE_PRESSED) {
            Component dispatchComponent = SwingUtilities.getDeepestComponentAt(editorComponent, 3, 3);
            MouseEvent e = (MouseEvent) anEvent;
            MouseEvent e2 = new MouseEvent(dispatchComponent, MouseEvent.MOUSE_RELEASED, e.getWhen() + 100000, e
                .getModifiers(), 3, 3, e.getClickCount(), e.isPopupTrigger());
            dispatchComponent.dispatchEvent(e2);
            e2 = new MouseEvent(dispatchComponent, MouseEvent.MOUSE_CLICKED, e.getWhen() + 100001, e.getModifiers(), 3,
                3, 1, e.isPopupTrigger());
            dispatchComponent.dispatchEvent(e2);
        }
        return false;
    }


    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }


    public void cancelCellEditing() {
        fireEditingCanceled();
    }


    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }


    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }


    protected void fireEditingStopped() {
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                try {
                    ((CellEditorListener) listeners[i + 1]).editingStopped(changeEvent);
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
        }
    }


    protected void fireEditingCanceled() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((CellEditorListener) listeners[i + 1]).editingCanceled(changeEvent);
            }
        }
    }


    // implements javax.swing.tree.TreeCellEditor
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded,
        boolean leaf, int row) {

        editorComponent = (JComponent) value;
        container = tree;
        return editorComponent;
    }


    // implements javax.swing.table.TableCellEditor
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        editorComponent = (JComponent) value;
        container = table;
        return editorComponent;
    }

} // End of class JComponentCellEditor
