package ll25.feedup.Host.service;

import ll25.feedup.Host.domain.Host;
import ll25.feedup.Host.dto.HostSignupRequest;
import ll25.feedup.Host.repository.HostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HostSignupService {

    private final HostRepository hostRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(HostSignupRequest signUp) {
        if(hostRepository.existsByLoginId(signUp.getLoginId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
        Host host = HostSignupRequest.toEntity(signUp);
        host.setPassword(passwordEncoder.encode(signUp.getPassword()));
        hostRepository.save(host);
    }
}