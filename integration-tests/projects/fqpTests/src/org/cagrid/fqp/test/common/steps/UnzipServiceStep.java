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
package org.cagrid.fqp.test.common.steps;

import java.io.File;

import gov.nih.nci.cagrid.common.ZipUtilities;
import gov.nih.nci.cagrid.testing.system.haste.Step;

/** 
 *  UnzipServiceStep
 *  Unzips a service to the specified directory
 * 
 * @author David Ervin
 * 
 * @created Jul 9, 2008 2:50:01 PM
 * @version $Id: UnzipServiceStep.java,v 1.1 2008-07-09 21:04:08 dervin Exp $ 
 */
public class UnzipServiceStep extends Step {
    
    private File serviceZip;
    private File outputDir;
    
    public UnzipServiceStep(File serviceZip, File outputDir) {
        this.serviceZip = serviceZip;
        this.outputDir = outputDir;
    }
    

    public void runStep() throws Throwable {
        ZipUtilities.unzip(serviceZip, outputDir);
    }
}
