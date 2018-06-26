package com.uraltranscom.buildreports.controller;

/**
 *
 * Контроллер
 *
 * @author Vladislav Klochkov
 * @version 1.0
 * @create 13.06.2018
 *
 * 13.06.2018
 *   1. Версия 1.0
 *
 */

import com.uraltranscom.buildreports.service.additional.MultipartFileToFile;
import com.uraltranscom.buildreports.service.impl.RootClazz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class BasicController {
    // Подключаем логгер
    private static Logger logger = LoggerFactory.getLogger(BasicController.class);

    @Autowired
    RootClazz rootClazz;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        return "welcome";
    }

    @RequestMapping(value = "/report", method = RequestMethod.POST)
    public String routeList(@RequestParam(value = "wagons") MultipartFile wagonFile,
                            @RequestParam(value = "routes") MultipartFile routeFile,
                            @RequestParam(value = "dateFrom") @DateTimeFormat(pattern="yyyy-MM-dd")  Date dateFrom,
                            @RequestParam(value = "dateTo") @DateTimeFormat(pattern="yyyy-MM-dd")  Date dateTo,
                            @RequestParam(value = "dateFromSpravo4no") @DateTimeFormat(pattern="yyyy-MM-dd")  Date dateFromSpravo4no,
                            @RequestParam(value = "dateToSpravo4no") @DateTimeFormat(pattern="yyyy-MM-dd")  Date dateToSpravo4no, HttpServletResponse response, Model model) {
        ArrayList<Date> dates = new ArrayList<>();
        ArrayList<Date> datesSpravo4no = new ArrayList<>();
        dates.add(dateFrom);
        dates.add(dateTo);
        datesSpravo4no.add(dateFromSpravo4no);
        datesSpravo4no.add(dateToSpravo4no);
        rootClazz.getGetListOfWagons().setFile(MultipartFileToFile.multipartToFile(wagonFile));
        rootClazz.getGetListOfRoutes().setFile(MultipartFileToFile.multipartToFile(routeFile));
        rootClazz.startProcess(dates, datesSpravo4no, response);
        return "welcome";
    }
}
