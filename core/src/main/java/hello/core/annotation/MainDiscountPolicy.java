package hello.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.beans.factory.annotation.Qualifier;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Qualifier("mainDiscountPolicy")
public @interface MainDiscountPolicy {
    /*
    @Qualifier("{구분자 명}")을 사용시, "구분자 명"에 오타가 있다면 '컴파일 타임'에서 오류를 체크할 수 없다.
    ex. 오타로 인한 버그가 발생할 수 있다.

    어노테이션을 직접 만들어서 사용하면 '컴파일 타임'에서의 오류를 방지할 수 있다.
     */
}
