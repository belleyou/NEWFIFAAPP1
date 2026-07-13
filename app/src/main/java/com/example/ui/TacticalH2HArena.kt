package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.*
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
import com.example.ui.theme.BrandOrangeRed
import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

enum class ArenaTab {
    OVERVIEW,
    HEAD_TO_HEAD,
    PLAYERS,
    INJURIES
}

@Composable
fun TacticalH2HArena(
    team1: Team,
    team2: Team,
    currentTheme: GlobeTheme,
    textColor: Color,
    cardBgColor: Color,
    accentColor: Color,
    allTeams: List<Team> = emptyList(),
    onCloseRequest: () -> Unit,
    currentLanguage: AppLanguage = AppLanguage.EN,
    onTeamsChanged: ((Team, Team) -> Unit)? = null
) {
    var activeTab by remember { mutableStateOf(ArenaTab.OVERVIEW) }
    val coroutineScope = rememberCoroutineScope()
    
    // States for dropdown team switches
    var showDropdown1 by remember { mutableStateOf(false) }
    var showDropdown2 by remember { mutableStateOf(false) }

    val isDark = currentTheme == GlobeTheme.COSMIC_DARK

    // Dynamic theme background brush
    val dayBackgroundBrush = if (isDark) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0F172A), // Deep Slate Navy
                Color(0xFF1E1E38), // Rich Indigo Space
                Color(0xFF020617)  // Deep abyss
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFFE0F2F1), // Soft Light Mint Teal
                Color(0xFFF1F8E9), // Gentle organic light green
                Color(0xFFFFFFFF)
            )
        )
    }

    // Layout colors matched to the current theme for outstanding contrast
    val dayTextColor = if (isDark) Color(0xFFF8FAFC) else Color(0xFF0F172A)
    val daySubtextColor = if (isDark) Color(0xFF94A3B8) else Color(0xFF475569)
    val dayCardBg = if (isDark) Color(0xFF1E293B).copy(alpha = 0.9f) else Color.White
    val dayAccentTeal = BrandOrangeRed
    val dayNavy = if (isDark) Color(0xFF38BDF8) else Color(0xFF1E3A8A)
    val dividerColor = if (isDark) Color(0xFF334155) else Color(0xFFE2E8F0)
    val backButtonBg = if (isDark) Color.White.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.6f)
    val tabRowBg = if (isDark) Color(0xFF1E293B).copy(alpha = 0.8f) else Color.White.copy(alpha = 0.8f)
    val dropdownBg = if (isDark) Color(0xFF1E293B) else Color.White

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dayBackgroundBrush)
            .testTag("tactical_h2h_arena_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // HEADER BAR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onCloseRequest,
                    modifier = Modifier
                        .size(40.dp)
                        .background(backButtonBg, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = dayTextColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = localize("COMPARE TEAMS", currentLanguage),
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = dayTextColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 40.dp) // Offset back button to center perfectly
                )
            }

            // TEAM SELECT BADGES ROW
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Team (Click to change)
                Box {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .clickable { showDropdown1 = true }
                            .padding(4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = team1.flag, fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = team1.name,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 16.sp,
                                        color = dayTextColor
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Change Team",
                                        tint = daySubtextColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Text(
                                    text = "#${team1.fifaRanking}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = daySubtextColor
                                )
                            }
                        }
                    }

                    // Dropdown for Team 1
                    DropdownMenu(
                        expanded = showDropdown1,
                        onDismissRequest = { showDropdown1 = false },
                        modifier = Modifier
                            .background(dropdownBg)
                            .heightIn(max = 300.dp)
                    ) {
                        allTeams.forEach { t ->
                            if (t.name != team2.name) {
                                DropdownMenuItem(
                                    text = { Text("${t.flag} ${t.name}", color = dayTextColor, fontWeight = FontWeight.Bold) },
                                    onClick = {
                                        showDropdown1 = false
                                        onTeamsChanged?.invoke(t, team2)
                                    }
                                )
                            }
                        }
                    }
                }

                // VS separator
                Text(
                    text = "vs",
                    fontWeight = FontWeight.Bold,
                    color = daySubtextColor.copy(alpha = 0.5f),
                    fontSize = 13.sp
                )

                // Right Team (Click to change)
                Box {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .clickable { showDropdown2 = true }
                            .padding(4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(horizontalAlignment = Alignment.End) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Change Team",
                                        tint = daySubtextColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = team2.name,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 16.sp,
                                        color = dayTextColor
                                    )
                                }
                                Text(
                                    text = "#${team2.fifaRanking}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = daySubtextColor
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = team2.flag, fontSize = 28.sp)
                        }
                    }

                    // Dropdown for Team 2
                    DropdownMenu(
                        expanded = showDropdown2,
                        onDismissRequest = { showDropdown2 = false },
                        modifier = Modifier
                            .background(dropdownBg)
                            .heightIn(max = 300.dp)
                    ) {
                        allTeams.forEach { t ->
                            if (t.name != team1.name) {
                                DropdownMenuItem(
                                    text = { Text("${t.flag} ${t.name}", color = dayTextColor, fontWeight = FontWeight.Bold) },
                                    onClick = {
                                        showDropdown2 = false
                                        onTeamsChanged?.invoke(team1, t)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // TABS NAVIGATION ROW
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(tabRowBg)
                    .border(1.dp, dividerColor, RoundedCornerShape(12.dp))
                    .padding(3.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ArenaTab.entries.forEach { tab ->
                    val isSelected = activeTab == tab
                    val tabBg = if (isSelected) dayAccentTeal else Color.Transparent
                    val tabContentColor = if (isSelected) Color.White else daySubtextColor

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(tabBg)
                            .clickable { activeTab = tab }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = localize(tab.name, currentLanguage),
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                            fontSize = 11.sp,
                            color = tabContentColor,
                            letterSpacing = 0.2.sp
                        )
                    }
                }
            }

            // TAB SCROLLABLE CONTENT
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (activeTab) {
                    ArenaTab.OVERVIEW -> OverviewTab(
                        team1 = team1,
                        team2 = team2,
                        textColor = dayTextColor,
                        subtextColor = daySubtextColor,
                        cardBg = dayCardBg,
                        accentTeal = dayAccentTeal,
                        accentNavy = dayNavy,
                        dividerColor = dividerColor,
                        currentTheme = currentTheme
                    )
                    ArenaTab.HEAD_TO_HEAD -> H2HMatchSimulatorTab(
                        team1 = team1,
                        team2 = team2,
                        textColor = dayTextColor,
                        accentColor = dayAccentTeal,
                        dividerColor = dividerColor,
                        cardBg = dayCardBg
                    )
                    ArenaTab.PLAYERS -> PlayersTab(
                        team1 = team1,
                        team2 = team2,
                        textColor = dayTextColor,
                        subtextColor = daySubtextColor,
                        cardBg = dayCardBg,
                        accentTeal = dayAccentTeal,
                        accentNavy = dayNavy
                    )
                    ArenaTab.INJURIES -> InjuriesTab(
                        team1 = team1,
                        team2 = team2,
                        textColor = dayTextColor,
                        subtextColor = daySubtextColor,
                        cardBg = dayCardBg,
                        accentTeal = dayAccentTeal,
                        accentNavy = dayNavy
                    )
                }
            }
        }
    }
}

