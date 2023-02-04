package dev.vadzimv.paraphrase.doubles

import dev.vadzimv.paraphrase.KeyValueStorage

class FakeKeyValueStorage(
    initialValues: Map<String, String> = mapOf()
) : KeyValueStorage {

    private val storage = initialValues.toMutableMap()

    override fun save(key: String, value: String) {
        storage[key] = value
    }

    override fun read(key: String): String? {
        return storage.getOrDefault(key, null)
    }
}