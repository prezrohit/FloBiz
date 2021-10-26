package com.flobiz.app.model

import androidx.lifecycle.MutableLiveData

class Tag(val name: String, var isChecked: MutableLiveData<Boolean>)