package ll25.feedup.promotionApply.dto;

import ll25.feedup.host.domain.PlaceCategory;
import ll25.feedup.promotionApply.domain.PromotionApply;
import lombok.Getter;
import java.time.format.DateTimeFormatter;

@Getter
public class ParticipationItem {
    private Long promotionId;
    private String nickname;
    private PlaceCategory category;
    private String address;
    private String thumbnail;
    private String start_date;
    private String end_date;

    public static ParticipationItem from(PromotionApply apply) {
        var p = apply.getPromotion();
        var h = p.getHost();
        var dto = new ParticipationItem();
        dto.promotionId = p.getId();
        dto.nickname = h.getNickname();
        dto.category = h.getCategory();
        dto.address = h.getAddress();
        dto.thumbnail = h.getThumbnail();
        dto.start_date = p.getStartDate().toLocalDate().format(DateTimeFormatter.ISO_DATE);
        dto.end_date   = p.getEndDate().toLocalDate().format(DateTimeFormatter.ISO_DATE);
        return dto;
    }
}
