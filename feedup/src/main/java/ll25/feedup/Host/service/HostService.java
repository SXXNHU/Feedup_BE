package ll25.feedup.Host.service;

import ll25.feedup.Host.domain.Host;
import ll25.feedup.Host.dto.HostSignUpRequest;
import ll25.feedup.Host.repository.HostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HostService {

    private final HostRepository hostRepository;
//    private final PasswordEncoder passwordEncoder;

    public void signup(HostSignUpRequest signUp) {
        if(hostRepository.existsByLoginId(signUp.getLoginId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
        Host host = HostSignUpRequest.toEntity(signUp);
        hostRepository.save(host);
    }

//    public HostResponseDto login(HostRequestDto.Login login) {
//        Host host = hostRepository.findById(login.getId())
//                .orElseThrow(() -> new RuntimeException("Host not found"));
//
//        if (!host.getHostPw().equals(login.getPassword())) {
//            throw new RuntimeException("비밀번호가 틀렸습니다.");
//        }
//
//        return
//    }
}