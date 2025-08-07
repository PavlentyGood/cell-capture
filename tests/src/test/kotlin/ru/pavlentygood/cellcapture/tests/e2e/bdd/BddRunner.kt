package ru.pavlentygood.cellcapture.tests.e2e.bdd

import io.cucumber.spring.CucumberContextConfiguration
import org.junit.platform.suite.api.IncludeEngines
import org.junit.platform.suite.api.SelectPackages
import org.junit.platform.suite.api.Suite
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignAutoConfiguration
import ru.pavlentygood.cellcapture.tests.e2e.Container

@Suite
@IncludeEngines("cucumber")
@SelectPackages(value = ["bdd"])
class BddRunner

@CucumberContextConfiguration
@EnableFeignClients(basePackages = ["ru.pavlentygood.cellcapture.tests.e2e.client"])
@SpringBootTest(
    classes = [
        FeignAutoConfiguration::class,
        JacksonAutoConfiguration::class,
        HttpMessageConvertersAutoConfiguration::class
    ]
)
class SpringConfig {

    init {
        Container.init()
    }
}
