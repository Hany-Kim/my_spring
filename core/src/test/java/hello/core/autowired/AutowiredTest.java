package hello.core.autowired;

import hello.core.member.Member;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

public class AutowiredTest {

    @Test
    void AutowiredOption(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);

    }

    static class TestBean{

        @Autowired(required = false)
        public void setNoBean1(Member noBean1) {
            /*
            required = false : 의존 관계가 없어, 메서드 호출이 안된다.
            자동 주입할 대상이 없으면 수정자 메서드 자체가 호출이 안됨.
            */
            System.out.println("noBean1 = " + noBean1);
        }

        @Autowired
        public void setNoBean2(@Nullable Member noBean2) {
            /*
            @Nullable : 파라미터값이 null이어도 메서드 호출은 된다.
            */
            System.out.println("noBean2 = " + noBean2);
        }

        @Autowired
        public void setNoBean3(Optional<Member> noBean3) {
            /*
            Optional : 파라미터값이 null이면 'Optional.empty'이 변수에 담긴다.
            */
            System.out.println("noBean3 = " + noBean3);
        }
    }

}
