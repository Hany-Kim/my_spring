package hello.core.singleton;

import static org.junit.jupiter.api.Assertions.*;

import hello.core.beanfind.ApplicationContextExtendsFindTest.TestConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

class StatefulServiceTest {

    @Test
    void statefulServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        // ThreadA: userA 10000원 주문
        statefulService1.order("userA", 10000);
        // ThreadB: userB 20000원 주문
        statefulService2.order("userB", 20000);

        // ThreadA: userA 주문 금액 조회
        int price = statefulService1.getPrice();
        System.out.println("price = " + price);

        /*
        문제
        userA는 10000원 주문했지만, 20000원이 조회된다.

        Thread를 예시에 적용하진 않았지만, 실제 서비스(웹)에선 각 Thread에서 userA, userB의 요청을 처리한다.

        스프링 빈은 무상태(stateless)로 설계해야한다.
         */

        Assertions.assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }

    static class TestConfig {

        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }

}