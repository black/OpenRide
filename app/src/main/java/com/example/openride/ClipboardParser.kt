package com.example.openride

import android.content.ClipboardManager
import android.content.Context

class ClipboardParser {
    fun getClipboardText(context: Context): String? {

        val clipboard =
            context.getSystemService(Context.CLIPBOARD_SERVICE)
                    as ClipboardManager

        val item = clipboard.primaryClip?.getItemAt(0)

        return item?.text?.toString()
    }
}