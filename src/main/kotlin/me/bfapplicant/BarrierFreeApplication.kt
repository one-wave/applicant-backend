package me.bfapplicant

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BarrierFreeApplication

fun main(args: Array<String>) {
    runApplication<BarrierFreeApplication>(*args)
}
