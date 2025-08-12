package ll25.feedup.controller;

import ll25.feedup.Mate.domain.Mate;
import ll25.feedup.Mate.repository.MateRepository;
import ll25.feedup.Host.domain.Host;
import ll25.feedup.Host.repository.HostRepository;
import ll25.feedup.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final MateRepository mateRepository;
    private final HostRepository hostRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwt;

    public record LoginRequest(String loginId, String password) {}
    public record LoginResponse(String accessToken, String refreshToken, String tokenType, long expiresIn) {}

    @PostMapping("/{type}/login")
    public ResponseEntity<?> login(@PathVariable String type, @RequestBody LoginRequest req) {
        // type: mate | host (대소문자 구분 안 함)
        String key = type == null ? "" : type.toLowerCase(Locale.ROOT);
        String msg = "아이디 또는 비밀번호가 올바르지 않습니다.";
        long expSec = 3600; // access-exp(ms) 3600000 기준

        switch (key) {
            case "mate" -> {
                var opt = mateRepository.findByLoginId(req.loginId());
                // 유저 없을 때도 matches 한 번 태워 타이밍 숨김
                String hash = opt.map(Mate::getPassword).orElse(dummyHash());
                if (opt.isEmpty() || !passwordEncoder.matches(req.password(), hash)) {
                    return ResponseEntity.status(401).body(msg);
                }
                String access = jwt.createAccessToken(opt.get().getLoginId(), "ROLE_MATE");
                String refresh = jwt.createRefreshToken(opt.get().getLoginId());
                return ResponseEntity.ok(new LoginResponse(access, refresh, "Bearer", expSec));
            }
            case "host" -> {
                var opt = hostRepository.findByLoginId(req.loginId());
                String hash = opt.map(Host::getPassword).orElse(dummyHash());
                if (opt.isEmpty() || !passwordEncoder.matches(req.password(), hash)) {
                    return ResponseEntity.status(401).body(msg);
                }
                String access = jwt.createAccessToken(opt.get().getLoginId(), "ROLE_HOST");
                String refresh = jwt.createRefreshToken(opt.get().getLoginId());
                return ResponseEntity.ok(new LoginResponse(access, refresh, "Bearer", expSec));
            }
            default -> {
                return ResponseEntity.badRequest().body("type은 mate 또는 host여야 합니다.");
            }
        }
    }

    // 더미 해시
    private String dummyHash() {
        return "$2a$10$eImiTXuWVxfM37uY4JANj.QaC3G1qGq1Zg0rj1cF9S7T2tQF/7T9a";
    }
}
