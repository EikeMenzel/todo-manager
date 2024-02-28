package org.tasskservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tasskservice.payload.CategoryDTO;

import java.util.List;


@FeignClient(name = "database-service")
public interface IDatabaseServiceClient {
    String BASE_PATH = "/api/v1/db";
    String CATEGORIES_PATH = BASE_PATH  + "/users/{userId}/categories";

    @PostMapping(CATEGORIES_PATH)
    ResponseEntity<Void> createCategory(@PathVariable Long userId, @RequestBody CategoryDTO categoryDTO);

    @GetMapping(CATEGORIES_PATH)
    ResponseEntity<List<CategoryDTO>> getCategoryDTOList(@PathVariable Long userId);

    @PutMapping(CATEGORIES_PATH + "/{categoryId}")
    ResponseEntity<Void> updateCategory(@PathVariable Long userId, @PathVariable Long categoryId, @RequestBody CategoryDTO categoryDTO);
}
