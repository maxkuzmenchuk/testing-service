package com.kzumenchuk.testingservice.util.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteTestRequest {
    private Long[] deleteIDs;
}
