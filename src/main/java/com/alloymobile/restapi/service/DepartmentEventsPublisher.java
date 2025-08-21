package com.alloymobile.restapi.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import com.alloymobile.restapi.persistence.Department;

@Component
@AllArgsConstructor
public class DepartmentEventsPublisher {

  private final SimpMessagingTemplate template;

  public void created(Department department) {
    template.convertAndSend("/topic/departments",
        new WsEvent<>("CREATED", department));
  }

  public void updated(Department dto) {
    template.convertAndSend("/topic/departments",
        new WsEvent<>("UPDATED", dto));
  }

  public void deleted(Long id) {
    template.convertAndSend("/topic/departments",
        new WsEvent<>("DELETED", new DeletedPayload(id)));
  }

  @Data @AllArgsConstructor
  static class WsEvent<T> {
    private String type;  // CREATED | UPDATED | DELETED
    private T payload;
  }

  @Data @AllArgsConstructor
  static class DeletedPayload {
    private Long id;
  }
}
