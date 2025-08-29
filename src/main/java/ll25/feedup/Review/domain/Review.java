package ll25.feedup.Review.domain;

import jakarta.persistence.*;
import ll25.feedup.Mate.domain.Mate;
import ll25.feedup.Promotion.domain.Promotion;
import ll25.feedup.Review.dto.ListStringJsonConverter;
import ll25.feedup.Review.dto.ReviewCreateRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter @Setter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mate_id", nullable = false)
    private Mate mate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "rate", nullable = false)
    private double rate;

    @Column(name = "photo_urls", columnDefinition = "json")
    @Convert(converter = ListStringJsonConverter.class)
    private List<String> photoUrls = new ArrayList<>();

    @Column(name = "is_reported", nullable = false)
    private boolean reported = false;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private LocalDateTime createdAt;

    public static Review toEntity(ReviewCreateRequest request){
        Review review = new Review();
        review.setContent(request.getContent());
        review.setRate(request.getRate());
        review.setPhotoUrls(new ArrayList<>(Optional.ofNullable(request.getPhotoUrls()).orElse(List.of())));
        return review;
    }
}