/*
 * The GNU Affero General Public License v3.0 (AGPL-3.0)
 *
 * Copyright (c) 2020 Ilya Lysenko
 *
 * Permissions of this strongest copyleft license are conditioned on making available complete source code of licensed
 * works and modifications, which include larger works using a licensed work, under the same license.
 * Copyright and license notices must be preserved. Contributors provide an express grant of patent rights.
 * When a modified version is used to provide a service over a network,  the complete source code of the modified
 * version must be made available.
 */
package ru.ilysenko.tinka.indicator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import ru.tinkoff.invest.model.Candle;

import java.util.List;

/**
 * Implementation of the Williams %R indicator
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "create", buildMethodName = "init")
public class WilliamsRIndicator extends AbstractIndicator {
    private int periodsCount = 3;

    private static final int OVERBOUGHT_THRESHOLD = -20;
    private static final int OVERSOLD_THRESHOLD = -80;

    @Override
    public double calculate(List<Candle> candles) {
        candles = limitCandles(candles, periodsCount);

        if (candles.size() < periodsCount) {
            return Double.NaN;
        }
        validate(candles);

        double maxHi = getMaxHi(candles);
        double minLo = getMinLo(candles);
        double currentPrice = getLastCandle(candles).getC();

        return ((currentPrice - maxHi) / (maxHi - minLo)) * 100;
    }

    @Override
    protected void validate(List<Candle> candles) {
        if(candles.size() > 1) {
            super.validate(candles);
        }
    }

    @Override
    public double getOverboughtThreshold() {
        return OVERBOUGHT_THRESHOLD;
    }

    @Override
    public double getOversoldThreshold() {
        return OVERSOLD_THRESHOLD;
    }

    private double getMinLo(List<Candle> candles) {
        return candles.stream()
                .map(Candle::getL)
                .min(Double::compare)
                .orElseThrow(IllegalStateException::new);
    }

    private double getMaxHi(List<Candle> candles) {
        return candles.stream()
                .map(Candle::getH)
                .max(Double::compare)
                .orElseThrow(IllegalStateException::new);
    }

    private Candle getLastCandle(List<Candle> candles) {
        return candles.get(0);
    }
}
