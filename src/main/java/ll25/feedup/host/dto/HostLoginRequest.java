package ll25.feedup.host.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HostLoginRequest {
    private String loginId;
    private String password;
}