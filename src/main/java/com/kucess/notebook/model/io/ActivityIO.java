package com.kucess.notebook.model.io;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
@Setter
public class ActivityIO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long index;

    private @NonNull String activityName;

    private @NonNull String activityDescription;

    private @NonNull double score;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String adminUserName;

}
