package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.HostStadium
import com.example.model.StadiumPlayer
import com.example.model.StadiumTeamDetails
import com.example.model.StadiumTeamRosterDataProvider

enum class SidePanelTab(val label: String, val icon: String) {
    ROSTER("Roster", "📋"),
    PLAYER_STATS("Player Stats", "📊"),
    HISTORY("History & Honors", "🏆")
}

@Composable
fun StadiumTeamSidePanel(
    stadium: HostStadium,
    currentTheme: GlobeTheme,
    onClose: () -> Unit,
    onCompareTeam: (String) -> Unit, // passes team abbreviation or name
    onRemindMatch: (matchTitle: String, date: String, time: String) -> Unit,
    isReminderActive: Boolean = false,
    modifier: Modifier = Modifier
) {
    val teamDetails = remember(stadium.id) {
        StadiumTeamRosterDataProvider.getTeamForStadium(stadium.id)
    }

    var activeTab by remember { mutableStateOf(SidePanelTab.ROSTER) }
    var selectedPlayer by remember { mutableStateOf<StadiumPlayer?>(null) }
    var rosterPositionFilter by remember { mutableStateOf("ALL") }

    val isDark = currentTheme == GlobeTheme.COSMIC_DARK
    val bgBrush = if (isDark) {
        Brush.verticalGradient(
            listOf(Color(0xFF0F172A).copy(alpha = 0.98f), Color(0xFF090D16).copy(alpha = 0.99f))
        )
    } else {
        Brush.verticalGradient(
            listOf(Color(0xFFFFFFFF).copy(alpha = 0.98f), Color(0xFFF1F5F9).copy(alpha = 0.99f))
        )
    }

    val primaryTextColor = if (isDark) Color.White else Color(0xFF0F172A)
    val secondaryTextColor = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
    val cardBg = if (isDark) Color(0xFF1E293B).copy(alpha = 0.75f) else Color(0xFFF8FAFC)
    val cardBorder = if (isDark) Color(0xFF334155).copy(alpha = 0.5f) else Color(0xFFE2E8F0)
    val accentTeal = Color(teamDetails.primaryColorHex)

    Box(
        modifier = modifier
            .fillMaxHeight()
            .widthIn(max = 440.dp)
            .fillMaxWidth()
            .background(bgBrush)
            .border(
                width = 1.dp,
                color = if (isDark) Color(0xFF334155).copy(alpha = 0.6f) else Color(0xFFCBD5E1),
                shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
            )
            .clip(RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp))
            .testTag("stadium_team_side_panel")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // TOP BAR & HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(36.dp)
                        .background(cardBg, CircleShape)
                        .testTag("side_panel_close_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Close Side Panel",
                        tint = primaryTextColor,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "ASSOCIATED HOME TEAM",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.2.sp,
                        color = accentTeal
                    )
                    Text(
                        text = stadium.name,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = secondaryTextColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // H2H Compare Shortcut Button
                Button(
                    onClick = { onCompareTeam(teamDetails.teamName) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentTeal.copy(alpha = 0.15f),
                        contentColor = accentTeal
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier
                        .height(32.dp)
                        .testTag("side_panel_compare_button")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("⚖️", fontSize = 11.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "COMPARE",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            // TEAM HERO CARD
            Surface(
                color = cardBg,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Flag or Emoji Logo
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(accentTeal.copy(alpha = 0.2f), CircleShape)
                                .border(1.5.dp, accentTeal, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = teamDetails.flagOrLogo, fontSize = 24.sp)
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = teamDetails.teamName,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = primaryTextColor,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = accentTeal.copy(alpha = 0.18f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = teamDetails.abbreviation,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = accentTeal,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }

                            Text(
                                text = "${teamDetails.leagueOrCompetition} • Head Coach: ${teamDetails.headCoach}",
                                fontSize = 10.sp,
                                color = secondaryTextColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = "📍 ${teamDetails.city} • Home: ${teamDetails.stadiumName}",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = primaryTextColor.copy(alpha = 0.8f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // NEXT FIXTURE & REMIND ME CALLOUT
                    Surface(
                        color = if (isDark) Color(0xFF0F172A) else Color(0xFFE2E8F0).copy(alpha = 0.7f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("⚡", fontSize = 10.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "UPCOMING MATCH",
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFFF59E0B)
                                    )
                                }
                                Text(
                                    text = teamDetails.nextMatchSummary,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryTextColor,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "🗓️ ${teamDetails.nextMatchDate} • ⏰ ${teamDetails.nextMatchTime}",
                                    fontSize = 9.5.sp,
                                    color = secondaryTextColor
                                )
                            }

                            // 'Remind Me' Button
                            Button(
                                onClick = {
                                    onRemindMatch(
                                        teamDetails.nextMatchSummary,
                                        teamDetails.nextMatchDate,
                                        teamDetails.nextMatchTime
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isReminderActive) Color(0xFF10B981) else accentTeal,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                modifier = Modifier
                                    .height(30.dp)
                                    .testTag("side_panel_remind_button")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (isReminderActive) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                                        contentDescription = "Remind Me",
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isReminderActive) "ACTIVE" else "REMIND ME",
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // NAVIGATION TAB STRIP
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SidePanelTab.entries.forEach { tab ->
                    val isSelected = activeTab == tab
                    Surface(
                        color = if (isSelected) accentTeal else cardBg,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) accentTeal else cardBorder
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(34.dp)
                            .clickable { activeTab = tab }
                            .testTag("side_panel_tab_${tab.name.lowercase()}")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(tab.icon, fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = tab.label,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                color = if (isSelected) Color.White else primaryTextColor
                            )
                        }
                    }
                }
            }

            // TAB CONTENT AREA
            when (activeTab) {
                SidePanelTab.ROSTER -> {
                    RosterTabContent(
                        roster = teamDetails.roster,
                        selectedPosition = rosterPositionFilter,
                        onPositionFilterChange = { rosterPositionFilter = it },
                        onPlayerClick = { selectedPlayer = it },
                        primaryTextColor = primaryTextColor,
                        secondaryTextColor = secondaryTextColor,
                        cardBg = cardBg,
                        cardBorder = cardBorder,
                        accentColor = accentTeal
                    )
                }
                SidePanelTab.PLAYER_STATS -> {
                    PlayerStatsTabContent(
                        roster = teamDetails.roster,
                        selectedPlayer = selectedPlayer,
                        onSelectPlayer = { selectedPlayer = it },
                        primaryTextColor = primaryTextColor,
                        secondaryTextColor = secondaryTextColor,
                        cardBg = cardBg,
                        cardBorder = cardBorder,
                        accentColor = accentTeal,
                        isDark = isDark
                    )
                }
                SidePanelTab.HISTORY -> {
                    HistoricalPerformanceTabContent(
                        performance = teamDetails.historicalPerformance,
                        teamDetails = teamDetails,
                        stadium = stadium,
                        primaryTextColor = primaryTextColor,
                        secondaryTextColor = secondaryTextColor,
                        cardBg = cardBg,
                        cardBorder = cardBorder,
                        accentColor = accentTeal,
                        isDark = isDark
                    )
                }
            }
        }
    }
}

@Composable
private fun RosterTabContent(
    roster: List<StadiumPlayer>,
    selectedPosition: String,
    onPositionFilterChange: (String) -> Unit,
    onPlayerClick: (StadiumPlayer) -> Unit,
    primaryTextColor: Color,
    secondaryTextColor: Color,
    cardBg: Color,
    cardBorder: Color,
    accentColor: Color
) {
    val positions = listOf("ALL", "FW", "MF", "DF", "GK", "PG", "SG", "SF", "PF", "C")
    val filteredRoster = remember(roster, selectedPosition) {
        if (selectedPosition == "ALL") roster
        else roster.filter { it.position.equals(selectedPosition, ignoreCase = true) }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Position Filter Chips
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(positions) { pos ->
                val isSelected = selectedPosition == pos
                Surface(
                    color = if (isSelected) accentColor.copy(alpha = 0.25f) else cardBg,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) accentColor else cardBorder
                    ),
                    modifier = Modifier
                        .clickable { onPositionFilterChange(pos) }
                        .padding(vertical = 2.dp)
                ) {
                    Text(
                        text = pos,
                        fontSize = 9.sp,
                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                        color = if (isSelected) accentColor else secondaryTextColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Squad List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredRoster) { player ->
                Surface(
                    color = cardBg,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onPlayerClick(player) }
                        .testTag("roster_player_card_${player.number}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Jersey Number Badge
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .background(accentColor.copy(alpha = 0.18f), CircleShape)
                                .border(1.dp, accentColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "#${player.number}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = accentColor
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = player.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = primaryTextColor
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = accentColor.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = player.position,
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = accentColor,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }

                            Text(
                                text = "${player.clubOrCollege} • Age ${player.age} • ${player.capsOrExp}",
                                fontSize = 9.5.sp,
                                color = secondaryTextColor
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "★ ${player.signatureSkill}",
                                fontSize = 8.5.sp,
                                color = primaryTextColor.copy(alpha = 0.75f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        // Rating Pill
                        Surface(
                            color = Color(0xFF10B981).copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            ) {
                                Text("★", fontSize = 9.sp, color = Color(0xFF10B981))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = player.stats.rating.toString(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF10B981)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayerStatsTabContent(
    roster: List<StadiumPlayer>,
    selectedPlayer: StadiumPlayer?,
    onSelectPlayer: (StadiumPlayer) -> Unit,
    primaryTextColor: Color,
    secondaryTextColor: Color,
    cardBg: Color,
    cardBorder: Color,
    accentColor: Color,
    isDark: Boolean
) {
    val activePlayer = selectedPlayer ?: roster.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 4.dp)
    ) {
        // Horizontal Player Selector Bar
        Text(
            text = "SELECT PLAYER FOR DETAILED STATS",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = secondaryTextColor,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(roster) { player ->
                val isSelected = activePlayer?.name == player.name
                Surface(
                    color = if (isSelected) accentColor else cardBg,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) accentColor else cardBorder
                    ),
                    modifier = Modifier
                        .clickable { onSelectPlayer(player) }
                        .padding(vertical = 2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "#${player.number} ${player.name}",
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                            color = if (isSelected) Color.White else primaryTextColor
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (activePlayer != null) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // Focus Player Spotlight
                item {
                    Surface(
                        color = cardBg,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column {
                                    Text(
                                        text = "#${activePlayer.number} ${activePlayer.name}",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Black,
                                        color = primaryTextColor
                                    )
                                    Text(
                                        text = "${activePlayer.position} • ${activePlayer.clubOrCollege}",
                                        fontSize = 10.sp,
                                        color = secondaryTextColor
                                    )
                                }

                                Surface(
                                    color = Color(0xFFF59E0B).copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "OVERALL ${activePlayer.stats.rating} ★",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFFF59E0B),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Signature: \"${activePlayer.signatureSkill}\"",
                                fontSize = 9.5.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                color = accentColor
                            )
                        }
                    }
                }

                // Metric Grid Cards
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            StatBox(
                                label = "SCORING / EFFICIENCY",
                                value = activePlayer.stats.goalsOrPpg,
                                icon = "⚽",
                                modifier = Modifier.weight(1f),
                                cardBg = cardBg,
                                cardBorder = cardBorder,
                                primaryTextColor = primaryTextColor,
                                secondaryTextColor = secondaryTextColor
                            )
                            StatBox(
                                label = "PLAYMAKING / CREATIVITY",
                                value = activePlayer.stats.assistsOrApg,
                                icon = "🎯",
                                modifier = Modifier.weight(1f),
                                cardBg = cardBg,
                                cardBorder = cardBorder,
                                primaryTextColor = primaryTextColor,
                                secondaryTextColor = secondaryTextColor
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            StatBox(
                                label = activePlayer.stats.keyMetricLabel.uppercase(),
                                value = activePlayer.stats.keyMetricValue,
                                icon = "🛡️",
                                modifier = Modifier.weight(1f),
                                cardBg = cardBg,
                                cardBorder = cardBorder,
                                primaryTextColor = primaryTextColor,
                                secondaryTextColor = secondaryTextColor
                            )
                            StatBox(
                                label = "SHOT ACCURACY / FG%",
                                value = activePlayer.stats.shotAccuracyOrFg,
                                icon = "📈",
                                modifier = Modifier.weight(1f),
                                cardBg = cardBg,
                                cardBorder = cardBorder,
                                primaryTextColor = primaryTextColor,
                                secondaryTextColor = secondaryTextColor
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            StatBox(
                                label = "MATCHES PLAYED",
                                value = "${activePlayer.stats.matchesPlayed} Apps",
                                icon = "⏱️",
                                modifier = Modifier.weight(1f),
                                cardBg = cardBg,
                                cardBorder = cardBorder,
                                primaryTextColor = primaryTextColor,
                                secondaryTextColor = secondaryTextColor
                            )
                            StatBox(
                                label = "TOTAL MINUTES",
                                value = "${activePlayer.stats.minutesPlayed} Mins",
                                icon = "⚡",
                                modifier = Modifier.weight(1f),
                                cardBg = cardBg,
                                cardBorder = cardBorder,
                                primaryTextColor = primaryTextColor,
                                secondaryTextColor = secondaryTextColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatBox(
    label: String,
    value: String,
    icon: String,
    modifier: Modifier = Modifier,
    cardBg: Color,
    cardBorder: Color,
    primaryTextColor: Color,
    secondaryTextColor: Color
) {
    Surface(
        color = cardBg,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(icon, fontSize = 11.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = label,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = secondaryTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Black,
                color = primaryTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun HistoricalPerformanceTabContent(
    performance: com.example.model.StadiumTeamHistoricalPerformance,
    teamDetails: StadiumTeamDetails,
    stadium: HostStadium,
    primaryTextColor: Color,
    secondaryTextColor: Color,
    cardBg: Color,
    cardBorder: Color,
    accentColor: Color,
    isDark: Boolean
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // CHAMPIONSHIPS & TITLES CABINET
        item {
            Surface(
                color = cardBg,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🏆", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "TROPHY CABINET & HONORS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = primaryTextColor
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    performance.championshipTitles.forEach { title ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(Color(0xFFF59E0B), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = title,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = primaryTextColor
                            )
                        }
                    }
                }
            }
        }

        // ALL-TIME RECORD & VENUE FORTRESS
        item {
            Surface(
                color = cardBg,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "HISTORICAL PERFORMANCE METRICS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = primaryTextColor
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("All-Time Record", fontSize = 9.sp, color = secondaryTextColor)
                            Text(
                                text = performance.allTimeRecord,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = primaryTextColor
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("Win Rate %", fontSize = 9.sp, color = secondaryTextColor)
                            Text(
                                text = performance.winPercentage,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF10B981)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Fortress Record
                    Surface(
                        color = accentColor.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text("🏰", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "HOME VENUE RECORD AT ${stadium.name.uppercase()}",
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = accentColor
                                )
                                Text(
                                    text = performance.venueHomeRecord,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryTextColor
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Recent Form Sequence
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Recent 5-Match Form:",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = secondaryTextColor
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            performance.recentForm.forEach { f ->
                                val badgeColor = when (f) {
                                    "W" -> Color(0xFF10B981)
                                    "D" -> Color(0xFFF59E0B)
                                    else -> Color(0xFFEF4444)
                                }
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .background(badgeColor, RoundedCornerShape(4.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = f,
                                        color = Color.White,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // ICONIC HISTORICAL MILESTONES AT THIS LOCATION
        item {
            Surface(
                color = cardBg,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, cardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("📜", fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ICONIC HISTORICAL MOMENTS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = primaryTextColor
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    performance.milestoneMoments.forEach { moment ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text("✨", fontSize = 10.sp, modifier = Modifier.padding(top = 1.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = moment,
                                fontSize = 10.sp,
                                color = primaryTextColor.copy(alpha = 0.88f),
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
