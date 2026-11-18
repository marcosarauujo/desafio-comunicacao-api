package com.luizalebs.comunicacao_api.fixture;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;

import java.util.Date;

public class ComunicacaoInDTOFixture {

    public static ComunicacaoInDTO build(Date dataHoraEnvio,
                                         String nomeDestinatario,
                                         String emailDestinatario,
                                         String telefoneDestinatario,
                                         String mensagem,
                                         ModoEnvioEnum modoDeEnvio,
                                         StatusEnvioEnum statusEnvio) {

        return new ComunicacaoInDTO(dataHoraEnvio, nomeDestinatario, emailDestinatario, telefoneDestinatario, mensagem, modoDeEnvio, statusEnvio);
    }
}
