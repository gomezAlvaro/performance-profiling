package training.performance.profiling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import training.performance.profiling.dto.LoanApplicationDto;
import training.performance.profiling.entity.LoanApplication;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BasicController {
  private final BasicService basicService;

  @GetMapping("basic")
  public String get() throws InterruptedException {
    basicService.getBasic();
    return "Hello there";
  }


}

