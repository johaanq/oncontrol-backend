package com.oncontrol.oncontrolbackend.symptoms.domain.model;

public enum SymptomSeverity {
    LEVE("Leve"),
    MODERADA("Moderada"),
    SEVERA("Severa"),
    CRITICA("Crítica");

    private final String displayName;

    SymptomSeverity(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
