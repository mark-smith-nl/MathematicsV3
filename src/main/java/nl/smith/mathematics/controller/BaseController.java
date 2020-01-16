package nl.smith.mathematics.controller;

import nl.smith.mathematics.service.MethodRunnerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
