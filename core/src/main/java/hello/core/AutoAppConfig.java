package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/*
@ComponentScan 을 붙이면, @Component가 붙은 클래스를 찾아서 Spring Bean으로 등록한다.
excludeFilters를 사용해 찾지 않을 클래스를 제외할 수 있다. // -> AppConfig.class 제외
*/

@Configuration
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = Configuration.class
        )
)
public class AutoAppConfig {

}
