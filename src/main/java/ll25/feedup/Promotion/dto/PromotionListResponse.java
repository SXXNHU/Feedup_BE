package ll25.feedup.promotion.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PromotionListResponse {

    private final List<PromotionItem> items;
    private final boolean hasNext;

    private PromotionListResponse(List<PromotionItem> items, boolean hasNext) {
        this.items = items;
        this.hasNext = hasNext;
    }

    public static PromotionListResponse of(List<PromotionItem> items, boolean hasNext) {
        return new PromotionListResponse(items, hasNext);
    }

    public static PromotionListResponse empty() {
        return new PromotionListResponse(List.of(), false);
    }
}