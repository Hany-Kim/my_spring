package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository = new MemoryMemberRepository();

    /*
    할인 정책을 변경하는 방법 1
    구현체를 변경한다.
    문제 1: 할인 정책을 변경하려면 OrderServiceImpl 클래스를 변경해야 하다.

    * 역할과 구현을 충실히 분리했는가? Yes
        (DiscountPolicy 역할과 FixDiscountPolicy, RateDiscountPolicy 구현을 분리함)
    * 다형성도 활용하고, 인터페이스와 구현 객체를 분리했는가? Yes

    * OCP, DIP 같은 객체지향 설계 원칙을 준수했는가? NO!!!!!!!!
        -> 그렇게 보이지만 사실은 아니다.
    * DIP : 주문서비스(OrderServiceImpl)은 DiscountPolicy 인터페이스에 의존하여 DIP를 잘 지킨것처럼 보인다.
        -> 클래스 의존관계를 분석 : 추상(인터페이스) 뿐만 아니라 구체(구현) 클래스에도 '함께' 의존하고 있다.
            - 추상(인터페이스): DiscountPolicy
            - 구체(구혋) : FixDiscountPolicy, RateDiscountPolicy
    *
     */
//    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    private final DiscountPolicy discountPolicy = new RateDiscountPolicy();

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
