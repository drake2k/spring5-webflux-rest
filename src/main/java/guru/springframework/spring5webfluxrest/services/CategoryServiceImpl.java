package guru.springframework.spring5webfluxrest.services;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.repository.CategoryRepository;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Flux<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Mono<Category> getCategoryById(String id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Flux<Category> saveAllCategories(Publisher<Category> categoryPublisher) {
        return categoryRepository.saveAll(categoryPublisher);
    }

    @Override
    public Mono<Category> saveCategory(String id, Category category) {
        category.setId(id);
        return  categoryRepository.save(category);
    }

    @Override
    public Mono<Category> patchCategory(String id, Category category) {
        return categoryRepository.findById(id).map(cat ->
        {
            if(category.getDescription()!=null)
                cat.setDescription(category.getDescription());

            saveCategory(id, cat);
            return (cat);
        });
    }

}
