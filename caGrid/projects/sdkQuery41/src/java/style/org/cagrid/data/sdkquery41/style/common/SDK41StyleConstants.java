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
package org.cagrid.data.sdkquery41.style.common;

import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;

import java.io.File;

public interface SDK41StyleConstants {

    public static final String STYLE_NAME = "caCORE SDK v 4.1";
    public static final String STYLE_DIR = ExtensionsLoader.getInstance().getExtensionsDir().getAbsolutePath()
        + File.separator + "data" + File.separator + "styles" + File.separator + "cacore41";
    public static final String DEPLOY_PROPERTIES_FILENAME = "deploy.properties";
    
    public interface DeployProperties {
        public static final String PROJECT_NAME = "PROJECT_NAME";
        public static final String MODEL_FILE = "MODEL_FILE";
        public static final String MODEL_TYPE = "MODEL_FILE_TYPE";
        public static final String MODEL_TYPE_EA = "EA";
        public static final String MODEL_TYPE_ARGO = "ARGO";
        public static final String SERVER_URL = "SERVER_URL";
        public static final String EXCLUDE_PACKAGE = "EXCLUDE_PACKAGE";
    }
}
