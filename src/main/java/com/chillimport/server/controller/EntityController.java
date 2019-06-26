package com.chillimport.server.controller;

import com.chillimport.server.entities.Entity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller class receiving requests for creating or getting Entities
 */
@RestController
public abstract class EntityController<T extends Entity> {


    public abstract ResponseEntity<?> create(T entity);

    public abstract ResponseEntity<?> get(int id);

    public abstract ResponseEntity<?> getAll();
}
