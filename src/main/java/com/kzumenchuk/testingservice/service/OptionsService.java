package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.exception.OptionNotFoundException;
import com.kzumenchuk.testingservice.repository.OptionsRepository;
import com.kzumenchuk.testingservice.repository.model.OptionEntity;
import com.kzumenchuk.testingservice.repository.model.QuestionEntity;
import com.kzumenchuk.testingservice.repository.model.UpdateLogEntity;
import com.kzumenchuk.testingservice.service.interfaces.IOptionsService;
import com.kzumenchuk.testingservice.util.EntityType;
import com.kzumenchuk.testingservice.util.UpdateLogUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OptionsService implements IOptionsService {
    private final OptionsRepository optionsRepository;

    private final UpdateLogService updateLogService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateOptions(QuestionEntity editedQuestion, Long updateUserID) {
        Set<OptionEntity> editedOptions = editedQuestion.getOptions();

        editedOptions.stream()
                .filter(Objects::nonNull)
                .forEach((editedOption) -> {
                    Optional<OptionEntity> databaseOptionData = optionsRepository.findById(editedOption.getOptionID());

                    if (databaseOptionData.isPresent()) {
                        OptionEntity optionEntity = databaseOptionData.get();

                        if (!editedOption.equals(optionEntity)) {

                            logOptionUpdate(optionEntity, editedOption, updateUserID);

                            optionEntity.setValue(editedOption.getValue());
                            optionEntity.setCorrect(editedOption.isCorrect());
                            optionEntity.setUpdateDate(LocalDateTime.now());

                            optionsRepository.save(optionEntity);
                        }
                    } else {
                        throw new OptionNotFoundException("Option (id = " + editedOption.getOptionID() + ") not found");
                    }
                });
    }


    @Transactional(rollbackFor = Exception.class)
    public void logOptionUpdate(OptionEntity oldOption, OptionEntity newOption, Long updateUserID) {
        if (!oldOption.getValue().equalsIgnoreCase(newOption.getValue())) {
            UpdateLogEntity logEntity = UpdateLogUtil.createLogEntity(oldOption.getOptionID(), EntityType.OPTION,
                    "U", "value", oldOption.getValue(), newOption.getValue(), updateUserID);
            updateLogService.saveLog(logEntity);
        }

        if (oldOption.isCorrect() != newOption.isCorrect()) {
            UpdateLogEntity logEntity = UpdateLogUtil.createLogEntity(oldOption.getOptionID(), EntityType.OPTION,
                    "U", "isCorrect", String.valueOf(oldOption.isCorrect()), String.valueOf(newOption.isCorrect()), updateUserID);
            updateLogService.saveLog(logEntity);
        }
    }
}
