package ru.albert.consoletodo.validation;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    private final ArrayList<String> errors = new ArrayList<>();

    public boolean isValid(){
        return errors.isEmpty();
    }

    public void addError(String code){
        errors.add(code);
    }

    public List<String> getErrors(){
        return new ArrayList<>(errors);
    }
}