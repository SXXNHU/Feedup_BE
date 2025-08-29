package ll25.feedup.Mate.service;

import ll25.feedup.Mate.dto.MateSignupRequest;
import ll25.feedup.Mate.domain.Mate;
import ll25.feedup.Mate.repository.MateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MateSignupService {

    private final MateRepository mateRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(MateSignupRequest signUp){
        if(mateRepository.existsByLoginId(signUp.getLoginId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
        Mate mate = MateSignupRequest.toEntity(signUp);
        mate.setPassword(passwordEncoder.encode(signUp.getPassword()));
        mateRepository.save(mate);
    }
}