package pl.wiktorgruszczynski.backend.product;

import org.springframework.data.cassandra.repository.CassandraRepository;
import pl.wiktorgruszczynski.backend.product.model.Product;

import java.util.UUID;

public interface ProductRepository extends CassandraRepository<Product, UUID> {
}
