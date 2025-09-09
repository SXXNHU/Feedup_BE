package ll25.feedup.host.service;

import ll25.feedup.host.domain.Host;
import ll25.feedup.host.repository.HostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class HostLoginService {

    private final HostRepository hostRepository;
    private final PasswordEncoder passwordEncoder;

    public Host authenticateHost(String loginId, String rawPassword) {
        Host host = hostRepository.findByLoginId(loginId)
                .orElseThrow(this::unauthorized);
        if (!passwordEncoder.matches(rawPassword, host.getPassword())) {
            throw unauthorized();
        }
        return host;
    }

    private ResponseStatusException unauthorized() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다.");
    }
}