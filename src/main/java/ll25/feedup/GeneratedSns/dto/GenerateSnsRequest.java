package ll25.feedup.generatedSns.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GenerateSnsRequest {
    private Long promotionId;
    private Boolean     force;       // optional
}