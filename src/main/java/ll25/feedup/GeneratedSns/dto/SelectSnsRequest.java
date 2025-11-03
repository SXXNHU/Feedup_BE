package ll25.feedup.generatedSns.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SelectSnsRequest {
    private String style; // "PRETTY" | "CLEAN"
}