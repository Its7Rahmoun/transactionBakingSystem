package org.sid.ebankingbackend.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {

    public Map<String,String> generateToken(String username, Collection<String> claims) {
        return createToken(claims, username);
    }


    public Map<String,String> createToken( Collection<String> claims, String username) {
        Claims claims1 = Jwts.claims();

        claims1.put("roles",claims);
        String acces_token = Jwts.builder()
                .setClaims(claims1)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*1))
                .signWith(getSecKey(), SignatureAlgorithm.HS256).compact();
        String refresh_token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*15))
                .signWith(getSecKey(), SignatureAlgorithm.HS256).compact();
        Map<String,String> idToken = new HashMap<>();
        idToken.put("accessToken",acces_token);
        idToken.put("refreshToken",refresh_token);
        if(claims.contains("ADMIN"))
            idToken.put("message","Admin logged in");
        else
            idToken.put("message","user logged in");
        return idToken;

    }

    public Key getSecKey() {
        byte[] keybytes = Decoders.BASE64.decode("2b7e151628aed2a6abf7158809cf4f3c2b7e151628aed2a6abf7158809cf4f3c");
        System.out.println(Keys.hmacShaKeyFor(keybytes).getAlgorithm());
        return Keys.hmacShaKeyFor(keybytes);
    }


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSecKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }



    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        boolean flag = username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
