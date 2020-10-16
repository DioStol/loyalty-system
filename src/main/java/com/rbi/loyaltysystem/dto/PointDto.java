package com.rbi.loyaltysystem.dto;

import com.rbi.loyaltysystem.model.Point;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@Getter
@AllArgsConstructor
public class PointDto {

    private List<Point> points;

    private double total;
}
