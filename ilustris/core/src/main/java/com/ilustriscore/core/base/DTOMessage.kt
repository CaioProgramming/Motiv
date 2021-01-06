package com.ilustriscore.core.base


import com.ilustriscore.core.utilities.ErrorType
import com.ilustriscore.core.utilities.MessageType
import com.ilustriscore.core.utilities.OperationType

data class DTOMessage(val message: String, val type: MessageType, val errorType: ErrorType? = null, val operationType: OperationType = OperationType.UNKNOW)