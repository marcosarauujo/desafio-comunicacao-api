# 📬 Comunicação API

API REST desenvolvida em **Java** e **Spring Boot** para gerenciamento, agendamento e envio de notificações multicanal (E-mail, SMS, Push, WhatsApp), com integração externa de microsserviços via **OpenFeign**, persistência em banco relacional **MySQL** e conteinerização completa com **Docker**.

Projeto desenvolvido como parte do **Desafio Técnico: Melhorias em um Projeto Existente**, evoluindo a arquitetura com boas práticas de mercado, documentação interativa, testes unitários e containerização.

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 17
- **Framework:** Spring Boot 2.7.0
- **Persistência & Banco:** Spring Data JPA, Hibernate, MySQL 8.0
- **Integração Externa:** Spring Cloud OpenFeign
- **Mapeamento de Objetos:** MapStruct 1.5.5
- **Documentação de API:** SpringDoc OpenAPI 3 (Swagger UI)
- **Containerização:** Docker & Docker Compose
- **Testes Unitários:** JUnit 5, Mockito, Spring MVC Test (MockMvc), Hamcrest
- **Produtividade:** Lombok

---

## 📌 Endpoints da Aplicação

### 1. Agendar Comunicação
- **POST /comunicacao/agendar**: Agenda uma nova comunicação com status inicial `PENDENTE`.
  O DTO deve ser preenchido com os seguintes dados:
  ```json
  {
    "dataHoraEnvio": "2026-11-09 15:00:00",
    "nomeDestinatario": "Marcos Araujo",
    "emailDestinatario": "marcos@email.com",
    "telefoneDestinatario": "11999999999",
    "mensagem": "Sua mensagem aqui",
    "modoDeEnvio": "EMAIL"
  }
  ```

### 2. Consultar Status
- **GET /comunicacao?emailDestinatario=exemplo@email.com**: Busca a comunicação e seu status atual através do e-mail do destinatário.

### 3. Cancelar Comunicação
- **PATCH /comunicacao/cancelar?emailDestinatario=exemplo@email.com**: Localiza a comunicação pelo e-mail do destinatário e altera seu status para `CANCELADO`.

### 4. Enviar Comunicação
- **POST /comunicacao/enviar?emailDestinatario=exemplo@email.com**: Envia a comunicação por e-mail integrando com a API externa de notificação e alterando o status para `ENVIADO`.

---

## 📖 Documentação Swagger (OpenAPI)

Com a aplicação em execução, acesse a documentação interativa dos endpoints em:  
👉 `http://localhost:8080/swagger-ui/index.html`

---

## 🧪 Testes Automatizados

O projeto conta com uma suíte abrangente de testes unitários cobrindo todas as camadas da aplicação:

- **Camada de Mapper (`ComunicacaoMapperTest`):** Validação bidirecional das conversões entre Entity e DTOs via MapStruct.
- **Camada de Negócio (`ComunicacaoServiceTest`):** 10 testes cobrindo todos os fluxos de sucesso e cenários de exceção (`BusinessException`, `ResourceNotFoundException`), com simulação de falhas de banco de dados, falhas de API externa e desacoplamento de relógio via injeção de `Clock` para testes determinísticos.
- **Camada Web (`ComunicacaoControllerTest`):** Validação de rotas, códigos de status HTTP (200, 400 Bad Request) e serialização JSON utilizando `MockMvc` em modo standalone.

### 🏃 Como executar os testes:
No terminal, execute:
```bash
mvn test
```

---

## 🐳 Como Rodar com Docker

Para facilitar e não precisar instalar o MySQL na sua máquina, a aplicação e o banco sobem juntos pelo **Docker Compose**.

### 💡 Ajustes e Decisões Técnicas:
- **MySQL no Docker:** O banco roda na porta `3306` em um container próprio com volume persistente.
- **Imagem Java (`eclipse-temurin:17-jre-alpine`):** Usada no `Dockerfile` por ser mais leve e atualizada, já que a imagem padrão do OpenJDK foi descontinuada.
- **Docker Compose:** Conecta a API e o banco na mesma rede interna de forma automática.

---

### 🚀 Passo a Passo para Executar:

1. **Gere o arquivo `.jar` do projeto:**
   ```bash
   mvn clean package -DskipTests
   ```

2. **Suba os containers (API + Banco):**
   ```bash
   docker compose up -d
   ```

3. **Acesse o Swagger no navegador:**  
   `http://localhost:8080/swagger-ui/index.html`

4. **Para parar tudo:**
   ```bash
   docker compose down
   ```

---

## ✒️ Autor

Desenvolvido por **Marcos Araújo**.
