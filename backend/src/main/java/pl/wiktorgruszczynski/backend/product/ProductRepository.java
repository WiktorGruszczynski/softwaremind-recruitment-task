package pl.wiktorgruszczynski.backend.product;

import org.springframework.data.cassandra.repository.CassandraRepository;
import pl.wiktorgruszczynski.backend.product.model.Product;
import pl.wiktorgruszczynski.backend.product.model.ProductCategory;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends CassandraRepository<Product, UUID> {

    List<Product> findByCategory(ProductCategory category);
}
