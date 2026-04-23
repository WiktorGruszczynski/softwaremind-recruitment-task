package pl.wiktorgruszczynski.backend.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.wiktorgruszczynski.backend.user.dto.UserResponse;

import java.util.List;

@Service
public class UserService {
    public UserResponse getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new UserResponse(email, roles);
    }
}
