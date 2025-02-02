package com.example.topics2.utilities.helper
import androidx.compose.runtime.rememberCoroutineScope
import com.example.topics2.db.dao.MessageDao
import com.example.topics2.db.dao.TopicDao
import com.example.topics2.db.enitities.MessageTbl
import com.example.topics2.db.enitities.TopicTbl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ThreadLocalRandom
class DatabaseSeeder(
    private val topicDao: TopicDao,
    private val messageDao: MessageDao
) {
    private companion object {
        const val MESSAGES_PER_TOPIC = 200
        const val REQUIRED_TOPIC_COUNT = 30
        const val WORDS_PER_MESSAGE = 300
        val TOPIC_NAMES = List(REQUIRED_TOPIC_COUNT) { "Sample Topic ${it + 1}" }

        val adjectives = listOf("beautiful", "fast", "slow", "ancient", "bright", "dark",
                              "tall", "small", "grumpy", "peaceful", "shiny", "dirty")
        val nouns = listOf("cat", "dog", "bird", "tree", "car", "mountain",
                         "river", "city", "forest", "lake", "house", "sky")
        val verbs = listOf("runs", "flies", "sits", "jumps", "swims", "walks",
                         "climbs", "dives", "sings", "barks", "howls", "shines")
        val adverbs = listOf("quickly", "loudly", "gracefully", "happily", "silently",
                           "randomly", "brightly", "gently", "strongly", "awkwardly")
    }

    suspend fun generateSampleData(categoryId: Int) {
        withContext(Dispatchers.IO) {
            // Check if any topics already exist
            if (topicDao.anyTopicsExist(TOPIC_NAMES)) {
                return@withContext
            }

            // Generate 200 topics
            TOPIC_NAMES.forEachIndexed { topicIndex, topicName ->
                // Create topic
                val topic = createTopic(categoryId, topicName, topicIndex)
                val topicId = topicDao.insert(topic).toInt()

                // Generate 2000 messages for this topic
                val messages = List(MESSAGES_PER_TOPIC) { messageIndex ->
                    createMessage(topicId, categoryId, messageIndex)
                }

                // Insert messages in chunks of 500
                val chunks = messages.chunked(500)
                for (chunk in chunks) {
                    messageDao.insertAll(chunk) // This is now in a coroutine body
                }
            }
        }
    }

    private fun createTopic(categoryId: Int, name: String, index: Int): TopicTbl {
        val now = System.currentTimeMillis()
        return TopicTbl(
            name = name,
            lastEditTime = now,
            createTime = now,
            colour = ThreadLocalRandom.current().nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt()),
            categoryId = categoryId,
            iconPath = "default_icon",
            priority = index + 1
        )
    }

    private fun createMessage(topicId: Int, categoryId: Int, messageIndex: Int): MessageTbl {
        val now = System.currentTimeMillis()
        return MessageTbl(
            topicId = topicId,
            content = generateMessageContent(messageIndex, topicId),
            lastEditTime = now,
            createTime = now,
            categoryId = categoryId,
            priority = messageIndex + 1,
            type = 0
        )
    }

    private fun generateMessageContent(messageIndex: Int, topicId: Int): String {
        val random = java.util.Random((topicId * MESSAGES_PER_TOPIC + messageIndex).toLong())
        val content = StringBuilder()
        var wordCount = 0

        while (wordCount < WORDS_PER_MESSAGE) {
            val sentence = buildSentence(random)
            val words = sentence.split(" ")

            if (wordCount + words.size > WORDS_PER_MESSAGE) {
                val remaining = WORDS_PER_MESSAGE - wordCount
                content.append(words.take(remaining).joinToString(" "))
                break
            }

            content.append(sentence).append(". ")
            wordCount += words.size
        }

        // Add unique identifier
        content.append("\n[UID: T$topicId-M$messageIndex-${System.currentTimeMillis()}]")
        return content.toString()
    }

    private fun buildSentence(random: java.util.Random): String {
        return when (random.nextInt(4)) {
            0 -> "${random.choice(adjectives)} ${random.choice(nouns)} " +
                 "${random.choice(verbs)} ${random.choice(adverbs)}"
            1 -> "${random.choice(nouns)} ${random.choice(verbs)} " +
                 "${random.choice(nouns)} ${random.choice(verbs)}"
            2 -> "${random.choice(adjectives)} ${random.choice(nouns)} " +
                 "${random.choice(verbs)}"
            else -> "${random.choice(verbs)} ${random.choice(nouns)} " +
                    "${random.choice(nouns)}"
        }
    }

    private fun <T> java.util.Random.choice(list: List<T>): T {
        return list[this.nextInt(list.size)]
    }
}
