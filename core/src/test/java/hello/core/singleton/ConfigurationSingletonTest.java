package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberRepository;
import hello.core.member.MemberServiceImpl;
import hello.core.order.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ConfigurationSingletonTest {

    @Test
    void configurationTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        // 구체(구현체)를 꺼내는 것이 좋지는 않지만 테스트 용도
        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class);
        MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);

        MemberRepository memberRepository1 = memberService.getMemberRepository();
        MemberRepository memberRepository2 = orderService.getMemberRepository();

        System.out.println("memberService -> memberRepository = " + memberRepository1);
        System.out.println("orderService -> memberRepository = " + memberRepository2);
        System.out.println("memberRepository = " + memberRepository);

        Assertions.assertThat(memberService.getMemberRepository()).isSameAs(memberRepository);
        Assertions.assertThat(orderService.getMemberRepository()).isSameAs(memberRepository);
    }

    @Test
    void configurationDeep() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        AppConfig bean = ac.getBean(AppConfig.class);

        System.out.println("bean = " + bean.getClass()); // bean = class hello.core.AppConfig$$SpringCGLIB$$0
        /*
        출력 결과에 $$SpringCGLIB$$0 가 뒤에 붙어 있다.

        순수한 클래스라면, class hello.core.AppConfig 만 출력된다.

        xxCGLIB는 사용자가 만든 클래스가 아닌,
        스프링이 CGLIB라는 바이트코드 조작 라이브러리를 사용해서 AppConfig 클래스를 상속받은 임의의 다른 클래스를 만들고,
        그 클래스를 스프링 빈으로 등록한 것이다.

        @Configuration을 삭제하면 CGLIB로 생성되는 AppConfig를 상속받은 임의의 다른 클래스 클래스가 생성되지 않고,
        직접 생성한 AppConfig클래스가 그대로 사용된다.
        (bean = class hello.core.AppConfig)
        그러나 call AppConfig.memberRepository가 3번 출력되면서,
        싱글톤이 깨진다.

         */
    }
}
