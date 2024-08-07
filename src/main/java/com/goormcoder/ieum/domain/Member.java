package com.goormcoder.ieum.domain;

import com.goormcoder.ieum.domain.enumeration.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String name;
    private MemberRole role;
    private Gender gender;
    private LocalDate birth;

    private String loginId;
    private String password;

    private String oauthType;
    private String oauthId;

    @CreatedDate
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    @Builder
    public Member(String name, MemberRole role, Gender gender, LocalDate birth, String loginId, String password, String oauthType, String oauthId) {
        this.name = name;
        this.role = role;
        this.gender = gender;
        this.birth = birth;
        this.loginId = loginId;
        this.password = password;
        this.oauthType = oauthType;
        this.oauthId = oauthId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    //수정필요
    public String getEmail() {
        return "email";
    }
}
