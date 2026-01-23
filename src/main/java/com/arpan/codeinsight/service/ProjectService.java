package com.arpan.codeinsight.service;

import com.arpan.codeinsight.model.ProjectEntity;
import com.arpan.codeinsight.repository.ProjectRepository;
import com.arpan.codeinsight.util.ZipExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Value("${codeinsight.storage.path}")
    private String basePath;

    @Autowired
    private ZipExtractor zipExtractor;

    @Autowired
    private FileScanService fileScanService;

    @Autowired
    private CodeAnalysisService codeAnalysisService;


    public ProjectEntity createProject(String projectName,
                                       MultipartFile zipFile) throws IOException{
        ProjectEntity project = new ProjectEntity();
        project.setName(projectName);
        project.setUploadType("ZIP");
        project.setStatus("UPLOADED");
        project.setCreatedAt(LocalDateTime.now());
        project = projectRepository.save(project);

        // 2️⃣ Create project directory
        Path projectDir = Paths.get(basePath, "project-" + project.getId());
        Files.createDirectories(projectDir);

        // 3️⃣ Save ZIP file
        Path zipPath = projectDir.resolve(zipFile.getOriginalFilename());
        Files.copy(zipFile.getInputStream(), zipPath, StandardCopyOption.REPLACE_EXISTING);

        // 4️⃣ Update storage path
        project.setStoragePath(projectDir.toString());

        // 5️⃣ Extract ZIP
        Path extractDir = projectDir.resolve("extracted");
        zipExtractor.extract(zipPath, extractDir);

// 6️⃣ Scan extracted files and save metadata
        fileScanService.scanAndSave(project.getId(), extractDir);

//        return projectRepository.save(project);

        ProjectEntity savedProject = projectRepository.save(project);

        // 🚨 TEMP ONLY
        codeAnalysisService.analyzeProject(savedProject.getId());

        return savedProject;

    }
}
