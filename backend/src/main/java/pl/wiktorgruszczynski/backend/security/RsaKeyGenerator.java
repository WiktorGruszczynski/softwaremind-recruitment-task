package pl.wiktorgruszczynski.backend.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
@Slf4j
public class RsaKeyGenerator {
    private final String PRIVATE_KEY_FILE = "private.pem";
    private final String PUBLIC_KEY_FILE = "public.pem";

    public void generateKeysIfMissing() {
        if (keysExist()) {
            log.info("RSA keys already exist. Skipping generation.");
            return;
        }

        try {
            log.info("Generating new RSA Key Pair...");
            KeyPair keyPair = generateKeyPair();

            saveKeyAsPem(keyPair.getPublic(), PUBLIC_KEY_FILE, "PUBLIC KEY");
            saveKeyAsPem(keyPair.getPrivate(), PRIVATE_KEY_FILE, "PRIVATE KEY");

            log.info("RSA keys successfully generated and saved to backend directory.");

        } catch (Exception e) {
            log.error("Failed to generate RSA keys", e);
            throw new RuntimeException("Key generation failed", e);
        }
    }

    private boolean keysExist() {
        return Files.exists(Paths.get(PRIVATE_KEY_FILE)) && Files.exists(Paths.get(PUBLIC_KEY_FILE));
    }

    private KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

    private void saveKeyAsPem(Key key, String fileName, String type) throws IOException {
        String pem = String.format(
                "-----BEGIN %s-----\n%s\n-----END %s-----",
                type,
                Base64.getMimeEncoder().encodeToString(key.getEncoded()),
                type
        );
        Files.writeString(Paths.get(fileName), pem);
    }
}
