# API Service

API Gateway RESTful baseado em Spring Boot que consome o microserviço People via gRPC e expõe endpoints HTTP. O projeto foi desenvolvido seguindo princípios de Clean Architecture com comunicação reativa.

## 📋 Índice

- [Visão Geral](#-visão-geral)
- [Arquitetura](#-arquitetura)
- [Tecnologias](#-tecnologias)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Pré-requisitos](#-pré-requisitos)
- [Configuração](#-configuração)
- [Como Executar](#-como-executar)
- [API REST](#-api-rest)
- [Detalhes Técnicos](#-detalhes-técnicos)
- [Build e Deploy](#-build-e-deploy)

## 🎯 Visão Geral

O **API Service** é um microserviço que atua como API Gateway, expondo endpoints REST para acesso às informações de pessoas. Internamente, consome o microserviço People via gRPC, aplicando os conceitos de Clean Architecture e programação reativa.

### Funcionalidades Principais

- **Buscar pessoa por ID**: Endpoint REST que consulta pessoa específica via gRPC
- **Listar todas as pessoas**: Endpoint REST que lista todas as pessoas via gRPC
- **Gateway Pattern**: Abstrai a complexidade da comunicação gRPC para clientes HTTP
- **Comunicação reativa**: Utiliza WebFlux para processamento não-bloqueante
- **Mapeamento type-safe**: MapStruct para conversão entre gRPC Proto e DTOs
- **Interface REST**: API HTTP simples e padronizada
- **Clean Architecture**: Separação clara de responsabilidades em camadas

## 🏗 Arquitetura

O projeto segue os princípios da **Clean Architecture**, organizando o código em camadas bem definidas:

```
┌─────────────────────────────────────────┐
│    Presentation (REST Controller)       │
│   - PeopleControllerImpl                │
└─────────────┬───────────────────────────┘
              │
┌─────────────▼───────────────────────────┐
│       Application Layer                 │
│   - PeopleService (interface)           │
│   - PeopleServiceImpl                   │
│   - PeopleResponse (DTO)                │
└─────────────┬───────────────────────────┘
              │
┌─────────────▼───────────────────────────┐
│       Domain Layer                      │
│   - PeopleServiceClient (Interface)     │
│   - PeopleRepository (Interface)        │
└─────────────┬───────────────────────────┘
              │
┌─────────────▼───────────────────────────┐
│      Infrastructure Layer               │
│   Repository:                           │
│   - PeopleRepositoryImpl                │
│                                         │
│   Client:                               │
│   - PeopleServiceGrpcClientImpl         │
│   - PeopleGrpcMapper (MapStruct)        │
│                                         │
│   Config:                               │
│   - RepositoryConfig                    │
└─────────────────────────────────────────┘
              │
         [gRPC Client]
              │
              ▼
      People Microservice
         (gRPC Server)
```

### Camadas

1. **Presentation** (`org.api.presentation`)
   - Controladores REST que expõem endpoints HTTP
   - Recebem requisições HTTP e retornam respostas JSON
   - Injetam interfaces de serviço (`PeopleService`)

2. **Application** (`org.api.application`)
   - Lógica de negócio da aplicação
   - DTOs (`PeopleResponse`)
   - Services (`PeopleService`, `PeopleServiceImpl`)
   - Orquestra as interações entre presentation e domain

3. **Domain** (`org.api.domain`)
   - Interfaces de cliente (`PeopleServiceClient`)
   - Interfaces de repositório (`PeopleRepository`)
   - Livre de dependências externas e frameworks

4. **Infrastructure** (`org.api.infrastructure`)
   - **Client**: Implementação do cliente gRPC
     - `PeopleServiceGrpcClientImpl`: Comunicação com People service
     - `PeopleGrpcMapper`: Conversão Proto ↔ DTO via MapStruct
   - **Repository**: Implementação do repositório
     - `PeopleRepositoryImpl`: Delega para o cliente gRPC
   - **Config**: Configurações e beans do Spring
     - `RepositoryConfig`: Registro do bean de repositório

## 🚀 Tecnologias

### Core
- **Java 21**: Versão LTS mais recente com recursos modernos
- **Spring Boot 3.3.3**: Framework principal para desenvolvimento
- **Maven**: Gerenciamento de dependências e build

### Comunicação
- **Spring WebFlux**: Framework reativo para REST API
- **gRPC 1.58.0**: Cliente gRPC para comunicação com People service
- **Protocol Buffers 3.24.3**: Serialização de dados
- **gRPC Spring Boot Starter 2.15.0**: Integração Spring + gRPC

### Utilitários
- **Lombok**: Redução de boilerplate code
- **MapStruct 1.5.5**: Mapeamento automático entre Proto e DTOs
- **Project Reactor**: Implementação do Reactive Streams
  - `Mono<T>`: Para operações que retornam 0 ou 1 elemento
  - `Flux<T>`: Para operações que retornam 0 a N elementos

## 📁 Estrutura do Projeto

```
api/
├── src/
│   ├── main/
│   │   ├── java/org/api/
│   │   │   ├── ApiApplication.java
│   │   │   │
│   │   │   ├── domain/
│   │   │   │   ├── client/
│   │   │   │   │   └── PeopleServiceClient.java    # Interface do cliente gRPC
│   │   │   │   └── repository/
│   │   │   │       └── PeopleRepository.java       # Interface do repositório
│   │   │   │
│   │   │   ├── application/
│   │   │   │   ├── dto/
│   │   │   │   │   └── PeopleResponse.java         # DTO de resposta
│   │   │   │   └── service/
│   │   │   │       ├── PeopleService.java          # Interface do serviço
│   │   │   │       └── PeopleServiceImpl.java      # Implementação do serviço
│   │   │   │
│   │   │   ├── presentation/
│   │   │   │   └── controller/
│   │   │   │       └── PeopleControllerImpl.java   # Controller REST
│   │   │   │
│   │   │   └── infrastructure/
│   │   │       ├── client/
│   │   │       │   ├── PeopleServiceGrpcClientImpl.java  # Cliente gRPC
│   │   │       │   └── PeopleGrpcMapper.java             # Mapper MapStruct
│   │   │       │
│   │   │       ├── repository/
│   │   │       │   └── PeopleRepositoryImpl.java   # Implementação do repositório
│   │   │       │
│   │   │       └── config/
│   │   │           └── RepositoryConfig.java       # Config do repository
│   │   │
│   │   ├── proto/
│   │   │   └── person.proto                        # Definição do serviço gRPC
│   │   │
│   │   └── resources/
│   │       └── application.yml                     # Configurações da aplicação
│   │
│   └── test/
│       └── java/org/api/
│           └── ApiApplicationTests.java
│
├── target/                                         # Arquivos compilados
├── pom.xml                                         # Configuração Maven
├── mvnw                                            # Maven Wrapper (Unix)
└── mvnw.cmd                                        # Maven Wrapper (Windows)
```

## 📋 Pré-requisitos

- **Java Development Kit (JDK) 21** ou superior
- **Maven 3.6+** (ou utilize o Maven Wrapper incluído no projeto)
- **Git** (para clonar o repositório)
- **People Microservice** rodando na porta 9090

### Verificar Instalações

```bash
# Verificar versão do Java
java -version

# Verificar versão do Maven
mvn -version
```

## ⚙ Configuração

### Arquivo application.yml

```yaml
server:
  port: 8081

spring:
  application:
    name: api

grpc:
  client:
    people-service:
      address: 'static://127.0.0.1:9090'
      negotiationType: PLAINTEXT
      enable-keep-alive: true
      keep-alive-without-calls: true
      max-inbound-message-size: 4194304 # 4 MB
```

### Configuração do Cliente gRPC

A aplicação se conecta ao microserviço People na porta 9090. Para alterar o endereço:

```yaml
grpc:
  client:
    people-service:
      address: 'static://localhost:9090'  # Endereço local
      # ou
      address: 'static://people-service:9090'  # Via DNS/Service Discovery
```

Ou defina via variável de ambiente:

```bash
export GRPC_CLIENT_PEOPLE_SERVICE_ADDRESS=static://localhost:9090
```

## 🏃 Como Executar

### 1. Iniciar o People Microservice

Primeiro, certifique-se de que o microserviço People está rodando na porta 9090:

```bash
cd ../people
./mvnw spring-boot:run
```

### 2. Executar o API Service

#### Windows
```cmd
# Limpar e compilar o projeto
.\mvnw.cmd clean install

# Executar a aplicação
.\mvnw.cmd spring-boot:run
```

#### Unix/Linux/MacOS
```bash
# Limpar e compilar o projeto
./mvnw clean install

# Executar a aplicação
./mvnw spring-boot:run
```

### Usando Maven Local

```bash
# Limpar e compilar
mvn clean install

# Executar
mvn spring-boot:run
```

### Executando o JAR Gerado

```bash
# Gerar o JAR
mvn clean package

# Executar o JAR
java -jar target/api-0.0.1-SNAPSHOT.jar
```

### Verificar Execução

Após iniciar a aplicação, você verá logs indicando que o servidor HTTP está rodando:

```
Tomcat started on port(s): 8081 (http) with context path ''
Started ApiApplication in X.XXX seconds
```

## 🔌 API REST

### Endpoints Disponíveis

#### 1. GET /api/peoples/{id}
Busca uma pessoa específica por ID.

**Exemplo de Uso:**
```bash
curl http://localhost:8081/api/peoples/1
```

**Resposta Esperada:**
```json
{
  "id": 1,
  "name": "Leanne Graham",
  "email": "Sincere@april.biz"
}
```

#### 2. GET /api/peoples
Lista todas as pessoas disponíveis.

**Exemplo de Uso:**
```bash
curl http://localhost:8081/api/peoples
```

**Resposta Esperada:**
```json
[
  {
    "id": 1,
    "name": "Leanne Graham",
    "email": "Sincere@april.biz"
  },
  {
    "id": 2,
    "name": "Ervin Howell",
    "email": "Shanna@melissa.tv"
  }
]
```

### Testando com curl

```bash
# Buscar pessoa por ID
curl -X GET http://localhost:8081/api/peoples/1

# Listar todas as pessoas
curl -X GET http://localhost:8081/api/peoples

# Com formatação JSON (usando jq)
curl -s http://localhost:8081/api/peoples | jq .
```

## 🔧 Detalhes Técnicos

### Padrão Service

O projeto utiliza **PeopleService** como camada de aplicação:

```java
@Service
@RequiredArgsConstructor
public class PeopleServiceImpl implements PeopleService {

    private final PeopleRepository peopleRepository;

    @Override
    public Mono<PeopleResponse> getById(int id) {
        return peopleRepository.findById(id);
    }

    @Override
    public Flux<PeopleResponse> listAll() {
        return peopleRepository.findAll();
    }
}
```

### Padrão Repository

O repositório delega para o cliente gRPC:

```java
@RequiredArgsConstructor
public class PeopleRepositoryImpl implements PeopleRepository {
    private final PeopleServiceClient peopleClient;

    @Override
    public Mono<PeopleResponse> findById(int id) {
        return peopleClient.getPeopleById(id);
    }

    @Override
    public Flux<PeopleResponse> findAll() {
        return peopleClient.listPeople();
    }
}
```

### Inversão de Dependência

O Controller injeta a interface do serviço, não a implementação:

```java
@RestController
@RequestMapping("/api/peoples")
@RequiredArgsConstructor
public class PeopleControllerImpl {

    private final PeopleService peopleService;  // Interface!

    @GetMapping("/{id}")
    public Mono<PeopleResponse> getPeopleById(@PathVariable int id) {
        return peopleService.getById(id);
    }

    @GetMapping
    public Flux<PeopleResponse> listPeople() {
        return peopleService.listAll();
    }
}
```

### Cliente gRPC Reativo

O cliente gRPC converte chamadas síncronas em fluxos reativos:

```java
@Component
@RequiredArgsConstructor
public class PeopleServiceGrpcClientImpl implements PeopleServiceClient {

    @GrpcClient("people-service")
    private PeopleServiceGrpc.PeopleServiceBlockingStub peopleServiceStub;

    private final PeopleGrpcMapper peopleGrpcMapper;

    @Override
    public Mono<PeopleResponse> getPeopleById(int id) {
        return Mono.fromCallable(() -> {
            ServiceProto.PeopleRequestGrpc request = ServiceProto.PeopleRequestGrpc.newBuilder()
                    .setId(id)
                    .build();

            ServiceProto.PeopleResponseGrpc response = peopleServiceStub.getPeople(request);

            return peopleGrpcMapper.toPeopleResponse(response);
        })
        .subscribeOn(Schedulers.boundedElastic());
    }
}
```

**Detalhes importantes:**
- `Mono.fromCallable()`: Converte chamada síncrona em `Mono`
- `subscribeOn(Schedulers.boundedElastic())`: Executa em thread pool elástico para I/O bloqueante
- `peopleGrpcMapper`: MapStruct mapper para conversão type-safe

### MapStruct

O MapStruct é utilizado para conversão type-safe entre gRPC Proto e DTOs:

```java
@Mapper(
    componentModel = "spring",
    implementationName = "PeopleGrpcMapperImpl"
)
public interface PeopleGrpcMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    PeopleResponse toPeopleResponse(ServiceProto.PeopleResponseGrpc response);
}
```

**Benefícios do MapStruct:**
- Conversão type-safe em tempo de compilação
- Código gerado otimizado
- Integração perfeita com Spring via `componentModel = "spring"`
- Reduz erros de mapeamento manual

### Lombok

Construtores são gerados automaticamente via Lombok:

```java
@Service
@RequiredArgsConstructor  // Gera construtor com campos final
public class PeopleServiceImpl implements PeopleService {
    private final PeopleRepository peopleRepository;
}
```

### Configuração de Beans

O `RepositoryConfig` registra manualmente o bean de repositório:

```java
@Configuration
public class RepositoryConfig {

    @Bean
    public PeopleRepository peopleRepository(PeopleServiceClient peopleClient) {
        return new PeopleRepositoryImpl(peopleClient);
    }
}
```

**Nota:** O serviço é registrado automaticamente via `@Service`, então não precisa de configuração manual.

## 📦 Build e Deploy

### Gerar Artefato de Produção

```bash
mvn clean package -DskipTests
```

### Docker (Exemplo)

```dockerfile
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY target/api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

ENV GRPC_CLIENT_PEOPLE_SERVICE_ADDRESS=static://people-service:9090
ENV SPRING_PROFILE=prod

ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Build e Run:**
```bash
docker build -t api-service:latest .
docker run -p 8081:8081 api-service:latest
```

### Docker Compose (API + People)

```yaml
version: '3.8'

services:
  people:
    build: ../people
    ports:
      - "9090:9090"
    environment:
      - ACTIVE_DATASOURCE=TYPICODE

  api:
    build: .
    ports:
      - "8081:8081"
    environment:
      - GRPC_CLIENT_PEOPLE_SERVICE_ADDRESS=static://people:9090
    depends_on:
      - people
```

**Executar:**
```bash
docker-compose up
```

## 🔄 Fluxo de Requisição

```
Cliente HTTP
    │
    │ GET /api/peoples/1
    ▼
PeopleControllerImpl
    │
    │ peopleService.getById(1)
    ▼
PeopleServiceImpl
    │
    │ peopleRepository.findById(1)
    ▼
PeopleRepositoryImpl
    │
    │ peopleClient.getPeopleById(1)
    ▼
PeopleServiceGrpcClientImpl
    │
    │ Mono.fromCallable()
    │ peopleServiceStub.getPeople(request)
    │ peopleGrpcMapper.toPeopleResponse(response)
    ▼
People Microservice (gRPC)
    │
    │ gRPC Response
    ▼
Mapper (Proto → DTO)
    │
    │ PeopleResponse
    ▼
Cliente HTTP (JSON Response)
```

## 📝 Licença

Este projeto é um exemplo educacional e está disponível para uso livre.

---

**Desenvolvido com Java 21 + Spring Boot + WebFlux + gRPC + MapStruct + Lombok**
