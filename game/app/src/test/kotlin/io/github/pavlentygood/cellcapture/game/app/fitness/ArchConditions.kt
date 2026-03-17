package io.github.pavlentygood.cellcapture.game.app.fitness

import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.domain.JavaModifier
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.SimpleConditionEvent

fun haveSinglePublicMethod() =
    object : ArchCondition<JavaClass>("have single public method") {
        override fun check(item: JavaClass, events: ConditionEvents) {
            val publicMethodCount = item.methods
                .count { it.modifiers.contains(JavaModifier.PUBLIC) }
            if (publicMethodCount > 1) {
                events.add(SimpleConditionEvent.violated(item, "Class ${item.name}"))
            }
        }
    }

fun notThrowAnyException() =
    object : ArchCondition<JavaClass>("not throw any exception") {
        override fun check(item: JavaClass, events: ConditionEvents) {
            item.methods
                .flatMap { it.callsFromSelf }
                .map { it.targetOwner }
                .filter { it.isAssignableTo(Exception::class.java) }
                .filter { !it.isAssignableTo(NoWhenBranchMatchedException::class.java) }
                .forEach {
                    events.add(SimpleConditionEvent.violated(item, "Exception ${it.name} in class ${item.name}"))
                }
        }
    }
