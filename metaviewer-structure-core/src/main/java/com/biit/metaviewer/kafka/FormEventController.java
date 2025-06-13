package com.biit.metaviewer.kafka;


import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.kafka.consumers.EventListener;
import com.biit.kafka.events.Event;
import com.biit.kafka.events.EventCustomProperties;
import com.biit.kafka.logger.EventsLogger;
import com.biit.metaviewer.cadt.CadtController;
import com.biit.metaviewer.controllers.FormController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.TimeZone;

@Controller
@ConditionalOnExpression("${spring.kafka.enabled:false}")
public class FormEventController {

    public static final String ALLOWED_FACT_TYPE = "DroolsResultForm";

    private final FormController formController;


    @Autowired(required = false)
    public FormEventController(EventListener eventListener, FormController formController) {
        this.formController = formController;

        //Listen to a topic
        if (eventListener != null) {
            eventListener.addListener((event, offset, groupId, key, partition, topic, timeStamp) ->
                    eventHandler(event, groupId, key, partition, topic, timeStamp));
        }
    }


    public void eventHandler(Event event, String groupId, String key, int partition, String topic, long timeStamp) {
        EventsLogger.debug(this.getClass(), "Received event '{}' on topic '{}', group '{}', key '{}', partition '{}' at '{}'",
                event, topic, groupId, key, partition, LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp),
                        TimeZone.getDefault().toZoneId()));

        try {
            if (event.getCustomProperties() != null) {
                if (!Objects.equals(event.getCustomProperty(EventCustomProperties.FACT_TYPE), ALLOWED_FACT_TYPE)) {
                    EventsLogger.debug(this.getClass(), "Event is not a form. Ignored.");
                    return;
                }
                if (Objects.equals(event.getTag(), CadtController.FORM_NAME)) {
                    EventsLogger.debug(this.getClass(), "Event is a CADT form. Ignored");
                    return;
                }
            }

            final DroolsSubmittedForm droolsForm = getDroolsForm(event);

            final String createdBy = event.getCustomProperties().get(EventCustomProperties.ISSUER.getTag()) != null
                    ? event.getCustomProperties().get(EventCustomProperties.ISSUER.getTag())
                    : event.getCreatedBy();

            EventsLogger.info(this.getClass(), "Received new drools form from '{}'", createdBy);

            formController.newFormReceived(droolsForm);

        } catch (Exception e) {
            EventsLogger.severe(this.getClass(), "Invalid event received!!\n" + event);
            EventsLogger.errorMessage(this.getClass(), e);
        }
    }

    private DroolsSubmittedForm getDroolsForm(Event event) {
        return event.getEntity(DroolsSubmittedForm.class);
    }
}
