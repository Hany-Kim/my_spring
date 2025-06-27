package hello.core.web;

import hello.core.common.MyLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
    private final MyLogger myLogger;

    @RequestMapping("/log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) throws InterruptedException {
        String requestURL = request.getRequestURL().toString();

        System.out.println("mylogger.getClass() = " + myLogger.getClass()); // class hello.core.common.MyLogger$$SpringCGLIB$$0 => CGLIB(바이트코드 조작 라이브러리) : 스프링이 생성했다는 것을 알수 있다.
        myLogger.setRequestURL(requestURL);

        myLogger.log("controller test");
        Thread.sleep(100);
        logDemoService.logic("testId"); // 사실상 가짜 프록시 객체의 메서드를 호출하는 것. 다형성이기에 클라이언트는 가짜인지 진짜인지 구분이 안됨
        return "OK";
    }

}
