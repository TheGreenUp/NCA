package ru.green.nca.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
/**
 * Утилита для создания и проверки JSON Web Token (JWT).
 */
@Component
public class JWTUtil {
    @Value("${jwt_secret}")
    private String secret;
    /**
     * Генерирует JWT на основе имени пользователя.
     *
     * @param username Имя пользователя.
     * @return Сгенерированный токен.
     */
    public String generateToken(String username) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());
        return JWT.create()
                .withSubject("User details")
                .withClaim("username", username)//клэйм
                .withIssuedAt(new Date())//дата начала
                .withIssuer("green")//кто выдал токен
                .withExpiresAt(expirationDate)//дата окончания
                .sign(Algorithm.HMAC256(secret)); //секретный ключ
    }
    /**
     * Проверяет и возвращает содержимое токена (клейм) на основе переданного токена.
     *
     * @param token Токен для проверки.
     * @return Клейм из токена.
     * @throws JWTVerificationException Если произошла ошибка при проверке токена.
     */
    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("green")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }
}
