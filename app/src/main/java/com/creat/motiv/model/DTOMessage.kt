package com.creat.motiv.model

import com.creat.motiv.utilities.ErrorType
import com.creat.motiv.utilities.MessageType
import com.creat.motiv.utilities.OperationType

data class DTOMessage(val message: String, val type: MessageType, val errorType: ErrorType = ErrorType.UNKNOW, val operationType: OperationType = OperationType.UNKNOW)