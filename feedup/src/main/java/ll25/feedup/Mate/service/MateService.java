package ll25.feedup.Mate.service;

import ll25.feedup.Mate.dto.MateSignUpRequest;
import ll25.feedup.Mate.domain.Mate;
import ll25.feedup.Mate.repository.MateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MateService {

    private final MateRepository mateRepository;

    public void signup(MateSignUpRequest signUp){
        if(mateRepository.existsByLoginId(signUp.getLoginId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
        Mate mate = MateSignUpRequest.toEntity(signUp);
        mateRepository.save(mate);
    }
}
