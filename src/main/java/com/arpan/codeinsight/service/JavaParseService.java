package com.arpan.codeinsight.service;

import com.arpan.codeinsight.model.ApiEndpointEntity;
import com.arpan.codeinsight.model.CodeComponentEntity;
import com.arpan.codeinsight.model.ProjectFileEntity;
import com.arpan.codeinsight.repository.ApiEndpointRepository;
import com.arpan.codeinsight.repository.CodeComponentRepository;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JavaParseService {

    @Autowired
    private CodeComponentRepository repository;

    @Autowired
    private ApiEndpointRepository apiEndpointRepository;


    public void parseJavaFiles(Long projectId, List<ProjectFileEntity> javaFiles)
            throws IOException {

        for (ProjectFileEntity file : javaFiles) {

            CompilationUnit cu =
                    StaticJavaParser.parse(new File(file.getFilePath()));

            cu.findAll(ClassOrInterfaceDeclaration.class)
                    .forEach(clazz -> {

                        String type = detectComponentType(clazz);
                        if (type == null) return;

                        CodeComponentEntity component = new CodeComponentEntity();
                        component.setProjectId(projectId);
                        component.setComponentType(type);
                        component.setClassName(clazz.getNameAsString());
                        component.setFilePath(file.getFilePath());
                        component.setCreatedAt(LocalDateTime.now());

                        repository.save(component);

                        if ("CONTROLLER".equals(type)) {

                            String basePath = "";

                            Optional<AnnotationExpr> mapping =
                                    clazz.getAnnotationByName("RequestMapping");

                            if (mapping.isPresent() && mapping.get().isSingleMemberAnnotationExpr()) {
                                basePath = mapping.get()
                                        .asSingleMemberAnnotationExpr()
                                        .getMemberValue()
                                        .toString()
                                        .replace("\"", "");
                            }
                            extractEndpoints(projectId, clazz, basePath);


                        }
                    });
        }
    }

    private String detectComponentType(ClassOrInterfaceDeclaration clazz) {

        if (clazz.isAnnotationPresent("RestController")
                || clazz.isAnnotationPresent("Controller")) {
            return "CONTROLLER";
        }

        if (clazz.isAnnotationPresent("Service")) {
            return "SERVICE";
        }

        if (clazz.isAnnotationPresent("Repository")) {
            return "REPOSITORY";
        }

        return null;
    }


    private void extractEndpoints(Long projectId,
                                  ClassOrInterfaceDeclaration clazz,
                                  String basePath) {

        clazz.getMethods().forEach(method -> {

            method.getAnnotations().forEach(annotation -> {

                String httpMethod = null;
                String path = "";

                switch (annotation.getNameAsString()) {
                    case "GetMapping" -> httpMethod = "GET";
                    case "PostMapping" -> httpMethod = "POST";
                    case "PutMapping" -> httpMethod = "PUT";
                    case "DeleteMapping" -> httpMethod = "DELETE";
                    case "RequestMapping" -> httpMethod = "REQUEST";
                }

                if (httpMethod == null) return;

                if (annotation.isSingleMemberAnnotationExpr()) {
                    path = annotation.asSingleMemberAnnotationExpr()
                            .getMemberValue().toString()
                            .replace("\"", "");
                }

                ApiEndpointEntity endpoint = new ApiEndpointEntity();
                endpoint.setProjectId(projectId);
                endpoint.setControllerName(clazz.getNameAsString());
                endpoint.setHttpMethod(httpMethod);
                endpoint.setPath(basePath + path);
                endpoint.setMethodName(method.getNameAsString());
                endpoint.setCreatedAt(LocalDateTime.now());

                apiEndpointRepository.save(endpoint);
            });
        });
    }

}
