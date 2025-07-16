[![CI](https://github.com/baloise-incubator/ms-teams-connector/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/baloise-incubator/ms-teams-connector/actions/workflows/ci.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=baloise-incubator_ms-teams-connector&metric=alert_status)](https://sonarcloud.io/dashboard?id=baloise-incubator_ms-teams-connector)


# ms-teams-connector

Utilize a [AdaptiveCard](https://adaptivecards.microsoft.com/?topic=AdaptiveCard) 
and publish it to a MS-Teams channel via webhook workflow.

Special thanks goes to <a href="https://github.com/luechtdiode" target="_blank"><b>Roland Seidel</b></a> for the idea and his reference implementation.

## Change Log

- Version 0.5.0
  - support blocking thread executor for batch / cli usage
  - ⚠️ __Breaking__: removed deprecated config parameter
- Version 0.4.0 is the first version supporting AdaptiveCards
- Version 0.2.3 is the last version supporting Java 8


## Usage

#### Create a webhook workflow that posts cards in teams

![MS Teams webhook workflow screenshot](docs/img/ms-teams-webhook-workflow.png)

#### Add dependency to your project

```xml
<dependency>
  <groupId>com.baloise.open</groupId>
  <artifactId>ms-teams-connector</artifactId>
  <version>0.5.0</version>
</dependency>
```

#### Create publisher per channel

```java
final String uri = "webhook_url";
final MessagePublisher channelPublisher = MessagePublisher.getInstance(uri);
```

Now you can publish any message using MessagePublisher (e.g., create and publish a simple message)
```java
final AdaptiveCard msg = AdaptiveCardFactory.createSimpleAdaptiveCard("Title", "Hello **World**");
ScheduledFuture<?> publishedFuture = channelPublisher.publish(msg);
```
![Example](docs/img/result_example.png)


#### Using MessageCardFactory builder

MessageCardFactory builder extends the "_SimpleMessageCard_" by adding facts. Facts are key-value pairs added as table below the text.

```java
AdaptiveCardFactory.builder("A crisp title", "A little more descriptive text.")
    .withFact("Status", "Failure")
    .withFact("Reason", "Out of memory")
    .build();
```

## Configuration

| Parameter            | Default | Description                                                                                                                               |
|----------------------|---------|-------------------------------------------------------------------------------------------------------------------------------------------|
| PROPERTY_RETRIES     | 3       | Defines the number of retries to publish the message in case of unsuccessful answer from webhook. Accepts any positive integer > 0.       |
| PROPERTY_RETRY_PAUSE | 60      | Defines the pause time between PROPERTY_RETRIES in seconds. Accepts any positive integer > 0.                                             |
| PROPERTY_WEBHOOK_URI | none    | The URI to your webhook. Required property provided either as String or URI.                                                              |
| PROPERTY_PROXY_URI   | none    | The URI to your web proxy.                                                                                                                |
| PROPERTY_BLOCKING    | false   | Set to ___true___ to block main thread until message was sent or max retries time is reached. Default is sending messages asynchronously. |

### System Environment

Alternatively you can also specify a system environment variable named ``https_proxy`` to connect via a proxy URL. 