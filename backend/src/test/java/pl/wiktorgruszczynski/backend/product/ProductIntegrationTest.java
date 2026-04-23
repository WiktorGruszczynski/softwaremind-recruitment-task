package pl.wiktorgruszczynski.backend.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.cassandra.CassandraContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.wiktorgruszczynski.backend.product.dto.ProductRequest;
import pl.wiktorgruszczynski.backend.product.model.Product;
import pl.wiktorgruszczynski.backend.product.model.ProductCategory;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
public class ProductIntegrationTest {

    @Container
    @ServiceConnection // Teraz to zadziała!
    static CassandraContainer cassandra = new CassandraContainer("cassandra:5.0.8")
            .withInitScript("database/init.cql");

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    private final String validDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus accumsan";

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    @DisplayName("ADMIN: Should create product and persist in Cassandra")
    void shouldCreateProductInDatabase() throws Exception{
        // GIVEN
        Product productRequest = Product.builder()
                .name("Gaming Laptop")
                .description("Good laptop for playing video games")
                .price(BigDecimal.valueOf(4999.99))
                .category(ProductCategory.ELECTRONICS)
                .build();

        // WHEN
        mvc.perform(post("/api/products")
                        .contextPath("/api")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(
                        status().isOk()
                );

        // THEN
        var products = productRepository.findAll();
        assertThat(products).hasSize(1);
        assertThat(products.getFirst().getName()).isEqualTo(productRequest.getName());
        assertThat(products.getFirst().getPrice()).isEqualByComparingTo(productRequest.getPrice());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    @DisplayName("ADMIN: Should update existing product in Cassandra")
    void shouldUpdateProductInDatabase() throws Exception {
        // GIVEN
        UUID id = UUID.randomUUID();
        Product existingProduct = new Product(
                id, "Old name", validDescription, BigDecimal.ONE, ProductCategory.OTHER
        );
        productRepository.save(existingProduct);

        Product updateRequest = Product.builder()
                .name("New name")
                .description(validDescription.replaceAll(" ", "X"))
                .price(new BigDecimal("99.99"))
                .category(ProductCategory.ELECTRONICS)
                .build();

        // WHEN
        mvc.perform(put("/api/products/" + id)
                        .contextPath("/api")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(
                        status().isOk()
                );

        // THEN
        Optional<Product> updated = productRepository.findById(id);
        assertThat(updated).isPresent();
        assertThat(updated.get().getName()).isEqualTo(updateRequest.getName());
        assertThat(updated.get().getPrice()).isEqualByComparingTo(updateRequest.getPrice());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    @DisplayName("ADMIN: Should delete existing product in Cassandra")
    public void shouldDeleteProductInDatabase() throws Exception {
        // GIVEN
        UUID id = UUID.randomUUID();
        Product existingProduct = new Product(
                id, "Name", validDescription, BigDecimal.ONE, ProductCategory.OTHER
        );
        productRepository.save(existingProduct);

        // WHEN
        mvc.perform(delete("/api/products/" + id)
                        .contextPath("/api")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        status().isNoContent()
                );

        // THEN
        assertThat(productRepository.findById(id)).isEmpty();
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DisplayName("USER: Should return 403 and NOT change data in database")
    void shouldNotAllowUserToChangeData() throws Exception {
        // GIVEN
        UUID id = UUID.randomUUID();
        Product existingProduct = new Product(
                id, "Name", "Opis", BigDecimal.TEN, ProductCategory.OTHER
        );

        productRepository.save(existingProduct);

        ProductRequest request = new ProductRequest(
                "New name",
                existingProduct.getDescription(),
                existingProduct.getPrice(),
                existingProduct.getCategory()
        );

        // WHEN
        mvc.perform(put("/api/products/" + id)
                        .contextPath("/api")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(
                        status().isForbidden()
                );

        // THEN
        Product productAfterAttack = productRepository.findById(id).orElseThrow();
        assertThat(productAfterAttack.getName()).isEqualTo(existingProduct.getName()); // Database unaffected
    }
}
