# Redis workshop

- explain redis

## Run in local

Or we can start them in docker:
```shell
docker run --name local-redis -p 6379:6379 -d redis
```

To run CLI we can do:
```shell
docker exec -it local-redis redis-cli
```

## Run basic commands

### SET, GET, KEYS

```shell
SET "last_user" "oriol.canalias"
```

```shell
GET "last_user"
```

```shell
KEYS *
```

```shell
SET "first_user" "uri"
SET "other_key" "other value"
KEYS "*user"
```

### Expirations and conditionals

The key pattern is with `:` separating words
Expiration is in seconds 

```shell
SET "some:words:here" "Wellcome with expiration" EX 20
TTL "some:words:here"
EXPIRE "some:words:here" 40
```
> note: PX for milliseconds

Atomic executions, so just store if doesn't exist yet:
```shell
SET "first:user" "oriol.canalias" NX
SET "first:user" "other user" NX
GET "fisrt:user"
```

Store only if exists:
```shell
SET "user:info:uri" "myinfo" XX
SET "user:info:uri" "myinfo" EX 500
SET "user:info:uri" "updateinfo1" XX
GET "user:info:uri"
TTL "user:info:uri"
```
How to keep the TTL
```shell
SET "user:info:uri" "updateinfo2" XX EX 500
TTL "user:info:uri"
SET "user:info:uri" "updateinfo2" KEEPTTL XX
SET "user:info:uri"
```

## Spring boot and cache
```kotlin
	implementation("org.springframework.boot:spring-boot-starter-cache")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
```

```yaml
  cache:
    type: redis
```

```yaml
  redis:
    image: redis
    ports:
      - 6301:6379
```
```java
@EnableCaching
```

Add `Serializable`
See the TTL
set:
```yaml
  cache:
    type: redis
    redis:
      time-to-live: PT60S
      key-prefix: "${spring.application.name}:"
```

show monitor


---
## Pending

Auth and Database

Lua scripts

cluster



Alternatives:
https://github.com/microsoft/garnet
https://github.com/valkey-io/valkey
https://www.dragonflydb.io/
