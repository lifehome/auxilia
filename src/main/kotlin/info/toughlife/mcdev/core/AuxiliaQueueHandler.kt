package info.toughlife.mcdev.core

import java.util.*

class AuxiliaQueueHandler {

    private val queue = PriorityQueue<AuxiliaQueue>()

    /**
     * Execute the next backup in a queue
     */
    fun next() {
        val queue = this.queue.poll()

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
    }

}