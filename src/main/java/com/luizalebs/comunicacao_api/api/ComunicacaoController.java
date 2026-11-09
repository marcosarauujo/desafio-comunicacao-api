package com.luizalebs.comunicacao_api.api;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.business.service.ComunicacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comunicacao")

@Tag(name = "Comunicação", description = "Agendamento de comunicação")
public class ComunicacaoController {

    private final ComunicacaoService service;

    public ComunicacaoController(ComunicacaoService service) {
        this.service = service;
    }

    @PostMapping("/agendar")
    @Operation(summary = "Agendar", description = "Cria um novo agendamento")

    @ApiResponse(responseCode = "200", description = "Agendamento criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de envio inválidos")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")

    public ResponseEntity<ComunicacaoOutDTO> agendar(@RequestBody ComunicacaoInDTO dto)  {
        return ResponseEntity.ok(service.agendarComunicacao(dto));
    }

    @GetMapping()
    @Operation(summary = "Buscar", description = "Buscar agendamentos por email")

    @ApiResponse(responseCode = "200", description = "Agendamento encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Agendamento não encontrado para o e-mail informado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<ComunicacaoOutDTO> buscarStatus(@RequestParam String emailDestinatario) {
        return ResponseEntity.ok(service.buscarStatusComunicacao(emailDestinatario));
    }

    @PatchMapping("/cancelar")
    @Operation(summary = "Alterar", description = "Altera status agendamentos por email")

    @ApiResponse(responseCode = "200", description = "Status do Agendamento alterado com sucesso")
    @ApiResponse(responseCode = "409", description = "Dados de envio inválidos")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")

    public ResponseEntity<ComunicacaoOutDTO> cancelarStatus(@RequestParam String emailDestinatario) {
        return ResponseEntity.ok(service.alterarStatusComunicacao(emailDestinatario));
    }

    @PostMapping("/enviar")
    @Operation(summary = "Enviar Comunicação", description = "Enviar comunicação que foi agendamentos por email")

    @ApiResponse(responseCode = "200", description = "Agendamento Enviado com sucesso")
    @ApiResponse(responseCode = "409", description = "Dados de envio inválidos")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<ComunicacaoOutDTO> enviarComunicacao(@RequestParam String emailDestinatario) {
        return ResponseEntity.ok(service.enviarComunicacao(emailDestinatario));
    }
}
