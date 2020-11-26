package com.github.bruce95a.file.share.controller;

import com.github.bruce95a.file.share.entity.ShareFile;
import com.github.bruce95a.file.share.service.CfgService;
import com.github.bruce95a.file.share.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @Autowired
    private CfgService cfgService;

    @PostMapping("/upload")
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        Map<String, Object> result = new HashMap<>();
        final String filePath = cfgService.getStorePath();
        final String fileName = multipartFile.getOriginalFilename();
        final String fileFullPath = filePath + fileName;
        File file = new File(fileFullPath);
        if (file.exists()) {
            result.put("msg", "文件已存在");
            return result;
        }
        try {
            multipartFile.transferTo(file);
            ShareFile shareFile = new ShareFile();
            shareFile.setName(fileName);
            fileService.saveFile(shareFile);
        } catch (IOException e) {
            logger.error("保存文件出错", e);
            result.put("msg", e.getMessage());
            return result;
        }
        return result;
    }

    @GetMapping("/reports/{uuid}")
    public StreamingResponseBody downloadFile(@PathVariable String uuid,
                                              HttpServletResponse response) {
        final String fileName = fileService.getFileName(uuid);
        final String filePath = cfgService.getStorePath();
        final String fileFullPath = filePath + fileName;
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        return outputStream -> {
            outputStream.write(Files.readAllBytes(Paths.get(fileFullPath)));
        };
    }

    @GetMapping("/reports")
    public Map<String, Object> reports(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return fileService.getFiles(page, size);
    }

    @GetMapping("/rescan")
    public void rescan() {
        fileService.rescan();
    }

    @DeleteMapping("/reports/{uuid}")
    public void del(@PathVariable String uuid) {
        int c = fileService.delFile(uuid);
        logger.debug("delete count" + c);
    }
}
