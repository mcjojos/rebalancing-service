package com.jojos.rebalancingservice.domain.service

import com.jojos.rebalancingservice.common.log
import com.jojos.rebalancingservice.config.properties.CSVProperties
import com.jojos.rebalancingservice.domain.models.Customer
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class ReadCustomers(private val csvProperties: CSVProperties) {
    companion object {
        internal val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }

    operator fun invoke(): List<Customer> {
        val customers = mutableListOf<Customer>()
        val reader = Files.newBufferedReader(Paths.get(csvProperties.customersFile))
        val customerParser = CSVParser(
            reader, CSVFormat.DEFAULT
                .withHeader("customerId", "email", "dateOfBirth", "riskLevel", "retirementAge")
                .withSkipHeaderRecord()
                .withIgnoreHeaderCase()
                .withTrim()
        )
        for (csvRecord in customerParser) {
            val customer = Customer(
                customerId = csvRecord.get("customerId").toInt(),
                email = csvRecord.get("email"),
                riskLevel = csvRecord.get("riskLevel").toInt(),
                yearsToRetirement = yearsToRetirement(
                    LocalDate.parse(csvRecord.get("dateOfBirth"), dateFormatter),
                    csvRecord.get("retirementAge").toInt())
            )
            log.info("customer read : $customer")
            customers.add(customer)
        }
        return customers
    }
}

internal fun yearsToRetirement(dateOfBirth: LocalDate, retirementAge: Int): Int {
    val yearOfRetirement = dateOfBirth.year + retirementAge
    return yearOfRetirement - LocalDate.now().year
}

