package com.oncontrol.oncontrolbackend.profiles.infrastructure.controller;

import com.oncontrol.oncontrolbackend.profiles.application.dto.CreateDoctorRequest;
import com.oncontrol.oncontrolbackend.profiles.application.dto.DoctorProfileResponse;
import com.oncontrol.oncontrolbackend.profiles.application.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OrganizationController - Organizations manage doctors
 */
@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
@Tag(name = "Organizations", description = "Organization management endpoints")
public class OrganizationController {

    private final ProfileService profileService;

    @PostMapping("/{organizationId}/doctors")
    @Operation(summary = "Create doctor", description = "Organization creates a new doctor")
    public ResponseEntity<?> createDoctor(
            @PathVariable Long organizationId,
            @Valid @RequestBody CreateDoctorRequest request) {
        try {
            DoctorProfileResponse doctor = profileService.createDoctor(organizationId, request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("doctor", doctor);
            response.put("message", "Doctor created successfully");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error creating doctor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{organizationId}/doctors")
    @Operation(summary = "Get organization doctors", description = "Get all doctors belonging to an organization")
    public ResponseEntity<?> getDoctors(@PathVariable Long organizationId) {
        try {
            List<DoctorProfileResponse> doctors = profileService.getDoctorsByOrganizationId(organizationId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("doctors", doctors);
            response.put("count", doctors.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error retrieving doctors: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{organizationId}/doctors/{doctorId}")
    @Operation(summary = "Get doctor by ID", description = "Get a specific doctor by ID")
    public ResponseEntity<?> getDoctorById(@PathVariable Long organizationId, @PathVariable Long doctorId) {
        try {
            DoctorProfileResponse doctor = profileService.getDoctorProfileById(doctorId);
            
            // Verify doctor belongs to organization
            if (!doctor.getOrganizationId().equals(organizationId)) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Doctor does not belong to this organization");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
            
            return ResponseEntity.ok(doctor);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error retrieving doctor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{organizationId}/dashboard")
    @Operation(summary = "Get organization dashboard", description = "Get organization dashboard with all doctors and statistics")
    public ResponseEntity<?> getOrganizationDashboard(@PathVariable Long organizationId) {
        try {
            List<DoctorProfileResponse> doctors = profileService.getDoctorsByOrganizationId(organizationId);
            
            // Count total patients across all doctors
            int totalPatients = 0;
            for (DoctorProfileResponse doctor : doctors) {
                var patients = profileService.getPatientsByDoctorId(doctor.getId());
                totalPatients += patients.size();
            }

            Map<String, Object> dashboard = new HashMap<>();
            dashboard.put("doctors", Map.of(
                    "total", doctors.size(),
                    "list", doctors
            ));
            dashboard.put("patients", Map.of(
                    "total", totalPatients
            ));

            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error retrieving dashboard: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}

