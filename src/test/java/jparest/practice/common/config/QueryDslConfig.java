package jparest.practice.common.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jparest.practice.group.repository.GroupQueryRepositoryImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@TestConfiguration
public class QueryDslConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public GroupQueryRepositoryImpl groupQueryRepository() {
        return new GroupQueryRepositoryImpl(jpaQueryFactory());
    }
}
