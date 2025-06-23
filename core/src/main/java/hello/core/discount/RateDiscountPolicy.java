package hello.core.discount;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/*
@Primary : 주입 의존성 정의, 최우선

메인 DB 커넥션, 보조 DB 커넥션 관련 주입 설정들이 있을 때,
메인 DB 커넥션이 90%, 보조 DB 커넥션이 5~10%정도 사용될 때,
메인 DB 커넥션은 @Primary로 정의, 보조 DB 커넥션은 @Qualifier로 정의
@Qualifier의 단점이 주입 받을 때마다 모든 코드에 @Qualifier를 사용해야 하기 때문.
 */

@Component
@MainDiscountPolicy
public class RateDiscountPolicy implements DiscountPolicy{

    private int discountPercent = 10;

    @Override
    public int discount(Member member, int price) {
        if(member.getGrade() == Grade.VIP) { // enum은 ==으로 비교연산자 사용
            return price * discountPercent / 100;
        } else {
            return 0;
        }
    }
}
