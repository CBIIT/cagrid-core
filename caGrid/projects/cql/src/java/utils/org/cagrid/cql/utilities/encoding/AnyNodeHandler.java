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
package org.cagrid.cql.utilities.encoding;

import org.cagrid.cql.utilities.AnyNodeHelper;
import org.exolab.castor.mapping.GeneralizedFieldHandler;
import org.exolab.castor.types.AnyNode;

public class AnyNodeHandler extends GeneralizedFieldHandler {

    public AnyNodeHandler() {
        super();
    }
    
    
    public Object convertUponGet(Object value) {
        if (value == null) return null;
        // String converted = AnyNodeHelper.convertAnyNodeToString((AnyNode) value);
        // return converted;
        return value;
    }


    public Object convertUponSet(Object value) {
        AnyNode node = null;
        if (value instanceof AnyNode) {
            node = (AnyNode) value;
        } else {
            try {
                node = AnyNodeHelper.convertStringToAnyNode((String) value);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return node;
    }


    public Class<?> getFieldType() {
        return AnyNode.class;
    }

}
