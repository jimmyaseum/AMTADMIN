package com.app.amtadminapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.widget.EditText
import androidx.annotation.RequiresApi
import com.google.android.gms.auth.api.phone.SmsRetriever
import java.util.regex.Matcher
import java.util.regex.Pattern


class OTP_Receiver : BroadcastReceiver() {
    fun setEditText(editText1: EditText?, editText2: EditText?, editText3: EditText?, editText4: EditText?) {
        Companion.editText1 = editText1
        Companion.editText2 = editText2
        Companion.editText3 = editText3
        Companion.editText4 = editText4
    }

    // OnReceive will keep trace when sms is been received in mobile
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    override fun onReceive(context: Context, intent: Intent) {
        //message will be holding complete sms that is received

        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        for (sms in messages) {
            val msg = sms.messageBody
            // here we are splitting the sms using " : " symbol

            val PAT = "(is|with)\\s+(\\d+)"
            val pats: Pattern = Pattern.compile(PAT)
            val m: Matcher = pats.matcher(msg)
            while (m.find()) {
                val grp = m.group(2)
                editText1!!.setText(grp.get(0).toString())
                editText2!!.setText(grp.get(1).toString())
                editText3!!.setText(grp.get(2).toString())
                editText4!!.setText(grp.get(3).toString())
            }
        }
    }

    companion object {
        private var editText1: EditText? = null
        private var editText2: EditText? = null
        private var editText3: EditText? = null
        private var editText4: EditText? = null
    }
}

