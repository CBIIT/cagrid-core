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
package gov.nih.nci.cagrid.graph.uml.layout;

import java.awt.Dimension;
import java.awt.Point;


/**
 * This interface has to be implemented by layouted nodes in
 * diagrams (i.e. classes or interfaces in a classdiagram).
 */
public interface LayoutedNode extends LayoutedObject {

    /**
     * Operation getSize returns the size of this node.
     *
     * @return The size of this node.
     */
    Dimension getSize();

    /**
     * Operation getLocation returns the current location of
     * this node.
     *
     * @return The location of this node.
     */
    Point getLocation();

    /**
     * Operation setLocation sets a new location for this
     * node.
     *
     * @param newLocation represents the new location for this node.
     */
    void setLocation(Point newLocation);
}
