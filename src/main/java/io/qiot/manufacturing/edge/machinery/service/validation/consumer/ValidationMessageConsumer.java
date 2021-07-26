package io.qiot.manufacturing.edge.machinery.service.validation.consumer;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;

import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.qiot.manufacturing.commons.domain.productionvalidation.ValidationResponseDTO;
import io.qiot.manufacturing.commons.util.producer.ReplyToQueueNameProducer;
import io.qiot.manufacturing.edge.machinery.domain.event.BootstrapCompletedEvent;
import io.qiot.manufacturing.edge.machinery.domain.event.chain.ValidationFailedEvent;
import io.qiot.manufacturing.edge.machinery.domain.event.chain.ValidationSuccessfullEvent;
import io.qiot.manufacturing.edge.machinery.service.machinery.MachineryService;

@ApplicationScoped
public class ValidationMessageConsumer implements Runnable {

    @Inject
    Logger LOGGER;

    @Inject
    ObjectMapper MAPPER;

    @Inject
    ConnectionFactory connectionFactory;

    @Inject
    MachineryService machineryService;

    @Inject
    ReplyToQueueNameProducer replyToQueueNameProducer;

    @Inject
    Event<ValidationSuccessfullEvent> successEvent;

    @Inject
    Event<ValidationFailedEvent> failureEvent;

    private JMSContext context;

    private JMSConsumer consumer;

    private String replyToQueueName;

    private Queue replyToQueue;

    private final ExecutorService scheduler = Executors
            .newSingleThreadExecutor();

    void init(@Observes BootstrapCompletedEvent event) {
        doInit();
        scheduler.submit(this);
    }

    @PreDestroy
    void destroy() {
        scheduler.shutdown();
        context.close();
    }

    private void doInit() {
        if (Objects.nonNull(context))
            context.close();
        context = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE);

        replyToQueueName = replyToQueueNameProducer
                .getReplyToQueueName(machineryService.getMachineryId());

        replyToQueue = context.createQueue(replyToQueueName);

        consumer = context.createConsumer(replyToQueue);
    }

    @Override
    public void run() {
        while (true) {
            try {
            Message message = consumer.receive();
                String messagePayload = message.getBody(String.class);
                ValidationResponseDTO messageDTO = MAPPER
                        .readValue(messagePayload, ValidationResponseDTO.class);
                LOGGER.info("Received validation result "
                        + "for STAGE {} on ITEM {} / PRODUCTLINE {}",
                        messageDTO.stage, messageDTO.itemId, messageDTO.productLineId);
                if (messageDTO.valid) {
                    ValidationSuccessfullEvent event = new ValidationSuccessfullEvent();
                    event.productLineId = messageDTO.productLineId;
                    event.itemId = messageDTO.itemId;
                    event.stage = messageDTO.stage;
                    successEvent.fire(event);
                } else {
                    ValidationFailedEvent event = new ValidationFailedEvent();
                    event.productLineId = messageDTO.productLineId;
                    event.itemId = messageDTO.itemId;
                    event.stage = messageDTO.stage;
                    failureEvent.fire(event);
                }
            } catch (JMSException e) {
                LOGGER.error(
                        "The messaging client returned an error: {} and will be restarted.",
                        e);
                doInit();
            } catch (JsonProcessingException e) {
                LOGGER.error(
                        "The message payload is malformed and the validation request will not be sent: {}",
                        e);
            } catch (Exception e) {
                LOGGER.error(
                        "GENERIC ERROR",
                        e);
            }
        }
    }
}