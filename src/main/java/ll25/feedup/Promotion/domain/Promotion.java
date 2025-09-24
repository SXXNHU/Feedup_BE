package ll25.feedup.promotion.domain;

import jakarta.persistence.*;
import ll25.feedup.host.domain.Host;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "promotions")
@Getter
@Setter
@NoArgsConstructor
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private Host host;

    @Column(name = "context", nullable = false, columnDefinition = "TEXT")
    private String context;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PromotionStatus status;

    @Column(name = "total_team", nullable = false)
    private int totalTeam;

    @Column(name = "current_team", nullable = false)
    private int currentTeam;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;
}