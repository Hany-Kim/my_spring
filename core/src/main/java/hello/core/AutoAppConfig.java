package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/*
@ComponentScan 을 붙이면, @Component가 붙은 클래스를 찾아서 Spring Bean으로 등록한다.
excludeFilters를 사용해 찾지 않을 클래스를 제외할 수 있다. // -> AppConfig.class 제외

basePackages와 basePackageClasses를 정의하지 않으면,
디폴트(basePackageClasses = AutoAppConfig.class)는 프로젝트 패키지는 모두 확인
== @ComponentScan이 붙은 클래스의 패키지(package hello.core;)를 탐색
*/

@Configuration
@ComponentScan(
//        basePackages = "hello.core.member", // 하위 패키지만 탐색
//        basePackageClasses = AutoAppConfig.class, // AutoAppConfig 클래스의 패키지(package hello.core;)를 탐색
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = Configuration.class
        )
)
public class AutoAppConfig {

}
