package hello.core.web;

import hello.core.common.MyLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
    private final MyLogger myLogger;
    /*
    * MyLogger는 request스코프다.
    * request 스코프는 사용자 요청이 들어오고 나갈때까지 생명주기다.
    *
    * 스프링 컨테이너가 생성될 때, MyLogger를 주입받고자 하지만
    * MyLogger는 는 request 스코프이기에 아직 등록된 빈이 없고, (=> 사용자 요청이 없었기 때문에)
    * 스프링 컨테이너가 생성될 시점에 LogDemoController 싱글톤 빈이 생성되어야하고,
    * 주입 받아야 MyLogger는 존재하지 않아 오류가 발생한다.
    * */

    @RequestMapping("/log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        myLogger.setRequestURL(requestURL);

        myLogger.log("controller test");
        logDemoService.logic("testId");
        return "OK";
    }

}
