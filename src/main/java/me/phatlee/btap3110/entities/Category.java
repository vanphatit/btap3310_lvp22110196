package me.phatlee.btap3110.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "categories")
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CategoryId")
    private int categoryId;

    @Column(name = "CategoryName", columnDefinition = "NVARCHAR(200) NOT NULL")
    @NotEmpty(message = "You must have a name for Category")
    private String categoryName;

    @Column(name = "Images", columnDefinition = "NVARCHAR(500) NULL")
    private String images;

    @Column(name = "Status")
    private int status;
}