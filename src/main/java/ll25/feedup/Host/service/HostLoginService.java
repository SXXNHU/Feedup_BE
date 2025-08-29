package ll25.feedup.Host.service;

import ll25.feedup.Host.domain.Host;
import ll25.feedup.Host.repository.HostRepository;
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
        if (!passwordEncoder.matches(rawPassword, host.getPassword())) { // host.getPassword()면 그걸로
            throw unauthorized();
        }
        return host;
    }

    private ResponseStatusException unauthorized() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다.");
    }
}