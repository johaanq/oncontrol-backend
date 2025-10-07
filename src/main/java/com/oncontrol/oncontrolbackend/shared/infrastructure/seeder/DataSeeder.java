package com.oncontrol.oncontrolbackend.shared.infrastructure.seeder;

import com.oncontrol.oncontrolbackend.appointments.domain.model.Appointment;
import com.oncontrol.oncontrolbackend.appointments.domain.model.AppointmentStatus;
import com.oncontrol.oncontrolbackend.appointments.domain.model.AppointmentType;
import com.oncontrol.oncontrolbackend.appointments.domain.repository.AppointmentRepository;
import com.oncontrol.oncontrolbackend.iam.domain.model.User;
import com.oncontrol.oncontrolbackend.iam.domain.model.UserRole;
import com.oncontrol.oncontrolbackend.iam.domain.repository.UserRepository;
import com.oncontrol.oncontrolbackend.profiles.domain.model.*;
import com.oncontrol.oncontrolbackend.profiles.domain.repository.DoctorProfileRepository;
import com.oncontrol.oncontrolbackend.profiles.domain.repository.PatientProfileRepository;
import com.oncontrol.oncontrolbackend.profiles.domain.repository.ProfileRepository;
import com.oncontrol.oncontrolbackend.symptoms.domain.model.Symptom;
import com.oncontrol.oncontrolbackend.symptoms.domain.model.SymptomSeverity;
import com.oncontrol.oncontrolbackend.symptoms.domain.repository.SymptomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DataSeeder - Crea datos iniciales para desarrollo y testing
 * 
 * Para DESACTIVAR el seeder, comenta la anotación @Component
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final AppointmentRepository appointmentRepository;
    private final SymptomRepository symptomRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Solo ejecutar si la base de datos está vacía
        if (userRepository.count() > 0) {
            log.info("💡 Database already has data. Skipping seeder.");
            return;
        }

        log.info("🌱 Starting DataSeeder...");

        try {
            // 1. Crear Organización
            User organization = createOrganization();
            
            // 2. Crear Doctores
            DoctorProfile doctor1 = createDoctor(organization, "Carlos", "García", "dr.garcia@hospital.com", "Oncología Médica", "MED-12345");
            DoctorProfile doctor2 = createDoctor(organization, "María", "Rodríguez", "dr.rodriguez@hospital.com", "Oncología Quirúrgica", "MED-67890");
            
            // 3. Crear Pacientes para Doctor 1
            PatientProfile patient1 = createPatient(doctor1, "Juan", "Pérez", "juan.perez@email.com", "O+", "Pulmón", "II");
            PatientProfile patient2 = createPatient(doctor1, "Ana", "Martínez", "ana.martinez@email.com", "A+", "Mama", "I");
            
            // 4. Crear Pacientes para Doctor 2
            PatientProfile patient3 = createPatient(doctor2, "Pedro", "López", "pedro.lopez@email.com", "B+", "Colon", "III");
            PatientProfile patient4 = createPatient(doctor2, "Laura", "Sánchez", "laura.sanchez@email.com", "AB+", "Próstata", "II");
            
            // 5. Crear Citas
            createAppointment(doctor1.getProfile(), patient1.getProfile(), LocalDateTime.now().plusDays(2), AppointmentType.PRIMERA_CONSULTA);
            createAppointment(doctor1.getProfile(), patient1.getProfile(), LocalDateTime.now().plusDays(7), AppointmentType.CONSULTA_SEGUIMIENTO);
            createAppointment(doctor1.getProfile(), patient2.getProfile(), LocalDateTime.now().plusDays(3), AppointmentType.REVISION_EXAMENES);
            createAppointment(doctor2.getProfile(), patient3.getProfile(), LocalDateTime.now().plusDays(1), AppointmentType.PRIMERA_CONSULTA);
            createAppointment(doctor2.getProfile(), patient4.getProfile(), LocalDateTime.now().plusDays(5), AppointmentType.CONSULTA_SEGUIMIENTO);
            
            // 6. Crear Síntomas
            createSymptom(patient1.getProfile(), "Dolor de cabeza", SymptomSeverity.MODERADA, LocalDate.now().minusDays(1));
            createSymptom(patient1.getProfile(), "Náusea", SymptomSeverity.SEVERA, LocalDate.now().minusDays(2));
            createSymptom(patient1.getProfile(), "Fatiga", SymptomSeverity.LEVE, LocalDate.now());
            
            createSymptom(patient2.getProfile(), "Dolor en el pecho", SymptomSeverity.MODERADA, LocalDate.now().minusDays(1));
            
            createSymptom(patient3.getProfile(), "Dolor abdominal", SymptomSeverity.SEVERA, LocalDate.now().minusDays(3));
            createSymptom(patient3.getProfile(), "Pérdida de apetito", SymptomSeverity.MODERADA, LocalDate.now().minusDays(1));
            
            createSymptom(patient4.getProfile(), "Dolor de espalda", SymptomSeverity.LEVE, LocalDate.now().minusDays(2));

            log.info("✅ DataSeeder completed successfully!");
            log.info("📊 Created:");
            log.info("   - 1 Organization");
            log.info("   - 2 Doctors");
            log.info("   - 4 Patients");
            log.info("   - 5 Appointments");
            log.info("   - 7 Symptoms");
            log.info("");
            log.info("🔐 Login credentials:");
            log.info("   Organization: admin@hospital.com / password123");
            log.info("   Doctor 1: dr.garcia@hospital.com / password123");
            log.info("   Doctor 2: dr.rodriguez@hospital.com / password123");
            log.info("   Patient 1: juan.perez@email.com / password123");
            log.info("   Patient 2: ana.martinez@email.com / password123");
            log.info("   Patient 3: pedro.lopez@email.com / password123");
            log.info("   Patient 4: laura.sanchez@email.com / password123");
            
        } catch (Exception e) {
            log.error("❌ Error in DataSeeder: {}", e.getMessage(), e);
        }
    }

    private User createOrganization() {
        User organization = User.builder()
                .email("admin@hospital.com")
                .password(passwordEncoder.encode("password123"))
                .organizationName("Hospital Central")
                .country("México")
                .city("Ciudad de México")
                .phone("+52 55 1234 5678")
                .address("Av. Reforma 123, Col. Centro")
                .website("www.hospitalcentral.com")
                .licenseNumber("HOSP-2024-001")
                .taxId("HCM240101ABC")
                .description("Hospital especializado en tratamiento oncológico")
                .role(UserRole.ORGANIZATION)
                .maxDoctors(50)
                .maxPatients(500)
                .isActive(true)
                .isEmailVerified(true)
                .build();
        
        organization = userRepository.save(organization);
        log.info("✓ Created organization: {}", organization.getOrganizationName());
        return organization;
    }

    private DoctorProfile createDoctor(User organization, String firstName, String lastName, String email, 
                                      String specialization, String licenseNumber) {
        // Crear Profile
        Profile profile = Profile.builder()
                .user(organization)
                .profileType(ProfileType.DOCTOR)
                .email(email)
                .password(passwordEncoder.encode("password123"))
                .firstName(firstName)
                .lastName(lastName)
                .phone("+52 55 " + (int)(Math.random() * 9000000 + 1000000))
                .birthDate(LocalDate.of(1980, 1, 1).plusDays((long)(Math.random() * 3650)))
                .city("Ciudad de México")
                .address("Consultorio " + (int)(Math.random() * 500 + 100))
                .isActive(true)
                .build();
        
        profile = profileRepository.save(profile);
        
        // Crear DoctorProfile
        DoctorProfile doctorProfile = DoctorProfile.builder()
                .profile(profile)
                .organization(organization)
                .specialization(specialization)
                .licenseNumber(licenseNumber)
                .yearsOfExperience((int)(Math.random() * 20 + 5))
                .hospitalAffiliation(organization.getOrganizationName())
                .consultationFee(new BigDecimal("1500.00"))
                .bio("Especialista en " + specialization + " con amplia experiencia en tratamientos oncológicos.")
                .isAvailable(true)
                .rating(4.5 + Math.random() * 0.5)
                .totalReviews((int)(Math.random() * 50 + 10))
                .build();
        
        doctorProfile = doctorProfileRepository.save(doctorProfile);
        log.info("✓ Created doctor: Dr. {} {}", firstName, lastName);
        return doctorProfile;
    }

    private PatientProfile createPatient(DoctorProfile doctorProfile, String firstName, String lastName, 
                                        String email, String bloodType, String cancerType, String cancerStage) {
        // Crear Profile
        Profile profile = Profile.builder()
                .user(doctorProfile.getOrganization())
                .profileType(ProfileType.PATIENT)
                .email(email)
                .password(passwordEncoder.encode("password123"))
                .firstName(firstName)
                .lastName(lastName)
                .phone("+52 55 " + (int)(Math.random() * 9000000 + 1000000))
                .birthDate(LocalDate.of(1950, 1, 1).plusDays((long)(Math.random() * 18250)))
                .city("Ciudad de México")
                .address("Calle " + (int)(Math.random() * 100 + 1) + " #" + (int)(Math.random() * 500 + 1))
                .isActive(true)
                .build();
        
        profile = profileRepository.save(profile);
        
        // Crear PatientProfile
        PatientProfile patientProfile = PatientProfile.builder()
                .profile(profile)
                .doctorProfile(doctorProfile)
                .bloodType(bloodType)
                .allergies("Ninguna conocida")
                .emergencyContactName(firstName + " " + lastName + " (Familiar)")
                .emergencyContactPhone("+52 55 " + (int)(Math.random() * 9000000 + 1000000))
                .emergencyContactRelationship("Familiar")
                .medicalHistory("Historial médico general")
                .currentMedications("Según prescripción médica")
                .insuranceProvider("Seguro Popular")
                .insuranceNumber("SP-" + (int)(Math.random() * 900000 + 100000))
                .cancerType(cancerType)
                .cancerStage(cancerStage)
                .diagnosisDate(LocalDate.now().minusMonths((long)(Math.random() * 12 + 1)))
                .treatmentStatus("En tratamiento")
                .lastTreatmentDate(LocalDate.now().minusDays((long)(Math.random() * 30 + 1)))
                .build();
        
        patientProfile = patientProfileRepository.save(patientProfile);
        log.info("✓ Created patient: {} {} (Cancer: {})", firstName, lastName, cancerType);
        return patientProfile;
    }

    private void createAppointment(Profile doctor, Profile patient, LocalDateTime dateTime, AppointmentType type) {
        Appointment appointment = Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .appointmentDate(dateTime)
                .durationMinutes(60)
                .type(type)
                .status(AppointmentStatus.SCHEDULED)
                .location("Consultorio " + (int)(Math.random() * 10 + 1))
                .notes("Cita programada")
                .preparationInstructions("Llegar 15 minutos antes")
                .sendReminder(true)
                .reminderSent(false)
                .build();
        
        appointmentRepository.save(appointment);
        log.info("✓ Created appointment: {} with {} on {}", type, patient.getFullName(), dateTime.toLocalDate());
    }

    private void createSymptom(Profile patient, String symptomName, SymptomSeverity severity, LocalDate date) {
        Symptom symptom = Symptom.builder()
                .profile(patient)
                .symptomName(symptomName)
                .severity(severity)
                .occurrenceDate(date)
                .occurrenceTime(LocalTime.of(10 + (int)(Math.random() * 8), (int)(Math.random() * 60)))
                .durationHours(2 + (int)(Math.random() * 6))
                .notes("Síntoma reportado por el paciente")
                .requiresMedicalAttention(severity == SymptomSeverity.SEVERA || severity == SymptomSeverity.CRITICA)
                .reportedToDoctor(false)
                .build();
        
        symptomRepository.save(symptom);
        log.info("✓ Created symptom: {} ({}) for {}", symptomName, severity, patient.getFullName());
    }
}

