package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.repository.UpdateLogRepository;
import com.kzumenchuk.testingservice.repository.model.UpdateLogEntity;
import com.kzumenchuk.testingservice.service.interfaces.IUpdateLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateLogService implements IUpdateLogService {
    private final UpdateLogRepository updateLogRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveLog(UpdateLogEntity logEntity) {
        updateLogRepository.saveAndFlush(logEntity);
    }
}
