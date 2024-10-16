package shift.lab.crm.core.service.serviceImpl;

import org.springframework.stereotype.Service;
import shift.lab.crm.core.service.DateTimeService;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class DateTimeServiceImpl implements DateTimeService {
    @Override
    public LocalDateTime parseToLocalDateTime(String date, boolean startOfDay) {
        LocalDate localDate = LocalDate.parse(date);
        return startOfDay ? localDate.atStartOfDay() : localDate.atTime(23, 59, 59);
    }
}
