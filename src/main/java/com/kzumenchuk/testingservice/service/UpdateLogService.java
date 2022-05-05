package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.repository.UpdateLogRepository;
import com.kzumenchuk.testingservice.repository.model.UpdateLogEntity;
import org.springframework.stereotype.Service;

@Service
public class UpdateLogService {
    private final UpdateLogRepository updateLogRepository;

    public UpdateLogService(UpdateLogRepository updateLogRepository) {
        this.updateLogRepository = updateLogRepository;
    }

    public void saveLog(UpdateLogEntity logEntity) {
        updateLogRepository.saveAndFlush(logEntity);
    }
}
