package com.github.paolodenti.smtp2slack.service;

import com.github.paolodenti.smtp2slack.config.AppProperties;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.subethamail.smtp.helper.SimpleMessageListener;

@Service
@Slf4j
@RequiredArgsConstructor
public class Smtp2SlackListener implements SimpleMessageListener {

    private final AppProperties appProperties;
    private final SlackPublisher slackPublisher;

    @Override
    public boolean accept(String from, String recipient) {

        return true;
    }

    @Override
    public void deliver(String from, String recipient, InputStream data) {

        log.debug("from={}, recipient={}", from, recipient);
        try {
            MimeMessage message = convertToMimeMessage(data);

            if (appProperties.getSmtp().isDetails()) {
                String detailMessage =
                        """
                                From: %s:
                                To: %s
                                Subject: %s
                                -------------------------
                                %s"""
                                .formatted(from, recipient, message.getSubject(), message.getContent().toString());

                slackPublisher.publish(detailMessage);
            } else {
                slackPublisher.publish(message.getContent().toString());
            }
        } catch (MessagingException | IOException | URISyntaxException e) {
            log.error("Error while converting to MimeMessage, dropping the message", e);
        } catch (InterruptedException e) {
            log.error("Interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Converter from smtp inputStream to a mime message.
     *
     * @param data smtp inputStream
     * @return mimeMessage
     * @throws MessagingException MessagingException
     */
    public MimeMessage convertToMimeMessage(InputStream data) throws MessagingException {

        Session session = Session.getDefaultInstance(new Properties());
        try {
            return new MimeMessage(session, data);
        } catch (MessagingException e) {
            throw new MessagingException();
        }
    }
}
