package blend

import org.slf4j.LoggerFactory

object Client {

    val name = "Blend"
    val version = "1.0.0"
    val logger = LoggerFactory.getLogger(name)!!

    fun init() {
        val preInitTime = System.currentTimeMillis()

        logger.info("Initialized $name v$version in ${System.currentTimeMillis() - preInitTime}ms")
    }

    fun shutdown() {

    }

}