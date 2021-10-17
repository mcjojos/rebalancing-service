package com.jojos.rebalancingservice.domain.service

import com.jojos.rebalancingservice.common.log
import com.jojos.rebalancingservice.config.properties.CSVProperties
import com.jojos.rebalancingservice.domain.models.Strategy
import com.jojos.rebalancingservice.domain.models.validator.PercentagesValidator
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths

@Service
class ReadStrategies(
    private val csvProperties: CSVProperties,
    private val percentagesValidator: PercentagesValidator
) {
    operator fun invoke(): List<Strategy> {
        val strategies = mutableListOf<Strategy>()
        val reader = Files.newBufferedReader(Paths.get(csvProperties.strategiesFile))
        val parser = CSVParser(
            reader, CSVFormat.DEFAULT
                .withHeader(
                    "strategyId", "minRiskLevel", "maxRiskLevel", "minYearsToRetirement", "maxYearsToRetirement",
                    "stocksPercentage", "cashPercentage", "bondsPercentage"
                )
                .withSkipHeaderRecord()
                .withIgnoreHeaderCase()
                .withTrim()
        )
        for (csvRecord in parser) {
            val strategy = Strategy(
                strategyId = csvRecord.get("strategyId").toInt(),
                minRiskLevel = csvRecord.get("minRiskLevel").toInt(),
                maxRiskLevel = csvRecord.get("maxRiskLevel").toInt(),
                minYearsToRetirement = csvRecord.get("minYearsToRetirement").toInt(),
                maxYearsToRetirement = csvRecord.get("maxYearsToRetirement").toInt(),
                stocksPercentage = csvRecord.get("stocksPercentage").toInt(),
                cashPercentage = csvRecord.get("cashPercentage").toInt(),
                bondsPercentage = csvRecord.get("bondsPercentage").toInt()
            )

            if (percentagesValidator.isValid(strategy)) {
                log.info("strategy read: $strategy")
                strategies.add(strategy)
            }
        }
        return strategies
    }
}

