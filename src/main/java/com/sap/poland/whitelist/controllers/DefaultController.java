package com.sap.poland.whitelist.controllers;

import com.sap.poland.whitelist.service.WhitelistService;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
@Singleton
public class DefaultController {
    private final WhitelistService _service;
    
    @Inject
    DefaultController(WhitelistService service) {
        _service = service;
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