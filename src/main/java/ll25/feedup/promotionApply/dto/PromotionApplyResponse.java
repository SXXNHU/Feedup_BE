package ll25.feedup.promotionApply.dto;

import ll25.feedup.promotionApply.domain.PromotionApply;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PromotionApplyResponse {
    private Long applyId;
    private Long promotionId;
    private String mateLoginId;
    private java.time.LocalDateTime appliedAt;
    private boolean reviewWritten;

    public static PromotionApplyResponse from(PromotionApply a) {
        return new PromotionApplyResponse(
                a.getId(),
                a.getPromotion().getId(),
                a.getMate().getLoginId(),
                a.getAppliedAt(),
                a.isReviewWritten()
        );
    }
}
