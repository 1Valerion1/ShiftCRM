package shift.lab.crm.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shift.lab.crm.api.controller.AnalyticController;
import shift.lab.crm.core.service.AnalyticService;

@ExtendWith(MockitoExtension.class)
public class AnalyticControllerTest {
    @InjectMocks
    private AnalyticController analyticController;
    @Mock
    private AnalyticService analyticService;

}
