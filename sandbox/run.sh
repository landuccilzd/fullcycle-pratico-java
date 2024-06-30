# Criar as docker networks
docker network create codeflix_services
docker network create codeflix_elk

# Criar as pastas com permissões
sudo chown root app/filebeat/filebeat.docker.yml
mkdir -m 777 .docker
mkdir -m 777 .docker/elasticsearch
mkdir -m 777 .docker/keycloak
mkdir -m 777 .docker/filebeat

docker compose -f services/docker-compose.yml up -d
docker compose -f elk/docker-compose.yml up -d
#docker compose -f app/docker-compose.yml up -d

echo "Inicializando os containers..."
sleep 20