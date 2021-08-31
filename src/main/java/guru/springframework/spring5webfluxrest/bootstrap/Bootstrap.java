package guru.springframework.spring5webfluxrest.bootstrap;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repository.CategoryRepository;
import guru.springframework.spring5webfluxrest.repository.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if(categoryRepository.count().block() > 0)
            return;

        Category cat1 = new Category();
        cat1.setDescription("Red");
        cat1.setId(UUID.randomUUID().toString());

        Category cat2 = new Category();
        cat2.setDescription("Blue");
        cat2.setId(UUID.randomUUID().toString());

        categoryRepository.save(cat1).block();
        categoryRepository.save(cat2).block();
        // much nicer
        categoryRepository.save(Category.builder().description("Green").id(UUID.randomUUID().toString()).build()).block();

        Vendor vendor1 = new Vendor();
        vendor1.setFirstName("John");
        vendor1.setLastName("Doe");
        vendor1.setId(UUID.randomUUID().toString());


        Vendor vendor2 = new Vendor();
        vendor2.setFirstName("Jane");
        vendor2.setLastName("Doe");
        vendor2.setId(UUID.randomUUID().toString());

        vendorRepository.save(vendor1).block();
        vendorRepository.save(vendor2).block();
    }
}
