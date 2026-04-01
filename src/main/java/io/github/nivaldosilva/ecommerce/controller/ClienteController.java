package io.github.nivaldosilva.ecommerce.controller;

import io.github.nivaldosilva.ecommerce.dto.ClienteDTO;
import io.github.nivaldosilva.ecommerce.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteDTO.ClienteResponse> novoCliente(
            @RequestBody @Valid ClienteDTO.ClienteRequest request) {
        ClienteDTO.ClienteResponse response = clienteService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO.ClienteResponse> buscarPorId(@PathVariable String id) {
        ClienteDTO.ClienteResponse response = clienteService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<ClienteDTO.ClienteResponse> buscarPorCpf(@PathVariable String cpf) {
        ClienteDTO.ClienteResponse response = clienteService.buscarPorCpf(cpf);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO.ClienteResponse>> buscarTodos() {
        List<ClienteDTO.ClienteResponse> response = clienteService.buscarTodos();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO.ClienteResponse> atualizar(
            @PathVariable String id,
            @RequestBody @Valid ClienteDTO.ClienteRequest request) {
        ClienteDTO.ClienteResponse response = clienteService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        clienteService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

}