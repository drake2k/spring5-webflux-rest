package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.services.VendorService;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class VendorController {

    public static final String BASE_URL = "/api/v1/vendors/";

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping(BASE_URL)
    public Flux<Vendor> list(){
        return  vendorService.getVendors();
    }

    @GetMapping(BASE_URL + "{id}")
    public Mono<Vendor> getById(@PathVariable String id){
        return vendorService.getVendorById(id).switchIfEmpty(Mono.error( new ResponseStatusException(HttpStatus.NOT_FOUND, "Nothing here")));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(BASE_URL)
    public Mono<Void> createCategory(@RequestBody Publisher<Vendor> vendorPublisher){
        return vendorService.saveAllVendors(vendorPublisher).then().then();
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping(BASE_URL + "{id}")
    public Mono<Vendor> updateCategory(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        return vendorService.saveVendor(vendor);
    }
}
