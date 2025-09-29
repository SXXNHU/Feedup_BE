package ll25.feedup.promotionApply.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ParticipationListResponse {
    private List<ParticipationItem> items;
    private int page;
    private int size;
    private boolean hasNext;
    private Integer nextPage;

    public static ParticipationListResponse of(
            List<ParticipationItem> items,
            int page,
            int size,
            boolean hasNext
    ) {
        return new ParticipationListResponse(
                items,
                page,
                size,
                hasNext,
                hasNext ? page + 1 : null
        );
    }
}