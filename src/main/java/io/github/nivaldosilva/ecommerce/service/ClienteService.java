package io.github.nivaldosilva.ecommerce.service;

import io.github.nivaldosilva.ecommerce.dto.ClienteDTO;
import io.github.nivaldosilva.ecommerce.document.Cliente;
import io.github.nivaldosilva.ecommerce.exception.ConflictException;
import io.github.nivaldosilva.ecommerce.exception.NotFoundException;
import io.github.nivaldosilva.ecommerce.mapper.ClienteMapper;
import io.github.nivaldosilva.ecommerce.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteDTO.ClienteResponse cadastrar(ClienteDTO.ClienteRequest request) {
        if (clienteRepository.findByCpf(request.cpf()).isPresent()) {
            throw new ConflictException("CPF já cadastrado: " + request.cpf());
        }

        if (clienteRepository.findByEmail(request.email()).isPresent()) {
            throw new ConflictException("E-mail já cadastrado: " + request.email());
        }

        Cliente cliente = ClienteMapper.toEntity(request);
        clienteRepository.save(cliente);
        return ClienteMapper.toResponse(cliente);
    }

    public ClienteDTO.ClienteResponse buscarPorId(String id) {
        return clienteRepository.findById(id)
                .map(ClienteMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado com id: " + id));
    }

    public ClienteDTO.ClienteResponse buscarPorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf)
                .map(ClienteMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado com CPF: " + cpf));
    }

    public List<ClienteDTO.ClienteResponse> buscarPorNome(String nome) {
        List<Cliente> clientes = clienteRepository.findByNomeCompletoContainingIgnoreCase(nome);
        if (clientes.isEmpty()) {
            throw new NotFoundException("Nenhum cliente encontrado com o nome: " + nome);
        }
        return clientes.stream()
                .map(ClienteMapper::toResponse)
                .toList();
    }

    public List<ClienteDTO.ClienteResponse> buscarTodos() {
        List<Cliente> clientes = clienteRepository.findAll();
        if (clientes.isEmpty()) {
            throw new NotFoundException("Nenhum cliente cadastrado no sistema.");
        }
        return clientes.stream()
                .map(ClienteMapper::toResponse)
                .toList();
    }

    public ClienteDTO.ClienteResponse atualizar(String id, ClienteDTO.ClienteRequest request) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Não é possível atualizar. Cliente não encontrado com id: " + id));

        clienteRepository.findByCpf(request.cpf())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> {
                    throw new ConflictException("O CPF " + request.cpf() + " já pertence a outro cliente.");
                });

        clienteRepository.findByEmail(request.email())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> {
                    throw new ConflictException("O E-mail " + request.email() + " já pertence a outro cliente.");
                });

        clienteExistente.setNomeCompleto(request.nomeCompleto());
        clienteExistente.setCpf(request.cpf());
        clienteExistente.setTelefone(request.telefone());
        clienteExistente.setEmail(request.email());

        clienteExistente.setEndereco(ClienteMapper.toEntity(request).getEndereco());

        clienteRepository.save(clienteExistente);
        return ClienteMapper.toResponse(clienteExistente);
    }

    public void deletarPorId(String id) {
        if (!clienteRepository.existsById(id)) {
            throw new NotFoundException("Falha ao deletar. Cliente não encontrado com id: " + id);
        }
        clienteRepository.deleteById(id);
    }
}