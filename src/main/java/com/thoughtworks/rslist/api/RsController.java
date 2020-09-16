package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<RsEvent> getAllRsEvents() {
        return rsList;
    }

    @PostMapping("/events")
    public RsEvent createRsEvent(@Valid @RequestBody RsEvent rsEvent) {
        User user = rsEvent.getUser();
        userService.registerUser(user);
        rsList.add(rsEvent);
        return rsEvent;
    }

    @PutMapping("/events/{index}")
    public RsEvent updateRsEvent(@RequestBody RsEvent rsEvent, @PathVariable int index) {
        index--;
        validateIndex(index);

        RsEvent oldRsEvent = rsList.get(index);
        RsEvent newRsEvent = oldRsEvent.merge(rsEvent);
        rsList.set(index, newRsEvent);

        return newRsEvent;
    }

    @DeleteMapping("/events/{index}")
    public RsEvent deleteRsEvent(@PathVariable int index) {
        index--;
        validateIndex(index);

        return rsList.remove(index);
    }

    private void validateIndex(int index) {
        if (index < 0 || index >= rsList.size()) {
            throw new RuntimeException("index out of range");
        }
    }
}
