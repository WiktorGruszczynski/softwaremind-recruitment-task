package pl.wiktorgruszczynski.backend.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wiktorgruszczynski.backend.product.dto.ProductRequest;
import pl.wiktorgruszczynski.backend.product.dto.ProductResponse;
import pl.wiktorgruszczynski.backend.product.model.Product;
import pl.wiktorgruszczynski.backend.product.model.ProductCategory;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("Should successfully add new product")
    void shouldAddProductSuccessfully() {
        ProductRequest request = new ProductRequest(
                "Laptop",
                "High end",
                BigDecimal.valueOf(2500.0),
                ProductCategory.ELECTRONICS
        );
        Product product = new Product();
        ProductResponse response = new ProductResponse(
                UUID.randomUUID(),
                "Laptop",
                "High end",
                BigDecimal.valueOf(2500.0),
                ProductCategory.ELECTRONICS
        );

        when(productMapper.toEntity(request)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toResponse(product)).thenReturn(response);

        ProductResponse result = productService.addProduct(request); // this line is tested

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Laptop");
        verify(productRepository, times(1)).save(any(Product.class));
    }
}
