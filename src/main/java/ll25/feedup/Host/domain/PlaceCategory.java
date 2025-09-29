package ll25.feedup.host.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlaceCategory {
    RESTAURANT("식당"),
    CAFE("카페"),
    OTHER("기타");

    private final String label;
    public static PlaceCategory of(String label){
        for(PlaceCategory category : PlaceCategory.values()){
            if(category.getLabel().equals(label)){
                return category;
            }
        }
        return null;
    }
}