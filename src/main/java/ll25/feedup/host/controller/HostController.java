package ll25.feedup.host.controller;

import ll25.feedup.promotion.dto.PromotionListResponse;
import ll25.feedup.promotion.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hosts")
public class HostController {

    private final PromotionService service;



    @GetMapping("/my-promotions")
    public ResponseEntity<PromotionListResponse> myPromotions(
            @AuthenticationPrincipal String loginId,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "20") int limit
    ) {
        return ResponseEntity.ok(
                service.getHostPromotionsByLoginId(
                        loginId,
                        PageRequest.of(Math.max(0, offset/Math.max(1,limit)), Math.max(1, Math.min(50, limit)))
                )
        );
    }
}
