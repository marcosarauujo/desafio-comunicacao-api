package com.luizalebs.comunicacao_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luizalebs.comunicacao_api.api.ComunicacaoController;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.business.service.ComunicacaoService;
import com.luizalebs.comunicacao_api.fixture.ComunicacaoInDTOFixture;
import com.luizalebs.comunicacao_api.fixture.ComunicacaoOutDTOFixture;
import com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
public class ComunicacaoControllerTest {

    @InjectMocks
    ComunicacaoController comunicacaoController;
    @Mock
    ComunicacaoService comunicacaoService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String url;

    ComunicacaoOutDTO comunicacaoOutDTO;
    ComunicacaoInDTO comunicacaoInDTO;

    ComunicacaoOutDTO comunicacaoOutDTOCancelado;
    ComunicacaoOutDTO comunicacaoOutDTOEnviado;
    private String json;
    private String email;
    Date dataAtual;

    @BeforeEach
    public void setup() throws Exception {


        mockMvc = MockMvcBuilders.standaloneSetup(comunicacaoController).alwaysDo(print()).build();
        url = "/comunicacao";
        email = "mvra@email.com";
        dataAtual = new Date();


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
        json = objectMapper.writeValueAsString(comunicacaoInDTO);

    }

    @Test
    void deveAgendarComsucesso() throws Exception {
        when(comunicacaoService.agendarComunicacao(comunicacaoInDTO)).thenReturn(comunicacaoOutDTO);

        mockMvc.perform(post(url + "/agendar")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk());

        verify(comunicacaoService).agendarComunicacao(comunicacaoInDTO);
        verifyNoMoreInteractions(comunicacaoService);
    }

    @Test
    void naoDeveAgendarComunicacaoNulo() throws Exception {
        mockMvc.perform(post(url + "/agendar")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("")
        ).andExpect(status().isBadRequest());
        verifyNoInteractions(comunicacaoService);
    }

    //buscarStatus
    @Test
    void deveBuscarStatusComsucesso() throws Exception {
        when(comunicacaoService.buscarStatusComunicacao(email)).thenReturn(comunicacaoOutDTO);

        mockMvc.perform(get(url)
                .param("emailDestinatario", email)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        verify(comunicacaoService).buscarStatusComunicacao(email);
        verifyNoMoreInteractions(comunicacaoService);
    }

    @Test
    void naoDeveBuscarStatusCasoEmailAusente() throws Exception {

        mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(comunicacaoService);
    }

    //AlterarStatus
    @Test
    void deveAlterarStatusComunicacaoComSucesso() throws Exception {
        when(comunicacaoService.alterarStatusComunicacao(email)).thenReturn(comunicacaoOutDTOCancelado);
        mockMvc.perform(patch(url + "/cancelar")
                .param("emailDestinatario", email)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        verify(comunicacaoService).alterarStatusComunicacao(email);
        verifyNoMoreInteractions(comunicacaoService);
    }

    @Test
    void naoDeveAlterarStatusComunicacaoEmailAusente() throws Exception {

        mockMvc.perform(patch(url + "/cancelar")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(comunicacaoService);
    }

    //Enviar

    @Test
    void deveEnviarComunicacaoComSucesso() throws Exception {
        when(comunicacaoService.enviarComunicacao(email)).thenReturn(comunicacaoOutDTOEnviado);

        mockMvc.perform(post(url + "/enviar")
                .param("emailDestinatario", email)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk());

        verify(comunicacaoService).enviarComunicacao(email);
        verifyNoMoreInteractions(comunicacaoService);
    }

    @Test
    void naoDeveEnviarComunicacaoCasoParametroEmailAusente() throws Exception {

        mockMvc.perform(post(url + "/enviar")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(comunicacaoService);
    }
}
