package ll25.feedup.Mate.repository;

import ll25.feedup.Mate.domain.Mate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MateRepository extends JpaRepository<Mate, Long> {
//    Optional<Mate> findById(String id);
    boolean existsByLoginId(String loginId);
}
