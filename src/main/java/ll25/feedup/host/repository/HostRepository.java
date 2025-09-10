package ll25.feedup.host.repository;

import ll25.feedup.host.domain.Host;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HostRepository extends JpaRepository<Host, Long> {
    Optional<Host> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
}
