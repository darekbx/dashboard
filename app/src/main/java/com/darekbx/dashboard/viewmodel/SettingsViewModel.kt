package com.darekbx.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import com.darekbx.dashboard.model.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(): ViewModel() {

    fun readSettings() = Settings(TimeUnit.MINUTES.toMillis(30 /* TODO move to user settings*/))
}