// ==================== OVERVIEW TAB ====================
@Composable
fun OverviewTab(
    team1: Team,
    team2: Team,
    textColor: Color,
    subtextColor: Color,
    cardBg: Color,
    accentTeal: Color,
    accentNavy: Color,
    dividerColor: Color,
    currentTheme: GlobeTheme
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. HEAD-TO-HEAD (ALL TIME)
        OverviewSectionCard(title = "HEAD-TO-HEAD (ALL TIME)", cardBg = cardBg, dividerColor = dividerColor) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                H2HTugOfWarRow(
                    label = "Goals",
                    val1 = team1.stats.goalsScored,
                    val2 = team2.stats.goalsScored,
                    color1 = accentTeal,
                    color2 = accentNavy,
                    textColor = textColor,
                    subtextColor = subtextColor
                )
                H2HTugOfWarRow(
                    label = "Wins",
                    val1 = team1.stats.wins,
                    val2 = team2.stats.wins,
                    color1 = accentTeal,
                    color2 = accentNavy,
                    textColor = textColor,
                    subtextColor = subtextColor
                )
                H2HTugOfWarRow(
                    label = "Draws",
                    val1 = team1.form.count { it == "D" } + 2, // simulated dynamic draws
                    val2 = team2.form.count { it == "D" } + 2,
                    color1 = accentTeal,
                    color2 = accentNavy,
                    textColor = textColor,
                    subtextColor = subtextColor
                )
                H2HTugOfWarRow(
                    label = "Avg Possession",
                    val1 = team1.stats.possessionPercent,
                    val2 = team2.stats.possessionPercent,
                    suffix = "%",
                    color1 = accentTeal,
                    color2 = accentNavy,
                    textColor = textColor,
                    subtextColor = subtextColor
                )
            }
        }

        // 2. RADAR COMPARISON (PER 90)
        OverviewSectionCard(title = "RADAR COMPARISON (PER 90)", cardBg = cardBg, dividerColor = dividerColor) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Legend
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(10.dp).background(accentTeal, RoundedCornerShape(2.dp)))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = team1.name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = subtextColor)
                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(10.dp).background(accentNavy, RoundedCornerShape(2.dp)))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = team2.name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = subtextColor)
                    }
                }

                // 8-Axis Octagon Radar Chart
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    OctagonRadarChart(
                        team1 = team1,
                        team2 = team2,
                        accentTeal = accentTeal,
                        accentNavy = accentNavy,
                        textColor = textColor
                    )
                }
            }
        }

        // 3. WORLD RANKING & FORM
        OverviewSectionCard(title = "WORLD RANKING & FORM", cardBg = cardBg, dividerColor = dividerColor) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Team 1 Info
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "#${team1.fifaRanking}",
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp,
                        color = textColor
                    )
                    Text(
                        text = "FIFA Ranking",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = subtextColor,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        team1.form.take(5).forEach { f ->
                            FormIndicator(f)
                        }
                    }
                }

                // Central VS badge (globe removed)
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF1E293B) else Color(0xFFF1F5F9), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "VS",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF38BDF8) else Color(0xFF0F172A)
                    )
                }

                // Team 2 Info
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "#${team2.fifaRanking}",
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp,
                        color = textColor
                    )
                    Text(
                        text = "FIFA Ranking",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = subtextColor,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        team2.form.take(5).forEach { f ->
                            FormIndicator(f)
                        }
                    }
                }
            }
        }

        // 4. ROAD TO THE FINAL (PATH)
        OverviewSectionCard(title = "ROAD TO THE FINAL (PATH)", cardBg = cardBg, dividerColor = dividerColor) {
            TournamentPathBracket(team1 = team1, team2 = team2, textColor = textColor, subtextColor = subtextColor)
        }

        // 5. KEY PLAYERS COMPARED
        OverviewSectionCard(title = "KEY PLAYERS COMPARED", cardBg = cardBg, dividerColor = dividerColor) {
            val p1 = team1.keyPlayers.firstOrNull() ?: Player("Pulisic", "FW", 10, "")
            val p2 = team2.keyPlayers.firstOrNull() ?: Player("Mbappé", "FW", 10, "")
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Player 1 Card
                Column(
                    modifier = Modifier.weight(1.1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .shadow(2.dp, CircleShape)
                            .background(accentTeal.copy(alpha = 0.15f), CircleShape)
                            .border(2.dp, accentTeal, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "⚽", fontSize = 28.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = p1.name,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        color = textColor,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "#${p1.number} - ${p1.position}",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = subtextColor,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = team1.abbreviation,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = accentTeal
                    )
                }

                // Compared Metrics Table
                Column(
                    modifier = Modifier
                        .weight(1.5f)
                        .padding(horizontal = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val goals1 = if (team1.abbreviation == "USA") 4 else 3
                    val goals2 = if (team2.abbreviation == "FRA") 5 else 3
                    PlayerStatCompareRow(label = "Goals", val1 = "$goals1", val2 = "$goals2", weight1 = true)
                    
                    val assists1 = if (team1.abbreviation == "USA") 2 else 1
                    val assists2 = if (team2.abbreviation == "FRA") 3 else 2
                    PlayerStatCompareRow(label = "Assists", val1 = "$assists1", val2 = "$assists2", weight1 = false)
                    
                    val shots1 = if (team1.abbreviation == "USA") 14 else 10
                    val shots2 = if (team2.abbreviation == "FRA") 18 else 12
                    PlayerStatCompareRow(label = "Shots", val1 = "$shots1", val2 = "$shots2", weight1 = false)
                    
                    val pass1 = if (team1.abbreviation == "USA") "87%" else "84%"
                    val pass2 = if (team2.abbreviation == "FRA") "89%" else "86%"
                    PlayerStatCompareRow(label = "Pass Accuracy", val1 = pass1, val2 = pass2, weight1 = false)
                    
                    val rating1 = if (team1.abbreviation == "USA") "7.4" else "7.1"
                    val rating2 = if (team2.abbreviation == "FRA") "7.9" else "7.5"
                    PlayerStatCompareRow(label = "Avg Rating", val1 = rating1, val2 = rating2, weight1 = false)
                }

                // Player 2 Card
                Column(
                    modifier = Modifier.weight(1.1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .shadow(2.dp, CircleShape)
                            .background(accentNavy.copy(alpha = 0.15f), CircleShape)
                            .border(2.dp, accentNavy, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👑", fontSize = 28.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = p2.name,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        color = textColor,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "#${p2.number} - ${p2.position}",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = subtextColor,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = team2.abbreviation,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = accentNavy
                    )
                }
            }
        }

        // 6. INJURY REPORT
        OverviewSectionCard(title = "INJURY REPORT", cardBg = cardBg, dividerColor = dividerColor) {
            val inj1 = team1.injuries.firstOrNull() ?: com.example.model.Injury("Timothy Weah", "Hamstring", "Out")
            val inj2 = team2.injuries.firstOrNull() ?: com.example.model.Injury("Aurélien Tchouaméni", "Ankle", "Doubtful")

            val innerCardBg = if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF0F172A).copy(alpha = 0.5f) else Color(0xFFF8FAFC)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Left team injury card
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(innerCardBg, RoundedCornerShape(12.dp))
                        .border(1.dp, dividerColor, RoundedCornerShape(12.dp))
                        .padding(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = team1.flag, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = team1.name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = inj1.name, fontSize = 11.sp, fontWeight = FontWeight.Black, color = textColor)
                    Text(text = inj1.injuryType, fontSize = 9.sp, color = subtextColor)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(6.dp).background(Color(0xFFEF4444), CircleShape))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Out", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEF4444))
                    }
                }

                // Right team injury card
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(innerCardBg, RoundedCornerShape(12.dp))
                        .border(1.dp, dividerColor, RoundedCornerShape(12.dp))
                        .padding(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = team2.flag, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = team2.name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = inj2.name, fontSize = 11.sp, fontWeight = FontWeight.Black, color = textColor)
                    Text(text = inj2.injuryType, fontSize = 9.sp, color = subtextColor)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(6.dp).background(Color(0xFFEAB308), CircleShape))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Doubtful", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEAB308))
                    }
                }
            }
        }

        // 7. DECIDE FOR ME (AI PREDICTION)
        OverviewSectionCard(title = "DECIDE FOR ME", cardBg = cardBg, dividerColor = dividerColor) {
            val coroutineScope = rememberCoroutineScope()
            var aiTextState by remember { mutableStateOf<String?>(null) }
            var isAiLoadingState by remember { mutableStateOf(false) }

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1.2f)) {
                        Text(text = "Our AI prediction", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = subtextColor)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = team2.name, fontWeight = FontWeight.Black, fontSize = 18.sp, color = textColor)
                        Text(text = "Win probability 62%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = accentTeal)
                    }

                    // Speedometer/Gauge widget
                    Box(
                        modifier = Modifier
                            .size(width = 100.dp, height = 54.dp)
                            .padding(horizontal = 4.dp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            
                            // Draw semicircular gauge background
                            drawArc(
                                color = dividerColor,
                                startAngle = 180f,
                                sweepAngle = 180f,
                                useCenter = false,
                                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                            )
                            
                            // Draw progress arc towards France (62% of the arc)
                            drawArc(
                                color = accentTeal,
                                startAngle = 180f,
                                sweepAngle = 180f * 0.62f,
                                useCenter = false,
                                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                            )

                            // Draw needle pointer
                            val needleAngleRad = Math.toRadians((180f + 180f * 0.62f).toDouble())
                            val needleLen = h * 0.8f
                            val startX = w / 2f
                            val startY = h
                            val endX = startX + needleLen * cos(needleAngleRad).toFloat()
                            val endY = startY + needleLen * sin(needleAngleRad).toFloat()
                            
                            drawLine(
                                color = textColor,
                                start = Offset(startX, startY),
                                end = Offset(endX, endY),
                                strokeWidth = 2.5.dp.toPx(),
                                cap = StrokeCap.Round
                            )

                            // Needle hub center
                            drawCircle(
                                color = textColor,
                                radius = 4.dp.toPx(),
                                center = Offset(startX, startY)
                            )
                        }
                    }

                    // ✨ Why? button
                    Button(
                        onClick = {
                            isAiLoadingState = true
                            aiTextState = null
                            coroutineScope.launch {
                                val s1 = "${team1.name} (Rank ${team1.fifaRanking}, Form: ${team1.form.joinToString("-")}, Goals: ${team1.stats.goalsScored})"
                                val s2 = "${team2.name} (Rank ${team2.fifaRanking}, Form: ${team2.form.joinToString("-")}, Goals: ${team2.stats.goalsScored})"
                                aiTextState = GeminiService.getComparisonAdvice(
                                    team1Name = team1.name,
                                    team2Name = team2.name,
                                    stats1 = s1,
                                    stats2 = s2
                                )
                                isAiLoadingState = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF334155) else Color(0xFF0F172A)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .height(38.dp)
                            .testTag("ai_decide_button_overview"),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        if (isAiLoadingState) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        } else {
                            Text(text = "✨ Why?", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }

                if (aiTextState != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF0F172A).copy(alpha = 0.5f) else Color(0xFFF1F5F9))
                            .border(1.dp, dividerColor, RoundedCornerShape(10.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = aiTextState!!,
                            color = textColor,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            modifier = Modifier.testTag("ai_response_text_overview")
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

// ==================== COMPONENT WIDGETS ====================

@Composable
fun OverviewSectionCard(
    title: String,
    cardBg: Color,
    dividerColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, dividerColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontWeight = FontWeight.Black,
                fontSize = 11.sp,
                color = Color(0xFF475569), // Slate subtext
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}

@Composable
fun H2HTugOfWarRow(
    label: String,
    val1: Int,
    val2: Int,
    suffix: String = "",
    color1: Color,
    color2: Color,
    textColor: Color,
    subtextColor: Color
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "$val1$suffix", fontWeight = FontWeight.Black, fontSize = 15.sp, color = textColor)
            Text(text = label, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = subtextColor)
            Text(text = "$val2$suffix", fontWeight = FontWeight.Black, fontSize = 15.sp, color = textColor)
        }

        // Relative Tug of War slider bar
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
        ) {
            val w = size.width
            val h = size.height
            val center = w / 2f
            val spacing = 6f
            val maxBarWidth = center - spacing
            
            // Background base track
            drawRoundRect(
                color = subtextColor.copy(alpha = 0.15f),
                topLeft = Offset(0f, 0f),
                size = Size(w, h),
                cornerRadius = CornerRadius(h/2, h/2)
            )

            val sum = (val1 + val2).toFloat()
            val ratio1 = if (sum > 0) val1.toFloat() / sum else 0.5f
            val ratio2 = if (sum > 0) val2.toFloat() / sum else 0.5f

            val bar1Width = maxBarWidth * ratio1
            val bar2Width = maxBarWidth * ratio2

            // Left bar: expanding leftwards from center-spacing
            drawRoundRect(
                color = color1,
                topLeft = Offset(center - spacing - bar1Width, 0f),
                size = Size(bar1Width, h),
                cornerRadius = CornerRadius(h/2, h/2)
            )

            // Right bar: expanding rightwards from center+spacing
            drawRoundRect(
                color = color2,
                topLeft = Offset(center + spacing, 0f),
                size = Size(bar2Width, h),
                cornerRadius = CornerRadius(h/2, h/2)
            )
        }
    }
}

