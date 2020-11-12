# chat-backend
a chat application backend

#docker-compose 支持env_file docker swarm不支持 docker swarm 需要配置对应的环境变量
docker swarm 不会自动创建对应的volume 可以使用docker-compose先拉起对应的服务或者手动创建volume文件夹

#start 
1.use docker-compose:  run docker-compose up -d 
2.generatorkey: openssl rand -base64 756 > mongodb.key
  chmod 400 mongodb.key 
3.set mongo: env docker exec -it mongo1 bash
4.run setup.sh: ./data/setup.sh

#use docker-swarm
export DOCKER_REGISTRY=registry.cn-shanghai.aliyuncs.com/haoyuv
docker stack deploy 'chat' --compose-file=docker-compose.yml