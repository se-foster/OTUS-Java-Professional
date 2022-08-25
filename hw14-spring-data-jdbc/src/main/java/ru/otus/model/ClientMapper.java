package ru.otus.model;

import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import ru.otus.service.AddressService;

@Component
public class ClientMapper {
    private final AddressService addressService;

    public ClientMapper(AddressService addressService) {
        this.addressService = addressService;
    }

    public ClientDTO entityToDto(Client client) {
        return new ClientDTO(client.getName(), getStreet(client), getPhoneNumbers(client));
    }

    public Client dtoToEntity(ClientDTO clientDto) {
        return new Client(clientDto.name(), getAddressId(clientDto.street()), new HashSet<>());
    }

    private Long getAddressId(String street) {
        return addressService.saveByName(street)
                .map(Address::getId)
                .orElseThrow(() -> new IllegalArgumentException("No address was saved"));
    }

    private String getStreet(Client client) {
        var address = addressService.getAddressById(client.getAddressId());
        return address.map(Address::getStreet)
                .orElseThrow(() -> new IllegalArgumentException("No address"));
    }

    private String getPhoneNumbers(Client client) {
        return client.getPhones()
                .stream()
                .map(Phone::getNumber)
                .collect(Collectors.joining(" "));
    }
}
