package ll25.feedup.Promotion.repository;

import ll25.feedup.Promotion.domain.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromotionRepository extends org.springframework.data.jpa.repository.JpaRepository<Promotion, Long> {

    Page<Promotion> findByHost_Id(Long hostId, Pageable pageable);

    @Query("select p from Promotion p join fetch p.host where p.id = :id")
    Optional<Promotion> findWithHostById(@Param("id") Long id);

}
