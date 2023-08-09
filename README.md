# Smtp To Slack

Simple SMTP to Slack converter, for internal use.

It posts the content of the incoming email to a Slack webhook. 

SMTP protocol is implemented without TLS/SSL/Authentication.
Only for internal use. **Do not publicly expose it**.

## How to use it

```
WEBHOOK_URL=https://hooks.slack.com/services/<your slack webhook> \
  ./mvnw spring-boot:run
```

### Listening port

By default, the smtp listens on port 2525.

To change the listening port set the `SMTP_PORT` environment variable, e.g.

```
WEBHOOK_URL=https://hooks.slack.com/services/<your slack webhook> \
SMTP_PORT=25 \
  ./mvnw spring-boot:run
```

### Message content

By default, all the email fields (from, to, subject, content) are posted to Slack.

If you want to receive only the content, set  `SMTP_DETAILS` to `false`, e.g.

```
WEBHOOK_URL=https://hooks.slack.com/services/<your slack webhook> \
SMTP_DETAILS=false \
  ./mvnw spring-boot:run
```

### Docker image

```
docker run --rm -d \
  -e WEBHOOK_URL=https://hooks.slack.com/services/<your slack webhook> \
  -p 25:2525
  paolodenti/smtp2slack:latest
```
