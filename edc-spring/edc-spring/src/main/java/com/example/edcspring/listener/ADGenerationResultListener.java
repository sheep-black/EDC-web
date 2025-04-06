package com.example.edcspring.listener;

import com.example.edcspring.entity.ADGenerationResult;
import com.example.edcspring.service.Impl.PredictServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ADGenerationResultListener {

    @Autowired
    private PredictServiceImpl predictService;

    @RabbitListener(queues = "ad-generation-results")
    public void handleResult(ADGenerationResult result) {
        predictService.handleADGenerationResult(result);
    }
}

