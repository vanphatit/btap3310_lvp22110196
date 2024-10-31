package me.phatlee.btap3110.controllers;

import jakarta.validation.Valid;
import me.phatlee.btap3110.entities.Category;
import me.phatlee.btap3110.models.CategoryModel;
import me.phatlee.btap3110.services.iCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("categories")
public class CategoryController {
    @Autowired
    iCategoryService categoryService;

    public CategoryController(iCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public String getListCategory(Model model,
                                  @RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);

        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("categoryName")); // Thay đổi nếu cần
        Page<Category> categoryPage = categoryService.findAll(pageable); // Lấy đối tượng Page<Category>

        model.addAttribute("categoryPage", categoryPage); // Truyền categoryPage vào model

        int totalPages = categoryPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "view/list";
    }

    @GetMapping("/add")
    public String getCreateCategoryPage(Model model) {
        CategoryModel category = new CategoryModel();
        category.setIsEdit(false);
        model.addAttribute("category", category);
        return "view/modify";
    }

    @PostMapping("/save")
    public ModelAndView handleModifyForm(ModelMap model,
                                         @Valid @ModelAttribute("category") CategoryModel categoryModel,
                                         BindingResult bindingResult,
                                         @RequestParam("image") MultipartFile file) {
        Category entity = new Category();
        if (bindingResult.hasErrors()) {
            return new ModelAndView("view/modify");
        }

        String image = this.handleSaveUploadFile(file, "D:\\upload");
        categoryModel.setImages(image);

        BeanUtils.copyProperties(categoryModel, entity);

        categoryService.save(entity);

        String message = "";

        if (categoryModel.getIsEdit()) {
            message="Category is edited successfully";
        } else {
            message=" Category is saved successfully";
        }

        model.addAttribute("message", message);
        return new ModelAndView("redirect:/categories", model);
    }

    @GetMapping("/edit/{categoryId}")
    public ModelAndView getEditCategoryPage(ModelMap model, @PathVariable("categoryId") Long categoryId) {
        Optional<Category> optionalCategory = categoryService.findById(categoryId);
        CategoryModel category = new CategoryModel();
        if (optionalCategory.isPresent()) {
            Category categoryEntity = optionalCategory.get();
            BeanUtils.copyProperties(categoryEntity, category);
            category.setIsEdit(true);
            model.addAttribute("category", category);
            return new ModelAndView("view/modify",model);
        }
        model.addAttribute("message", "Category is not existed");
        return new ModelAndView("forward:/categories", model);
    }

    @GetMapping("/delete/{categoryId}")
    public ModelAndView handleDeleteForm(ModelMap model, @PathVariable("categoryId") Long categoryId) {
        categoryService.deleteById(categoryId);
        model.addAttribute("message", "Category deleted successfully");
        return new ModelAndView("redirect:/categories", model);
    }

    @RequestMapping("/searchpaginated")
    public String search(ModelMap model,
                         @RequestParam(name="name", required = false) String name,
                         @RequestParam("page") Optional<Integer> page,
                         @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);

        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("categoryName")); // Sửa Sort theo tên thực tế

        Page<Category> resultPage;
        if (StringUtils.hasText(name)) {
            resultPage = categoryService.findByCategoryNameContaining(name, pageable);
            model.addAttribute("name", name);
        } else {
            resultPage = categoryService.findAll(pageable);
        }

        model.addAttribute("categoryPage", resultPage); // Truyền categoryPage vào model

        int totalPages = resultPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "view/list";
    }


    public String handleSaveUploadFile(MultipartFile file, String uploadDir) {
        if (!file.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
                Path path = Paths.get(uploadDir, fileName);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                return fileName;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}