package com.kucess.notebook.model.io;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class ActivityIO {

    private @NonNull String activityName;

    private @NonNull String activityDescription;

    private @NonNull double score;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String adminUserName;

}
