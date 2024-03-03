package org.databaseservice.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.databaseservice.constants.ErrorMessage;
import org.databaseservice.exceptions.handler.ValidationExceptionHandler;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CategoryDTO {
    @NotNull(message = ErrorMessage.CATEGORY_ID_REQUIRED, groups = ValidationExceptionHandler.OnUpdate.class)
    @Positive(message = ErrorMessage.ID_MUST_BE_POSITIVE)
    private Long id;

    @NotBlank(message = ErrorMessage.CATEGORY_REQUIRED)
    @Size(min = 1, max = 32, message = ErrorMessage.CATEGORY_LENGTH)
    private String name;
}
