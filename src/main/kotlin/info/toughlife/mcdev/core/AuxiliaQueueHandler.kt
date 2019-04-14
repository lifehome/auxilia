package info.toughlife.mcdev.core

import java.util.*

class AuxiliaQueueHandler {

    private val queue = LinkedList<AuxiliaQueue>()

    var allowNext = true

    /**
     * Execute the next backup in a queue
     */
    fun next() {
        if (!allowNext)
            return

        val queue = this.queue.poll() ?: return

        allowNext = false

        queue.start()
    }

    fun cancelAll() {
        for (queue in queue) {
            queue.interrupt()
        }
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