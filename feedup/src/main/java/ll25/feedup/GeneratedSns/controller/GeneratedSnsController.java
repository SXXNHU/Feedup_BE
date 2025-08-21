package ll25.feedup.GeneratedSns.controller;

import ll25.feedup.GeneratedSns.dto.*;
import ll25.feedup.GeneratedSns.service.GeneratedSnsService;
import ll25.feedup.Promotion.dto.PromotionListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/generated-sns")
public class GeneratedSnsController {

    private final GeneratedSnsService service;

    @PostMapping
    public ResponseEntity<OptionsResponse> generate(Authentication auth, @RequestBody GenerateSnsRequest request) {
        return ResponseEntity.ok(service.generate(auth.getName(), request));
    }

    @PostMapping("/select")
    public ResponseEntity<Void> select(Authentication auth, @RequestBody SelectSnsRequest request) {
        service.select(auth.getName(), request.getPromotionId(), request.getStyle());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/options")
    public ResponseEntity<OptionsResponse> options(Authentication auth, @RequestParam("promotionId") Long promotionId) {
        return ResponseEntity.ok(service.options(auth.getName(), promotionId));
    }

    @GetMapping("/preview")
    public ResponseEntity<PreviewResponse> preview(Authentication auth, @RequestParam("promotionId") Long promotionId) {
        return ResponseEntity.ok(service.preview(auth.getName(), promotionId));
    }

    // (선택) COMPLETED 대상 리스트업 필요하면 사용
    @GetMapping("/targets")
    public ResponseEntity<PromotionListResponse> listTargets(
            Authentication auth,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(service.listCompletedTargets(auth.getName(), pageable));
    }
}