package main.java.com.goormcoder.ieum.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "t_place")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Column(name = "place_order", nullable = false)
    private int placeOrder;

    @Column(name = "place_day", nullable = false)
    private LocalDate placeDay;

    @Column(name = "place_name", nullable = false)
    private String placeName;

    @Column(name = "place_location", nullable = false)
    private String placeLocation;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public Place(Plan plan, int placeOrder, LocalDate placeDay, String placeName, String placeLocation) {
        this.plan = plan;
        this.placeOrder = placeOrder;
        this.placeDay = placeDay;
        this.placeName = placeName;
        this.placeLocation = placeLocation;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
