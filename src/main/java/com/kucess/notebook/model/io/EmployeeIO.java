package com.kucess.notebook.model.io;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
@NoArgsConstructor
public class EmployeeIO extends UserIO {

    private List<ActivityIO> activityIOs;

    private List<AdminIO> adminIOS;

}
