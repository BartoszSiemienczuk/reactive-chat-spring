package com.softwarehut.reactivechat.rest;

import com.softwarehut.reactivechat.model.Message;
import com.softwarehut.reactivechat.service.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/*
Kontroler utworzony klasyczną metodą adnotacji do złudzenia przypomina kontroler
klasycznego, blokującego Spring MVC. Korzysta jednak z reaktywnym typów w non-blocking sposób.
 */
@RestController
public class ClassicController {
    private static final Logger logger = LoggerFactory.getLogger(ClassicController.class);
    private MessageRepository messageRepository;

    public ClassicController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /*
    Korzystając z tej metody zwykła przeglądarka nie może obsłużyć rezultatu jako strumienia,
    więc zostanie wyprodukowany klasyczny JSON zawierający wszystkie dane.
     */
    @GetMapping("/messages")
    public Flux<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    /*
    Czym się różni od powyższej metody? Content type to text/event-stream, więc wysyła wiadomości
    z użyciem Server Side Events, pozwalając na asynchroniczne przetwarzanie strumieniowe.
     */
    @GetMapping(value = "/streamMessages", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Message> streamAllMessages(){
        return messageRepository.findAll();
    }

    /*
    Ta metoda streamuje wiadomości z wybranego kanału. Metoda w reposiotry musi być @Tailable, a kolekcja capped.
     */
    @GetMapping(value = "/rooms/{room}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Message> getMessagesByRoom(@PathVariable String room){
        return messageRepository.findByRoom(room);
    }

    @PostMapping("/messages")
    public Mono<Message> createMessage(@Valid @RequestBody Message message){
        logger.debug("Got a message on annotated controller.");
        return messageRepository.save(message);
    }

}
