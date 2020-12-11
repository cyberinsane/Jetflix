package com.cyberinsane.jetflix.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.cyberinsane.jetflix.R
import com.cyberinsane.jetflix.data.model.Show
import com.cyberinsane.jetflix.data.model.getImage
import com.cyberinsane.jetflix.domain.model.TVCollection
import com.cyberinsane.jetflix.ui.theme.typography
import dev.chrisbanes.accompanist.coil.CoilImage
import dev.chrisbanes.accompanist.insets.statusBarsHeight
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun Main() {
    val viewModel: MainViewModel = viewModel()

    val viewState by viewModel.state.collectAsState()

    Surface(Modifier.fillMaxSize()) {
        MainContent(
            collection = viewState.tvCollection
        )
    }
}

@Composable
fun MainContent(collection: TVCollection?) {
    Column(modifier = Modifier.fillMaxSize()) {

        val appBarColor = MaterialTheme.colors.surface.copy(alpha = 1f)

        // Draw a scrim over the status bar which matches the app bar
        Spacer(Modifier.background(appBarColor).fillMaxWidth().statusBarsHeight())

        HomeAppBar(backgroundColor = Color.Black)

        ScrollableColumn {

            collection?.trending?.let {
                ShowsCarousel("Trending", it)
            }

            collection?.topRated?.let {
                ShowsCarousel("Top Rated", it)
            }

            collection?.popular?.let {
                ShowsCarousel("Popular", it)
            }

        }
    }
}

@Composable
fun HomeAppBar(
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Row {
                Image(
                    asset = vectorResource(R.drawable.ic_launcher_foreground),
                    modifier = Modifier.preferredSize(48.dp)
                )
                Text(
                    text = stringResource(id = R.string.app_name),
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        },
        backgroundColor = backgroundColor,
        modifier = modifier
    )
}

@Composable
fun ShowsCarousel(title: String, shows: List<Show>) {
    Column(modifier = Modifier.preferredHeight(320.dp)) {
        Spacer(modifier = Modifier.padding(top = 16.dp))
        Text(
            text = title, modifier = Modifier.padding(start = 16.dp),
            style = typography.h6
        )
        LazyRowFor(items = shows) {
            ShowItem(show = it)
        }
    }
}

@Composable
fun ShowItem(show: Show) {
    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
        show.posterPath?.let {
            CoilImage(data = show.getImage(), modifier = Modifier.preferredWidth(140.dp))
        }
        Spacer(Modifier.preferredHeight(8.dp))
        show.name?.let {
            Text(
                text = show.name,
                style = typography.body2,
                maxLines = 2,
                modifier = Modifier.preferredWidth(140.dp)
            )
        }
    }
}