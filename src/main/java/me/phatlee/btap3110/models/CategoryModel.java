package me.phatlee.btap3110.models;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryModel implements Serializable {
    @Id
    private int categoryId;

    @NotEmpty(message = "You must have a name for Category")
    private String categoryName;

    private String images;

    private int status;

    private Boolean isEdit = false;
}
