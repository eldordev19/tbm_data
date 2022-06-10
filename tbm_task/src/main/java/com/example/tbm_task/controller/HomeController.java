package com.example.tbm_task.controller;

import com.example.tbm_task.model.InfoBts;
import com.example.tbm_task.service.CSVService;
import com.example.tbm_task.service.InfoBtsService;
import com.example.tbm_task.service.Main;
import com.example.tbm_task.model.Result;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    public static List<Result> results = new ArrayList<>();
    @Autowired
    CSVService csvService;

    @Autowired
    InfoBtsService infoBtsService;

    @GetMapping({"/", "/getFormDb"})
    public String getForm( Model model) throws IOException {
        List<InfoBts> infoBts = infoBtsService.getInfoBts();
        model.addAttribute("count", infoBts.size());
        //csvService.writeToCSV();
        return "file-upload-form-db";
    }

    @PostMapping("/uploadDb")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        if (csvService.hasCSVFormat(file)) {
            System.out.println("This is csv format");
             results = csvService.get(file);
            model.addAttribute("fileList", results);
        }
        return "file-upload-form-db";
    }

    @PostMapping("/upload")
    public String uploadFileToDb(@RequestParam("data") MultipartFile file) throws IOException {
        if (csvService.hasCSVFormat(file)) {
            System.out.println("This is csv format");
            csvService.saveDataToDB(file);
        }
        return "file-upload-form-db";
    }


    @GetMapping("/download")
    public void downloadCsv(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=result.csv");
        Main main = new Main();
        ByteArrayInputStream stream = main.getExcelFile(results);
        IOUtils.copy(stream, response.getOutputStream());
    }

}
