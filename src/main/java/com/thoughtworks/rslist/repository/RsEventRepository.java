package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.exception.RsEventNotFoundException;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RsEventRepository extends CrudRepository<RsEventEntity, Integer> {

    List<RsEventEntity> findAllByUserId(Integer userId);

    boolean existsByIdAndUserId(Integer id, Integer userId);

    default RsEventEntity update(RsEventEntity rsEventEntity) {
        RsEventEntity oldRsEventEntity = this.findById(rsEventEntity.getId())
                .orElseThrow(RsEventNotFoundException::new);

        this.save(oldRsEventEntity.merge(rsEventEntity));

        return oldRsEventEntity;
    }
}
