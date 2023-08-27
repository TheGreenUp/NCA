package ru.green.nca.enums;

public enum UserRole {
    NONE(0),
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
