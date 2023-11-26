package com.example.posingottae.ui.socialmedia

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.posingottae.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KoGPTActivity : AppCompatActivity() {

    private lateinit var chatTextView: TextView
    private lateinit var editText: EditText
    private lateinit var sendButton: Button
    private lateinit var kogptApiHandler: KoGPTApiHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ko_gptactivity)

        chatTextView = findViewById(R.id.chatTextView)
        editText = findViewById(R.id.editText)
        sendButton = findViewById(R.id.sendButton)
        val restApiKey = "be786f93fe75774da70538d4475f07a4"
        kogptApiHandler = KoGPTApiHandler(restApiKey)

        sendButton.setOnClickListener {
            val userInput = editText.text.toString()
            if (userInput.isNotBlank()) {
                appendMessage("You: $userInput")
                editText.text.clear()

                // KoGPT API 호출
                GlobalScope.launch(Dispatchers.IO) {
                    val kogptResponse = kogptApiHandler.performKoGPTApiCall(userInput)
                    withContext(Dispatchers.Main) {
                        // UI 스레드에서 챗봇 응답을 추가
                        appendMessage("Chatbot: ${kogptResponse.text}")
                    }
                }
            }

        }
    }

    private fun appendMessage(message: String) {
        chatTextView.append("$message\n\n")
    }
}