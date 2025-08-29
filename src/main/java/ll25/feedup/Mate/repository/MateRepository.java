package ll25.feedup.Mate.repository;

import ll25.feedup.Mate.domain.Mate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MateRepository extends JpaRepository<Mate, Long> {
    Optional<Mate> findByLoginId(String loginId); // 로그인용
    boolean existsByLoginId(String loginId);
}
