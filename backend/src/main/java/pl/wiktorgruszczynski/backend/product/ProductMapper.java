package pl.wiktorgruszczynski.backend.product;

import org.springframework.stereotype.Component;
import pl.wiktorgruszczynski.backend.product.dto.ProductRequest;
import pl.wiktorgruszczynski.backend.product.dto.ProductResponse;
import pl.wiktorgruszczynski.backend.product.model.Product;

import java.util.List;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequest request){
        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setCategory(request.category());

        return product;
    }

    public ProductResponse toResponse(Product product){
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory()
        );
    }

    public List<ProductResponse> toResponse(List<Product> products){
        return products.stream().map(this::toResponse).toList();
    }

    public void updateProduct(Product product, ProductRequest request){
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setCategory(request.category());
    }
}
