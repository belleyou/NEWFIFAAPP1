package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

enum class HudDimension {
    COUNTRY,
    CITY,
    SPORT,
    STADIUM
}

data class CountryHudItem(
    val name: String,
    val abbrev: String,
    val flag: String,
    val lat: Double,
    val lon: Double,
    val premierLeague: String
)

data class CityHudItem(
    val name: String,
    val country: String,
    val flag: String,
    val stadiumName: String,
    val stadiumId: String,
    val lat: Double,
    val lon: Double,
    val sport: String,
    val date: String
)

data class SportHudItem(
    val name: String,
    val icon: String,
    val league: String,
    val venueName: String,
    val venueId: String,
    val city: String,
    val country: String,
    val flag: String,
    val lat: Double,
    val lon: Double
)

data class StadiumHudItem(
    val name: String,
    val id: String,
    val city: String,
    val country: String,
    val flag: String,
    val capacity: String,
    val sport: String,
    val lat: Double,
    val lon: Double
)

object HudDataProvider {
    val countries: List<CountryHudItem> = listOf(
        CountryHudItem("United States", "USA", "🇺🇸", 38.9072, -77.0369, "WNBA / NWSL"),
        CountryHudItem("Brazil", "BRA", "🇧🇷", -15.7938, -47.8827, "2027 WWC Host"),
        CountryHudItem("England", "ENG", "🏴󠁧󠁢󠁥󠁮󠁧󠁿", 51.5074, -0.1278, "Wembley / WSL"),
        CountryHudItem("Spain", "ESP", "🇪🇸", 40.4168, -3.7038, "Liga F / World Champs"),
        CountryHudItem("France", "FRA", "🇫🇷", 48.8566, 2.3522, "Roland Garros / D1"),
        CountryHudItem("Australia", "AUS", "🇦🇺", -35.2809, 149.1300, "Matildas / Aus Open"),
        CountryHudItem("Japan", "JPN", "🇯🇵", 35.6762, 139.6503, "WE League"),
        CountryHudItem("Germany", "GER", "🇩🇪", 52.5200, 13.4050, "Frauen-Bundesliga"),
        CountryHudItem("Canada", "CAN", "🇨🇦", 45.4215, -75.6972, "PWHL / NSL"),
        CountryHudItem("Sweden", "SWE", "🇸🇪", 59.3293, 18.0686, "Damallsvenskan")
    )

    val cities: List<CityHudItem> = listOf(
        CityHudItem("Seattle", "USA", "🇺🇸", "Climate Pledge Arena", "STAD_CLIMATE", 47.6221, -122.3540, "WNBA", "Jul 18"),
        CityHudItem("New York", "USA", "🇺🇸", "Barclays Center", "STAD_BARCLAYS", 40.6826, -73.9754, "WNBA", "Jul 20"),
        CityHudItem("Indianapolis", "USA", "🇺🇸", "Gainbridge Fieldhouse", "STAD_GAINBRIDGE", 39.7640, -86.1555, "WNBA", "Jul 22"),
        CityHudItem("Las Vegas", "USA", "🇺🇸", "Michelob ULTRA Arena", "STAD_MICHELOB", 36.0919, -115.1764, "WNBA", "Jul 24"),
        CityHudItem("Kansas City", "USA", "🇺🇸", "CPKC Stadium", "STAD_CPKC", 39.1172, -94.5772, "NWSL", "Aug 02"),
        CityHudItem("Los Angeles", "USA", "🇺🇸", "BMO Stadium", "STAD_BMO_LA", 34.0128, -118.2849, "NWSL", "Aug 05"),
        CityHudItem("Portland", "USA", "🇺🇸", "Providence Park", "STAD_PROVIDENCE_PK", 45.5216, -122.6917, "NWSL", "Aug 10"),
        CityHudItem("San Diego", "USA", "🇺🇸", "Snapdragon Stadium", "STAD_SNAPDRAGON", 32.7844, -117.1224, "NWSL", "Aug 14"),
        CityHudItem("Flushing", "USA", "🇺🇸", "Arthur Ashe Stadium", "STAD_ARTHUR_ASHE", 40.7499, -73.8466, "Tennis", "Aug 28"),
        CityHudItem("London", "UK", "🇬🇧", "Wembley Stadium", "STAD_WEMBLEY", 51.5560, -0.2796, "Soccer", "Sep 05"),
        CityHudItem("Paris", "France", "🇫🇷", "Philippe-Chatrier", "STAD_ROLAND_GARROS", 48.8471, 2.2498, "Tennis", "Sep 12"),
        CityHudItem("Rio de Janeiro", "Brazil", "🇧🇷", "Maracanã Stadium", "STAD_MARACANA", -22.9122, -43.2302, "Soccer", "Oct 01"),
        CityHudItem("Melbourne", "Australia", "🇦🇺", "Rod Laver Arena", "STAD_ROD_LAVER", -37.8216, 144.9785, "Tennis", "Oct 15"),
        CityHudItem("Minneapolis", "USA", "🇺🇸", "Xcel Energy Center", "STAD_XCEL", 44.9448, -93.1011, "PWHL", "Nov 01"),
        CityHudItem("Omaha", "USA", "🇺🇸", "CHI Health Center", "STAD_CHI_HEALTH", 41.2625, -95.9288, "Volleyball", "Nov 15")
    )

