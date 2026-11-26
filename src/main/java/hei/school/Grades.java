package hei.school;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Grade out of 20 and late penalty: -10 % per started day, capped at -50 %.
 */
public final class Grades {

    private static final int PENALTY_PER_DAY = 10;
    private static final int MAX_PENALTY = 50;

    private Grades() {
    }

    /** Started days of lateness (0 when submitted on the deadline day or before). */
    public static int lateDays(LocalDate deadline, LocalDateTime submittedAt) {
        long days = ChronoUnit.DAYS.between(deadline, submittedAt.toLocalDate());
        return (int) Math.max(0, days);
    }

    public static int penaltyPercent(int lateDays) {
        return Math.min(MAX_PENALTY, PENALTY_PER_DAY * lateDays);
    }

    public static BigDecimal gradeOutOf20(BigDecimal raw, BigDecimal max) {
        return raw.multiply(BigDecimal.valueOf(20)).divide(max, 1, RoundingMode.HALF_UP);
    }

    public static BigDecimal finalGrade(BigDecimal raw, BigDecimal max, int lateDays) {
        int penalty = penaltyPercent(lateDays);
        return gradeOutOf20(raw, max)
                .multiply(BigDecimal.valueOf(100 - penalty))
                .divide(BigDecimal.valueOf(100), 1, RoundingMode.HALF_UP);
    }

    /** 14.4 -> "14,4", for French user-facing texts. */
    public static String formatGrade(BigDecimal grade) {
        return grade.toPlainString().replace('.', ',');
    }
}
