package com.example.contentmanager

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.content.FileProvider
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonRetrieve: Button
    private lateinit var openButton: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.edit_text)
        buttonSave = findViewById(R.id.button_save)
        buttonRetrieve = findViewById(R.id.button_retrieve)
        openButton = findViewById(R.id.button_open_log)

        buttonSave.setOnClickListener {
            saveTextToFile(editText.text.toString())
        }

        buttonRetrieve.setOnClickListener {
            editText.setText(retrieveTextFromFile())
        }

        openButton.setOnClickListener {
            val logFile = getLogFile()
            val uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", logFile)

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "text/plain")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            val chooser = Intent.createChooser(intent, "Open log file with")
            startActivity(chooser)
        }
    }

    private fun getLogFile(): File {
        val dir = getExternalFilesDir(null)
        return File(dir, "log.txt")
    }


    private fun saveTextToFile(text: String) {
        val filename = "log_file.txt"
        val fileOutputStream: FileOutputStream
        try {
            val file = File(getExternalFilesDir(null), filename)
            fileOutputStream = FileOutputStream(file, true)
            val message = "$text\n"
            fileOutputStream.write(message.toByteArray())
            fileOutputStream.close()
            editText.text.clear()
            editText.hint = "Text saved to log file"
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun retrieveTextFromFile(): String {
        val filename = "log_file.txt"
        val fileInputStream: FileInputStream
        try {
            val file = File(getExternalFilesDir(null), filename)
            fileInputStream = FileInputStream(file)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder = StringBuilder()
            var text: String? = null
            while ({ text = bufferedReader.readLine(); text }() != null) {
                stringBuilder.append(text).append("\n")
            }
            fileInputStream.close()
            return stringBuilder.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}