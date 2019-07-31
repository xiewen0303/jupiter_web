package com.jupiter.web.manager.bus.appUser.dto;

public enum UserActionType {

    LOGIN("login"), CHANGE_PWD("change_pwd");

    private String name;

    UserActionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static boolean checkType(String type) {
        for (UserActionType actionType : UserActionType.values()) {
            if (type.equals(actionType.getName())) {
                return true;
            }
        }
        return false;
    }

}
