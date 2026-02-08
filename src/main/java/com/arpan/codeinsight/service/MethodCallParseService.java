package com.arpan.codeinsight.service;

import com.arpan.codeinsight.model.MethodCallEntity;
import com.arpan.codeinsight.repository.MethodCallRepository;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MethodCallParseService {

    @Autowired
    private MethodCallRepository methodCallRepository;

    public void extractMethodCalls(Long projectId,
                                   String className,
                                   MethodDeclaration method) {

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

                    // 🔑 traversal identity (MUST be stable)
                    entity.setCalleeClass(className);

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
