package pl.wiktorgruszczynski.backend.user.dto;

public record AuthRequest(
        String email,
        String password
) {
}
