package com.mycompany.ticketingsystem.validation;

import com.mycompany.ticketingsystem.annotation.EmailMustBeUnique;
import com.mycompany.ticketingsystem.model.User;
import com.mycompany.ticketingsystem.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailMustBeUniqueValidator implements ConstraintValidator<EmailMustBeUnique, String> {

    private final UserRepository userRepository;
    private User tempUser;

    public EmailMustBeUniqueValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(EmailMustBeUnique emailMustBeUnique) {
        // Method empty because I could not find use to it.
    }

    @Override
    public boolean isValid(String emailField, ConstraintValidatorContext constraintValidatorContext) {
        tempUser = userRepository.findByEmail(emailField);
        return tempUser == null;
    }
}
