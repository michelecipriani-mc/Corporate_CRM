package com.crm.corporate_crm.security.service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class ValidationRuleService {

    public static Map<String, Object> extractRule(Class<?> c) {
        Map<String, Object> rules = new HashMap<>();

        for (Field field : c.getDeclaredFields()) {
            Map<String, Object> fieldRules = new HashMap<>();

            if (field.isAnnotationPresent(NotBlank.class)) {
                NotBlank nBlank = field.getAnnotation(NotBlank.class);
                fieldRules.put("required", true);
                fieldRules.put("message", nBlank.message());
            }

            if (field.isAnnotationPresent(NotNull.class)) {
                NotNull nNull = field.getAnnotation(NotNull.class);
                fieldRules.put("required", true);
                fieldRules.put("message", nNull.message());
            }

            if (field.isAnnotationPresent(Pattern.class)) {
                Pattern pattern = field.getAnnotation(Pattern.class);
                fieldRules.put("pattern", pattern.regexp());
                fieldRules.put("patternMessage", pattern.message());
            }

            if (field.isAnnotationPresent(Email.class)) {
                Email email = field.getAnnotation(Email.class);
                fieldRules.put("email", true);
                fieldRules.put("emailMessage", email.message());
            }

            if (!fieldRules.isEmpty()) {
                rules.put(field.getName(), fieldRules);
            }
        }

        return rules;
    }
}
