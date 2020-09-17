package com.thoughtworks.rslist.entity;

import com.thoughtworks.rslist.dto.RsEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "rs_event")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String eventName;

    @Column(nullable = false)
    private String keyword;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public RsEventEntity merge(RsEventEntity rsEventEntity) {
        if (rsEventEntity.eventName != null) {
            this.eventName = rsEventEntity.eventName;
        }

        if (rsEventEntity.keyword != null) {
            this.keyword = rsEventEntity.keyword;
        }

        return this;
    }

    public static RsEventEntity from(RsEvent rsEvent) {
        return RsEventEntity.builder()
                .id(rsEvent.getId())
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyword())
                .user(UserEntity.builder()
                        .id(rsEvent.getUserId())
                        .build())
                .build();
    }
}
