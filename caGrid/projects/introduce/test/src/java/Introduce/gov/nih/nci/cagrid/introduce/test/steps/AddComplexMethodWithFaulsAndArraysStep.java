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
package gov.nih.nci.cagrid.introduce.test.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptions;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptionsException;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.codegen.SyncTools;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;

import java.io.File;

import javax.xml.namespace.QName;


public class AddComplexMethodWithFaulsAndArraysStep extends BaseStep {
	private TestCaseInfo tci;

	private String methodName;


	public AddComplexMethodWithFaulsAndArraysStep(TestCaseInfo tci, String methodName, boolean build) throws Exception{
		super(tci.getDir(),build);
		this.tci = tci;
		this.methodName = methodName;
	}


	public void runStep() throws Throwable {
		System.out.println("Adding a complex method with fault.");


		ServiceDescription introService = (ServiceDescription) Utils.deserializeDocument(getBaseDir() + File.separator
			+ tci.getDir() + File.separator + "introduce.xml", ServiceDescription.class);
		
		MethodsType methodsType =  CommonTools.getService(introService.getServices(),tci.getName()).getMethods();

		MethodType method = new MethodType();
		method.setName(methodName);

		// set the output
		MethodTypeOutput output = new MethodTypeOutput();
		output.setQName(new QName("gme://projectmobius.org/1/BookStore","Book"));
		output.setIsArray(true);

		// set some parameters
		MethodTypeInputs inputs = new MethodTypeInputs();
		MethodTypeInputsInput[] inputsArray = new MethodTypeInputsInput[1];
		MethodTypeInputsInput input = new MethodTypeInputsInput();
		input.setName("inputOne");
		input.setIsArray(true);
		input.setQName(new QName("gme://projectmobius.org/1/BookStore","Book"));
		input.setIsArray(true);
		inputsArray[0] = input;
		inputs.setInput(inputsArray);
		method.setInputs(inputs);

		// set a fault
		MethodTypeExceptionsException[] exceptionsArray = new MethodTypeExceptionsException[2];
		MethodTypeExceptionsException exception1 = new MethodTypeExceptionsException();
		exception1.setName("testFault1");
		MethodTypeExceptionsException exception2 = new MethodTypeExceptionsException();
		exception2.setName("testFault2");
		exceptionsArray[0] = exception1;
		exceptionsArray[1] = exception2;
		MethodTypeExceptions exceptions = new MethodTypeExceptions();
		exceptions.setException(exceptionsArray);
		method.setExceptions(exceptions);
		method.setOutput(output);

		// add new method to array in bean
		// this seems to be a wierd way be adding things....
		MethodType[] newMethods;
		int newLength = 0;
		if (methodsType.getMethod() != null) {
			newLength = methodsType.getMethod().length + 1;
			newMethods = new MethodType[newLength];
			System.arraycopy(methodsType.getMethod(), 0, newMethods, 0, methodsType.getMethod().length);
		} else {
			newLength = 1;
			newMethods = new MethodType[newLength];
		}
		newMethods[newLength - 1] = method;
		methodsType.setMethod(newMethods);

		Utils.serializeDocument(getBaseDir() + File.separator + tci.getDir() + File.separator + "introduce.xml",
			introService, IntroduceConstants.INTRODUCE_SKELETON_QNAME);
		
		try {
			SyncTools sync = new SyncTools(new File(getBaseDir()+ File.separator + tci.getDir()));
			sync.sync();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		// look at the interface to make sure method exists.......
		String serviceInterface = getBaseDir() + File.separator + tci.getDir() + File.separator + "src" + File.separator
			+ tci.getPackageDir()+ File.separator + File.separator + "common" + File.separator + tci.getName() + "I.java";
		assertTrue(StepTools.methodExists(serviceInterface, methodName));

		buildStep();
	}
}
