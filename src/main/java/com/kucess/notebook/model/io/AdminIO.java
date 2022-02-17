package com.kucess.notebook.model.io;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder(toBuilder = true)
@Getter
@NoArgsConstructor
public class AdminIO extends UserIO {

    private List<EmployeeIO> employeeIOs;

}
