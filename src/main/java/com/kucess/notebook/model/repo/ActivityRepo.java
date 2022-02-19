package com.kucess.notebook.model.repo;

import com.kucess.notebook.model.entity.Activity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ActivityRepo extends CrudRepository<Activity, Long>{

    @Query("select A from Activity A where A.admin.userName = ?1 and A.employee.userName = ?2")
    List<Activity> findByAdminAndEmployee(String adminUserName, String employeeUserName);

}
