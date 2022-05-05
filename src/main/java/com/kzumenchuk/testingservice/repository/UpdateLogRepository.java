package com.kzumenchuk.testingservice.repository;

import com.kzumenchuk.testingservice.repository.model.UpdateLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpdateLogRepository extends JpaRepository<UpdateLogEntity, Long> {

}
