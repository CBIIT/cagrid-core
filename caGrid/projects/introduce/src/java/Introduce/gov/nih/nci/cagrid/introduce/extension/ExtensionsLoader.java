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
package gov.nih.nci.cagrid.introduce.extension;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.beans.extension.AuthorizationExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.DeploymentExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.IntroduceGDEExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.Properties;
import gov.nih.nci.cagrid.introduce.beans.extension.PropertiesProperty;
import gov.nih.nci.cagrid.introduce.beans.extension.ResourcePropertyEditorExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.IntroducePropertiesManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.cagrid.grape.ConfigurationManager;
import org.cagrid.grape.model.Application;


public class ExtensionsLoader {
    private static final Logger logger = Logger.getLogger(ExtensionsLoader.class);

    private static ExtensionsLoader loader = null;

    public static final String EXTENSIONS_DIRECTORY = "." + File.separator + "extensions";
    public static final String DISCOVERY_EXTENSION = "DISCOVERY";
    public static final String SERVICE_EXTENSION = "SERVICE";
    public static final String DEPLOYMENT_EXTENSION = "DEPLOYMENT";
    public static final String RESOURCE_PROPERTY_EDITOR_EXTENSION = "RESOURCE_PROPERTY_EDITOR";
    public static final String AUTHORIZATION_EXTENSION = "AUTHORIZATION";
    public static final String INTRODUCE_GDE_EXTENSION = "INTRODUCE_GDE";

    private List<ServiceExtensionDescriptionType> serviceExtensionDescriptors;
    private List<DiscoveryExtensionDescriptionType> discoveryExtensionDescriptors;
    private List<DeploymentExtensionDescriptionType> deploymentExtensionDescriptors;
    private List<ResourcePropertyEditorExtensionDescriptionType> resourcePropertyEditorExtensionDescriptors;
    private List<AuthorizationExtensionDescriptionType> authorizationExtensionDescriptors;
    private List<IntroduceGDEExtensionDescriptionType> introduceGDEExtensionDescriptors;
    private List<ExtensionDescription> extensions;

    private File extensionsDir;


    private ExtensionsLoader() {
        this.extensionsDir = new File(EXTENSIONS_DIRECTORY);
        serviceExtensionDescriptors = new ArrayList<ServiceExtensionDescriptionType>();
        discoveryExtensionDescriptors = new ArrayList<DiscoveryExtensionDescriptionType>();
        resourcePropertyEditorExtensionDescriptors = new ArrayList<ResourcePropertyEditorExtensionDescriptionType>();
        authorizationExtensionDescriptors = new ArrayList<AuthorizationExtensionDescriptionType>();
        deploymentExtensionDescriptors = new ArrayList<DeploymentExtensionDescriptionType>();
        introduceGDEExtensionDescriptors = new ArrayList<IntroduceGDEExtensionDescriptionType>();
        extensions = new ArrayList<ExtensionDescription>();
        try {
            this.load();
        } catch (Exception e) {
            logger.error(e);
        }
    }


    public static ExtensionsLoader getInstance() {
        if (loader == null) {
            loader = new ExtensionsLoader();
        }
        return loader;
    }


