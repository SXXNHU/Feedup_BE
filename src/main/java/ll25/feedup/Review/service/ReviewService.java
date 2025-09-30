package ll25.feedup.review.service;

import ll25.feedup.global.exception.BusinessException;
import ll25.feedup.global.exception.ExceptionCode;
import ll25.feedup.mate.domain.Mate;
import ll25.feedup.mate.repository.MateRepository;
import ll25.feedup.promotion.domain.Promotion;
import ll25.feedup.promotion.repository.PromotionRepository;
import ll25.feedup.review.domain.Review;
import ll25.feedup.review.dto.MyReviewsResponse;
import ll25.feedup.review.dto.ReviewCreateRequest;
import ll25.feedup.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private static final int MAX_PHOTOS = 10;
    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 50;

    private final ReviewRepository reviewRepository;
    private final MateRepository mateRepository;
    private final PromotionRepository promotionRepository;

    @Value("${app.s3.bucket}") private String bucket;
    @Value("${app.s3.region}") private String region;

    /** 리뷰 작성 **/
    @Transactional
    public void createReview(String mateLoginId, ReviewCreateRequest request) {
        Mate mate = mateRepository.findByLoginId(mateLoginId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.UNAUTHORIZED));

        Promotion promotion = promotionRepository.findById(request.getPromotionId())
                .orElseThrow(() -> new BusinessException(ExceptionCode.PROMO_NOT_FOUND));

        List<String> urls = (request.getPhotoUrls() == null) ? List.of()
                : request.getPhotoUrls().stream()
                .map(s -> s == null ? "" : s.trim())
                .filter(s -> !s.isBlank())
                .distinct()
                .limit(MAX_PHOTOS)
                .toList();

        // S3 퍼블릭 URL 가드
        String base = "https://" + bucket + ".s3." + region + ".amazonaws.com/";
        for (String url : urls) {
            if (!url.startsWith(base)) {
                throw new BusinessException(ExceptionCode.REVIEW_INVALID_IMAGE, "허용되지 않은 이미지 경로가 포함되어 있습니다.");
            }
        }

        Review review = Review.toEntity(request);
        review.setMate(mate);
        review.setPromotion(promotion);
        review.setPhotoUrls(urls);

        reviewRepository.save(review);
    }

    /** 내가 쓴 리뷰 목록 **/
    @Transactional(readOnly = true)
    public MyReviewsResponse getMyReviews(String mateLoginId, int page, int size) {
        Mate me = mateRepository.findByLoginId(mateLoginId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.UNAUTHORIZED));

        int safePage = Math.max(0, page);
        int safeSize = Math.max(MIN_PAGE_SIZE, Math.min(MAX_PAGE_SIZE, size));

        Page<Review> result = reviewRepository.findByMateOrderByCreatedAtDesc(
                me, PageRequest.of(safePage, safeSize)
        );
        return MyReviewsResponse.from(result);
    }

    /** 특정 프로모션의 리뷰 목록 **/
    @Transactional(readOnly = true)
    public MyReviewsResponse getReviewsByPromotion(Long promotionId, int page, int size) {
        if (!promotionRepository.existsById(promotionId)) {
            throw new BusinessException(ExceptionCode.PROMO_NOT_FOUND);
        }

        int safePage = Math.max(0, page);
        int safeSize = Math.max(MIN_PAGE_SIZE, Math.min(MAX_PAGE_SIZE, size));

        Page<Review> result = reviewRepository.findByPromotionIdOrderByCreatedAtDesc(
                promotionId, PageRequest.of(safePage, safeSize)
        );
        return MyReviewsResponse.from(result);
    }
}