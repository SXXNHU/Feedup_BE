package ll25.feedup.global.upload;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/uploads")
public class UploadController {

    private final UploadService service;

    // 1) 리뷰 사진 업로드
    @PostMapping(path = "/review-photos", consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, Object>> reviewPhotos(@AuthenticationPrincipal String loginId,
                                                            @RequestParam("promotionId") Long promotionId,
                                                            @RequestPart("photos") List<MultipartFile> photos) {
        var urls = service.uploadReviewPhotos(promotionId, photos);
        return ResponseEntity.ok(Map.of("urls", urls));
    }

    // 2) 자영업자 대표사진 업로드
    @PostMapping(path = "/host-thumbnail", consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, String>> hostThumbnail(@AuthenticationPrincipal String loginIdFromToken,
                                                             @RequestParam("loginId") String loginIdQuery,
                                                             @RequestPart("thumbnail") MultipartFile thumbnail) {
        String url = service.uploadHostThumbnail(loginIdQuery, thumbnail);
        return ResponseEntity.ok(Map.of("url", url));
    }
}
