package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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

    val accentColor = when (currentTheme) {
        GlobeTheme.GLASS_LIGHT -> Color(0xFF0D9488) // Soft Teal
        GlobeTheme.COSMIC_DARK -> Color(0xFFF59E0B) // Amber
    }

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

                    Button(
                        onClick = { isCompareDrawerOpen = !isCompareDrawerOpen },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accentColor,
                            contentColor = if (currentTheme == GlobeTheme.GLASS_LIGHT) Color.White else Color.Black
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier
                            .height(36.dp)
                            .testTag("compare_drawer_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Compare,
                            contentDescription = "Compare Teams",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isCompareDrawerOpen) "Close" else "Compare",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
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

            // COMPARISON OVERLAY SHEET / EXPANDABLE DRAWER
            AnimatedVisibility(
                visible = isCompareDrawerOpen,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    // Team Select Dropdowns Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .shadow(2.dp, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = cardBgColor),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, textColor.copy(alpha = 0.1f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "⚔️ Choose Teams to Compare",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = textColor.copy(alpha = 0.6f),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Dropdown team 1
                                Box(modifier = Modifier.weight(1f)) {
                                    TeamDropdown(
                                        selectedTeam = compareTeam1,
                                        allTeams = teams,
                                        textColor = textColor,
                                        cardBgColor = cardBgColor,
                                        onSelect = { compareTeam1 = it }
                                    )
                                }

                                Text(
                                    text = "VS",
                                    fontWeight = FontWeight.Black,
                                    color = accentColor,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )

                                // Dropdown team 2
                                Box(modifier = Modifier.weight(1f)) {
                                    TeamDropdown(
                                        selectedTeam = compareTeam2,
                                        allTeams = teams,
                                        textColor = textColor,
                                        cardBgColor = cardBgColor,
                                        onSelect = { compareTeam2 = it }
                                    )
                                }
                            }
                        }
                    }

                    // Interactive H2H Tactical Arena View
                    if (compareTeam1 != null && compareTeam2 != null) {
                        TacticalH2HArena(
                            team1 = compareTeam1!!,
                            team2 = compareTeam2!!,
                            currentTheme = currentTheme,
                            textColor = textColor,
                            cardBgColor = cardBgColor,
                            accentColor = accentColor,
                            onCloseRequest = { isCompareDrawerOpen = false }
                        )
                    }
                }
            }

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
                        val glowBrush = if (currentTheme == GlobeTheme.GLASS_LIGHT) {
                            Brush.radialGradient(
                                colors = listOf(Color(0xFF80CBC4).copy(alpha = 0.25f), Color.Transparent),
                                center = Offset(centerX, centerY),
                                radius = currentRadius * 1.5f
                            )
                        } else {
                            Brush.radialGradient(
                                colors = listOf(Color(0xFFFFB300).copy(alpha = 0.15f), Color.Transparent),
                                center = Offset(centerX, centerY),
                                radius = currentRadius * 1.5f
                            )
                        }
                        drawCircle(brush = glowBrush, radius = currentRadius * 1.5f, center = Offset(centerX, centerY))

                        // 2. Draw the shaded Base Sphere Circle
                        val sphereColor = if (currentTheme == GlobeTheme.GLASS_LIGHT) {
                            Color.White.copy(alpha = 0.5f)
                        } else {
                            Color(0xFF0F172A).copy(alpha = 0.6f)
                        }
                        drawCircle(color = sphereColor, radius = currentRadius, center = Offset(centerX, centerY))

                        // 3. Draw outer atmospheric ring
                        val ringColor = if (currentTheme == GlobeTheme.GLASS_LIGHT) {
                            Color(0xFF00BFA5).copy(alpha = 0.4f)
                        } else {
                            Color(0xFFFFB300).copy(alpha = 0.4f)
                        }
                        drawCircle(
                            color = ringColor,
                            radius = currentRadius,
                            center = Offset(centerX, centerY),
                            style = Stroke(width = 2.dp.toPx())
                        )

                        // 4. DRAW 3D GRIDLINES (PARALLELS & MERIDIANS)
                        val gridLineColor = if (currentTheme == GlobeTheme.GLASS_LIGHT) {
                            Color(0xFF00BFA5).copy(alpha = 0.15f)
                        } else {
                            Color(0xFFFFB300).copy(alpha = 0.15f)
                        }

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
                            .shadow(6.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                        colors = CardDefaults.cardColors(containerColor = cardBgColor),
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 16.dp, bottomEnd = 16.dp),
                        border = BorderStroke(1.dp, textColor.copy(alpha = 0.1f))
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 280.dp)
                                .padding(16.dp)
                        ) {
                            // Header with Flag, Name, and Coach
                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = team.flag, fontSize = 28.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = "${team.name} Profile",
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 18.sp,
                                                color = textColor
                                            )
                                            Text(
                                                text = "Manager: ${team.coach} | FIFA Rank: #${team.fifaRanking}",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = textColor.copy(alpha = 0.6f)
                                            )
                                        }
                                    }
                                    IconButton(
                                        onClick = { selectedTeam = null },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Close details",
                                            tint = textColor.copy(alpha = 0.6f)
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = team.profile,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    color = textColor.copy(alpha = 0.8f)
                                )
                                
                                Divider(modifier = Modifier.padding(vertical = 12.dp), color = textColor.copy(alpha = 0.1f))
                            }

                            // TOURNAMENT STATS & FORM BAR
                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = "📊 TOURNAMENT STATS", fontSize = 11.sp, fontWeight = FontWeight.Black, color = accentColor)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        StatRow(label = "Goals Scored", value = "${team.stats.goalsScored}", color = textColor)
                                        StatRow(label = "Wins / Draws / Losses", value = "${team.stats.wins}W / 1D / 0L", color = textColor)
                                        StatRow(label = "Possession Avg", value = "${team.stats.possessionPercent}%", color = textColor)
                                        StatRow(label = "Shots on Target", value = "${team.stats.shotsOnTarget}", color = textColor)
                                        StatRow(label = "Clean Sheets", value = "${team.stats.cleanSheets}", color = textColor)
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = "📈 PATH TO QUARTER", fontSize = 11.sp, fontWeight = FontWeight.Black, color = accentColor)
                                        Spacer(modifier = Modifier.height(6.dp))
                                        team.path.forEach { stageResult ->
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding(bottom = 4.dp)
                                            ) {
                                                Text(text = "🔹", fontSize = 10.sp, modifier = Modifier.padding(end = 4.dp))
                                                Text(text = stageResult, fontSize = 10.sp, color = textColor.copy(alpha = 0.8f), fontWeight = FontWeight.Medium)
                                            }
                                        }
                                    }
                                }
                                Divider(modifier = Modifier.padding(vertical = 12.dp), color = textColor.copy(alpha = 0.1f))
                            }

                            // PLAYERS ROSTER (horizontal list of Star Players)
                            item {
                                Text(
                                    text = "⭐ STAR PLAYER ROSTER",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = accentColor,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                LazyRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    items(team.keyPlayers) { player ->
                                        Card(
                                            colors = CardDefaults.cardColors(
                                                containerColor = textColor.copy(alpha = 0.04f)
                                            ),
                                            shape = RoundedCornerShape(12.dp),
                                            modifier = Modifier
                                                .width(140.dp)
                                                .border(1.dp, textColor.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                                        ) {
                                            Column(modifier = Modifier.padding(10.dp)) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    // Simulated Avatar with custom initials
                                                    Box(
                                                        modifier = Modifier
                                                            .size(24.dp)
                                                            .background(accentColor, CircleShape),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Text(
                                                            text = player.name.split(" ").mapNotNull { it.firstOrNull() }.joinToString("").take(2),
                                                            color = if (currentTheme == GlobeTheme.GLASS_LIGHT) Color.White else Color.Black,
                                                            fontSize = 8.sp,
                                                            fontWeight = FontWeight.Black
                                                        )
                                                    }
                                                    Text(
                                                        text = "#${player.number}",
                                                        fontWeight = FontWeight.Black,
                                                        fontSize = 11.sp,
                                                        color = accentColor
                                                    )
                                                }
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = player.name,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 11.sp,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    color = textColor
                                                )
                                                Text(
                                                    text = player.position,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 9.sp,
                                                    color = textColor.copy(alpha = 0.5f)
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = player.description,
                                                    fontSize = 8.sp,
                                                    lineHeight = 11.sp,
                                                    color = textColor.copy(alpha = 0.6f),
                                                    maxLines = 3,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                    }
                                }
                                Divider(modifier = Modifier.padding(vertical = 12.dp), color = textColor.copy(alpha = 0.1f))
                            }

                            // MEDICAL / INJURY REPORT
                            if (team.injuries.isNotEmpty()) {
                                item {
                                    Text(
                                        text = "🏥 MEDICAL & INJURY REPORT",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFFC62828),
                                        modifier = Modifier.padding(bottom = 6.dp)
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        team.injuries.forEach { injury ->
                                            Card(
                                                colors = CardDefaults.cardColors(
                                                    containerColor = Color(0xFFC62828).copy(alpha = 0.05f)
                                                ),
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .border(1.dp, Color(0xFFC62828).copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                            ) {
                                                Column(modifier = Modifier.padding(8.dp)) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Text(text = "🚨", fontSize = 10.sp)
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text(text = injury.name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor)
                                                    }
                                                    Spacer(modifier = Modifier.height(2.dp))
                                                    Text(text = injury.injuryType, fontSize = 9.sp, color = textColor.copy(alpha = 0.7f), fontWeight = FontWeight.Medium)
                                                    Text(text = injury.returnEstimate, fontSize = 8.sp, color = Color(0xFFC62828), fontWeight = FontWeight.Bold)
                                                }
                                            }
                                        }
                                    }
                                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = textColor.copy(alpha = 0.1f))
                                }
                            }

                            // NEXT MATCH SCHEDULE, STADIUM BLUEPRINT MAP & WEATHER
                            item {
                                Text(
                                    text = "🏟️ NEXT UPCOMING MATCH DETAIL",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = accentColor,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // Match Card Detail
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "vs ${team.nextMatch.opponent}",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 14.sp,
                                            color = textColor
                                        )
                                        Text(
                                            text = "📅 ${team.nextMatch.date}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = textColor.copy(alpha = 0.8f)
                                        )
                                        Text(
                                            text = "⏰ ${team.nextMatch.time}",
                                            fontSize = 10.sp,
                                            color = textColor.copy(alpha = 0.6f)
                                        )
                                        
                                        Spacer(modifier = Modifier.height(6.dp))
                                        
                                        // Weather dynamic card
                                        Card(
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color(0xFF0D9488).copy(alpha = 0.08f)
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = when (team.nextMatch.stadium.weatherCondition) {
                                                        "Sunny & Clear" -> "☀️"
                                                        "Partly Cloudy" -> "⛅"
                                                        "Humid & Showers" -> "🌧️"
                                                        else -> "🌧️"
                                                    },
                                                    fontSize = 20.sp
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Column {
                                                    Text(
                                                        text = team.nextMatch.stadium.weatherTemp,
                                                        fontWeight = FontWeight.Black,
                                                        fontSize = 11.sp,
                                                        color = textColor
                                                    )
                                                    Text(
                                                        text = team.nextMatch.stadium.weatherCondition,
                                                        fontSize = 8.sp,
                                                        color = textColor.copy(alpha = 0.6f)
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    // Mini blueprint-style pitch outline drawing on Canvas!
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = team.nextMatch.stadium.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = textColor,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "${team.nextMatch.stadium.city} • Cap: ${team.nextMatch.stadium.capacity}",
                                            fontSize = 9.sp,
                                            color = textColor.copy(alpha = 0.6f),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        StadiumMiniMap()
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
    Canvas(
        modifier = Modifier
            .size(150.dp, 80.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
    ) {
        val fieldColor = Color(0xFF1B5E20)
        val lineColor = Color.White.copy(alpha = 0.5f)
        
        // Background grass field
        drawRect(color = fieldColor)
        
        // Outer boundaries
        drawRect(
            color = lineColor,
            topLeft = Offset(10f, 10f),
            size = Size(size.width - 20f, size.height - 20f),
            style = Stroke(width = 1.5.dp.toPx())
        )
        
        // Halfway line
        drawLine(
            color = lineColor,
            start = Offset(size.width / 2f, 10f),
            end = Offset(size.width / 2f, size.height - 10f),
            strokeWidth = 1.5.dp.toPx()
        )
        
        // Center Circle
        drawCircle(
            color = lineColor,
            radius = 16.dp.toPx(),
            center = Offset(size.width / 2f, size.height / 2f),
            style = Stroke(width = 1.5.dp.toPx())
        )
        
        // Penalty Area Left
        drawRect(
            color = lineColor,
            topLeft = Offset(10f, size.height / 4f),
            size = Size(20.dp.toPx(), size.height / 2f),
            style = Stroke(width = 1.5.dp.toPx())
        )
        
        // Penalty Area Right
        drawRect(
            color = lineColor,
            topLeft = Offset(size.width - 10f - 20.dp.toPx(), size.height / 4f),
            size = Size(20.dp.toPx(), size.height / 2f),
            style = Stroke(width = 1.5.dp.toPx())
        )

        // Floating seating rings
        drawRoundRect(
            color = Color.White.copy(alpha = 0.15f),
            topLeft = Offset(-10f, -10f),
            size = Size(size.width + 20f, size.height + 20f),
            cornerRadius = CornerRadius(12f, 12f),
            style = Stroke(width = 1f)
        )
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
