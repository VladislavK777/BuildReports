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
import com.uraltranscom.buildreports.service.export.WriteToFileExcel;
import com.uraltranscom.buildreports.service.impl.GetListOfWagonsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Controller
public class BasicController {
    // Подключаем логгер
    private static Logger logger = LoggerFactory.getLogger(BasicController.class);

    @Autowired
    WriteToFileExcel writeToFileExcel;
    @Autowired
    GetListOfWagonsImpl getListOfWagons;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        return "welcome";
    }

    @RequestMapping(value = "/report", method = RequestMethod.POST)
    public String routeList(@RequestParam(value = "wagons") MultipartFile wagonFile,
                            @RequestParam(value = "routes") MultipartFile routeFile, HttpServletResponse response, Model model) {
        writeToFileExcel.setFile(null);
        writeToFileExcel.setFile(MultipartFileToFile.multipartToFile(routeFile));
        getListOfWagons.setFile(MultipartFileToFile.multipartToFile(wagonFile));
        writeToFileExcel.downloadFileExcel(response);
        return "welcome";
    }
}
