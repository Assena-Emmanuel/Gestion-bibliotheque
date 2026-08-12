package com.biblio.app.categories.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblio.app.categories.dto.CategorieRequest;
import com.biblio.app.categories.service.CategorieService;
import com.biblio.app.common.response.ResponseDto;
import com.biblio.app.common.constant.ApiMessages;

import lombok.AllArgsConstructor;

import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@AllArgsConstructor
@RequestMapping("/api/categories")
public class CategorieController {
    private final CategorieService categorieService;


    @GetMapping
    public ResponseDto getMethodName() {
        return new ResponseDto(
            true,
            ApiMessages.CATEGORIES_RETRIEVED,
            categorieService.getAllCategories()
        );
    }

    @GetMapping("/{id}")
    public ResponseDto getCategorieById(@PathVariable UUID id) {
        return new ResponseDto(
            true,
            ApiMessages.CATEGORY_RETRIEVED,
            categorieService.getCategorieById(id)
        );
    }

    @PostMapping
    public ResponseDto postCategorie(@RequestBody CategorieRequest categorieRequest) {
        if(categorieService.existsByName(categorieRequest.getName())) {
            return new ResponseDto(
                false,
                ApiMessages.categoryAlreadyExistsWithName(categorieRequest.getName()),
                null
            );
        }
        return new ResponseDto(
            true,
            ApiMessages.CATEGORY_CREATED,
            categorieService.createCategorie(categorieRequest)
        );
    }

    @PostMapping("/more")
    public ResponseDto postMoreCategories(@RequestBody java.util.List<CategorieRequest> categorieRequests) {
        return new ResponseDto(
            true,
            ApiMessages.CATEGORIES_CREATED,
            categorieService.createCategories(categorieRequests)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseDto deleteCategorieById(@PathVariable UUID id) {
        categorieService.deleteCategorieById(id);
        return new ResponseDto(
            true,
            ApiMessages.CATEGORY_DELETED,
            null
        );
    }   

    @PatchMapping("/{id}")
    public ResponseDto updateCategorie(@PathVariable UUID id, @RequestBody CategorieRequest categorieRequest) {
        return new ResponseDto(
            true,
            ApiMessages.CATEGORY_UPDATED,
            categorieService.updateCategorie(id, categorieRequest)
        ); 
    }
    
}
