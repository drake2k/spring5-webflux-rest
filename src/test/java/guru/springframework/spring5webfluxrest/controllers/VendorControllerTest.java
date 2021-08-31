package guru.springframework.spring5webfluxrest.controllers;


import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.services.VendorService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

public class VendorControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @Mock
    VendorService vendorService;

    VendorController VendorController;

    private String aUUID = UUID.randomUUID().toString();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        //VendorService = Mockito.mock(VendorService.class);
        VendorController = new VendorController(vendorService);
        webTestClient = WebTestClient.bindToController(VendorController).build();
    }


    @Test
    public void TestGetAllVendors(){

        BDDMockito.given(vendorService.getVendors())
                .willReturn(Flux.just(
                        Vendor.builder().id(UUID.randomUUID().toString()).firstName("jf").lastName("beaulac").build(),
                        Vendor.builder().id(UUID.randomUUID().toString()).firstName("john").lastName("doe").build(),
                        Vendor.builder().id(aUUID).firstName("jane").lastName("doe").build()
                ));

        webTestClient.get().uri(VendorController.BASE_URL)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Vendor.class).
                hasSize(3);
    }

    @Test
    public void TestGetVendorByIdFound(){
        BDDMockito.given(vendorService.getVendorById(aUUID))
                .willReturn(Mono.just(new Vendor(aUUID, "jf", "beaulac")));


        webTestClient.get().uri(VendorController.BASE_URL + aUUID)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8);
    }

    @Test
    public void TestGetVendorByIdNotFound(){
        BDDMockito.given(vendorService.getVendorById(aUUID))
                .willReturn(Mono.empty());

        webTestClient.get().uri(VendorController.BASE_URL + aUUID)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void TestCreateVendors()
    {
        BDDMockito.given(vendorService.saveAllVendors(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> vendorMono = Mono.just(Vendor.builder().firstName("Check return").lastName("value vendor").id("42").build());

        webTestClient.post()
                .uri(VendorController.BASE_URL)
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }


    @Test
    public void TestUpdateVendor(){
        BDDMockito.given(vendorService.saveVendor(any(String.class),any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().firstName("Check return").lastName("value vendor").id("42").build()));

        Mono<Vendor> vendorMono = Mono.just(Vendor.builder().firstName("joe").lastName("doe").id("999").build());

        webTestClient.put()
                .uri(VendorController.BASE_URL + "42")
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isAccepted()
                .expectBody(Vendor.class)
                .value(vendor -> vendor.getId().equals("42"));

    }

    @Test
    public void TestPatchVendor() {
        BDDMockito.given(vendorService.patchVendor(any(String.class),any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().firstName("Check return").lastName("value vendor").id("42").build()));

        Mono<Vendor> vendorMono = Mono.just(Vendor.builder().firstName("joe").lastName("doe").id("999").build());

        webTestClient.patch()
                .uri(VendorController.BASE_URL + "42")
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isAccepted()
                .expectBody(Vendor.class)
                .value(vendor -> vendor.getFirstName().equals("Check return"));
    }

}
