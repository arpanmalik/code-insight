package com.arpan.codeinsight.service;

import com.arpan.codeinsight.model.MethodCallEntity;
import com.arpan.codeinsight.repository.MethodCallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExecutionFlowService {
    @Autowired
    private MethodCallRepository methodCallRepository;

    public List<MethodCallEntity> getDirectMethodCalls(
            Long projectId,
            String callerClass,
            String callerMethod
    ) {
        return methodCallRepository
                .findByProjectIdAndCallerClassAndCallerMethod(
                        projectId,
                        callerClass,
                        callerMethod
                );
    }

    public void buildExecutionFlow(
            Long projectId,
            String callerClass,
            String callerMethod,
            List<MethodCallEntity> flow,
            int depth
    ) {
        // safety guard
        if (depth > 10) return;

        List<MethodCallEntity> calls =
                getDirectMethodCalls(projectId, callerClass, callerMethod);

        for (MethodCallEntity call : calls) {

            // prevent infinite loops
            boolean alreadyVisited = flow.stream().anyMatch(existing ->
                    existing.getCallerClass().equals(call.getCallerClass()) &&
                            existing.getCallerMethod().equals(call.getCallerMethod()) &&
                            existing.getCalleeClass().equals(call.getCalleeClass()) &&
                            existing.getCalleeMethod().equals(call.getCalleeMethod())
            );

            if (alreadyVisited) continue;

            flow.add(call);

            // recursive dive
            buildExecutionFlow(
                    projectId,
                    call.getCalleeClass(),
                    call.getCalleeMethod(),
                    flow,
                    depth + 1
            );
        }
    }
}
