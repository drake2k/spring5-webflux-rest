package guru.springframework.spring5webfluxrest.services;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repository.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VendorServiceImpl implements VendorService{
    private final VendorRepository vendorRepository;

    public VendorServiceImpl(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Override
    public Flux<Vendor> getVendors() {
        return vendorRepository.findAll();
    }

    @Override
    public Mono<Vendor> getVendorById(String id) {
        return vendorRepository.findById(id);
    }

    @Override
    public Flux<Vendor> saveAllVendors(Publisher<Vendor> vendorPublisher) {
        return vendorRepository.saveAll(vendorPublisher);
    }

    @Override
    public Mono<Vendor> saveVendor(String id, Vendor vendor) {
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @Override
    public Mono<Vendor> patchVendor(String id, Vendor vendor) {
        return vendorRepository.findById(id).map(vend ->
        {
            if(vendor.getFirstName()!=null)
                vend.setFirstName(vendor.getFirstName());

            if(vendor.getLastName()!=null)
                vend.setLastName(vendor.getLastName());

            vendorRepository.save(vend);
            return (vend);
        });
    }
}
