package ru.pavlentygood.cellcapture.tests.e2e

import org.testcontainers.containers.ComposeContainer

fun ComposeContainer.withLogConsumer(serviceName: String): ComposeContainer =
    withLogConsumer(serviceName) { log ->
        print("$serviceName | ${log.utf8String}")
    }
