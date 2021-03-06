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
 * GroupLogicalOperator.java
 * 
 * This file was auto-generated from WSDL by the Apache Axis 1.2RC2 Apr 28, 2006
 * (12:42:00 EDT) WSDL2Java emitter.
 */

package org.cagrid.cql2;

public class GroupLogicalOperator implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap<String, GroupLogicalOperator> _table_ = new java.util.HashMap<String, GroupLogicalOperator>();

    public static final java.lang.String _AND = "AND";
    public static final java.lang.String _OR = "OR";
    public static final GroupLogicalOperator AND = new GroupLogicalOperator(_AND);
    public static final GroupLogicalOperator OR = new GroupLogicalOperator(_OR);


    private GroupLogicalOperator(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_, this);
    }


    public java.lang.String getValue() {
        return _value_;
    }
    

    // for castor per http://www.castor.org/how-to-map-enums.html
    public static GroupLogicalOperator valueOf(String value) throws IllegalArgumentException {
        return fromValue(value);
    }


    public static GroupLogicalOperator fromValue(java.lang.String value) throws java.lang.IllegalArgumentException {
        GroupLogicalOperator enumeration = _table_.get(value);
        if (enumeration == null)
            throw new java.lang.IllegalArgumentException();
        return enumeration;
    }


    public static GroupLogicalOperator fromString(java.lang.String value) throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }


    public boolean equals(java.lang.Object obj) {
        return (obj == this);
    }


    public int hashCode() {
        return toString().hashCode();
    }


    public java.lang.String toString() {
        return _value_;
    }
}
