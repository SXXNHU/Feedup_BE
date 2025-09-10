package ll25.feedup.mate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MateLoginRequest {
    private String loginId;
    private String password;
}