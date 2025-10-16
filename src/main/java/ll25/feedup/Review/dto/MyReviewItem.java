package ll25.feedup.review.dto;

import ll25.feedup.review.domain.Review;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class MyReviewItem {
    private Long reviewId;
    private String hostNickname; // 프로모션의 Host.nickname
    private String nickname;     // 작성자 Mate.nickname
    private double rate;         // Review.rate
    private String content;
    private List<String> photoUrls;

    public static MyReviewItem from(Review r) {
        return new MyReviewItem(
                r.getId(),
                r.getPromotion().getHost().getNickname(),
                r.getMate().getNickname(),
                r.getRate(),
                r.getContent(),
                r.getPhotoUrls() == null ? List.of() : r.getPhotoUrls()
        );
    }
}