package de.hsos.swa.domain.entity;

public enum VoteType {
    UP("up"),
    DOWN("down"),
    NONE("NONE");

    private final String value;

    VoteType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static VoteType fromValue(String value) {
        for (VoteType voteType : VoteType.values()) {
            if (voteType.value.equalsIgnoreCase(value)) {
                return voteType;
            }
        }
        throw new IllegalArgumentException("Invalid value for VoteType: " + value);
    }
}