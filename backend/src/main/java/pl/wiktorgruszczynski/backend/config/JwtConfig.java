package pl.wiktorgruszczynski.backend.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import pl.wiktorgruszczynski.backend.security.RsaKeyGenerator;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JwtConfig {
    public JwtConfig(RsaKeyGenerator rsaKeyGenerator) {
        rsaKeyGenerator.generateKeysIfMissing();
    }

    @Value("file:public.pem")
    private Resource publicKeyResource;

    @Value("file:private.pem")
    private Resource privateKeyResource;

    @Bean
    public JwtEncoder jwtEncoder() throws Exception {
        RSAPublicKey publicKey = readPublicKey(publicKeyResource);
        RSAPrivateKey privateKey = readPrivateKey(privateKeyResource);

        JWK jwk = new RSAKey
                .Builder(publicKey)
                .privateKey(privateKey)
                .keyID("default-key-id")
                .build();

        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public JwtDecoder jwtDecoder() throws Exception {
        return NimbusJwtDecoder.withPublicKey(readPublicKey(publicKeyResource)).build();
    }

    private RSAPublicKey readPublicKey(Resource resource) throws Exception {
        String key = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] encoded = Base64.getDecoder().decode(key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    private RSAPrivateKey readPrivateKey(Resource resource) throws Exception {
        // 1. Odczytaj cały plik jako String
        String keyContent = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        // 2. Usuń nagłówki i stopki (używamy regexa, który ignoruje to co pomiędzy myślnikami)
        String cleanKey = keyContent
                .replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s", ""); // Usuwa spacje, taby i WSZYSTKIE rodzaje nowej linii (\r, \n)

        // 3. Dekoduj Base64
        byte[] encoded = Base64.getDecoder().decode(cleanKey);

        // 4. Generuj klucz
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }
}
