# chat-backend
a chat application backend

#start 
1.use docker-compose:  run docker-compose up -d 
2.generatorkey: openssl rand -base64 756 > mongodb.key
  chmod 400 mongodb.key 
3.set mongo: env docker exec -it mongo1 bash
4.run setup.sh: ./data/setup.sh

#use docker-swarm
export DOCKER_REGISTRY=registry.cn-shanghai.aliyuncs.com/haoyuv
docker stack deploy 'chat' --compose-file=docker-compose.yml
