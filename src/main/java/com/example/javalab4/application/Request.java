package com.example.javalab4.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Все заявки на рабоут лифта представляют собой объект этого класса
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    private int startFloor;
    private int endFloor;
    // находится ли заявка в работе
    private boolean inWork;
    private int elevatorNumber;
}
