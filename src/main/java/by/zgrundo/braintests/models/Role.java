package by.zgrundo.braintests.models;

public enum Role {

    ROLE_ADMIN("Админ"),
    ROLE_USER("Ученик"),

    ROLE_TEACHER("Учитель");

    private final String roleValue;

    private Role(String roleValue) {
        this.roleValue = roleValue;
    }

    public String getRoleValue() {
        return roleValue;
    }
}

