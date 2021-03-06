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
package org.cagrid.identifiers.test.system.steps;

import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.io.IOException;

import org.apache.axis.types.URI.MalformedURIException;
import org.cagrid.identifiers.test.system.IdentifiersTestInfo;


public class CopyNamingAuthorityConfigStep extends Step {

    private IdentifiersTestInfo testInfo;


    public CopyNamingAuthorityConfigStep(IdentifiersTestInfo info) {
        this.testInfo = info;
    }


    public String buildNAConfigStr() throws MalformedURIException {
        StringBuffer sb = new StringBuffer();
        sb
            .append("#written by the copy naming authority config step")
            .append("\ncagrid.na.prefix=").append(testInfo.getNAPrefix())
            .append("\ncagrid.na.schemauri=http://na.cagrid.org/org.cagrid.identifiers.namingauthority.xsd")
            .append("\ncagrid.na.grid.url=").append(testInfo.getGridSvcURL())
            .append("\ncagrid.na.db.dialect=org.hibernate.dialect.MySQL5InnoDBDialect")
            .append("\ncagrid.na.db.driver=com.mysql.jdbc.Driver").append("\ncagrid.na.db.name=nainttestdb")
            .append("\ncagrid.na.db.url=jdbc:mysql://localhost:3306/${cagrid.na.db.name}")
            .append("\ncagrid.na.db.username=root").append("\ncagrid.na.db.password=");

        return sb.toString();
    }


    public void writeNAProperties(String props, File toFile) throws IOException {
        Writer output = new BufferedWriter(new FileWriter(toFile));
        output.write(props);
        output.close();
    }


    @Override
    public void runStep() throws MalformedURIException {
        String configStr = buildNAConfigStr();
        File webAppPropertiesFile = new File(IdentifiersTestInfo.WEBAPP_NA_PROPERTIES);
        File gridSvcPropertiesFile = new File(IdentifiersTestInfo.GRIDSVC_NA_PROPERTIES);

        try {
            writeNAProperties(configStr, webAppPropertiesFile);
            writeNAProperties(configStr, gridSvcPropertiesFile);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to write NA properties file:" + e.getMessage());
        }
    }
}
