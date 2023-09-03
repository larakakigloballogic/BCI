package com.bci.exercise.domain.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@Data
@NoArgsConstructor
public class RequestUser {

    private User user;

}
