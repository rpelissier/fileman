package fr.dalae.fileman

import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class CountingObserver(val observeEveryN: Int = 100) {
    var count = 0
        private set

    fun notifyOne() {
        count++
        if (count % observeEveryN > 0) return

        val shouldContinue = observeAndConfirmContinue(count, false)
        if (!shouldContinue) throw InterruptedException("Interruption requested.")
    }

    fun notifyDone() {
        observeAndConfirmContinue(count, true)
    }

    abstract fun observeAndConfirmContinue(count: Int, completed: Boolean): Boolean

    companion object {
        val log: Logger = LoggerFactory.getLogger(CountingObserver::class.java)
        fun loggingObserver(observeEveryN: Int) = object : CountingObserver(observeEveryN) {
            override fun observeAndConfirmContinue(count: Int, completed: Boolean): Boolean {
                log.info("$count files imported.")
                if (completed) log.info("Import completed.")
                return true
            }
        }
    }
}
