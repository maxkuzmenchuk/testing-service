package com.kzumenchuk.testingservice.service.interfaces;

import com.kzumenchuk.testingservice.repository.model.TagEntity;

import java.util.Set;

public interface ITagsService {
    void editTags(Set<TagEntity> editedTags, Long updateUserID);
}
