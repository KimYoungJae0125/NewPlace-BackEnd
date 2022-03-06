package shop.newplace.common.util;

import java.time.LocalDateTime;

public class DateUtil {

    public static LocalDateTime getToday() {
        return LocalDateTime.now();
    }
    
    public static LocalDateTime todayPlusMonths(int months) {
    	return LocalDateTime.now().plusMonths(months);
    }
    
    public static LocalDateTime plusMonths(LocalDateTime localDateTime, int months) {
    	return localDateTime.plusMonths(months);
    }
    
    public static Boolean isBeforeToday(LocalDateTime localDateTime) {
    	return localDateTime.isBefore(getToday());
    }

    public static Boolean isAfterToday(LocalDateTime localDateTime) {
    	return localDateTime.isAfter(getToday());
    }

}
