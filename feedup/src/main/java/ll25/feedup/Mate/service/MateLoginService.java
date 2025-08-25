package ll25.feedup.Mate.service;

import ll25.feedup.Mate.domain.Mate;
import ll25.feedup.Mate.repository.MateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MateLoginService {

    private final MateRepository mateRepository;
    private final PasswordEncoder passwordEncoder;

    public Mate authenticateMate(String loginId, String rawPassword) {
        Mate mate = mateRepository.findByLoginId(loginId)
                .orElseThrow(this::unauthorized);
        if (!passwordEncoder.matches(rawPassword, mate.getPassword())) {
            throw unauthorized();
        }
        return mate;
    }

    private ResponseStatusException unauthorized() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다.");
    }
}
