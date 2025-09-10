package ll25.feedup.host.controller;

import ll25.feedup.host.domain.Host;
import ll25.feedup.host.dto.HostInfoResponse;
import ll25.feedup.host.repository.HostRepository;
import ll25.feedup.promotion.dto.PromotionListResponse;
import ll25.feedup.promotion.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hosts")
public class HostController {

    private final PromotionService promotionService;
    private final HostRepository HostRepository;

    /** 사용자(ROLE:host) 정보 반환 **/
    @GetMapping("/info")
    public ResponseEntity<HostInfoResponse> info(@AuthenticationPrincipal String loginId) {
        Host host = HostRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));
        return ResponseEntity.ok(HostInfoResponse.from(host));
    }

    /** 사용자(ROLE:host)가 생성한 프로모션 리스트 조회  **/
    @GetMapping("/my-promotions")
    public ResponseEntity<PromotionListResponse> myPromotions(
            @AuthenticationPrincipal String loginId,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "20") int limit
    ) {
        return ResponseEntity.ok(
                promotionService.getHostPromotionsByLoginId(
                        loginId,
                        PageRequest.of(Math.max(0, offset/Math.max(1,limit)), Math.max(1, Math.min(50, limit)))
                )
        );
    }
}
