package com.concord.trivio.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "equipment")
public class Equipment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "equipment_id")
    private Long id_equipment;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "model", length = 50)
    private String model;

    @Column(name = "manufacturer", length = 50)    
    private String manufacturer;

    @Column(name = "active", length = 1)
    private Boolean active = true;

    
}
