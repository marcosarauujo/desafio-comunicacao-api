package com.luizalebs.comunicacao_api.api.mapper;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface ComunicacaoMapper {
    @Mapping(target = "dataHoraenvio", source = "dataHoraEnvio")
    ComunicacaoEntity paraEntity(ComunicacaoInDTO dto);

    @Mapping(target = "dataHoraEnvio", source = "dataHoraenvio")
    ComunicacaoOutDTO paraDTO(ComunicacaoEntity entity);
}
