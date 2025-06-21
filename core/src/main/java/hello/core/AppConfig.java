package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
@Configuration으로 '설정 정보'혹은 '구성 정보'하는 곳을 표시(등록)
 */
@Configuration
public class AppConfig {

    /*
    Spring을 사용해 DI(의존성 주입) 컨테이너 관리

    각 메서드에 @Bean을 사용하면 spring컨테이너에 등록
     */

    @Bean
    public MemberService memberService() {
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public OrderService orderService() {
        System.out.println("call AppConfig.orderService");
//        return new OrderServiceImpl(memberRepository(), discountPolicy());
        return null;
    }

    /*
    @Configuration + @Bean을 통해 빈으로 등록할 때,
    static 메서드를 사용할 경우 싱글톤을 보장해주지 못함.
     */

    @Bean
    public MemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }

    @Bean
    public DiscountPolicy discountPolicy() {
//        return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }

    /*
    우리의 예상
    call AppConfig.memberService
    call AppConfig.memberRepository
    call AppConfig.memberRepository
    call AppConfig.orderService
    call AppConfig.memberRepository

    실제 결과
    call AppConfig.memberService
    call AppConfig.memberRepository
    call AppConfig.orderService
    */
}
