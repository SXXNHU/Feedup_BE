package ll25.feedup.GeneratedSns.service;

import jakarta.transaction.Transactional;
import ll25.feedup.GeneratedSns.domain.GeneratedSns;
import ll25.feedup.GeneratedSns.domain.SnsStyle;
import ll25.feedup.GeneratedSns.dto.GenerateSnsRequest;
import ll25.feedup.GeneratedSns.dto.GeneratedSnsItemResponse;
import ll25.feedup.GeneratedSns.dto.OptionsResponse;
import ll25.feedup.GeneratedSns.dto.PreviewResponse;
import ll25.feedup.GeneratedSns.infra.OpenAiChatClient;
import ll25.feedup.GeneratedSns.repository.GeneratedSnsRepository;
import ll25.feedup.GeneratedSns.support.PromptBuilder;
import ll25.feedup.Host.domain.Host;
import ll25.feedup.Host.repository.HostRepository;
import ll25.feedup.Promotion.domain.Promotion;
import ll25.feedup.Promotion.domain.PromotionStatus;
import ll25.feedup.Promotion.dto.PromotionItem;
import ll25.feedup.Promotion.dto.PromotionListResponse;
import ll25.feedup.Promotion.repository.PromotionRepository;
import ll25.feedup.Review.domain.Review;
import ll25.feedup.Review.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GeneratedSnsService {

    private final PromotionRepository promotionRepository;
    private final HostRepository hostRepository;
    private final ReviewRepository reviewRepository;
    private final GeneratedSnsRepository generatedSnsRepository;
    private final OpenAiChatClient openAiChatClient;

    public GeneratedSnsService(PromotionRepository promotionRepository,
                               HostRepository hostRepository,
                               ReviewRepository reviewRepository,
                               GeneratedSnsRepository generatedSnsRepository,
                               OpenAiChatClient openAiChatClient) {
        this.promotionRepository = promotionRepository;
        this.hostRepository = hostRepository;
        this.reviewRepository = reviewRepository;
        this.generatedSnsRepository = generatedSnsRepository;
        this.openAiChatClient = openAiChatClient;
    }

    /** COMPLETED 대상(내 것만) 목록 — 프론트가 타겟 선택할 때 사용하려면 호출 */
    @Transactional
    public PromotionListResponse listCompletedTargets(String hostLoginId, Pageable pageable) {
        Host host = mustFindHost(hostLoginId);
        Page<Promotion> page = promotionRepository
                .findByHost_IdAndPromotionStatusOrderByCreatedAtDesc(host.getId(), PromotionStatus.COMPLETED, pageable);
        return PromotionListResponse.of(page.getContent().stream().map(PromotionItem::from).toList(), page.hasNext());
    }

    @Transactional
    public OptionsResponse generate(String hostLoginId, GenerateSnsRequest request) {
        if (request == null || request.getPromotionId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "promotionId가 필요합니다.");
        }
        Promotion promotion = mustFindPromotion(request.getPromotionId());
        Host host = mustFindHost(hostLoginId);
        assertOwnedBy(promotion, host);

        List<Review> reviews = reviewRepository.findApprovedByPromotionId(promotion.getId());
        if (reviews == null || reviews.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "리뷰가 최소 1개 필요합니다.");
        }
        boolean invalid = reviews.stream().anyMatch(r -> {
            double rate = r.getRate();
            return Double.isNaN(rate) || rate <= 0.0 || rate > 5.0;
        });
        if (invalid) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "모든 리뷰의 별점(rate)이 필요합니다.");
        }

        Boolean force = request.getForce();
        boolean forceGenerate = force != null && force;
        if (!forceGenerate) {
            List<GeneratedSns> exists = generatedSnsRepository.findAllByPromotion_Id(promotion.getId());
            if (exists != null && exists.size() == 2) {
                return toOptionsResponse(promotion.getId(), exists);
            }
        }

        String system = PromptBuilder.buildSystem();
        String userPretty = PromptBuilder.buildUserBlock(promotion, reviews)
                + "\n" + PromptBuilder.buildStyle(SnsStyle.PRETTY);
        String userClean = PromptBuilder.buildUserBlock(promotion, reviews)
                + "\n" + PromptBuilder.buildStyle(SnsStyle.CLEAN);

        OpenAiChatClient.LlmCaptionResult pretty =
                openAiChatClient.generateJson(system, userPretty, Duration.ofSeconds(15));
        OpenAiChatClient.LlmCaptionResult clean  =
                openAiChatClient.generateJson(system, userClean,  Duration.ofSeconds(15));

        GeneratedSns rowPretty = upsertGenerated(promotion, host, SnsStyle.PRETTY, pretty.getContent());
        GeneratedSns rowClean  = upsertGenerated(promotion, host, SnsStyle.CLEAN,  clean.getContent());

        return toOptionsResponse(promotion.getId(), List.of(rowPretty, rowClean));
    }

    @Transactional
    public void select(String hostLoginId, Long promotionId, String styleLiteral) {
        if (promotionId == null || styleLiteral == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "promotionId와 style이 필요합니다.");
        }
        Promotion promotion = mustFindPromotion(promotionId);
        Host host = mustFindHost(hostLoginId);
        assertOwnedBy(promotion, host);

        SnsStyle style;
        try { style = SnsStyle.valueOf(styleLiteral.toUpperCase(Locale.ROOT)); }
        catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "style은 PRETTY 또는 CLEAN이어야 합니다.");
        }

        Optional<GeneratedSns> target = generatedSnsRepository.findByPromotion_IdAndStyle(promotionId, style);
        if (target.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 스타일의 생성 결과가 없습니다. 먼저 생성하세요.");
        }

        generatedSnsRepository.clearSelection(promotionId);
        generatedSnsRepository.markSelected(promotionId, style);
    }

    @Transactional
    public OptionsResponse options(String hostLoginId, Long promotionId) {
        if (promotionId == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "promotionId가 필요합니다.");
        Promotion promotion = mustFindPromotion(promotionId);
        Host host = mustFindHost(hostLoginId);
        assertOwnedBy(promotion, host);

        List<GeneratedSns> exists = generatedSnsRepository.findAllByPromotion_Id(promotionId);
        if (exists == null || exists.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "생성된 옵션이 없습니다. 먼저 생성하세요.");
        }
        return toOptionsResponse(promotionId, exists);
    }

    @Transactional
    public PreviewResponse preview(String hostLoginId, Long promotionId) {
        if (promotionId == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "promotionId가 필요합니다.");
        Promotion promotion = mustFindPromotion(promotionId);
        Host host = mustFindHost(hostLoginId);
        assertOwnedBy(promotion, host);

        GeneratedSns selected = generatedSnsRepository.findByPromotion_IdAndSelectedTrue(promotionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "선택된 컨텐츠가 없습니다."));

        // 사진은 리뷰 테이블에서만 수집
        List<String> mediaUrls = reviewRepository.findApprovedByPromotionId(promotionId).stream()
                .flatMap(r -> r.getPhotoUrls().stream())
                .distinct()
                .toList();

        return new PreviewResponse(
                selected.getId(),
                promotionId,
                selected.getStyle().name(),
                selected.getContent(),
                mediaUrls
        );
    }

    private GeneratedSns upsertGenerated(Promotion promotion, Host host, SnsStyle style, String content) {
        Optional<GeneratedSns> exist = generatedSnsRepository.findByPromotion_IdAndStyle(promotion.getId(), style);
        if (exist.isPresent()) {
            GeneratedSns row = exist.get();
            row.updateContent(content);
            return row;
        }
        return generatedSnsRepository.save(new GeneratedSns(promotion, host, style, content, false, host.getId()));
    }

    private OptionsResponse toOptionsResponse(Long promotionId, List<GeneratedSns> rows) {
        List<String> mediaUrls = reviewRepository.findApprovedByPromotionId(promotionId).stream()
                .flatMap(r -> r.getPhotoUrls().stream())
                .distinct()
                .toList();

        List<GeneratedSnsItemResponse> items = rows.stream()
                .sorted(Comparator.comparing(a -> a.getStyle().name()))
                .map(row -> new GeneratedSnsItemResponse(
                        row.getId(), row.getStyle().name(), row.getContent(), row.isSelected(), mediaUrls
                ))
                .collect(Collectors.toList());
        return new OptionsResponse(promotionId, items);
    }

    private Promotion mustFindPromotion(Long promotionId) {
        return promotionRepository.findById(promotionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "promotion이 존재하지 않습니다."));
    }
    private Host mustFindHost(String loginId) {
        return hostRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "host 인증 실패"));
    }
    private void assertOwnedBy(Promotion promotion, Host host) {
        if (!promotion.getHost().getId().equals(host.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 프로모션이 아닙니다.");
        }
    }
}
