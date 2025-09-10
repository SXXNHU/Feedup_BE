package ll25.feedup.host.service;

import ll25.feedup.global.exception.BusinessException;
import ll25.feedup.global.exception.ExceptionCode;
import ll25.feedup.host.domain.Host;
import ll25.feedup.host.dto.HostSignupRequest;
import ll25.feedup.host.repository.HostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HostSignupService {

    private final HostRepository hostRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(HostSignupRequest signUp) {
        if (hostRepository.existsByLoginId(signUp.getLoginId())) {
            throw new BusinessException(ExceptionCode.CONFLICT, "이미 사용 중인 아이디입니다.");
        }
        Host host = HostSignupRequest.toEntity(signUp);
        host.setPassword(passwordEncoder.encode(signUp.getPassword()));
        hostRepository.save(host);
    }
}
