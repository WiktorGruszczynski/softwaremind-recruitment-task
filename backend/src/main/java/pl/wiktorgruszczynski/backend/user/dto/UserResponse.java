package pl.wiktorgruszczynski.backend.user.dto;

import java.util.List;

public record UserResponse(
        String email,
        List<String> roles
) {
}
