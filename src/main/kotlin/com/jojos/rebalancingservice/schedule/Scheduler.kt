package com.jojos.rebalancingservice.schedule

import com.jojos.rebalancingservice.common.log
import com.jojos.rebalancingservice.domain.service.OrchestrateRebalancing
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.system.measureTimeMillis

@Component
class Scheduler(private val orchestrateRebalancing: OrchestrateRebalancing) {

    private val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @Scheduled(fixedRateString = "\${scheduler.rate}")
    fun start() {
        log.info("scheduler started at ${dateFormat.format(Date())}")
        val duration = measureTimeMillis {
            orchestrateRebalancing()
        }
        log.info("rebalancing took $duration ms")
    }

}