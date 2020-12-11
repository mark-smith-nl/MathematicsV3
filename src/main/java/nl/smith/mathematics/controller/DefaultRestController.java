package nl.smith.mathematics.controller;

import nl.smith.mathematics.numbertype.RationalNumber;
import nl.smith.mathematics.service.MethodRunnerService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(DefaultController.MAPPING_FUNCTION)
public class DefaultRestController {

private final MethodRunnerService methodRunnerService;

    public DefaultRestController(MethodRunnerService methodRunnerService) {
        this.methodRunnerService = methodRunnerService;
    }

    @PostMapping("MyTestMapping")
    public Map<Integer, String> createProduct(@RequestBody FunctionDefinition functionDefinition) {
        //methodRunnerService.invokeMathematicalMethodForNumberType()
        Map<Integer, String> results = new HashMap<>();
        results.put(1, "Mark");
        results.put(2, "Tom");
        results.put(3, "Frank");
        results.put(4, "Petra");
        results.put(5, "Irene");
        results.put(6, "Henk");
        results.put(7, "Paul");
        results.put(8, functionDefinition.getFunctionName());

        return results;
    }


}
