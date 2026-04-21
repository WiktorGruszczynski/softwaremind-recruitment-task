package pl.wiktorgruszczynski.backend.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.wiktorgruszczynski.backend.user.dto.AuthRequest;
import pl.wiktorgruszczynski.backend.user.model.Role;
import pl.wiktorgruszczynski.backend.user.model.User;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public void register(AuthRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()){
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
    }

    public void login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void registerAdmin(String email, String password) {
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
}
