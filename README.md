# Chat em Tempo Real

Aplicação full-stack de chat com salas de conversa, mensagens instantâneas e lista de usuários online.

## Tecnologia

- **Backend:** Java 17 + Spring Boot 3.2 + WebSocket (STOMP)
- **Frontend:** HTML5 + CSS3 + JavaScript (SockJS + STOMP)

## Funcionalidades

- Múltiplas salas de chat
- Mensagens instantâneas via WebSocket
- Lista de usuários online por sala (atualizada em tempo real)
- Notificações de entrada e saída de usuários
- Criação de novas salas
- Interface responsiva com tema escuro

## Requisitos

- [Java 17](https://www.oracle.com/java/technologies/downloads/#java17) ou superior
- [Maven](https://maven.apache.org/download.cgi) instalado e configurado no PATH

## Como rodar

### 2. Compile e execute

```bash
mvn spring-boot:run
```

Ou compile e rode o JAR:

```bash
mvn clean package -DskipTests
java -jar target/chat-tempo-real-1.0.0.jar
```

### 3. Acesse no navegador

```
http://localhost:8081
```

> **Nota:** A aplicação roda na porta **8081** para evitar conflitos com outras aplicações.  
> Se quiser mudar a porta, edite o arquivo `src/main/resources/application.properties`.

## Como usar

1. Abra `http://localhost:8081` no navegador
2. Digite um **nome de usuário** e clique em **Entrar**
3. Escolha uma sala no menu lateral (**Geral**, **Tecnologia**, **Jogos**)
4. Digite mensagens no campo inferior e pressione **Enter** ou clique em **Enviar**
5. Para testar em tempo real, abra outra aba do navegador com outro usuário

## Criar novas salas

- Digite o nome da nova sala no campo embaixo da lista de salas
- Clique em **Criar**
- A sala será criada e você será redirecionado automaticamente

## Parar a aplicação

No terminal onde o servidor está rodando, pressione:

```
Ctrl + C
```

## Estrutura do projeto

```
Chat tempo real/
├── pom.xml                                # Configuração Maven
├── src/
│   ├── main/
│   │   ├── java/com/example/chat/
│   │   │   ├── ChatApplication.java       # Classe principal Spring Boot
│   │   │   ├── config/
│   │   │   │   └── WebSocketConfig.java   # Configuração STOMP WebSocket
│   │   │   ├── controller/
│   │   │   │   ├── ChatController.java    # Controle de mensagens WebSocket
│   │   │   │   └── RoomController.java    # API REST de salas
│   │   │   ├── model/
│   │   │   │   ├── ChatMessage.java       # Modelo de mensagem
│   │   │   │   └── ChatRoom.java          # Modelo de sala
│   │   │   └── service/
│   │   │       ├── OnlineUserService.java # Serviço de usuários online
│   │   │       └── RoomService.java       # Serviço de salas
│   │   └── resources/
│   │       ├── application.properties     # Configurações (porta 8081)
│   │       └── static/
│   │           ├── index.html             # Interface do chat
│   │           ├── style.css              # Estilos
│   │           └── app.js                 # Lógica do cliente WebSocket
```

## Endpoints

| Endpoint | Método | Descrição |
|----------|--------|-----------|
| `/api/rooms` | GET | Lista todas as salas |
| `/api/rooms?name=Nome` | POST | Cria uma nova sala |
| `/ws` | WebSocket | Conexão SockJS para mensagens em tempo real |