@Composable
fun FormIndicator(formLetter: String) {
    val (bgColor, txtColor) = when (formLetter.uppercase()) {
        "W" -> Pair(Color(0xFF0D9488), Color.White) // Teal Win
        "D" -> Pair(Color(0xFF94A3B8), Color.White) // Gray Draw
        else -> Pair(Color(0xFFEF4444), Color.White) // Red Loss
    }

    Box(
        modifier = Modifier
            .size(18.dp)
            .background(bgColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = formLetter,
            color = txtColor,
            fontWeight = FontWeight.Black,
            fontSize = 9.sp
        )
    }
}

@Composable
fun OctagonRadarChart(
    team1: Team,
    team2: Team,
    accentTeal: Color,
    accentNavy: Color,
    textColor: Color
) {
    val textMeasurer = rememberTextMeasurer()

    // 8 categories normalized: Goals, Clean Sheets, Possession, Shots, Wins, xG, Tackles, Interceptions
    // We scale dynamically so that any chosen teams get proper radar shapes
    val stats1 = listOf(
        (team1.stats.goalsScored / 16f).coerceIn(0.1f, 1f),
        (team1.stats.cleanSheets / 5f).coerceIn(0.1f, 1f),
        ((team1.stats.possessionPercent - 40f) / 30f).coerceIn(0.1f, 1f),
        (team1.stats.shotsOnTarget / 20f).coerceIn(0.1f, 1f),
        (team1.stats.wins / 5f).coerceIn(0.1f, 1f),
        0.75f, // xG USA approximate ratio
        0.82f, // Tackles USA
        0.78f  // Interceptions USA
    )

    val stats2 = listOf(
        (team2.stats.goalsScored / 16f).coerceIn(0.1f, 1f),
        (team2.stats.cleanSheets / 5f).coerceIn(0.1f, 1f),
        ((team2.stats.possessionPercent - 40f) / 30f).coerceIn(0.1f, 1f),
        (team2.stats.shotsOnTarget / 20f).coerceIn(0.1f, 1f),
        (team2.stats.wins / 5f).coerceIn(0.1f, 1f),
        0.95f, // xG France
        0.88f, // Tackles France
        0.85f  // Interceptions France
    )

    val dimensions = listOf(
        "Goals\n1.80", "xG\n1.95", "Shots\n13.2", "Pass Acc.\n87%",
        "Possession\n58%", "Tackles\n15.1", "Intercept\n8.7", "Clean Sh.\n0.75"
    )

    Canvas(
        modifier = Modifier
            .size(200.dp)
            .testTag("canvas_radar_chart")
    ) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = size.minDimension / 2.5f

        // Draw concentric octagons background
        val gridLevels = 4
        for (i in 1..gridLevels) {
            val levelRadius = radius * (i.toFloat() / gridLevels)
            val path = Path()
            for (j in 0..7) {
                val angleRad = Math.toRadians(-90.0 + j * 45.0)
                val x = center.x + levelRadius * cos(angleRad).toFloat()
                val y = center.y + levelRadius * sin(angleRad).toFloat()
                if (j == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()
            drawPath(path = path, color = textColor.copy(alpha = 0.08f), style = Stroke(width = 1.dp.toPx()))
        }

        // Radial lines and labels
        for (j in 0..7) {
            val angleRad = Math.toRadians(-90.0 + j * 45.0)
            val x = center.x + radius * cos(angleRad).toFloat()
            val y = center.y + radius * sin(angleRad).toFloat()
            
            drawLine(
                color = textColor.copy(alpha = 0.1f),
                start = center,
                end = Offset(x, y),
                strokeWidth = 1.dp.toPx()
            )

            // Measure & Position labels outside vertices
            val labelText = dimensions[j]
            val textLayoutResult = textMeasurer.measure(
                text = labelText,
                style = TextStyle(
                    color = textColor.copy(alpha = 0.6f),
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    lineHeight = 10.sp
                )
            )

            val labelRadius = radius + 15.dp.toPx()
            val lx = center.x + labelRadius * cos(angleRad).toFloat() - textLayoutResult.size.width / 2f
            val ly = center.y + labelRadius * sin(angleRad).toFloat() - textLayoutResult.size.height / 2f
            drawText(textLayoutResult, topLeft = Offset(lx, ly))
        }

        // Draw team shape polygons
        fun drawRadarPolygon(stats: List<Float>, color: Color) {
            val path = Path()
            for (j in 0..7) {
                val valNormalized = stats[j].coerceIn(0.1f, 1f)
                val angleRad = Math.toRadians(-90.0 + j * 45.0)
                val x = center.x + (radius * valNormalized) * cos(angleRad).toFloat()
                val y = center.y + (radius * valNormalized) * sin(angleRad).toFloat()
                if (j == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()

            // Fill Translucent
            drawPath(path = path, color = color.copy(alpha = 0.2f))
            // Thick Outline
            drawPath(path = path, color = color, style = Stroke(width = 2.dp.toPx()))
            // Vertex Dots
            for (j in 0..7) {
                val valNormalized = stats[j]
                val angleRad = Math.toRadians(-90.0 + j * 45.0)
                val x = center.x + (radius * valNormalized) * cos(angleRad).toFloat()
                val y = center.y + (radius * valNormalized) * sin(angleRad).toFloat()
                drawCircle(color = color, radius = 3.dp.toPx(), center = Offset(x, y))
            }
        }

        // Team 1 (Teal)
        drawRadarPolygon(stats1, accentTeal)
        // Team 2 (Navy)
        drawRadarPolygon(stats2, accentNavy)
    }
}

@Composable
fun TournamentPathBracket(
    team1: Team,
    team2: Team,
    textColor: Color,
    subtextColor: Color
) {
    val pathBg = if (textColor == Color(0xFFF8FAFC)) Color(0xFF1E293B) else Color(0xFFF8FAFC)
    val pathDivider = if (textColor == Color(0xFFF8FAFC)) Color(0xFF334155) else Color(0xFFE2E8F0)
    val team2RankColor = if (textColor == Color(0xFFF8FAFC)) Color(0xFF38BDF8) else Color(0xFF1E3A8A)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Team 1 Path
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(pathBg, RoundedCornerShape(10.dp))
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Group C", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = subtextColor)
                Text(text = "1st", fontSize = 10.sp, fontWeight = FontWeight.Black, color = BrandOrangeRed)
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(color = pathDivider)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Round of 32", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = subtextColor)
                Text(text = "${team1.abbreviation} 2 - 0 MAR", fontSize = 10.sp, fontWeight = FontWeight.Black, color = textColor)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Round of 16", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = subtextColor)
                Text(text = "${team1.abbreviation} 3 - 1 NED", fontSize = 10.sp, fontWeight = FontWeight.Black, color = textColor)
            }

            // Center Bracket Connector
            Column(
                modifier = Modifier
                    .width(44.dp)
                    .padding(horizontal = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Quarter", fontSize = 8.sp, fontWeight = FontWeight.Black, color = subtextColor)
                Text(text = "Final", fontSize = 8.sp, fontWeight = FontWeight.Black, color = subtextColor)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "⚡", fontSize = 12.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "Jul 12", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = subtextColor)
            }

            // Team 2 Path
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(pathBg, RoundedCornerShape(10.dp))
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Group D", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = subtextColor)
                Text(text = "1st", fontSize = 10.sp, fontWeight = FontWeight.Black, color = team2RankColor)
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(color = pathDivider)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Round of 32", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = subtextColor)
                Text(text = "FRA 2 - 1 POL", fontSize = 10.sp, fontWeight = FontWeight.Black, color = textColor)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Round of 16", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = subtextColor)
                Text(text = "FRA 2 - 0 BEL", fontSize = 10.sp, fontWeight = FontWeight.Black, color = textColor)
            }
        }
    }
}

