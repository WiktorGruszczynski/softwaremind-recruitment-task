package pl.wiktorgruszczynski.backend.product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wiktorgruszczynski.backend.product.dto.ProductRequest;
import pl.wiktorgruszczynski.backend.product.dto.ProductResponse;
import pl.wiktorgruszczynski.backend.product.model.ProductCategory;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest productRequest
    ) {
        return ResponseEntity.ok(
                productService.addProduct(productRequest)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(
            @PathVariable("id") UUID id
    ) {
        return ResponseEntity.ok(
                productService.getProduct(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts() {
        return ResponseEntity.ok(
                productService.getProducts()
        );
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(
            @PathVariable("category") ProductCategory category
            ) {
        return ResponseEntity.ok(
                productService.getProductsByCategory(category)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable("id") UUID id,
            @Valid @RequestBody ProductRequest request
    ) {
        return ResponseEntity.ok(
                productService.updateProduct(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable UUID id
    ) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
