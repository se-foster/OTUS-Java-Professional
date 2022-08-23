package ru.otus.dao.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.model.Address;

public interface AddressRepository extends CrudRepository<Address, Long> {
    Address save(Address address);
}
