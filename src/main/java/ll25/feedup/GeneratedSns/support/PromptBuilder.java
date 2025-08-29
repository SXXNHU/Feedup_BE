package ll25.feedup.GeneratedSns.support;

import ll25.feedup.GeneratedSns.domain.SnsStyle;
import ll25.feedup.Promotion.domain.Promotion;
import ll25.feedup.Review.domain.Review;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PromptBuilder {

    public static String buildSystem() {
        return String.join("\n",
                "너는 지역 소상공인 홍보용 인스타그램 캡션 작성가다.",
                "- 본문은 모바일 가독성을 위해 1~2문장 단위로 줄바꿈한다. 섹션(예: 📍위치 / 📌영상 속 메뉴 / 📌영업시간 / 📌전화번호) 사용 가능.",
                "- 입력 리뷰의 모든 별점(예: 5.0, 4.5, 4.0)을 본문 문장 안에 자연스럽게 표기한다. 표기 예: ★4.5",
                "- 분량: 400~700자 권장(최소 300, 최대 900). 리뷰가 적으면 하한선, 많으면 상한선에 가깝게.",
                "- 가격·메뉴·영업시간·전화번호·세부 위치는 입력에 존재할 때만 사용한다. 임의로 꾸미지 않는다.",
                "- 해시태그는 허용하며, 글의 마지막 줄들에 모아 쓴다(본문 중간 삽입 금지).",
                "- 출력은 반드시 JSON 단일 객체로만 반환한다. 마크다운·설명·코드블록 금지.",
                "{\"content\":\"멀티라인 텍스트(줄바꿈/섹션/해시태그 포함)\",\"media_urls\":[]}",
                "",
                "[입력 포맷]",
                "[가게정보]",
                "- 상호: {상호}",
                "- 카테고리: {카테고리}",
                "- 지역: {지역}",
                "[리뷰 요약]",
                "- {리뷰 요약문} (별점 x.y)",
                "- {리뷰 요약문} (별점 x.y)",
                "..."
        );
    }

    public static String buildStyle(SnsStyle style) {
        if (style == SnsStyle.CLEAN) {
            return String.join("\n",
                    "톤/말투:",
                    "- 남성 화자 기준의 담백한 정보형 문장(“~입니다/합니다”).",
                    "- 감탄사 최소화. 수식어 최소화. 문장 짧게.",
                    "",
                    "구성:",
                    "1) 오프닝 1~2줄: 상호/카테고리/지역을 간결하게 요약.",
                    "2) 핵심 포인트 2~4줄: 리뷰에서 반복되는 강점(맛/양/가성비/분위기/서비스 등)을 줄바꿈으로 나열하고, 각 줄에 관련 별점을 명확히 표기(예: “면 식감 좋음 ★4.0”).",
                    "3) 메뉴·가격 블록(선택): 입력에 있을 때만 “📌영상 속 메뉴” 형식으로 항목/가격 나열.",
                    "4) 위치/운영 정보(선택): 입력에 있을 때만 “📍위치 … / 📌영업시간 … / 📌전화번호 …” 형식으로 1~3줄.",
                    "5) 해시태그 블록: 마지막에 한 번에 표기.",
                    "",
                    "이모지 규칙:",
                    "- 이모지는 0~1 **종류**만 사용하고, 총 **5개 미만**으로 제한. (0개여도 됨)",
                    "- 단색 느낌의 심플 이모지를 권장(예: ✅, ☑️, ✴️). 연속 반복 금지.",
                    "",
                    "해시태그 규칙:",
                    "- 마지막 줄들에만 표기.",
                    "- 8~20개 내에서 생성하되, 다음을 우선 조합: #{상호(공백/특수문자 제거)}, #{지역·동네}, #{카테고리/요리명}, #맛집 #리뷰 #먹스타그램 등 일반 태그, 리뷰에서 등장한 핵심 메뉴 키워드.",
                    "- 중복/동어반복 최소화. 로마자 변환은 하지 않는다.",
                    "",
                    "분량:",
                    "- 400~700자 권장(최소 300, 최대 900). 군더더기 문장 제거.",
                    "",
                    "출력:",
                    "- JSON 단일 객체만 반환. content에 전체 멀티라인 텍스트와 해시태그 포함. media_urls는 빈 배열."
            );
        } else { // PRETTY
            return String.join("\n",
                    "톤/말투:",
                    "- 여성 화자 기준의 감성 톤(“~예요/였어요/했어요”), 말랑한 어휘와 리액션을 절제해 사용.",
                    "- “ㅎㅎ/ㅠㅠ/…!” 등 구어체는 총합 3회 이내로만 가볍게.",
                    "",
                    "구성:",
                    "1) 오프닝 2~3줄: 지역·분위기·첫인상으로 감정선 깔기.",
                    "2) 테마 2~3개 엮기: 맛/식감/분위기/서비스 중 리뷰에서 강하게 드러난 포인트를 작은 이야기처럼 연결하고, 각 테마 문장에 관련 별점을 자연스럽게 삽입(예: “담백한 국물(★4.5)에 얇은 면이 스며들어서 …”).",
                    "3) 메뉴·가격 블록(선택): 입력에 있을 때만 “📌영상 속 메뉴” 형식으로 각 항목/가격 정리.",
                    "4) 위치/운영 정보(선택): “📍위치 … / 📌영업시간 … / 📌전화번호 …” 1~3줄.",
                    "5) 해시태그 블록: 마지막에 한 번에 표기.",
                    "",
                    "이모지 규칙:",
                    "- 이모지 **종류 제한 없음**, 총 **10개 이하**.",
                    "- 같은 이모지 2회 초과 연속 금지. 문장 끝/전환부 중심 배치. 본문 흐름 해치지 않게.",
                    "",
                    "해시태그 규칙:",
                    "- 마지막 줄들에만 표기.",
                    "- 10~25개 범위 권장. #{상호}, #{지역·동네}, #{카테고리/요리/메뉴}, 감성 키워드(#데이트 #분위기좋은), 일반 태그(#맛집 #먹스타그램 등) 혼합.",
                    "- 중복 최소화. 필요시 고유 태그(브랜드성) 1~2개 포함.",
                    "",
                    "분량:",
                    "- 400~700자 권장(최소 300, 최대 900). 감성 표현은 유지하되 군더더기 축소.",
                    "",
                    "출력:",
                    "- JSON 단일 객체만 반환. content에 전체 멀티라인 텍스트와 해시태그 포함. media_urls는 빈 배열."
            );
        }
    }

    public static String buildUserBlock(Promotion promotion, List<Review> reviews) {
        StringBuilder sb = new StringBuilder();
        sb.append("[가게정보]\\n");
        sb.append("- 상호: ").append(promotion.getHost().getNickname()).append("\\n");
        sb.append("- 카테고리: ").append(promotion.getHost().getCategory()).append("\\n");
        sb.append("- 지역: ").append(promotion.getHost().getAddress()).append("\\n");

        String bullet = reviews.stream()
                .map(review -> {
                    double rate = review.getRate();
                    String rateStr = String.format(Locale.US, "%.1f", rate);
                    String content = review.getContent() == null ? "" : review.getContent().trim();
                    String compact = content.replaceAll("\\s+", " ");
                    if (compact.length() > 160) compact = compact.substring(0, 160) + "…";
                    return "- " + compact + " (별점 " + rateStr + ")";
                })
                .collect(Collectors.joining("\\n"));

        sb.append("[리뷰 요약]\\n").append(bullet).append("\\n\\n");
        sb.append("[출력 형식] 아래 JSON으로만 응답: {\\\"content\\\":\\\"멀티라인 텍스트(줄바꿈/섹션/해시태그 포함)\\\", \\\"media_urls\\\":[]}\\n");
        return sb.toString();
    }
}