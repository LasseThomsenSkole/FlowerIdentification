package org.example.floweridentification.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IdentificationController {
    @Value("${openai.api.key}")
    private String apikey;

}
