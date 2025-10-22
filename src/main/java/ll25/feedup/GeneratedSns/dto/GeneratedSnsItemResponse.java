package ll25.feedup.generatedSns.dto;

import java.util.List;

public record GeneratedSnsItemResponse(Long id, String style, String content, boolean selected,
                                       List<String> mediaUrls) {

}
