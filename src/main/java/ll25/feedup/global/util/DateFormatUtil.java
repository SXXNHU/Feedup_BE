package ll25.feedup.global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class DateFormatUtil {

    private DateFormatUtil() {}

    public static final ZoneId ZONE = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter FMT_TODAY = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter FMT_SAME_YEAR = DateTimeFormatter.ofPattern("MM/dd");
    private static final DateTimeFormatter FMT_DIFF_YEAR = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    /** 오늘: HH:mm, 같은 해: MM/dd, 그 외: yyyy/MM/dd */
    public static String formatCreatedAt(LocalDateTime dt) {
        LocalDate today = LocalDate.now(ZONE);
        LocalDate d = dt.toLocalDate();
        if (d.isEqual(today)) return dt.format(FMT_TODAY);
        if (d.getYear() == today.getYear()) return dt.format(FMT_SAME_YEAR);
        return dt.format(FMT_DIFF_YEAR);
    }
}
