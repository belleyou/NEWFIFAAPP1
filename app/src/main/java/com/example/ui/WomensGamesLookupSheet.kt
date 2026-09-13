package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.service.MatchNotificationManager
import com.example.ui.theme.BrandOrangeRed
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WomensGamesLookupSheet(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onFlyToStadium: (String) -> Unit,
    currentTheme: GlobeTheme,
    initialCategory: WomensSportCategory = WomensSportCategory.ALL,
    modifier: Modifier = Modifier
) {
    if (!isOpen) return

    val context = LocalContext.current
    var selectedScope by remember { mutableStateOf(WomensScope.ALL) }
    var selectedCategory by remember(initialCategory, isOpen) { mutableStateOf(initialCategory) }
    var searchQuery by remember { mutableStateOf("") }
    var expandedAiGameId by remember { mutableStateOf<String?>(null) }
    var scheduledGameIds by remember { mutableStateOf(setOf<String>()) }

    val filteredGames = remember(selectedScope, selectedCategory, searchQuery) {
        WomensSportsDataProvider.filterGames(selectedScope, selectedCategory, searchQuery)
    }

    val isDark = currentTheme == GlobeTheme.COSMIC_DARK
    val sheetBg = if (isDark) Color(0xFF0F172A) else Color(0xFFFFFFFF)
    val cardBg = if (isDark) Color(0xFF1E293B) else Color(0xFFF8FAFC)
    val textPrimary = if (isDark) Color.White else Color(0xFF0F172A)
    val textSecondary = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
    val accentGreen = Color(0xFF10B981)
    val accentPink = Color(0xFFEC4899)
    val accentCyan = Color(0xFF06B6D4)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = sheetBg,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(48.dp)
                    .height(5.dp)
                    .background(textSecondary.copy(alpha = 0.3f), CircleShape)
            )
        },
        modifier = modifier.testTag("womens_games_lookup_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(horizontal = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                Brush.linearGradient(listOf(Color(0xFFF43F5E), Color(0xFF8B5CF6))),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "⚡", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Women's Sports Games Look-Up",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = textPrimary
                        )
                        Text(
                            text = "Look up games globally across USA & international leagues",
                            fontSize = 11.sp,
                            color = textSecondary
                        )
                    }
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(32.dp)
                        .background(textSecondary.copy(alpha = 0.15f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = textPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("games_search_input"),
                placeholder = {
                    Text(
                        "Search teams, stars (Caitlin Clark, Coco Gauff, Marta), cities...",
                        fontSize = 12.sp,
                        color = textSecondary
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = accentCyan
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear", tint = textSecondary)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = accentCyan,
                    unfocusedBorderColor = textSecondary.copy(alpha = 0.3f),
                    focusedContainerColor = cardBg,
                    unfocusedContainerColor = cardBg
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Scope Selector Chips (All, USA National, International)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WomensScope.values().forEach { scope ->
                    val isSelected = selectedScope == scope
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .shadow(if (isSelected) 3.dp else 0.dp, RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) {
                                    Brush.linearGradient(listOf(Color(0xFF2563EB), Color(0xFF7C3AED)))
                                } else {
                                    Brush.linearGradient(listOf(cardBg, cardBg))
                                },
                                RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) Color(0xFF60A5FA) else textSecondary.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedScope = scope }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${scope.emoji} ${scope.displayName}",
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else textPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sport Category Switcher Carousel (Tactile, Accessible, Rich Indicators)
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(WomensSportCategory.values()) { cat ->
                    val isSelected = selectedCategory == cat
                    val matchingGames = WomensSportsDataProvider.sampleGames.filter { g ->
                        (cat == WomensSportCategory.ALL || g.sport == cat) &&
                        (selectedScope == WomensScope.ALL || g.scope == selectedScope)
                    }
                    val hasLiveGame = matchingGames.any { it.status == "LIVE" }
                    val leagueSub = when (cat) {
                        WomensSportCategory.ALL -> "All Leagues"
                        WomensSportCategory.BASKETBALL -> "WNBA"
                        WomensSportCategory.SOCCER -> "NWSL • FIFA"
                        WomensSportCategory.TENNIS -> "WTA Tour"
                        WomensSportCategory.ICE_HOCKEY -> "PWHL"
                        WomensSportCategory.VOLLEYBALL -> "LOVB"
                        WomensSportCategory.CRICKET -> "ICC T20"
                    }

                    Box(
                        modifier = Modifier
                            .height(58.dp)
                            .shadow(
                                elevation = if (isSelected) 6.dp else 0.dp,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .background(
                                brush = if (isSelected) {
                                    Brush.linearGradient(listOf(Color(0xFF2563EB), Color(0xFF7C3AED)))
                                } else {
                                    Brush.linearGradient(
                                        listOf(
                                            if (isDark) Color(0xFF1E293B).copy(alpha = 0.9f) else Color(0xFFF1F5F9),
                                            if (isDark) Color(0xFF0F172A).copy(alpha = 0.9f) else Color(0xFFE2E8F0)
                                        )
                                    )
                                },
                                shape = RoundedCornerShape(14.dp)
                            )
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) Color(0xFF93C5FD) else textSecondary.copy(alpha = 0.22f),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("sport_category_tab_${cat.name}"),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Emoji circle icon
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .background(
                                        if (isSelected) Color.White.copy(alpha = 0.22f) else textSecondary.copy(alpha = 0.12f),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cat.emoji,
                                    fontSize = 17.sp
                                )
                            }

                            Column(
                                verticalArrangement = Arrangement.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = cat.displayName,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                        color = if (isSelected) Color.White else textPrimary
                                    )
                                    if (hasLiveGame) {
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0xFFEF4444), RoundedCornerShape(4.dp))
                                                .padding(horizontal = 4.dp, vertical = 1.dp)
                                        ) {
                                            Text(
                                                text = "LIVE",
                                                color = Color.White,
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                        }
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = leagueSub,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (isSelected) Color.White.copy(alpha = 0.85f) else textSecondary
                                    )
                                    Text(
                                        text = "• ${matchingGames.size}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color(0xFFFDE047) else Color(0xFF10B981)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Results count badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Showing ${filteredGames.size} Games & Events",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = textSecondary
                )
                if (filteredGames.any { it.status == "LIVE" }) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(0xFFEF4444), CircleShape)
                        )
                        Text(
                            text = "LIVE GAMES IN PROGRESS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFEF4444)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Games List
            if (filteredGames.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🔍", fontSize = 42.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No women's sports games matched your query",
                            fontWeight = FontWeight.Bold,
                            color = textPrimary,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Try clearing search or switching between All Sports / USA / Global",
                            fontSize = 11.sp,
                            color = textSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(filteredGames, key = { it.id }) { game ->
                        val isLive = game.status == "LIVE"
                        val isFinal = game.status == "FINAL"
                        val isExpanded = expandedAiGameId == game.id
                        val isAlertScheduled = scheduledGameIds.contains(game.id)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(4.dp, RoundedCornerShape(18.dp))
                                .border(
                                    width = if (isLive) 2.dp else 1.dp,
                                    color = if (isLive) Color(0xFFEF4444) else textSecondary.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(18.dp)
                                ),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = cardBg)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                // Top status bar
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        // League Badge
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    if (isDark) Color(0xFF334155) else Color(0xFFE2E8F0),
                                                    RoundedCornerShape(6.dp)
                                                )
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "${game.sport.emoji} ${game.league}",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Black,
                                                color = textPrimary
                                            )
                                        }
                                        // Scope badge
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    if (game.scope == WomensScope.USA_NATIONAL) Color(0xFF1E3A8A).copy(alpha = 0.25f) else Color(0xFF14532D).copy(alpha = 0.25f),
                                                    RoundedCornerShape(6.dp)
                                                )
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "${game.scope.emoji} ${game.scope.displayName}",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (game.scope == WomensScope.USA_NATIONAL) Color(0xFF60A5FA) else Color(0xFF4ADE80)
                                            )
                                        }
                                    }

                                    // Status Badge (LIVE / FINAL / DATE)
                                    if (isLive) {
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0xFFDC2626), RoundedCornerShape(8.dp))
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        ) {
                                            Text(
                                                text = "🔴 LIVE ${game.periodOrSet ?: ""}",
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                        }
                                    } else if (isFinal) {
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0xFF475569), RoundedCornerShape(8.dp))
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        ) {
                                            Text(
                                                text = "FINAL",
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                        }
                                    } else {
                                        Text(
                                            text = "${game.date} · ${game.time}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = accentCyan
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                // Round/Stage Label
                                Text(
                                    text = game.roundOrStage,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = textSecondary
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // Matchup Row (Home vs Away)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // Home Team / Athlete
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = game.homeFlagOrLogo, fontSize = 24.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = game.homeTeamOrAthlete,
                                                fontWeight = FontWeight.Black,
                                                fontSize = 14.sp,
                                                color = textPrimary,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            if (isLive || isFinal) {
                                                Text(
                                                    text = "${game.homeScore ?: 0}",
                                                    fontWeight = FontWeight.Black,
                                                    fontSize = 18.sp,
                                                    color = if (isLive) Color(0xFF10B981) else textPrimary
                                                )
                                            }
                                        }
                                    }

                                    Text(
                                        text = "VS",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 13.sp,
                                        color = textSecondary.copy(alpha = 0.6f),
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )

                                    // Away Team / Athlete
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(
                                                text = game.awayTeamOrAthlete,
                                                fontWeight = FontWeight.Black,
                                                fontSize = 14.sp,
                                                color = textPrimary,
                                                textAlign = TextAlign.End,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            if (isLive || isFinal) {
                                                Text(
                                                    text = "${game.awayScore ?: 0}",
                                                    fontWeight = FontWeight.Black,
                                                    fontSize = 18.sp,
                                                    color = if (isLive) Color(0xFF10B981) else textPrimary
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = game.awayFlagOrLogo, fontSize = 24.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Broadcast Network
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Tv,
                                        contentDescription = "Broadcast",
                                        tint = textSecondary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Broadcast: ${game.broadcast}",
                                        fontSize = 11.sp,
                                        color = textSecondary,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Featured Star Players Strip
                                if (game.keyStars.isNotEmpty()) {
                                    Text(
                                        text = "⭐ Key Stars to Watch:",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDark) Color(0xFFFBBF24) else Color(0xFFD97706)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        game.keyStars.take(2).forEach { star ->
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(
                                                        if (isDark) Color(0xFF0F172A).copy(alpha = 0.5f) else Color(0xFFEDE9FE).copy(alpha = 0.4f),
                                                        RoundedCornerShape(8.dp)
                                                    )
                                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = "🔥", fontSize = 12.sp)
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = star.name,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 11.sp,
                                                    color = textPrimary
                                                )
                                                Text(
                                                    text = " (${star.position})",
                                                    fontSize = 10.sp,
                                                    color = textSecondary
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "· ${star.stats}",
                                                    fontSize = 10.sp,
                                                    color = if (isDark) Color(0xFF38BDF8) else Color(0xFF0369A1),
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Action Buttons: Fly to Stadium & 30-min Reminder & Gemini AI Preview
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Fly to Stadium on 3D Globe Button
                                    Button(
                                        onClick = {
                                            onFlyToStadium(game.stadiumId)
                                            onDismiss()
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(36.dp),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isDark) Color(0xFF059669) else Color(0xFF10B981)
                                        ),
                                        contentPadding = PaddingValues(horizontal = 8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Public,
                                            contentDescription = "Fly to Stadium",
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Fly to ${game.stadiumName}",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    // Reminder Bell Button
                                    IconButton(
                                        onClick = {
                                            if (isAlertScheduled) {
                                                MatchNotificationManager.cancelMatchAlarm(context, game.id)
                                                scheduledGameIds = scheduledGameIds - game.id
                                            } else {
                                                val triggerCal = Calendar.getInstance().apply {
                                                    add(Calendar.MINUTE, 30)
                                                }
                                                MatchNotificationManager.scheduleMatchAlarm(
                                                    context = context,
                                                    matchId = game.id,
                                                    teamName = game.homeTeamOrAthlete,
                                                    flag = game.homeFlagOrLogo,
                                                    opponent = game.awayTeamOrAthlete,
                                                    stadiumName = game.stadiumName,
                                                    timeStr = game.time,
                                                    epochMillis = triggerCal.timeInMillis
                                                )
                                                scheduledGameIds = scheduledGameIds + game.id
                                            }
                                        },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(
                                                if (isAlertScheduled) Color(0xFFF59E0B) else textSecondary.copy(alpha = 0.15f),
                                                RoundedCornerShape(10.dp)
                                            )
                                    ) {
                                        Icon(
                                            imageVector = if (isAlertScheduled) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                                            contentDescription = "Set Reminder",
                                            tint = if (isAlertScheduled) Color.White else textPrimary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }

                                    // Gemini AI Tactical Preview Toggle
                                    IconButton(
                                        onClick = {
                                            expandedAiGameId = if (isExpanded) null else game.id
                                        },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(
                                                if (isExpanded) Color(0xFF8B5CF6) else textSecondary.copy(alpha = 0.15f),
                                                RoundedCornerShape(10.dp)
                                            )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Psychology,
                                            contentDescription = "AI Match Analysis",
                                            tint = if (isExpanded) Color.White else Color(0xFFA78BFA),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                // Expanded AI Analysis Box
                                AnimatedVisibility(
                                    visible = isExpanded,
                                    enter = fadeIn(),
                                    exit = fadeOut()
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 10.dp)
                                            .background(
                                                if (isDark) Color(0xFF0F172A) else Color(0xFFF3E8FF),
                                                RoundedCornerShape(12.dp)
                                            )
                                            .border(
                                                1.dp,
                                                Color(0xFF8B5CF6).copy(alpha = 0.4f),
                                                RoundedCornerShape(12.dp)
                                            )
                                            .padding(10.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(text = "✨", fontSize = 14.sp)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "Gemini AI Tactical Analysis",
                                                fontWeight = FontWeight.Black,
                                                fontSize = 11.sp,
                                                color = Color(0xFFA78BFA)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = game.aiPreview,
                                            fontSize = 11.sp,
                                            color = textPrimary,
                                            lineHeight = 15.sp
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "Head-to-Head: ${game.h2hRecord}",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = textSecondary
                                        )
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
