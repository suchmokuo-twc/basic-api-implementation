package com.thoughtworks.rslist.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.exception.InvalidParamException;
import com.thoughtworks.rslist.exception.InvalidRequestParamException;
import com.thoughtworks.rslist.service.RsEventService;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import com.thoughtworks.rslist.dto.RsEvent;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/rs", produces = "application/json; charset=utf-8")
public class RsController {

    @Autowired
    private UserService userService;

    @Autowired
    private RsEventService rsEventService;

    @GetMapping("/events")
    @JsonView(RsEvent.WithoutUserView.class)
    public ResponseEntity<List<RsEvent>> getAllRsEvents(@RequestParam(required = false) Integer start,
                                                        @RequestParam(required = false) Integer end) {
        List<RsEvent> rsList = rsEventService.getAllRsEvents();

        if (start == null && end == null) {
            return ResponseEntity.ok(rsList);
        }

        try {
            return ResponseEntity.ok(rsList.subList(start - 1, end));
        } catch (Exception ex) {
            throw new InvalidRequestParamException();
        }
    }

    @GetMapping("/events/{id}")
    @JsonView(RsEvent.WithoutUserView.class)
    @ResponseBody
    public RsEvent getRsEventById(@PathVariable int id) {
        return rsEventService.getRsEventById(id);
    }

    @PostMapping("/events")
    public ResponseEntity<RsEvent> createRsEvent(@Valid @RequestBody RsEvent rsEvent, Errors errors) {
        if (errors.hasErrors()) {
            throw new InvalidParamException();
        }

        RsEvent createdRsEvent = rsEventService.createRsEvent(rsEvent);

        int eventId = createdRsEvent.getId();

        return ResponseEntity
                .created(URI.create("/rs/events/" + eventId))
                .body(rsEvent);
    }

    @PatchMapping("/events/{id}")
    public ResponseEntity<RsEvent> updateRsEvent(@RequestBody RsEvent rsEvent, @PathVariable int id) {
        rsEvent.setId(id);
        RsEvent updatedRsEvent = rsEventService.updateRsEvent(rsEvent);

        return ResponseEntity.ok(updatedRsEvent);
    }

    @DeleteMapping("/events/{id}")
    public void deleteRsEventById(@PathVariable int id) {
        rsEventService.deleteRsEventById(id);
    }

    @PostMapping("/votes/{rsEventId}")
    @ResponseBody
    public Vote createVote(@RequestBody Vote vote, @PathVariable int rsEventId) {
        vote.setRsEventId(rsEventId);

        return rsEventService.createVote(vote);
    }
}
