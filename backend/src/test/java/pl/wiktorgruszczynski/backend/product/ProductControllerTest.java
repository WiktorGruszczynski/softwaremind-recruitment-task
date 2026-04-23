package pl.wiktorgruszczynski.backend.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.wiktorgruszczynski.backend.config.SecurityConfig;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ProductController.class)
@Import(SecurityConfig.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    @DisplayName("Should return 400 Bad Request when price is negative")
    void shouldReturn400WhenPriceIsNegative() throws Exception {
        String invalidProductJson = """
            {
                "name": "Broken Phone",
                "description": "_",
                "price": -100.0,
                "category": "electronics"
            }
        """;

        mockMvc.perform(post("/api/products")
                        .contextPath("/api")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidProductJson))
                .andExpect(
                        status().isBadRequest()
                );
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DisplayName("Should return 403 Forbidden when user without ADMIN role tries to add product")
    void shouldReturn403WhenNotAdmin() throws Exception {
        String invalidProductJson = """
            {
                "name": "Product",
                "description": "_",
                "price": 100.0,
                "category": "electronics"
            }
        """;

        mockMvc.perform(post("/api/products")
                        .contextPath("/api")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidProductJson))
                .andExpect(
                        status().isForbidden()
                );
    }
}
