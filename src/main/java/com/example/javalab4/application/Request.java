package com.example.javalab4.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    private int startFloor;
    private int endFloor;
    private boolean inWork;
    private int elevatorNumber;
}
