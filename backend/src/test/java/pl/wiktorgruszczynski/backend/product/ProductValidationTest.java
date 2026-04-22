package pl.wiktorgruszczynski.backend.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.wiktorgruszczynski.backend.config.SecurityConfig;
import pl.wiktorgruszczynski.backend.product.dto.ProductRequest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@WebMvcTest(ProductController.class)
@Import(SecurityConfig.class)
public class ProductValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private ProductMapper productMapper;

    private ProductRequest createExampleProductRequest() {
        return new ProductRequest(
                "Example name",
                "Example description",
                BigDecimal.valueOf(-1),
                "Default category"
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnErrorWhenPriceIsNegative() throws Exception {
        ProductRequest invalidRequest = createExampleProductRequest();


        MvcResult result = mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequest.toString())
        ).andReturn();

        int statusCode = result.getResponse().getStatus();

        assertEquals(HttpStatus.BAD_REQUEST.value(), statusCode);
    }
}
