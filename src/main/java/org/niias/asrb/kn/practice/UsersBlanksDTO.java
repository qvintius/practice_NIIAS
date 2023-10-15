package org.niias.asrb.kn.practice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
public class UsersBlanksDTO extends BlanksViewsDTO{

    @Getter
    @Setter
    private String railwayShortName;

    @Getter
    @Setter
    private String subdivision;

    @Getter
    @Setter
    private String fio;

}
