package com.example.topics2.model

data class Message(val sender: String, val text: String, val isUser: Boolean)
data class MessageSearchContent(val id: Int, val content: String, val topicId: Int)
