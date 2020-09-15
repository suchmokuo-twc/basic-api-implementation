package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEvent;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(path = "/rs", produces = "application/json; charset=utf-8")
public class RsController {

  private final List<RsEvent> rsList;

  public RsController() {
    rsList = new ArrayList<RsEvent>() {{
      add(new RsEvent("第一条事件", "无分类"));
      add(new RsEvent("第二条事件", "无分类"));
      add(new RsEvent("第三条事件", "无分类"));
    }};
  }

  @PutMapping("/event/{index}")
  public RsEvent updateRsEvent(@RequestBody RsEvent rsEvent, @PathVariable int index) {
    index--;
    validateIndex(index);

    rsList.set(index, rsEvent);

    return rsEvent;
  }

  @DeleteMapping("/event/{index}")
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
