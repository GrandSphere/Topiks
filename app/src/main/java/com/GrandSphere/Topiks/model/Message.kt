package com.GrandSphere.Topiks.model

data class Message(val id: Int, val content: String)
data class MessageSearchContent(val id: Int, val content: String, val topicId: Int, val topicName: String)

