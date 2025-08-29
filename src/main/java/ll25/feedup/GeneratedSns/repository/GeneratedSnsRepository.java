package ll25.feedup.GeneratedSns.repository;

import ll25.feedup.GeneratedSns.domain.GeneratedSns;
import ll25.feedup.GeneratedSns.domain.SnsStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GeneratedSnsRepository extends JpaRepository<GeneratedSns, Long> {

    List<GeneratedSns> findAllByPromotion_Id(Long promotionId);

    Optional<GeneratedSns> findByPromotion_IdAndStyle(Long promotionId, SnsStyle style);

    Optional<GeneratedSns> findByPromotion_IdAndSelectedTrue(Long promotionId);

    @Modifying
    @Query("update GeneratedSns g set g.selected = false where g.promotion.id = :promotionId")
    void clearSelection(Long promotionId);

    @Modifying
    @Query("update GeneratedSns g set g.selected = true where g.promotion.id = :promotionId and g.style = :style")
    int markSelected(Long promotionId, SnsStyle style);
}