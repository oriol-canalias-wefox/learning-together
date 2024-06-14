# Spring cloud config

Application, two databases, different urls depends on the profile.
- local
- dev
- stg

## Create new repo 
Create a repo and add the files. 

What structure?
- project
    - profile

## Create the new project

Create new project 
- web
- cloud server

Add `@EnableConfigServer`

```yaml
spring:
  application:
    name: configuration-server
  cloud:
    config:
      server:
        git:
          uri: https://github.com/oriol-canalias-wefox/repoconfig.git
          search-paths: '{application}/{profile}'
```
## Url
- http://localhost:8080/workshop-employee/dev

## Connect with the employee

```kotlin
	implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation("org.springframework.cloud:spring-cloud-starter-config")

dependencyManagement {
  imports {
    mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
  }
}

```


```yaml
spring:
  cloud:
    config:
      uri: http://localhost:8080
      name: workshop-employee
```
first in the application.yaml
later, in the bootstrap.yml


cloud config down and restart.

```yaml
    fail-fast: true
```

Take a look to the delay, add clone on start server: 

```yaml
    clone-on-start: true
    refreshRate: 30
```

Another way using the client
```yaml
spring:
  config:
    import: configserver:http://localhost:8080
    name: ${spring.application.name}
```

## Remove for local profile

bootstrap-local.yml

```yaml
spring:
  cloud:
    config:
      enabled: false
```

## encrypt properties
```yaml
encrypt:
  key: MYFUCKINGKEY


  my-secret: '{cipher}adadadd...'
```


```shell
curl -X POST --data-urlencode 'Text to be encrypt' http://localhost:8080/encrypt
```

The project works well


See the url to show the problem:
- http://localhost:8080/workshop-employee/dev

## How to use the refresh (just with url)
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health, refresh
```

note: The refesh can happen using rabbitMQ

## AWS

```kotlin
	implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
	implementation("io.awspring.cloud:spring-cloud-starter-aws-parameter-store-config:2.4.4")
```

bootstrap
```yaml
aws:
  paramstore:
    prefix: /finops/dev
    fail-fast: true
    region: eu-central-1
    enabled: true
    profile-separator: "/"
    name: workshop-employee
```

