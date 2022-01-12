package com.example.lock;

import java.util.List;
import java.util.stream.Collectors;

import com.example.lock.entity.ClientDTO;
import com.example.lock.entity.ErrorDTO;
import com.example.lock.exception.NotFoundException;
import com.example.lock.mapper.ClientMapper;
import com.example.lock.mongodb.ClientRepository;
import com.example.lock.service.LockService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestController
@AllArgsConstructor
public class ClientController {

  private static final String SCHEDULER_LOCK = "CONTROLLER_LOCK";

  private static final long LOCK_EXPIRATION = 120;

  ClientRepository clientRepository;

  LockService lockService;

  ClientMapper clientMapper;

  @RequestMapping(value = "/client",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      method = RequestMethod.POST)
  ResponseEntity<Object> addClient(@Valid @RequestBody ClientDTO request) {
    ClientDTO client = new ClientDTO();
    client.setName(request.getName());
    var clientDocument = clientRepository.insert(clientMapper.asClientDocument(client));
    return ResponseEntity.status(HttpStatus.CREATED).body(clientMapper.asClientDTO(clientDocument));
  }

  @RequestMapping(value = "/client",
      produces = MediaType.APPLICATION_JSON_VALUE,
      method = RequestMethod.GET)
  ResponseEntity<List<ClientDTO>> getClients() {
    return ResponseEntity.status(HttpStatus.OK)
        .body(clientRepository.findAll().stream().map(c -> clientMapper.asClientDTO(c)).collect(Collectors.toList()));
  }

  @RequestMapping(value = "/client/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE,
      method = RequestMethod.GET)
  ResponseEntity<ClientDTO> getClientById(@PathVariable("id") @Valid String id) throws NotFoundException {
    var clientDocument = clientRepository.findById(id);
    if (clientDocument.isPresent()) {
      return ResponseEntity.ok(clientMapper.asClientDTO(clientDocument.get()));
    } else {
      throw new NotFoundException("404", "CLIENT NOT FOUND", "Client not found");
    }
  }

  @GetMapping("/client/count")
  ResponseEntity<Long> getClientCount() {
    return ResponseEntity.ok(clientRepository.count());
  }

  @ExceptionHandler(value = NotFoundException.class)
  protected ResponseEntity<Object> handleNotFoundException(final NotFoundException ex, final WebRequest request) {
    log.error("Entity not found exception - {}", ex.getMessage());
    ErrorDTO errorDTO = new ErrorDTO();
    errorDTO.setCode(HttpStatus.NOT_FOUND.value());
    errorDTO.setTitle(ex.getTitle());
    errorDTO.setDetail(ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
  }
}
