package ll25.feedup.promotionApply.domain;

import jakarta.persistence.*;
import ll25.feedup.mate.domain.Mate;
import ll25.feedup.promotion.domain.Promotion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "promotion_applies",
        uniqueConstraints = @UniqueConstraint(name = "ux_apply_unique", columnNames = {"mate_id","promotion_id"}))
@Getter @Setter @NoArgsConstructor
public class PromotionApply {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "mate_id", nullable = false)
    private Mate mate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    @Column(name = "applied_at", nullable = false, updatable = false)
    private LocalDateTime appliedAt;

    @Column(name = "review_written", nullable = false)
    private boolean reviewWritten = false;

    @PrePersist
    public void onCreate() {
        if (appliedAt == null) appliedAt = LocalDateTime.now();
    }
}
