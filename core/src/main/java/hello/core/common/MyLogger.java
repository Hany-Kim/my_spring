package hello.core.common;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.UUID;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/*
* proxyMode = ScopedProxyMode.TARGET_CLASS
* ObjectProvider로 감싸는 과정을 생략
* MyLogger의 가짜 프록시 클래스를 만들어두고, 가짜 프록시 클래스를 다른 빈에 주입해둠.
*
* TARGET_CLASS : 적용대상이 클래스 (MyLogger 클래스)
* INTERFACES : 적용대상이 인터페이스
* */

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyLogger {

    private String uuid;
    private String requestURL;

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String message) {
        System.out.println("[" + uuid + "]" + "[" + requestURL + "]" + message);
    }

    @PostConstruct
    public void init() {
        /* 요청이 들어올 때 호출 */
        uuid = UUID.randomUUID().toString();
        System.out.println("[" + uuid + "] request scope bean create: " + this);
    }

    @PreDestroy
    public void close() {
        /* 요청이 빠져나갈 때 호출 */
        System.out.println("[" + uuid + "] request scope bean close: " + this);
    }
}