@Composable
fun PlayerStatCompareRow(
    label: String,
    val1: String,
    val2: String,
    weight1: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = val1,
            fontWeight = if (weight1) FontWeight.ExtraBold else FontWeight.Medium,
            fontSize = 11.sp,
            color = Color(0xFF0F172A),
            modifier = Modifier.width(32.dp),
            textAlign = TextAlign.Start
        )
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = val2,
            fontWeight = if (!weight1) FontWeight.ExtraBold else FontWeight.Medium,
            fontSize = 11.sp,
            color = Color(0xFF0F172A),
            modifier = Modifier.width(32.dp),
            textAlign = TextAlign.End
        )
    }
}


// ==================== TACTICAL H2H / LIVE COMMENTARY SIMULATOR ====================
data class MatchComment(
    val minute: Int,
    val isGoal: Boolean,
    val text: String
)

@Composable
fun H2HMatchSimulatorTab(
    team1: Team,
    team2: Team,
    textColor: Color,
    accentColor: Color,
    dividerColor: Color,
    cardBg: Color
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()

    var isSimulating by remember { mutableStateOf(false) }
    var currentMin by remember { mutableStateOf(0) }
    var score1 by remember { mutableStateOf(0) }
    var score2 by remember { mutableStateOf(0) }
    var simulationLogs = remember { mutableStateListOf<MatchComment>() }
    var simCompleted by remember { mutableStateOf(false) }

    fun runGameSimulation() {
        if (isSimulating) return
        
        isSimulating = true
        simCompleted = false
        currentMin = 0
        score1 = 0
        score2 = 0
        simulationLogs.clear()

        coroutineScope.launch {
            simulationLogs.add(MatchComment(0, false, "🟢 Kickoff! The match is underway. ${team1.name} and ${team2.name} square off!"))
            
            val t1Stars = team1.keyPlayers
            val t2Stars = team2.keyPlayers

            val minutesToSimulate = listOf(7, 15, 29, 36, 45, 58, 69, 77, 86, 90)
            
            for (min in minutesToSimulate) {
                delay(800)
                currentMin = min

                val eventRoll = (1..10).random()
                if (eventRoll <= 4) {
                    // Team 1 attack
                    val isGoal = (1..10).random() > 6
                    val star = t1Stars.random().name
                    if (isGoal) {
                        score1++
                        simulationLogs.add(MatchComment(min, true, "⚽ GOAL!!! Wonderful play! $star loops it perfectly into the net! ${team1.name} leads!"))
                    } else {
                        simulationLogs.add(MatchComment(min, false, "🧤 SAVE! $star shoots but the goalkeeper makes an outstanding save!"))
                    }
                } else if (eventRoll <= 8) {
                    // Team 2 attack
                    val isGoal = (1..10).random() > 6
                    val star = t2Stars.random().name
                    if (isGoal) {
                        score2++
                        simulationLogs.add(MatchComment(min, true, "⚽ GOAL!!! Clinical finish! $star cracks a rocket from distance! ${team2.name}!"))
                    } else {
                        simulationLogs.add(MatchComment(min, false, "🛡️ DEFLECTED! $star unleashes a powerful drive, but it's blocked away!"))
                    }
                } else {
                    val genericText = listOf(
                        "🟨 Yellow Card issued for a tactical slide in midfield.",
                        "🔄 Quick tactical shifts as coaches direct defensive configurations.",
                        "📐 Corner kick curled in but safely headed away by the defense."
                    ).random()
                    simulationLogs.add(MatchComment(min, false, genericText))
                }
                
                if (simulationLogs.isNotEmpty()) {
                    scrollState.animateScrollToItem(simulationLogs.size - 1)
                }
            }

            delay(800)
            simCompleted = true
            isSimulating = false
            simulationLogs.add(MatchComment(90, false, "🏁 Full Time! The game concludes! Final Score: ${team1.name} $score1 - $score2 ${team2.name}."))
            scrollState.animateScrollToItem(simulationLogs.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Electronic Retro Board
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
                    text = if (isSimulating) "🔴 LIVE MATCH SIMULATION" else if (simCompleted) "🏁 FINAL SCORE" else "🎯 KICKOFF READY",
                    color = if (isSimulating) Color.Red else Color(0xFF39FF14),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = team1.flag, fontSize = 24.sp)
                        Text(text = team1.abbreviation, color = Color.White, fontWeight = FontWeight.Black, fontSize = 14.sp)
                    }

                    Text(
                        text = "$score1 - $score2",
                        color = Color(0xFF39FF14),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = team2.flag, fontSize = 24.sp)
                        Text(text = team2.abbreviation, color = Color.White, fontWeight = FontWeight.Black, fontSize = 14.sp)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "TIME: $currentMin'",
                    color = Color.Red,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Black
                )
            }
        }

        Button(
            onClick = { runGameSimulation() },
            enabled = !isSimulating,
            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(46.dp)
        ) {
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Run Simulator")
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "RUN LIVE TACTICAL SIMULATOR", fontWeight = FontWeight.Black, fontSize = 12.sp)
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            border = BorderStroke(1.dp, dividerColor),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (simulationLogs.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Press play above to run a stats-driven simulated clash with minute-by-minute live sports commentary!",
                        color = Color.Gray,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            } else {
                LazyColumn(
                    state = scrollState,
                    modifier = Modifier.fillMaxSize().padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(simulationLogs) { log ->
                        val logColor = if (log.isGoal) Color(0xFF0D9488) else textColor
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "${log.minute}'",
                                color = accentColor,
                                fontWeight = FontWeight.Black,
                                fontSize = 11.sp,
                                modifier = Modifier.width(32.dp)
                            )
                            Text(
                                text = log.text,
                                color = logColor,
                                fontWeight = if (log.isGoal) FontWeight.ExtraBold else FontWeight.Medium,
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


// ==================== PLAYERS TAB ====================
@Composable
fun PlayersTab(
    team1: Team,
    team2: Team,
    textColor: Color,
    subtextColor: Color,
    cardBg: Color,
    accentTeal: Color,
    accentNavy: Color
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Squad Depth & Key Player Clash",
            fontWeight = FontWeight.Black,
            fontSize = 14.sp,
            color = textColor
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Team 1 players
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = team1.flag, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = team1.name, fontWeight = FontWeight.Black, fontSize = 12.sp, color = textColor)
                }

                team1.keyPlayers.forEach { p ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        border = BorderStroke(1.dp, accentTeal.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.size(18.dp).background(accentTeal, CircleShape),
                                    contentAlignment = Alignment.Center
                               ) {
                                    Text(text = "${p.number}", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                               }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = p.name, fontWeight = FontWeight.Black, fontSize = 11.sp, color = textColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = p.position, fontWeight = FontWeight.Bold, fontSize = 9.sp, color = accentTeal)
                            Text(text = p.description, fontSize = 9.sp, color = subtextColor, lineHeight = 11.sp)
                        }
                    }
                }
            }

            // Team 2 players
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = team2.flag, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = team2.name, fontWeight = FontWeight.Black, fontSize = 12.sp, color = textColor)
                }

                team2.keyPlayers.forEach { p ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        border = BorderStroke(1.dp, accentNavy.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.size(18.dp).background(accentNavy, CircleShape),
                                    contentAlignment = Alignment.Center
                               ) {
                                    Text(text = "${p.number}", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                               }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = p.name, fontWeight = FontWeight.Black, fontSize = 11.sp, color = textColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = p.position, fontWeight = FontWeight.Bold, fontSize = 9.sp, color = accentNavy)
                            Text(text = p.description, fontSize = 9.sp, color = subtextColor, lineHeight = 11.sp)
                        }
                    }
                }
            }
        }
    }
}


