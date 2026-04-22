package pl.wiktorgruszczynski.backend.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.wiktorgruszczynski.backend.common.exception.UserAlreadyExistsException;
import pl.wiktorgruszczynski.backend.security.TokenService;
import pl.wiktorgruszczynski.backend.user.UserRepository;
import pl.wiktorgruszczynski.backend.auth.dto.AuthRequest;
import pl.wiktorgruszczynski.backend.auth.dto.LoginResponse;
import pl.wiktorgruszczynski.backend.user.model.Role;
import pl.wiktorgruszczynski.backend.user.model.User;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public void register(AuthRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()){
            throw new UserAlreadyExistsException("User already exists");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
    }

    public LoginResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        return new LoginResponse(
                tokenService.generateToken(authentication)
        );
    }
}
