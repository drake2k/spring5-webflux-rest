package guru.springframework.spring5webfluxrest.controllers;


import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.services.CategoryService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;

/*@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient*/
public class CategoryControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Mock
    CategoryService categoryService;

    CategoryController categoryController;

    private String aUUID = UUID.randomUUID().toString();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        //categoryService = Mockito.mock(CategoryService.class);
        categoryController = new CategoryController(categoryService);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }


    @Test
    public void TestGetAllCategories() {

        BDDMockito.given(categoryService.getCategories())
                .willReturn(Flux.just(
                        Category.builder().id(UUID.randomUUID().toString()).description("TEST_CAT1").build(),
                        Category.builder().id(UUID.randomUUID().toString()).description("TEST_CAT2").build(),
                        Category.builder().id(aUUID).description("TEST_CAT3").build()
                ));

        webTestClient.get().uri(CategoryController.BASE_URL)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Category.class).
                hasSize(3);
    }

    @Test
    public void TestGetCategoryByIdFound() {
        BDDMockito.given(categoryService.getCategoryById(aUUID))
                .willReturn(Mono.just(new Category(aUUID, "TEST_CAT")));


        webTestClient.get().uri(CategoryController.BASE_URL + aUUID)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8);
    }

    @Test
    public void TestGetCategoryByIdNotFound() {
        BDDMockito.given(categoryService.getCategoryById(aUUID))
                .willReturn(Mono.empty());

        webTestClient.get().uri(CategoryController.BASE_URL + aUUID)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void TestCreateCategories() {
        BDDMockito.given(categoryService.saveAllCategories(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().build()));

        Mono<Category> categoryMono = Mono.just(Category.builder().description("TO SAVE CAT").build());

        webTestClient.post()
                .uri(CategoryController.BASE_URL)
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }


    @Test
    public void TestUpdateCategory() {
        BDDMockito.given(categoryService.saveCategory(any(String.class), any(Category.class)))
                .willReturn(Mono.just(Category.builder().description("Check return value cat").id("42").build()));

        Mono<Category> categoryMono = Mono.just(Category.builder().description("TO SAVE CAT").id("999").build());

        webTestClient.put()
                .uri(CategoryController.BASE_URL + "42")
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus()
                .isAccepted()
                .expectBody(Category.class)
                .value(category -> category.getId().equals("42"));

    }


    @Test
    public void TestPatchCategory() {
        BDDMockito.given(categoryService.patchCategory(any(String.class),any(Category.class)))
                .willReturn(Mono.just(Category.builder().description("Check return value cat").id("42").build()));

        Mono<Category> categoryMono = Mono.just(Category.builder().description("TO SAVE CAT").id("999").build());

        webTestClient.patch()
                .uri(CategoryController.BASE_URL + "42")
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus()
                .isAccepted()
                .expectBody(Category.class)
                .value(category -> category.getDescription().equals("Check return value cat"));
    }

}
