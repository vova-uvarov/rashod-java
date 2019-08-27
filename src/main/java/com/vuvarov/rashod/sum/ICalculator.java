package com.vuvarov.rashod.sum;

public interface ICalculator<I, R> {
    R calculate(I inputData);
}
