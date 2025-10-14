package ll25.feedup.review.dto;

import ll25.feedup.review.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public record MyReviewsResponse(List<MyReviewItem> items, boolean hasNext, int page, int size, int totalPages,
                                long totalElements) {
    public static MyReviewsResponse from(Page<Review> pageObj) {
        List<MyReviewItem> items = pageObj.getContent().stream()
                .map(MyReviewItem::from)
                .toList();

        return new MyReviewsResponse(
                items,
                pageObj.hasNext(),
                pageObj.getNumber(),
                pageObj.getSize(),
                pageObj.getTotalPages(),
                pageObj.getTotalElements()
        );
    }
}