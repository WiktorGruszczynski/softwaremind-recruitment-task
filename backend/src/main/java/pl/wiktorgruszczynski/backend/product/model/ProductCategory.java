package pl.wiktorgruszczynski.backend.product.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum ProductCategory {
    ELECTRONICS("Electronics"),
    FASHION("Fashion"),
    HOME_GARDEN("Home & Garden"),
    HEALTH("Health"),
    BEAUTY("Beauty"),
    SPORTS("Sports"),
    GROCERIES("Groceries"),
    AUTOMOTIVE("Automotive"),
    OTHER("Other");

    private final String label;

    ProductCategory(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static ProductCategory fromString(String value) {
        if (value == null || value.isEmpty()){
            return OTHER;
        }

        return Stream.of(ProductCategory.values())
                .filter(c -> c.name().equalsIgnoreCase(value) || c.label.equalsIgnoreCase(value))
                .findFirst()
                .orElse(OTHER);
    }
}
