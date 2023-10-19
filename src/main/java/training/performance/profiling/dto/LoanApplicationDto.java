package training.performance.profiling.dto;

import lombok.Value;
import training.performance.profiling.entity.LoanApplication;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Value
public class LoanApplicationDto {
  Long id;
  String title;
  LoanApplication.Status globalStatus;
  List<String> comments;

  public LoanApplicationDto(LoanApplication loanApplication, List<CommentDto> comments) {
    id = loanApplication.getId();
    title = loanApplication.getTitle();
    globalStatus = loanApplication.getCurrentStatus();
    this.comments = comments.stream().map(CommentDto::getBody).collect(toList());
  }
}
