package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import com.thoughtworks.rslist.dto.RsEvent;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/rs", produces = "application/json; charset=utf-8")
public class RsController {

    @Autowired
    private UserService userService;

    private final List<RsEvent> rsList;

    public RsController() {
        rsList = new ArrayList<RsEvent>() {{
            add(RsEvent.builder().eventName("第一条事件").keyword("无分类").build());
            add(RsEvent.builder().eventName("第二条事件").keyword("无分类").build());
            add(RsEvent.builder().eventName("第三条事件").keyword("无分类").build());
        }};
    }

    @GetMapping("/events")
    public ResponseEntity<List<RsEvent>> getAllRsEvents() {
        return ResponseEntity.ok(rsList);
    }

    @PostMapping("/events")
    public ResponseEntity<RsEvent> createRsEvent(@Valid @RequestBody RsEvent rsEvent) {
        User user = rsEvent.getUser();
        userService.registerUser(user);
        rsList.add(rsEvent);
        int index = rsList.size();
        return ResponseEntity
                .created(URI.create("/rs/events/" + index))
                .body(rsEvent);
    }

    @PutMapping("/events/{index}")
    public ResponseEntity<RsEvent> updateRsEvent(@RequestBody RsEvent rsEvent, @PathVariable int index) {
        index--;
        validateIndex(index);

        RsEvent oldRsEvent = rsList.get(index);
        RsEvent newRsEvent = oldRsEvent.merge(rsEvent);
        rsList.set(index, newRsEvent);

        return ResponseEntity.ok(newRsEvent);
    }

    @DeleteMapping("/events/{index}")
    public ResponseEntity<RsEvent> deleteRsEvent(@PathVariable int index) {
        index--;
        validateIndex(index);

        return ResponseEntity.ok(rsList.remove(index));
    }

    private void validateIndex(int index) {
        if (index < 0 || index >= rsList.size()) {
            throw new RuntimeException("index out of range");
        }
    }
}
