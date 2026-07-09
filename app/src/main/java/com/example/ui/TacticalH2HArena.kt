package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Player
import com.example.model.Team
import com.example.service.GeminiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

enum class ArenaTab {
    ANALYTICS,
    LINEUPS,
    SIMULATOR
}

@Composable
fun TacticalH2HArena(
    team1: Team,
    team2: Team,
    currentTheme: GlobeTheme,
    textColor: Color,
    cardBgColor: Color,
    accentColor: Color,
    onCloseRequest: () -> Unit
) {
    var activeTab by remember { mutableStateOf(ArenaTab.ANALYTICS) }
    val coroutineScope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(24.dp))
            .testTag("tactical_h2h_arena_card"),
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, textColor.copy(alpha = 0.15f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Screen Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "⚔️",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Column {
                        Text(
                            text = "Tactical H2H Arena",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = textColor
                        )
                        Text(
                            text = "${team1.flag} ${team1.name} vs ${team2.name} ${team2.flag}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentColor
                        )
                    }
                }
                
                IconButton(
                    onClick = onCloseRequest,
                    modifier = Modifier
                        .size(32.dp)
                        .background(textColor.copy(alpha = 0.05f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Arena",
                        tint = textColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sub-navigation tab bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(textColor.copy(alpha = 0.04f))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ArenaTab.entries.forEach { tab ->
                    val isSelected = activeTab == tab
                    val tabBg = if (isSelected) accentColor else Color.Transparent
                    val tabContentColor = if (isSelected) {
                        if (currentTheme == GlobeTheme.GLASS_LIGHT) Color.White else Color(0xFF0F172A)
                    } else {
                        textColor.copy(alpha = 0.7f)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(tabBg)
                            .clickable { activeTab = tab }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (tab) {
                                ArenaTab.ANALYTICS -> "📊 Stats"
                                ArenaTab.LINEUPS -> "🛡️ Lineups"
                                ArenaTab.SIMULATOR -> "🎮 Match Sim"
                            },
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                            fontSize = 11.sp,
                            color = tabContentColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Content Area based on Tab
            AnimatedContent(
                targetState = activeTab,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
                },
                label = "ArenaTabAnimation"
            ) { tabState ->
                when (tabState) {
                    ArenaTab.ANALYTICS -> StatAnalyticsTab(
                        team1 = team1,
                        team2 = team2,
                        currentTheme = currentTheme,
                        textColor = textColor,
                        accentColor = accentColor,
                        cardBgColor = cardBgColor
                    )
                    ArenaTab.LINEUPS -> TacticalLineupsTab(
                        team1 = team1,
                        team2 = team2,
                        currentTheme = currentTheme,
                        textColor = textColor,
                        accentColor = accentColor
                    )
                    ArenaTab.SIMULATOR -> MatchSimulatorTab(
                        team1 = team1,
                        team2 = team2,
                        currentTheme = currentTheme,
                        textColor = textColor,
                        accentColor = accentColor
                    )
                }
            }
        }
    }
}

// ==================== TAB 1: STAT ANALYTICS (RADAR CHART) ====================

@Composable
fun StatAnalyticsTab(
    team1: Team,
    team2: Team,
    currentTheme: GlobeTheme,
    textColor: Color,
    accentColor: Color,
    cardBgColor: Color
) {
    val coroutineScope = rememberCoroutineScope()
    var aiAnalysisText by remember { mutableStateOf<String?>(null) }
    var isAiLoading by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Interactive Radar Comparison",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = textColor.copy(alpha = 0.8f),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Custom Radar Chart on Compose Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            RadarChart(
                team1 = team1,
                team2 = team2,
                currentTheme = currentTheme,
                textColor = textColor,
                accentColor = accentColor
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Quick Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color(0xFF0D9488), RoundedCornerShape(2.dp))
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = team1.name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor)
            }
            Spacer(modifier = Modifier.width(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color(0xFFF59E0B), RoundedCornerShape(2.dp))
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = team2.name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tug-of-war Stat Rows
        Text(
            text = "H2H Tug of War Metrics",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = textColor.copy(alpha = 0.8f),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        H2HBar(
            label = "Goals Scored",
            val1 = team1.stats.goalsScored,
            val2 = team2.stats.goalsScored,
            accentColor = accentColor,
            textColor = textColor
        )
        H2HBar(
            label = "Total Wins",
            val1 = team1.stats.wins,
            val2 = team2.stats.wins,
            accentColor = accentColor,
            textColor = textColor
        )
        H2HBar(
            label = "Average Possession %",
            val1 = team1.stats.possessionPercent,
            val2 = team2.stats.possessionPercent,
            suffix = "%",
            accentColor = accentColor,
            textColor = textColor
        )
        H2HBar(
            label = "Shots on Target",
            val1 = team1.stats.shotsOnTarget,
            val2 = team2.stats.shotsOnTarget,
            accentColor = accentColor,
            textColor = textColor
        )
        H2HBar(
            label = "Clean Sheets",
            val1 = team1.stats.cleanSheets,
            val2 = team2.stats.cleanSheets,
            accentColor = accentColor,
            textColor = textColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Decider Gemini Advice Row
        Button(
            onClick = {
                isAiLoading = true
                aiAnalysisText = null
                coroutineScope.launch {
                    val s1 = "${team1.name} (Rank ${team1.fifaRanking}, Form: ${team1.form.joinToString("-")}, Goals: ${team1.stats.goalsScored}, Possession: ${team1.stats.possessionPercent}%)"
                    val s2 = "${team2.name} (Rank ${team2.fifaRanking}, Form: ${team2.form.joinToString("-")}, Goals: ${team2.stats.goalsScored}, Possession: ${team2.stats.possessionPercent}%)"
                    aiAnalysisText = GeminiService.getComparisonAdvice(
                        team1Name = team1.name,
                        team2Name = team2.name,
                        stats1 = s1,
                        stats2 = s2
                    )
                    isAiLoading = false
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("ai_decide_button_arena")
        ) {
            if (isAiLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("AI Reviewing Tactical Data...", fontSize = 12.sp)
            } else {
                Text("✨ Ask Gemini Tactical Advisor", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }

        if (aiAnalysisText != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .border(
                        width = 1.dp,
                        color = accentColor.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = if (currentTheme == GlobeTheme.GLASS_LIGHT) Color(0xFFF0FDFA) else Color(0xFF131E35)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "🤖 Gemini Sportscast Breakdown",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = accentColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = aiAnalysisText!!,
                        fontSize = 11.sp,
                        lineHeight = 15.sp,
                        color = textColor,
                        modifier = Modifier.testTag("ai_response_text_arena")
                    )
                }
            }
        }
    }
}

@Composable
fun RadarChart(
    team1: Team,
    team2: Team,
    currentTheme: GlobeTheme,
    textColor: Color,
    accentColor: Color
) {
    val textMeasurer = rememberTextMeasurer()

    // Normalized stats: Attack, Defense, Midfield Control, Shooting Precision, Form
    val stats1 = listOf(
        team1.stats.goalsScored / 20.0f,
        team1.stats.cleanSheets / 5.0f,
        (team1.stats.possessionPercent - 40f) / 30f,
        team1.stats.shotsOnTarget / 25.0f,
        team1.form.count { it == "W" } / 5.0f
    )

    val stats2 = listOf(
        team2.stats.goalsScored / 20.0f,
        team2.stats.cleanSheets / 5.0f,
        (team2.stats.possessionPercent - 40f) / 30f,
        team2.stats.shotsOnTarget / 25.0f,
        team2.form.count { it == "W" } / 5.0f
    )

    val dimensions = listOf("ATTACK", "DEFENSE", "MIDFIELD", "PRECISION", "FORM")

    Canvas(
        modifier = Modifier
            .size(180.dp)
            .testTag("canvas_radar_chart")
    ) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = size.minDimension / 2.3f

        // Draw concentric pentagons as background grid
        val gridLevels = 4
        val gridColor = textColor.copy(alpha = 0.08f)
        for (i in 1..gridLevels) {
            val levelRadius = radius * (i.toFloat() / gridLevels)
            val path = Path()
            for (j in 0..4) {
                val angleDeg = -90f + j * 72f
                val angleRad = Math.toRadians(angleDeg.toDouble())
                val x = center.x + levelRadius * cos(angleRad).toFloat()
                val y = center.y + levelRadius * sin(angleRad).toFloat()
                if (j == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()
            drawPath(path = path, color = gridColor, style = Stroke(width = 1.dp.toPx()))
        }

        // Draw radial gridlines
        for (j in 0..4) {
            val angleDeg = -90f + j * 72f
            val angleRad = Math.toRadians(angleDeg.toDouble())
            val x = center.x + radius * cos(angleRad).toFloat()
            val y = center.y + radius * sin(angleRad).toFloat()
            drawLine(
                color = textColor.copy(alpha = 0.12f),
                start = center,
                end = Offset(x, y),
                strokeWidth = 1.dp.toPx()
            )
            
            // Draw axis title labels with TextMeasurer
            val label = dimensions[j]
            val textLayoutResult = textMeasurer.measure(
                text = label,
                style = TextStyle(
                    color = textColor.copy(alpha = 0.5f),
                    fontSize = 7.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace
                )
            )
            // Position label outside the pentagon point
            val labelRadius = radius + 15.dp.toPx()
            val lx = center.x + labelRadius * cos(angleRad).toFloat() - textLayoutResult.size.width / 2f
            val ly = center.y + labelRadius * sin(angleRad).toFloat() - textLayoutResult.size.height / 2f
            drawText(textLayoutResult, topLeft = Offset(lx, ly))
        }

        // Helper to draw team shape
        fun drawTeamPolygon(stats: List<Float>, color: Color) {
            val path = Path()
            for (j in 0..4) {
                val valNormalized = stats[j].coerceIn(0.1f, 1.0f)
                val angleDeg = -90f + j * 72f
                val angleRad = Math.toRadians(angleDeg.toDouble())
                val x = center.x + (radius * valNormalized) * cos(angleRad).toFloat()
                val y = center.y + (radius * valNormalized) * sin(angleRad).toFloat()
                if (j == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()

            // Draw translucent fill
            drawPath(path = path, color = color.copy(alpha = 0.25f))
            // Draw bright outline
            drawPath(path = path, color = color, style = Stroke(width = 2.dp.toPx()))
            
            // Draw dots at vertices
            for (j in 0..4) {
                val valNormalized = stats[j].coerceIn(0.1f, 1.0f)
                val angleDeg = -90f + j * 72f
                val angleRad = Math.toRadians(angleDeg.toDouble())
                val x = center.x + (radius * valNormalized) * cos(angleRad).toFloat()
                val y = center.y + (radius * valNormalized) * sin(angleRad).toFloat()
                drawCircle(color = color, radius = 3.dp.toPx(), center = Offset(x, y))
            }
        }

        // Draw Team 1 (Teal)
        drawTeamPolygon(stats1, Color(0xFF0D9488))

        // Draw Team 2 (Amber)
        drawTeamPolygon(stats2, Color(0xFFF59E0B))
    }
}

// ==================== TAB 2: TACTICAL LINEUPS (SOCCER FIELD) ====================

@Composable
fun TacticalLineupsTab(
    team1: Team,
    team2: Team,
    currentTheme: GlobeTheme,
    textColor: Color,
    accentColor: Color
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🛡️ Formations Clash",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = textColor.copy(alpha = 0.8f)
            )
            Text(
                text = "Coach clash: ${team1.coach} vs ${team2.coach}",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor.copy(alpha = 0.5f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Blueprint Soccer Pitch with players plotted
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.2.dp, textColor.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                .testTag("canvas_tactical_field")
        ) {
            val pitchColor = if (currentTheme == GlobeTheme.GLASS_LIGHT) {
                Color(0xFFE2F1ED)
            } else {
                Color(0xFF132321)
            }
            val lineColor = textColor.copy(alpha = 0.25f)
            
            // Draw grassy floor
            drawRect(color = pitchColor)

            // Outer field margins (10dp)
            val m = 10.dp.toPx()
            val w = size.width - 2 * m
            val h = size.height - 2 * m

            // Boundary rectangle
            drawRect(
                color = lineColor,
                topLeft = Offset(m, m),
                size = Size(w, h),
                style = Stroke(width = 1.dp.toPx())
            )

            // Halfway line (Vertical down the center)
            drawLine(
                color = lineColor,
                start = Offset(size.width / 2f, m),
                end = Offset(size.width / 2f, size.height - m),
                strokeWidth = 1.dp.toPx()
            )

            // Center Circle
            drawCircle(
                color = lineColor,
                radius = 28.dp.toPx(),
                center = Offset(size.width / 2f, size.height / 2f),
                style = Stroke(width = 1.dp.toPx())
            )

            // Center spot
            drawCircle(
                color = lineColor,
                radius = 2.dp.toPx(),
                center = Offset(size.width / 2f, size.height / 2f)
            )

            // Penalty Area Left (Team 1)
            drawRect(
                color = lineColor,
                topLeft = Offset(m, size.height / 4f),
                size = Size(35.dp.toPx(), size.height / 2f),
                style = Stroke(width = 1.dp.toPx())
            )

            // Penalty Area Right (Team 2)
            drawRect(
                color = lineColor,
                topLeft = Offset(size.width - m - 35.dp.toPx(), size.height / 4f),
                size = Size(35.dp.toPx(), size.height / 2f),
                style = Stroke(width = 1.dp.toPx())
            )

            // Goalpost outlines (external)
            drawRect(
                color = lineColor,
                topLeft = Offset(m - 4.dp.toPx(), size.height / 2.5f),
                size = Size(4.dp.toPx(), size.height / 5f),
                style = Stroke(width = 1.dp.toPx())
            )
            drawRect(
                color = lineColor,
                topLeft = Offset(size.width - m, size.height / 2.5f),
                size = Size(4.dp.toPx(), size.height / 5f),
                style = Stroke(width = 1.dp.toPx())
            )

            // Plot key players from both teams on field!
            // Team 1 plays Left-to-Right, Team 2 plays Right-to-Left
            // Dynamic placement based on player position: GK, DF, MF, FW

            fun getPlayerCoordinates(teamSide: Int, pos: String): Offset {
                val cx = size.width / 2f
                val cy = size.height / 2f
                val padding = 20.dp.toPx()
                
                return if (teamSide == 1) { // Left team (Team 1)
                    when (pos.uppercase()) {
                        "GK" -> Offset(m + 15.dp.toPx(), cy)
                        "DF" -> Offset(m + 45.dp.toPx(), cy + 30.dp.toPx())
                        "MF" -> Offset(cx - 30.dp.toPx(), cy - 10.dp.toPx())
                        "FW" -> Offset(cx - 15.dp.toPx(), cy + 40.dp.toPx())
                        else -> Offset(m + 60.dp.toPx(), cy)
                    }
                } else { // Right team (Team 2)
                    when (pos.uppercase()) {
                        "GK" -> Offset(size.width - m - 15.dp.toPx(), cy)
                        "DF" -> Offset(size.width - m - 45.dp.toPx(), cy - 30.dp.toPx())
                        "MF" -> Offset(cx + 30.dp.toPx(), cy + 10.dp.toPx())
                        "FW" -> Offset(cx + 15.dp.toPx(), cy - 40.dp.toPx())
                        else -> Offset(size.width - m - 60.dp.toPx(), cy)
                    }
                }
            }

            // Draw Team 1 Key Players
            team1.keyPlayers.forEach { player ->
                val coords = getPlayerCoordinates(1, player.position)
                drawCircle(
                    color = Color(0xFF0D9488),
                    radius = 8.dp.toPx(),
                    center = coords
                )
                drawCircle(
                    color = Color.White,
                    radius = 8.dp.toPx(),
                    center = coords,
                    style = Stroke(width = 1.5.dp.toPx())
                )
            }

            // Draw Team 2 Key Players
            team2.keyPlayers.forEach { player ->
                val coords = getPlayerCoordinates(2, player.position)
                drawCircle(
                    color = Color(0xFFF59E0B),
                    radius = 8.dp.toPx(),
                    center = coords
                )
                drawCircle(
                    color = Color.White,
                    radius = 8.dp.toPx(),
                    center = coords,
                    style = Stroke(width = 1.5.dp.toPx())
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Side-by-side key player list details with matching jersey indicators
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // Team 1 Stars
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = team1.flag, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = team1.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textColor)
                }
                Spacer(modifier = Modifier.height(4.dp))
                team1.keyPlayers.forEach { player ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(Color(0xFF0D9488), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = player.number.toString(), color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(text = player.name, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = textColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(text = player.position, fontSize = 9.sp, color = textColor.copy(alpha = 0.5f))
                        }
                    }
                }
            }

            // Team 2 Stars
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.align(Alignment.End)) {
                    Text(text = team2.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textColor)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = team2.flag, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                team2.keyPlayers.forEach { player ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(1f)) {
                            Text(text = player.name, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = textColor, maxLines = 1, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.End)
                            Text(text = player.position, fontSize = 9.sp, color = textColor.copy(alpha = 0.5f), textAlign = TextAlign.End)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(Color(0xFFF59E0B), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = player.number.toString(), color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// ==================== TAB 3: RETRO-TICKER MATCH SIMULATOR ====================

data class CommentaryLog(
    val minute: Int,
    val isGoal: Boolean,
    val text: String
)

@Composable
fun MatchSimulatorTab(
    team1: Team,
    team2: Team,
    currentTheme: GlobeTheme,
    textColor: Color,
    accentColor: Color
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()

    var isSimulating by remember { mutableStateOf(false) }
    var currentMin by remember { mutableStateOf(0) }
    var score1 by remember { mutableStateOf(0) }
    var score2 by remember { mutableStateOf(0) }
    var simulationLogs = remember { mutableStateListOf<CommentaryLog>() }
    var simCompleted by remember { mutableStateOf(false) }

    // Generates a random commentary sequence based on teams, players, and stats
    fun runGameSimulation() {
        if (isSimulating) return
        
        isSimulating = true
        simCompleted = false
        currentMin = 0
        score1 = 0
        score2 = 0
        simulationLogs.clear()

        coroutineScope.launch {
            simulationLogs.add(CommentaryLog(0, false, "🟢 Kickoff! The referee blows the whistle. ${team1.name} and ${team2.name} clash in front of a capacity crowd!"))
            
            val t1Stars = team1.keyPlayers
            val t2Stars = team2.keyPlayers
            val stadium = team1.nextMatch.stadium

            // Probability weights based on stats
            val attack1 = team1.stats.goalsScored
            val attack2 = team2.stats.goalsScored
            val def1 = team1.stats.cleanSheets
            val def2 = team2.stats.cleanSheets

            // Simulate minutes
            val minutesToSimulate = listOf(5, 12, 24, 38, 45, 54, 67, 78, 85, 90)
            
            for (min in minutesToSimulate) {
                delay(900) // Fast pacing but legible
                currentMin = min

                val randomChance = (1..10).random()
                if (randomChance <= 4) {
                    // Attack event from Team 1
                    val isGoal = (1..10).random() > (6 + def2 - attack1 / 5).coerceIn(2, 9)
                    val star = t1Stars.random().name
                    if (isGoal) {
                        score1++
                        simulationLogs.add(CommentaryLog(min, true, "⚽ GOAL!!! SENSATIONAL! $star unleashes a lethal strike into the top corner! ${team1.name} leads!"))
                    } else {
                        simulationLogs.add(CommentaryLog(min, false, "🧤 CHANCE! $star gets past the defense but the goalkeeper pulls off a miraculous diving save!"))
                    }
                } else if (randomChance <= 8) {
                    // Attack event from Team 2
                    val isGoal = (1..10).random() > (6 + def1 - attack2 / 5).coerceIn(2, 9)
                    val star = t2Stars.random().name
                    if (isGoal) {
                        score2++
                        simulationLogs.add(CommentaryLog(min, true, "⚽ GOAL!!! UNBELIEVABLE! $star breaks the line and clinically slides it home for ${team2.name}!"))
                    } else {
                        simulationLogs.add(CommentaryLog(min, false, "🧱 BLOCKED! $star unleashes a rocket but it's heroically blocked by an elite sliding tackle!"))
                    }
                } else {
                    // Midfield action / yellow cards
                    val randomMidfieldText = listOf(
                        "🟨 Tactical foul. A yellow card is brandished in the center circle.",
                        "🔄 Tactical shifts. Both managers are shouting commands passionately from the touchlines.",
                        "📐 Corner kick swung in with massive trajectory, but cleared away safely.",
                        "💨 Fast break! Blinding speed on the flank but the pass goes out of play."
                    ).random()
                    simulationLogs.add(CommentaryLog(min, false, "$randomMidfieldText"))
                }
                
                // Auto scroll to bottom
                coroutineScope.launch {
                    if (simulationLogs.isNotEmpty()) {
                        scrollState.animateScrollToItem(simulationLogs.size - 1)
                    }
                }
            }

            delay(1000)
            simCompleted = true
            isSimulating = false
            simulationLogs.add(CommentaryLog(90, false, "🏁 Full Time! The referee blows the final whistle! Match ends: ${team1.name} $score1 - $score2 ${team2.name}."))
            scrollState.animateScrollToItem(simulationLogs.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        // Blueprint Electronic LED Scoreboard
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.Black),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isSimulating) "🔴 LIVE SIMULATION" else if (simCompleted) "🏁 FINAL SCORE" else "🎯 READY FOR KICKOFF",
                    color = if (isSimulating) Color.Red else accentColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Team 1
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = team1.flag, fontSize = 28.sp)
                        Text(text = team1.abbreviation, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    // Scoreboard digit
                    Text(
                        text = "$score1 - $score2",
                        color = Color(0xFF39FF14), // Neon green LED glow
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )

                    // Team 2
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = team2.flag, fontSize = 28.sp)
                        Text(text = team2.abbreviation, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Time ticker
                Text(
                    text = "TIME: $currentMin'",
                    color = Color.Red.copy(alpha = 0.85f),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Trigger Button
        Button(
            onClick = { runGameSimulation() },
            enabled = !isSimulating,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSimulating) textColor.copy(alpha = 0.1f) else accentColor,
                disabledContainerColor = accentColor.copy(alpha = 0.4f)
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("simulate_match_button")
        ) {
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Simulate", modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (isSimulating) "Match in Progress..." else "🔥 RUN LIVE TACTICAL SIMULATION",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Match Commentary Log Scroll (Retro glass ticker style)
        Text(
            text = "📻 Live Commentary Feed",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = textColor.copy(alpha = 0.8f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp),
            colors = CardDefaults.cardColors(
                containerColor = textColor.copy(alpha = 0.03f)
            ),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, textColor.copy(alpha = 0.1f))
        ) {
            if (simulationLogs.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Press play above to run a stats-driven simulated clash!",
                        fontSize = 11.sp,
                        color = textColor.copy(alpha = 0.4f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                LazyColumn(
                    state = scrollState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(simulationLogs) { log ->
                        val logColor = if (log.isGoal) Color(0xFF2E7D32) else textColor
                        val logWeight = if (log.isGoal) FontWeight.ExtraBold else FontWeight.Normal
                        
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "${log.minute}'",
                                color = accentColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.width(30.dp)
                            )
                            Text(
                                text = log.text,
                                color = logColor,
                                fontWeight = logWeight,
                                fontSize = 11.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}
