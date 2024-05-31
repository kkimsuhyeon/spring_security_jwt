package demo.spring_security_jwt.config.jwt;

import demo.spring_security_jwt.config.security.CustomUserDetailService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class TokenProvider {

    private static final SecretKey key = Jwts.SIG.HS512.key().build();

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer-";

    public static final Long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 1; // ACCESS 토근 만료 시간: 1시간
    public static final Long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 6; // Refresh 토큰 만료 시간 : 6시간

    private final CustomUserDetailService userDetailService;

    @Autowired
    public TokenProvider(CustomUserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    public String createToken(Long memberId, String id, List<String> roles, Long expirationTime) {

        Date now = new Date();
        Date expiredDate = new Date(System.currentTimeMillis() + expirationTime);

        return Jwts.builder()
                .header().type("jwt")
                .and()
                .subject(String.valueOf(memberId)).claim("member_id", memberId).claim("id", id).claim("roles", roles)
                .issuedAt(now).expiration(expiredDate)
                .signWith(key).compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailService.loadUserByUsername(this.getId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getId(String token) {
        Object id = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().get("id");
        return String.valueOf(id);
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }


}
