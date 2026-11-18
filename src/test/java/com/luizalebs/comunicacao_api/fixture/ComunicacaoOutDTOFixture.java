package com.luizalebs.comunicacao_api.fixture;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;

import java.util.Date;

public class ComunicacaoOutDTOFixture {
    public static ComunicacaoOutDTO build(Date dataHoraEnvio,
                                          String nomeDestinatario,
                                          String emailDestinatario,
                                          String telefoneDestinatario,
                                          String mensagem,
                                          ModoEnvioEnum modoDeEnvio,
                                          StatusEnvioEnum statusEnvio) {

        return new ComunicacaoOutDTO(dataHoraEnvio, nomeDestinatario, emailDestinatario, telefoneDestinatario, mensagem, modoDeEnvio, statusEnvio);
    }
}
