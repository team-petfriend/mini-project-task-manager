package com.example.petfriend.common.errors;

public record FieldErrorItem(
        String field,
        String rejected,
        String message
) {
}
