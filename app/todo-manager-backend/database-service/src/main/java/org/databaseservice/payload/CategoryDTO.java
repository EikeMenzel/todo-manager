package org.databaseservice.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.databaseservice.constants.ErrorMessage;
import org.databaseservice.exceptions.handler.ValidationExceptionHandler;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Schema(description = "Category data transfer object")
public class CategoryDTO {
    @Null(groups = ValidationExceptionHandler.OnCreate.class, message = ErrorMessage.ID_CREATION)
    @NotNull(groups = ValidationExceptionHandler.OnUpdate.class, message = ErrorMessage.CATEGORY_ID_REQUIRED)
    @Positive(groups = ValidationExceptionHandler.OnUpdate.class, message = ErrorMessage.ID_MUST_BE_POSITIVE)
    @Schema(description = "Category ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = ErrorMessage.CATEGORY_REQUIRED)
    @Size(min = 1, max = 32, message = ErrorMessage.CATEGORY_LENGTH)
    @Schema(description = "Name of the category", example = "Books")
    private String name;
}
