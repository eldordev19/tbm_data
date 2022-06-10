package com.example.tbm_task.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Result {

    private String device_name_id;

    private String logic_id;

    private String time_up;

    private String time_down;

    private String duration;

    private String topology;

    private String additional_info;

    private String latitude;

    private String longitude;

}
