package com.kucess.notebook.model.service;

import com.kucess.notebook.bussiness.IOUserConvertor;
import com.kucess.notebook.model.entity.Admin;
import com.kucess.notebook.model.entity.AuthorityType;
import com.kucess.notebook.model.io.AdminIO;
import com.kucess.notebook.model.repo.PersonRepo;
import com.kucess.notebook.model.service.exception.DuplicateUsernameException;
import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService extends UserService {

    private final IOUserConvertor ioUserConvertor;

    private final PasswordEncoder passwordEncoder;

    public AdminService(PersonRepo personRepo, IOUserConvertor ioUserConvertor, PasswordEncoder passwordEncoder) {
        super(personRepo);
        this.ioUserConvertor = ioUserConvertor;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Register new admin
     */
    @SneakyThrows
    public void saveAdmin(AdminIO adminIO){
        if (existsByUsername(adminIO.getUserName()))
            throw new DuplicateUsernameException(adminIO.getUserName());
        Admin admin = Admin.builder()
                .name(adminIO.getName())
                .lastName(adminIO.getLastName())
                .userName(adminIO.getUserName())
                .password(passwordEncoder.encode(adminIO.getPassword()))
                .authorityType(AuthorityType.ADMIN)
                .build();
        super.getPersonRepo().save(admin);
    }

    /**
     * Update admin
     */
    public void updateAdmin(AdminIO adminIO, String userName){
        Admin admin = (Admin) getUserByUserName(userName);
        admin.setName(adminIO.getName());
        admin.setLastName(adminIO.getLastName());
        admin.setUserName(adminIO.getUserName());
        admin.setPassword(passwordEncoder.encode(adminIO.getPassword()));
    }

    /**
     * Delete admin
     */
    public void deleteAdminByUserName(String userName){
        super.getPersonRepo().delete(
                getUserByUserName(userName)
        );
    }

    public AdminIO findAdminByUserName(String userName){
        Admin admin = (Admin) getUserByUserName(userName);
        return ioUserConvertor.adminsToIO(admin);
    }





}
