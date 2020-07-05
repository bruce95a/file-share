package com.github.bruce95a.file.share.controller;

import com.github.bruce95a.file.share.entity.ShareFile;
import com.github.bruce95a.file.share.service.ConfigService;
import com.github.bruce95a.file.share.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

@Controller
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @Autowired
    private ConfigService configService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadFile(@RequestParam("file") MultipartFile multipartFile, Model model) {
        final String filePath = configService.getStorePath();
        final String fileName = multipartFile.getOriginalFilename();
        final String fileFullPath = filePath + fileName;
        File file = new File(fileFullPath);
        if (file.exists()) {
            model.addAttribute("msg", "文件已存在");
            return "upload";
        }
        try {
            multipartFile.transferTo(file);
            ShareFile shareFile = new ShareFile();
            shareFile.setName(fileName);
            fileService.saveFile(shareFile);
        } catch (IOException e) {
            logger.error("保存文件出错", e);
            model.addAttribute("msg", e.getMessage());
            return "upload";
        }
        return "redirect:/report";
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public StreamingResponseBody downloadFile(@RequestParam("id") String uuid,
                                              HttpServletResponse response) {
        final String fileName = fileService.getFileName(uuid);
        final String filePath = configService.getStorePath();
        final String fileFullPath = filePath + fileName;
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        return outputStream -> {
            outputStream.write(Files.readAllBytes(Paths.get(fileFullPath)));
        };
    }

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public String report(@RequestParam("p") Optional<String> page,
                         @RequestParam("n") Optional<String> fileName,
                         @RequestParam("b") Optional<String> sdt,
                         @RequestParam("e") Optional<String> edt,
                         Model model) {
        Map<String, Object> map = fileService.getFiles(page.orElse("1"), fileName.orElse(""), sdt.orElse(""), edt.orElse(""));
        model.addAttribute("page", Integer.valueOf(page.orElse("1")));
        if (map.get("count") != null) {
            int count = (int) map.get("count");
            int totalPage = count % 10 == 0 ? (count / 10) : (count / 10 + 1);
            if (totalPage == 0) {
                totalPage = 1;
            }
            model.addAttribute("totalPage", totalPage);
        }
        if (map.get("list") != null) {
            model.addAttribute("reports", map.get("list"));
        }
        return "report";
    }

    @RequestMapping(value = "/rescan", method = RequestMethod.GET)
    public String rescan(Model model) {
        fileService.rescan();
        return "redirect:/report";
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public String del(@RequestParam("id") String uuid,
                      Model model) {
        final boolean b = fileService.delFile(uuid);
        return "redirect:/report";
    }
}
