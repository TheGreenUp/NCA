package ru.green.nca.enums;

public enum UserRole {
    ADMIN(1),
    JOURNALIST(2),
    SUBSCRIBER(3);

    private final int value;

    UserRole(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
