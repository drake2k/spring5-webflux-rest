package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.services.CategoryService;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CategoryController {

    public static final String BASE_URL = "/api/v1/categories/";

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(BASE_URL)
    public Flux<Category> list(){
        return categoryService.getCategories();
    }

    @GetMapping(BASE_URL + "{id}")
    public Mono<Category> getById(@PathVariable String id){
        return categoryService.getCategoryById(id).switchIfEmpty(Mono.error( new ResponseStatusException(HttpStatus.NOT_FOUND, "Nothing here")));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(BASE_URL)
    public Mono<Void> createCategory(@RequestBody  Publisher<Category> categoryPublisher){
        return categoryService.saveAllCategories(categoryPublisher).then().then();
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping(BASE_URL + "{id}")
    public Mono<Category> updateCategory(@PathVariable String id, @RequestBody Category category) {
        category.setId(id);
        return categoryService.saveCategory(category);
    }


}
