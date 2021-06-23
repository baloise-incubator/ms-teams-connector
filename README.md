# ms-teams-connector
Utilize a [MessageCard](https://docs.microsoft.com/en-us/microsoftteams/platform/webhooks-and-connectors/how-to/connectors-using) 
and publish it to a MS-Teams channel via webhook.

Special thanks goes to <a href="https://github.com/luechtdiode" target="_blank"><b>Roland Seidel</b></a> for the idea and his reference implementation.

## Usage
Add dependency to your project
```xml
<dependency>
  <groupId>com.baloise.open</groupId>
  <artifactId>ms-teams-connector</artifactId>
  <version>0.1.0-SNAPSHOT</version>
</dependency>
```

Create publisher per channel
```java
final String uri = "https://teams.proxy.url/webhook/TEAM_ID/IncomingWebhook/CHANNEL_ID/WEBHOOK_ID";
final MessagePublisher channelPublisher = MessagePublisher.getInstance(uri);
```

Now you can publish any message using MessagePublisher (e.g., create and publish a simple message)
```java
final MessageCard msg = MessageCardFactory.createSimpleMessageCard("MyTitle", "MyMessage: Hello MFA-Team");
channelPublisher.publish(msg);
```

![Example](docs/img/result_example.png)

## Configuration

|Parameter|Default|Description|
|---------|-------|-----------|
|PROPERTY_RETRIES|3|Defines the number of retries to publish the message in case of unsuccessful answer from webhook. Accepts any positive integer > 0.|
|PROPERTY_RETRY_PAUSE|60|Defines the pause time between PROPERTY_RETRIES in seconds. Accepts any positive integer > 0.|
|PROPERTY_WEBHOOK_URI|none|The URI to your webhook. Required property provided either as String or URI.|

## MessageCard creation

TODO: implement