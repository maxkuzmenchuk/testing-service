package com.kzumenchuk.testingservice.service.interfaces;

import com.kzumenchuk.testingservice.repository.model.UpdateLogEntity;

public interface IUpdateLogService {
    void saveLog(UpdateLogEntity logEntity);
}
