package com.pramatiworks.et.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.pramatiworks.et.utils.TimeProvider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Component
public class JwtTokenUtil {
	static String CLAIM_KEY_USERNAME = "sub";
	static String CLAIM_KEY_AUDIENCE = "aud";
	static String CLAIM_KEY_CREATED = "iat";

	private static String AUDIENCE_UNKNOWN = "unknown";
	private static String AUDIENCE_WEB = "web";
	private static String AUDIENCE_TABLET = "tablet";
	private static String AUDIENCE_MOBILE = "mobile";

	private Long expiration = 604800L;
	private String secretKey = "itsMySecretKeyAndYouCantPossiblyBreakItUnlessYouAreMe"; // LOL :D

	@Autowired
	private TimeProvider timeProvider;

	private String getAudience(Device device) {
		if (device.isMobile())
			return AUDIENCE_MOBILE;
		else if (device.isNormal())
			return AUDIENCE_WEB;
		else if (device.isTablet())
			return AUDIENCE_TABLET;
		else
			return AUDIENCE_UNKNOWN;
	}
																																				
	private Claims getAllClaims(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}

	private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		Claims claims = getAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getCreatedDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public String getAudienceFromToken(String token) {
		return getClaimFromToken(token, Claims::getAudience);
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(timeProvider.now());
	}

	public String generateToken(UserDetails userDetails, Device device) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername(), getAudience(device));
	}

	private String doGenerateToken(Map<String, Object> claims, String subject, String audience) {
		final Date createdDate = timeProvider.now();
		final Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);

		return Jwts.builder().setClaims(claims).setSubject(subject).setAudience(audience).setIssuedAt(createdDate)
				.setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secretKey).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		JwtUser user = (JwtUser) userDetails;
		final String username = getUsernameFromToken(token);
		// final Date expiration = getExpirationDateFromToken(token);
		return (username.equals(user.getUsername()) && !isTokenExpired(token));
	}

}
