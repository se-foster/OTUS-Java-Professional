package ru.otus.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import ru.otus.dao.repository.PhoneRepository;
import ru.otus.dao.sessionmanager.TransactionManager;
import ru.otus.model.Phone;

@Service
public class PhoneServiceImpl implements PhoneService {
    private final PhoneRepository repository;
    private final TransactionManager transactionManager;

    public PhoneServiceImpl(PhoneRepository repository, TransactionManager transactionManager) {
        this.repository = repository;
        this.transactionManager = transactionManager;
    }

    @Override
    public void save(Phone phone) {
        transactionManager.doInTransaction(() -> Optional.ofNullable(repository.save(phone)));
    }
}
