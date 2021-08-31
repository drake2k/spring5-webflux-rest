package guru.springframework.spring5webfluxrest.services;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VendorService {
    Flux<Vendor> getVendors();
    Mono<Vendor> getVendorById(String id);
    Flux<Vendor> saveAllVendors(Publisher<Vendor> vendorPublisher);
    Mono<Vendor> saveVendor(String id, Vendor Vendor);
    Mono<Vendor> patchVendor(String id, Vendor vendor);
}
