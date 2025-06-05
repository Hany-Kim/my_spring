package hello.core;

import hello.core.discount.FixDiscountPolicy;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;

public class AppConfig {

    /*
    할인 정책을 변경하는 방법 3
    생성자 주입 방식
    환경을 구성 : 사용할 구현체를 정의하는 곳, 애플리케이션의 실제 동작에 필요한 구현 객체를 생성

    구현체가 주입(정의)될때, 클라이언트(OrderServiceImpl)의 생성자의 파라미터(인터페이스)로 전달된다.
    new OrderServiceImpl(new MemoryMemberRepository(), new FixDiscountPolicy());

    즉, AppConfig는 생성한 객체 인스턴스의 참조(레퍼런스)를 '생성자를 통해서 주입(연결)' 해준다.

    * 객체의 생성과 연결은 AppConfig가 담당
    * DIP 완성 : MemberServiceImpl은 MemberRepository인 추상에만 의존, 구현체를 몰라도 됨
    * 관심사의 분리 : 객체를 '생성'하고 '연결'하는 역할(AppConfig)과 실행하는 역할(~~~ServiceImpl)이 명확히 분리됨
    * 의존성(의존관계) 주입 : 클라이언트(~~~ServiceImpl)의 입장에서 보면, 마치 '의존관계'를 '외부'에서 '주입'해주는 것과 같다.
     */

    public MemberService memberService() {
        /*
         MemberService 인터페이스의 구현체(MemberServiceImpl)을 생성하고,
         MemoryMemberRepository 구현체의 인스턴스를 MemberRepository 인터페이스에 연결
         */
        return new MemberServiceImpl(new MemoryMemberRepository());
    }

    public OrderService orderService() {
        /*
         OrderService 인터페이스의 구현체(OrderServiceImpl)을 생성하고,
         MemoryMemberRepository,FixDiscountPolicy 구현체의 인스턴스를 각각 MemberRepository, DiscountPolicy 인터페이스에 연결
         */
        return new OrderServiceImpl(new MemoryMemberRepository(), new FixDiscountPolicy());
    }
}
