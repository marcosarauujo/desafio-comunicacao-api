package com.luizalebs.comunicacao_api.business.service;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.api.dto.TarefasDTO;
import com.luizalebs.comunicacao_api.api.mapper.ComunicacaoMapper;
import com.luizalebs.comunicacao_api.infraestructure.client.NotificacaoClient;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.excpetions.BusinessException;
import com.luizalebs.comunicacao_api.infraestructure.excpetions.ResourceNotFoundException;
import com.luizalebs.comunicacao_api.infraestructure.repositories.ComunicacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ComunicacaoService {

    private final ComunicacaoRepository repository;
    private final ComunicacaoMapper mapper;
    private final NotificacaoClient client;

    private final Clock clock;

    public ComunicacaoOutDTO agendarComunicacao(ComunicacaoInDTO dto) {
        if (Objects.isNull(dto)) {
            throw new BusinessException("Os dados do agendamento não podem ser nulos");
        }
        try {
        dto.setStatusEnvio(StatusEnvioEnum.PENDENTE);
        ComunicacaoEntity entity = mapper.paraEntity(dto);
        repository.save(entity);
        return mapper.paraDTO(entity);
    } catch (Exception e){
                throw new BusinessException("Erro ao salvar agendamento de comunicação", e);
            }
        }

    public ComunicacaoOutDTO buscarStatusComunicacao(String emailDestinatario) {
        ComunicacaoEntity entity = repository.findByEmailDestinatario(emailDestinatario);
        if (Objects.isNull(entity)) {
            throw new ResourceNotFoundException("Comunicação não encontrada para o e-mail: " + emailDestinatario);
        }
        return mapper.paraDTO(entity);
    }

    public ComunicacaoOutDTO alterarStatusComunicacao(String emailDestinatario) {
        ComunicacaoEntity entity = repository.findByEmailDestinatario(emailDestinatario);
        if (Objects.isNull(entity)) {
            throw new ResourceNotFoundException("Comunicação não encontrada para o e-mail: " + emailDestinatario);
        }
        entity.setStatusEnvio(StatusEnvioEnum.CANCELADO);
        repository.save(entity);
        return (mapper.paraDTO(entity));
    }

    public ComunicacaoOutDTO enviarComunicacao(String emailDestinatario){
        ComunicacaoEntity entity = repository.findByEmailDestinatario(emailDestinatario);
        if (Objects.isNull(entity)) {
            throw new ResourceNotFoundException("Comunicação não encontrada para o e-mail: " + emailDestinatario);
        }
        TarefasDTO tarefasDTO = TarefasDTO.builder()
                .emailUsuario(entity.getEmailDestinatario())
                .nomeTarefa("Notificação: " + entity.getNomeDestinatario())
                .descricao(entity.getMensagem())
                .dataEvento(LocalDateTime.now(clock))
                .build();
        try {
            client.enviarEmail(tarefasDTO);
        } catch (Exception e){
            throw new BusinessException("Erro ao disparar envio de e-mail na API externa", e);
        }
        entity.setStatusEnvio(StatusEnvioEnum.ENVIADO);
        repository.save(entity);

        return (mapper.paraDTO(entity));
    }



}
