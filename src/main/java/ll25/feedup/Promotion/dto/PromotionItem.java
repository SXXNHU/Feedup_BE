package ll25.feedup.promotion.dto;

import ll25.feedup.host.domain.PlaceCategory;
import ll25.feedup.promotion.domain.Promotion;
import ll25.feedup.promotion.domain.PromotionStatus;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import static ll25.feedup.global.util.DateFormatUtil.formatCreatedAt;

@Getter
public class PromotionItem {
    private Long promotionId;
    private String createdAt;
    private PromotionStatus promotionStatus;
    private String placeName;
    private PlaceCategory category;
    private String address;
    private String startDate;
    private String thumbnail;

    public static PromotionItem from(Promotion p) {
        PromotionItem dto = new PromotionItem();
        dto.promotionId = p.getId();
        dto.createdAt = formatCreatedAt(p.getCreatedAt());
        dto.promotionStatus = p.getStatus();
        dto.placeName = p.getHost().getNickname();
        dto.category = p.getHost().getCategory();
        dto.address = p.getHost().getAddress();
        dto.startDate = p.getStartDate().toLocalDate().format(DateTimeFormatter.ISO_DATE);
        dto.thumbnail = p.getHost().getThumbnail();
        return dto;
    }
}