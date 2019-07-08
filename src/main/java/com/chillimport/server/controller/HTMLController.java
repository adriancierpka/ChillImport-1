package com.chillimport.server.controller;

import com.chillimport.server.FileManager;
import com.chillimport.server.UploadHandler;
import com.chillimport.server.config.Configuration;
import com.chillimport.server.config.DataType;
import com.chillimport.server.errors.ErrorHandler;
import com.chillimport.server.errors.LogManager;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;


@RestController
public class HTMLController {


    private @Autowired
    HttpServletRequest request;

    @RequestMapping(value = "/websitepreview", method = RequestMethod.POST)
    public ResponseEntity<?> websitePreview(@RequestParam String s) {
        FileManager fm = new FileManager();
        File file = fm.storeFromURL(s);
        //möglicherweise instabil

        Configuration sampleConfig;
        ArrayList<ArrayList<String>> firstThreeRowsOfTable;

        if (file.getName().endsWith(".xls") ||
                file.getName().endsWith(".xlsx")) {
            sampleConfig = new Configuration(0, ";", DataType.EXCEL);
        }
        else if (file.getName().endsWith(".csv")) {
            sampleConfig = new Configuration(0,";", DataType.CSV);
        }
        else {
            return new ResponseEntity<>("File is not XSLX,XLS,CSV", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            firstThreeRowsOfTable = UploadHandler.preview(file, sampleConfig);
        } catch (IOException e) {
            ErrorHandler.getInstance().addRows(-1,e);
            return new ResponseEntity<>("Failed to convert file", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(firstThreeRowsOfTable, HttpStatus.OK);
    }



    @RequestMapping(value = "/errors/returnFiles", method = RequestMethod.GET)
    @ResponseBody
    public String returnFile() {
        return ErrorHandler.getInstance().returnFiles();
    }


    @RequestMapping(value = "/server-check", method = RequestMethod.GET)
    @ResponseBody
    public boolean pingFROSTServer() {
        try {
            return InetAddress.getByName(FileManager.getServerURL().getHost()).isReachable(6000);
        } catch (UnknownHostException e) {
            LogManager.getInstance().writeToLog("Server not reachable", true);
            ErrorHandler.getInstance().addRows(-1,e);
            return false;
        } catch (MalformedURLException e) {
            LogManager.getInstance().writeToLog("Server address malformed", true);
            ErrorHandler.getInstance().addRows(-1,e);
            return false;
        } catch (IOException e) {
            LogManager.getInstance().writeToLog("Internet connection offline", true);
            ErrorHandler.getInstance().addRows(-1,e);
            return false;
        }
    }

    @RequestMapping(value = "errors/delFile", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity delFile(@RequestParam String name) {
        File file = new File(FileManager.getLogPath() + File.separator + "returnRows" + File.separator + name);if(!file.delete()){
            return new ResponseEntity("Could not delete File", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity("Deleted File", HttpStatus.OK);
    }
    @GetMapping("/returnRows/{fileName:.+}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        FileManager fm = new FileManager();
        Resource res = fm.download(fileName);
        String contentType;
        try {
            contentType = request.getServletContext().getMimeType(res.getFile().getAbsolutePath());
        } catch (IOException e) {
            contentType = "application/octet-stream";
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + res.getFilename() + "\"").body(res);
    }


    @GetMapping("/get-return")
    public ResponseEntity<?> downloadFile(@RequestParam String fileName) {
        FileManager fm = new FileManager();
        Resource res = fm.download(fileName);
        String contentType;

        try {
            contentType = this.request.getServletContext().getMimeType(res.getFile().getAbsolutePath());
        } catch (IOException e) {
            contentType = "application/octet-stream";
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + res.getFilename() + "\"").body(res);
    }


    @RequestMapping(value = "/getfrosturl", method = RequestMethod.GET)
    @ResponseBody
    public String getFrostServerURL() {
        try {
            InetAddress.getByName(FileManager.getServerURL().getHost()).isReachable(6000);
            return FileManager.getServerURL().toString();
        } catch (UnknownHostException e) {
            return "Server not reachable";
        } catch (MalformedURLException e) {
            return "FROST URL malformed";
        } catch (IOException e) {
            return "Internet connection offline";
        }
    }


}
