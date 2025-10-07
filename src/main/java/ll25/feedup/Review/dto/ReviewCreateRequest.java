package ll25.feedup.review.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ReviewCreateRequest {

    private long promotionId;
    private String content;

    @DecimalMin("0.5") @DecimalMax("5.0")
    private double rate;

    private List<String> photoUrls;
}
