package pl.wiktorgruszczynski.backend.common.dto;

public record ErrorResponse(
        int status,
        String message,
        long timestamp
) {
}
