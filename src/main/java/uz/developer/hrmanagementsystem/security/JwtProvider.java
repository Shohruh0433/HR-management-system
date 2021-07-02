package uz.developer.hrmanagementsystem.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import uz.developer.hrmanagementsystem.entity.Role;

import java.util.Date;
import java.util.Set;

@Component
public class JwtProvider {
    private final String key="Assalom aleykum bo'lajak dasturchilar";

    //tokenni generate qilish
    public String generateToken(String username, Set<Role> roles){

        long expiresTime = 1000 * 60 * 60 * 24;
        Date expireDate=new Date(System.currentTimeMillis()+ expiresTime);
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(expireDate)
                .setIssuedAt(new Date())
                .claim("roles", roles)
                .signWith(SignatureAlgorithm.HS512,key)
                .compact();


    }

    //token orqali username ni olish
    public String getUsernameFromToken(String token){
        try {
            return Jwts
                    .parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody().getSubject();

        }catch (Exception e){
            return null;
        }
    }


}
