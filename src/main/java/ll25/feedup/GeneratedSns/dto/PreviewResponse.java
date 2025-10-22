package ll25.feedup.generatedSns.dto;

import java.util.List;

public record PreviewResponse(Long id, Long promotionId, String style, String content, List<String> mediaUrls) {

}