    val sports: List<SportHudItem> = listOf(
        SportHudItem("Basketball", "🏀", "WNBA", "Barclays Center", "STAD_BARCLAYS", "New York", "USA", "🇺🇸", 40.6826, -73.9754),
        SportHudItem("Soccer", "⚽", "NWSL / FIFA", "CPKC Stadium", "STAD_CPKC", "Kansas City", "USA", "🇺🇸", 39.1172, -94.5772),
        SportHudItem("Tennis", "🎾", "WTA / Slam", "Arthur Ashe Stadium", "STAD_ARTHUR_ASHE", "Flushing", "USA", "🇺🇸", 40.7499, -73.8466),
        SportHudItem("Ice Hockey", "🏒", "PWHL", "Xcel Energy Center", "STAD_XCEL", "St. Paul", "USA", "🇺🇸", 44.9448, -93.1011),
        SportHudItem("Volleyball", "🏐", "LOVB / PVF", "CHI Health Center", "STAD_CHI_HEALTH", "Omaha", "USA", "🇺🇸", 41.2625, -95.9288),
        SportHudItem("Cricket", "🏏", "WPL", "Brabourne Stadium", "STAD_BRABOURNE", "Mumbai", "India", "🇮🇳", 18.9322, 72.8264)
    )

    val stadiums: List<StadiumHudItem> = listOf(
        StadiumHudItem("Barclays Center", "STAD_BARCLAYS", "New York", "USA", "🇺🇸", "17,732", "Basketball", 40.6826, -73.9754),
        StadiumHudItem("CPKC Stadium", "STAD_CPKC", "Kansas City", "USA", "🇺🇸", "11,500", "Soccer", 39.1172, -94.5772),
        StadiumHudItem("Climate Pledge Arena", "STAD_CLIMATE", "Seattle", "USA", "🇺🇸", "18,100", "Basketball", 47.6221, -122.3540),
        StadiumHudItem("Arthur Ashe Stadium", "STAD_ARTHUR_ASHE", "Flushing", "USA", "🇺🇸", "23,771", "Tennis", 40.7499, -73.8466),
        StadiumHudItem("Gainbridge Fieldhouse", "STAD_GAINBRIDGE", "Indianapolis", "USA", "🇺🇸", "17,274", "Basketball", 39.7640, -86.1555),
        StadiumHudItem("Michelob ULTRA Arena", "STAD_MICHELOB", "Las Vegas", "USA", "🇺🇸", "12,000", "Basketball", 36.0919, -115.1764),
        StadiumHudItem("BMO Stadium", "STAD_BMO_LA", "Los Angeles", "USA", "🇺🇸", "22,000", "Soccer", 34.0128, -118.2849),
        StadiumHudItem("Providence Park", "STAD_PROVIDENCE_PK", "Portland", "USA", "🇺🇸", "25,218", "Soccer", 45.5216, -122.6917),
        StadiumHudItem("Snapdragon Stadium", "STAD_SNAPDRAGON", "San Diego", "USA", "🇺🇸", "35,000", "Soccer", 32.7844, -117.1224),
        StadiumHudItem("Xcel Energy Center", "STAD_XCEL", "St. Paul", "USA", "🇺🇸", "18,064", "Ice Hockey", 44.9448, -93.1011),
        StadiumHudItem("CHI Health Center", "STAD_CHI_HEALTH", "Omaha", "USA", "🇺🇸", "18,320", "Volleyball", 41.2625, -95.9288),
        StadiumHudItem("Wembley Stadium", "STAD_WEMBLEY", "London", "UK", "🇬🇧", "90,000", "Soccer", 51.5560, -0.2796),
        StadiumHudItem("Maracanã Stadium", "STAD_MARACANA", "Rio de Janeiro", "Brazil", "🇧🇷", "78,838", "Soccer", -22.9122, -43.2302),
        StadiumHudItem("Philippe-Chatrier", "STAD_ROLAND_GARROS", "Paris", "France", "🇫🇷", "15,225", "Tennis", 48.8471, 2.2498),
        StadiumHudItem("Rod Laver Arena", "STAD_ROD_LAVER", "Melbourne", "Australia", "🇦🇺", "14,820", "Tennis", -37.8216, 144.9785),
        StadiumHudItem("Brabourne Stadium", "STAD_BRABOURNE", "Mumbai", "India", "🇮🇳", "20,000", "Cricket", 18.9322, 72.8264)
    )
}

