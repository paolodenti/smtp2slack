package com.github.paolodenti.smtp2slack.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.paolodenti.smtp2slack.config.AppProperties;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SlackPublisher {

    private final AppProperties appProperties;
    private final ObjectMapper objectMapper;
    private URI webhookUri;

    /**
     * Slack publisher.
     *
     * @param content the text to publish
     * @throws IOException          IOException
     * @throws InterruptedException InterruptedException
     * @throws URISyntaxException   URISyntaxException
     */
    public void publish(String content) throws IOException, InterruptedException, URISyntaxException {

        log.debug("Posting webhook");
        try {
            String slackContent = objectMapper.writeValueAsString(new SlackPayload(content));

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(getWebhookUri())
                    .POST(HttpRequest.BodyPublishers.ofString(slackContent))
                    .build();

            HttpClient httpClient = HttpClient.newHttpClient();
            httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException | URISyntaxException e) {
            log.error("Webhook POST failed", e);
            throw e;
        }
    }

    private URI getWebhookUri() throws URISyntaxException {

        if (webhookUri == null) {
            webhookUri = new URI(appProperties.getSlack().getWebhook());
        }
        return webhookUri;
    }

    private record SlackPayload(String text) {

    }
}
