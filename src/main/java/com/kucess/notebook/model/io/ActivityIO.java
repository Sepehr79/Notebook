package com.kucess.notebook.model.io;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityIO {

    private int index;

    private @NonNull String activityName;

    private @NonNull String activityDescription;

    private @NonNull double score;

    private String adminUserName;

}
