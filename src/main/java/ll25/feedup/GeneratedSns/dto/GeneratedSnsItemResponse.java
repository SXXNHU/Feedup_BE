package ll25.feedup.GeneratedSns.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class GeneratedSnsItemResponse {
    private final Long id;
    private final String style;
    private final String content;
    private final boolean selected;
    private final List<String> mediaUrls;

    public GeneratedSnsItemResponse(Long id, String style, String content, boolean selected, List<String> mediaUrls) {
        this.id = id;
        this.style = style;
        this.content = content;
        this.selected = selected;
        this.mediaUrls = mediaUrls;
    }

}
