package pl.wiktorgruszczynski.backend.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wiktorgruszczynski.backend.product.dto.ProductRequest;
import pl.wiktorgruszczynski.backend.product.dto.ProductResponse;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponse addProduct(ProductRequest request){
        return null;
    }

    public ProductResponse getProduct(UUID id){
        return null;
    }

    public List<ProductResponse> getProducts(){
        return null;
    }

    public ProductResponse updateProduct(UUID id, ProductRequest request){
        return null;
    }

    public void deleteProduct(UUID id){

    }
}
