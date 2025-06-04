package hello.hellospring;

import hello.hellospring.aop.TimeTraceAop;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    private final MemberRepository memberRepository;

    @Autowired
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository);
    }

    /*@Bean
    public MemberRepository memberRepository() {
        *//*
        객체지향 특성 중 '다형성'을 이용한 방식 -> 개방-폐쇄 원칙 : 확장에는 열려있고, 수정(변경)에는 닫혀있다.

        spring DI(의존성 주입) : 기존 코드를 전혀 손대지 않고, 설정만으로 구현클래스를 변경할 수 있다.

        repository 구현체를 바꿔끼우기 할 수 있다.
         *//*
//        return new MemoryMemberRepository();
//        return new JdbcMemberRepository(dataSource);
//        return new JdbcTemplateMemberRepository(dataSource);
        return new JpaMemberRepository(em);
    }*/

    /*
    // AOP를 컨테이너에 등록한는 방법 1
    @Bean
    public TimeTraceAop timeTraceAop() {
        return new TimeTraceAop();
    }
    */
}
