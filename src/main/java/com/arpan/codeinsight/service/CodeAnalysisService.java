package com.arpan.codeinsight.service;

import com.arpan.codeinsight.model.ProjectFileEntity;
import com.arpan.codeinsight.repository.ProjectFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CodeAnalysisService {

    @Autowired
    private ProjectFileRepository projectFileRepository;

    @Autowired
    private JavaParseService javaParseService;

    public void analyzeProject(Long projectId) throws IOException {

        List<ProjectFileEntity> javaFiles =
                projectFileRepository.findByProjectIdAndFileType(projectId, "JAVA");

        if (javaFiles.isEmpty()) {
            return;
        }

        javaParseService.parseJavaFiles(projectId, javaFiles);
    }
}
