package com.kucess.notebook.model.response;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Message {

    public static final String ADMIN_REGISTERED = "Admin successfully registered";
    public static final String ADMIN_UPDATED = "Admin successfully updated";
    public static final String ADMIN_DELETED = "Admin successfully deleted";

    public static final String EMPLOYEE_ADD_TOO_ADMIN = "New employee added to admin";
    public static final String CURRENT_EMPLOYEE_ADD_TO_ADMIN = "employee added to admin";
    public static final String EMPLOYEE_REMOVED_FROM_ADMIN = "employee removed from admin";
    public static final String EMPLOYEE_PASSWORD_CHANGE = "Employee password changed";

    public static final String ACTIVITY_ADDED_TO_EMPLOYEE = "Activity added to employee";
    public static final String ACTIVITY_REMOVED_FROM_ADMIN = "Admin activity removed";
    public static final String ACTIVITY_UPDATED = "Activity updated";
}
