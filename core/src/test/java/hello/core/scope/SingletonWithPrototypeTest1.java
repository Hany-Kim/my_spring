package hello.core.scope;

import static org.assertj.core.api.Assertions.*;

import hello.core.scope.PrototypeTest.PrototypeBean;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

public class SingletonWithPrototypeTest1 {

    @Test
    void prototypeFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        prototypeBean1.addCount();
        assertThat(prototypeBean1.getCount()).isEqualTo(1);

        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        prototypeBean2.addCount();
        assertThat(prototypeBean2.getCount()).isEqualTo(1);
    }

    @Test
    void singletonClientUsePrototype() {
        /*
        ClientBean은 싱글톤 스코프에 생명주기를 가짐.
        PrototypeBean은 프로토타입 스코프에 생명주기를 가짐.

        ClientBean은 스프링이 실행될 때 컨테이너에 빈이 등록됨.
        ClientBean이 생성될 때, PrototypeBean이 의존관계에 의해 함께 생성됨.
        PrototypeBean은 프로토타입 스코프이기에 생성된 후 ClientBean(클라이언트)에게 관리를 넘김.
        즉, PrototypeBean은 스프링이 생명주기를 관리하지 않음.

        clientBean1, clientBean2는 같은 ClientBean을 참조함.
        그렇기 때문에 clientBean1, clientBean2는 같은 PrototypeBean를 참조함. => 문제발생
         */
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);

        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(1);
    }

    @Scope("singleton")
    static class ClientBean {
        @Autowired
        private ApplicationContext ac;

        public int logic() {
            /*
            * 해결방법1 싱글톤 빈이 프로토타입을 사용할 때마다 스프링 컨테이너에 새로 요청
            *
            * 의존관계를 외부에서 주입(DI) 받는게 아니라
            * 직접 필요한 의존관계를 찾는 것을 Dependency Lookup(DL) 의존관계 조회(탐색)이라 한다.
            *
            * 이렇게 스프링의 ApplicationContext 전체를 주입받으면,
            * 스프링 컨테이너에 종속적인 코드가 되고, 단위 테스트가 어려워진다.
            *
            * 지금 필요한 기능은 지정한 프로토타입 빈을 컨테이너에서 대신 찾아주는 딱 DL 정도의 기능만 필요하다.
            */
            PrototypeBean prototypeBean = ac.getBean(PrototypeBean.class);

            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }
    }

    @Scope("prototype")
    static class PrototypeBean {
        private int count = 0;

        public void addCount() {
            count++;
        }

        public int getCount() {
            return count;
        }

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init" + this);
        }

        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy" + this);
        }
    }
}
