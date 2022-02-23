package com.kucess.notebook.model.service;

import com.kucess.notebook.bussiness.IOUserConvertor;
import com.kucess.notebook.model.entity.Admin;
import com.kucess.notebook.model.entity.AuthorityType;
import com.kucess.notebook.model.io.AdminIO;
import com.kucess.notebook.model.repo.AdminRepo;
import com.kucess.notebook.model.repo.EmployeeRepo;
import org.springframework.stereotype.Service;

@Service
public class AdminService extends UserService {

    private final IOUserConvertor ioUserConvertor;

    public AdminService(AdminRepo adminRepo, EmployeeRepo employeeRepo, IOUserConvertor ioUserConvertor) {
        super(adminRepo, employeeRepo);
        this.ioUserConvertor = ioUserConvertor;
    }

    /**
     * Register new admin
     */
    public void saveAdmin(AdminIO adminIO){
        Admin admin = Admin.builder()
                .name(adminIO.getName())
                .lastName(adminIO.getLastName())
                .userName(adminIO.getUserName())
                .password(adminIO.getPassword())
                .authorityType(AuthorityType.ADMIN)
                .build();
        super.getAdminRepo().save(admin);
    }

    /**
     * Update admin
     */
    public void updateAdmin(AdminIO adminIO, String userName){
        Admin admin = getAdminByUserName(userName);
        admin.setName(adminIO.getName());
        admin.setLastName(adminIO.getLastName());
        admin.setUserName(adminIO.getUserName());
        admin.setPassword(adminIO.getPassword());
    }

    /**
     * Delete admin
     */
    public void deleteAdminByUserName(String userName){
        super.getAdminRepo().delete(
                getAdminByUserName(userName)
        );
    }

    public AdminIO findAdminByUserName(String userName){
        Admin admin = getAdminByUserName(userName);
        return ioUserConvertor.adminsToIO(admin);
    }



}