    private synchronized void load() throws Exception {
        if (extensionsDir.isDirectory()) {
            // XXX: This should be using ConfigurationUtil but, long story
            // short, this is called from the preinitialization process, but the
            // configurationmanager is not initialized until after the
            // preinitializers are run, and so using the ConfigurationUtil
            // causes it to cache a configurationmanager that is not used by the
            // rest of Introduce.

            Application app = null;
            app = Utils.deserializeDocument(
                IntroducePropertiesManager.getIntroduceConfigurationFile(),
                Application.class);
            ConfigurationManager configurationManager = new ConfigurationManager(app.getConfiguration(), null);

            final File[] dirs = extensionsDir.listFiles();
            for (int i = 0; i < dirs.length; i++) {
                final int count = i;
                if (dirs[i].isDirectory()) {
                    if (new File(dirs[i].getAbsolutePath() + File.separator + "extension.xml").exists()) {

                        logger.info("Loading extension: " + dirs[count].getAbsolutePath() + File.separator
                            + "extension.xml");
                        ExtensionDescription extDesc = null;

                        try {
                            extDesc = Utils.deserializeDocument(new File(dirs[count].getAbsolutePath()
                                + File.separator + "extension.xml").getAbsolutePath(), ExtensionDescription.class);

                            // process the extension properties and add the ones
                            // that are desired to be made global if they do not
                            // exist yet

                            extensions.add(extDesc);

                            if (extDesc.getExtensionType().equals(DISCOVERY_EXTENSION)) {
                                discoveryExtensionDescriptors.add(extDesc.getDiscoveryExtensionDescription());
                                processExtensionProperties(extDesc.getDiscoveryExtensionDescription().getProperties(),
                                    configurationManager);
                            } else if (extDesc.getExtensionType().equals(SERVICE_EXTENSION)) {
                                serviceExtensionDescriptors.add(extDesc.getServiceExtensionDescription());
                                processExtensionProperties(extDesc.getServiceExtensionDescription().getProperties(),
                                    configurationManager);
                            } else if (extDesc.getExtensionType().equals(RESOURCE_PROPERTY_EDITOR_EXTENSION)) {
                                resourcePropertyEditorExtensionDescriptors.add(extDesc
                                    .getResourcePropertyEditorExtensionDescription());
                                processExtensionProperties(extDesc.getResourcePropertyEditorExtensionDescription()
                                    .getProperties(), configurationManager);
                            } else if (extDesc.getExtensionType().equals(AUTHORIZATION_EXTENSION)) {
                                authorizationExtensionDescriptors.add(extDesc.getAuthorizationExtensionDescription());
                                processExtensionProperties(extDesc.getAuthorizationExtensionDescription()
                                    .getProperties(), configurationManager);
                            } else if (extDesc.getExtensionType().equals(DEPLOYMENT_EXTENSION)) {
                                deploymentExtensionDescriptors.add(extDesc.getDeploymentExtensionDescription());
                                processExtensionProperties(extDesc.getDeploymentExtensionDescription().getProperties(),
                                    configurationManager);
                            } else if (extDesc.getExtensionType().equals(INTRODUCE_GDE_EXTENSION)) {
                                introduceGDEExtensionDescriptors.add(extDesc.getIntroduceGDEExtensionDescriptionType());
                                processExtensionProperties(extDesc.getIntroduceGDEExtensionDescriptionType()
                                    .getProperties(), configurationManager);
                            } else {
                                logger.warn("Unsupported Extension Type: " + extDesc.getExtensionType());
                            }
                        } catch (Exception e) {
                            logger.error(e);
                        }
                    }
                }
            }
        }
    }


    private void processExtensionProperties(Properties properties, ConfigurationManager configurationManager)
        throws Exception {
        if (properties != null && properties.getProperty() != null) {
            for (int i = 0; i < properties.getProperty().length; i++) {
                PropertiesProperty prop = properties.getProperty(i);
                try {
                    if (prop != null && prop.getMakeGlobal() != null && prop.getMakeGlobal().booleanValue()) {
                        Properties globalExtensionProperties = (Properties) configurationManager
                            .getConfigurationObject("introduceGlobalExtensionProperties");
                        if (globalExtensionProperties != null && globalExtensionProperties.getProperty() != null) {
                            boolean found = false;
                            for (int propi = 0; propi < globalExtensionProperties.getProperty().length; propi++) {
                                PropertiesProperty checkProp = globalExtensionProperties.getProperty(propi);
                                if (checkProp.getKey().equals(prop.getKey())) {
                                    found = true;
                                }
                            }
                            if (!found) {
                                PropertiesProperty[] props = new PropertiesProperty[globalExtensionProperties
                                    .getProperty().length + 1];
                                System.arraycopy(globalExtensionProperties.getProperty(), 0, props, 0,
                                    globalExtensionProperties.getProperty().length);
                                props[globalExtensionProperties.getProperty().length] = prop;
                                globalExtensionProperties.setProperty(props);
                            }
                        } else if (globalExtensionProperties != null) {
                            PropertiesProperty[] props = new PropertiesProperty[1];
                            props[0] = prop;
                            globalExtensionProperties.setProperty(props);
                        }
                        configurationManager.saveAll();

                    }
                } catch (Exception e) {
                    logger.warn("WARNING: extension property " + prop.getKey() + " could not be processed");
                }
            }
        }
    }


    public List<AuthorizationExtensionDescriptionType> getAuthorizationExtensions() {
        return this.authorizationExtensionDescriptors;
    }


    public List<ServiceExtensionDescriptionType> getServiceExtensions() {
        return this.serviceExtensionDescriptors;
    }


    public List<IntroduceGDEExtensionDescriptionType> getIntroduceGDEExtensions() {
        return this.introduceGDEExtensionDescriptors;
    }


    public List<ResourcePropertyEditorExtensionDescriptionType> getResourcePropertyEditorExtensions() {
        return this.resourcePropertyEditorExtensionDescriptors;
    }


