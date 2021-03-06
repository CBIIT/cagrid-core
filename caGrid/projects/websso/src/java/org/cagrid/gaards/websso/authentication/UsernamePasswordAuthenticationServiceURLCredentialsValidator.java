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
package org.cagrid.gaards.websso.authentication;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * A validator to check if UserNamePasswordCredentials is valid.
 * 
 * @author Scott Battaglia
 * @version $Revision: 1.6 $ $Date: 2009-01-31 00:03:07 $
 * @since 3.0
 */
public final class UsernamePasswordAuthenticationServiceURLCredentialsValidator implements Validator {

    @SuppressWarnings("rawtypes")
	public boolean supports(final Class clazz) {
        return UsernamePasswordAuthenticationServiceURLCredentials.class.isAssignableFrom(clazz);
    }

    public void validate(final Object o, final Errors errors) {
    	UsernamePasswordAuthenticationServiceURLCredentials userNamePasswordCredentials = (UsernamePasswordAuthenticationServiceURLCredentials) o;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username","required.username");
		if (UsernamePasswordAuthenticationServiceURLCredentials.BASIC_AUTHENTICATION
				.equals(userNamePasswordCredentials.getAuthenticationServiceProfile())) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password","required.password");
		}
		if (UsernamePasswordAuthenticationServiceURLCredentials.ONE_TIME_PASSWORD
				.equals(userNamePasswordCredentials.getAuthenticationServiceProfile())) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "onetimepassword", "required.password");
		}
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dorianName","required.dorian.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors,
				"authenticationServiceURL", "required.authentication.service");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors,
				"authenticationServiceProfile","required.authentication.profile");

		if (userNamePasswordCredentials.getDorianName().equals("-")) {
			errors.rejectValue("dorianName",
					"error.authentication.dorian.name.incorrect", null);
		}
		if (userNamePasswordCredentials.getAuthenticationServiceURL().equals(
				"-")) {
			errors.rejectValue("authenticationServiceURL",
					"error.authentication.service.incorrect", null);
		}
		if (userNamePasswordCredentials.getAuthenticationServiceProfile()!=null &&				
			userNamePasswordCredentials.getAuthenticationServiceProfile().equals("-")) {
			errors.rejectValue("authenticationServiceProfile",
					"error.authentication.profile.incorrect", null);
		}
	}
}
