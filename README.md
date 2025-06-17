# Desafio técnico Itaú

## Executando localmente

- Clone o projeto na sua máquina
- Suba os containers através do comando

```shell
docker compose up -d
```


*RabbitMQ* já configura exchanges e queues quando o container sobe. Abaixo digo o que cada queue representa.

*PAYMENT_QUEUE*: é uma queue que backend consome para processar as solicitações e se elas devem ser aprovadas ou reprovadas.
A queue aceita mensagem no formato JSON com seguinte body

```json 
{
  "policyIdExternal": "00000000-0000-0000-0000-000000000000", // UUID
  "paymentPhase": "CONFIRMED" // [IN_PROGRESS, CONFIRMED, REJECTED]
}

```

*SUBSCRIPTION_AUTHORIZATION_QUEUE*: é uma queue que backend consome para processar as solicitações e se elas devem ser aprovadas ou reprovadas.
A queue aceita mensagem no formato JSON com seguinte body

```json 
{
  "policyIdExternal": "00000000-0000-0000-0000-000000000000", // UUID
  "subscriptionAuthorizationPhase": "CONFIRMED" // [IN_PROGRESS, CONFIRMED, REJECTED]
}

```

*POLICY_QUEUE*: é usada para backend produzir mensagens.




*API* vai está pronta no *localhost* na porta *8080*

## Tecnologias

### Principais Tecnologias
- Java 17
- Maven
- PostgreSQL
- RabbitMQ

### Principais Frameworks
- Spring Boot
- Spring Data JPA
- Spring Schedules
- Spring OpenFeign
- Hibernate ORM
- Flyway Migration
- JUnit5
- Mockito
- MockServer

## Padrões utilizados

- A nomenclatura padrão do projeto é toda em inglês
- Design patterns implementados Strategy, Builder

## Arquitetura do projeto

<b>*🐘 PostgreSQL*</b>:  Foi utilizado este banco de dados para ter um schema fixo e operações transacionais ACID.

<b>*🐇 RabbitMQ*</b>: Foi utilizado para maior segurança para quem envia e quem recebe as mensagens.

### consumer
É aonde fica DTOs e classes responsáveis por consumir mensagens de mensagerias

### controller
É aonde deve ficar os controladores para receber requisições HTTP

### controlleradvice

É aonde fica os tratamentos de exceções

### dto

É aonde fica os DTOs para fins de transferência de dados

### exception

É aonde fica as classes de exceções

### feign

É aonde fica os serviços para comunicação para outras APIs


### filter

É aonde fica os filters para conseguir adicionar filtros principalmente em consultas

### model

É aonde fica os modelos representando as tabelas do banco de dados

### producer

É aonde fica as classes responsável por produzir mensagens para alguma mensageria

### repository

É a camada responsável por se comunicar com algum armazenamento de dados (Banco de dados relacional, não relacional, em memória, etc...)

### schedule

Responsável por executar jobs

### service

Responsável por fazer chamada e tratamento para outros sistemas

### settings

Configurações sobre o servidor

### usecase

Camada responsável por conter casos de uso de funcionalidades

### util

Camada utilitária para métodos comumente usados

### validator

Camada responsável por manter validações de objetos

## Melhorias futuras

- Simplificar implementações de tipos de cliente (regular, alto risco, preferencial, sem informação, etc...) 
- Evitando race condition nas solicitações de apólice.

## Exemplo para criar uma nova solicitação de apólice

POST http://localhost:8080/policies

```bash 
curl -X 'POST' \
  'http://localhost:8080/policies' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "category": "LIFE",
  "salesChannel": "WEBSITE",
  "paymentMethod": "PIX",
  "coverages": [
    {
      "title": "Roubo",
      "amount": 1000.5
    },
    {
      "title": "Perda total",
      "amount": 10000.25
    }
  ],
  "assistances": [
    "Guincho até 250km",
    "Troca de óleo",
    "Chaveiro 24h"
  ],
  "customer_id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "product_id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "total_monthly_premium_amount": 75000,
  "insured_amount": 80000
}'
```
## JSON 
```json
{
  "category": "LIFE",
  "salesChannel": "WEBSITE",
  "paymentMethod": "PIX",
  "coverages": [
    {
      "title": "Roubo",
      "amount": 1000.5
    },
    {
      "title": "Perda total",
      "amount": 10000.25
    }
  ],
  "assistances": [
    "Guincho até 250km",
    "Troca de óleo",
    "Chaveiro 24h"
  ],
  "customer_id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "product_id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "total_monthly_premium_amount": 75000,
  "insured_amount": 80000
}
```

## Exemplo para cancelar uma solicitação de apólice

PATCH http://localhost:8080/policies

```bash
curl -X 'PATCH' \
  'http://localhost:8080/policies/cabe4ea5-eca5-4ae2-82af-f3558c1ed260' \
  -H 'accept: application/json'
```

## Exemplo para consultar uma solicitação apólice

GET http://localhost:8080/policies

```bash
curl -X 'GET' \
  'http://localhost:8080/policies?idExternal=cabe4ea5-eca5-4ae2-82af-f3558c1ed260' \
  -H 'accept: application/json'
```