package com.smart_contact_manager.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smart_contact_manager.entities.User;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
    // extra methods db relatedoperations
    // custom query methods
    // custom finder methods

   User findByEmail(String email);

    User findByEmailAndPassword(String email, String password);

}