    public ExtensionDescription getExtension(String name) {
        for (int i = 0; i < extensions.size(); i++) {
            ExtensionDescription ex = extensions.get(i);
            if (ex.getDiscoveryExtensionDescription() != null
                && ex.getDiscoveryExtensionDescription().getName().equals(name)) {
                return ex;
            } else if (ex.getServiceExtensionDescription() != null
                && ex.getServiceExtensionDescription().getName().equals(name)) {
                return ex;
            } else if (ex.getResourcePropertyEditorExtensionDescription() != null
                && ex.getResourcePropertyEditorExtensionDescription().getName().equals(name)) {
                return ex;
            } else if (ex.getAuthorizationExtensionDescription() != null
                && ex.getAuthorizationExtensionDescription().getName().equals(name)) {
                return ex;
            } else if (ex.getDeploymentExtensionDescription() != null
                && ex.getDeploymentExtensionDescription().getName().equals(name)) {
                return ex;
            } else if (ex.getIntroduceGDEExtensionDescriptionType() != null
                && ex.getIntroduceGDEExtensionDescriptionType().getName().equals(name)) {
                return ex;
            }
        }
        return null;
    }


    public ServiceExtensionDescriptionType getServiceExtension(String name) {
        for (int i = 0; i < serviceExtensionDescriptors.size(); i++) {
            ServiceExtensionDescriptionType ex = serviceExtensionDescriptors.get(i);
            if (ex.getName().equals(name)) {
                return ex;
            }
        }
        return null;
    }


    public DeploymentExtensionDescriptionType getDeploymentExtension(String name) {
        for (int i = 0; i < deploymentExtensionDescriptors.size(); i++) {
            DeploymentExtensionDescriptionType ex = deploymentExtensionDescriptors.get(i);
            if (ex.getName().equals(name)) {
                return ex;
            }
        }
        return null;
    }


    public AuthorizationExtensionDescriptionType getAuthorizationExtension(String name) {
        for (int i = 0; i < authorizationExtensionDescriptors.size(); i++) {
            AuthorizationExtensionDescriptionType ex = authorizationExtensionDescriptors.get(i);
            if (ex.getName().equals(name)) {
                return ex;
            }
        }
        return null;
    }


    public ResourcePropertyEditorExtensionDescriptionType getResourcePropertyEditorExtension(String name) {
        for (int i = 0; i < resourcePropertyEditorExtensionDescriptors.size(); i++) {
            ResourcePropertyEditorExtensionDescriptionType ex = resourcePropertyEditorExtensionDescriptors.get(i);
            if (ex.getName().equals(name)) {
                return ex;
            }
        }
        return null;
    }


    public IntroduceGDEExtensionDescriptionType getIntroduceGDEExtension(String name) {
        for (int i = 0; i < introduceGDEExtensionDescriptors.size(); i++) {
            IntroduceGDEExtensionDescriptionType ex = introduceGDEExtensionDescriptors.get(i);
            if (ex.getName().equals(name)) {
                return ex;
            }
        }
        return null;
    }


    public ServiceExtensionDescriptionType getServiceExtensionByDisplayName(String displayName) {
        for (int i = 0; i < serviceExtensionDescriptors.size(); i++) {
            ServiceExtensionDescriptionType ex = serviceExtensionDescriptors.get(i);
            if (ex.getDisplayName().equals(displayName)) {
                return ex;
            }
        }
        return null;
    }


    public DiscoveryExtensionDescriptionType getDiscoveryExtension(String name) {
        for (int i = 0; i < discoveryExtensionDescriptors.size(); i++) {
            DiscoveryExtensionDescriptionType ex = discoveryExtensionDescriptors.get(i);
            if (ex.getName().equals(name)) {
                return ex;
            }
        }
        return null;
    }


    public DiscoveryExtensionDescriptionType getDiscoveryExtensionByDisplayName(String displayName) {
        for (int i = 0; i < discoveryExtensionDescriptors.size(); i++) {
            DiscoveryExtensionDescriptionType ex = discoveryExtensionDescriptors.get(i);
            if (ex.getDisplayName().equals(displayName)) {
                return ex;
            }
        }
        return null;
    }


    public AuthorizationExtensionDescriptionType getAuthorizationExtensionByDisplayName(String displayName) {
        for (int i = 0; i < authorizationExtensionDescriptors.size(); i++) {
            AuthorizationExtensionDescriptionType ex = authorizationExtensionDescriptors.get(i);
            if (ex.getDisplayName().equals(displayName)) {
                return ex;
            }
        }
        return null;
    }


    public List<DiscoveryExtensionDescriptionType> getDiscoveryExtensions() {
        return this.discoveryExtensionDescriptors;
    }


    public List<DeploymentExtensionDescriptionType> getDeploymentExtensions() {
        return this.deploymentExtensionDescriptors;
    }


    public File getExtensionsDir() {
        return extensionsDir;
    }
}
