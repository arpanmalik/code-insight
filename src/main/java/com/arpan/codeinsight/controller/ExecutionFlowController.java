package com.arpan.codeinsight.controller;

import com.arpan.codeinsight.model.MethodCallEntity;
import com.arpan.codeinsight.service.ExecutionFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/execution-flow")
public class ExecutionFlowController {
    @Autowired
    private ExecutionFlowService executionFlowService;

    @GetMapping
    public List<MethodCallEntity> getExecutionFlow(
            @RequestParam Long projectId,
            @RequestParam String startClass,
            @RequestParam String startMethod
    ) {
        List<MethodCallEntity> flow = new ArrayList<>();

        executionFlowService.buildExecutionFlow(
                projectId,
                startClass,
                startMethod,
                flow,
                0
        );

        return flow;
    }
}


