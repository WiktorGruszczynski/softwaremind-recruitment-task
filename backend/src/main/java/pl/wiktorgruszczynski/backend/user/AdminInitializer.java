package pl.wiktorgruszczynski.backend.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInitializer {
    private final UserService userService;

    @Value("${ADMIN_EMAIL}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @EventListener(ApplicationReadyEvent.class)
    public void initAdmin(ApplicationReadyEvent event) {
        log.info("Starting administrator account initialization for: {}", adminEmail);
        try {
            userService.registerAdmin(adminEmail, adminPassword);
            log.info("Administrator account initialized successfully.");
        }
        catch (Exception e) {
            log.error("Failed to initialize admin account for email: {}", adminEmail, e);
        }
    }
}
