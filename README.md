## Endpoints da Aplicação

- **POST /comunicacao/agendar**: Agenda uma nova comunicação com status inicial PENDENTE.
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

- POST /comunicacao/enviar?emailDestinatario=exemplo@email.com: Envia a comunicação por e-mail integrando com a API externa de notificação e alterando o status para ENVIADO.


- **GET /comunicacao?emailDestinatario=exemplo@email.com**: Busca a comunicação e seu status atual através do e-mail do destinatário.


- **PATCH /comunicacao/cancelar?emailDestinatario=exemplo@email.com**: Localiza a comunicação pelo e-mail do destinatário e altera seu status para CANCELADO.

## Documentação Swagger (OpenAPI)

Com a aplicação em execução, acesse a documentação interativa dos endpoints em:
`http://localhost:8080/swagger-ui/index.html`


## 🐳 Como Rodar com Docker

Para facilitar e não precisar instalar o MySQL na sua máquina, a aplicação e o banco sobem juntos pelo **Docker Compose**.

### 💡 Ajustes e Decisões Técnicas:
- **MySQL no Docker:** O banco roda na porta `3306` em um container próprio.
- **Imagem Java (`eclipse-temurin:11-jre-alpine`):** Usada no `Dockerfile` por ser mais leve e atualizada, já que a imagem padrão do OpenJDK foi descontinuada.
- **Docker Compose:** Conecta a API e o banco na mesma rede interna de forma automática.

---

### 🚀 Passo a Passo para Executar:

1. **Gere o arquivo `.jar` do projeto:**
   ```bash
   mvn clean package
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