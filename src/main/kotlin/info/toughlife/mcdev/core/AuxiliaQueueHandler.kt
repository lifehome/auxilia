package info.toughlife.mcdev.core

import java.util.*

class AuxiliaQueueHandler {

    val queue = LinkedList<AuxiliaQueue>()

    var current: AuxiliaQueue? = null
    var allowNext = true

    /**
     * Execute the next backup in a queue
     */
    fun next() {
        if (!allowNext)
            return

        val queue = this.queue.poll() ?: return

        allowNext = false

        current = queue

        queue.start()
    }

    fun cancelAll() {
        for (queue in queue) {
            queue.interrupt()
        }
        current = null

        queue.clear()
    }

    fun addToQueue(queue: AuxiliaQueue) {
        this.queue.add(queue)
        next()
    }

    fun addToQueuePriority(queue: AuxiliaQueue) {
        this.queue.offerFirst(queue)
        next()
    }

    fun size(): Int {
        return queue.size
    }

}