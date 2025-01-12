package ru.khav.NewsPaper.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.khav.NewsPaper.models.BlackListTokens;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtill {
    private final BlackListTokens blackListTokens;
    @Value("${jwt_secret}")
    private String secret;

    public JWTUtill(BlackListTokens blackListTokens) {
        this.blackListTokens = blackListTokens;
    }

    public String generateToken(String email) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

        return JWT.create()
                .withSubject("user details")
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .withIssuer("Ulyashka")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetriveClaim(String Token) throws JWTVerificationException {
        if (blackListTokens.isTokenBlacklisted(Token)) {
            throw new JWTVerificationException("this token in BlackList");
        }
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("user details")
                .withIssuer("Ulyashka")
                .build();

        DecodedJWT jwt = verifier.verify(Token);
        return jwt.getClaim("email").asString();

    }

    public void addTokenToBlackList(String Token) {
        blackListTokens.addBlackListToken(Token);
    }
}
