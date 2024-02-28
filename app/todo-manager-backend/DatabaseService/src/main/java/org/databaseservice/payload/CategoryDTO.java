package org.databaseservice.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.databaseservice.constants.ErrorMessage;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CategoryDTO {
    private Long id;

    @NotBlank(message = ErrorMessage.CATEGORY_REQUIRED)
    @Size(min = 1, max = 32, message = ErrorMessage.CATEGORY_LENGTH)
    private String name;
}
