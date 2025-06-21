package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/*
@Component 로 FixDiscountPolicy 클래스을 Spring Bean으로 등록하면,
기존에 등록되어 있던 RateDiscountPolicy로 인해
DiscountPolicy의 구현된 클래스가 FixDiscountPolicy와 RateDiscountPolicy 두 클래스이기에
OrderServiceImpl의 생성자 주입시, DiscountPolicy의 구현 클래스를 찾을 때 2개의 빈등록으로 인해 오류(NoUniqueBeanDefinitionException)를 반환한다.
 */

@Component
@Qualifier("fixDiscountPolicy")
public class FixDiscountPolicy implements DiscountPolicy {

    private int discountFixAmount = 1000;

    @Override
    public int discount(Member member, int price) {
        if(member.getGrade() == Grade.VIP) { // enum은 ==으로 비교연산자 사용
            return discountFixAmount;
        } else {
            return 0;
        }
    }
}
