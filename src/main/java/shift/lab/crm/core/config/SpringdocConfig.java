package shift.lab.crm.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringdocConfig {
    private final String DOC_TITLE = "Документация по CRM - системе";
    private final String DOC_VERSION = "1.0.0";
    private final String DOC_DESCRIPTION = " Данная CRM-система, разработана для " +
            "управления информацией о продавцах и их транзакциях. " +
            "Система поддерживает операции создания, чтения, обновления и удаления (CRUD) для продавцов и транзакций, " +
            "а также включает функции аналитики для определения самых продуктивных продавцов и анализа транзакционных данных.";

    @Bean
    public OpenAPI baseOpenApi() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title(DOC_TITLE)
                                .version(DOC_VERSION)
                                .description(DOC_DESCRIPTION)
                );
    }
}