// ==================== INJURIES TAB ====================
@Composable
fun InjuriesTab(
    team1: Team,
    team2: Team,
    textColor: Color,
    subtextColor: Color,
    cardBg: Color,
    accentTeal: Color,
    accentNavy: Color
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Injuries & Suspensions Log",
            fontWeight = FontWeight.Black,
            fontSize = 14.sp,
            color = textColor
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Team 1 Injuries
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = team1.flag, fontSize = 15.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = team1.name, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = textColor)
                }

                if (team1.injuries.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(cardBg, RoundedCornerShape(10.dp))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No recorded injuries. Fully fit squad! ✅", color = subtextColor, fontSize = 10.sp, textAlign = TextAlign.Center)
                    }
                } else {
                    team1.injuries.forEach { inj ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = cardBg),
                            border = BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.2f)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(text = inj.name, fontWeight = FontWeight.Black, fontSize = 11.sp, color = textColor)
                                Text(text = inj.injuryType, fontSize = 9.sp, color = subtextColor)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(5.dp).background(Color(0xFFEF4444), CircleShape))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = inj.returnEstimate, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEF4444))
                                }
                            }
                        }
                    }
                }
            }

            // Team 2 Injuries
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = team2.flag, fontSize = 15.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = team2.name, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = textColor)
                }

                if (team2.injuries.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(cardBg, RoundedCornerShape(10.dp))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No recorded injuries. Fully fit squad! ✅", color = subtextColor, fontSize = 10.sp, textAlign = TextAlign.Center)
                    }
                } else {
                    team2.injuries.forEach { inj ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = cardBg),
                            border = BorderStroke(1.dp, Color(0xFFEAB308).copy(alpha = 0.2f)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(text = inj.name, fontWeight = FontWeight.Black, fontSize = 11.sp, color = textColor)
                                Text(text = inj.injuryType, fontSize = 9.sp, color = subtextColor)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(5.dp).background(Color(0xFFEAB308), CircleShape))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = inj.returnEstimate, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEAB308))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
