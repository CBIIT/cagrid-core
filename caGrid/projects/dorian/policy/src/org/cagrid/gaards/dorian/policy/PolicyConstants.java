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
package org.cagrid.gaards.dorian.policy;

public class PolicyConstants {

    /** XML core namespace */
    public final static String XML_NS = "http://www.w3.org/XML/1998/namespace";

    /** XML namespace for xmlns attributes */
    public final static String XMLNS_NS = "http://www.w3.org/2000/xmlns/";

    /** XML Schema Instance namespace */
    public final static String XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";

    /** XML Schema Instance namespace */
    public final static String XSD_NS = "http://www.w3.org/2001/XMLSchema";

    /** XML Signature namespace */
    public final static String XMLSIG_NS = "http://www.w3.org/2000/09/xmldsig#";

    /** XML core schema identifier */
    public final static String XML_SCHEMA_ID = "xml.xsd";

    /** XML Signature Schema Identifier */
    public final static String XMLSIG_SCHEMA_ID = "xmldsig-core-schema.xsd";

    public final static String HOST_AGREEMENT_ID = "dorian-host-agreement.xsd";

    public static String HOST_AGREEMENT_NS = "http://www.cagrid.org/gaards/1/dorian-host-agreement";
    public static String HOST_AGREEMENT_ELEMENT = "HostAgreement";
    public static String NAME_ELEMENT = "name";
    public static String INCLUSIVE_NAMESPACES = "#default policy ds xsd xsi code kind rw typens";

}
