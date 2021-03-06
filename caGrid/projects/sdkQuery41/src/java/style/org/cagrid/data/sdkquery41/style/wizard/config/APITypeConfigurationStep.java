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
package org.cagrid.data.sdkquery41.style.wizard.config;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import java.io.File;

import org.cagrid.data.sdkquery41.processor.SDK41QueryProcessor;
import org.cagrid.data.sdkquery41.processor2.SDK41CQL2QueryProcessor;

public class APITypeConfigurationStep extends AbstractStyleConfigurationStep {
    
    private ApiType apiType;
    private String hostname;
    private Integer portNumber;
    private Boolean useHttps;

    public APITypeConfigurationStep(ServiceInformation serviceInfo) {
        super(serviceInfo);
    }


    public void applyConfiguration() throws Exception {
        // set service properties
        setCql1ProcessorProperty(SDK41QueryProcessor.PROPERTY_USE_LOCAL_API,
            apiType != null ? String.valueOf(apiType == ApiType.LOCAL_API) : SDK41QueryProcessor.DEFAULT_USE_LOCAL_API, false);
        setCql2ProcessorProperty(SDK41CQL2QueryProcessor.PROPERTY_USE_LOCAL_API,
            apiType != null ? String.valueOf(apiType == ApiType.LOCAL_API) : SDK41QueryProcessor.DEFAULT_USE_LOCAL_API, false);
        setCql1ProcessorProperty(SDK41QueryProcessor.PROPERTY_HOST_NAME,
            hostname != null ? hostname : "", false);
        setCql2ProcessorProperty(SDK41CQL2QueryProcessor.PROPERTY_HOST_NAME,
            hostname != null ? hostname : "", false);
        setCql1ProcessorProperty(
            SDK41QueryProcessor.PROPERTY_HOST_PORT,
            portNumber != null ? String.valueOf(portNumber) : "", false);
        setCql2ProcessorProperty(
            SDK41CQL2QueryProcessor.PROPERTY_HOST_PORT,
            portNumber != null ? String.valueOf(portNumber) : "", false);
        setCql1ProcessorProperty(
            SDK41QueryProcessor.PROPERTY_HOST_HTTPS,
            useHttps != null ? String.valueOf(useHttps) : SDK41QueryProcessor.DEFAULT_HOST_HTTPS, false);
        setCql2ProcessorProperty(
            SDK41CQL2QueryProcessor.PROPERTY_HOST_HTTPS,
            useHttps != null ? String.valueOf(useHttps) : SDK41QueryProcessor.DEFAULT_HOST_HTTPS, false);
        // copy the right config jar into the service's lib dir
        File libDir = new File(getServiceInformation().getBaseDirectory(), "lib");
        File configJar = getApiType() == ApiType.LOCAL_API ? 
            SharedConfiguration.getInstance().getLocalConfigJarFile() : 
                SharedConfiguration.getInstance().getRemoteConfigJarFile();
        Utils.copyFile(configJar, new File(libDir, configJar.getName()));
        // if other jar exists, delete it
        File otherConfigJar = getApiType() == ApiType.LOCAL_API ?
            SharedConfiguration.getInstance().getRemoteConfigJarFile() :
                SharedConfiguration.getInstance().getLocalConfigJarFile();
        File maybeCopiedOtherConfigJar = new File(libDir, otherConfigJar.getName());
        if (maybeCopiedOtherConfigJar.exists()) {
            maybeCopiedOtherConfigJar.delete();
        }
    }
    
        
    public void setApiType(ApiType apiType) {
        this.apiType = apiType;
    }
    
    
    public ApiType getApiType() {
        return apiType;
    }


    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
    
    
    public String getHostname() {
        return hostname;
    }


    public void setPortNumber(Integer portNumber) {
        this.portNumber = portNumber;
    }
    
    
    public Integer getPortNumber() {
        return portNumber;
    }


    public void setUseHttps(Boolean useHttps) {
        this.useHttps = useHttps;
    }
    
    
    public Boolean getUseHttps() {
        return useHttps;
    }
    
    
    public static enum ApiType {
        LOCAL_API, REMOTE_API
    }
}
