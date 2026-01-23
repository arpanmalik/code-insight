package com.arpan.codeinsight.service;

import com.arpan.codeinsight.model.ProjectFileEntity;
import com.arpan.codeinsight.repository.ProjectFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileScanService {

    @Autowired
    private ProjectFileRepository projectFileRepository;

    @Autowired
    private JavaParseService javaParseService;

    public void scanAndSave(Long projectId, Path extractDir) throws IOException {
        List<ProjectFileEntity> javaFiles = new ArrayList<>();
        Files.walk(extractDir)
                .filter(Files::isRegularFile)
                .forEach(path -> {

                    String fileName = path.getFileName().toString();
                    String fileType = detectFileType(fileName);

                    if (fileType == null) {
                        return;
                    }

                    ProjectFileEntity file = new ProjectFileEntity();
                    file.setProjectId(projectId);
                    file.setFileName(fileName);
                    file.setFilePath(path.toString());
                    file.setFileType(fileType);
                    file.setSize(path.toFile().length());
                    file.setCreatedAt(LocalDateTime.now());

                    projectFileRepository.save(file);

                    if ("JAVA".equals(fileType)) {
                        javaFiles.add(file);
                    }
                });
        javaParseService.parseJavaFiles(projectId, javaFiles);
    }

    private String detectFileType(String name) {
        if (name.endsWith(".java")) return "JAVA";
        if (name.endsWith(".xml")) return "XML";
        if (name.endsWith(".yml") || name.endsWith(".properties")) return "CONFIG";
        return null;
    }
}
