package com.augusto.employees.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.augusto.employees.model.Photo;

public interface PhotoRepository extends JpaRepository<Photo,Long> {
    
}
