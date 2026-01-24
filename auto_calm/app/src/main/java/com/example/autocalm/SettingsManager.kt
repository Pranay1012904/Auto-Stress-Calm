package com.example.autocalm

import android.content.Context
import android.content.SharedPreferences

data class VibrationSettings(
    val repeats: Int = 1,
    val maxIntensity: Int = 10, // 1-10, scales to 10-100%
    val minIntensity: Int = 2,  // 1-10, scales to 10-100%
    val segmentDuration: Int = 3 // seconds per phase (inhale/pause/exhale)
)

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("vibration_settings", Context.MODE_PRIVATE)

    fun saveSettings(settings: VibrationSettings) {
        prefs.edit().apply {
            putInt("repeats", settings.repeats)
            putInt("maxIntensity", settings.maxIntensity)
            putInt("minIntensity", settings.minIntensity)
            putInt("segmentDuration", settings.segmentDuration)
            apply()
        }
    }

    fun getSettings(): VibrationSettings {
        return VibrationSettings(
            repeats = prefs.getInt("repeats", 1).coerceIn(1, 10),
            maxIntensity = prefs.getInt("maxIntensity", 10).coerceIn(1, 10),
            minIntensity = prefs.getInt("minIntensity", 2).coerceIn(1, 10),
            segmentDuration = prefs.getInt("segmentDuration", 3).coerceIn(1, 10)
        )
    }

    companion object {
        fun generateWaveform(settings: VibrationSettings): Pair<LongArray, IntArray> {
            val phaseMs = (settings.segmentDuration * 1000L)
            
            // We use 10 steps for ramp up and ramp down
            val steps = 10
            val stepDuration = phaseMs / steps
            
            val minAmp = (settings.minIntensity * 25.5).toInt().coerceIn(0, 255)
            val maxAmp = (settings.maxIntensity * 25.5).toInt().coerceIn(0, 255)
            
            val timings = mutableListOf<Long>()
            val amplitudes = mutableListOf<Int>()
            
            repeat(settings.repeats) {
                // Inhale (Ramp Up)
                for (i in 0 until steps) {
                    timings.add(stepDuration)
                    val amp = minAmp + ((maxAmp - minAmp) * (i.toFloat() / (steps - 1))).toInt()
                    amplitudes.add(amp)
                }
                
                // Pause
                timings.add(phaseMs)
                amplitudes.add(0)
                
                // Exhale (Ramp Down)
                for (i in 0 until steps) {
                    timings.add(stepDuration)
                    val amp = maxAmp - ((maxAmp - minAmp) * (i.toFloat() / (steps - 1))).toInt()
                    amplitudes.add(amp)
                }
            }
            
            return Pair(timings.toLongArray(), amplitudes.toIntArray())
        }
    }
}
