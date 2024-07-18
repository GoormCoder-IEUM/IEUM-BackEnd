package com.goormcoder.ieum.domain;

import com.goormcoder.ieum.domain.Plan;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "t_accommodation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
public class Accommodation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accom_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Column(name = "accom_name", nullable = false)
    private String name;

    @Column(name = "accom_location", nullable = false)
    private String location;

    @Column(name = "accom_day", nullable = false)
    private LocalDateTime accomDay;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public Accommodation(Plan plan, String name, String location, LocalDateTime stayDate) {
        this.plan = plan;
        this.name = name;
        this.location = location;
        this.accomDay = accomDay;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }


    public void setId(Long id) {
    }
}
