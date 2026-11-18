package com.luizalebs.comunicacao_api.mapper;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.api.mapper.ComunicacaoMapper;
import com.luizalebs.comunicacao_api.fixture.ComunicacaoInDTOFixture;
import com.luizalebs.comunicacao_api.fixture.ComunicacaoOutDTOFixture;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComunicacaoMapperTest {

    ComunicacaoMapper comunicacaoMapper;
    ComunicacaoEntity comunicacaoEntity;
    ComunicacaoOutDTO comunicacaoOutDTO;
    ComunicacaoInDTO comunicacaoInDTO;
    Date dataAtual;

    @BeforeEach
    public void setup() {
        comunicacaoMapper = Mappers.getMapper(ComunicacaoMapper.class);
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
    }

    @Test
    void deveConverterParaDTOComSucesso() {
        ComunicacaoOutDTO dto = comunicacaoMapper.paraDTO(comunicacaoEntity);
        assertEquals(comunicacaoOutDTO, dto);
    }

    @Test
    void deveConverterParaEntityComSucesso() {
        ComunicacaoEntity entity = comunicacaoMapper.paraEntity(comunicacaoInDTO);
        assertEquals(comunicacaoEntity,entity);

    }


}
