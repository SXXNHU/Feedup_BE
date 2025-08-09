package ll25.feedup.Mate.dto;

import ll25.feedup.Mate.domain.Mate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MateSignUpRequest {

    private String loginId;
    private String password;
    private String nickname;
    private String phone;

    public static Mate toEntity(MateSignUpRequest mateDto){
        Mate mate = new Mate();
        mate.setLoginId(mateDto.getLoginId());
        mate.setPassword(mateDto.getPassword());
        mate.setNickname(mateDto.getNickname());
        mate.setPhone(mateDto.getPhone());
        return mate;
    }
}