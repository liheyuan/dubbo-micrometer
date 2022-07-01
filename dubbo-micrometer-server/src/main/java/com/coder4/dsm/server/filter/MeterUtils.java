package com.coder4.dsm.server.filter;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MeterUtils {

    private static MeterRegistry meterRegistry;

    @Autowired
    private void setMeterRegistry(MeterRegistry meterRegistry) {
        MeterUtils.meterRegistry = meterRegistry;
    }

    public static MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }

}
