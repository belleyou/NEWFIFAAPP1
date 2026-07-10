package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Team
import com.example.model.TeamDataProvider
import com.example.service.GeminiService
import com.example.ui.theme.BrandOrangeRed
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.ui.viewinterop.AndroidView

// Visual Theme Variations enum
enum class GlobeTheme {
    GLASS_LIGHT,
    COSMIC_DARK
}

// Stage filtering enum
enum class TournamentStage(val label: String) {
    ALL("All 48"),
    ROUND_32("All 32"),
    ROUND_16("All 16"),
    QUARTER("Quarter-Finals")
}

data class ProjectedPoint(
    val team: Team,
    val screenX: Float,
    val screenY: Float,
    val rotatedZ: Float, // Used for hemispherical occlusion (visible if > 0)
    val isInteractive: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobeScreen() {
    val coroutineScope = rememberCoroutineScope()
    
    // Theme state (2 options for home/globe screen)
    var currentTheme by remember { mutableStateOf(GlobeTheme.GLASS_LIGHT) }
    
    // Stage Filter state
    var selectedStage by remember { mutableStateOf(TournamentStage.QUARTER) }
    
    // Globe position states
    var rotX by remember { mutableStateOf(0.4f) }
    var rotY by remember { mutableStateOf(0.8f) }
    var zoomScale by remember { mutableStateOf(1.0f) }
    
    // Selection state
    val teams = TeamDataProvider.teams
    var selectedTeam by remember { mutableStateOf<Team?>(teams.firstOrNull()) }
    var profileTab by remember { mutableStateOf(ProfileTab.OVERVIEW) }
    
    // Comparison drawer states
    var compareTeam1 by remember { mutableStateOf<Team?>(teams[0]) }
    var compareTeam2 by remember { mutableStateOf<Team?>(teams[1]) }
    var isCompareDrawerOpen by remember { mutableStateOf(false) }
    
    // AI Prediction / Decide For Me states
    var aiAnalysisText by remember { mutableStateOf<String?>(null) }
    var isAiLoading by remember { mutableStateOf(false) }

    // Pulsing animations for country pins
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseRadius"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    // Dynamic background brush based on selected variation
    val backgroundBrush = when (currentTheme) {
        GlobeTheme.GLASS_LIGHT -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFFE0F2F1), // Soft Light Mint Teal
                Color(0xFFF1F8E9), // Gentle organic light green
                Color(0xFFFFFFFF)
            )
        )
        GlobeTheme.COSMIC_DARK -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0F172A), // Deep Slate Navy
                Color(0xFF1E1E38), // Rich Indigo Space
                Color(0xFF020617)  // Deep abyss
            )
        )
    }

    val textColor = when (currentTheme) {
        GlobeTheme.GLASS_LIGHT -> Color(0xFF111827)
        GlobeTheme.COSMIC_DARK -> Color(0xFFF8FAFC)
    }

    val cardBgColor = when (currentTheme) {
        GlobeTheme.GLASS_LIGHT -> Color.White.copy(alpha = 0.85f)
        GlobeTheme.COSMIC_DARK -> Color(0xFF1E293B).copy(alpha = 0.9f)
    }

    val accentColor = BrandOrangeRed

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        // MAIN MOBILE LAYOUT
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // APP HEADER with Simulated Notch & Title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "🏆 FIFA 2026 WORLD CUP",
                        color = accentColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.SansSerif,
                        letterSpacing = 0.5.sp
                    )
                }

                // Theme switch toggle & compare drawer trigger
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            currentTheme = if (currentTheme == GlobeTheme.GLASS_LIGHT) GlobeTheme.COSMIC_DARK else GlobeTheme.GLASS_LIGHT
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = if (currentTheme == GlobeTheme.GLASS_LIGHT) Color.Black.copy(alpha = 0.05f) else Color.White.copy(alpha = 0.08f),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = if (currentTheme == GlobeTheme.GLASS_LIGHT) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = "Toggle Theme",
                            tint = if (currentTheme == GlobeTheme.GLASS_LIGHT) Color(0xFF111827) else Color(0xFFFCD34D)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    VsLogoButton(
                        onClick = { isCompareDrawerOpen = !isCompareDrawerOpen }
                    )
                }
            }

            // FILTER TABS (Round of 32 down to Finals)
            ScrollableTabRow(
                selectedTabIndex = selectedStage.ordinal,
                edgePadding = 0.dp,
                containerColor = Color.Transparent,
                divider = {},
                indicator = { tabPositions ->
                    if (tabPositions.isNotEmpty()) {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedStage.ordinal]),
                            color = accentColor
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                TournamentStage.entries.forEach { stage ->
                    Tab(
                        selected = selectedStage == stage,
                        onClick = {
                            selectedStage = stage
                        },
                        text = {
                            Text(
                                text = stage.label,
                                fontSize = 12.sp,
                                fontWeight = if (selectedStage == stage) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedStage == stage) accentColor else textColor.copy(alpha = 0.6f)
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // MAIN CONTENT AREA: 3D GLOBE / SCREEN VARIATIONS
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                InteractiveThreeJsGlobe(
                    selectedTeam = selectedTeam,
                    onTeamSelected = { selectedTeam = it },
                    theme = currentTheme,
                    stage = selectedStage,
                    zoomScale = zoomScale,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .testTag("interactive_3d_globe")
                )
            }

            @Composable
            fun LegacyGlobeCanvasHidden(
                rotXState: Float,
                rotYState: Float,
                zoomScaleState: Float,
                selectedStageState: TournamentStage,
                selectedTeamState: Team?,
                currentThemeState: GlobeTheme,
                textColorState: Color,
                accentColorState: Color,
                cardBgColorState: Color,
                pulseAlphaState: Float,
                pulseRadiusState: Float,
                teamsList: List<Team>,
                onTeamSelectedCallback: (Team?) -> Unit
            ) {
                var rotX = rotXState
                var rotY = rotYState
                var zoomScale = zoomScaleState
                val selectedStage = selectedStageState
                var selectedTeam = selectedTeamState
                val currentTheme = currentThemeState
                val textColor = textColorState
                val accentColor = accentColorState
                val cardBgColor = cardBgColorState
                val pulseAlpha = pulseAlphaState
                val pulseRadius = pulseRadiusState
                val teams = teamsList
                var onTeamSelected = onTeamSelectedCallback

                // 3D GLOBE CANVAS WITH MAPPED DOTS
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val width = constraints.maxWidth.toFloat()
                    val height = constraints.maxHeight.toFloat()
                    val sizePx = minOf(width, height)
                    
                    // Sphere parameters
                    val baseRadius = sizePx * 0.35f
                    val currentRadius = baseRadius * zoomScale

                    // Project the points
                    val projectedPoints = remember(rotX, rotY, zoomScale, currentRadius, width, height, selectedStage) {
                        teams.mapNotNull { team ->
                            // Map logic: filter based on current Stage selection
                            val isMatchInSelectedStage = when (selectedStage) {
                                TournamentStage.ALL -> true
                                TournamentStage.ROUND_32 -> team.abbreviation in listOf("ARG", "FRA", "ESP", "BRA", "ENG", "USA", "MEX", "CAN", "GER", "ITA", "POR", "NED", "BEL", "CRO", "URU", "COL", "MAR", "SEN", "JPN", "KOR", "AUS", "SUI", "DEN", "UKR", "POL", "AUT", "ECU", "CHI", "NGA", "EGY", "CMR", "TUR")
                                TournamentStage.ROUND_16 -> team.abbreviation in listOf("ARG", "FRA", "ESP", "BRA", "ENG", "USA", "MEX", "CAN", "GER", "ITA", "POR", "NED", "BEL", "CRO", "URU", "COL")
                                TournamentStage.QUARTER -> team.abbreviation in listOf("ARG", "FRA", "ESP", "BRA", "ENG", "USA", "MEX", "CAN")
                            }

                            // Convert spherical latitude/longitude to radians
                            val latRad = Math.toRadians(team.latitude).toFloat()
                            val lonRad = Math.toRadians(team.longitude).toFloat()

                            // Base coordinates on 3D sphere
                            val x = currentRadius * cos(latRad) * sin(lonRad)
                            val y = -currentRadius * sin(latRad)
                            val z = currentRadius * cos(latRad) * cos(lonRad)

                            // Rotate about Y axis (horizontal drag)
                            val cosY = cos(rotY)
                            val sinY = sin(rotY)
                            val rx1 = x * cosY + z * sinY
                            val rz1 = -x * sinY + z * cosY
                            val ry1 = y

                            // Rotate about X axis (vertical tilt)
                            val cosX = cos(rotX)
                            val sinX = sin(rotX)
                            val rx2 = rx1
                            val ry2 = ry1 * cosX - rz1 * sinX
                            val rz2 = ry1 * sinX + rz1 * cosX

                            // Screen projection
                            val screenX = width / 2f + rx2
                            val screenY = height / 2f + ry2

                            ProjectedPoint(
                                team = team,
                                screenX = screenX,
                                screenY = screenY,
                                rotatedZ = rz2,
                                isInteractive = isMatchInSelectedStage
                            )
                        }
                    }

                    // Globe drawing Canvas
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    rotY += dragAmount.x * 0.005f
                                    rotX = (rotX - dragAmount.y * 0.005f).coerceIn(-1.2f, 1.2f)
                                }
                            }
                            .pointerInput(projectedPoints) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    rotY += dragAmount.x * 0.005f
                                    rotX = (rotX - dragAmount.y * 0.005f).coerceIn(-1.2f, 1.2f)
                                }
                            }
                            .pointerInput(projectedPoints) {
                                // CLICK COUNTRY PIN DETECTION
                                detectTapGestures { tapOffset ->
                                    var closestPoint: ProjectedPoint? = null
                                    var minDistance = Float.MAX_VALUE
                                    
                                    // Match point within visible hemisphere
                                    projectedPoints.forEach { point ->
                                        if (point.rotatedZ > 0 && point.isInteractive) {
                                            val dx = point.screenX - tapOffset.x
                                            val dy = point.screenY - tapOffset.y
                                            val dist = sqrt((dx * dx + dy * dy).toDouble()).toFloat()
                                            if (dist < minDistance) {
                                                minDistance = dist
                                                closestPoint = point
                                            }
                                        }
                                    }
                                    
                                    // 40dp radius click detection
                                    if (minDistance < 100f && closestPoint != null) {
                                        selectedTeam = closestPoint!!.team
                                    }
                                }
                            }
                    ) {
                        val centerX = width / 2f
                        val centerY = height / 2f

                        // 1. Draw a beautiful background glow representing atmosphere
                        // User request: "If the app is in a dark mode, the globe should be in the light color."
                        // We use the light globe colors (teal/white/emerald) for both themes to ensure great visibility.
                        val glowBrush = Brush.radialGradient(
                            colors = listOf(Color(0xFF80CBC4).copy(alpha = 0.25f), Color.Transparent),
                            center = Offset(centerX, centerY),
                            radius = currentRadius * 1.5f
                        )
                        drawCircle(brush = glowBrush, radius = currentRadius * 1.5f, center = Offset(centerX, centerY))

                        // 2. Draw the shaded Base Sphere Circle
                        val sphereColor = Color.White.copy(alpha = 0.5f)
                        drawCircle(color = sphereColor, radius = currentRadius, center = Offset(centerX, centerY))

                        // 3. Draw outer atmospheric ring
                        val ringColor = Color(0xFF00BFA5).copy(alpha = 0.4f)
                        drawCircle(
                            color = ringColor,
                            radius = currentRadius,
                            center = Offset(centerX, centerY),
                            style = Stroke(width = 2.dp.toPx())
                        )

                        // 4. DRAW 3D GRIDLINES (PARALLELS & MERIDIANS)
                        val gridLineColor = Color(0xFF00BFA5).copy(alpha = 0.15f)

                        // Parallels (latitude lines at -60, -30, 0, 30, 60)
                        val parallels = listOf(-60f, -30f, 0f, 30f, 60f)
                        parallels.forEach { latDeg ->
                            val latRad = Math.toRadians(latDeg.toDouble()).toFloat()
                            val parallelRadius = currentRadius * cos(latRad)
                            val heightOffset = -currentRadius * sin(latRad)

                            // Sample 36 points around parallel circle
                            var lastPoint: Offset? = null
                            var firstPoint: Offset? = null

                            for (i in 0..36) {
                                val lonDeg = i * 10f
                                val lonRad = Math.toRadians(lonDeg.toDouble()).toFloat()

                                val x = parallelRadius * sin(lonRad)
                                val y = heightOffset
                                val z = parallelRadius * cos(lonRad)

                                // Rotate Y
                                val rx1 = x * cos(rotY) + z * sin(rotY)
                                val rz1 = -x * sin(rotY) + z * cos(rotY)

                                // Rotate X
                                val rx2 = rx1
                                val ry2 = y * cos(rotX) - rz1 * sin(rotX)
                                val rz2 = y * sin(rotX) + rz1 * cos(rotX)

                                if (rz2 > 0) { // Visible on front hemisphere
                                    val pointOffset = Offset(centerX + rx2, centerY + ry2)
                                    if (lastPoint != null) {
                                        drawLine(
                                            color = gridLineColor,
                                            start = lastPoint,
                                            end = pointOffset,
                                            strokeWidth = 1.dp.toPx()
                                        )
                                    } else {
                                        firstPoint = pointOffset
                                    }
                                    lastPoint = pointOffset
                                } else {
                                    lastPoint = null
                                }
                            }
                        }

                        // Meridians (longitude lines)
                        val meridians = listOf(-120f, -60f, 0f, 60f, 120f, 180f)
                        meridians.forEach { lonDeg ->
                            val lonRad = Math.toRadians(lonDeg.toDouble()).toFloat()

                            var lastPoint: Offset? = null
                            for (i in -18..18) {
                                val latDeg = i * 5f
                                val latRad = Math.toRadians(latDeg.toDouble()).toFloat()

                                val x = currentRadius * cos(latRad) * sin(lonRad)
                                val y = -currentRadius * sin(latRad)
                                val z = currentRadius * cos(latRad) * cos(lonRad)

                                // Rotate Y
                                val rx1 = x * cos(rotY) + z * sin(rotY)
                                val rz1 = -x * sin(rotY) + z * cos(rotY)

                                // Rotate X
                                val rx2 = rx1
                                val ry2 = y * cos(rotX) - rz1 * sin(rotX)
                                val rz2 = y * sin(rotX) + rz1 * cos(rotX)

                                if (rz2 > 0) {
                                    val pointOffset = Offset(centerX + rx2, centerY + ry2)
                                    if (lastPoint != null) {
                                        drawLine(
                                            color = gridLineColor,
                                            start = lastPoint,
                                            end = pointOffset,
                                            strokeWidth = 1.dp.toPx()
                                        )
                                    }
                                    lastPoint = pointOffset
                                } else {
                                    lastPoint = null
                                }
                            }
                        }

                        // 5. DRAW CLUSTER/LINE CONNECIONS (Paths for finals or paths of tournaments)

                        // 6. DRAW THE COUNTRY DOTS / FLAGS ON THE CANVAS
                        projectedPoints.forEach { point ->
                            // Hemispherical Occlusion check (only draw if z > 0, which is the front half)
                            if (point.rotatedZ > 0) {
                                val isSelected = point.team == selectedTeam
                                val scaleFactor = if (isSelected) 1.4f else 1.0f

                                if (point.isInteractive) {
                                    // Draw glowing active wave pulse around the dot
                                    drawCircle(
                                        color = accentColor.copy(alpha = pulseAlpha),
                                        radius = pulseRadius * scaleFactor * 1.5f,
                                        center = Offset(point.screenX, point.screenY)
                                    )

                                    // Draw inner solid dot
                                    drawCircle(
                                        color = accentColor,
                                        radius = 6.dp.toPx() * scaleFactor,
                                        center = Offset(point.screenX, point.screenY)
                                        // outline stroke
                                    )
                                    
                                    drawCircle(
                                        color = Color.White,
                                        radius = 2.dp.toPx() * scaleFactor,
                                        center = Offset(point.screenX, point.screenY)
                                    )
                                } else {
                                    // Non-interactive (unfocused or eliminated) drawn as a small grey lock dot
                                    drawCircle(
                                        color = textColor.copy(alpha = 0.15f),
                                        radius = 4.dp.toPx(),
                                        center = Offset(point.screenX, point.screenY)
                                    )
                                }
                            }
                        }
                    }

                    // 7. FLOAT FLAGS / TEXT LABELS OVER SPECIFIC VISIBLE INTERACTIVE DOTS
                    // We render them as actual HTML/Compose components layered on top of Canvas!
                    projectedPoints.forEach { point ->
                        if (point.rotatedZ > 0 && point.isInteractive) {
                            val isSelected = point.team == selectedTeam
                            
                            // Adjust positioning so the text label floats exactly next to/above the dot
                            Box(
                                modifier = Modifier
                                    .offset(
                                        x = with(LocalDensity.current) { (point.screenX - 25.dp.toPx()).toDp() },
                                        y = with(LocalDensity.current) { (point.screenY - 35.dp.toPx()).toDp() }
                                    )
                                    .size(50.dp, 30.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Card(
                                    modifier = Modifier
                                        .shadow(2.dp, CircleShape)
                                        .clickable { selectedTeam = point.team }
                                        .testTag("flag_pin_${point.team.abbreviation}"),
                                    shape = CircleShape,
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) accentColor else cardBgColor
                                    ),
                                    border = BorderStroke(1.dp, if (isSelected) Color.White else accentColor.copy(alpha = 0.4f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(text = point.team.flag, fontSize = 11.sp)
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(
                                            text = point.team.abbreviation,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) (if (currentTheme == GlobeTheme.GLASS_LIGHT) Color.White else Color.Black) else textColor
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Rotation drag hint
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 8.dp)
                            .background(textColor.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "👈 Drag to Rotate • Pinch/Slider to Zoom 👉",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            // ZOOM CONTROL BAR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ZoomOut,
                    contentDescription = "Zoom Out",
                    tint = textColor.copy(alpha = 0.5f),
                    modifier = Modifier.size(18.dp)
                )
                Slider(
                    value = zoomScale,
                    onValueChange = { zoomScale = it },
                    valueRange = 0.6f..1.6f,
                    colors = SliderDefaults.colors(
                        thumbColor = accentColor,
                        activeTrackColor = accentColor,
                        inactiveTrackColor = textColor.copy(alpha = 0.1f)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .testTag("zoom_slider")
                )
                Icon(
                    imageVector = Icons.Default.ZoomIn,
                    contentDescription = "Zoom In",
                    tint = textColor.copy(alpha = 0.5f),
                    modifier = Modifier.size(18.dp)
                )
            }

            // SELECTED TEAM DETAIL SHEET / CARD OVERLAY
            AnimatedVisibility(
                visible = selectedTeam != null,
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Bottom),
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom)
            ) {
                if (selectedTeam != null) {
                    val team = selectedTeam!!
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 560.dp)
                            .shadow(
                                elevation = 16.dp,
                                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                                clip = false,
                                ambientColor = accentColor.copy(alpha = 0.4f),
                                spotColor = Color.Black
                            )
                            .border(
                                BorderStroke(
                                    1.2.dp,
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            accentColor.copy(alpha = 0.5f),
                                            textColor.copy(alpha = 0.05f)
                                        )
                                    )
                                ),
                                RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF0F172A) else Color(0xFFF8FAFC)
                        ),
                        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            // 1. STADIUM BACKGROUND HEADER
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                            ) {
                                // Canvas stadium pitch background
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    val skyColor = if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF020617) else Color(0xFF1E3A8A)
                                    val pitchColor = if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF0F3A20) else Color(0xFF166534)
                                    drawRect(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(skyColor, pitchColor)
                                        )
                                    )
                                    
                                    // Pitch halfway line and center circle
                                    val strokeWidth = 1.5.dp.toPx()
                                    val lineBrush = Color.White.copy(alpha = 0.25f)
                                    val midY = size.height * 0.85f
                                    drawLine(
                                        color = lineBrush,
                                        start = Offset(0f, midY),
                                        end = Offset(size.width, midY),
                                        strokeWidth = strokeWidth
                                    )
                                    drawCircle(
                                        color = lineBrush,
                                        radius = 45.dp.toPx(),
                                        center = Offset(size.width / 2f, midY),
                                        style = Stroke(width = strokeWidth)
                                    )
                                    
                                    // Soft light flares representing stadium lights
                                    drawCircle(
                                        brush = Brush.radialGradient(
                                            colors = listOf(Color.White.copy(alpha = 0.2f), Color.Transparent),
                                            center = Offset(0f, 0f),
                                            radius = 160.dp.toPx()
                                        ),
                                        radius = 160.dp.toPx(),
                                        center = Offset(0f, 0f)
                                    )
                                    drawCircle(
                                        brush = Brush.radialGradient(
                                            colors = listOf(Color.White.copy(alpha = 0.2f), Color.Transparent),
                                            center = Offset(size.width, 0f),
                                            radius = 160.dp.toPx()
                                        ),
                                        radius = 160.dp.toPx(),
                                        center = Offset(size.width, 0f)
                                    )
                                }

                                // Dark overlay for text contrast
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(Color.Black.copy(alpha = 0.4f), Color.Transparent, Color.Black.copy(alpha = 0.6f))
                                            )
                                        )
                                )

                                // Header buttons: Back and Star
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = { selectedTeam = null },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = "Back",
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    IconButton(
                                        onClick = { /* Star action */ },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.StarBorder,
                                            contentDescription = "Favorite",
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }

                                // Centered Team info: Logo badge and Name text
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.BottomCenter)
                                        .padding(bottom = 12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    TeamBadge(team = team)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = team.name.uppercase(),
                                        color = Color.White,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 22.sp,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        text = if (team.abbreviation == "USA") "United States Men's National Team" else "${team.name} National Team",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = null,
                                            tint = Color(0xFF0D9488),
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "FIFA Ranking: #${team.fifaRanking}",
                                            color = Color.White.copy(alpha = 0.8f),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }

                            // 2. TAB ROW SELECTION
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF1E293B) else Color.White)
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                ProfileTab.entries.forEach { tab ->
                                    val isSelected = profileTab == tab
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { profileTab = tab }
                                            .padding(vertical = 8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = tab.name,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                            color = if (isSelected) Color(0xFF0D9488) else textColor.copy(alpha = 0.6f)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Box(
                                            modifier = Modifier
                                                .width(40.dp)
                                                .height(2.5.dp)
                                                .background(
                                                    color = if (isSelected) Color(0xFF0D9488) else Color.Transparent,
                                                    shape = RoundedCornerShape(2.dp)
                                                )
                                        )
                                    }
                                }
                            }

                            // 3. SCROLLABLE TAB CONTAINER
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                when (profileTab) {
                                    ProfileTab.OVERVIEW -> {
                                        // A. NEXT MATCH Section
                                        item {
                                            Row(
                                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = "NEXT MATCH", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFF0D9488))
                                                Text(text = "Quarter-Final", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = textColor.copy(alpha = 0.5f))
                                            }

                                            Card(
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(16.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF1E293B) else Color.White
                                                ),
                                                border = BorderStroke(1.dp, textColor.copy(alpha = 0.08f))
                                            ) {
                                                Column(modifier = Modifier.padding(14.dp)) {
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        // Team 1 (Selected Team)
                                                        Column(
                                                            horizontalAlignment = Alignment.CenterHorizontally,
                                                            modifier = Modifier.weight(1f)
                                                        ) {
                                                            TeamBadge(team = team, modifier = Modifier.size(56.dp))
                                                            Spacer(modifier = Modifier.height(4.dp))
                                                            Text(text = team.abbreviation, fontWeight = FontWeight.Black, fontSize = 13.sp, color = textColor)
                                                            Text(text = "#${team.fifaRanking}", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = textColor.copy(alpha = 0.5f))
                                                        }

                                                        // VS Label
                                                        Text(
                                                            text = "VS",
                                                            fontWeight = FontWeight.Black,
                                                            fontSize = 14.sp,
                                                            color = accentColor,
                                                            modifier = Modifier.padding(horizontal = 12.dp)
                                                        )

                                                        // Team 2 (Opponent)
                                                        val opponentName = team.nextMatch.opponent
                                                        val opponentTeam = teams.find { it.name.lowercase() == opponentName.lowercase() || it.abbreviation.lowercase() == opponentName.lowercase() }
                                                        Column(
                                                            horizontalAlignment = Alignment.CenterHorizontally,
                                                            modifier = Modifier.weight(1f)
                                                        ) {
                                                            if (opponentTeam != null) {
                                                                TeamBadge(team = opponentTeam, modifier = Modifier.size(56.dp))
                                                            } else {
                                                                Box(
                                                                    modifier = Modifier
                                                                        .size(56.dp)
                                                                        .background(textColor.copy(alpha = 0.1f), CircleShape),
                                                                    contentAlignment = Alignment.Center
                                                                ) {
                                                                    Text(text = "🏳️", fontSize = 28.sp)
                                                                }
                                                            }
                                                            Spacer(modifier = Modifier.height(4.dp))
                                                            Text(
                                                                text = opponentTeam?.abbreviation ?: opponentName.take(3).uppercase(),
                                                                fontWeight = FontWeight.Black,
                                                                fontSize = 13.sp,
                                                                color = textColor
                                                            )
                                                            Text(
                                                                text = opponentTeam?.let { "#${it.fifaRanking}" } ?: "#2",
                                                                fontWeight = FontWeight.Bold,
                                                                fontSize = 10.sp,
                                                                color = textColor.copy(alpha = 0.5f)
                                                            )
                                                        }
                                                    }

                                                    Spacer(modifier = Modifier.height(10.dp))
                                                    HorizontalDivider(color = textColor.copy(alpha = 0.05f))
                                                    Spacer(modifier = Modifier.height(10.dp))

                                                    // Time & Venue icons
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = accentColor, modifier = Modifier.size(13.dp))
                                                            Spacer(modifier = Modifier.width(4.dp))
                                                            Text(text = team.nextMatch.date, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = textColor.copy(alpha = 0.7f))
                                                        }
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            Icon(Icons.Default.AccessTime, contentDescription = null, tint = accentColor, modifier = Modifier.size(13.dp))
                                                            Spacer(modifier = Modifier.width(4.dp))
                                                            Text(text = team.nextMatch.time, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = textColor.copy(alpha = 0.7f))
                                                        }
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = accentColor, modifier = Modifier.size(13.dp))
                                                            Spacer(modifier = Modifier.width(4.dp))
                                                            Text(
                                                                text = team.nextMatch.stadium.name.split(" ").firstOrNull() ?: team.nextMatch.stadium.name,
                                                                fontSize = 9.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = textColor.copy(alpha = 0.7f),
                                                                maxLines = 1,
                                                                overflow = TextOverflow.Ellipsis
                                                            )
                                                        }
                                                    }

                                                    Spacer(modifier = Modifier.height(10.dp))

                                                    // Weather info row inside Match Card
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .background(
                                                                if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF131D31) else Color(0xFFF0FDF4),
                                                                RoundedCornerShape(12.dp)
                                                            )
                                                            .padding(8.dp)
                                                    ) {
                                                        Row(
                                                            modifier = Modifier.fillMaxWidth(),
                                                            horizontalArrangement = Arrangement.SpaceBetween,
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                val weatherEmoji = when (team.nextMatch.stadium.weatherCondition) {
                                                                    "Sunny & Clear" -> "☀️"
                                                                    "Partly Cloudy" -> "⛅"
                                                                    "Humid & Showers" -> "🌧️"
                                                                    else -> "⛅"
                                                                }
                                                                Text(text = weatherEmoji, fontSize = 16.sp)
                                                                Spacer(modifier = Modifier.width(6.dp))
                                                                Column {
                                                                    Text(text = team.nextMatch.stadium.weatherTemp, fontWeight = FontWeight.Black, fontSize = 10.sp, color = textColor)
                                                                    Text(text = team.nextMatch.stadium.weatherCondition, fontSize = 7.sp, color = textColor.copy(alpha = 0.5f), fontWeight = FontWeight.Bold)
                                                                }
                                                            }

                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                Text(text = "💧", fontSize = 11.sp)
                                                                Spacer(modifier = Modifier.width(4.dp))
                                                                Column {
                                                                    Text(text = "18%", fontWeight = FontWeight.Black, fontSize = 10.sp, color = textColor)
                                                                    Text(text = "Humidity", fontSize = 7.sp, color = textColor.copy(alpha = 0.5f))
                                                                }
                                                            }

                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                Text(text = "💨", fontSize = 11.sp)
                                                                Spacer(modifier = Modifier.width(4.dp))
                                                                Column {
                                                                    Text(text = "14 km/h", fontWeight = FontWeight.Black, fontSize = 10.sp, color = textColor)
                                                                    Text(text = "Wind", fontSize = 7.sp, color = textColor.copy(alpha = 0.5f))
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        // B. TEAM OVERVIEW Grid Section
                                        item {
                                            Row(
                                                modifier = Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = "TEAM OVERVIEW", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFF0D9488))
                                            }

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                val overviewStats = listOf(
                                                    ProfileOverviewStat("⚽", "${team.stats.goalsScored}", "Goals Scored", Color(0xFF10B981)),
                                                    ProfileOverviewStat("🏆", "${team.stats.wins}", "Wins", Color(0xFFF59E0B)),
                                                    ProfileOverviewStat("📊", "${team.stats.possessionPercent}%", "Avg Poss", Color(0xFF3B82F6)),
                                                    ProfileOverviewStat("🛡️", "${team.stats.cleanSheets}", "Clean Sheets", Color(0xFF8B5CF6))
                                                )

                                                overviewStats.forEach { statItem ->
                                                    Card(
                                                        modifier = Modifier.weight(1f),
                                                        shape = RoundedCornerShape(12.dp),
                                                        colors = CardDefaults.cardColors(
                                                            containerColor = if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF1E293B) else Color.White
                                                        ),
                                                        border = BorderStroke(1.dp, textColor.copy(alpha = 0.08f))
                                                    ) {
                                                        Column(
                                                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 2.dp),
                                                            horizontalAlignment = Alignment.CenterHorizontally
                                                        ) {
                                                            Box(
                                                                modifier = Modifier
                                                                    .size(24.dp)
                                                                    .background(statItem.color.copy(alpha = 0.1f), CircleShape),
                                                                contentAlignment = Alignment.Center
                                                            ) {
                                                                Text(text = statItem.emoji, fontSize = 12.sp)
                                                            }
                                                            Spacer(modifier = Modifier.height(4.dp))
                                                            Text(text = statItem.value, fontWeight = FontWeight.Black, fontSize = 12.sp, color = textColor)
                                                            Spacer(modifier = Modifier.height(1.dp))
                                                            Text(
                                                                text = statItem.label,
                                                                fontSize = 7.5.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = textColor.copy(alpha = 0.5f),
                                                                textAlign = TextAlign.Center,
                                                                maxLines = 1
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        // C. STADIUM Section
                                        item {
                                            Row(
                                                modifier = Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = "STADIUM", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFF0D9488))
                                            }

                                            Card(
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(16.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF1E293B) else Color.White
                                                ),
                                                border = BorderStroke(1.dp, textColor.copy(alpha = 0.08f))
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(12.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Column(modifier = Modifier.weight(1.2f)) {
                                                        Text(
                                                            text = team.nextMatch.stadium.name,
                                                            fontWeight = FontWeight.ExtraBold,
                                                            fontSize = 13.sp,
                                                            color = textColor
                                                        )
                                                        Text(
                                                            text = team.nextMatch.stadium.city,
                                                            fontSize = 10.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = textColor.copy(alpha = 0.5f)
                                                        )
                                                        Text(
                                                            text = "Capacity: ${team.nextMatch.stadium.capacity}",
                                                            fontSize = 9.sp,
                                                            color = textColor.copy(alpha = 0.6f)
                                                        )
                                                        
                                                        Spacer(modifier = Modifier.height(8.dp))
                                                        
                                                        Button(
                                                            onClick = { /* View on Map */ },
                                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A)),
                                                            shape = RoundedCornerShape(8.dp),
                                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                                            modifier = Modifier.height(28.dp)
                                                        ) {
                                                            Text(text = "VIEW ON MAP", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                                        }
                                                    }

                                                    Spacer(modifier = Modifier.width(10.dp))

                                                    MapPinMiniMap(modifier = Modifier.size(115.dp, 80.dp))
                                                }
                                            }
                                        }

                                        // D. HEAD COACH Section
                                        item {
                                            Row(
                                                modifier = Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = "HEAD COACH", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFF0D9488))
                                            }

                                            Card(
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(16.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF1E293B) else Color.White
                                                ),
                                                border = BorderStroke(1.dp, textColor.copy(alpha = 0.08f))
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(12.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(48.dp)
                                                            .background(
                                                                Brush.radialGradient(
                                                                    colors = listOf(Color(0xFF38BDF8), Color(0xFF1E3A8A))
                                                                ),
                                                                CircleShape
                                                            ),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Text(
                                                            text = team.coach.split(" ").mapNotNull { it.firstOrNull() }.joinToString("").take(2),
                                                            color = Color.White,
                                                            fontSize = 14.sp,
                                                            fontWeight = FontWeight.Black
                                                        )
                                                    }

                                                    Spacer(modifier = Modifier.width(12.dp))

                                                    Column(modifier = Modifier.weight(1f)) {
                                                        Text(
                                                            text = team.coach,
                                                            fontWeight = FontWeight.ExtraBold,
                                                            fontSize = 13.sp,
                                                            color = textColor
                                                        )
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            val flagCode = when (team.coach) {
                                                                "Lionel Scaloni" -> "🇦🇷"
                                                                "Didier Deschamps" -> "🇫🇷"
                                                                "Luis de la Fuente" -> "🇪🇸"
                                                                "Dorival Júnior" -> "🇧🇷"
                                                                "Thomas Tuchel" -> "🇩🇪"
                                                                "Mauricio Pochettino" -> "🇦🇷"
                                                                "Javier Aguirre" -> "🇲🇽"
                                                                "Jesse Marsch" -> "🇺🇸"
                                                                else -> "🏳️"
                                                            }
                                                            val countryName = when (team.coach) {
                                                                "Lionel Scaloni" -> "Argentina"
                                                                "Didier Deschamps" -> "France"
                                                                "Luis de la Fuente" -> "Spain"
                                                                "Dorival Júnior" -> "Brazil"
                                                                "Thomas Tuchel" -> "Germany"
                                                                "Mauricio Pochettino" -> "Argentina"
                                                                "Javier Aguirre" -> "Mexico"
                                                                "Jesse Marsch" -> "United States"
                                                                else -> "International"
                                                            }
                                                            Text(text = "$flagCode  ", fontSize = 11.sp)
                                                            Text(text = countryName, fontSize = 10.sp, color = textColor.copy(alpha = 0.5f), fontWeight = FontWeight.Bold)
                                                        }
                                                        Spacer(modifier = Modifier.height(2.dp))
                                                        Text(
                                                            text = "Age: 52  •  Since: Sep 2024  •  Formation: 4-3-3",
                                                            fontSize = 8.sp,
                                                            color = textColor.copy(alpha = 0.4f),
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }

                                                    Icon(
                                                        imageVector = Icons.Default.ChevronRight,
                                                        contentDescription = null,
                                                        tint = textColor.copy(alpha = 0.3f),
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }
                                            }
                                        }

                                        // E. KEY PLAYERS Section
                                        item {
                                            Row(
                                                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = "KEY PLAYERS", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFF0D9488))
                                                TextButton(
                                                    onClick = { profileTab = ProfileTab.SQUAD },
                                                    contentPadding = PaddingValues(0.dp),
                                                    modifier = Modifier.height(28.dp)
                                                ) {
                                                    Text(text = "View all", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D9488))
                                                }
                                            }

                                            LazyRow(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                                            ) {
                                                items(team.keyPlayers) { player ->
                                                    var showPlayerPopup by remember { mutableStateOf(false) }
                                                    
                                                    Card(
                                                        modifier = Modifier
                                                            .width(130.dp)
                                                            .clickable { showPlayerPopup = true }
                                                            .border(1.dp, textColor.copy(alpha = 0.08f), RoundedCornerShape(14.dp)),
                                                        shape = RoundedCornerShape(14.dp),
                                                        colors = CardDefaults.cardColors(
                                                            containerColor = if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF1E293B) else Color.White
                                                        )
                                                    ) {
                                                        Column(
                                                            modifier = Modifier.padding(10.dp),
                                                            horizontalAlignment = Alignment.CenterHorizontally
                                                        ) {
                                                            Box(
                                                                modifier = Modifier
                                                                    .size(44.dp)
                                                                    .background(
                                                                        Brush.linearGradient(
                                                                            colors = listOf(Color(0xFF0D9488), Color(0xFF10B981))
                                                                        ),
                                                                        CircleShape
                                                                    ),
                                                                contentAlignment = Alignment.Center
                                                            ) {
                                                                Text(
                                                                    text = player.name.split(" ").mapNotNull { it.firstOrNull() }.joinToString("").take(2),
                                                                    color = Color.White,
                                                                    fontSize = 13.sp,
                                                                    fontWeight = FontWeight.Black
                                                                )
                                                                
                                                                Box(
                                                                    modifier = Modifier
                                                                        .align(Alignment.BottomEnd)
                                                                        .background(accentColor, CircleShape)
                                                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                                                ) {
                                                                    Text(text = player.position, fontSize = 6.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                                                }
                                                            }

                                                            Spacer(modifier = Modifier.height(6.dp))
                                                            
                                                            Text(
                                                                text = player.name,
                                                                fontWeight = FontWeight.ExtraBold,
                                                                fontSize = 10.5.sp,
                                                                color = textColor,
                                                                maxLines = 1,
                                                                overflow = TextOverflow.Ellipsis,
                                                                textAlign = TextAlign.Center
                                                            )
                                                            
                                                            Text(
                                                                text = "${player.position} • #${player.number}",
                                                                fontSize = 8.5.sp,
                                                                color = textColor.copy(alpha = 0.5f),
                                                                fontWeight = FontWeight.Bold
                                                            )
                                                        }
                                                    }
                                                    
                                                    if (showPlayerPopup) {
                                                        AlertDialog(
                                                            onDismissRequest = { showPlayerPopup = false },
                                                            title = { Text(text = player.name, fontWeight = FontWeight.Black, fontSize = 15.sp, color = textColor) },
                                                            text = {
                                                                Column {
                                                                    Text(text = "Position: ${player.position}  •  Number: #${player.number}", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = accentColor)
                                                                    Spacer(modifier = Modifier.height(6.dp))
                                                                    Text(text = player.description, fontSize = 11.sp, color = textColor.copy(alpha = 0.8f))
                                                                }
                                                            },
                                                            confirmButton = {
                                                                TextButton(onClick = { showPlayerPopup = false }) {
                                                                    Text("Close", color = Color(0xFF0D9488), fontWeight = FontWeight.Bold)
                                                                }
                                                            },
                                                            containerColor = if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF1E293B) else Color.White
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                        // F. TOURNAMENT STATS List Section
                                        item {
                                            Row(
                                                modifier = Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 4.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = "TOURNAMENT STATS", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFF0D9488))
                                                TextButton(
                                                    onClick = { profileTab = ProfileTab.STATS },
                                                    contentPadding = PaddingValues(0.dp),
                                                    modifier = Modifier.height(28.dp)
                                                ) {
                                                    Text(text = "View all", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D9488))
                                                }
                                            }

                                            Card(
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(16.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF1E293B) else Color.White
                                                ),
                                                border = BorderStroke(1.dp, textColor.copy(alpha = 0.08f))
                                            ) {
                                                Column(modifier = Modifier.padding(12.dp)) {
                                                    val statsRows = listOf(
                                                        Triple("📋", "Matches Played", "4"),
                                                        Triple("⚽", "Goals Scored", "${team.stats.goalsScored}"),
                                                        Triple("🥅", "Goals Conceded", "4"),
                                                        Triple("🟨", "Yellow Cards", "6"),
                                                        Triple("🟥", "Red Cards", "0"),
                                                        Triple("🎯", "Pass Accuracy", "87%")
                                                    )

                                                    statsRows.forEachIndexed { idx, (emoji, name, value) ->
                                                        Row(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(vertical = 5.dp),
                                                            horizontalArrangement = Arrangement.SpaceBetween,
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                Text(text = emoji, fontSize = 13.sp)
                                                                Spacer(modifier = Modifier.width(6.dp))
                                                                Text(text = name, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = textColor.copy(alpha = 0.8f))
                                                            }
                                                            Text(text = value, fontSize = 10.5.sp, fontWeight = FontWeight.Black, color = textColor)
                                                        }
                                                        if (idx < statsRows.lastIndex) {
                                                            HorizontalDivider(color = textColor.copy(alpha = 0.05f))
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    ProfileTab.SQUAD -> {
                                        item {
                                            Column(modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                                                Text(
                                                    text = "FULL TEAM SQUAD ROSTER",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = Color(0xFF0D9488),
                                                    modifier = Modifier.padding(bottom = 10.dp)
                                                )

                                                team.keyPlayers.forEach { player ->
                                                    Card(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(bottom = 8.dp)
                                                            .border(1.dp, textColor.copy(alpha = 0.06f), RoundedCornerShape(14.dp)),
                                                        shape = RoundedCornerShape(14.dp),
                                                        colors = CardDefaults.cardColors(
                                                            containerColor = if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF1E293B) else Color.White
                                                        )
                                                    ) {
                                                        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                                            Box(
                                                                modifier = Modifier
                                                                    .size(36.dp)
                                                                    .background(Color(0xFF0D9488).copy(alpha = 0.1f), CircleShape)
                                                                    .border(1.dp, Color(0xFF0D9488).copy(alpha = 0.2f), CircleShape),
                                                                contentAlignment = Alignment.Center
                                                            ) {
                                                                Text(
                                                                    text = player.number.toString(),
                                                                    color = Color(0xFF0D9488),
                                                                    fontSize = 12.sp,
                                                                    fontWeight = FontWeight.Black
                                                                )
                                                            }
                                                            
                                                            Spacer(modifier = Modifier.width(10.dp))
                                                            
                                                            Column(modifier = Modifier.weight(1f)) {
                                                                Text(text = player.name, fontWeight = FontWeight.Black, fontSize = 12.sp, color = textColor)
                                                                Text(text = player.position, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = textColor.copy(alpha = 0.5f))
                                                                Spacer(modifier = Modifier.height(2.dp))
                                                                Text(text = player.description, fontSize = 9.sp, color = textColor.copy(alpha = 0.7f), lineHeight = 12.sp)
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    ProfileTab.STATS -> {
                                        item {
                                            Column(modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                                                Text(
                                                    text = "TOURNAMENT PERFORMANCE GAUGES",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = Color(0xFF0D9488),
                                                    modifier = Modifier.padding(bottom = 10.dp)
                                                )

                                                val performanceStats = listOf(
                                                    Triple("Goals Scored", team.stats.goalsScored, 20),
                                                    Triple("Wins / Unbeaten Matches", team.stats.wins, 5),
                                                    Triple("Ball Possession Percent", team.stats.possessionPercent, 100),
                                                    Triple("Shots on Target", team.stats.shotsOnTarget, 25),
                                                    Triple("Clean Sheets Secured", team.stats.cleanSheets, 5)
                                                )

                                                performanceStats.forEach { (label, value, maxValue) ->
                                                    Card(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(bottom = 8.dp),
                                                        shape = RoundedCornerShape(12.dp),
                                                        colors = CardDefaults.cardColors(
                                                            containerColor = if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF1E293B) else Color.White
                                                        )
                                                    ) {
                                                        Column(modifier = Modifier.padding(10.dp)) {
                                                            Row(
                                                                modifier = Modifier.fillMaxWidth(),
                                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                                verticalAlignment = Alignment.CenterVertically
                                                            ) {
                                                                Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = textColor)
                                                                Text(
                                                                    text = "$value / $maxValue",
                                                                    fontSize = 10.sp,
                                                                    fontWeight = FontWeight.Black,
                                                                    color = Color(0xFF0D9488)
                                                                )
                                                            }
                                                            Spacer(modifier = Modifier.height(4.dp))
                                                            
                                                            val progressFraction = (value.toFloat() / maxValue.toFloat()).coerceIn(0f, 1f)
                                                            Box(
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .height(6.dp)
                                                                    .background(textColor.copy(alpha = 0.05f), CircleShape)
                                                            ) {
                                                                Box(
                                                                    modifier = Modifier
                                                                        .fillMaxWidth(progressFraction)
                                                                        .fillMaxHeight()
                                                                        .background(
                                                                            Brush.horizontalGradient(
                                                                                colors = listOf(Color(0xFF10B981), Color(0xFF0D9488))
                                                                            ),
                                                                            CircleShape
                                                                        )
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    ProfileTab.SCHEDULE -> {
                                        item {
                                            Column(modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                                                Text(
                                                    text = "ROAD TO QUARTER & NEXT MATCH",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = Color(0xFF0D9488),
                                                    modifier = Modifier.padding(bottom = 10.dp)
                                                )

                                                Card(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(bottom = 10.dp),
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF1E293B) else Color.White
                                                    )
                                                ) {
                                                    Column(modifier = Modifier.padding(10.dp)) {
                                                        Text(text = "UPCOMING MATCH", fontSize = 8.5.sp, fontWeight = FontWeight.Black, color = accentColor)
                                                        Spacer(modifier = Modifier.height(3.dp))
                                                        Text(text = "vs ${team.nextMatch.opponent}", fontWeight = FontWeight.Black, fontSize = 13.sp, color = textColor)
                                                        Text(text = "🏟️ ${team.nextMatch.stadium.name} (${team.nextMatch.stadium.city})", fontSize = 10.sp, color = textColor.copy(alpha = 0.6f))
                                                        Text(text = "📅 ${team.nextMatch.date} • ⏰ ${team.nextMatch.time}", fontSize = 9.sp, color = textColor.copy(alpha = 0.5f), fontWeight = FontWeight.Bold)
                                                    }
                                                }

                                                team.path.forEach { stageResult ->
                                                    Card(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(bottom = 6.dp),
                                                        colors = CardDefaults.cardColors(
                                                            containerColor = if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF1E293B).copy(alpha = 0.5f) else Color.White.copy(alpha = 0.5f)
                                                        )
                                                    ) {
                                                        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                                            Text(text = "✔️", fontSize = 12.sp)
                                                            Spacer(modifier = Modifier.width(8.dp))
                                                            Text(text = stageResult, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = textColor)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    ProfileTab.NEWS -> {
                                        item {
                                            Column(modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                                                Text(
                                                    text = "TACTICAL FIELD NEWS",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = Color(0xFF0D9488),
                                                    modifier = Modifier.padding(bottom = 10.dp)
                                                )

                                                val newsArticles = listOf(
                                                    Pair("${team.name} optimistic ahead of crucial quarter-final match", "Reporters say the squad was seen training with high spirits today. Main strategists focus heavily on counter pressing options."),
                                                    Pair("Analyst Breakdown: How Coach ${team.coach} has transformed their form", "The strategic overview shows an impressive adaptation of tactical depth, leading to high-octane horizontal crossing and solid defensive play.")
                                                )

                                                newsArticles.forEach { (title, snippet) ->
                                                    Card(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(bottom = 8.dp),
                                                        colors = CardDefaults.cardColors(
                                                            containerColor = if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF1E293B) else Color.White
                                                        )
                                                    ) {
                                                        Column(modifier = Modifier.padding(10.dp)) {
                                                            Text(text = title, fontWeight = FontWeight.Black, fontSize = 11.5.sp, color = textColor)
                                                            Spacer(modifier = Modifier.height(3.dp))
                                                            Text(text = snippet, fontSize = 9.sp, color = textColor.copy(alpha = 0.7f), lineHeight = 12.sp)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        // FULL-SCREEN TACTICAL COMPARISON OVERLAY
        AnimatedVisibility(
            visible = isCompareDrawerOpen,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            if (compareTeam1 != null && compareTeam2 != null) {
                TacticalH2HArena(
                    team1 = compareTeam1!!,
                    team2 = compareTeam2!!,
                    currentTheme = currentTheme,
                    textColor = textColor,
                    cardBgColor = cardBgColor,
                    accentColor = accentColor,
                    allTeams = teams,
                    onCloseRequest = { isCompareDrawerOpen = false },
                    onTeamsChanged = { t1, t2 ->
                        compareTeam1 = t1
                        compareTeam2 = t2
                    }
                )
            }
        }
    }
}
}

@Composable
fun VsLogoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(82.dp)
            .height(44.dp)
            .clickable(onClick = onClick)
            .testTag("vs_logo_compare_button"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val random = java.util.Random(13)

            // Left brush stroke (Orange)
            val leftPath = Path().apply {
                moveTo(w * 0.1f, h * 0.5f)
                quadraticTo(w * 0.18f, h * 0.15f, w * 0.45f, h * 0.12f)
                lineTo(w * 0.52f, h * 0.5f)
                lineTo(w * 0.45f, h * 0.88f)
                quadraticTo(w * 0.18f, h * 0.85f, w * 0.1f, h * 0.5f)
                close()
            }
            drawPath(path = leftPath, color = Color(0xFFF97316))

            // Draw orange splat lines
            for (i in 0..4) {
                val startX = w * (0.05f + random.nextFloat() * 0.12f)
                val startY = h * (0.25f + random.nextFloat() * 0.5f)
                val len = w * (0.05f + random.nextFloat() * 0.1f)
                drawLine(
                    color = Color(0xFFF97316),
                    start = Offset(startX, startY),
                    end = Offset(startX + len, startY + (random.nextFloat() - 0.5f) * 4f),
                    strokeWidth = 1.5.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // Right brush stroke (Purple)
            val rightPath = Path().apply {
                moveTo(w * 0.9f, h * 0.5f)
                quadraticTo(w * 0.82f, h * 0.15f, w * 0.55f, h * 0.12f)
                lineTo(w * 0.48f, h * 0.5f)
                lineTo(w * 0.55f, h * 0.88f)
                quadraticTo(w * 0.82f, h * 0.85f, w * 0.9f, h * 0.5f)
                close()
            }
            drawPath(path = rightPath, color = Color(0xFFA855F7))

            // Draw purple splat lines
            for (i in 0..4) {
                val startX = w * (0.83f + random.nextFloat() * 0.12f)
                val startY = h * (0.25f + random.nextFloat() * 0.5f)
                val len = w * (0.05f + random.nextFloat() * 0.1f)
                drawLine(
                    color = Color(0xFFA855F7),
                    start = Offset(startX, startY),
                    end = Offset(startX - len, startY + (random.nextFloat() - 0.5f) * 4f),
                    strokeWidth = 1.5.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }

        // Central white oval for "VS" text
        Box(
            modifier = Modifier
                .size(width = 34.dp, height = 24.dp)
                .shadow(1.5.dp, shape = CircleShape)
                .background(Color.White, CircleShape)
                .border(0.8.dp, Color.LightGray.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "VS",
                fontWeight = FontWeight.Black,
                fontSize = 11.sp,
                color = Color(0xFF0F172A),
                letterSpacing = (-0.5).sp
            )
        }
    }
}

@Composable
fun TeamDropdown(
    selectedTeam: Team?,
    allTeams: List<Team>,
    textColor: Color,
    cardBgColor: Color,
    onSelect: (Team) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .border(1.dp, textColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = cardBgColor.copy(alpha = 0.95f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectedTeam != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = selectedTeam.flag, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = selectedTeam.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textColor)
                    }
                } else {
                    Text(text = "Select Team", fontSize = 12.sp, color = textColor.copy(alpha = 0.5f))
                }
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown",
                    tint = textColor.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(cardBgColor)
                .border(1.dp, textColor.copy(alpha = 0.1f))
        ) {
            allTeams.forEach { team ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = team.flag, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = team.name, fontSize = 12.sp, color = textColor, fontWeight = FontWeight.Bold)
                        }
                    },
                    onClick = {
                        onSelect(team)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun FormBadge(letter: String) {
    val badgeBg = when (letter) {
        "W" -> Color(0xFF2E7D32) // Green
        "D" -> Color(0xFFF59E0B) // Amber
        "L" -> Color(0xFFC62828) // Red
        else -> Color.Gray
    }
    Box(
        modifier = Modifier
            .padding(end = 3.dp, top = 2.dp)
            .size(14.dp)
            .background(badgeBg, RoundedCornerShape(3.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            color = Color.White,
            fontSize = 8.sp,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
fun StatRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 10.sp, color = color.copy(alpha = 0.6f))
        Text(text = value, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun H2HBar(
    label: String,
    val1: Int,
    val2: Int,
    suffix: String = "",
    accentColor: Color,
    textColor: Color
) {
    val total = (val1 + val2).toFloat()
    val ratio = if (total > 0) val1.toFloat() / total else 0.5f

    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "$val1$suffix", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor)
            Text(text = label, fontSize = 10.sp, color = textColor.copy(alpha = 0.5f), fontWeight = FontWeight.Bold)
            Text(text = "$val2$suffix", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor)
        }
        
        Spacer(modifier = Modifier.height(2.dp))
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape)
                .background(textColor.copy(alpha = 0.05f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(ratio.coerceIn(0.05f, 0.95f))
                    .background(accentColor)
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight((1f - ratio).coerceIn(0.05f, 0.95f))
                    .background(textColor.copy(alpha = 0.2f))
            )
        }
    }
}

@Composable
fun StadiumMiniMap() {
    val infiniteTransition = rememberInfiniteTransition(label = "stadium3D")
    
    // Laser scanning sweep animation
    val scanProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scanProgress"
    )

    // Floating hover animation to enhance 3D hologram look
    val hoverOffset by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hoverOffset"
    )

    Canvas(
        modifier = Modifier
            .size(160.dp, 96.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
    ) {
        // Deep background coloring for high-tech blueprint look
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF071911), Color(0xFF0F3124))
            )
        )

        val W = size.width
        val H = size.height
        val cx = W / 2f
        val cy = H / 2f + 14f + hoverOffset // translate down slightly & apply float offset

        // 3D Isometric projection function
        // Takes local 2D pitch space (centered at (0,0)) and elevation Z, and returns 2D screen coordinates
        fun project(lx: Float, ly: Float, lz: Float): Offset {
            // Isometric perspective coefficients (angle 30 degrees)
            // Flatten vertical axis (Y) to create an elegant tilted perspective
            val rx = lx * 0.866f - ly * 0.866f
            val ry = (lx * 0.5f + ly * 0.5f) * 0.5f - lz // Z goes upwards (subtract)
            return Offset(cx + rx, cy + ry)
        }

        // Pitch Dimensions in Local Coordinates
        val pw = 90f
        val ph = 50f
        val left = -pw / 2f
        val right = pw / 2f
        val top = -ph / 2f
        val bottom = ph / 2f

        // 1. Draw Alternating 3D Grass Turf Stripes
        val numStripes = 6
        val stripeW = pw / numStripes
        for (i in 0 until numStripes) {
            val sLeft = left + i * stripeW
            val sRight = sLeft + stripeW
            val stripePath = Path().apply {
                val p1 = project(sLeft, top, 0f)
                val p2 = project(sRight, top, 0f)
                val p3 = project(sRight, bottom, 0f)
                val p4 = project(sLeft, bottom, 0f)
                moveTo(p1.x, p1.y)
                lineTo(p2.x, p2.y)
                lineTo(p3.x, p3.y)
                lineTo(p4.x, p4.y)
                close()
            }
            // Use organic green hues matching elite soccer field turf
            val stripeColor = if (i % 2 == 0) Color(0xFF2E7D32) else Color(0xFF1B5E20)
            drawPath(path = stripePath, color = stripeColor)
        }

        val lineStroke = 1.5.dp.toPx()
        val whiteLineColor = Color.White.copy(alpha = 0.65f)

        // 2. Draw 3D Outer Field Boundary
        val boundaryPath = Path().apply {
            val p1 = project(left, top, 0f)
            val p2 = project(right, top, 0f)
            val p3 = project(right, bottom, 0f)
            val p4 = project(left, bottom, 0f)
            moveTo(p1.x, p1.y)
            lineTo(p2.x, p2.y)
            lineTo(p3.x, p3.y)
            lineTo(p4.x, p4.y)
            close()
        }
        drawPath(boundaryPath, color = whiteLineColor, style = Stroke(width = lineStroke))

        // 3. Draw 3D Center Halfway Line
        val midTop = project(0f, top, 0f)
        val midBottom = project(0f, bottom, 0f)
        drawLine(
            color = whiteLineColor,
            start = midTop,
            end = midBottom,
            strokeWidth = lineStroke
        )

        // 4. Draw Center Circle in 3D Perspective
        val circlePath = Path()
        val cRadius = 13f
        for (deg in 0..360 step 10) {
            val rad = Math.toRadians(deg.toDouble())
            val lx = cRadius * cos(rad).toFloat()
            val ly = cRadius * sin(rad).toFloat()
            val pt = project(lx, ly, 0f)
            if (deg == 0) circlePath.moveTo(pt.x, pt.y) else circlePath.lineTo(pt.x, pt.y)
        }
        circlePath.close()
        drawPath(circlePath, color = whiteLineColor, style = Stroke(width = lineStroke))

        // 5. Draw 3D Penalty Areas
        // Left Penalty Area
        val paPath = Path().apply {
            val p1 = project(left, -16f, 0f)
            val p2 = project(left + 16f, -16f, 0f)
            val p3 = project(left + 16f, 16f, 0f)
            val p4 = project(left, 16f, 0f)
            moveTo(p1.x, p1.y)
            lineTo(p2.x, p2.y)
            lineTo(p3.x, p3.y)
            lineTo(p4.x, p4.y)
        }
        drawPath(paPath, color = whiteLineColor, style = Stroke(width = lineStroke))

        // Right Penalty Area
        val pbPath = Path().apply {
            val p1 = project(right, -16f, 0f)
            val p2 = project(right - 16f, -16f, 0f)
            val p3 = project(right - 16f, 16f, 0f)
            val p4 = project(right, 16f, 0f)
            moveTo(p1.x, p1.y)
            lineTo(p2.x, p2.y)
            lineTo(p3.x, p3.y)
            lineTo(p4.x, p4.y)
        }
        drawPath(pbPath, color = whiteLineColor, style = Stroke(width = lineStroke))

        // 6. Draw 3D Goal Posts (standing vertically!)
        val goalH = 8f
        val goalW = 6f
        
        // Left Goal
        val lg1 = project(left, -goalW, 0f)
        val lg1t = project(left, -goalW, goalH)
        val lg2 = project(left, goalW, 0f)
        val lg2t = project(left, goalW, goalH)
        val lgb = project(left - 4f, 0f, 0f) // net back base
        val lgbt = project(left - 4f, 0f, goalH) // net back top
        
        // Draw Left Posts & Crossbar
        drawLine(color = Color.White, start = lg1, end = lg1t, strokeWidth = 2f)
        drawLine(color = Color.White, start = lg2, end = lg2t, strokeWidth = 2f)
        drawLine(color = Color.White, start = lg1t, end = lg2t, strokeWidth = 2f)
        // Draw Left Goal Net Outlines
        drawLine(color = Color.White.copy(alpha = 0.3f), start = lg1t, end = lgbt, strokeWidth = 1f)
        drawLine(color = Color.White.copy(alpha = 0.3f), start = lg2t, end = lgbt, strokeWidth = 1f)
        drawLine(color = Color.White.copy(alpha = 0.3f), start = lg1, end = lgb, strokeWidth = 1f)
        drawLine(color = Color.White.copy(alpha = 0.3f), start = lg2, end = lgb, strokeWidth = 1f)
        drawLine(color = Color.White.copy(alpha = 0.3f), start = lgbt, end = lgb, strokeWidth = 1f)

        // Right Goal
        val rg1 = project(right, -goalW, 0f)
        val rg1t = project(right, -goalW, goalH)
        val rg2 = project(right, goalW, 0f)
        val rg2t = project(right, goalW, goalH)
        val rgb = project(right + 4f, 0f, 0f) // net back base
        val rgbt = project(right + 4f, 0f, goalH) // net back top
        
        // Draw Right Posts & Crossbar
        drawLine(color = Color.White, start = rg1, end = rg1t, strokeWidth = 2f)
        drawLine(color = Color.White, start = rg2, end = rg2t, strokeWidth = 2f)
        drawLine(color = Color.White, start = rg1t, end = rg2t, strokeWidth = 2f)
        // Draw Right Goal Net Outlines
        drawLine(color = Color.White.copy(alpha = 0.3f), start = rg1t, end = rgb, strokeWidth = 1f)
        drawLine(color = Color.White.copy(alpha = 0.3f), start = rg2t, end = rgb, strokeWidth = 1f)
        drawLine(color = Color.White.copy(alpha = 0.3f), start = rg1, end = rgb, strokeWidth = 1f)
        drawLine(color = Color.White.copy(alpha = 0.3f), start = rg2, end = rgb, strokeWidth = 1f)
        drawLine(color = Color.White.copy(alpha = 0.3f), start = rgbt, end = rgb, strokeWidth = 1f)

        // 7. Draw Double-Tier 3D Stadium Seating Bowl
        // Lower Tier stands (Outer offset 10 units)
        val s1xMin = left - 10f
        val s1xMax = right + 10f
        val s1yMin = top - 10f
        val s1yMax = bottom + 10f
        val s1h = 10f

        val st1_1 = project(s1xMin, s1yMin, 0f)
        val st1_2 = project(s1xMax, s1yMin, 0f)
        val st1_3 = project(s1xMax, s1yMax, 0f)
        val st1_4 = project(s1xMin, s1yMax, 0f)

        val st1_1h = project(s1xMin, s1yMin, s1h)
        val st1_2h = project(s1xMax, s1yMin, s1h)
        val st1_3h = project(s1xMax, s1yMax, s1h)
        val st1_4h = project(s1xMin, s1yMax, s1h)

        // Upper Tier stands (Outer offset 18 units, higher up)
        val s2xMin = left - 18f
        val s2xMax = right + 18f
        val s2yMin = top - 18f
        val s2yMax = bottom + 18f
        val s2h = 22f

        val st2_1h = project(s2xMin, s2yMin, s2h)
        val st2_2h = project(s2xMax, s2yMin, s2h)
        val st2_3h = project(s2xMax, s2yMax, s2h)
        val st2_4h = project(s2xMin, s2yMax, s2h)

        // Helper to draw translucent walls representing stadium seating blocks
        fun drawSeatingWall(p1: Offset, p2: Offset, p3: Offset, p4: Offset, color: Color) {
            val wallPath = Path().apply {
                moveTo(p1.x, p1.y)
                lineTo(p2.x, p2.y)
                lineTo(p3.x, p3.y)
                lineTo(p4.x, p4.y)
                close()
            }
            drawPath(path = wallPath, color = color)
        }

        // Draw Translucent Lower Seating Stands Wall Layers
        val lowerBowlColor = Color(0xFF0D9488).copy(alpha = 0.12f)
        drawSeatingWall(st1_1, st1_1h, st1_2h, st1_2, lowerBowlColor)
        drawSeatingWall(st1_2, st1_2h, st1_3h, st1_3, lowerBowlColor)
        drawSeatingWall(st1_3, st1_3h, st1_4h, st1_4, lowerBowlColor)
        drawSeatingWall(st1_4, st1_4h, st1_1h, st1_1, lowerBowlColor)

        // Draw Translucent Upper Seating Stands Wall Layers
        val upperBowlColor = Color(0xFF14B8A6).copy(alpha = 0.08f)
        drawSeatingWall(st1_1h, st2_1h, st2_2h, st1_2h, upperBowlColor)
        drawSeatingWall(st1_2h, st2_2h, st2_3h, st1_3h, upperBowlColor)
        drawSeatingWall(st1_3h, st2_3h, st2_4h, st1_4h, upperBowlColor)
        drawSeatingWall(st1_4h, st2_4h, st2_1h, st1_1h, upperBowlColor)

        // Draw Ring lines to define architectural borders
        val standStroke = Stroke(width = 1.dp.toPx())
        val standLineColor = Color(0xFF14B8A6).copy(alpha = 0.4f)
        
        // Lower ring
        val r1Path = Path().apply {
            moveTo(st1_1h.x, st1_1h.y)
            lineTo(st1_2h.x, st1_2h.y)
            lineTo(st1_3h.x, st1_3h.y)
            lineTo(st1_4h.x, st1_4h.y)
            close()
        }
        drawPath(r1Path, color = standLineColor, style = standStroke)

        // Upper ring (Roof/Top rim)
        val r2Path = Path().apply {
            moveTo(st2_1h.x, st2_1h.y)
            lineTo(st2_2h.x, st2_2h.y)
            lineTo(st2_3h.x, st2_3h.y)
            lineTo(st2_4h.x, st2_4h.y)
            close()
        }
        drawPath(r2Path, color = standLineColor.copy(alpha = 0.6f), style = standStroke)

        // 8. Corner Floodlight Towers with Spotlights
        val towers = listOf(
            project(s2xMin, s2yMin, 0f) to project(s2xMin, s2yMin, 32f),
            project(s2xMax, s2yMin, 0f) to project(s2xMax, s2yMin, 32f),
            project(s2xMax, s2yMax, 0f) to project(s2xMax, s2yMax, 32f),
            project(s2xMin, s2yMax, 0f) to project(s2xMin, s2yMax, 32f)
        )

        towers.forEach { (base, top) ->
            // Pillar post
            drawLine(color = Color(0xFF2DD4BF).copy(alpha = 0.4f), start = base, end = top, strokeWidth = 1.5.dp.toPx())
            // Glowing light-head
            drawCircle(color = Color(0xFF34D399), radius = 2.5.dp.toPx(), center = top)
            
            // Subtle conical light beams projecting towards center
            val beamPath = Path().apply {
                moveTo(top.x, top.y)
                lineTo(cx - 15f, cy + 10f)
                lineTo(cx + 15f, cy + 10f)
                close()
            }
            drawPath(path = beamPath, brush = Brush.radialGradient(
                colors = listOf(Color(0xFF34D399).copy(alpha = 0.12f), Color.Transparent),
                center = top,
                radius = 80f
            ))
        }

        // 9. Glowing 3D Holographic Laser Scan Sweep
        // Sweeps horizontally back and forth across the pitch
        val scanX = left - 15f + scanProgress * (pw + 30f)
        val sp_y1 = top - 12f
        val sp_y2 = bottom + 12f
        
        // Scan bottom plane line
        val scBaseLeft = project(scanX, sp_y1, 0f)
        val scBaseRight = project(scanX, sp_y2, 0f)
        
        // Scan elevated top plane line (the sweeping wall)
        val scTopLeft = project(scanX, sp_y1, 26f)
        val scTopRight = project(scanX, sp_y2, 26f)

        val sweepPath = Path().apply {
            moveTo(scBaseLeft.x, scBaseLeft.y)
            lineTo(scTopLeft.x, scTopLeft.y)
            lineTo(scTopRight.x, scTopRight.y)
            lineTo(scBaseRight.x, scBaseRight.y)
            close()
        }

        // Glowing cyan sweep laser
        drawPath(
            path = sweepPath,
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF22D3EE).copy(alpha = 0.0f),
                    Color(0xFF22D3EE).copy(alpha = 0.25f),
                    Color(0xFF22D3EE).copy(alpha = 0.0f)
                ),
                start = scBaseLeft,
                end = scBaseRight
            )
        )
        // Bright beam edges
        drawLine(color = Color(0xFF22D3EE).copy(alpha = 0.7f), start = scBaseLeft, end = scBaseRight, strokeWidth = 1.2.dp.toPx())
        drawLine(color = Color(0xFF22D3EE).copy(alpha = 0.4f), start = scTopLeft, end = scTopRight, strokeWidth = 1.dp.toPx())
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun InteractiveThreeJsGlobe(
    selectedTeam: Team?,
    onTeamSelected: (Team) -> Unit,
    theme: GlobeTheme,
    stage: TournamentStage,
    zoomScale: Float,
    modifier: Modifier = Modifier
) {
    val teams = remember { TeamDataProvider.teams }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }

    // Sync selected stage with WebView
    LaunchedEffect(stage) {
        webViewRef?.evaluateJavascript("javascript:setStageFromAndroid('${stage.label}')", null)
    }

    // Sync theme with WebView
    LaunchedEffect(theme) {
        val themeStr = if (theme == GlobeTheme.GLASS_LIGHT) "light" else "dark"
        webViewRef?.evaluateJavascript("javascript:setThemeFromAndroid('$themeStr')", null)
    }

    // Sync zoom with WebView
    LaunchedEffect(zoomScale) {
        webViewRef?.evaluateJavascript("javascript:setZoomFromAndroid($zoomScale)", null)
    }

    // Sync selection with WebView
    LaunchedEffect(selectedTeam) {
        selectedTeam?.let {
            webViewRef?.evaluateJavascript("javascript:selectTeamFromAndroid('${it.abbreviation}')", null)
        }
    }

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    allowFileAccess = true
                    allowContentAccess = true
                    // Enable transparent background so Compose themes shine through
                    setBackgroundColor(0)
                }
                
                // Set up the bridge interface
                addJavascriptInterface(object {
                    @JavascriptInterface
                    fun onCountryClick(abbreviation: String) {
                        // Crucial: run on main thread!
                        post {
                            val matched = teams.find { it.abbreviation == abbreviation }
                            matched?.let { onTeamSelected(it) }
                        }
                    }

                    @JavascriptInterface
                    fun onStadiumClick(stadiumId: String) {
                        // Handled natively within the WebView popup to display stadium info
                    }
                }, "Android")

                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        // Initialize states once page loads
                        val themeStr = if (theme == GlobeTheme.GLASS_LIGHT) "light" else "dark"
                        view?.evaluateJavascript("javascript:setThemeFromAndroid('$themeStr')", null)
                        view?.evaluateJavascript("javascript:setStageFromAndroid('${stage.label}')", null)
                        view?.evaluateJavascript("javascript:setZoomFromAndroid($zoomScale)", null)
                        selectedTeam?.let {
                            view?.evaluateJavascript("javascript:selectTeamFromAndroid('${it.abbreviation}')", null)
                        }
                    }
                }
                
                // Load local assets globe HTML
                loadUrl("file:///android_asset/globe.html")
                webViewRef = this
            }
        },
        update = { webView ->
            // Update can handle standard lifecycle updates if any
        },
        modifier = modifier
    )
}

enum class ProfileTab {
    OVERVIEW, SQUAD, STATS, SCHEDULE, NEWS
}

data class ProfileOverviewStat(
    val emoji: String,
    val value: String,
    val label: String,
    val color: Color
)

@Composable
fun TeamBadge(team: Team, modifier: Modifier = Modifier) {
    // Determine the colors of the badge based on the team's abbreviation
    val stripeColors = when (team.abbreviation) {
        "USA" -> listOf(Color(0xFF1E3A8A), Color.Red, Color.White, Color.Red, Color.White, Color.Red)
        "ARG" -> listOf(Color(0xFF74ACDF), Color.White, Color(0xFF74ACDF))
        "FRA" -> listOf(Color(0xFF1E3A8A), Color.White, Color(0xFFEF4444))
        "ESP" -> listOf(Color(0xFFC2410C), Color(0xFFFBBF24), Color(0xFFC2410C))
        "BRA" -> listOf(Color(0xFFFACC15), Color(0xFF15803D), listOf(Color(0xFFFACC15), Color(0xFF15803D)).random())
        "ENG" -> listOf(Color.White, Color.Red, Color.White)
        "MEX" -> listOf(Color(0xFF15803D), Color.White, Color(0xFFB91C1C))
        "CAN" -> listOf(Color(0xFFB91C1C), Color.White, Color(0xFFB91C1C))
        else -> listOf(Color(0xFF334155), Color(0xFF94A3B8), Color(0xFF64748B))
    }

    val secondaryColor = when (team.abbreviation) {
        "USA" -> Color(0xFF0F172A)
        "ARG" -> Color(0xFF74ACDF)
        "FRA" -> Color(0xFF1E3A8A)
        "ESP" -> Color(0xFFC2410C)
        "BRA" -> Color(0xFFFACC15)
        "ENG" -> Color.White
        "MEX" -> Color(0xFF15803D)
        "CAN" -> Color(0xFFB91C1C)
        else -> Color(0xFF334155)
    }

    Box(
        modifier = modifier
            .size(72.dp)
            .shadow(6.dp, CircleShape)
            .background(secondaryColor, CircleShape)
            .border(2.dp, Color.White, CircleShape)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        // Draw stripes or custom pattern inside the circle
        Canvas(modifier = Modifier.fillMaxSize().clip(CircleShape)) {
            val numStripes = stripeColors.size
            val stripeWidth = size.width / numStripes
            for (i in 0 until numStripes) {
                drawRect(
                    color = stripeColors[i],
                    topLeft = Offset(i * stripeWidth, 0f),
                    size = Size(stripeWidth, size.height)
                )
            }
        }
        // Overlay the country flag emoji in the center
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(Color.White.copy(alpha = 0.9f), CircleShape)
                .border(1.dp, Color.LightGray.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = team.flag, fontSize = 24.sp)
        }
    }
}

@Composable
fun MapPinMiniMap(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .size(140.dp, 100.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
    ) {
        // Soft green/gray map background
        drawRect(Color(0xFFE8F5E9))
        
        // Draw elegant grid lines representing roads
        val path = Path()
        // Horizontal roads
        path.moveTo(0f, size.height * 0.2f)
        path.quadraticTo(size.width * 0.5f, size.height * 0.25f, size.width, size.height * 0.15f)
        path.moveTo(0f, size.height * 0.5f)
        path.quadraticTo(size.width * 0.4f, size.height * 0.45f, size.width, size.height * 0.55f)
        path.moveTo(0f, size.height * 0.8f)
        path.quadraticTo(size.width * 0.5f, size.height * 0.85f, size.width, size.height * 0.75f)
        
        // Vertical roads
        path.moveTo(size.width * 0.25f, 0f)
        path.quadraticTo(size.width * 0.2f, size.height * 0.5f, size.width * 0.3f, size.height)
        path.moveTo(size.width * 0.5f, 0f)
        path.quadraticTo(size.width * 0.55f, size.height * 0.4f, size.width * 0.45f, size.height)
        path.moveTo(size.width * 0.75f, 0f)
        path.quadraticTo(size.width * 0.7f, size.height * 0.6f, size.width * 0.8f, size.height)
        
        drawPath(
            path = path,
            color = Color.White,
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round, join = androidx.compose.ui.graphics.StrokeJoin.Round)
        )
        
        // Let's draw a beautiful navy map pin in the middle (just like the attachment)
        val pinX = size.width / 2f
        val pinY = size.height / 2f
        
        // Draw the pin shadow
        drawOval(
            color = Color.Black.copy(alpha = 0.15f),
            topLeft = Offset(pinX - 10f, pinY + 6f),
            size = Size(20f, 8f)
        )
        
        // Draw the main pin drop shape
        val pinPath = Path().apply {
            moveTo(pinX, pinY)
            cubicTo(pinX - 16f, pinY - 18f, pinX - 16f, pinY - 36f, pinX, pinY - 36f)
            cubicTo(pinX + 16f, pinY - 36f, pinX + 16f, pinY - 18f, pinX, pinY)
            close()
        }
        drawPath(
            path = pinPath,
            color = Color(0xFF1E3A8A) // deep blue pin
        )
        
        // Draw white circle inside the pin
        drawCircle(
            color = Color.White,
            radius = 5.dp.toPx(),
            center = Offset(pinX, pinY - 24f)
        )
    }
}

