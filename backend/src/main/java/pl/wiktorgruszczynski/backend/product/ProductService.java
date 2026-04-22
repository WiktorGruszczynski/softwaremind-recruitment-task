package pl.wiktorgruszczynski.backend.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wiktorgruszczynski.backend.common.exception.EntityNotFoundException;
import pl.wiktorgruszczynski.backend.product.dto.ProductRequest;
import pl.wiktorgruszczynski.backend.product.dto.ProductResponse;
import pl.wiktorgruszczynski.backend.product.model.Product;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductResponse addProduct(ProductRequest request){
        return productMapper.toResponse(
                productRepository.save(
                        productMapper.toEntity(request)
                )
        );
    }

    public ProductResponse getProduct(UUID id){
        return productMapper.toResponse(
                productRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Product not found id: " + id))
        );
    }

    public List<ProductResponse> getProducts(){
        return productMapper.toResponse(
                productRepository.findAll()
        );
    }

    public ProductResponse updateProduct(UUID id, ProductRequest request){
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));

        productMapper.updateProduct(product, request);

        return productMapper.toResponse(
                productRepository.save(product)
        );
    }

    public void deleteProduct(UUID id){
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product not found id: " + id);
        }

        productRepository.deleteById(id);
    }
}
