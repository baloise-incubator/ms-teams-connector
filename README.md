# ms-teams-connector
Utilize a [MessageCard](https://docs.microsoft.com/en-us/microsoftteams/platform/webhooks-and-connectors/how-to/connectors-using) 
and publish it to a MS-Teams channel via webhook.

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
final Map<String, Object> configMap = Collections.singletonMap(MessagePublisher.PROPERTY_WEBHOOK_URI, uri);
final MessagePublisher channelPublisher = MessagePublisher.getInstance(configMap);
```

Now you can publish Message via publisher (e.g., using simple Message)
```java
final MessageCard msg = MessageCardFactory.createSimpleMessageCard("MyTitle", "MyMessage: Hello MFA-Team");
channelPublisher.publish(msg);
```

![Example](docs/img/result_example.png)

## Config

TODO: implement

## MessageCard creation

TODO: implement