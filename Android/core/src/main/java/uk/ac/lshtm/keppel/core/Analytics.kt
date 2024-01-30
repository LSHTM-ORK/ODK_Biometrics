package uk.ac.lshtm.keppel.core

interface Analytics {

    fun log(event: String)

    companion object {

        private var instance: Analytics? = null

        fun log(event: String) {
            instance?.log(event)
        }

        fun setInstance(instance: Analytics) {
            this.instance = instance
        }
    }
}
