package pl.wiktorgruszczynski.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.wiktorgruszczynski.backend.product.model.ProductCategory;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(String.class, ProductCategory.class,
                source -> ProductCategory.valueOf(source.toUpperCase()));
    }
}
