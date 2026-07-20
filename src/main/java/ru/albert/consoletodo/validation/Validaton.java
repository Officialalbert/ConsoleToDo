package ru.albert.consoletodo.validation;

public interface Validaton<T> {
    ValidationResult validate(T obj);
}