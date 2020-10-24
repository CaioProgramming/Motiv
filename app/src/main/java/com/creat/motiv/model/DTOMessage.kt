package com.creat.motiv.model

import com.creat.motiv.utils.ErrorType
import com.creat.motiv.utils.MessageType

data class DTOMessage(val message: String, val type: MessageType, val errorType: ErrorType = ErrorType.UNKNOW)