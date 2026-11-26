package hei.school;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Note sur 20 et pénalité de retard : -10 % par jour entamé, plafonnée à -50 %.
 */
public final class Grades {

    private static final int PENALTY_PER_DAY = 10;
    private static final int MAX_PENALTY = 50;

    private Grades() {
    }

    /** Jours de retard entamés (0 si rendu le jour de l'échéance ou avant). */
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

    /** 14.4 → "14,4" pour les textes français. */
    public static String french(BigDecimal grade) {
        return grade.toPlainString().replace('.', ',');
    }
}
