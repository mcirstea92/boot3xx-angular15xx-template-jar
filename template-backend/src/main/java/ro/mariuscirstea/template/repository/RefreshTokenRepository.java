package ro.mariuscirstea.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.mariuscirstea.template.entity.RefreshToken;
import ro.mariuscirstea.template.entity.USR_User;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByUser(USR_User user);

    @Modifying
    @Query(value = "delete from refreshtoken where user_id =?1 and token != ?2", nativeQuery = true)
    int deleteOthersForUserExcept(Long userId, String requestRefreshToken);

}
