package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.exception.TagNotFoundException;
import com.kzumenchuk.testingservice.repository.TagsRepository;
import com.kzumenchuk.testingservice.repository.model.TagEntity;
import com.kzumenchuk.testingservice.repository.model.UpdateLogEntity;
import com.kzumenchuk.testingservice.service.interfaces.ITagsService;
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
public class TagsService implements ITagsService {
    private final TagsRepository tagsRepository;
    private final UpdateLogService updateLogService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editTags(Set<TagEntity> editedTags, Long updateUserID) {
        editedTags.stream()
                .filter(Objects::nonNull)
                .forEach((editedTag) -> {
                    Optional<TagEntity> databaseTagData = tagsRepository.findById(editedTag.getTagID());

                    if (databaseTagData.isPresent()) {
                        TagEntity tag = databaseTagData.get();

                        if (!tag.equals(editedTag)) {
                            logTagsUpdate(updateUserID, editedTag, tag);

                            tag.setValue(editedTag.getValue());
                            tag.setUpdateDate(LocalDateTime.now());

                            tagsRepository.save(tag);
                        }
                    } else {
                        throw new TagNotFoundException("Tag (id = " + editedTag.getTagID() + ") not found");
                    }
                });
    }

    @Transactional(rollbackFor = Exception.class)
    public void logTagsUpdate(Long updateUserID, TagEntity editedTag, TagEntity tag) {
        UpdateLogEntity logEntity = UpdateLogUtil.createLogEntity(tag.getTagID(), EntityType.TAG,
                "U", "value", tag.getValue(), editedTag.getValue(), updateUserID);
        updateLogService.saveLog(logEntity);
    }
}
