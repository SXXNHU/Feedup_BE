package ll25.feedup.review.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ll25.feedup.global.exception.BusinessException;
import ll25.feedup.global.exception.ExceptionCode;

import java.util.ArrayList;
import java.util.List;

@Converter
public class ListStringJsonConverter implements AttributeConverter<List<String>, String> {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        try {
            if (attribute == null || attribute.isEmpty()) return "[]";
            return MAPPER.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new BusinessException(ExceptionCode.BAD_REQUEST, "photoUrls 직렬화 실패", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isBlank()) return new ArrayList<>();
            return MAPPER.readValue(dbData, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new IllegalArgumentException("photoUrls 역직렬화 실패", e);
        }
    }
}
