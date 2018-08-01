package com.marcosbarbero.databaseservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "avenger")
@AllArgsConstructor
@NoArgsConstructor
public class Avenger {
    @Id
    private Integer id;
    private String name;
}
