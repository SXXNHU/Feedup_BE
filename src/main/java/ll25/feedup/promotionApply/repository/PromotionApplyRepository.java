package ll25.feedup.promotionApply.repository;

import ll25.feedup.mate.domain.Mate;
import ll25.feedup.promotionApply.domain.PromotionApply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionApplyRepository extends JpaRepository<PromotionApply, Long> {
    boolean existsByMate_IdAndPromotion_Id(Long mateId, Long promotionId);

    @EntityGraph(attributePaths = {"promotion", "promotion.host"})
    Page<PromotionApply> findByMateOrderByIdDesc(Mate mate, Pageable pageable);
}
