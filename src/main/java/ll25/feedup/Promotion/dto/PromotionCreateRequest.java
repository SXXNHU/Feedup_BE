package ll25.feedup.Promotion.dto;

import ll25.feedup.Host.domain.Host;
import ll25.feedup.Plan.domain.Plan;
import ll25.feedup.Promotion.domain.Promotion;
import ll25.feedup.Promotion.domain.PromotionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PromotionCreateRequest {
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private String startDate;
    private String endDate;
    private String promotionContext;
    private Long planId;

    public Promotion toEntity(Host host, Plan plan) {
        if (host == null) throw badRequest("host가 유효하지 않습니다.");
        if (plan == null) throw badRequest("plan이 유효하지 않습니다.");

        LocalDate start = parseDate(startDate);
        LocalDate end   = parseDate(endDate);
        if (end.isBefore(start)) throw badRequest("endDate가 startDate보다 앞일 수 없습니다.");

        LocalDateTime startDt = start.atStartOfDay();
        LocalDateTime endDt   = end.atTime(23, 59, 59);

        Promotion p = new Promotion();
        p.setHost(host);
        p.setContext(promotionContext);
        p.setStatus(PromotionStatus.ACTIVE);
        p.setTotalTeam(plan.getTeamLimit());
        p.setCurrentTeam(0);
        p.setStartDate(startDt);
        p.setEndDate(endDt);
        return p;
    }

    private LocalDate parseDate(String s) {
        if (s == null || s.isBlank()) throw badRequest("날짜는 yyyy-MM-dd 형식이어야 합니다.");
        try { return LocalDate.parse(s, DATE); }
        catch (Exception e) { throw badRequest("날짜 파싱 실패: " + s + " (형식: yyyy-MM-dd)"); }
    }

    private ResponseStatusException badRequest(String msg) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
    }
}
