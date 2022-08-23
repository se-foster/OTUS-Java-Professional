package ru.otus.service;

import java.util.Optional;

import ru.otus.model.Address;

public interface AddressService {
    Optional<Address> getAddressById(long id);
    Optional<Address> saveByName(String street);
}
