package ll25.feedup.promotionApply.service;

import ll25.feedup.global.exception.BusinessException;
import ll25.feedup.global.exception.ExceptionCode;
import ll25.feedup.mate.domain.Mate;
import ll25.feedup.mate.repository.MateRepository;
import ll25.feedup.promotion.domain.Promotion;
import ll25.feedup.promotion.domain.PromotionStatus;
import ll25.feedup.promotion.repository.PromotionRepository;
import ll25.feedup.promotionApply.domain.PromotionApply;
import ll25.feedup.promotionApply.dto.ParticipationItem;
import ll25.feedup.promotionApply.dto.ParticipationListResponse;
import ll25.feedup.promotionApply.dto.PromotionApplyRequest;
import ll25.feedup.promotionApply.dto.PromotionApplyResponse;
import ll25.feedup.promotionApply.repository.PromotionApplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionApplyService {

    private static final int MIN_SIZE = 1;
    private static final int MAX_SIZE = 50;

    private final PromotionApplyRepository promotionApplyRepository;
    private final PromotionRepository promotionRepository;
    private final MateRepository mateRepository;

    /** 특정 프로모션 신청 **/
    @Transactional
    public PromotionApplyResponse apply(String mateLoginId, PromotionApplyRequest req) {

        final Long promotionId = requirePromotionId(req);
        final Mate mate = mateRepository.findByLoginId(mateLoginId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.UNAUTHORIZED));
        final Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.PROMO_NOT_FOUND));

        validatePromotionAvailability(promotion);
        ensureNotAlreadyApplied(mate, promotion);

        final PromotionApply apply = new PromotionApply();
        apply.setMate(mate);
        apply.setPromotion(promotion);

        final PromotionApply saved = promotionApplyRepository.save(apply);
        promotion.setCurrentTeam(promotion.getCurrentTeam() + 1); // 정책: 즉시 증가

        return PromotionApplyResponse.from(saved);
    }

    /** 사용자가 참여중인 프로모션 목록 (page/size) **/
    @Transactional(readOnly = true)
    public ParticipationListResponse myApplies(String mateLoginId, int page, int size) {
        final Mate me = mateRepository.findByLoginId(mateLoginId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.UNAUTHORIZED));

        final int safeSize = Math.max(MIN_SIZE, Math.min(MAX_SIZE, size));
        final int safePage = Math.max(0, page);

        final PageRequest pageable = PageRequest.of(safePage, safeSize);
        final Page<PromotionApply> result = promotionApplyRepository.findByMateOrderByIdDesc(me, pageable);

        final List<ParticipationItem> items = result.getContent().stream()
                .map(ParticipationItem::from)
                .toList();

        final boolean hasNext = result.hasNext();

        return ParticipationListResponse.of(items, safePage, safeSize, hasNext);
    }

    /** promotionId 필수값 검증 **/
    private static Long requirePromotionId(PromotionApplyRequest req) {
        if (req == null || req.getPromotionId() == null) {
            throw new BusinessException(ExceptionCode.BAD_REQUEST, "promotionId는 필수입니다.");
        }
        return req.getPromotionId();
    }

    /** 프로모션 상태/정원 검증 **/
    private static void validatePromotionAvailability(Promotion promotion) {
        if (promotion.getStatus() != PromotionStatus.ACTIVE) {
            throw new BusinessException(ExceptionCode.PROMO_NOT_ACTIVE);
        }
        if (promotion.getCurrentTeam() >= promotion.getTotalTeam()) {
            throw new BusinessException(ExceptionCode.PROMO_FULL);
        }
    }

    /** 중복 신청 방지 검증 **/
    private void ensureNotAlreadyApplied(Mate mate, Promotion promotion) {
        final boolean exists = promotionApplyRepository
                .existsByMate_IdAndPromotion_Id(mate.getId(), promotion.getId());
        if (exists) {
            throw new BusinessException(ExceptionCode.APPLY_ALREADY);
        }
    }
}