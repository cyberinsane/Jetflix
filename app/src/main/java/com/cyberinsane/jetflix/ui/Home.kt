package com.cyberinsane.jetflix.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cyberinsane.jetflix.R
import com.cyberinsane.jetflix.data.model.Show
import com.cyberinsane.jetflix.data.model.getHeroImage
import com.cyberinsane.jetflix.data.model.getImage
import com.cyberinsane.jetflix.domain.model.TVCollection
import com.cyberinsane.jetflix.ui.theme.typography
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun Home(mainViewModel: MainViewModel, navController: NavHostController) {

    val viewState by mainViewModel.state.collectAsState()

    Surface(Modifier.fillMaxSize()) {
        MainContent(
            collection = viewState.tvCollection
        )
    }
}

@Composable
fun MainContent(collection: TVCollection?) {
    Column(modifier = Modifier.fillMaxSize()) {
        HomeAppBar(backgroundColor = Color.Black)
        ScrollableColumn {

            collection?.trending?.let {
                HeroContent(show = it.first())
                ShowsCarousel("Trending", it)
            }
            collection?.topRated?.let {
                ShowsCarousel("Top Rated", it)
            }
            collection?.popular?.let {
                ShowsCarousel("Popular", it)
            }

            Spacer(modifier = Modifier.preferredHeight(80.dp))
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
                    vectorResource(id = R.drawable.ic_launcher_foreground),
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
fun HeroContent(show: Show) {
    Box() {
        Column(modifier = Modifier.fillMaxWidth()) {
            CoilImage(
                data = show.getHeroImage(),
                modifier = Modifier.fillMaxWidth().preferredHeight(460.dp),
                contentScale = ContentScale.Crop,
                fadeIn = true
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Action • Sci-fi • Drama",
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 32.dp, end = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {}) {
                    Text(text = "List")
                }

                Button(onClick = {}) {
                    Text(text = "Play")
                }

                Button(onClick = {}) {
                    Text(text = "Info")
                }
            }
        }
    }


    // Image
    // + My List , Play >, Info (i)
}

@Composable
fun ShowsCarousel(title: String, shows: List<Show>) {
    Column() {
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
            CoilImage(
                data = show.getImage(),
                modifier = Modifier.preferredWidth(140.dp).preferredHeight(230.dp)
            )
        }
        Spacer(Modifier.preferredHeight(8.dp))
        show.name?.let {
            Text(
                text = show.name,
                style = typography.body2,
                maxLines = 1,
                modifier = Modifier.preferredWidth(140.dp)
            )
        }
    }
}