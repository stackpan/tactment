package com.ivanzkyanto.tactment.repository;

import com.ivanzkyanto.tactment.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, String> {
}
