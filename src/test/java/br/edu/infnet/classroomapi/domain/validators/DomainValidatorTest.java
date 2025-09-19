package br.edu.infnet.classroomapi.domain.validators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("DomainValidator Tests")
class DomainValidatorTest {

    @Nested
    @DisplayName("validateNotNull Tests")
    class ValidateNotNullTests {

        @Test
        @DisplayName("Should pass when value is not null")
        void shouldPassWhenValueIsNotNull() {
            String value = "test";
            
            assertDoesNotThrow(() -> DomainValidator.validateNotNull(value, "TestField"));
        }

        @Test
        @DisplayName("Should throw exception when value is null")
        void shouldThrowExceptionWhenValueIsNull() {
            String value = null;
            
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DomainValidator.validateNotNull(value, "TestField")
            );
            
            assertEquals("TestField cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should work with different object types")
        void shouldWorkWithDifferentObjectTypes() {
            // Test with Integer
            assertDoesNotThrow(() -> DomainValidator.validateNotNull(42, "IntegerField"));
            
            // Test with Boolean
            assertDoesNotThrow(() -> DomainValidator.validateNotNull(true, "BooleanField"));
            
            // Test with Object
            Object obj = new Object();
            assertDoesNotThrow(() -> DomainValidator.validateNotNull(obj, "ObjectField"));
        }

        @Test
        @DisplayName("Should throw exception with custom field name")
        void shouldThrowExceptionWithCustomFieldName() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DomainValidator.validateNotNull(null, "CustomFieldName")
            );
            
