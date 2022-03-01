package com.kucess.notebook.model.io;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
public class EmployeeIO extends UserIO {

    private List<ActivityIO> activityIOs;

    private List<AdminIO> adminIOS;

}
