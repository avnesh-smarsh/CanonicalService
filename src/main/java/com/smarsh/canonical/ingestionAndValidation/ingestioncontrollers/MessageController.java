
package com.smarsh.canonical.ingestionAndValidation.ingestioncontrollers;

//import com.smarsh.canonical.ingestionAndValidation.services.MessageProducerService;
import com.smarsh.canonical.ingestionAndValidation.services.MessageValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageValidationService messageValidationService;

    public MessageController(MessageValidationService messageValidationService) {
        this.messageValidationService = messageValidationService;
    }

    @PostMapping
    public ResponseEntity<String> ingestMessage(@RequestBody String payload) throws Exception {
        String result = messageValidationService.messageValidation(payload);
        return ResponseEntity.ok(result);
    }
}
