package Validation;

public interface Validaton<T> {
    ValidationResult validate(T obj);
}
