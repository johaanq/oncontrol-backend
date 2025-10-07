package com.oncontrol.oncontrolbackend.symptoms.application.service;

import com.oncontrol.oncontrolbackend.symptoms.application.dto.SymptomRequest;
import com.oncontrol.oncontrolbackend.symptoms.domain.model.Symptom;
import com.oncontrol.oncontrolbackend.symptoms.domain.repository.SymptomRepository;
import com.oncontrol.oncontrolbackend.profiles.domain.model.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class SymptomService {

    private final SymptomRepository symptomRepository;

    public Symptom reportSymptom(SymptomRequest request, Profile profile) {
        Symptom symptom = Symptom.builder()
                .profile(profile)
                .symptomName(request.getSymptomName())
                .severity(request.getSeverity())
                .occurrenceDate(request.getOccurrenceDate())
                .occurrenceTime(request.getOccurrenceTime())
                .durationHours(request.getDurationHours())
                .notes(request.getNotes())
                .triggers(request.getTriggers())
                .managementActions(request.getManagementActions())
                .impactOnDailyLife(request.getImpactOnDailyLife())
                .requiresMedicalAttention(request.getRequiresMedicalAttention())
                .reportedToDoctor(false)
                .build();

        return symptomRepository.save(symptom);
    }

    @Transactional(readOnly = true)
    public List<Symptom> getPatientSymptoms(Profile profile, LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return symptomRepository.findByProfileAndOccurrenceDateBetween(profile, startDate, endDate);
        } else {
            return symptomRepository.findByProfileOrderByOccurrenceDateDesc(profile);
        }
    }

    @Transactional(readOnly = true)
    public List<Symptom> getRecentSymptoms(Profile profile, int days) {
        LocalDate startDate = LocalDate.now().minusDays(days);
        return symptomRepository.findRecentByProfile(profile, startDate);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getSymptomStats(Profile profile) {
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        
        Long totalSymptoms = symptomRepository.countByProfileAndOccurrenceDateAfter(profile, thirtyDaysAgo);
        Long severeSymptoms = symptomRepository.countSevereSymptomsByProfile(profile, thirtyDaysAgo);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSymptoms", totalSymptoms);
        stats.put("severeSymptoms", severeSymptoms);
        stats.put("criticalSymptoms", symptomRepository.countByProfileAndOccurrenceDateAfter(profile, thirtyDaysAgo));
        stats.put("period", "30 days");
        
        return stats;
    }
}
