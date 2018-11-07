package com.softwarehut.reactivechat;

import com.softwarehut.reactivechat.model.Message;
import com.softwarehut.reactivechat.service.repository.MessageRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
public class StarterDataLoader implements CommandLineRunner {
    private final MessageRepository msgRepo;
    private final ReactiveMongoTemplate mongoTemplate;

    public StarterDataLoader(MessageRepository msgRepo, ReactiveMongoTemplate mongoTemplate) {
        this.msgRepo = msgRepo;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        //Musimy utworzyć kolekcję "capped" (o zdefiniowanej pojemności) aby strumieniować z niej eventy
        mongoTemplate.dropCollection("messages").then(
            mongoTemplate.createCollection(
                    "messages",
                    CollectionOptions.empty().capped().size(10240).maxDocuments(100000)
            )
        ).subscribe();

        List<Message> messagesToInsert =  List.of(
            new Message("Hello chat!", "room1", "Janek"),
            new Message("Hello reactive world!", "room1", "Wojtek"),
            new Message("Third message", "room1", "Katarzyna"),
            new Message("Drugi pokój", "room2", "Katarzyna")
        );

        Flux.fromIterable(messagesToInsert).subscribe(message -> {
            msgRepo.insert(message).subscribe(insertedMessage -> {
                System.out.println("Message with content: /" + insertedMessage.getContent() + "/ was inserted.");
            });
        });
    }
}
