package ll25.feedup.GeneratedSns.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class OptionsResponse {
    private final Long promotionId;
    private final List<GeneratedSnsItemResponse> items;

    public OptionsResponse(Long promotionId, List<GeneratedSnsItemResponse> items) {
        this.promotionId = promotionId;
        this.items = items;
    }

}