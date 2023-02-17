package ro.mariuscirstea.template.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ro.mariuscirstea.template.entity.USR_User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<USR_User, Long> {

    Optional<USR_User> findByEmail(String email);

    @Query(value = "Select id from usr_user u WHERE u.email= :userEmail", nativeQuery = true)
    Long getUserIdByEmail(@Param("userEmail") String userEmail);

    boolean existsByEmail(String email);

    Optional<USR_User> findByUsername(String username);

    Optional<USR_User> findUSR_UserByUsername(String username);

    Optional<USR_User> findUSR_UserByEmail(String email);
}
