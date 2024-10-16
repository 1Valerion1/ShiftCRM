package shift.lab.crm.core.service;

import java.time.LocalDateTime;

public interface DateTimeService {
    LocalDateTime parseToLocalDateTime(String date, boolean startOfDay);
}
