package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.repository.TagsRepository;
import com.kzumenchuk.testingservice.repository.model.TagEntity;
import com.kzumenchuk.testingservice.repository.model.UpdateLogEntity;
import com.kzumenchuk.testingservice.util.EntityType;
import com.kzumenchuk.testingservice.util.UpdateLogUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class TagsService {
    private final TagsRepository tagsRepository;
    private final UpdateLogService updateLogService;

    public TagsService(TagsRepository tagsRepository, UpdateLogService updateLogService) {
        this.tagsRepository = tagsRepository;
        this.updateLogService = updateLogService;
    }

    public TagEntity saveOptions(TagEntity tagEntity) {
        return tagsRepository.save(tagEntity);
    }

    public void editTags(Set<TagEntity> editedTags, Long updateUserID) {
        editedTags.stream()
                .filter(Objects::nonNull)
                .forEach((editedTag) -> {
                    Optional<TagEntity> databaseTagData = tagsRepository.findById(editedTag.getTagID());

                    if (databaseTagData.isPresent()) {
                        TagEntity tag = databaseTagData.get();

                        if (!tag.equals(editedTag)) {
                            UpdateLogEntity tagLog = UpdateLogUtil.createLogEntity(tag.getTagID(), EntityType.TAG,
                                    "U", "value", tag.getValue(), editedTag.getValue(), updateUserID);
                            updateLogService.saveLog(tagLog);

                            tag.setValue(editedTag.getValue());
                            tag.setUpdateDate(LocalDateTime.now());

                            tagsRepository.save(tag);
                        }
                    } // TODO: ADD ELSE BLOCK
                });
    }
}
