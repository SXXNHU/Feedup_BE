package ll25.feedup.GeneratedSns.domain;

import jakarta.persistence.*;
import ll25.feedup.Host.domain.Host;
import ll25.feedup.Promotion.domain.Promotion;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@Table(name = "generated_sns",
        uniqueConstraints = @UniqueConstraint(name = "uq_promo_style",
                columnNames = {"promotion_id", "style"}))
@EntityListeners(AuditingEntityListener.class)
public class GeneratedSns {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "host_id", nullable = false)
    private Host host;

    @Enumerated(EnumType.STRING)
    @Column(name = "style", nullable = false, length = 16)
    private SnsStyle style;

    @Lob
    @Column(name = "content", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String content;

    @Setter
    @Column(name = "is_selected", nullable = false)
    private boolean selected;

    protected GeneratedSns() {}

    public GeneratedSns(Promotion promotion, Host host, SnsStyle style, String content, boolean selected) {
        this.promotion = promotion;
        this.host = host;
        this.style = style;
        this.content = content;
        this.selected = selected;
    }

    public void updateContent(String content) { this.content = content; }
}