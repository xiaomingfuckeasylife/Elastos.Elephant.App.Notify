# Elastos.ORG.Fun

### software requirement 

- jdk1.8 or above
- redis
- maven 
- tomcat

### packaging

```sh
>$ cd Elastos.ORG.Fun
>$ mvn clean package
>$ cp target/fun.war tomcat/webapp
>$ cd tomcat-location
>$ sh bin/start.sh
>$ tail -f logs/catalina.out
```

