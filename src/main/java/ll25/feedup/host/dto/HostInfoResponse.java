package ll25.feedup.host.dto;

import ll25.feedup.host.domain.Host;
import ll25.feedup.host.domain.PlaceCategory;
import lombok.Getter;

@Getter
public class HostInfoResponse {
    private String nickname;
    private String phone;
    private String address;
    private PlaceCategory category;

    public static HostInfoResponse from(Host h) {
        HostInfoResponse dto = new HostInfoResponse();
        dto.nickname = h.getNickname();
        dto.phone = h.getPhone();
        dto.address = h.getAddress();
        dto.category = h.getCategory();
        return dto;
    }
}