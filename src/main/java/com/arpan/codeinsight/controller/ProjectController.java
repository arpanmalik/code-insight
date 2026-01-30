package com.arpan.codeinsight.controller;

import com.arpan.codeinsight.model.ProjectEntity;
import com.arpan.codeinsight.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    @Autowired
    private  ProjectService projectService;

    @PostMapping("/upload")
    public ProjectEntity createProject(@RequestParam String name,
                                       @RequestParam MultipartFile file) throws IOException {
        return projectService.createProject(name, file);
    }

    @GetMapping("/home")
    public String home(){
        return "Home sweet home";
    }
}
