package ll25.feedup.controller;

import ll25.feedup.Mate.domain.Mate;
import ll25.feedup.Mate.service.MateLoginService;
import ll25.feedup.Host.domain.Host;
import ll25.feedup.Host.service.HostLoginService;
import ll25.feedup.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final MateLoginService mateLoginService;
    private final HostLoginService hostLoginService;
    private final JwtTokenProvider jwt;

    public record LoginRequest(String loginId, String password) {}
    public record LoginResponse(String accessToken, String refreshToken, String tokenType, long expiresIn) {}

    @PostMapping("/{role}")
    public ResponseEntity<?> login(@PathVariable String role, @RequestBody LoginRequest req) {
        String key = role == null ? "" : role.toLowerCase(Locale.ROOT);
        long expSec = 3600;

        return switch (key) {
            case "mate" -> {
                Mate mate = mateLoginService.authenticateMate(req.loginId(), req.password());
                String access = jwt.createAccessToken(mate.getLoginId(), "ROLE_MATE");
                String refresh = jwt.createRefreshToken(mate.getLoginId());
                yield ResponseEntity.ok(new LoginResponse(access, refresh, "Bearer", expSec));
            }
            case "host" -> {
                Host host = hostLoginService.authenticateHost(req.loginId(), req.password());
                String access = jwt.createAccessToken(host.getLoginId(), "ROLE_HOST");
                String refresh = jwt.createRefreshToken(host.getLoginId());
                yield ResponseEntity.ok(new LoginResponse(access, refresh, "Bearer", expSec));
            }
            default -> ResponseEntity.badRequest().body("type은 mate 또는 host여야 합니다.");
        };
    }
}
