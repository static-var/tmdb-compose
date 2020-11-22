package dev.shreyansh.tmdb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.ui.platform.setContent
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import dev.shreyansh.tmdb.ui.TmDBApp
import dev.shreyansh.tmdb.ui.TmdbViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: TmdbViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This app draws behind the system bars, so we want to handle fitting system windows
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent { TmDBApp(viewModel) }
    }
}