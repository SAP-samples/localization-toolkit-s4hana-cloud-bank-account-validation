package com.sap.poland.whitelist.controllers;

import com.sap.poland.whitelist.service.WhitelistService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.io.StringWriter;

@RequestMapping("/")
@RestController
@Singleton
@SpringBootApplication
@ComponentScan("com.sap.poland")
public class DefaultController {
    private final WhitelistService _service;
    
    @Autowired
    DefaultController(WhitelistService service) {
        _service = service;
        System.err.println("Controller: " + service.getClass());
    }


    public static void main(String[] args) {
        SpringApplication.run(DefaultController.class, args);
    }


    @GetMapping("/validate")
    @ResponseBody 
    public String validate(@RequestBody() String request) throws Exception {
        try {
            return _service.validate(request);
        } catch (NotFoundException nfEx) {
            return nfEx.getMessage();
        } catch (Exception ex) {
            return printStackTrace(ex);
        }
    }

    @GetMapping("*")
    public String getElse(){
        return "Asterisk";
    }

        
    @GetMapping("/download")
    public String download() {
        return _service.downloadWhitelist(); 
    }
    
    private static String printStackTrace(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

}