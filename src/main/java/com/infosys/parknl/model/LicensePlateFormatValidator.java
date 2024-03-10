package com.infosys.parknl.model;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {LicensePlateFormatValidator.Validator.class})
public @interface LicensePlateFormatValidator {

    String message() default "Invalid License Number ";

    class Validator implements ConstraintValidator<LicensePlateFormatValidator, String> {
        private static final String LICENSE_REGEX = "[A-Z][A-Z]([A-Z]|\\d)\\d\\d";
        private String message;

        @Override
        public void initialize(LicensePlateFormatValidator requiredIfChecked) {
            this.message = requiredIfChecked.message();
        }

        public boolean isValid(String value, ConstraintValidatorContext context) {
            boolean valid = this.isValidLicenseNumber(value);

            if (!Boolean.TRUE.equals(valid)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message + value).addConstraintViolation();
            }
            return valid;
        }

        private boolean isValidLicenseNumber(String licenseNumber) {
            if (licenseNumber == null) {
                return false;
            }
            Matcher m = Pattern.compile(LICENSE_REGEX).matcher(licenseNumber);
            if (m.find()) {
                System.out.println(licenseNumber + " is a valid number plate");
                return true;
            } else {
                System.out.println(licenseNumber + " is not a valid number plate");
                return false;
            }
        }
    }
}