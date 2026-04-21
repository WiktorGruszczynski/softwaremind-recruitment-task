package pl.wiktorgruszczynski.backend.product.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table("products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @PrimaryKey
    private UUID id;

    private String name;

    private String description;

    private BigDecimal price;

    @Indexed // faster queries
    private String category;
}
