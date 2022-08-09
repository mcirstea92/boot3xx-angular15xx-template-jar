package ro.mariuscirstea.eventtracker.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ro.mariuscirstea.eventtracker.entity.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query(value="Select id from User u WHERE u.email= :userEmail",nativeQuery = true)
    Long getUserIdByEmail(@Param("userEmail") String userEmail);

}
