package com.linecorp.spoon.dto;


import com.linecorp.spoon.service.JoinCollectService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
public class EventInfoDto {

    private String holder;

    private List<String> joinerList = new ArrayList<String>();
}
