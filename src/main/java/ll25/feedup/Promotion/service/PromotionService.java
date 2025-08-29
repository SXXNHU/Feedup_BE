package ll25.feedup.Promotion.service;

import ll25.feedup.Host.domain.Host;
import ll25.feedup.Host.repository.HostRepository;
import ll25.feedup.Plan.domain.Plan;
import ll25.feedup.Plan.repository.PlanRepository;
import ll25.feedup.Promotion.domain.Promotion;
import ll25.feedup.Promotion.domain.PromotionStatus;
import ll25.feedup.Promotion.dto.*;
import ll25.feedup.Promotion.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final HostRepository hostRepository;
    private final PlanRepository planRepository;

    /** 1) 프로모션 생성 **/
    @Transactional
    public PromotionCreateResponse createPromotion(String loginId, PromotionCreateRequest request) {
        // Host 로그인 아이디로 조회 (토큰 subject = loginId)
        Host host = hostRepository.findByLoginId(loginId)
                .orElseThrow(() -> notFound("Host(loginId)", loginId));

        Plan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> notFound("Plan", request.getPlanId()));

        Promotion promotion = request.toEntity(host, plan);
        Promotion saved = promotionRepository.save(promotion);
        return PromotionCreateResponse.from(saved, plan);
    }

    /** 2) 로그인한 호스트의 전체 프로모션(상태 무관) **/
    public PromotionListResponse getHostPromotionsByLoginId(final String loginId, final Pageable pageable) {
        Host host = hostRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));

        Page<Promotion> page = promotionRepository.findByHost_IdOrderByCreatedAtDesc(host.getId(), pageable);

        return PromotionListResponse.of(
                page.getContent().stream().map(PromotionItem::from).toList(),
                page.hasNext()
        );
    }


    /** 3) 전체 오픈 프로모션: ACTIVE && endDate(날짜) ≥ 오늘(KST) */
    public PromotionListResponse getOpenPromotions(Pageable pageable) {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        Page<Promotion> page = promotionRepository
                .findByPromotionStatusAndEndDateOnlyGreaterThanEqualOrderByStartDateAsc(
                        PromotionStatus.ACTIVE, today, pageable);
        return PromotionListResponse.of(page.getContent().stream().map(PromotionItem::from).toList(), page.hasNext());
    }

    /** 4) 전체 완료된 프로모션: COMPLETED */
    public PromotionListResponse getCompletedPromotions(Pageable pageable) {
        Page<Promotion> page = promotionRepository
                .findByPromotionStatusOrderByEndDateDesc(
                        PromotionStatus.COMPLETED, pageable);
        return PromotionListResponse.of(page.getContent().stream().map(PromotionItem::from).toList(), page.hasNext());
    }

    /** 5) 단건 상세 */
    public PromotionDetailResponse getPromotionDetail(final Long promotionId) {
        Promotion promotion = promotionRepository.findWithHostById(promotionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Promotion not found: " + promotionId));
        return PromotionDetailResponse.from(promotion);
    }

    private ResponseStatusException notFound(String target, Object id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, target + " not found: " + id);
    }
}