/**
 * WomenSportsRotatingHud
 *
 * Implements the sleek dark futuristic HUD concept from the reference design.
 * Users can switch between Country, City, Sport, and Stadium using the tactile prev/next
 * buttons or by tapping directly on the Country slot, Hero Dial, Sport slot, or mode tabs.
 * Rotating the HUD animates and focuses the 3D globe to the exact coordinates.
 */
@Composable
fun WomenSportsRotatingHud(
    onRotateToCoordinates: (lat: Double, lon: Double, zoom: Float) -> Unit,
    onCountrySelected: (CountryHudItem) -> Unit,
    onCitySelected: (CityHudItem) -> Unit,
    onSportSelected: (SportHudItem) -> Unit,
    onStadiumSelected: ((StadiumHudItem) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var activeDimension by remember { mutableStateOf(HudDimension.CITY) }
    var countryIndex by remember { mutableIntStateOf(0) }
    var cityIndex by remember { mutableIntStateOf(0) }
    var sportIndex by remember { mutableIntStateOf(0) }
    var stadiumIndex by remember { mutableIntStateOf(0) }

    val currentCountry = HudDataProvider.countries.getOrElse(countryIndex) { HudDataProvider.countries[0] }
    val currentCity = HudDataProvider.cities.getOrElse(cityIndex) { HudDataProvider.cities[0] }
    val currentSport = HudDataProvider.sports.getOrElse(sportIndex) { HudDataProvider.sports[0] }
    val currentStadium = HudDataProvider.stadiums.getOrElse(stadiumIndex) { HudDataProvider.stadiums[0] }

    fun triggerRotation() {
        when (activeDimension) {
            HudDimension.COUNTRY -> {
                onCountrySelected(currentCountry)
                onRotateToCoordinates(currentCountry.lat, currentCountry.lon, 2.2f)
            }
            HudDimension.CITY -> {
                onCitySelected(currentCity)
                onRotateToCoordinates(currentCity.lat, currentCity.lon, 2.6f)
            }
            HudDimension.SPORT -> {
                onSportSelected(currentSport)
                onRotateToCoordinates(currentSport.lat, currentSport.lon, 2.4f)
            }
            HudDimension.STADIUM -> {
                onStadiumSelected?.invoke(currentStadium)
                onRotateToCoordinates(currentStadium.lat, currentStadium.lon, 2.7f)
            }
        }
    }

    fun stepPrevious() {
        when (activeDimension) {
            HudDimension.COUNTRY -> {
                countryIndex = if (countryIndex > 0) countryIndex - 1 else HudDataProvider.countries.size - 1
            }
            HudDimension.CITY -> {
                cityIndex = if (cityIndex > 0) cityIndex - 1 else HudDataProvider.cities.size - 1
            }
            HudDimension.SPORT -> {
                sportIndex = if (sportIndex > 0) sportIndex - 1 else HudDataProvider.sports.size - 1
            }
            HudDimension.STADIUM -> {
                stadiumIndex = if (stadiumIndex > 0) stadiumIndex - 1 else HudDataProvider.stadiums.size - 1
            }
        }
        triggerRotation()
    }

    fun stepNext() {
        when (activeDimension) {
            HudDimension.COUNTRY -> {
                countryIndex = (countryIndex + 1) % HudDataProvider.countries.size
            }
            HudDimension.CITY -> {
                cityIndex = (cityIndex + 1) % HudDataProvider.cities.size
            }
            HudDimension.SPORT -> {
                sportIndex = (sportIndex + 1) % HudDataProvider.sports.size
            }
            HudDimension.STADIUM -> {
                stadiumIndex = (stadiumIndex + 1) % HudDataProvider.stadiums.size
            }
        }
        triggerRotation()
    }

    // Colors matching the GolfX dark emerald & neon lime styling
    val neonLime = Color(0xFF76FF03)
    val neonEmerald = Color(0xFF00E676)
    val podDarkBg = Color(0xFF0D1611)
    val podBorderColor = Color(0xFF2E4C38)
    val inactiveTextColor = Color(0xFF94A3B8)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // MAIN POD CONTAINER (Dark curved console floating above bottom)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(32.dp),
                    clip = false
                )
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF16251C),
                            podDarkBg
                        )
                    ),
                    shape = RoundedCornerShape(32.dp)
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            neonLime.copy(alpha = 0.55f),
                            podBorderColor.copy(alpha = 0.35f)
                        )
                    ),
                    shape = RoundedCornerShape(32.dp)
                )
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 1. LEFT CIRCULAR PREV ARROW (<)
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1B2E22))
                        .border(1.dp, neonLime.copy(alpha = 0.25f), CircleShape)
                        .clickable { stepPrevious() }
                        .testTag("hud_prev_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = "Previous Destination",
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier
                            .size(18.dp)
                            .offset(x = 2.dp)
                    )
                }

                // 2. LEFT SLOT: COUNTRY (Annotated in user reference)
                val isCountryActive = activeDimension == HudDimension.COUNTRY
                val countryAnimColor by animateColorAsState(
                    targetValue = if (isCountryActive) neonLime else Color.White,
                    animationSpec = tween(250),
                    label = "countryColor"
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            activeDimension = HudDimension.COUNTRY
                            triggerRotation()
                        }
                        .padding(horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedContent(
                        targetState = currentCountry,
                        transitionSpec = {
                            fadeIn(tween(250)) + slideInVertically { it / 2 } togetherWith
                                    fadeOut(tween(200)) + slideOutVertically { -it / 2 }
                        },
                        label = "CountryAnim"
                    ) { country ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = country.flag,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = country.abbrev,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = countryAnimColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (isCountryActive) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .background(neonLime, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                        }
                        Text(
                            text = "COUNTRY",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCountryActive) neonLime else inactiveTextColor,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // 3. CENTER HERO 3D DIAL (City / Date / Stadium)
                val isCityActive = activeDimension == HudDimension.CITY
                val isStadiumActive = activeDimension == HudDimension.STADIUM
                val isDialActive = isCityActive || isStadiumActive
                val dialPulseScale by animateFloatAsState(
                    targetValue = if (isDialActive) 1.05f else 0.98f,
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                    label = "dialScale"
                )

                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .graphicsLayer {
                            scaleX = dialPulseScale
                            scaleY = dialPulseScale
                        }
                        .clip(CircleShape)
                        .shadow(elevation = 12.dp, shape = CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF243B2B),
                                    Color(0xFF0F1A12)
                                )
                            )
                        )
                        .border(
                            width = if (isDialActive) 2.5.dp else 1.5.dp,
                            brush = Brush.sweepGradient(
                                listOf(
                                    neonLime,
                                    neonEmerald,
                                    Color(0xFF00B0FF),
                                    neonLime
                                )
                            ),
                            shape = CircleShape
                        )
                        .clickable {
                            if (!isDialActive) {
                                activeDimension = HudDimension.CITY
                            }
                            triggerRotation()
                        }
                        .testTag("hud_hero_dial"),
                    contentAlignment = Alignment.Center
                ) {
                    // Honeycomb mesh background lines drawn onto dial canvas
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val centerX = size.width / 2f
                        val centerY = size.height / 2f
                        val radius = size.width * 0.42f

                        // Draw subtle hex/concentric guides
                        for (step in 1..3) {
                            val r = radius * (step / 3f)
                            drawCircle(
                                color = Color.White.copy(alpha = 0.04f * step),
                                radius = r,
                                center = Offset(centerX, centerY)
                            )
                        }

                        // Little accent ticks on border
                        for (i in 0 until 12) {
                            val angle = (i * 30f) * (PI / 180f).toFloat()
                            val tickStart = Offset(
                                centerX + (radius - 4.dp.toPx()) * cos(angle),
                                centerY + (radius - 4.dp.toPx()) * sin(angle)
                            )
                            val tickEnd = Offset(
                                centerX + radius * cos(angle),
                                centerY + radius * sin(angle)
                            )
                            drawLine(
                                color = if (i % 3 == 0) neonLime.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.15f),
                                start = tickStart,
                                end = tickEnd,
                                strokeWidth = if (i % 3 == 0) 1.5.dp.toPx() else 1.dp.toPx()
                            )
                        }
                    }

                    // Content inside dial
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    ) {
                        AnimatedContent(
                            targetState = if (isStadiumActive) {
                                Pair(currentStadium.name, "${currentStadium.flag} ${currentStadium.city} • ${currentStadium.sport}")
                            } else {
                                Pair(currentCity.name, "${currentCity.sport} • ${currentCity.date}")
                            },
                            transitionSpec = {
                                fadeIn(tween(250)) + slideInVertically { it / 2 } togetherWith
                                        fadeOut(tween(200)) + slideOutVertically { -it / 2 }
                            },
                            label = "DialAnim"
                        ) { (primaryText, secondaryText) ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = primaryText,
                                    fontSize = if (primaryText.length > 12) 11.sp else 13.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.SansSerif,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = secondaryText,
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = neonLime,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = if (isStadiumActive) "STADIUM" else "CITY",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            color = if (isDialActive) neonLime else inactiveTextColor
                        )
                    }
                }

                // 4. RIGHT SLOT: SPORT (Annotated in user reference)
                val isSportActive = activeDimension == HudDimension.SPORT
                val sportAnimColor by animateColorAsState(
                    targetValue = if (isSportActive) neonLime else Color.White,
                    animationSpec = tween(250),
                    label = "sportColor"
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            activeDimension = HudDimension.SPORT
                            triggerRotation()
                        }
                        .padding(horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedContent(
                        targetState = currentSport,
                        transitionSpec = {
                            fadeIn(tween(250)) + slideInVertically { it / 2 } togetherWith
                                    fadeOut(tween(200)) + slideOutVertically { -it / 2 }
                        },
                        label = "SportAnim"
                    ) { sport ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = sport.icon,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = sport.league,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = sportAnimColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (isSportActive) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .background(neonLime, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                        }
                        Text(
                            text = "SPORT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSportActive) neonLime else inactiveTextColor,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // 5. RIGHT CIRCULAR NEXT ARROW (>)
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1B2E22))
                        .border(1.dp, neonLime.copy(alpha = 0.25f), CircleShape)
                        .clickable { stepNext() }
                        .testTag("hud_next_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Next Destination",
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier
                            .size(18.dp)
                            .offset(x = 1.dp)
                    )
                }
            }
        }

        // BOTTOM MODE SELECTOR CAPSULE TABS (Course / Coach AI / Statistics style from reference)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF09120C))
                .border(1.dp, Color(0xFF1E3324), RoundedCornerShape(20.dp))
                .padding(vertical = 4.dp, horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HudTabItem(
                title = "Country",
                icon = "🌎",
                isSelected = activeDimension == HudDimension.COUNTRY,
                activeColor = neonLime,
                onClick = {
                    activeDimension = HudDimension.COUNTRY
                    triggerRotation()
                }
            )

            HudTabItem(
                title = "City",
                icon = "📍",
                isSelected = activeDimension == HudDimension.CITY,
                activeColor = neonLime,
                onClick = {
                    activeDimension = HudDimension.CITY
                    triggerRotation()
                }
            )

            HudTabItem(
                title = "Sport",
                icon = "🏆",
                isSelected = activeDimension == HudDimension.SPORT,
                activeColor = neonLime,
                onClick = {
                    activeDimension = HudDimension.SPORT
                    triggerRotation()
                }
            )

            HudTabItem(
                title = "Stadium",
                icon = "🏟️",
                isSelected = activeDimension == HudDimension.STADIUM,
                activeColor = neonLime,
                onClick = {
                    activeDimension = HudDimension.STADIUM
                    triggerRotation()
                }
            )
        }
    }
}

@Composable
private fun HudTabItem(
    title: String,
    icon: String,
    isSelected: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = icon, fontSize = 12.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                color = if (isSelected) Color.White else Color(0xFF94A3B8)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Neon active indicator line (matching the reference GolfX tabs)
        Box(
            modifier = Modifier
                .width(28.dp)
                .height(3.dp)
                .background(
                    if (isSelected) activeColor else Color.Transparent,
                    RoundedCornerShape(2.dp)
                )
        )
    }
}
