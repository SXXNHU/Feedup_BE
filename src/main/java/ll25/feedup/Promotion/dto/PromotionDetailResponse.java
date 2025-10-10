package ll25.feedup.promotion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ll25.feedup.host.domain.PlaceCategory;
import ll25.feedup.promotion.domain.Promotion;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

import static ll25.feedup.global.util.DateFormatUtil.formatCreatedAt;

@Getter
public class PromotionDetailResponse {

    // Host
    private String thumbnail;
    private PlaceCategory category;
    private String address;
    private String nickname;
    private String phone;

    // Promotion
    @JsonProperty("start_date")
    private String startDate;
    @JsonProperty("end_date")
    private String endDate;
    private String context;
    private String createdAt;

    public static PromotionDetailResponse from(Promotion p) {
        var h = p.getHost();

        PromotionDetailResponse dto = new PromotionDetailResponse();
        // Host
        dto.thumbnail = h.getThumbnail();
        dto.category  = h.getCategory();
        dto.address   = h.getAddress();
        dto.nickname  = h.getNickname();
        dto.phone     = h.getPhone();

        // Promotion
        dto.startDate = p.getStartDate().toLocalDate().format(DateTimeFormatter.ISO_DATE);
        dto.endDate   = p.getEndDate().toLocalDate().format(DateTimeFormatter.ISO_DATE);
        dto.context   = p.getContext();
        dto.createdAt = formatCreatedAt(p.getCreatedAt());
        return dto;
    }
}