package com.example.tbm_task.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BTS {

    private String logic_id;

    private String time_up;

    private String time_down;

    private String duration;

    private String topology;

    private String device_name;

    private String additional_info;

    private String count_alarm;

    private String count_repeated;

    private String duration_in_hour;

    private String sum_duration;

    private String duration_in_min;

    private String duration_in_second;
}
