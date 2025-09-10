package ll25.feedup.host.service;

import ll25.feedup.global.exception.BusinessException;
import ll25.feedup.global.exception.ExceptionCode;
import ll25.feedup.host.domain.Host;
import ll25.feedup.host.repository.HostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HostLoginService {

    private final HostRepository hostRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Host authenticateHost(String loginId, String rawPassword) {
        Host host = hostRepository.findByLoginId(loginId)
                .orElseThrow(this::unauthorized);

        if (!passwordEncoder.matches(rawPassword, host.getPassword())) {
            throw unauthorized();
        }
        return host;
    }

    private BusinessException unauthorized() {
        return new BusinessException(
                ExceptionCode.UNAUTHORIZED,
                "아이디 또는 비밀번호가 올바르지 않습니다."
        );
    }
}
