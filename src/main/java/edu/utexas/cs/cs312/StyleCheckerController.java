package edu.utexas.cs.cs312;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StyleCheckerController {

    @RequestMapping("/")
    public String index() {
        return "Hello World";
    }
}
