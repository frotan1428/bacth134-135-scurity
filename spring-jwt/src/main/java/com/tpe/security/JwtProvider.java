package com.tpe.security;

import com.tpe.security.service.UserDetailImpl;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    /*
        In this class :
            1. Generate JW Token
            2. Validate JW Token
            3. extract userName from JW Token
     */


    private String jwtSecretKey = "sboot"; //secret key will be used to generate/parse token

    private long jwtExpiration =  86400000; //24*60*60*1000 = 24 hours

    //*************** GENERATE JW TOKEN *****************
    /*
        to generate TOKEN we need 3 things
            1. userName
            2. expire duration
            3. secret key
     */
    public String createToken(Authentication authentication){
        //getPrincipal method will give us currently logged in user
        UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();

        return Jwts.builder().
                setSubject(userDetail.getUsername()).//userName of loggedIn / authenticated user
                setIssuedAt(new Date()).   //when jwt is created
                setExpiration(new Date(new Date().getTime()+jwtExpiration)). //expire time
                signWith(SignatureAlgorithm.HS512, jwtSecretKey). //encoding algorithm + secret key
                compact(); //compact /zip everything
    }


    //*************** VALIDATE JW TOKEN *****************

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
        } catch (UnsupportedJwtException e) {
            e.printStackTrace();
        } catch (MalformedJwtException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }

    //*************** EXTRACT USERNAME FROM JW TOKEN *****************
    public String extractUserNameFromJwToken(String token){
        return Jwts.parser().setSigningKey(jwtSecretKey).
                parseClaimsJws(token).
                getBody().
                getSubject();
    }

}
