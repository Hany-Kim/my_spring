package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService {

    /*
    할인 정책을 변경하는 방법 3

    클라이언트(OrderServiceImpl)에서는 할인정책 변경하더라도,
    수정(변경)되는 코드는 없다.

    즉, 클라이언트(OrderServiceImpl)의 생성자를 통해서 어떤 구현 객체를 주입할지는 외부(AppConfig)에서 결정한다.
    클라이언트(OrderServiceImpl)는 이제부터 "의존관계에 대한 고민은 외부"에 맡기고 "실행에만 집중" 한다.

     */

    // final : 생성할 때 정해지면 이후로는 안 바뀜 : 생성자 or 초기값에서만 값을 바꿀 수 있다.
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    /*
    @RequiredArgsConstructor : 아래 생성자 주입 코드와 동일한 동작을 수행한다.
    final이 붙은 '필수값(필드)'을 파라미터로 받는 생성자를 생성한다.

    */
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, @Qualifier("mainDiscountPolicy") DiscountPolicy discountPolicy) {
        /*
        @Qualifier("{구분자 명}") : 구분자 이름을 통해 매칭, 빈 이름 변경하는 것은 아님
        "필드 주입", "수정자 주입"방식에서도 동일하게 파라미터 앞에 붙여 사용 가능.

        주의!
        @Qualifier("{구분자 명}")를 사용할 때 구분자 이름을 매칭하지 못하면(찾지 못하면) 동일한 빈이름을 추가로 찾는다.
        이 기능 때문에 빈 이름을 변경해준다고 '오해'할 수 있다. @Qualifier는 @Qualifier를 찾는 용도로만 사용하자.
         */
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    // 테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
