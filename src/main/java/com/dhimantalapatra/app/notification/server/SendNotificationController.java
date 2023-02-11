package com.dhimantalapatra.app.notification.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/")
public class SendNotificationController {

    private JmsTemplate jmsTemplate;
    private ObjectMapper objectMapper;

    public SendNotificationController(JmsTemplate jmsTemplate, ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
    }

    @GetMapping("")
    public ModelAndView loadPublishMessage(){
        ModelAndView mav = new ModelAndView("publishNotification","notification",getPlaceHolder());
        return mav;
    }

    @PostMapping("")
    public ModelAndView publishMessage(@ModelAttribute NotificationMessage notificationMessage){
        ModelAndView mav = new ModelAndView("publishNotification");
        try {
            String payload  = objectMapper.writeValueAsString(notificationMessage);
            System.out.println("Publishing "+payload);
            jmsTemplate.convertAndSend("messageServer", payload,messagePostProcessor -> {
                messagePostProcessor.setStringProperty("consumer",
                        notificationMessage.getConsumerId());
                return messagePostProcessor;
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            mav.addObject("error","Message publishing failed");
        }
        mav.addObject("notification",getPlaceHolder());
        mav.addObject("message","Message "+notificationMessage.getMsgId()+" published");
        return mav;
    }

    private NotificationMessage getPlaceHolder(){
        String msgId = UUID.randomUUID().toString();
        String consumer = "dexter";
        return new NotificationMessage(msgId,consumer,null,null,null);
    }
}
