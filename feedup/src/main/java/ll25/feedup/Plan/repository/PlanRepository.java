package ll25.feedup.Plan.repository;

import ll25.feedup.Plan.domain.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
}
