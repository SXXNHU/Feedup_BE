package ll25.feedup.global.upload;

import ll25.feedup.promotion.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadService {

    private final S3Client s3;
    private final PromotionRepository promotionRepository;

    @Value("${app.s3.bucket}") private String bucket;
    @Value("${app.s3.region}") private String region;

    private static final long MAX_FILE_SIZE = 10L * 1024 * 1024; // 10MB
    private static final int MAX_REVIEW_PHOTOS = 10;

    public List<String> uploadReviewPhotos(Long promotionId, List<MultipartFile> photos) {
        if (promotionId == null) throw badRequest("promotionId is required.");
        if (!promotionRepository.existsById(promotionId)) {
            throw notFound(promotionId);
        }
        if (photos == null || photos.isEmpty()) throw badRequest("photos is required.");
        if (photos.size() > MAX_REVIEW_PHOTOS) throw badRequest("Too many photos (max " + MAX_REVIEW_PHOTOS + ").");

        List<String> urls = new ArrayList<>();
        for (MultipartFile f : photos) {
            if (f == null || f.isEmpty()) continue;
            if (f.getSize() > MAX_FILE_SIZE) throw badRequest("file too large (max 10MB).");
            String ext = safeExt(f);
            String key = "reviews/promotion-" + promotionId + "/" + UUID.randomUUID() + (ext.isBlank() ? "" : "." + ext);
            putObject(key, f);
            urls.add(publicUrl(key));
        }
        if (urls.isEmpty()) throw badRequest("no valid files.");
        return urls;
    }

    public String uploadHostThumbnail(String loginId, MultipartFile thumbnail) {
        if (loginId == null || loginId.isBlank()) throw badRequest("loginId is required.");
        if (thumbnail == null || thumbnail.isEmpty()) throw badRequest("thumbnail is required.");
        if (thumbnail.getSize() > MAX_FILE_SIZE) throw badRequest("file too large (max 10MB).");

        String ext = safeExt(thumbnail);
        String key = "stores/cover/" + loginId + "-" + UUID.randomUUID() + (ext.isBlank() ? "" : "." + ext);
        putObject(key, thumbnail);
        return publicUrl(key);
    }

    private void putObject(String key, MultipartFile file) {
        try {
            PutObjectRequest put = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();
            s3.putObject(put, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "upload failed", e);
        }
    }

    private String publicUrl(String key) {
        return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;
    }

    private String safeExt(MultipartFile f) {
        String name = f.getOriginalFilename();
        String ext = name == null ? "" : FilenameUtils.getExtension(name).toLowerCase();
        if (ext.matches("(?i)jpg|jpeg|png|webp|gif|bmp|heic|heif")) return ext.toLowerCase();
        String ct = f.getContentType() == null ? "" : f.getContentType().toLowerCase();
        if (ext.isBlank()) {
            if (ct.contains("jpeg")) return "jpg";
            if (ct.contains("png")) return "png";
            if (ct.contains("webp")) return "webp";
        }
        return ext;
    }

    private ResponseStatusException badRequest(String m) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, m);
    }

    private ResponseStatusException notFound(Object id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "프로모션을 찾을 수 없습니다 : " + id);
    }
}
