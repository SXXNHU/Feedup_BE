package ll25.feedup.promotion.repository;

import ll25.feedup.promotion.domain.Promotion;
import ll25.feedup.promotion.domain.PromotionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    @EntityGraph(attributePaths = {"host"})
    Page<Promotion> findByHost_IdOrderByCreatedAtDesc(Long hostId, Pageable pageable);

    // 오픈 리스트
    @EntityGraph(attributePaths = {"host"})
    Page<Promotion> findByStatusAndEndDateGreaterThanEqualOrderByStartDateAsc(
            PromotionStatus status, java.time.LocalDateTime now, Pageable pageable);

    // 완료 리스트
    @EntityGraph(attributePaths = {"host"})
    Page<Promotion> findByStatusOrderByEndDateDesc(PromotionStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"host"})
    Optional<Promotion> findWithHostById(Long id);
}