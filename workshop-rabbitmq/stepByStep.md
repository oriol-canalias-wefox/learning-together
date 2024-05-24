# Workshop RabbitMQ

## Graphic interfaces

Run docker explaining ports

```shell
docker run --rm --name local-rabbit -p 15672:15672 -p 5672:5672 \
-e RABBITMQ_DEFAULT_USER=admin \
-e RABBITMQ_DEFAULT_PASS=admin \
-d rabbitmq:3-management
```

- Create a queue and explain some parameters
- Create manually in the interface a direct exchange and publish manually

## Using code Part 1
- Create the beans with the queue
```kotlin
@Configuration
class DirectConfiguration {
    @Bean
    fun directExchange(): Exchange =
        ExchangeBuilder
            .directExchange("direct-exchange")
            .build()

    @Bean
    fun queue1(): Queue =
        QueueBuilder
            .durable("queue1")
            .lazy()
            .build()

    @Bean
    fun binding() : Binding =
        BindingBuilder
            .bind(queue1())
            .to(directExchange())
            .with("to-queue1")
            .noargs()
}
```
- Publish the message to an existing exchange/queue
    - see the message in rabbitmq
- publish the messate to a non-existing routing key
    - no error
- publish the messate to a non-existing exchange
    - error just in log
- explain the async publisher
- put the rabbit down and verify the publisher

- Create a consumer using `@RabbitListener`
    - Receiving Message
```kotlin
@Service
class ConsumerService {
    private val logger = LoggerFactory.getLogger(javaClass)

    @RabbitListener(queues = ["queue1"])
    fun consume(message: Message) {
        logger.info("Consuming message: {}", message)
        val body = message.body.toString(Charset.defaultCharset())
        logger.info("body: {}", body)
    }
}
```
    - Receiving String


- stop de consumer

- Publish a object message
    - see the error
    - Add the converter
```kotlin
    @Bean
    fun messageConverter(objectMapper: ObjectMapper): MessageConverter =
        Jackson2JsonMessageConverter(objectMapper)
```
- Up consumer and see the error
    - add the converter
    - consume the message

## Using code part 2

- Create the queues using RabbitAdmin
```kotlin
@Configuration
class RabbitConfiguration(private val connectionFactory: ConnectionFactory) {
    @PostConstruct
    fun postConstruct() {
        val rabbitAdmin = RabbitAdmin(connectionFactory)
        val queue2 = QueueBuilder
            .durable("queue2")
            .ttl(100_000)
            .maxLength(10L)
            .deadLetterExchange("direct-exchange")
            .deadLetterRoutingKey("to-dlq")
            .build()
        rabbitAdmin.declareQueue(queue2)
        val exchange = ExchangeBuilder
            .directExchange("direct-exchange")
            .build<Exchange>()
        rabbitAdmin.declareExchange(exchange)
        rabbitAdmin.declareBinding(BindingBuilder
            .bind(queue2)
            .to(exchange)
            .with("to-queue2")
            .noargs())
        val dlq = QueueBuilder.durable("dlq")
            .build()
        rabbitAdmin.declareQueue(dlq)
        rabbitAdmin.declareBinding(BindingBuilder
            .bind(dlq)
            .to(exchange)
            .with("to-dlq")
            .noargs())
    }
}
```
- consumer for the new queue
```kotlin
    @RabbitListener(queues = ["queue2"])
    fun consumeSecondQueue(message: Person) {
        logger.info("Consuming message: {}", message)
        if (message.age == null) {
            logger.warn("Received message without age")
            throw RuntimeException("Age cannot be null")
        }
    }
```
- Publish with null age
    - Change for `AmqpRejectAndDontRequeueException`
    - verify the DLQ

- Controlling the flow:
    - manual listener
```kotlin
@Bean
    fun listener(connectionFactory: ConnectionFactory, queueListener: QueueListener): MessageListenerContainer {
        val container = SimpleMessageListenerContainer()
        container.connectionFactory = connectionFactory
        container.setQueueNames("queue2")
        container.setMessageListener(queueListener)
        return container
    }
```

```kotlin
@Component
class QueueListener(
    private val messageConverter: MessageConverter
): MessageListener {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun onMessage(message: Message) {
        log.info("receive message from ${message.messageProperties?.consumerQueue}")
        val person: Person = messageConverter.fromMessage(message) as Person
        log.info("person $person")
        if (person.age == null) {
            throw RuntimeException("Wrong data for person class")
        }
    }
}
```
    - test and nothing change
```kotlin
    private fun retryPolicy(): Advice {
        return RetryInterceptorBuilder
            .stateless()
            .maxAttempts(5)
            .backOffOptions(
                1000, // Initial interval
                2.0, // Multiplier
                6000 // Max interval
            )
            .build()
    }

    container.setAdviceChain(retryPolicy())
```
- Test and see is not in the DLQ
    - add the recovery `.recoverer(RejectAndDontRequeueRecoverer())`
    - show `RepublishMessageRecoverer`



Advance configurations
- number of consumers
- prefetch

- Virtual hosts
- clusters
