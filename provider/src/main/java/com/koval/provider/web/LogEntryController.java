package com.koval.provider.web;

import com.koval.provider.service.LogEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Volodymyr Kovalenko
 */
@Controller
@RequestMapping(path = "/api/access")
public class LogEntryController {
    @Autowired
    private LogEntryService logEntryService;

    @RequestMapping(path = "/countct", method = RequestMethod.GET)
    @ResponseBody
    public Long countByTypeAndClient(@RequestParam(name = "type") String type,
                                     @RequestParam(name = "client") String client){
       return logEntryService.countByTypeAndClient(type, client);
    }
}
