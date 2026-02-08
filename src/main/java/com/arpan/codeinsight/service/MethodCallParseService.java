package com.arpan.codeinsight.service;

import com.arpan.codeinsight.model.MethodCallEntity;
import com.arpan.codeinsight.repository.MethodCallRepository;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class MethodCallParseService {

    @Autowired
    private MethodCallRepository methodCallRepository;

    public void extractMethodCalls(Long projectId,
                                   String className,
                                   MethodDeclaration method,
                                   ClassOrInterfaceDeclaration clazz) {

        Map<String, String> fieldTypeMap = new HashMap<>();
        clazz.findAll(FieldDeclaration.class).forEach(field -> {
            field.getVariables().forEach(v -> {
                fieldTypeMap.put(v.getNameAsString(), field.getElementType().asString());
            });
        });

        method.findAll(MethodCallExpr.class)
                .forEach(call -> {

                    MethodCallEntity entity = new MethodCallEntity();
                    entity.setProjectId(projectId);

                    // caller
                    entity.setCallerClass(className);
                    entity.setCallerMethod(method.getNameAsString());

                    // callee
                    entity.setCalleeMethod(call.getNameAsString());

                    String scope = call.getScope()
                            .map(Object::toString)
                            .orElse("");

                    String calleeClass = scope.isEmpty() ? className : fieldTypeMap.getOrDefault(scope, scope);
                    entity.setCalleeClass(calleeClass);

                    // 🔑 traversal identity (MUST be stable)
//                    entity.setCalleeClass(className);
                    // Resolve the actual callee class
//                    String calleeClass = scope.isEmpty() ? className : variableTypeMap.getOrDefault(scope, scope);
//                    entity.setCalleeClass(calleeClass);

                    // 🏷 layer classification (your original idea)
                    String callType = "UNKNOWN";
                    String scopeLower = scope.toLowerCase();

                    if (scopeLower.contains("service")) {
                        callType = "SERVICE";
                    } else if (scopeLower.contains("repo")) {
                        callType = "REPOSITORY";
                    }

                    entity.setCallType(callType);
                    entity.setCreatedAt(LocalDateTime.now());

                    methodCallRepository.save(entity);
                });
    }
}

//@Service
//public class MethodCallParseService {
//
//    @Autowired
//    private MethodCallRepository methodCallRepository;
//
//    public void extractMethodCalls(Long projectId,
//                                   String className,
//                                   MethodDeclaration method) {
//
//        method.findAll(MethodCallExpr.class)
//                .forEach(call -> {
//
//                    MethodCallEntity entity = new MethodCallEntity();
//                    entity.setProjectId(projectId);
//
//                    // caller info
//                    entity.setCallerClass(className);
//                    entity.setCallerMethod(method.getNameAsString());
//
//                    // callee info (best-effort for now)
//                    entity.setCalleeMethod(call.getNameAsString());
//
//                    String calleeClass = call.getScope()
//                            .map(Object::toString)
//                            .orElse("UNKNOWN");
//                    entity.setCalleeClass(
//                            call.getScope()
//                                    .map(Object::toString)
//                                    .orElse("UNKNOWN")
//                    );
//
//                    String callType = "UNKNOWN";
//                    String scopeLower = calleeClass.toLowerCase();
//
//                    if (scopeLower.contains("service")) {
//                        callType = "SERVICE";
//                    } else if (scopeLower.contains("repo")) {
//                        callType = "REPOSITORY";
//                    }
//
//                    entity.setCallType(callType);
//
//                    entity.setCreatedAt(LocalDateTime.now());
//
//                    methodCallRepository.save(entity);
//                });
//    }
//}
