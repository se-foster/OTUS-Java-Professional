package ru.otus.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import ru.otus.dao.repository.AddressRepository;
import ru.otus.dao.sessionmanager.TransactionManager;
import ru.otus.model.Address;

@Service
public class AddressServiceImpl implements AddressService {
    private final TransactionManager transactionManager;
    private final AddressRepository addressRepository;

    public AddressServiceImpl(TransactionManager transactionManager, AddressRepository addressRepository) {
        this.transactionManager = transactionManager;
        this.addressRepository = addressRepository;
    }


    @Override
    public Optional<Address> getAddressById(long id) {
        return transactionManager.doInReadOnlyTransaction(() -> addressRepository.findById(id));
    }

    @Override
    public Optional<Address> saveByName(String street) {
        return transactionManager.doInTransaction(() -> Optional.ofNullable(addressRepository.save(new Address(street))));
    }
}
