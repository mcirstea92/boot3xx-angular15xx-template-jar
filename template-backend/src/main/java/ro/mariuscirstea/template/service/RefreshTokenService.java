package ro.mariuscirstea.template.service;

import ro.mariuscirstea.template.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {

    Optional<RefreshToken> findByToken(String token);

    RefreshToken createRefreshToken(Long userId);

    RefreshToken verifyExpiration(RefreshToken token);

    int deleteByUserId(Long userId);

    int clearOtherTokens(Long userId, String requestRefreshToken);
}
