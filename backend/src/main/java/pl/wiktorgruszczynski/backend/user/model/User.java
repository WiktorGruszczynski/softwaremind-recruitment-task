package pl.wiktorgruszczynski.backend.user.model;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;


@Table("users")
@Getter @Setter
public class User {
    @PrimaryKey
    private String email;

    private String password;

    @CassandraType(type = CassandraType.Name.TEXT)
    private Role role;
}
