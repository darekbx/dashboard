package com.darekbx.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import com.darekbx.dashboard.model.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(): ViewModel() {

    fun readSettings() = Settings(15000L)
}