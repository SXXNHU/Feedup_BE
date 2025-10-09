package ll25.feedup.review.repository;

import ll25.feedup.review.domain.Review;
import ll25.feedup.mate.domain.Mate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @EntityGraph(attributePaths = {"promotion", "promotion.host"})
    Page<Review> findByMateOrderByCreatedAtDesc(Mate mate, Pageable pageable);

    @EntityGraph(attributePaths = {"promotion", "promotion.host"})
    Page<Review> findByPromotionIdOrderByCreatedAtDesc(Long promotionId, Pageable pageable);
}