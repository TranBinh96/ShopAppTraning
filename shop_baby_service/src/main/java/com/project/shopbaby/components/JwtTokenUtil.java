package com.project.shopbaby.components;

import com.project.shopbaby.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${jwt.expiration}")
    private  int expiration;

    @Value("${jwt.secreateKey}")
    private  String secretKey;
    public  String generateToken(User user){
        Map<String,Object> claims = new HashMap<>();
        claims.put("phoneNumber",user.getPhoneNumber());
        //this.generateSecretKey();
        try{
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
            return  token;
        }catch (Exception exception){
            //co the dung logger
             throw  new InvalidParameterException("Cannot create jwt token, error : "+exception.getMessage());
        }
    }
    private Key getSignInKey(){
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    private  String generateSecretKey(){
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[32];
        secureRandom.nextBytes(keyBytes);
        String screateKey = Encoders.BASE64.encode(keyBytes);
        return  screateKey;
    }

    private Claims extractAllClaims(String token){
        return  Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token).getBody();
    }

    public   <T> T extractClaim(String token, Function<Claims,T> claimsTFunction){
        final  Claims  claims = this.extractAllClaims(token);
        return  claimsTFunction.apply(claims);
    }

    //check expiration
    private  boolean isTonkenExpired(String token){
        Date expiritionDate =  this.extractClaim(token,Claims::getExpiration);
        return expiritionDate.before(new Date());
    }

    public  String extractPhoneNumber(String token){
        return  extractClaim(token,Claims::getSubject);
    }

    public  boolean validateToken(String token, UserDetails userDetails){
        String phoneNumber =  extractPhoneNumber(token);
        return  (phoneNumber.equals(userDetails.getUsername()) && !isTonkenExpired(token));
    }




}
