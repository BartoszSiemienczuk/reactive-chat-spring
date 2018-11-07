package com.softwarehut.reactivechat.rest;

import com.mongodb.connection.Server;
import com.softwarehut.reactivechat.model.Message;
import com.softwarehut.reactivechat.service.repository.MessageRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class FunctionalHandler {
    private MessageRepository messageRepository;

    public FunctionalHandler(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Mono<ServerResponse> streamAll(ServerRequest request){
        return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(messageRepository.findAll(), Message.class);
    }

    public Mono<ServerResponse> streamRoom(ServerRequest request){
        String room = request.pathVariable("room");
        return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(messageRepository.findByRoom(room), Message.class);
    }

    public Mono<ServerResponse> createMessage(ServerRequest request){
        Mono<Message> message = request.bodyToMono(Message.class);
        return ServerResponse.ok().body(
            BodyInserters.fromPublisher( messageRepository.insert(message), Message.class )
        );
    }
}
