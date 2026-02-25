O Vibe é o embrião de uma rede social focada em comunicação instantânea. O projeto utiliza uma arquitetura baseada em eventos para garantir que as mensagens sejam processadas de forma assíncrona e entregues em tempo real.
Tecnologias Utilizadas

    Java 21 & Spring Boot 3: Núcleo da aplicação e gerenciamento de dependências.

    PostgreSQL: Banco de dados relacional para persistência duradoura das mensagens.

    RabbitMQ: Broker de mensagens para desacoplamento e resiliência do sistema.

    WebSockets (STOMP): Protocolo para comunicação bidirecional e entrega de mensagens em tempo real no frontend/mobile.

    Docker & Docker Compose: Conteinerização de toda a infraestrutura para garantir paridade entre ambientes (Desenvolvimento e Cloud).

Funcionalidades Implementadas

    Envio de Mensagens (API REST): Endpoint POST /messages que recebe e valida o envio de novas mensagens.

    Persistência Automática: Integração com Spring Data JPA/Hibernate para salvar cada mensagem no PostgreSQL.

    Processamento Assíncrono: Uso do RabbitMQ para enfileirar mensagens, permitindo que o sistema suporte picos de carga sem travamentos.

    Monitoramento Real-Time: WebSocket configurado para "empurrar" novas mensagens para clientes conectados instantaneamente.

    Infraestrutura como Código: Arquivo docker-compose.yml que sobe o banco de dados e o broker de mensagens com um único comando.

Arquitetura do Fluxo de Dados

O fluxo de uma mensagem no Vibe segue este caminho:

    Client → Envia POST JSON para a API.

    API → Salva no Postgres e envia para o RabbitMQ.

    Consumer → Detecta a mensagem na fila e a processa.

    WebSocket → O Consumer dispara a mensagem para o tópico /topic/messages, atualizando o Navegador/App.

Como Executar

    Subir os serviços: docker compose up -d.

    Executar a aplicação: ./mvnw spring-boot:run.

    Acessar o monitor: http://localhost:8080/index.html
