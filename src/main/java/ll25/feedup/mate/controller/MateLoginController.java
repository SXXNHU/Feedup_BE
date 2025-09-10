package ll25.feedup.mate.controller;

import ll25.feedup.mate.domain.Mate;
import ll25.feedup.mate.dto.MateLoginRequest;
import ll25.feedup.mate.service.MateLoginService;
import ll25.feedup.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class MateLoginController {

    private final MateLoginService mateLoginService;
    private final JwtTokenProvider jwt;

    /** 대학생 로그인 **/
    @PostMapping("/mate")
    public ResponseEntity<?> login(@RequestBody MateLoginRequest req) {

        Mate mate = mateLoginService.authenticateMate(req.getLoginId(), req.getPassword());
        String accessToken = jwt.createAccessToken(mate.getLoginId(), "ROLE_MATE");
        String refreshToken = jwt.createRefreshToken(mate.getLoginId());

        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "mateId", mate.getId()
        ));    }
}
