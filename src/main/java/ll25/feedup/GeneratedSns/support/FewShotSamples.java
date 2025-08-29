package ll25.feedup.GeneratedSns.support;

import ll25.feedup.GeneratedSns.domain.SnsStyle;
import ll25.feedup.Promotion.domain.Promotion;
import ll25.feedup.Review.domain.Review;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class FewShotSamples {

    private FewShotSamples() {}

    /** 예시 한 쌍
     * @param tags 카테고리/키워드/지역 등
     */
        private record Ex(SnsStyle style, String user, String assistantJson, Set<String> tags) {
    }

    /** 샘플 풀 — 네가 준 데이터 축약본(해시태그/섹션/말투/이모지 패턴 유지) */
    private static final List<Ex> POOL = List.of(
            // ===== CLEAN (남자 말투, 정보형) =====
            new Ex(SnsStyle.CLEAN,
                    // 라멘
                    "[가게정보]\n- 상호: 라멘도치\n- 카테고리: 라멘\n- 지역: 서울 어디든\n[리뷰 요약]\n- 다찌석, 혼밥 좋아요 (별점 4.5)\n- 담백한 간장 육수, 기름기 적음 (별점 4.0)\n- 얇은 면, 국물 잘 스밈 (별점 4.0)",
                    "{\"content\":\"일본식 라멘 전문 라멘도치입니다. 다찌석 위주라 혼밥하기 편합니다.\\n\\n간장 베이스의 담백한 육수로 깔끔합니다 ★4.0\\n얇은 면이라 국물이 잘 스며 깊게 먹힙니다 ★4.0\\n좌석 배치가 트여 있어 답답하지 않습니다 ★4.5\\n\\n📌영상 속 메뉴\\n- 소유라멘 10,900원\\n- 가라아게 3,500원\\n- 차항 7,900원\\n\\n#라멘 #일본라멘 #혼밥 #다찌석 #담백한라멘 #가라아게 #차항 #면식감 #맛집 #먹스타그램\",\"media_urls\":[]}",
                    set("라멘","일식","혼밥","간장","다찌","ramen","tokyo","도치")
            ),
            new Ex(SnsStyle.CLEAN,
                    // 타코
                    "[가게정보]\n- 상호: 옐로우타코 서문본점\n- 카테고리: 멕시칸\n- 지역: 충북 청주시 상당구\n[리뷰 요약]\n- 한 손에 묵직, 고기 듬뿍 (별점 5.0)\n- 향신료 배합 현지 느낌 (별점 4.5)",
                    "{\"content\":\"청주 상당구의 멕시칸 전문점 옐로우타코 서문본점입니다. 든든하게 먹기 좋습니다.\\n\\n한 손에 잡았을 때 묵직할 정도로 고기가 넉넉합니다 ★5.0\\n향신료 배합이 과하지 않고 현지 느낌에 가깝습니다 ★4.5\\n\\n📌영상 속 메뉴\\n- 비프타코 2pcs 10,800원\\n- 초리조타코 2pcs 11,500원\\n- 퀘사디아 16,000원\\n\\n#옐로우타코 #청주맛집 #타코 #멕시칸 #초리조타코 #성안길맛집 #맛집 #맛집리뷰 #먹스타그램 #릴스\",\"media_urls\":[]}",
                    set("멕시칸","타코","초리조","청주","서문","taco","mexican")
            ),
            new Ex(SnsStyle.CLEAN,
                    // 모수(파인다이닝)
                    "[가게정보]\n- 상호: 모수 서울\n- 카테고리: 파인다이닝\n- 지역: 서울 용산구\n[리뷰 요약]\n- 셰프 정체성이 강한 코스 (별점 5.0)\n- 와인 페어링 경험 좋음 (별점 4.5)",
                    "{\"content\":\"용산의 파인다이닝, 모수 서울입니다. 코스 구성과 페어링이 인상적입니다.\\n\\n요리마다 셰프의 정체성이 분명합니다 ★5.0\\n와인 페어링 밸런스가 좋아 경험의 밀도가 높습니다 ★4.5\\n\\n📍위치: 서울 용산구 회나무로41길 4\\n📌가격: 디너 코스 1인 42만원\\n\\n#모수 #모수서울 #파인다이닝 #미슐랭 #와인페어링 #용산맛집 #한남동맛집 #맛집 #리뷰 #먹스타그램\",\"media_urls\":[]}",
                    set("파인다이닝","미슐랭","코스","와인","용산","한남","모수","fine","dining")
            ),

            // ===== PRETTY (여자 말투, 감성형) =====
            new Ex(SnsStyle.PRETTY,
                    // 피자 EPP
                    "[가게정보]\n- 상호: 피자EPP 구로가산점\n- 카테고리: 피자\n- 지역: 서울 금천구\n[리뷰 요약]\n- 느끼함 적고 깔끔 (별점 4.5)\n- 꽁다리 식감이 특히 좋음 (별점 5.0)",
                    "{\"content\":\"올해 먹은 피자 중 유독 깔끔했어요(★4.5). 끝까지 편안해서 더 좋았어요.\\n바삭한 꽁다리에 고소함이 꽉 차서 마지막 한 입까지 행복했답니다(★5.0). 🍕✨\\n\\n📌영상 속 메뉴\\n- EPP 프리미엄 33,800원\\n- 파이브치즈 32,500원\\n\\n#피자 #피자맛집 #가산맛집 #구로맛집 #피자추천 #치즈피자 #금천구맛집 #가산디지털단지맛집 #먹스타그램\",\"media_urls\":[]}",
                    set("피자","가산","구로","EPP","도우","치즈","pizza")
            ),
            new Ex(SnsStyle.PRETTY,
                    // 우설+파, 비빔냉면
                    "[가게정보]\n- 상호: 짝귀 마곡발산점\n- 카테고리: 고기/이자카야\n- 지역: 서울 강서구\n[리뷰 요약]\n- 파를 품은 우설 조화 (별점 4.5)\n- 비빔냉면 새콤달달 깔끔 (별점 5.0)",
                    "{\"content\":\"마곡에서만 느낄 수 있는 조화였어요. 양념된 파의 단맛이 서서히 올라오고, 우설의 식감이 톡 하고 살아나요(★4.5).\\n새콤달달한 비빔냉면이 한입 물면, 그 자리에서 그대로 빠져들어요(★5.0). 😍\\n\\n📍위치: 발산역 근처\\n📌영상 속 메뉴\\n- 한우 특상(파)우설 36,000원\\n- 비빔냉면\\n\\n#마곡맛집 #발산맛집 #우설 #비빔냉면 #고기집 #분위기좋은 #맛집 #먹스타그램\",\"media_urls\":[]}",
                    set("우설","마곡","발산","비빔냉면","이자카야","강서")
            ),
            new Ex(SnsStyle.PRETTY,
                    // 초록문(회)
                    "[가게정보]\n- 상호: 초록문\n- 카테고리: 회/이자카야\n- 지역: 서울 관악구\n[리뷰 요약]\n- 모둠회 퀄리티 대비 합리적 (별점 4.5)\n- 안키모/구이 별미 (별점 4.0)",
                    "{\"content\":\"신선함이 먼저 인사하는 집이에요(★4.5).\\n안키모랑 구이까지 하나씩 맛보다 보면, 오늘 기분이 촉촉해져요(★4.0). 🐟\\n\\n📍위치: 서울대입구역 도보권\\n📌영상 속 메뉴\\n- 모둠회 36,900원\\n\\n#초록문 #샤로수길 #서울대입구역맛집 #모둠회 #안키모 #이자카야 #분위기좋은 #맛집 #먹스타그램\",\"media_urls\":[]}",
                    set("회","모둠회","안키모","샤로수길","서울대입구","이자카야","관악")
            )
    );

    /** 외부에서 호출: 입력에 가장 가까운 예시 N쌍(기본 2) 선택 */
    public static List<Map<String, String>> select(SnsStyle style,
                                                   Promotion promotion,
                                                   List<Review> reviews,
                                                   int maxPairs) {
        String raw = (promotion != null && promotion.getHost() != null)
                ? (safe(promotion.getHost().getNickname()) + " " + safe(promotion.getHost().getCategory()) + " " + safe(promotion.getHost().getAddress()))
                : "";
        String reviewText = reviews == null ? "" : reviews.stream()
                .map(r -> safe(r.getContent()))
                .collect(Collectors.joining(" "));
        Set<String> queryTokens = tokenize(raw + " " + reviewText);

        return POOL.stream()
                .filter(ex -> ex.style == style)
                .sorted((a,b) -> Double.compare(score(b.tags, queryTokens), score(a.tags, queryTokens)))
                .limit(Math.max(1, maxPairs))
                .flatMap(ex -> Stream.of(
                        msg("user", ex.user),
                        msg("assistant", ex.assistantJson)
                ))
                .collect(Collectors.toList());
    }

    // ===== helpers =====
    private static Map<String,String> msg(String role, String content) {
        Map<String,String> m = new LinkedHashMap<>();
        m.put("role", role);
        m.put("content", content);
        return m;
    }

    private static Set<String> set(String... s){ return Arrays.stream(s).map(FewShotSamples::norm).collect(Collectors.toSet()); }
    private static String safe(String s){ return s==null?"":s; }
    private static String norm(String s){
        String t = s==null? "": s.toLowerCase(Locale.ROOT);
        t = t.replaceAll("[^0-9a-z가-힣]", " ");
        return t.trim();
    }

    private static Set<String> tokenize(String s){
        return Arrays.stream(norm(s).split("\\s+"))
                .filter(w -> w.length()>=2)
                .collect(Collectors.toSet());
    }

    private static double score(Set<String> tags, Set<String> q){
        if (tags.isEmpty() || q.isEmpty()) return 0.0;
        long hit = tags.stream().filter(q::contains).count();
        return (double) hit / (double) tags.size();
    }

    private static String safe(Object o) {
        if (o == null) return "";
        return String.valueOf(o);
    }
}