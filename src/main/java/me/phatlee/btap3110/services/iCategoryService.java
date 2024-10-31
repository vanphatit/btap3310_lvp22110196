package me.phatlee.btap3110.services;

import me.phatlee.btap3110.entities.Category;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface iCategoryService {
    long count();

    void deleteById(Long id);

    List<Category> findAll();

    List<Category> findAll(Sort sort);

    Page<Category> findAll(Pageable pageable);

    Page<Category> findByCategoryNameContaining(String categoryName, Pageable pageable);

    Optional<Category> findByCategoryName(String categoryName);

    <S extends Category> S save(S entity);

    Optional<Category> findById(Long aLong);
}
