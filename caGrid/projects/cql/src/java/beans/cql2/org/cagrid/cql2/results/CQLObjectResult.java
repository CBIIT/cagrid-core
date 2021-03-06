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
/**
 * CQLObjectResult.java
 * 
 * This file was auto-generated from WSDL by the Apache Axis 1.2RC2 Apr 28, 2006
 * (12:42:00 EDT) WSDL2Java emitter.
 */

package org.cagrid.cql2.results;

// import org.apache.axis.message.MessageElement;
// import org.cagrid.cql.utilities.AnyNodeHelper;
import org.exolab.castor.types.AnyNode;


/**
 * Result object
 */
public class CQLObjectResult extends org.cagrid.cql2.results.CQLResult implements java.io.Serializable {
    private AnyNode _any;


    public CQLObjectResult() {
    }


    public CQLObjectResult(AnyNode _any) {
        this._any = _any;
    }


    /**
     * Gets the _any value for this CQLObjectResult.
     * 
     * @return _any
     */
    public AnyNode get_any() {
        return _any;
    }


    /**
     * Sets the _any value for this CQLObjectResult.
     * 
     * @param _any
     */
    public void set_any(AnyNode _any) {
        this._any = _any;
    }


    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CQLObjectResult))
            return false;
        CQLObjectResult other = (CQLObjectResult) obj;
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        boolean _equals;
        _equals = super.equals(obj) && anysEqual(other);
        return _equals;
    }


    public synchronized int hashCode() {
        int _hashCode = super.hashCode();
        if (get_any() != null) {
            _hashCode += get_any().hashCode();
        }
        return _hashCode;
    }
    
    
    private boolean anysEqual(CQLObjectResult other) {
        AnyNode oAny = other.get_any();
        if (this._any == null && oAny == null) {
            return true;
        } else if ((this._any != null && oAny == null)
            || (this._any == null && oAny != null)) {
            return false;
        } else {
            try {
                //MessageElement myAnyElement = AnyNodeHelper.convertAnyNodeToMessageElement(_any);
                // MessageElement theirAnyElement = AnyNodeHelper.convertAnyNodeToMessageElement(oAny);
                // return myAnyElement.equals(theirAnyElement);
                // HACK:
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }
    }
}
