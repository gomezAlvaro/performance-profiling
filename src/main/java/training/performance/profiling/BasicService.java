package training.performance.profiling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.performance.profiling.dto.CommentDto;
import training.performance.profiling.dto.LoanApplicationDto;
import training.performance.profiling.entity.Audit;
import training.performance.profiling.entity.LoanApplication;
import training.performance.profiling.entity.Payment;
import training.performance.profiling.repo.AuditRepo;
import training.performance.profiling.repo.LoanApplicationRepo;
import training.performance.profiling.repo.PaymentRepo;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.LongStream;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BasicService {
  public void getBasic() throws InterruptedException {
    Thread.sleep(2000);
    myFirstMethod();
    mySecondMethod();
  }

  private void mySecondMethod() throws InterruptedException {
    Thread.sleep(2000);
  }

  private void myFirstMethod() throws InterruptedException {
    Thread.sleep(2000);

  }
}
