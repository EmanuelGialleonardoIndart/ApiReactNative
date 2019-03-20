package app.supportClases;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenServices {

	final static Key key=Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
	public String generateToken(String username,int segundos) {
		 Date exp=getExpiration(new Date(),segundos);
		 return Jwts.builder().setSubject(username).signWith(key).setExpiration(exp).compact();
	}
	
	private Date getExpiration(Date date,int segundos) {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND,segundos);
		return  calendar.getTime();
	}
	
	public static boolean validateToken(String token) {
		String prefix="Bearer";
		try {
			if(token.startsWith(prefix)) {
				token=token.substring(prefix.length()).trim();
			}
			Claims claims=Jwts.parser()
					.setSigningKey(key)
					.parseClaimsJws(token).getBody();
			System.out.println("ID: " + claims.getId());
            System.out.println("Subject: " + claims.getSubject());
            System.out.println("Issuer: " + claims.getIssuer());
            System.out.println("Expiration: " + claims.getExpiration());
			return true;
		}catch(ExpiredJwtException e) {
			System.out.println("Expired");
		    return false;
		}catch(JwtException e) {
			System.out.println("Error: "+e.getMessage() );
			return false;
		}
	}
	
}
