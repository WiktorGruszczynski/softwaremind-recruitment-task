package pl.wiktorgruszczynski.backend.product.dto;

import pl.wiktorgruszczynski.backend.product.model.ProductCategory;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        ProductCategory category
) {
}
