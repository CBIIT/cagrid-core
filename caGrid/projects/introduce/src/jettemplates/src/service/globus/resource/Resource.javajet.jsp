<%@ page package="gov.nih.nci.cagrid.introduce.templates.service.globus.resource.lifetime" class="LifetimeResourceTemplate" import="gov.nih.nci.cagrid.introduce.common.*,gov.nih.nci.cagrid.introduce.codegen.utils.*,gov.nih.nci.cagrid.introduce.codegen.*,gov.nih.nci.cagrid.introduce.beans.namespace.*,java.util.*,gov.nih.nci.cagrid.introduce.beans.resource.*"%>
<%  gov.nih.nci.cagrid.introduce.common.SpecificServiceInformation arguments = (gov.nih.nci.cagrid.introduce.common.SpecificServiceInformation) argument; 
  	Properties properties = arguments.getIntroduceServiceProperties();
	ResourcePropertiesListType metadataList = arguments.getService().getResourcePropertiesList();
	String serviceName = properties.getProperty(gov.nih.nci.cagrid.introduce.IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME);
	String modifiedServiceName = serviceName;
	if(serviceName.endsWith("Service")){
		modifiedServiceName = serviceName.substring(0,serviceName.length()-"Service".length());
	}
%>
package <%=arguments.getService().getPackageName()%>.service.globus.resource;

import org.globus.wsrf.InvalidResourceKeyException;
import org.globus.wsrf.NoSuchResourceException;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceKey;


/** 
 * The implementation of this <%=arguments.getService().getName()%>Resource type.
 * 
 * @created by Introduce Toolkit version <%=IntroducePropertiesManager.getIntroduceVersion()%>
 * 
 */
public class <%=arguments.getService().getName()%>Resource extends <%=arguments.getService().getName()%>ResourceBase {

}
