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
package gov.nih.nci.cagrid.sdkquery4.processor;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.XMLUtilities;
import gov.nih.nci.cagrid.data.utilities.DomainModelUtils;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.sdkquery4.encoding.SDK40EncodingUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.axis.utils.ClassUtils;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;
import org.jdom.filter.Filter;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * Implementation of the class discriminator resolver which makes use
 * of information in the service's domain model and the SDK-generated
 * HBM files to determine class discriminator values.
 * 
 * HBM documents are assumed to be available on the classpath.
 * 
 * @author David
 */
public class HBMClassDiscriminatorResolver implements ClassDiscriminatorResolver {
    // HBM DTD
    public static final String HBM_DTD = "org/hibernate/hibernate-mapping-3.0.dtd";
    public static final String HBM_DTD_ENTITY = "-//Hibernate/Hibernate Mapping DTD 3.0//EN";
    
    private DomainModel model = null;
    private Map<String, Object> discriminators = null;
    private Filter discriminatorFilter = null;
    private Filter joinedSubclassFilter = null;
    private EntityResolver dtdResolver = null;
    
    public HBMClassDiscriminatorResolver(DomainModel model) {
        this.model = model;
        this.discriminators = new HashMap<String, Object>();
        // filter for discriminator elements
        this.discriminatorFilter = new ElementFilter("discriminator");
        // filter for joined-subclass elements
        this.joinedSubclassFilter = new ElementFilter("joined-subclass");
    }
    

    public Object getClassDiscriminatorValue(String classname) throws Exception {
        Object identifier = discriminators.get(classname);
        if (identifier == null) {
            String baseClassName = getBaseClassName(classname);
            Element hbmElem = getHbmElement(baseClassName);
            
            // if there's a discriminator element, we're using a String
            String shortClassname = getShortClassName(classname);
            Iterator<?> discriminatorElements = hbmElem.getDescendants(discriminatorFilter);
            if (discriminatorElements.hasNext()) {
                identifier = shortClassname;
            } else {
                // using an integer, but we need to know which one
                Element classElem = hbmElem.getChild("class", hbmElem.getNamespace());
                // TODO: is the search for subclass index ID breadth or depth first?  
                // I'm assuming depth, since I have nothing else to go on
                // JDom's getDescendents() operation works depth first
                Iterator<?> subclassIter = classElem.getDescendants(joinedSubclassFilter);
                int index = 0;
                while (subclassIter.hasNext()) {
                    index++; // first subclass is index 1, so this is fine
                    Element subclassElem = (Element) subclassIter.next();
                    String name = subclassElem.getAttributeValue("name");
                    if (shortClassname.equals(name)) {
                        identifier = Integer.valueOf(index);
                        break;
                    }
                }
                
                // handle the case of checking the base class
                if (identifier == null && baseClassName.equals(classname)) {
                    identifier = Integer.valueOf(0);
                }
            }
            
            discriminators.put(classname, identifier);
        }
        return identifier;
    }
    
    
    private String getBaseClassName(String classname) {
        UMLClass[] baseClasses = DomainModelUtils.getAllSuperclasses(model, classname);
        String topLevelClass = classname;
        for (UMLClass c : baseClasses) {
            String fqClassname = getFullyQualifiedClassName(c);
            if (DomainModelUtils.getAllSuperclasses(model, fqClassname).length == 0) {
                topLevelClass = fqClassname;
                break;
            }
        }
        return topLevelClass;
    }
    
        
    private Element getHbmElement(String className) throws Exception {
        // load the HBM XML document from the classpath
        String hbmResourceName = getHbmResourceName(className);
        InputStream hbmStream = Thread.currentThread().getContextClassLoader()
            .getResourceAsStream(hbmResourceName);
        Element hbmElem = XMLUtilities.streamToDocument(hbmStream, getDtdResolver()).getRootElement();
        hbmStream.close();
        return hbmElem;
    }
    
    
    private String getHbmResourceName(String className) {
        String slashified = className.replace('.', '/');
        return slashified + ".hbm.xml";
    }
    
    
    private String getShortClassName(String className) {
        int dotIndex = className.lastIndexOf('.');
        return className.substring(dotIndex + 1);
    }
    
    
    private String getFullyQualifiedClassName(UMLClass clazz) {
        String name = clazz.getClassName();
        if (clazz.getPackageName() != null && clazz.getPackageName().length() != 0) {
            name = clazz.getPackageName() + "." + name;
        }
        return name;
    }
    
   
    protected EntityResolver getDtdResolver() {
        if (dtdResolver == null) {
            // simple entity resolver to load the castor dtd from the class
            // loader
            dtdResolver = new EntityResolver() {
                public InputSource resolveEntity(String publicId, String systemId) throws IOException {
                    if (publicId.equals(HBM_DTD_ENTITY)) {
                        InputStream stream = ClassUtils.getResourceAsStream(SDK40EncodingUtils.class, HBM_DTD);
                        String text = Utils.inputStreamToStringBuffer(stream).toString();
                        stream.close();
                        return new InputSource(new StringReader(text));
                    }
                    return null;
                }
            };
        }
        return dtdResolver;
    }
}
