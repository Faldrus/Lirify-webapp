To see the tomcat welcome page use this command inside the web docker:
`mv webapps.dist/* webapps/`

to install vi:
`apt-get update && apt-get install vim -y`

If you can't login on manager gui:
`vi webapps/manager/META-INF/context.xml`

inside the context.xml comment the row Valve:
```xml
<!--
<Valve ..... />
-->
```

----------------------------------------------
For Intellij users:
To connect the database to the intellij interface:
```
New Data Source PostreSQL:
- Host: localhost
- Port: 5432 (make ure is not used in your local machine)
- User: postgres
- Psw: postgres
- Database: nomedb
  Test Connection and enjoy
```

----------------------------------------------
Useful commands:
`docker-compose up`
`docker ps`
`docker exec -it <docker-name> bash`