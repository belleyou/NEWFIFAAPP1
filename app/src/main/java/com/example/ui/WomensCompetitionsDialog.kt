package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.InternationalCompetition
import com.example.data.NationalLeagueGame
import com.example.data.WomensCompetitionsDataProvider

enum class CompetitionTab {
    INTERNATIONAL,
    NATIONAL_LEAGUES
}

/**
 * WomensCompetitionsDialog
 *
 * Centered modal window allowing users to browse and select:
 * 1. International FIFA or Olympic games, and specific types of international women's sports.
 * 2. National games in each country across North America and LATAM.
 *
 * Tapping on any competition or league triggers 3D globe navigation to the host destination.
 */
@Composable
fun WomensCompetitionsDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onNavigateToLocation: (lat: Double, lon: Double, label: String, sportCategory: String) -> Unit
) {
    if (!isOpen) return

    var selectedTab by remember { mutableStateOf(CompetitionTab.INTERNATIONAL) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryFilter by remember { mutableStateOf("All") }
    var selectedCountryFilter by remember { mutableStateOf("All") }

    val neonLime = Color(0xFF76FF03)
    val neonEmerald = Color(0xFF00E676)
    val accentCyan = Color(0xFF00E5FF)
    val darkCardBg = Color(0xFF0D151E)
    val surfaceBorder = Color(0xFF1E2D3D)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.72f))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth(0.92f)
                    .heightIn(max = 640.dp)
                    .clickable(enabled = false) {}
                    .shadow(elevation = 24.dp, shape = RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            listOf(
                                neonLime.copy(alpha = 0.8f),
                                Color(0xFF00E5FF).copy(alpha = 0.5f),
                                surfaceBorder
                            )
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .background(darkCardBg)
                    .testTag("womens_competitions_dialog"),
                colors = CardDefaults.cardColors(containerColor = darkCardBg)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // DIALOG HEADER
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(neonLime.copy(alpha = 0.3f), Color(0xFF162533))
                                        )
                                    )
                                    .border(1.dp, neonLime, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = "Major Competitions",
                                    tint = neonLime,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = "WOMEN'S COMPETITIONS",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    letterSpacing = 0.8.sp
                                )
                                Text(
                                    text = "FIFA, Olympics & National Leagues",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1E293B))
                                .testTag("close_competitions_dialog_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // PRIMARY TABS (International FIFA / Olympics vs National Leagues)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF131D28))
                            .border(1.dp, Color(0xFF223447), RoundedCornerShape(14.dp))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CompetitionTabPill(
                            title = "🌐 International & Olympic",
                            isSelected = selectedTab == CompetitionTab.INTERNATIONAL,
                            activeColor = neonLime,
                            onClick = {
                                selectedTab = CompetitionTab.INTERNATIONAL
                                selectedCategoryFilter = "All"
                            },
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        CompetitionTabPill(
                            title = "🌎 National (NA & LATAM)",
                            isSelected = selectedTab == CompetitionTab.NATIONAL_LEAGUES,
                            activeColor = accentCyan,
                            onClick = {
                                selectedTab = CompetitionTab.NATIONAL_LEAGUES
                                selectedCountryFilter = "All"
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // SEARCH BOX
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF162230))
                            .border(1.dp, Color(0xFF2B3E52), RoundedCornerShape(10.dp))
                            .padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            singleLine = true,
                            textStyle = TextStyle(
                                color = Color.White,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.SansSerif
                            ),
                            cursorBrush = SolidColor(neonLime),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("competitions_search_input"),
                            decorationBox = { innerTextField ->
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = if (selectedTab == CompetitionTab.INTERNATIONAL)
                                            "Search FIFA, Olympics, rugby, cricket..."
                                        else
                                            "Search WNBA, NWSL, Liga MX, Superliga...",
                                        color = Color(0xFF64748B),
                                        fontSize = 12.sp
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // SUB-FILTER CHIPS
                    if (selectedTab == CompetitionTab.INTERNATIONAL) {
                        val categories = listOf(
                            "All",
                            "FIFA & Football",
                            "Olympic Games",
                            "Basketball",
                            "Volleyball",
                            "Cricket",
                            "Tennis",
                            "Field Hockey",
                            "Ice Hockey",
                            "Combat Sports",
                            "Gymnastics",
                            "Athletics",
                            "Aquatics",
                            "Cycling",
                            "Winter Sports",
                            "Endurance"
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            categories.forEach { cat ->
                                val isCatSelected = selectedCategoryFilter == cat
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(
                                            if (isCatSelected) neonLime.copy(alpha = 0.2f) else Color(0xFF131F2C)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = if (isCatSelected) neonLime else Color(0xFF223447),
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .clickable { selectedCategoryFilter = cat }
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = cat,
                                        fontSize = 11.sp,
                                        fontWeight = if (isCatSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isCatSelected) neonLime else Color(0xFFCBD5E1)
                                    )
                                }
                            }
                        }
                    } else {
                        val countries = listOf(
                            "All",
                            "🇺🇸 USA",
                            "🇨🇦 Canada",
                            "🇲🇽 Mexico",
                            "🇧🇷 Brazil",
                            "🇦🇷 Argentina",
                            "🇨🇴 Colombia",
                            "🇨🇱 Chile",
                            "🇨🇷 Costa Rica",
                            "🇵🇷 Puerto Rico",
                            "🇩🇴 Dominican Rep."
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            countries.forEach { cty ->
                                val isCtySelected = selectedCountryFilter == cty
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(
                                            if (isCtySelected) accentCyan.copy(alpha = 0.2f) else Color(0xFF131F2C)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = if (isCtySelected) accentCyan else Color(0xFF223447),
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .clickable { selectedCountryFilter = cty }
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = cty,
                                        fontSize = 11.sp,
                                        fontWeight = if (isCtySelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isCtySelected) accentCyan else Color(0xFFCBD5E1)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // SCROLLABLE LIST OF COMPETITIONS / LEAGUES
                    if (selectedTab == CompetitionTab.INTERNATIONAL) {
                        val filteredInternational = remember(searchQuery, selectedCategoryFilter) {
                            WomensCompetitionsDataProvider.internationalCompetitions.filter { comp ->
                                val matchesSearch = searchQuery.isBlank() ||
                                        comp.name.contains(searchQuery, ignoreCase = true) ||
                                        comp.sportType.contains(searchQuery, ignoreCase = true) ||
                                        comp.hostCountry.contains(searchQuery, ignoreCase = true) ||
                                        comp.category.contains(searchQuery, ignoreCase = true)

                                val matchesCategory = selectedCategoryFilter == "All" ||
                                        comp.category.equals(selectedCategoryFilter, ignoreCase = true)

                                matchesSearch && matchesCategory
                            }
                        }

                        if (filteredInternational.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No international competitions found",
                                    color = Color(0xFF64748B),
                                    fontSize = 13.sp
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(vertical = 4.dp)
                            ) {
                                items(filteredInternational, key = { it.id }) { comp ->
                                    InternationalCompetitionCard(
                                        item = comp,
                                        onSelect = {
                                            onNavigateToLocation(comp.lat, comp.lon, comp.name, comp.sportType)
                                            onDismiss()
                                        }
                                    )
                                }
                            }
                        }
                    } else {
                        val filteredNational = remember(searchQuery, selectedCountryFilter) {
                            WomensCompetitionsDataProvider.nationalLeagues.filter { league ->
                                val matchesSearch = searchQuery.isBlank() ||
                                        league.leagueName.contains(searchQuery, ignoreCase = true) ||
                                        league.sport.contains(searchQuery, ignoreCase = true) ||
                                        league.country.contains(searchQuery, ignoreCase = true) ||
                                        league.championOrTopClub.contains(searchQuery, ignoreCase = true)

                                val cleanCountry = selectedCountryFilter.replace(Regex("[^a-zA-Z ]"), "").trim()
                                val matchesCountry = selectedCountryFilter == "All" ||
                                        league.country.contains(cleanCountry, ignoreCase = true)

                                matchesSearch && matchesCountry
                            }
                        }

                        if (filteredNational.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No national leagues found",
                                    color = Color(0xFF64748B),
                                    fontSize = 13.sp
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(vertical = 4.dp)
                            ) {
                                items(filteredNational, key = { it.id }) { league ->
                                    NationalLeagueCard(
                                        league = league,
                                        onSelect = {
                                            onNavigateToLocation(league.lat, league.lon, league.leagueName, league.sport)
                                            onDismiss()
                                        }
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

@Composable
private fun CompetitionTabPill(
    title: String,
    isSelected: Boolean,
    activeColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) activeColor.copy(alpha = 0.2f) else Color.Transparent)
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = if (isSelected) activeColor else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
            color = if (isSelected) Color.White else Color(0xFF94A3B8),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun InternationalCompetitionCard(
    item: InternationalCompetition,
    onSelect: () -> Unit
) {
    val neonLime = Color(0xFF76FF03)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, Color(0xFF1E2D3D), RoundedCornerShape(14.dp))
            .background(Color(0xFF111C27))
            .clickable(onClick = onSelect),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF111C27))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1A2A38))
                    .border(1.dp, neonLime.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item.icon, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = item.name,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${item.sportType} • ${item.format}",
                    fontSize = 11.sp,
                    color = neonLime,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = item.flag, fontSize = 11.sp)
                    Text(
                        text = "${item.hostCity}, ${item.hostCountry} (${item.editionYear})",
                        fontSize = 10.5.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(neonLime.copy(alpha = 0.15f))
                    .border(1.dp, neonLime.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Explore,
                    contentDescription = "Fly to location",
                    tint = neonLime,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun NationalLeagueCard(
    league: NationalLeagueGame,
    onSelect: () -> Unit
) {
    val accentCyan = Color(0xFF00E5FF)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, Color(0xFF1E2D3D), RoundedCornerShape(14.dp))
            .background(Color(0xFF111C27))
            .clickable(onClick = onSelect),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF111C27))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1A2A38))
                    .border(1.dp, accentCyan.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = league.icon, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = league.flag, fontSize = 13.sp)
                    Text(
                        text = league.leagueName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${league.sport} • ${league.country} (${league.region})",
                    fontSize = 11.sp,
                    color = accentCyan,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${league.numTeams} • ${league.championOrTopClub}",
                    fontSize = 10.5.sp,
                    color = Color(0xFF94A3B8),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(accentCyan.copy(alpha = 0.15f))
                    .border(1.dp, accentCyan.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Explore,
                    contentDescription = "Focus league on globe",
                    tint = accentCyan,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
