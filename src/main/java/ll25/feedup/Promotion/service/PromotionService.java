package ll25.feedup.promotion.service;

import ll25.feedup.global.exception.BusinessException;
import ll25.feedup.global.exception.ExceptionCode;
import ll25.feedup.host.domain.Host;
import ll25.feedup.host.repository.HostRepository;
import ll25.feedup.plan.domain.Plan;
import ll25.feedup.plan.repository.PlanRepository;
import ll25.feedup.promotion.domain.Promotion;
import ll25.feedup.promotion.domain.PromotionStatus;
import ll25.feedup.promotion.dto.PromotionCreateRequest;
import ll25.feedup.promotion.dto.PromotionCreateResponse;
import ll25.feedup.promotion.dto.PromotionDetailResponse;
import ll25.feedup.promotion.dto.PromotionItem;
import ll25.feedup.promotion.dto.PromotionListResponse;
import ll25.feedup.promotion.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final HostRepository hostRepository;
    private final PlanRepository planRepository;

    /** 프로모션 생성 **/
    @Transactional
    public PromotionCreateResponse createPromotion(String loginId, PromotionCreateRequest request) {
        Host host = hostRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.UNAUTHORIZED));
        Plan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new BusinessException(ExceptionCode.PLAN_NOT_FOUND));

        Promotion promotion = request.toEntity(host, plan);
        Promotion saved = promotionRepository.save(promotion);
        return PromotionCreateResponse.from(saved, plan);
    }

    /** 로그인한 호스트의 전체 프로모션(상태 무관) **/
    @Transactional(readOnly = true)
    public PromotionListResponse getHostPromotionsByLoginId(String loginId, Pageable pageable) {
        Host host = hostRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.UNAUTHORIZED));

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