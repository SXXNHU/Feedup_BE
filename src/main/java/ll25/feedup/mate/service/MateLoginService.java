package ll25.feedup.mate.service;

import ll25.feedup.global.exception.BusinessException;
import ll25.feedup.global.exception.ExceptionCode;
import ll25.feedup.mate.domain.Mate;
import ll25.feedup.mate.repository.MateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    private BusinessException unauthorized() {
        return new BusinessException(ExceptionCode.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다.");
    }
}
