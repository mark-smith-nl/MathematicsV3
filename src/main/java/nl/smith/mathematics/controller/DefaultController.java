package nl.smith.mathematics.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("mathematics")
public class DefaultController {

    @GetMapping("properties")
    public @ResponseBody Map<Integer, String> listProperties() {
        return Map.of(1, "Mark Smith", 2, "Tom Smith", 3, "Tom Smith", 4, "Petra Smith");
    }
}
