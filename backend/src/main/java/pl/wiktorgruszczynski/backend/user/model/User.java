package pl.wiktorgruszczynski.backend.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;


@Table("users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @PrimaryKey
    private String email;

    private String password;

    @CassandraType(type = CassandraType.Name.TEXT)
    private Role role;
}
