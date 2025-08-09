package ll25.feedup.Host.dto;

import ll25.feedup.Host.domain.Host;
import ll25.feedup.Host.domain.PlaceCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HostSignUpRequest {

        private String loginId;
        private String password;
        private String nickname;
        private String phone;
        private String address;
        private PlaceCategory category;
        private String thumbnail;

        public static Host toEntity(HostSignUpRequest hostDto){
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