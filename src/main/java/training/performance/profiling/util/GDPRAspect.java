package training.performance.profiling.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
@Aspect
public class GDPRAspect {
  private final RestTemplate restTemplate;

  @Retention(RetentionPolicy.RUNTIME)
  public @interface VisibleFor {
    String value();
  }

  @Around("@within(org.springframework.web.bind.annotation.RestController))")
  public Object clearNonVisibleFields(ProceedingJoinPoint pjp) throws Throwable {
    // well done finding this! we're spending some time in here, but do we need to do so? can we improve the code somehow?
    Object resultDto = pjp.proceed();
    if (resultDto == null) {
      return null;
    }
    if (!resultDto.getClass().getSimpleName().equals("LoanApplicationDto")) {
      return resultDto;
    }

    List<Field> annotatedFields = getAnnotatedFields(resultDto);

    String userJurisdiction = fetchJurisdiction();

    if (annotatedFields.isEmpty()) {
      return resultDto;
    }



    clearFields(resultDto, userJurisdiction, annotatedFields);
    return resultDto;
  }

  @Nullable
  private String fetchJurisdiction() {
    try {
      return restTemplate.getForObject("http://localhost:9999/jurisdiction", String.class);
    } catch (RestClientException e) {
      throw new RuntimeException("No Jurisdiction", e);
    }
  }

  private static void clearFields(Object result, String userJurisdiction, List<Field> fieldsToClear) throws IllegalAccessException {
    for (Field field : fieldsToClear) {
      String requiredJurisdiction = field.getAnnotation(VisibleFor.class).value();
      if (!requiredJurisdiction.equals(userJurisdiction)) {
        field.set(result, null);
      }
    }
  }

  private static List<Field> getAnnotatedFields(Object result) {
    List<Field> annotatedFields = new ArrayList<>();
    for (Field field : result.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      VisibleFor annot = field.getAnnotation(VisibleFor.class);
      if (annot != null) {
        annotatedFields.add(field);
      }
    }
    return annotatedFields;
  }
}
