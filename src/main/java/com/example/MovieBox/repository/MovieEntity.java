package com.example.MovieBox.repository;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Movies")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieEntity {

    @Id
    String id;
    String name;
    String description;
}
