package nl.smith.mathematics.controller;

import nl.smith.mathematics.service.MethodRunnerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class BaseController {

  private final MethodRunnerService methodRunnerService;

  public BaseController(MethodRunnerService methodRunnerService) {
    this.methodRunnerService = methodRunnerService;
  }

  @GetMapping({"/", "home", "index"})
  public  String home() {
    return "index";
  }

  @GetMapping("/helloWorld")
  public @ResponseBody String helloWorld() {
    return "Mark Smith";
  }

  @GetMapping("/setCookie")
  public @ResponseBody String setCookie(HttpServletResponse response) {
    Cookie cookie = new Cookie("Olamaa", "Bokassa");
    cookie.setMaxAge(36000);
   // cookie.setDomain("www.example.com");
    cookie.setPath("/test");
    response.addCookie(cookie);
    return "Cookie was set........";
  }

  @GetMapping("/getCookie")
  public @ResponseBody String getCookie(@CookieValue("Olamaa") String fooCookie) {
    return fooCookie;
  }
}
