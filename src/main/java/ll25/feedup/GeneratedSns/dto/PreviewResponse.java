package ll25.feedup.GeneratedSns.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PreviewResponse {
    private final Long id;
    private final Long promotionId;
    private final String style;
    private final String content;
    private final List<String> mediaUrls;

    public PreviewResponse(Long id, Long promotionId, String style, String content, List<String> mediaUrls) {
        this.id = id;
        this.promotionId = promotionId;
        this.style = style;
        this.content = content;
        this.mediaUrls = mediaUrls;
    }

}