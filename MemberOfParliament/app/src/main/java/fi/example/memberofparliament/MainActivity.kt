package fi.example.memberofparliament

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fi.example.memberofparliament.ui.theme.MemberOfParliamentTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MemberOfParliamentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MemberOfParliament()
                }
            }
        }
    }
}

@Composable
fun MemberOfParliament() {
    val images = listOf(
        painterResource(R.drawable.sannamarin),
        painterResource(R.drawable.petteriorpo),
        painterResource(R.drawable.alexanderstubb)
    )

    val names = listOf(
        stringResource(R.string.sannamarin),
        stringResource(R.string.petteriorpo),
        stringResource(R.string.alexanderstubb)

    )

    var currentIndex by remember { mutableIntStateOf(Random.nextInt(images.size)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = images[currentIndex],
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = names[currentIndex])
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            currentIndex = Random.nextInt(images.size)
        }) {
            Text("Next MP")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MemberOfParliamentPreview() {
    MemberOfParliamentTheme {
        MemberOfParliament()
    }
}



