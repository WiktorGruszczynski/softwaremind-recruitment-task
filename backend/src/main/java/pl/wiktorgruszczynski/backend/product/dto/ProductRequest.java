package pl.wiktorgruszczynski.backend.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import pl.wiktorgruszczynski.backend.product.model.ProductCategory;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Name is required")
        String name,

        @Size(min = 16, max = 512, message = "Description must be between 16 and 512 characters")
        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than zero")
        BigDecimal price,

        @NotNull(message = "Category is required")
        ProductCategory category
) {
}
