package com.example.Movies.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le label du tag est obligatoire")
    @Size(min = 2, max = 50, message = "Le label doit faire entre 2 et 50 caractères")
    @Column(unique = true, nullable = false)
    private String label;

    @ManyToMany(mappedBy = "tags") // <-- Doit être identique au nom du champ dans Movie
    private Set<Movie> movies = new HashSet<>();
}