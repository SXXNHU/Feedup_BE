package ll25.feedup.host.controller;

import ll25.feedup.host.dto.HostLoginRequest;
import ll25.feedup.host.service.HostLoginService;
import ll25.feedup.host.domain.Host;
import ll25.feedup.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class HostLoginController {

    private final HostLoginService hostLoginService;
    private final JwtTokenProvider jwt;

    /** 자영업자 로그인 **/
    @PostMapping("/host")
    public ResponseEntity<?> login(@RequestBody HostLoginRequest request) {
        Host host = hostLoginService.authenticateHost(request.getLoginId(), request.getPassword());
        String accessToken = jwt.createAccessToken(host.getLoginId(), "ROLE_HOST");
        String refreshToken = jwt.createRefreshToken(host.getLoginId());

        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "hostId", host.getId()
        ));
    }
}