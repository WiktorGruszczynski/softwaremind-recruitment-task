package pl.wiktorgruszczynski.backend.user;


import org.springframework.data.cassandra.repository.CassandraRepository;
import pl.wiktorgruszczynski.backend.user.model.User;

import java.util.Optional;

public interface UserRepository extends CassandraRepository<User, String> {
    Optional<User> findByEmail(String email);
}
