package com.MunguVision.Xrange.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "lktB7vXTm724K6OKWBP89cYs8CfyOPG2WQIbvFsP7Ay19tK8ygVWp4HEP01iFpnhc4PQL" +
            "TovUxGkI9ApIF+AG7WmSds8+CKcWY0TfZffMys+HMS1tDGlMfzPKCs/hNtrUdPAMZBgMp9R1CdRR6ENI+EVo6Ew+cJ4d3tcihc6mKg" +
            "46G4JzUaMv9fLO/4Qi9jOJ9Jo+IA9JFmfci8+Xc5j6Cfx3AzkffNTCF9CY4hxZfvHqnL6n9WHyrUq2iubI+vaX9vN8QRDdYlMM83zZ" +
            "Eb5TW9NPlky2iWdH63GgzYd0/ClirKDReiGaVaj7w8Nhwk3wAJQBSN0D711lToxzRDU8QK0hO/g5wbUK3WCSVF4vKk=";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValide(String token, UserDetails userDetails){
        final String username =  extractUsername(token);
        return (username.equals(userDetails.getUsername())) && isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
