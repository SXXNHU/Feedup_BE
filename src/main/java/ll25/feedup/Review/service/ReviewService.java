package ll25.feedup.Review.service;

import ll25.feedup.Mate.domain.Mate;
import ll25.feedup.Mate.repository.MateRepository;
import ll25.feedup.Promotion.domain.Promotion;
import ll25.feedup.Promotion.repository.PromotionRepository;
import ll25.feedup.Review.domain.Review;
import ll25.feedup.Review.dto.MyReviewsResponse;
import ll25.feedup.Review.dto.ReviewCreateRequest;
import ll25.feedup.Review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MateRepository mateRepository;
    private final PromotionRepository promotionRepository;

    @Value("${app.s3.bucket}")
    private String bucket;

    @Value("${app.s3.region}")
    private String region;

    private static final int MAX_PHOTOS = 10;

    @Transactional
    public void createReview(String mateLoginId, ReviewCreateRequest request) {
        Mate mate = mateRepository.findByLoginId(mateLoginId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));
        Promotion promotion = promotionRepository.findById(request.getPromotionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "프로모션이 없습니다."));

        // URL 정제 + 최소 검증
        List<String> urls = (request.getPhotoUrls() == null) ? List.of()
                : request.getPhotoUrls().stream()
                .map(s -> s == null ? "" : s.trim())
                .filter(s -> !s.isBlank())
                .distinct()
                .limit(MAX_PHOTOS)
                .toList();

        String base = "https://" + bucket + ".s3." + region + ".amazonaws.com/";
        for (String u : urls) {
            if (!u.startsWith(base)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "허용되지 않은 이미지 경로가 포함되어 있습니다.");
            }
        }

        Review review = Review.toEntity(request);
        review.setMate(mate);
        review.setPromotion(promotion);
        review.setPhotoUrls(urls);

        reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public MyReviewsResponse getMyReviews(String mateLoginId, int offset, int limit) {
        Mate me = mateRepository.findByLoginId(mateLoginId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));

        int page = Math.max(0, offset) / Math.max(1, limit);
        int size = Math.max(1, Math.min(limit, 50));

        Page<Review> result = reviewRepository.findByMateOrderByCreatedAtDesc(me, PageRequest.of(page, size));
        return MyReviewsResponse.from(result, Math.max(0, offset), size);
    }

    @Transactional(readOnly = true)
    public MyReviewsResponse getReviewsByPromotion(Long promotionId, int offset, int limit) {
        if (!promotionRepository.existsById(promotionId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "프로모션이 없습니다.");
        }
        int size = Math.max(1, Math.min(limit, 50));
        int page = Math.max(0, offset) / size;

        Page<Review> result = reviewRepository.findByPromotionIdOrderByCreatedAtDesc(
                promotionId, PageRequest.of(page, size)
        );
        return MyReviewsResponse.from(result, Math.max(0, offset), size);
    }
}