package pl.wiktorgruszczynski.backend.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.wiktorgruszczynski.backend.user.model.Role;
import pl.wiktorgruszczynski.backend.user.model.User;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_EMAIL}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    private void registerAdmin(String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            return;
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(
                passwordEncoder.encode(password)
        );
        user.setRole(Role.ROLE_ADMIN);
        userRepository.save(user);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initAdmin() {
        log.info("Starting administrator account initialization for: {}", adminEmail);
        try {
            registerAdmin(adminEmail, adminPassword);
            log.info("Administrator account initialized successfully.");
        }
        catch (Exception e) {
            log.error("Failed to initialize admin account for email: {}", adminEmail, e);
        }
    }
}
