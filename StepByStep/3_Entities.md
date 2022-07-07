# 1. Map Java classes to DB tables

## 1.1. Add necessary dependency

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

Briefly, Spring Data JPA is an **abstraction layer on the top of Hibernate** (JPA provider by default).
It immensely **reduces** the amount of boiler plate code needed to interact with a Database.
