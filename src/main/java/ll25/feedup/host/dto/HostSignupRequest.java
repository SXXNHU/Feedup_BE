package ll25.feedup.host.dto;

import ll25.feedup.host.domain.Host;
import ll25.feedup.host.domain.PlaceCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HostSignupRequest {

        private String loginId;
        private String password;
        private String nickname;
        private String phone;
        private String address;
        private PlaceCategory category;
        private String thumbnail;

        public static Host toEntity(HostSignupRequest hostDto){
            Host host = new Host();
            host.setLoginId(hostDto.getLoginId());
            host.setPassword(hostDto.getPassword());
            host.setNickname(hostDto.getNickname());
            host.setPhone(hostDto.getPhone());
            host.setAddress(hostDto.getAddress());
            host.setCategory(hostDto.getCategory());
            host.setThumbnail(hostDto.getThumbnail());
            return host;
        }
}