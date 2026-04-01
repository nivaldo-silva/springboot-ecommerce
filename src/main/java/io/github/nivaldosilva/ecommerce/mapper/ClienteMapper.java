package io.github.nivaldosilva.ecommerce.mapper;

import io.github.nivaldosilva.ecommerce.dto.ClienteDTO;
import io.github.nivaldosilva.ecommerce.document.Cliente;
import lombok.experimental.UtilityClass;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class ClienteMapper {

    public static Cliente toEntity(ClienteDTO.ClienteRequest request) {
        if (request == null) return null;

        List<io.github.nivaldosilva.ecommerce.document.vo.Endereco> enderecos = request.enderecos() == null
                ? Collections.emptyList()
                : request.enderecos().stream()
                .map(EnderecoMapper::toEntity)
                .toList();

        return Cliente.builder()
                .nome(request.nome())
                .cpf(request.cpf())
                .email(request.email())
                .telefone(request.telefone())
                .enderecos(enderecos)
                .build();
    }

    public static ClienteDTO.ClienteResponse toResponse(Cliente entity) {
        if (entity == null) return null;

        List<ClienteDTO.EnderecoResponse> enderecos = entity.getEnderecos() == null
                ? Collections.emptyList()
                : entity.getEnderecos().stream()
                .map(EnderecoMapper::toResponse)
                .toList();

        return ClienteDTO.ClienteResponse.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .cpf(entity.getCpf())
                .email(entity.getEmail())
                .telefone(entity.getTelefone())
                .enderecos(enderecos)
                .build();
    }
}