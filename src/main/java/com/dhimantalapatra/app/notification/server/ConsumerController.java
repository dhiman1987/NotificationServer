package com.dhimantalapatra.app.notification.server;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    @GetMapping("/{consumerId}/notification/{notificationId}/action/{action}")
    public String registerConsumer(@PathVariable String consumerId,
                                   @PathVariable String notificationId,
                                   @PathVariable String action){
        return String.format("Customer with id[%s] " +
                "accessed notification with id [%s] " +
                "to execute action [%s]",consumerId,notificationId,action);
    }
}
