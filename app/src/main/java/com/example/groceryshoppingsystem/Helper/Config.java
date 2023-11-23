package com.example.groceryshoppingsystem.Helper;

import java.security.Key;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class Config {

    public static String getJWT() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", "ebb483b2-d06b-453e-b596-715559490bb4");
        claims.put("kid", "70fd9236-882f-4f1f-9d66-0c6d53894f9a");
        byte[] keyBytes = Decoders.BASE64URL.decode("84fh22GichFRMMa2wyhmtiOoS65W0PhArUfirAiikmM");
        claims.put("aud", "doordash");
        claims.put("exp", ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(5).toEpochSecond());
        claims.put("iat", ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond());
        Key key = Keys.hmacShaKeyFor(keyBytes);
        String jwt = Jwts.builder()
                .setHeaderParam("dd-ver", "DD-JWT-V1")
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .signWith(key)
                .compact();
        return jwt;
    }
}
