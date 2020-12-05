echo "**********Mongo replSet init...************"
mongo -u admin -p 123456 << EOF
rs.initiate({
    _id: "mongos",
    members: [
        { _id : 0, host : "mongodb1:27017" },
        { _id : 1, host : "mongodb2:27017" },
        { _id : 2, host : "mongodb3:27017" }
    ]
});
use admin
db.createUser({user: 'root', pwd: '123456', roles: [{role: 'userAdminAnyDatabase', db: 'admin'}]});
use realtime
db.createUser({user: 'chat', pwd: '123456', roles:[{role:'readWrite',db:'chat'}]})
EOF
echo "************Mongo users created************"