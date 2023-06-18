package jparest.practice.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "jparest.practice",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASPECTJ, pattern = "jparest.practice.auth.jwt.*"
        )
)
public class JpaConfig {
}