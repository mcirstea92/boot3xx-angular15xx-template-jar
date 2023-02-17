package ro.mariuscirstea.template.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.mariuscirstea.template.entity.RefreshToken;
import ro.mariuscirstea.template.exception.TokenRefreshException;
import ro.mariuscirstea.template.exception.TypeNotFoundException;
import ro.mariuscirstea.template.repository.RefreshTokenRepository;
import ro.mariuscirstea.template.repository.UserRepository;
import ro.mariuscirstea.template.service.RefreshTokenService;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${ro.marius.cirstea.properties.jwtRefreshExpiration}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findById(userId).orElseThrow(() -> new TypeNotFoundException("usr_user", userId)));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token availability expired. Please login again!");
        }
        return token;
    }

    @Override
    @Transactional
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).orElseThrow(() -> new TypeNotFoundException("usr_user", userId)));
    }

    @Override
    @Transactional
    public int clearOtherTokens(Long userId, String requestRefreshToken) {
        return refreshTokenRepository
                .deleteOthersForUserExcept(
                        userRepository
                                .findById(userId)
                                .orElseThrow(() -> new TypeNotFoundException("usr_user", userId))
                                .getId(),
                        requestRefreshToken);
    }
}