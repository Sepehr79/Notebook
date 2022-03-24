package com.kucess.notebook.bussiness.validation;

import com.kucess.notebook.model.repo.PersonRepo;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueUserName, String> {

    private final PersonRepo personRepo;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !personRepo.existsByUserName(value);
    }
}
