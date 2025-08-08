package ll25.feedup.Host.repository;

import ll25.feedup.Host.domain.Host;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HostRepository extends JpaRepository<Host, Long> {
//    Optional<Host> findById(String id);
    boolean existsByLoginId(String loginId);
}
