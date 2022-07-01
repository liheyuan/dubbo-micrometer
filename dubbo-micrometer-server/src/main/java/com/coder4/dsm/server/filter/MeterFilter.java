package com.coder4.dsm.server.filter;

import io.micrometer.core.instrument.DistributionSummary;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import static org.apache.dubbo.common.constants.CommonConstants.PROVIDER;

@Activate(group = {PROVIDER})
public class MeterFilter implements Filter {

    private static String getMethod(Invoker invoker) {
        return String.format("%s_%s",
                invoker.getInterface().getSimpleName(),
                invoker.getInterface().getMethods()[0].getName());
    }

    private void recordTimeDistribution(Invoker invoker, long ms) {
        String method = getMethod(invoker);
        DistributionSummary.builder("app_rpc_request_time_ms")
                .tag("method", method)
                .publishPercentiles(0.5, 0.95)
                .publishPercentileHistogram()
                .register(MeterUtils.getMeterRegistry())
                .record(ms);
    }

    public void recordCounterOfTotalCounts(Invoker invoker) {
        String method = getMethod(invoker);
        MeterUtils.getMeterRegistry().counter("app_rpc_request_total_counts", "method", method).increment();
    }

    public void recordCounterOfErrorCounts(Invoker invoker) {
        String method = getMethod(invoker);
        MeterUtils.getMeterRegistry().counter("app_rpc_request_error_counts", "method", method).increment();
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        long start = System.currentTimeMillis();
        try {
            return invoker.invoke(invocation);
        } catch (Exception e) {
            recordCounterOfErrorCounts(invoker);
            throw e;
        } finally {
            recordTimeDistribution(invoker, System.currentTimeMillis() - start);
            recordCounterOfTotalCounts(invoker);
        }
    }
}