            assertEquals("CustomFieldName cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should handle empty field name")
        void shouldHandleEmptyFieldName() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DomainValidator.validateNotNull(null, "")
            );
            
            assertEquals(" cannot be null", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("validateNotEmpty Tests")
    class ValidateNotEmptyTests {

        @Test
        @DisplayName("Should pass when string is not empty")
        void shouldPassWhenStringIsNotEmpty() {
            String value = "test";
            
            assertDoesNotThrow(() -> DomainValidator.validateNotEmpty(value, "TestField"));
        }

        @Test
        @DisplayName("Should pass when string has only content")
        void shouldPassWhenStringHasOnlyContent() {
            String value = "a";
            
            assertDoesNotThrow(() -> DomainValidator.validateNotEmpty(value, "TestField"));
        }

        @Test
        @DisplayName("Should throw exception when string is null")
        void shouldThrowExceptionWhenStringIsNull() {
            String value = null;
            
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DomainValidator.validateNotEmpty(value, "TestField")
            );
            
            assertEquals("TestField cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when string is empty")
        void shouldThrowExceptionWhenStringIsEmpty() {
            String value = "";
            
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DomainValidator.validateNotEmpty(value, "TestField")
            );
            
            assertEquals("TestField cannot be empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when string contains only spaces")
        void shouldThrowExceptionWhenStringContainsOnlySpaces() {
            String value = "   ";
            
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DomainValidator.validateNotEmpty(value, "TestField")
            );
            
            assertEquals("TestField cannot be empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when string contains only tabs")
        void shouldThrowExceptionWhenStringContainsOnlyTabs() {
            String value = "\t\t\t";
            
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DomainValidator.validateNotEmpty(value, "TestField")
            );
            
            assertEquals("TestField cannot be empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when string contains mixed whitespace")
        void shouldThrowExceptionWhenStringContainsMixedWhitespace() {
            String value = " \t \n ";
            
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DomainValidator.validateNotEmpty(value, "TestField")
            );
            
            assertEquals("TestField cannot be empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should pass when string has content with surrounding spaces")
        void shouldPassWhenStringHasContentWithSurroundingSpaces() {
            String value = "  test  ";
            
            assertDoesNotThrow(() -> DomainValidator.validateNotEmpty(value, "TestField"));
        }

        @Test
        @DisplayName("Should throw exception with custom field name")
        void shouldThrowExceptionWithCustomFieldName() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DomainValidator.validateNotEmpty("", "UserName")
            );
            
            assertEquals("UserName cannot be empty", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("validateEmail Tests")
    class ValidateEmailTests {

        @Test
        @DisplayName("Should pass with valid email formats")
        void shouldPassWithValidEmailFormats() {
            String[] validEmails = {
                "user@example.com",
                "test.email@domain.org",
                "user+tag@example.net",
                "user_name@example.co.uk",
                "123@example.com",
                "user-name@example-domain.com",
                "a@b.co",
                "very.long.email.address@very.long.domain.name.com"
            };

            for (String email : validEmails) {
                assertDoesNotThrow(() -> DomainValidator.validateEmail(email),
                    "Should accept valid email: " + email);
            }
        }

        @Test
        @DisplayName("Should throw exception when email is null")
        void shouldThrowExceptionWhenEmailIsNull() {
            String email = null;
            
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DomainValidator.validateEmail(email)
            );
            
            assertEquals("Email cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when email is empty")
        void shouldThrowExceptionWhenEmailIsEmpty() {
            String email = "";
            
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DomainValidator.validateEmail(email)
            );
            
            assertEquals("Email cannot be empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when email contains only spaces")
        void shouldThrowExceptionWhenEmailContainsOnlySpaces() {
            String email = "   ";
            
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DomainValidator.validateEmail(email)
            );
            
            assertEquals("Email cannot be empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception with invalid email formats")
        void shouldThrowExceptionWithInvalidEmailFormats() {
            String[] invalidEmails = {
                "plainaddress",
                "@missinglocal.com", 
                "missing-at-sign.com",
                "missing.domain@",
                "email@",
                "@"
            };

            for (String email : invalidEmails) {
                IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> DomainValidator.validateEmail(email),
                    "Should reject invalid email: " + email
                );
                
                assertEquals("Invalid email format", exception.getMessage());
            }
        }

        @Test
        @DisplayName("Should handle edge cases")
        void shouldHandleEdgeCases() {
            // Single character local part
            assertDoesNotThrow(() -> DomainValidator.validateEmail("a@example.com"));
            
            // Numbers in email
            assertDoesNotThrow(() -> DomainValidator.validateEmail("123456@example.com"));
            
            // Special characters
            assertDoesNotThrow(() -> DomainValidator.validateEmail("user+tag@example.com"));
            assertDoesNotThrow(() -> DomainValidator.validateEmail("user_name@example.com"));
            assertDoesNotThrow(() -> DomainValidator.validateEmail("user-name@example.com"));
            assertDoesNotThrow(() -> DomainValidator.validateEmail("user.name@example.com"));
        }
    }

    @Nested
    @DisplayName("validatePositive Tests")
    class ValidatePositiveTests {

        @Test
        @DisplayName("Should pass when value is positive")
        void shouldPassWhenValueIsPositive() {
            Integer value = 1;
            
            assertDoesNotThrow(() -> DomainValidator.validatePositive(value, "TestField"));
        }

        @Test
        @DisplayName("Should pass with large positive values")
        void shouldPassWithLargePositiveValues() {
            Integer[] positiveValues = {1, 10, 100, 1000, Integer.MAX_VALUE};

            for (Integer value : positiveValues) {
                assertDoesNotThrow(() -> DomainValidator.validatePositive(value, "TestField"),
                    "Should accept positive value: " + value);
            }
        }

        @Test
        @DisplayName("Should throw exception when value is null")
        void shouldThrowExceptionWhenValueIsNull() {
            Integer value = null;
            
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DomainValidator.validatePositive(value, "TestField")
            );
            
            assertEquals("TestField cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when value is zero")
        void shouldThrowExceptionWhenValueIsZero() {
            Integer value = 0;
            
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DomainValidator.validatePositive(value, "TestField")
            );
            
            assertEquals("TestField must be positive", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when value is negative")
        void shouldThrowExceptionWhenValueIsNegative() {
            Integer[] negativeValues = {-1, -10, -100, Integer.MIN_VALUE};

            for (Integer value : negativeValues) {
                IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> DomainValidator.validatePositive(value, "TestField"),
                    "Should reject negative value: " + value
                );
                
                assertEquals("TestField must be positive", exception.getMessage());
            }
        }

        @Test
        @DisplayName("Should throw exception with custom field name")
        void shouldThrowExceptionWithCustomFieldName() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DomainValidator.validatePositive(0, "Age")
            );
            
            assertEquals("Age must be positive", exception.getMessage());
        }

        @Test
        @DisplayName("Should handle different field names consistently")
        void shouldHandleDifferentFieldNamesConsistently() {
            String[] fieldNames = {"Id", "Count", "Size", "Quantity", "Amount"};

            for (String fieldName : fieldNames) {
                IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> DomainValidator.validatePositive(-1, fieldName)
                );
                
                assertEquals(fieldName + " must be positive", exception.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should validate multiple fields in sequence")
        void shouldValidateMultipleFieldsInSequence() {
            String name = "John Doe";
            String email = "john@example.com";
            Integer age = 25;

            assertDoesNotThrow(() -> {
                DomainValidator.validateNotEmpty(name, "Name");
                DomainValidator.validateEmail(email);
                DomainValidator.validatePositive(age, "Age");
            });
        }

        @Test
        @DisplayName("Should fail at first invalid field in sequence")
        void shouldFailAtFirstInvalidFieldInSequence() {
            String name = "";  // Invalid
            String email = "john@example.com";
            Integer age = 25;

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    DomainValidator.validateNotEmpty(name, "Name");
                    DomainValidator.validateEmail(email);
                    DomainValidator.validatePositive(age, "Age");
                }
            );
            
            assertEquals("Name cannot be empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should validate complex scenarios")
        void shouldValidateComplexScenarios() {
            // Valid scenario
            assertDoesNotThrow(() -> {
                DomainValidator.validateNotNull("value", "Field");
                DomainValidator.validateNotEmpty("non-empty", "Field");
                DomainValidator.validateEmail("test@example.com");
                DomainValidator.validatePositive(100, "Field");
            });

            // Invalid email in sequence
            assertThrows(IllegalArgumentException.class, () -> {
                DomainValidator.validateNotNull("value", "Field");
                DomainValidator.validateNotEmpty("non-empty", "Field");
                DomainValidator.validateEmail("invalid-email");
                DomainValidator.validatePositive(100, "Field");
            });
        }

        @Test
        @DisplayName("Should handle real-world validation patterns")
        void shouldHandleRealWorldValidationPatterns() {
            // Simulate Student validation
            assertDoesNotThrow(() -> {
                String studentName = "Alice Johnson";
                String studentEmail = "alice.johnson@university.edu";
                
                DomainValidator.validateNotEmpty(studentName, "Student name");
                DomainValidator.validateEmail(studentEmail);
            });

            // Simulate Subject validation
            assertDoesNotThrow(() -> {
                String subjectName = "Advanced Java Programming";
                String subjectCode = "CS301";
                
                DomainValidator.validateNotEmpty(subjectName, "Subject name");
                DomainValidator.validateNotEmpty(subjectCode, "Subject code");
            });
        }
    }

    @Nested
    @DisplayName("Edge Cases and Boundary Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle minimum positive integer")
        void shouldHandleMinimumPositiveInteger() {
            assertDoesNotThrow(() -> DomainValidator.validatePositive(1, "Field"));
        }

        @Test
        @DisplayName("Should handle maximum integer value")
        void shouldHandleMaximumIntegerValue() {
            assertDoesNotThrow(() -> DomainValidator.validatePositive(Integer.MAX_VALUE, "Field"));
        }

        @Test
        @DisplayName("Should handle very long valid email")
        void shouldHandleVeryLongValidEmail() {
            String longEmail = "very.long.email.address.with.many.dots@very.long.domain.name.with.subdomains.example.com";
            assertDoesNotThrow(() -> DomainValidator.validateEmail(longEmail));
        }

        @Test
        @DisplayName("Should handle single character strings")
        void shouldHandleSingleCharacterStrings() {
            assertDoesNotThrow(() -> DomainValidator.validateNotEmpty("a", "Field"));
        }

        @Test
        @DisplayName("Should handle special characters in field names")
        void shouldHandleSpecialCharactersInFieldNames() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DomainValidator.validateNotNull(null, "Field-Name_With.Special@Characters")
            );
            
            assertEquals("Field-Name_With.Special@Characters cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should handle unicode characters in strings")
        void shouldHandleUnicodeCharactersInStrings() {
            String unicodeString = "José María Ñoño";
            assertDoesNotThrow(() -> DomainValidator.validateNotEmpty(unicodeString, "Name"));
        }
    }
}
