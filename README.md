# FullCycle - Prático JAVA
## Proejto Codeflix

Aplicação desenvolvida em acompanhamento do curso da FullCycle de arquitetura exagonal. Microserviço
de administração de um catálogo de vídeos.

## Tecnologias utilizadas

- JDK 17
- IntelliJ Community Edition
- Docker
- Gradle 8.4
- SpringBoot 3.2.2
- Spring JPA
- Spring AMPQ
- Spring Security
- RabbitMQ
- Keycloak
- Base de dados MySQL e H2
- Flyway 10.15.0
- JUnit 5
- Mockito
- Jacoco
- Middleware: Undertow

## Clonar o repositório:
```sh
git clone https://github.com/landuccilzd/fullcycle-pratico-java.git
```

## Subir os serviços de banco de dados MySQL, RabbitMQ e Keycloak com Docker:
A partir da raiz da aplicação
```shell
cd sandbox/services
docker compose up -d
```

## Subir os serviços de observabilidade - Elasticsearch, Kibana e Logstash
A partir da raiz da aplicação
```shell
cd sandbox/elk
docker compose up -d
```

## Subir a aplicação e Filebeat
A partir da raiz da aplicação
```shell
cd sandbox/app
docker compose up -d
```

## Executar as migrações do MySQL com o Flyway:
A partir da raiz da aplicação
```shell
./gradlew flywayMigrate
```
#### Limpar as migrações do banco
É possível limpar (deletar todas as tabelas) seu banco de dados, basta
executar o seguinte comando:
```shell
./gradlew flywayClean
```

#### Reparando as migrações do banco
Existe duas maneiras de gerar uma inconsistência no Flyway deixando ele no estado de 
reparação:
1. Algum arquivo SQL de migração com erro;
2. Algum arquivo de migração já aplicado foi alterado (modificando o `checksum`).

Quando isso acontecer o flyway ficará em um estado de reparação com um registro na 
tabela `flyway_schema_history` com erro (`sucesso = 0`).

Para executar a reparação, corrija os arquivos e execute:
```shell
./gradlew flywayRepair
```

Com o comando acima o Flyway limpará os registros com erro da tabela `flyway_schema_history`,
na sequência execute o comando FlywayMigrate para tentar migrar-los novamente.

#### Outros comandos úteis do Flyway

Além dos comandos já exibidos, temos alguns outros muito úteis como o info e o validate:

```shell
./gradlew flywayInfo
./gradlew flywayValidate
```
Para saber todos os comandos disponíveis: [Flyway Gradle Plugin](https://flywaydb.org/documentation/usage/gradle/info)
