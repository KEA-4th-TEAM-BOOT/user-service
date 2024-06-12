package userservice.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import userservice.domain.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUserLink(String userLink);

    @Query(value = "SELECT * FROM users ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<User> findRandomUsers(@Param("limit") int limit);
}
