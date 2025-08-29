package ll25.feedup.Review.dto;

import ll25.feedup.Review.domain.Review;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class MyReviewsResponse {
    private List<MyReviewItem> items;
    private boolean hasNext;
    private int nextOffset;

    public static MyReviewsResponse from(Page<Review> page, int offset, int limit) {
        List<MyReviewItem> items = page.getContent().stream()
                .map(MyReviewItem::from)
                .toList();
        boolean hasNext = page.hasNext();
        int nextOffset = hasNext ? offset + limit : offset;
        return new MyReviewsResponse(items, hasNext, nextOffset);
    }
}