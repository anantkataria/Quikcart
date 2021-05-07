package com.savage9ishere.tiwarimart.main_flow.ui.user.customer_support.chat

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatUser(
    val firstName: String,
    val username: String
) : Parcelable