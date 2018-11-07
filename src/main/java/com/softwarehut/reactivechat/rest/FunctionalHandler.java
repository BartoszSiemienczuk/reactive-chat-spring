package com.softwarehut.reactivechat.rest;

import com.mongodb.connection.Server;
import com.softwarehut.reactivechat.model.Message;
import com.softwarehut.reactivechat.service.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class FunctionalHandler {
    private static final Logger logger = LoggerFactory.getLogger(FunctionalHandler.class);
    private MessageRepository messageRepository;

    public FunctionalHandler(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /*
    Pamiętajmy o prawidłowym contentType dla streamujących endpointów
     */
    public Mono<ServerResponse> streamAll(ServerRequest request){
        return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(messageRepository.findAll(), Message.class);
    }

    public Mono<ServerResponse> streamRoom(ServerRequest request){
        String room = request.pathVariable("room");
        return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(messageRepository.findByRoom(room), Message.class);
    }

    /*
    BodyInserters.fromPublisher umożliwia zacommitowanie odpowiedzi kiedy publisher skończy działanie
     */
    public Mono<ServerResponse> createMessage(ServerRequest request){
        Mono<Message> message = request.bodyToMono(Message.class);
        logger.debug("Got a message on functional controller.");
        return ServerResponse.ok().body(
            BodyInserters.fromPublisher( messageRepository.insert(message), Message.class )
        );
    }
}
