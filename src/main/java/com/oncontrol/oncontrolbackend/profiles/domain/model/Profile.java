package com.oncontrol.oncontrolbackend.profiles.domain.model;

import com.oncontrol.oncontrolbackend.iam.domain.model.User;
import com.oncontrol.oncontrolbackend.shared.domain.model.AuditableModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Profile entity contains common attributes for all profiles (doctors and patients)
 * Specific attributes are stored in DoctorProfile and PatientProfile entities
 */
@Entity
@Table(name = "profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Profile extends AuditableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Reference to the organization that owns this profile

    @Column(name = "profile_id", unique = true)
    private String profileId;

    @Enumerated(EnumType.STRING)
    @Column(name = "profile_type", nullable = false)
    private ProfileType profileType;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @PrePersist
    protected void generateProfileId() {
        if (profileId == null) {
            if (this.profileType == ProfileType.DOCTOR) {
                profileId = "DOC-" + System.currentTimeMillis();
            } else if (this.profileType == ProfileType.PATIENT) {
                profileId = "PAT-" + System.currentTimeMillis();
            }
        }
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
