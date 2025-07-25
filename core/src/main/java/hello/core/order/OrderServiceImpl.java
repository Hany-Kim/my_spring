package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
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
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
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
