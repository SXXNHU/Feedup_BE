package ll25.feedup.Promotion.domain;

public enum PromotionStatus {
    ACTIVE,      // current_team == 0
    APPLYING,    // 0 < current_team < total_team
    FILLED,      // current_team == total_team (리뷰 미작성)
    REVIEWING,   // current_team == total_team, 일부 리뷰 작성
    REVIEWED,    // current_team == total_team, 모든 리뷰 작성
    APPROVED     // current_team == total_team, 리뷰 승인 완료
}