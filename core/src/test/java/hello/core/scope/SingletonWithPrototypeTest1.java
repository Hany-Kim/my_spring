package hello.core.scope;

import static org.assertj.core.api.Assertions.*;

import hello.core.scope.PrototypeTest.PrototypeBean;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
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
        private ObjectProvider<PrototypeBean> prototypeBeanProvider; // 인터페이스로 ObjectFactory를 상속받음
//        private ObjectFactory<PrototypeBean> prototypeBeanProvider;
        // ObjectProvider, ObjectFactory는 스프링에 의존횐다.

        public int logic() {
            PrototypeBean prototypeBean = prototypeBeanProvider.getObject(); // 새로운 프로토타입 빈 생성
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
