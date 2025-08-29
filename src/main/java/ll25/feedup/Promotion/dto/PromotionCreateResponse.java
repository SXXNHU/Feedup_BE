package ll25.feedup.Promotion.dto;

import ll25.feedup.Plan.domain.Plan;
import ll25.feedup.Promotion.domain.Promotion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class PromotionCreateResponse {
    private String id;
    private String status;
    private PlanInfo plan;

    @Getter @Setter
    @AllArgsConstructor
    public static class PlanInfo {
        private Long planId;
        private int price;
        private int teamLimit;

        public static PlanInfo from(Plan plan) {
            return new PlanInfo(plan.getId(), plan.getPrice(), plan.getTeamLimit());
        }
    }

    public static PromotionCreateResponse from(Promotion promotion, Plan plan) {
        return new PromotionCreateResponse(
                "PROMO_" + promotion.getId(),
                "pending_payment",
                PlanInfo.from(plan)
        );
    }
}
