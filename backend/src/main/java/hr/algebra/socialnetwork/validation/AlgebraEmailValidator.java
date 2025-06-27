package hr.algebra.socialnetwork.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AlgebraEmailValidator implements ConstraintValidator<AlgebraEmail, String> {

  @Override
  public boolean isValid(String email, ConstraintValidatorContext context) {
    return email != null && email.endsWith("@algebra.hr");
  }
}
