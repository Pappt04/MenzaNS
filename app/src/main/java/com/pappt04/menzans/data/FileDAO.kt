package com.pappt04.menzans.data

import android.app.NotificationManager
import android.content.Context
import androidx.core.content.ContextCompat
import com.pappt04.menzans.notifications.sendTopUpReminder

open class FileDAO(var context: Context, private var fileName:String) {

    protected var data:String= readFromFile()

    fun changeJob(con: Context,f:String)
    {
        data=""
        context=con
        fileName=f
    }

    fun getDAOContext(): Context
    {
        return context
    }

    fun getDAOFileName(): String
    {
        return fileName
    }

    fun getDAOData(): String
    {
        return data
    }

    fun saveToFile(remaining: Int, notify: Boolean=false) {
        val s1 = remaining.toString()
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(s1.toByteArray())
        }
        if (fileName in DummyData.FileNames && remaining <= DummyData.MINIMUM_TOKEN_TRESHOLD && notify) {
            val notificationManager = context.let {
                ContextCompat.getSystemService(
                    it,
                    NotificationManager::class.java
                )
            } as NotificationManager
            notificationManager.sendTopUpReminder(context, fileName, remaining)
        }
    }

    fun readFromFile(): String {
        var s1 = ""
        val files: Array<String> = context.fileList()
        if (fileName in files) {
            context.openFileInput(fileName).bufferedReader().useLines { lines ->
                lines.fold("") { some, text ->
                    s1 = "$some$text"
                    s1
                }
            }
        }
        return s1
    }
}