package org.example.api.hotel.exception.builder;

import net.datafaker.Faker;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidationExceptionMessageBuilderTest {
    private static final ValidationExceptionMessageBuilder VALIDATION_EXCEPTION_MESSAGE_BUILDER =
            new ValidationExceptionMessageBuilder();

    private static final Faker FAKER = new Faker();

    private static final FieldError FIELD_ERROR = new FieldError(FAKER.lorem().word(), FAKER.lorem().word(),
            FAKER.lorem().sentence());
    private static final FieldError FIRST_FIELD_ERROR = new FieldError(FAKER.lorem().word(), FAKER.lorem().word(),
            FAKER.lorem().sentence());
    private static final FieldError SECOND_FIELD_ERROR = new FieldError(FAKER.lorem().word(), FAKER.lorem().word(),
            FAKER.lorem().sentence());
    private static final FieldError THIRD_FIELD_ERROR = new FieldError(FAKER.lorem().word(), FAKER.lorem().word(),
            FAKER.lorem().sentence());

    private static final String BASE_MESSAGE = "Validation failed for fields: ";

    @ParameterizedTest
    @MethodSource("provideFieldErrors")
    public void testBuildValidationErrorMessage(List<FieldError> fieldErrors, String expectedErrorMessage) {
        String errorMessage = VALIDATION_EXCEPTION_MESSAGE_BUILDER.buildValidationErrorMessage(fieldErrors);

        assertEquals(expectedErrorMessage, errorMessage);
    }

    private static Stream<Arguments> provideFieldErrors() {
        return Stream.of(
                Arguments.of(List.of(), BASE_MESSAGE),

                Arguments.of(List.of(FIELD_ERROR),
                        BASE_MESSAGE + FIELD_ERROR.getField() + " (" + FIELD_ERROR.getDefaultMessage() + ")"),

                Arguments.of(List.of(FIRST_FIELD_ERROR, SECOND_FIELD_ERROR, THIRD_FIELD_ERROR),
                        BASE_MESSAGE + FIRST_FIELD_ERROR.getField() + " (" + FIRST_FIELD_ERROR.getDefaultMessage() +
                                "), " +
                                SECOND_FIELD_ERROR.getField() + " (" + SECOND_FIELD_ERROR.getDefaultMessage() + "), " +
                                THIRD_FIELD_ERROR.getField() +
                                " (" + THIRD_FIELD_ERROR.getDefaultMessage() + ")")
        );
    }
}