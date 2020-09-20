package com.thoughtworks.rslist.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.exception.InvalidParamException;
import com.thoughtworks.rslist.exception.InvalidRequestParamException;
import com.thoughtworks.rslist.service.RsService;
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
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(path = "/rs", produces = "application/json; charset=utf-8")
public class RsController {

    private final RsService rsService;

    @Autowired
    public RsController(RsService rsService) {
        this.rsService = rsService;
    }

    @GetMapping("/events")
    @JsonView(RsEvent.WithoutUserView.class)
    public ResponseEntity<List<RsEvent>> getAllRsEvents(@RequestParam(required = false) Integer start,
                                                        @RequestParam(required = false) Integer end) {
        List<RsEvent> rsList = rsService.getAllRsEvents();

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
        return rsService.getRsEventById(id);
    }

    @PostMapping("/events")
    public ResponseEntity<RsEvent> createRsEvent(@Valid @RequestBody RsEvent rsEvent, Errors errors) {
        if (errors.hasErrors()) {
            throw new InvalidParamException();
        }

        RsEvent createdRsEvent = rsService.createRsEvent(rsEvent);

        int eventId = createdRsEvent.getId();

        return ResponseEntity
                .created(URI.create("/rs/events/" + eventId))
                .body(rsEvent);
    }

    @PatchMapping("/events/{id}")
    public ResponseEntity<RsEvent> updateRsEvent(@RequestBody RsEvent rsEvent, @PathVariable int id) {
        rsEvent.setId(id);
        RsEvent updatedRsEvent = rsService.updateRsEvent(rsEvent);

        return ResponseEntity.ok(updatedRsEvent);
    }

    @DeleteMapping("/events/{id}")
    public void deleteRsEventById(@PathVariable int id) {
        rsService.deleteRsEventById(id);
    }

    @PostMapping("/votes/{rsEventId}")
    @ResponseBody
    public Vote createVote(@RequestBody Vote vote, @PathVariable int rsEventId) {
        vote.setRsEventId(rsEventId);

        return rsService.createVote(vote);
    }

    @GetMapping("/votes")
    @ResponseBody
    public List<Vote> getVotes(@RequestParam @NotNull Timestamp startTime,
                               @RequestParam @NotNull Timestamp endTime) {
        return rsService.getVotes(startTime, endTime);
    }
}
