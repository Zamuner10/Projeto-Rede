# A Rede - API de Rede Social

## Visao Geral do Projeto

O projeto A Rede consiste em uma API RESTful desenvolvida com Spring Boot para simular as operacoes essenciais de uma rede social. A aplicacao abrange desde o gerenciamento de contas de usuario, autenticacao segura com JWT e controle de privacidade de perfil, ate interacoes entre usuarios como solicitaçoes de seguidor, publicacoes, comentarios e sistema de mensagens.

Todo o backend foi construído com foco em boas praticas de arquitetura, separacao de responsabilidades em camadas (Controllers, Services, Repositories e DTOs) e cobertura robusta de testes unitarios com JUnit 5 e Mockito.

## Arquitetura e Tecnologias Utilizadas

* Java 21

* Spring Boot 4.1.1

* Spring Security e JWT (io.jsonwebtoken:jjwt 0.12.5 para autenticacao e autorizacao stateless)

* Spring Data JPA / Hibernate (mapeamento objeto-relacional e persistencia)

* RestTemplate (integracao externa para envio de e-mails transacionais)

* JUnit 5 e Mockito (frameworks de testes unitarios e criacao de objetos simulados)

* H2 / PostgreSQL (banco de dados)

## Funcionalidades da Aplicacao

### 1. Autenticacao e Seguranca

* Cadastro de novos usuarios com validacao de campos e e-mail unico.

* Login com geracao de Token JWT (versao 0.12.5) para acesso autenticado aos recursos protegidos.

* Integracao com CustomUserDetailsService para validacao dos detalhes do usuario logado.

### 2. Gerenciamento de Perfil e Privacidade

* Cada usuario possui um perfil associado contendo biografia e URL da foto.

* Suporte a perfis publicos e privados (`isPrivate`).

* Regra de visibilidade: usuarios com perfil privado so podem ter suas informacoes acessadas por seguidores cuja solicitacao foi previamente aceita (`ACCEPTED`).

* Atualizacao e conclusao de perfil com validacao de tamanho maximo de biografia (ate 50 caracteres).

### 3. Sistema de Seguidores (Follow)

* Envio de solicitacoes para seguir outros usuarios.

* Estados de solicitacao de amizade (`PENDING`, `ACCEPTED`).

* Endpoints e servicos para aceite e recusa de conexoes.

* Consultas parametrizadas para contagem de seguidores e contas seguidas.

### 4. Publicacoes e Comentarios

* Criacao de postagens vinculadas ao usuario autor.

* Sistema de comentarios em postagens existentes.

* Validacoes de existencia de usuario e postagem antes de salvar novas interacoes.

### 5. Chat e Mensagens

* Servico de mensagens capaz de verificar a existencia de conversas previas entre dois usuarios.

* Reutilizacao de salas de chat existentes ou criacao automatica de novas conversas caso seja o primeiro contato.

* Registro do historico de mensagens com remetente, destinatario e data/hora.

### 6. Servico de E-mail

* Comunicacao com servico externo via `RestTemplate` para notificacoes e e-mails de integracao.

## Testes Automatizados e Cobertura

A suíte de testes unitarios da aplicacao conta com 43 testes automatizados passando com sucesso, garantindo a integridade do codigo e o funcionamento correto das regras de negocio e excecoes.

### Detalhamento da Cobertura de Testes:

1. User e Auth (UserServiceTest, LoginServiceTest, CustomUserDetailsServiceTest):

    * Valida cadastro de novos usuarios.

    * Garante erro ao tentar cadastrar e-mail ja existente.

    * Testa autenticacao via JWT e recuperacao das credenciais do usuario.

2. Perfil e Privacidade (ProfileServiceTest):

    * Acesso liberado para perfis publicos.

    * Bloqueio de acesso a perfis privados quando o usuario nao e seguidor aceito, lancando `PrivateProfileAccessException`.

    * Permissao de acesso a perfis privados quando o relacionamento possui status `ACCEPTED`.

    * Validacao de usuario inexistente (`UserNotFoundException`).

    * Validacao de tamanho limite da biografia (maximo 50 caracteres) lancando `IllegalArgumentException`.

    * Atualizacao de bio e foto de perfil com persistencia correta.

3. Relacionamentos (FollowServiceTest):

    * Envio de solicitacao de follow e transicao de status.

    * Aceite e recusa de solicitacoes.

    * Impedimento de solicitacoes duplicadas.

4. Chat e Mensagens (ChatServiceTest):

    * Criacao de nova conversa quando nao existir historico previo.

    * Reutilizacao da conversa existente ao enviar novas mensagens.

    * Validacao da associacao entre remetente, destinatario e mensagem.

5. Conteudo e Comentarios (PostServiceTest, CommentServiceTest):

    * Publicacao de posts vinculados ao usuario correto.

    * Adicao de comentarios validando a existencia do post alvo.

6. Integracoes Externas (EmailServiceTest):

    * Simulacao de chamadas HTTP externas utilizando Mockito e `RestTemplate` para envio de notificacoes.

## Como Executar o Projeto

### Pre-requisitos

* Java 21 instalado.

* Maven configurado ou utilizacao do Maven Wrapper incluso no projeto (`./mvnw`).

### Executando a Aplicacao

1. Compile e execute o projeto atraves do comando:

```
./mvnw spring-boot:run
```

2. A API estara disponivel por padrao no endereco `http://localhost:8080`.

### Executando a Suite de Testes

Para rodar todos os 43 testes unitarios e verificar a cobertura do projeto, execute:

```
./mvnw clean test
```

(Como o projeto está em andamento o README.md pode não estar correto, mas será atualizado com o tempo !)