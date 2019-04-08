# Elastos.ORG.Fun

### software requirement 

- jdk1.8 or above
- maven 
- tomcat

### packaging

```sh
>$ cd Elastos.Elephant.App.Notify
>$ mvn clean package
>$ cp target/notify.war tomcat/webapp
>$ cd tomcat-location
>$ sh bin/start.sh
>$ tail -f logs/catalina.out
```

