package ru.otus.api;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.domain.Message;
import ru.otus.domain.MessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.service.DataStore;

@RestController
public class DataController {
    private static final Logger log = LoggerFactory.getLogger(DataController.class);
    private static final String ALL_MESSAGE_ROOM_ID = "1408";
    private final DataStore dataStore;
    private final Scheduler workerPool;

    public DataController(DataStore dataStore, Scheduler workerPool) {
        this.dataStore = dataStore;
        this.workerPool = workerPool;
    }

    @PostMapping(value = "/msg/{roomId}")
    public Mono<Long> messageFromChat(@PathVariable("roomId") String roomId,
                                      @RequestBody MessageDto messageDto) {
        var messageStr = messageDto.messageStr();
        log.info("messageFromChat, roomId:{}, msg:{}", roomId, messageStr);

        var msgId = Mono.just(new Message(roomId, messageStr))
                .doOnNext(msg -> log.info("msg saving:{}", msg))
                .flatMap(dataStore::saveMessage)
                .publishOn(workerPool)
                .doOnNext(msgSaved -> log.info("msgSaved id:{}", msgSaved.getId()))
                .map(Message::getId)
                .subscribeOn(workerPool);

        log.info("messageFromChat, roomId:{}, msg:{} done", roomId, messageStr);
        return msgId;
    }

    @GetMapping(value = "/msg/{roomId}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<MessageDto> getMessagesByRoomId(@PathVariable("roomId") String roomId) {
        log.info("getMessagesByRoomId, roomId:{}", roomId);
        Flux<Message> result;
        if (roomId.equals(ALL_MESSAGE_ROOM_ID)) {
            log.info("loading messages from all rooms");
            result = dataStore.getAllMessages();
        } else {
            result = Mono.just(roomId)
                    .doOnNext(room -> log.info("loading for roomId:{}", room))
                    .flatMapMany(dataStore::loadMessages);
        }
        return result.publishOn(workerPool)
                    .map(message -> new MessageDto(message.getMsgText()))
                    .doOnNext(msgDto -> log.info("msgDto:{}", msgDto))
                    .subscribeOn(workerPool);
    }
}
