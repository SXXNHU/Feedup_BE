package ll25.feedup.promotionApply.controller;

import ll25.feedup.promotionApply.dto.ParticipationListResponse;
import ll25.feedup.promotionApply.dto.PromotionApplyRequest;
import ll25.feedup.promotionApply.dto.PromotionApplyResponse;
import ll25.feedup.promotionApply.service.PromotionApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/promotion-applies")
public class PromotionApplyController {

    private final PromotionApplyService service;

    /** 특정 프로모션 신청 **/
    @PostMapping
    public ResponseEntity<PromotionApplyResponse> apply(@AuthenticationPrincipal String mateLoginId,
                                                        @RequestBody PromotionApplyRequest req) {
        return ResponseEntity.ok(service.apply(mateLoginId, req));
    }

    /** 사용자가 참여중인 프로모션 조회 **/
    @GetMapping("/me")
    public ResponseEntity<ParticipationListResponse> myApplies(@AuthenticationPrincipal String mateLoginId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.myApplies(mateLoginId, page, size));
    }
}
