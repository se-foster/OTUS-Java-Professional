package ru.otus.contoller;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.model.Client;
import ru.otus.model.ClientDTO;
import ru.otus.model.ClientMapper;
import ru.otus.model.Phone;
import ru.otus.service.ClientService;
import ru.otus.service.PhoneService;

@Controller
public class ClientController {

    private final ClientService clientService;
    private final PhoneService phoneService;
    private final ClientMapper mapper;

    public ClientController(ClientService clientService, PhoneService phoneService, ClientMapper mapper) {
        this.clientService = clientService;
        this.phoneService = phoneService;
        this.mapper = mapper;
    }

    @GetMapping("/")
    public String getHomePage() {
        return "index.html";
    }

    @GetMapping("/create_client")
    public String getSaveClientPage() {
        return "create_client.html";
    }

    @PostMapping("/create_client")
    public String saveClient(ClientDTO clientDto) {
        Client client = mapper.dtoToEntity(clientDto);
        Client savedClient = clientService.saveClient(client);
        savePhones(clientDto.phonesNumbers(), savedClient.getId());
        return "redirect:/clients_list";
    }

    private void savePhones(String phonesNumbers, long clientId) {
        String[] phones = phonesNumbers.split(",");
        Arrays.stream(phones).forEach(number-> phoneService.save(new Phone(number, clientId)));
    }

    @GetMapping("/clients_list")
    public String getAllClient(Model model) {
        List<ClientDTO> clients = clientService.findAll()
                .stream()
                .map(mapper::entityToDto)
                .toList();
        model.addAttribute("clients", clients);
        return "clients_list.html";
    }
}
