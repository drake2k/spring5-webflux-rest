package guru.springframework.spring5webfluxrest.services;

import guru.springframework.spring5webfluxrest.domain.Category;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface CategoryService {
    Flux<Category> getCategories();
    Mono<Category> getCategoryById(String id);
    Flux<Category> saveAllCategories(Publisher<Category> categoryPublisher);
    Mono<Category> saveCategory(Category category);
}
