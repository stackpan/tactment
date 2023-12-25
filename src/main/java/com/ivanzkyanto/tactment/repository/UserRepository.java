package com.ivanzkyanto.tactment.repository;

import com.ivanzkyanto.tactment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
