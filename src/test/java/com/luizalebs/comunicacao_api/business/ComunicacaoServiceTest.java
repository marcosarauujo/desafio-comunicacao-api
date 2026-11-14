package com.luizalebs.comunicacao_api.business;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.api.dto.TarefasDTO;
import com.luizalebs.comunicacao_api.api.mapper.ComunicacaoMapper;
import com.luizalebs.comunicacao_api.business.service.ComunicacaoService;
import com.luizalebs.comunicacao_api.fixture.ComunicacaoInDTOFixture;
import com.luizalebs.comunicacao_api.fixture.ComunicacaoOutDTOFixture;
import com.luizalebs.comunicacao_api.infraestructure.client.NotificacaoClient;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.repositories.ComunicacaoRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.util.Date;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


@ExtendWith(MockitoExtension.class)
public class ComunicacaoServiceTest {

    @InjectMocks
    private ComunicacaoService comunicacaoService;

    @Mock
    private ComunicacaoRepository repository;
    @Mock
    private ComunicacaoMapper mapper;
    @Mock
    private NotificacaoClient client;

    @Mock
    private Clock clock;

    ComunicacaoEntity comunicacaoEntity;
    ComunicacaoOutDTO comunicacaoOutDTO;
    ComunicacaoInDTO comunicacaoInDTO;
    Date dataAtual;

    LocalDateTime dataHora;
    TarefasDTO tarefasDTO;


    ComunicacaoOutDTO comunicacaoOutDTOCancelado;
    ComunicacaoOutDTO comunicacaoOutDTOEnviado;

    @BeforeEach
    public void setup() {

        dataAtual = new Date();

        comunicacaoEntity = ComunicacaoEntity.builder()
                .dataHoraenvio(dataAtual)
                .nomeDestinatario("Marcos Araújo")
                .emailDestinatario("mvra@email.com")
                .telefoneDestinatario("987456123")
                .mensagem("Hoje vai ter pizza!")
                .modoDeEnvio(ModoEnvioEnum.EMAIL)
                .statusEnvio(StatusEnvioEnum.PENDENTE)
                .build();

        comunicacaoOutDTO = ComunicacaoOutDTOFixture.build(
                dataAtual,
                "Marcos Araújo",
                "mvra@email.com",
                "987456123",
                "Hoje vai ter pizza!",
                ModoEnvioEnum.EMAIL,
                StatusEnvioEnum.PENDENTE
        );

        comunicacaoInDTO = ComunicacaoInDTOFixture.build(
                dataAtual,
                "Marcos Araújo",
                "mvra@email.com",
                "987456123",
                "Hoje vai ter pizza!",
                ModoEnvioEnum.EMAIL,
                StatusEnvioEnum.PENDENTE
        );

        comunicacaoOutDTOCancelado = ComunicacaoOutDTOFixture.build(
                dataAtual,
                "Marcos Araújo",
                "mvra@email.com",
                "987456123",
                "Hoje vai ter pizza!",
                ModoEnvioEnum.EMAIL,
                StatusEnvioEnum.CANCELADO
        );

        comunicacaoOutDTOEnviado = ComunicacaoOutDTOFixture.build(
                dataAtual,
                "Marcos Araújo",
                "mvra@email.com",
                "987456123",
                "Hoje vai ter pizza!",
                ModoEnvioEnum.EMAIL,
                StatusEnvioEnum.ENVIADO
        );

        dataHora = LocalDateTime.of(2026,11,14,10,0,0);


        tarefasDTO = TarefasDTO.builder()
                .emailUsuario(comunicacaoEntity.getEmailDestinatario())
                .nomeTarefa("Notificação: " + comunicacaoEntity.getNomeDestinatario())
                .descricao(comunicacaoEntity.getMensagem())
                .dataEvento(dataHora)
                .build();
    }

    @Test
    void agendaComunicacaoComSucesso() {
        when(mapper.paraEntity(comunicacaoInDTO)).thenReturn(comunicacaoEntity);
        when(repository.save(comunicacaoEntity)).thenReturn(comunicacaoEntity);
        when(mapper.paraDTO(comunicacaoEntity)).thenReturn(comunicacaoOutDTO);

        ComunicacaoOutDTO resposta = comunicacaoService.agendarComunicacao(comunicacaoInDTO);

        assertEquals(comunicacaoOutDTO, resposta);

        verify(mapper).paraEntity(comunicacaoInDTO);
        verify(repository).save(comunicacaoEntity);
        verify(mapper).paraDTO(comunicacaoEntity);

        verifyNoInteractions(client);
        verifyNoMoreInteractions(repository, mapper);

    }

    @Test
    void buscarStatusComunicacaoComSucesso() {
        when(repository.findByEmailDestinatario(comunicacaoEntity.getEmailDestinatario())).thenReturn(comunicacaoEntity);
        when(mapper.paraDTO(comunicacaoEntity)).thenReturn(comunicacaoOutDTO);

        ComunicacaoOutDTO resposta = comunicacaoService.buscarStatusComunicacao(comunicacaoEntity.getEmailDestinatario());


        assertEquals(comunicacaoOutDTO, resposta);

        verify(repository).findByEmailDestinatario(comunicacaoEntity.getEmailDestinatario());
        verify(mapper).paraDTO(comunicacaoEntity);

        verifyNoInteractions(client);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void alterarStatusComunicacaoComSucesso() {
        when(repository.findByEmailDestinatario(comunicacaoEntity.getEmailDestinatario())).thenReturn(comunicacaoEntity);
        when(repository.save(comunicacaoEntity)).thenReturn(comunicacaoEntity);
        when(mapper.paraDTO(comunicacaoEntity)).thenReturn(comunicacaoOutDTOCancelado);

        ComunicacaoOutDTO resposta = comunicacaoService.alterarStatusComunicacao(comunicacaoEntity.getEmailDestinatario());
        assertEquals(comunicacaoOutDTOCancelado, resposta);

        verify(repository).findByEmailDestinatario(comunicacaoEntity.getEmailDestinatario());
        verify(repository).save(comunicacaoEntity);
        verify(mapper).paraDTO(comunicacaoEntity);

        verifyNoInteractions(client);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void enviarComunicacaoComSucesso() {

        ZoneId zoneId = ZoneId.systemDefault();
        Clock fixedClock = Clock.fixed(dataHora.atZone(zoneId).toInstant(),zoneId);
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();

        when(repository.findByEmailDestinatario(comunicacaoEntity.getEmailDestinatario())).thenReturn(comunicacaoEntity);
        when(repository.save(comunicacaoEntity)).thenReturn(comunicacaoEntity);
        when(mapper.paraDTO(comunicacaoEntity)).thenReturn(comunicacaoOutDTOEnviado);

        ComunicacaoOutDTO resposta = comunicacaoService.enviarComunicacao(comunicacaoEntity.getEmailDestinatario());
        assertEquals(comunicacaoOutDTOEnviado, resposta);

        verify(repository).findByEmailDestinatario(comunicacaoEntity.getEmailDestinatario());
        verify(clock).instant();
        verify(clock).getZone();
        verify(client).enviarEmail(tarefasDTO);
        verify(repository).save(comunicacaoEntity);
        verify(mapper).paraDTO(comunicacaoEntity);

        verifyNoMoreInteractions(repository, mapper, client, clock);

    }
}
