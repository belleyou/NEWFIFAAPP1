package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.graphics.graphicsLayer
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.drawBehind
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
import com.example.model.HostStadium
import com.example.model.HostStadiumDataProvider
import com.example.model.WomensSportCategory
import com.example.model.WomensSportsDataProvider
import coil.compose.AsyncImage
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.service.GeminiService
import com.example.ui.theme.BrandOrangeRed
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

// Visual Theme Variations enum
enum class GlobeTheme {
    GLASS_LIGHT,
    COSMIC_DARK
}

// Stage filtering enum
enum class TournamentStage(val label: String) {
    ALL("All 48"),
    QUALIFIED("Qualified"),
    ROUND_32("All 32"),
    ROUND_16("All 16"),
    QUARTER("Quarter Finals"),
    SEMI("Semi Finals"),
    FINAL("Final"),
    BRONZE("Bronze Medal")
}

enum class AppLanguage(val code: String, val displayName: String) {
    EN("EN", "English"),
    TH("TH", "ไทย"),
    ES("ES", "Español"),
    CN("CN", "中文"),
    JP("JP", "日本語")
}

fun getRealTimeTeamsForStage(stage: TournamentStage, map: Map<String, List<String>>?): List<String>? {
    if (map != null) {
        map[stage.label]?.let { return it }
    }
    if (stage == TournamentStage.BRONZE) {
        val keys = listOf("Bronze Medal", "Third Place Play-off", "Third Place", "Bronze")
        if (map != null) {
            for (k in keys) {
                map[k]?.let { return it }
            }
        }
        return listOf("FRA", "ENG")
    }
    if (stage == TournamentStage.FINAL) {
        val keys = listOf("Final", "Championship Final", "FINAL", "Finals")
        if (map != null) {
            for (k in keys) {
                map[k]?.let { return it }
            }
        }
        return listOf("USA", "ESP")
    }
    if (stage == TournamentStage.ROUND_32) {
        val keys = listOf("All 32", "All 32", "Round of 32", "R32", "Round 32")
        if (map != null) {
            for (k in keys) {
                map[k]?.let { return it }
            }
        }
        return listOf("RSA", "CAN", "BRA", "JPN", "GER", "PAR", "NED", "MAR", "CIV", "NOR", "FRA", "SWE", "MEX", "ECU", "ENG", "COD", "BEL", "SEN", "ARG", "SUI", "URU", "CRO", "EGY", "NGA", "CMR", "TUR", "POL", "AUT", "CHI", "UKR", "DEN", "POR")
    }
    if (stage == TournamentStage.ROUND_16) {
        val keys = listOf("All 16", "Round of 16", "R16", "Round 16")
        if (map != null) {
            for (k in keys) {
                map[k]?.let { return it }
            }
        }
        return listOf("ARG", "FRA", "ESP", "BRA", "ENG", "USA", "MEX", "CAN", "GER", "POR", "NED", "BEL", "CRO", "URU", "COL")
    }
    if (stage == TournamentStage.QUARTER) {
        val keys = listOf("Quarter Finals", "Quarterfinals", "Quarter-Finals", "Quarters")
        if (map != null) {
            for (k in keys) {
                map[k]?.let { return it }
            }
        }
        return listOf("ARG", "FRA", "ESP", "BRA", "ENG", "USA", "MEX", "CAN")
    }
    if (stage == TournamentStage.SEMI) {
        val keys = listOf("Semi Finals", "Semifinals", "Semi-Finals", "Semis")
        if (map != null) {
            for (k in keys) {
                map[k]?.let { return it }
            }
        }
        return listOf("ARG", "ESP", "USA", "MEX")
    }
    if (stage == TournamentStage.ALL) {
        val keys = listOf("All 48", "Round of 48", "R48", "Round 48")
        if (map != null) {
            for (k in keys) {
                map[k]?.let { return it }
            }
        }
        return null
    }
    return null
}

fun localize(key: String, lang: AppLanguage): String {
    return when (lang) {
        AppLanguage.EN -> when (key) {
            "WOMEN SPORT" -> "WOMEN SPORTS"
            "WOMEN SPORTS" -> "WOMEN SPORTS"
            "WOMEN'S SPORTS" -> "WOMEN SPORTS"
            "WOMEN'S SPORTS 3D HUB" -> "WOMEN SPORTS"
            "FIFA 2027 WOMEN WORLD CUP" -> "FIFA 2027 WOMEN WORLD CUP"
            "All 48" -> "All 48"
            "Qualified" -> "Qualified"
            "All 32" -> "All 32"
            "All 36" -> "All 36"
            "All 16" -> "All 16"
            "Quarter Finals" -> "Quarter Finals"
            "Semi Finals" -> "Semi Finals"
            "Final" -> "Final"
            "Bronze Medal" -> "Bronze Medal"
            "NEXT MATCH" -> "NEXT MATCH"
            "TEAM OVERVIEW" -> "TEAM OVERVIEW"
            "STADIUM" -> "STADIUM"
            "HEAD COACH" -> "HEAD COACH"
            "KEY PLAYERS" -> "KEY PLAYERS"
            "VIEW ON MAP" -> "VIEW ON MAP"
            "Capacity" -> "Capacity"
            "FIFA Ranking" -> "FIFA Ranking"
            "Goals Scored" -> "Goals Scored"
            "Wins" -> "Wins"
            "Avg Poss" -> "Avg Poss"
            "Clean Sheets" -> "Clean Sheets"
            "COMPARE TEAMS" -> "COMPARE TEAMS"
            "VS Prediction" -> "VS Prediction"
            "AI Decides For Me" -> "AI Decides For Me"
            "Close" -> "Close"
            "Select Team" -> "Select Team"
            "Predicted Winner" -> "Predicted Winner"
            "ANALYZING..." -> "ANALYZING..."
            "Compare" -> "Compare"
            "OVERVIEW" -> "OVERVIEW"
            "SQUAD" -> "SQUAD"
            "STATS" -> "STATS"
            "SCHEDULE" -> "SCHEDULE"
            "NEWS" -> "NEWS"
            "HEAD_TO_HEAD" -> "HEAD-TO-HEAD"
            "PLAYERS" -> "PLAYERS"
            "INJURIES" -> "INJURIES"
            else -> key
        }
        AppLanguage.TH -> when (key) {
            "WOMEN SPORTS" -> "กีฬาหญิง"
            "WOMEN'S SPORTS" -> "กีฬาหญิง"
            "WOMEN'S SPORTS 3D HUB" -> "กีฬาหญิง"
            "FIFA 2027 WOMEN WORLD CUP" -> "ฟีฟ่า 2027 ทีมหญิง เวิลด์คัพ"
            "All 48" -> "ทั้งหมด 48 ทีม"
            "Qualified" -> "ทีมที่ผ่านเข้ารอบ"
            "All 32" -> "ทั้งหมด 32 ทีม"
            "All 36" -> "ทั้งหมด 36 ทีม"
            "All 16" -> "ทั้งหมด 16 ทีม"
            "Quarter Finals" -> "รอบ 8 ทีม"
            "Semi Finals" -> "รอบรองชนะเลิศ"
            "Final" -> "รอบชิงชนะเลิศ"
            "Bronze Medal" -> "นัดชิงอันดับสาม"
            "NEXT MATCH" -> "การแข่งขันถัดไป"
            "TEAM OVERVIEW" -> "ภาพรวมทีม"
            "STADIUM" -> "สนามกีฬา"
            "HEAD COACH" -> "หัวหน้าผู้ฝึกสอน"
            "KEY PLAYERS" -> "ผู้เล่นคนสำคัญ"
            "VIEW ON MAP" -> "ดูแผนที่"
            "Capacity" -> "ความจุ"
            "FIFA Ranking" -> "อันดับฟีฟ่า"
            "Goals Scored" -> "ประตูที่ทำได้"
            "Wins" -> "ชนะ"
            "Avg Poss" -> "ครองบอลเฉลี่ย"
            "Clean Sheets" -> "คลีนชีต"
            "COMPARE TEAMS" -> "เปรียบเทียบทีม"
            "VS Prediction" -> "ทำนายผลการแข่งขัน"
            "AI Decides For Me" -> "เอไอช่วยวิเคราะห์"
            "Close" -> "ปิด"
            "Select Team" -> "เลือกทีม"
            "Predicted Winner" -> "ผู้ชนะที่คาดการณ์"
            "ANALYZING..." -> "กำลังวิเคราะห์..."
            "Compare" -> "เปรียบเทียบ"
            "OVERVIEW" -> "ภาพรวม"
            "SQUAD" -> "รายชื่อผู้เล่น"
            "STATS" -> "สถิติ"
            "SCHEDULE" -> "ตารางแข่ง"
            "NEWS" -> "ข่าวสาร"
            "HEAD_TO_HEAD" -> "ตัวต่อตัว"
            "PLAYERS" -> "ผู้เล่น"
            "INJURIES" -> "ผู้บาดเจ็บ"
            else -> key
        }
        AppLanguage.ES -> when (key) {
            "WOMEN SPORTS" -> "DEPORTES FEMENINOS"
            "WOMEN'S SPORTS" -> "DEPORTES FEMENINOS"
            "WOMEN'S SPORTS 3D HUB" -> "DEPORTES FEMENINOS"
            "FIFA 2027 WOMEN WORLD CUP" -> "FIFA 2027 COPA MUNDIAL FEMENINA"
            "All 48" -> "Todos 48"
            "Qualified" -> "Clasificados"
            "All 32" -> "Todos 32"
            "All 36" -> "Todos 36"
            "All 16" -> "Todos 16"
            "Quarter Finals" -> "Cuartos"
            "Semi Finals" -> "Semifinales"
            "Final" -> "Final"
            "Bronze Medal" -> "Tercer Puesto"
            "NEXT MATCH" -> "PRÓXIMO PARTIDO"
            "TEAM OVERVIEW" -> "RESUMEN DEL EQUIPO"
            "STADIUM" -> "ESTADIO"
            "HEAD COACH" -> "ENTRENADOR"
            "KEY PLAYERS" -> "JUGADORES CLAVE"
            "VIEW ON MAP" -> "VER MAPA"
            "Capacity" -> "Capacidad"
            "FIFA Ranking" -> "Clasificación FIFA"
            "Goals Scored" -> "Goles Marcados"
            "Wins" -> "Victorias"
            "Avg Poss" -> "Posesión Prom"
            "Clean Sheets" -> "Porterías a Cero"
            "COMPARE TEAMS" -> "COMPARAR EQUIPOS"
            "VS Prediction" -> "Predicción"
            "AI Decides For Me" -> "AI Decide por Mí"
            "Close" -> "Cerrar"
            "Select Team" -> "Elegir Equipo"
            "Predicted Winner" -> "Ganador Predicho"
            "ANALYZING..." -> "ANALIZANDO..."
            "Compare" -> "Comparar"
            "OVERVIEW" -> "RESUMEN"
            "SQUAD" -> "PLANTILLA"
            "STATS" -> "ESTADÍSTICAS"
            "SCHEDULE" -> "CALENDARIO"
            "NEWS" -> "NOTICIAS"
            "HEAD_TO_HEAD" -> "FRENTE A FRENTE"
            "PLAYERS" -> "JUGADORES"
            "INJURIES" -> "LESIONES"
            else -> key
        }
        AppLanguage.CN -> when (key) {
            "WOMEN SPORTS" -> "女子体育"
            "WOMEN'S SPORTS" -> "女子体育"
            "WOMEN'S SPORTS 3D HUB" -> "女子体育"
            "FIFA 2027 WOMEN WORLD CUP" -> "FIFA 2027 女子世界杯"
            "All 48" -> "所有48强"
            "Qualified" -> "已出线球队"
            "All 32" -> "所有32强"
            "All 36" -> "所有36强"
            "All 16" -> "所有16强"
            "Quarter Finals" -> "1/4决赛"
            "Semi Finals" -> "半决赛"
            "Final" -> "决赛"
            "Bronze Medal" -> "三四名决赛"
            "NEXT MATCH" -> "下一场比赛"
            "TEAM OVERVIEW" -> "球队概况"
            "STADIUM" -> "体育场"
            "HEAD COACH" -> "主教练"
            "KEY PLAYERS" -> "核心球员"
            "VIEW ON MAP" -> "查看地图"
            "Capacity" -> "容纳人数"
            "FIFA Ranking" -> "FIFA 排名"
            "Goals Scored" -> "总进球数"
            "Wins" -> "获胜场次"
            "Avg Poss" -> "平均控球率"
            "Clean Sheets" -> "零封场次"
            "COMPARE TEAMS" -> "球队对比"
            "VS Prediction" -> "对决预测"
            "AI Decides For Me" -> "AI 帮我决定"
            "Close" -> "关闭"
            "Select Team" -> "选择球队"
            "Predicted Winner" -> "预测获胜者"
            "ANALYZING..." -> "分析中..."
            "Compare" -> "对比"
            "OVERVIEW" -> "概览"
            "SQUAD" -> "阵容"
            "STATS" -> "统计"
            "SCHEDULE" -> "赛程"
            "NEWS" -> "新闻"
            "HEAD_TO_HEAD" -> "对决"
            "PLAYERS" -> "球员"
            "INJURIES" -> "伤病"
            else -> key
        }
        AppLanguage.JP -> when (key) {
            "WOMEN SPORTS" -> "女子スポーツ"
            "WOMEN'S SPORTS" -> "女子スポーツ"
            "WOMEN'S SPORTS 3D HUB" -> "女子スポーツ"
            "FIFA 2027 WOMEN WORLD CUP" -> "FIFA 2027 女子ワールドカップ"
            "All 48" -> "全48チーム"
            "Qualified" -> "出場決定"
            "All 32" -> "全32チーム"
            "All 36" -> "全36チーム"
            "All 16" -> "全16チーム"
            "Quarter Finals" -> "準々決勝"
            "Semi Finals" -> "準決勝"
            "Final" -> "決勝"
            "Bronze Medal" -> "3位決定戦"
            "NEXT MATCH" -> "次の試合"
            "TEAM OVERVIEW" -> "チーム概要"
            "STADIUM" -> "スタジアム"
            "HEAD COACH" -> "ヘッドコーチ"
            "KEY PLAYERS" -> "キープレーヤー"
            "VIEW ON MAP" -> "地図で見る"
            "Capacity" -> "収容人数"
            "FIFA Ranking" -> "FIFAランキング"
            "Goals Scored" -> "得点数"
            "Wins" -> "勝利数"
            "Avg Poss" -> "平均支配率"
            "Clean Sheets" -> "クリーンシート"
            "COMPARE TEAMS" -> "チーム比較"
            "VS Prediction" -> "対戦予測"
            "AI Decides For Me" -> "AIに決めてもらう"
            "Close" -> "閉じる"
            "Select Team" -> "チーム選択"
            "Predicted Winner" -> "予想勝者"
            "ANALYZING..." -> "分析中..."
            "Compare" -> "比較"
            "OVERVIEW" -> "概要"
            "SQUAD" -> "選手"
            "STATS" -> "統計"
            "SCHEDULE" -> "日程"
            "NEWS" -> "ニュース"
            "HEAD_TO_HEAD" -> "直接対決"
            "PLAYERS" -> "選手"
            "INJURIES" -> "怪我"
            else -> key
        }
    }
}

fun getStageDisplayLabel(stage: TournamentStage, isWomensWorldCup: Boolean): String {
    return if (isWomensWorldCup && stage == TournamentStage.FINAL) {
        "Final"
    } else {
        stage.label
    }
}

fun getWomensTeamsForStage(stage: TournamentStage, realTimeMap: Map<String, List<String>>? = null): List<String> {
    if (realTimeMap != null) {
        val key = when (stage) {
            TournamentStage.ALL -> "All 32"
            TournamentStage.QUALIFIED -> "Qualified"
            TournamentStage.ROUND_32 -> "All 32"
            TournamentStage.ROUND_16 -> "All 16"
            TournamentStage.QUARTER -> "Quarter Finals"
            TournamentStage.SEMI -> "Semi Finals"
            TournamentStage.FINAL -> "Final"
            TournamentStage.BRONZE -> "Bronze Medal"
        }
        realTimeMap[key]?.let { return it }
    }

    val all32Qualified = listOf(
        "BRA", "USA", "ENG", "ESP", "GER", "FRA", "JPN", "AUS",
        "CAN", "SWE", "NED", "COL", "MAR", "NGA", "DEN", "ARG",
        "PHI", "CHN", "PRK", "KOR", "NZL", "ZAM", "ITA", "CRC",
        "CHI", "RSA", "JAM", "SCO", "CZE", "BIH", "HAI", "PER"
    )
    return when (stage) {
        TournamentStage.ALL -> all32Qualified
        TournamentStage.QUALIFIED -> all32Qualified
        TournamentStage.ROUND_32 -> all32Qualified
        TournamentStage.ROUND_16 -> emptyList()
        TournamentStage.QUARTER -> emptyList()
        TournamentStage.SEMI -> emptyList()
        TournamentStage.FINAL -> emptyList()
        TournamentStage.BRONZE -> emptyList()
    }
}

@Composable
fun RotatingStageSelector(
    selectedStage: TournamentStage,
    onStageSelected: (TournamentStage) -> Unit,
    currentLanguage: AppLanguage,
    theme: GlobeTheme,
    accentColor: Color,
    textColor: Color,
    isWomensWorldCup: Boolean = false,
    modifier: Modifier = Modifier
) {
    val stages = if (isWomensWorldCup) {
        listOf(
            TournamentStage.QUALIFIED,
            TournamentStage.ROUND_32,
            TournamentStage.ROUND_16,
            TournamentStage.QUARTER,
            TournamentStage.SEMI,
            TournamentStage.BRONZE,
            TournamentStage.FINAL
        )
    } else {
        listOf(
            TournamentStage.ALL,
            TournamentStage.ROUND_32,
            TournamentStage.ROUND_16,
            TournamentStage.QUARTER,
            TournamentStage.SEMI,
            TournamentStage.BRONZE,
            TournamentStage.FINAL
        )
    }
    val currentIndex = stages.indexOf(selectedStage).coerceAtLeast(0)

    val prevIndex = (currentIndex - 1 + stages.size) % stages.size
    val nextIndex = (currentIndex + 1) % stages.size

    val prevStage = stages[prevIndex]
    val nextStage = stages[nextIndex]

    // Vertical cylinder metallic brush
    val containerBgBrush = if (theme == GlobeTheme.GLASS_LIGHT) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFFCBD5E1),
                Color(0xFFF8FAFC),
                Color(0xFFCBD5E1)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0B0F19),
                Color(0xFF242F41),
                Color(0xFF0B0F19)
            )
        )
    }

    // High-resolution diagonal 3D bevel stroke brush
    val beveledBorderBrush = if (theme == GlobeTheme.GLASS_LIGHT) {
        Brush.linearGradient(
            colors = listOf(Color.White, Color.Black.copy(alpha = 0.25f)),
            start = Offset(0f, 0f),
            end = Offset.Infinite
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.White.copy(alpha = 0.35f), Color.Black.copy(alpha = 0.8f)),
            start = Offset(0f, 0f),
            end = Offset.Infinite
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(29.dp),
                clip = false
            )
            .background(
                brush = containerBgBrush,
                shape = RoundedCornerShape(29.dp)
            )
            .border(
                width = 1.5.dp,
                brush = beveledBorderBrush,
                shape = RoundedCornerShape(29.dp)
            )
            .drawWithContent {
                drawContent()
                // Left and right visual gradient fades to emphasize the physical curved/cylinder 3D depth of the drum
                drawRect(
                    brush = Brush.horizontalGradient(
                        0.0f to (if (theme == GlobeTheme.GLASS_LIGHT) Color.Black.copy(alpha = 0.12f) else Color.Black.copy(alpha = 0.5f)),
                        0.18f to Color.Transparent,
                        0.82f to Color.Transparent,
                        1.0f to (if (theme == GlobeTheme.GLASS_LIGHT) Color.Black.copy(alpha = 0.12f) else Color.Black.copy(alpha = 0.5f))
                    ),
                    size = size
                )
            }
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left Chevron Button (Rotated ChevronRight) - Styled as a 3D glass tactile button
        IconButton(
            onClick = {
                onStageSelected(stages[prevIndex])
            },
            modifier = Modifier
                .size(42.dp)
                .shadow(elevation = 4.dp, shape = CircleShape)
                .background(
                    brush = if (theme == GlobeTheme.GLASS_LIGHT) {
                        Brush.verticalGradient(listOf(Color.White, Color(0xFFE2E8F0)))
                    } else {
                        Brush.verticalGradient(listOf(Color(0xFF334155), Color(0xFF1E293B)))
                    },
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    brush = if (theme == GlobeTheme.GLASS_LIGHT) {
                        Brush.linearGradient(listOf(Color.White, Color.Black.copy(alpha = 0.15f)))
                    } else {
                        Brush.linearGradient(listOf(Color.White.copy(alpha = 0.25f), Color.Black.copy(alpha = 0.6f)))
                    },
                    shape = CircleShape
                )
                .testTag("stage_rotate_left_button")
        ) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Rotate Left",
                tint = textColor.copy(alpha = 0.85f),
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer(rotationZ = 180f)
            )
        }

        // Center Rotating Drum View
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        if (dragAmount > 20) {
                            onStageSelected(stages[prevIndex])
                            change.consume()
                        } else if (dragAmount < -20) {
                            onStageSelected(stages[nextIndex])
                            change.consume()
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val density = LocalDensity.current

                // Left Receding Item with dramatic 3D cylinder depth
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onStageSelected(prevStage)
                        }
                        .graphicsLayer {
                            rotationY = 32f
                            scaleX = 0.82f
                            scaleY = 0.82f
                            translationX = 14f
                            cameraDistance = 8f * density.density
                            alpha = 0.45f
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = localize(getStageDisplayLabel(prevStage, isWomensWorldCup), currentLanguage),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }

                // Center Highlighted Item - Styled as an illuminated, floating 3D glass control capsule
                Box(
                    modifier = Modifier
                        .weight(1.35f)
                        .shadow(
                            elevation = 5.dp,
                            shape = RoundedCornerShape(18.dp),
                            ambientColor = accentColor,
                            spotColor = accentColor,
                            clip = false
                        )
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    accentColor.copy(alpha = 0.28f),
                                    accentColor.copy(alpha = 0.08f),
                                    accentColor.copy(alpha = 0.28f)
                                )
                            ),
                            shape = RoundedCornerShape(18.dp)
                        )
                        .border(
                            width = 1.5.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(Color.White.copy(alpha = 0.75f), accentColor.copy(alpha = 0.45f)),
                                start = Offset(0f, 0f),
                                end = Offset.Infinite
                            ),
                            shape = RoundedCornerShape(18.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = localize(getStageDisplayLabel(selectedStage, isWomensWorldCup), currentLanguage),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = if (theme == GlobeTheme.GLASS_LIGHT) accentColor else Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }

                // Right Receding Item with dramatic 3D cylinder depth
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onStageSelected(nextStage)
                        }
                        .graphicsLayer {
                            rotationY = -32f
                            scaleX = 0.82f
                            scaleY = 0.82f
                            translationX = -14f
                            cameraDistance = 8f * density.density
                            alpha = 0.45f
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = localize(getStageDisplayLabel(nextStage, isWomensWorldCup), currentLanguage),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Right Chevron Button - Styled as a 3D glass tactile button
        IconButton(
            onClick = {
                onStageSelected(stages[nextIndex])
            },
            modifier = Modifier
                .size(42.dp)
                .shadow(elevation = 4.dp, shape = CircleShape)
                .background(
                    brush = if (theme == GlobeTheme.GLASS_LIGHT) {
                        Brush.verticalGradient(listOf(Color.White, Color(0xFFE2E8F0)))
                    } else {
                        Brush.verticalGradient(listOf(Color(0xFF334155), Color(0xFF1E293B)))
                    },
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    brush = if (theme == GlobeTheme.GLASS_LIGHT) {
                        Brush.linearGradient(listOf(Color.White, Color.Black.copy(alpha = 0.15f)))
                    } else {
                        Brush.linearGradient(listOf(Color.White.copy(alpha = 0.25f), Color.Black.copy(alpha = 0.6f)))
                    },
                    shape = CircleShape
                )
                .testTag("stage_rotate_right_button")
        ) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Rotate Right",
                tint = textColor.copy(alpha = 0.85f),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun FifaLogoZoomSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedRange<Float>,
    modifier: Modifier = Modifier,
    accentColor: Color,
    theme: GlobeTheme,
    textColor: Color
) {
    var isDragging by remember { mutableStateOf(false) }
    
    // Smooth physical spring animations for 3D tactile feedback
    val animatedScale by animateFloatAsState(
        targetValue = if (isDragging) 1.25f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
    )
    val animatedRotation by animateFloatAsState(
        targetValue = if (isDragging) 18f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
    )

    // Vertical cylinder metallic brush for container
    val containerBgBrush = if (theme == GlobeTheme.GLASS_LIGHT) {
        Brush.verticalGradient(
            colors = listOf(Color(0xFFE2E8F0), Color(0xFFF8FAFC), Color(0xFFE2E8F0))
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(Color(0xFF0F172A), Color(0xFF242F41), Color(0xFF0F172A))
        )
    }

    // 3D Bevel border stroke
    val bevelBorderBrush = if (theme == GlobeTheme.GLASS_LIGHT) {
        Brush.linearGradient(
            colors = listOf(Color.White, Color.Black.copy(alpha = 0.2f)),
            start = Offset(0f, 0f),
            end = Offset.Infinite
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.White.copy(alpha = 0.3f), Color.Black.copy(alpha = 0.7f)),
            start = Offset(0f, 0f),
            end = Offset.Infinite
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(28.dp),
                clip = false
            )
            .background(
                brush = containerBgBrush,
                shape = RoundedCornerShape(28.dp)
            )
            .border(
                width = 1.5.dp,
                brush = bevelBorderBrush,
                shape = RoundedCornerShape(28.dp)
            )
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Zoom Out Button - Styled as 3D tactile push key
        IconButton(
            onClick = {
                val step = (valueRange.endInclusive - valueRange.start) / 8f
                val newValue = (value - step).coerceIn(valueRange.start, valueRange.endInclusive)
                onValueChange(newValue)
            },
            modifier = Modifier
                .size(36.dp)
                .shadow(elevation = 2.dp, shape = CircleShape)
                .background(
                    brush = if (theme == GlobeTheme.GLASS_LIGHT) {
                        Brush.verticalGradient(listOf(Color.White, Color(0xFFF1F5F9)))
                    } else {
                        Brush.verticalGradient(listOf(Color(0xFF334155), Color(0xFF1E293B)))
                    },
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    brush = if (theme == GlobeTheme.GLASS_LIGHT) {
                        Brush.linearGradient(listOf(Color.White, Color.Black.copy(alpha = 0.1f)))
                    } else {
                        Brush.linearGradient(listOf(Color.White.copy(alpha = 0.2f), Color.Black.copy(alpha = 0.5f)))
                    },
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.ZoomOut,
                contentDescription = "Zoom Out",
                tint = textColor.copy(alpha = 0.85f),
                modifier = Modifier.size(18.dp)
            )
        }

        // Draggable Track & Thumb
        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            contentAlignment = Alignment.CenterStart
        ) {
            val widthPx = constraints.maxWidth.toFloat()
            val thumbSize = 38.dp
            val density = LocalDensity.current
            val thumbSizePx = with(density) { thumbSize.toPx() }
            val thumbRadiusPx = thumbSizePx / 2f
            
            // Track Width for clamping
            val trackWidthPx = widthPx - thumbSizePx
            val fraction = ((value - valueRange.start) / (valueRange.endInclusive - valueRange.start)).coerceIn(0f, 1f)
            val thumbOffsetPx = thumbRadiusPx + fraction * trackWidthPx

            // Track Background Capsule - Styled as a recessed 3D groove
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(
                        brush = if (theme == GlobeTheme.GLASS_LIGHT) {
                            Brush.verticalGradient(listOf(Color(0xFFCBD5E1), Color.White))
                        } else {
                            Brush.verticalGradient(listOf(Color(0xFF090D16), Color(0xFF334155)))
                        },
                        shape = RoundedCornerShape(4.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = if (theme == GlobeTheme.GLASS_LIGHT) Color.Black.copy(alpha = 0.08f) else Color.White.copy(alpha = 0.05f),
                        shape = RoundedCornerShape(4.dp)
                    )
            ) {
                // Active Track Highlight
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(with(density) { thumbOffsetPx.toDp() })
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(accentColor.copy(alpha = 0.7f), accentColor)
                            ),
                            shape = RoundedCornerShape(4.dp)
                        )
                )
            }

            // Draggable Thumb Container - Raised 3D Dial with rotating Trophy
            Box(
                modifier = Modifier
                    .offset(x = with(density) { (thumbOffsetPx - thumbRadiusPx).toDp() })
                    .size(thumbSize)
                    .graphicsLayer {
                        scaleX = animatedScale
                        scaleY = animatedScale
                        rotationZ = animatedRotation
                    }
                    .shadow(
                        elevation = if (isDragging) 8.dp else 4.dp,
                        shape = CircleShape,
                        clip = false
                    )
                    .background(Color.White, shape = CircleShape)
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(Color.White, accentColor)
                        ),
                        shape = CircleShape
                    )
                    .pointerInput(widthPx, trackWidthPx) {
                        detectHorizontalDragGestures(
                            onDragStart = { isDragging = true },
                            onDragEnd = { isDragging = false },
                            onDragCancel = { isDragging = false },
                            onHorizontalDrag = { change, dragAmount ->
                                change.consume()
                                val currentFraction = ((value - valueRange.start) / (valueRange.endInclusive - valueRange.start)).coerceIn(0f, 1f)
                                val currentOffsetPx = thumbRadiusPx + currentFraction * trackWidthPx
                                val newOffsetPx = (currentOffsetPx + dragAmount).coerceIn(thumbRadiusPx, widthPx - thumbRadiusPx)
                                val newFraction = (newOffsetPx - thumbRadiusPx) / trackWidthPx
                                val newValue = valueRange.start + newFraction * (valueRange.endInclusive - valueRange.start)
                                onValueChange(newValue)
                            }
                        )
                    }
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🏆",
                    fontSize = 20.sp
                )
            }
        }

        // Zoom In Button - Styled as 3D tactile push key
        IconButton(
            onClick = {
                val step = (valueRange.endInclusive - valueRange.start) / 8f
                val newValue = (value + step).coerceIn(valueRange.start, valueRange.endInclusive)
                onValueChange(newValue)
            },
            modifier = Modifier
                .size(36.dp)
                .shadow(elevation = 2.dp, shape = CircleShape)
                .background(
                    brush = if (theme == GlobeTheme.GLASS_LIGHT) {
                        Brush.verticalGradient(listOf(Color.White, Color(0xFFF1F5F9)))
                    } else {
                        Brush.verticalGradient(listOf(Color(0xFF334155), Color(0xFF1E293B)))
                    },
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    brush = if (theme == GlobeTheme.GLASS_LIGHT) {
                        Brush.linearGradient(listOf(Color.White, Color.Black.copy(alpha = 0.1f)))
                    } else {
                        Brush.linearGradient(listOf(Color.White.copy(alpha = 0.2f), Color.Black.copy(alpha = 0.5f)))
                    },
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.ZoomIn,
                contentDescription = "Zoom In",
                tint = textColor.copy(alpha = 0.85f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

data class ProjectedPoint(
    val team: Team,
    val screenX: Float,
    val screenY: Float,
    val rotatedZ: Float, // Used for hemispherical occlusion (visible if > 0)
    val isInteractive: Boolean
)

private const val LIVE_STAGE_REFRESH_MS = 5 * 60 * 1000L

private fun parseStageTeamsJson(jsonStr: String): Map<String, List<String>> {
    if (jsonStr.isBlank()) return emptyMap()

    val jsonObject = org.json.JSONObject(jsonStr)
    val parsedMap = mutableMapOf<String, List<String>>()
    val keys = jsonObject.keys()

    while (keys.hasNext()) {
        val key = keys.next()
        val array = jsonObject.optJSONArray(key) ?: continue
        val teams = buildList {
            for (i in 0 until array.length()) {
                val code = array.optString(i).trim().uppercase()
                if (code.isNotBlank()) add(code)
            }
        }.distinct()

        parsedMap[key] = teams
    }

    return parsedMap
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobeScreen() {
    val coroutineScope = rememberCoroutineScope()
    val context = androidx.compose.ui.platform.LocalContext.current
    val database = remember { com.example.data.DatabaseProvider.getDatabase(context) }
    val favoritesDao = remember { database.favoritesDao() }
    val notificationsDao = remember { database.matchNotificationDao() }
    
    val favoriteTeams by favoritesDao.getFavoriteTeamsFlow().collectAsState(initial = emptyList())
    val matchNotifications by notificationsDao.getMatchNotificationsFlow().collectAsState(initial = emptyList())

    val permissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            android.widget.Toast.makeText(context, "Notifications enabled! Reminders will trigger 30 min before matches.", android.widget.Toast.LENGTH_LONG).show()
        } else {
            android.widget.Toast.makeText(context, "Notification permission denied. Reminders cannot be posted.", android.widget.Toast.LENGTH_LONG).show()
        }
    }

    fun ensureNotificationPermission(onGranted: () -> Unit) {
        val hasPermission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            androidx.core.content.ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
        if (hasPermission) {
            onGranted()
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    
    // Theme state (default to COSMIC_DARK: deep slate navy)
    var currentTheme by remember { mutableStateOf(GlobeTheme.COSMIC_DARK) }
    
    // Language state (EN, TH, ES, CN, JP)
    var currentLanguage by remember { mutableStateOf(AppLanguage.EN) }
    var isLanguageMenuExpanded by remember { mutableStateOf(false) }
    
    // Stage Filter state
    var selectedStage by remember { mutableStateOf(TournamentStage.FINAL) }

    // Real-time team abbreviations list for stages fetched dynamically from Gemini API / FIFA Live Feeds
    var realTimeAdvancedTeams by remember {
        mutableStateOf<Map<String, List<String>>?>(null)
    }

    var realTimeWomensAdvancedTeams by remember {
        mutableStateOf<Map<String, List<String>>?>(null)
    }

    // Globe position states
    var rotX by remember { mutableStateOf(0.4f) }
    var rotY by remember { mutableStateOf(0.8f) }
    var zoomScale by remember { mutableStateOf(1.0f) }
    
    // Selection state
    var selectedTeam by remember { mutableStateOf<Team?>(null) }
    var profileTab by remember { mutableStateOf(ProfileTab.OVERVIEW) }
    
    // Stadiums and games exploration states
    var isPlaybookOpen by remember { mutableStateOf(false) }
    var isGamesSheetOpen by remember { mutableStateOf(false) }
    var isStadiumsSheetOpen by remember { mutableStateOf(false) }
    var isCompetitionsDialogOpen by remember { mutableStateOf(false) }
    var stadiumScopeFilter by remember { mutableStateOf("ALL") }
    var stadiumSportFilter by remember { mutableStateOf("ALL") }
    var selectedSportCategory by remember { mutableStateOf(WomensSportCategory.ALL) }
    var selectedStadiumId by remember { mutableStateOf<String?>(null) }
    var galleryStadium by remember { mutableStateOf<HostStadium?>(null) }
    var arCameraStadium by remember { mutableStateOf<HostStadium?>(null) }
    var hudRotationTarget by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    
    // Comparison drawer states
    var compareTeam1 by remember { mutableStateOf<Team?>(TeamDataProvider.womensTeams.getOrNull(0)) }
    var compareTeam2 by remember { mutableStateOf<Team?>(TeamDataProvider.womensTeams.getOrNull(1)) }
    var isCompareDrawerOpen by remember { mutableStateOf(false) }

    // Side panel matches & performance metrics states
    var isSidePanelOpen by remember { mutableStateOf(false) }
    var sidePanelStadium by remember { mutableStateOf<HostStadium?>(null) }
    var activeReminders by remember { mutableStateOf<Set<String>>(emptySet()) }
    var isMetricsSheetOpen by remember { mutableStateOf(false) }
    var selectedTeamForMetrics by remember { mutableStateOf<Team?>(null) }
    var highlightedStadiumId by remember { mutableStateOf<String?>(null) }

    // Active tournament states - Dedicated Women's Sports Hub
    var isWomensWorldCup by remember { mutableStateOf(true) }
    val teams = TeamDataProvider.womensTeams

    // Poll the live-stage feed automatically.
    LaunchedEffect(Unit) {
        while (true) {
            try {
                val jsonStr = GeminiService.getRealTimeWomensWorldCupTeams()
                val parsedMap = parseStageTeamsJson(jsonStr)
                if (parsedMap.isNotEmpty()) {
                    realTimeWomensAdvancedTeams = parsedMap
                }
            } catch (e: Exception) {
                android.util.Log.e(
                    "GlobeScreen",
                    "Unable to refresh live tournament stages; keeping last successful data",
                    e
                )
            }

            delay(LIVE_STAGE_REFRESH_MS)
        }
    }

    // Real-time Weather state
    var realTimeWeather by remember { mutableStateOf<com.example.model.WeatherService.RealTimeWeather?>(null) }
    var allStadiumsWeatherMap by remember { mutableStateOf<Map<String, com.example.model.WeatherService.RealTimeWeather>>(emptyMap()) }

    LaunchedEffect(Unit) {
        val list = HostStadiumDataProvider.womensHostStadiums
        val resultMap = mutableMapOf<String, com.example.model.WeatherService.RealTimeWeather>()
        list.forEach { stad ->
            try {
                val w = com.example.model.WeatherService.fetchWeather(stad.latitude, stad.longitude)
                resultMap[stad.id] = w
            } catch (e: Exception) {
                // Keep fallback
            }
        }
        allStadiumsWeatherMap = resultMap
    }

    LaunchedEffect(selectedStage) {
        highlightedStadiumId = null
    }

    LaunchedEffect(selectedTeam, selectedStage) {
        realTimeWeather = null
        selectedTeam?.let { team ->
            try {
                val lat = team.nextMatch.stadium.latitude
                val lon = team.nextMatch.stadium.longitude
                val fetched = com.example.model.WeatherService.fetchWeather(lat, lon)
                realTimeWeather = fetched
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    LaunchedEffect(realTimeWomensAdvancedTeams) {
        compareTeam1 = TeamDataProvider.womensTeams.getOrNull(0)
        compareTeam2 = TeamDataProvider.womensTeams.getOrNull(1)
        selectedTeam = null
        
        val stagesToCheck = listOf(
            TournamentStage.FINAL,
            TournamentStage.BRONZE,
            TournamentStage.SEMI,
            TournamentStage.QUARTER,
            TournamentStage.ROUND_16,
            TournamentStage.ROUND_32,
            TournamentStage.QUALIFIED
        )
        val mostUpdated = stagesToCheck.firstOrNull { stage ->
            getWomensTeamsForStage(stage, realTimeWomensAdvancedTeams).isNotEmpty()
        } ?: TournamentStage.QUALIFIED
        selectedStage = mostUpdated
    }
    
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

    // Dynamic background brush based on selected variation (using deep slate navy palette)
    val backgroundBrush = when (currentTheme) {
        GlobeTheme.GLASS_LIGHT -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0F172A), // Deep Slate Navy
                Color(0xFF1E1E38), // Rich Indigo Space
                Color(0xFF020617)  // Deep abyss
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

    val textColor = Color(0xFFF8FAFC)

    val cardBgColor = Color(0xFF1E293B).copy(alpha = 0.9f)

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
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 3D FLOATING HEADER CONSOLE DECK
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(24.dp),
                        clip = false
                    )
                    .background(
                        brush = if (currentTheme == GlobeTheme.GLASS_LIGHT) {
                            Brush.verticalGradient(listOf(Color(0xFFF1F5F9), Color.White))
                        } else {
                            Brush.verticalGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A)))
                        },
                        shape = RoundedCornerShape(24.dp)
                    )
                    .border(
                        width = 1.5.dp,
                        brush = if (currentTheme == GlobeTheme.GLASS_LIGHT) {
                            Brush.linearGradient(listOf(Color.White, Color.Black.copy(alpha = 0.12f)))
                        } else {
                            Brush.linearGradient(listOf(Color.White.copy(alpha = 0.22f), Color.Black.copy(alpha = 0.65f)))
                        },
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 14.dp)
            ) {
                // APP HEADER with Sleek Avatar + GAMES on Left, WOMEN SPORTS in Center of blank space, and Grid Menu on Right
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left side: Sport Avatar button + Games button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val sportLogoVector = when (selectedSportCategory) {
                            WomensSportCategory.BASKETBALL -> Icons.Default.SportsBasketball
                            WomensSportCategory.SOCCER -> Icons.Default.SportsSoccer
                            WomensSportCategory.TENNIS -> Icons.Default.SportsTennis
                            WomensSportCategory.ICE_HOCKEY -> Icons.Default.SportsHockey
                            WomensSportCategory.VOLLEYBALL -> Icons.Default.SportsVolleyball
                            WomensSportCategory.CRICKET -> Icons.Default.SportsCricket
                            WomensSportCategory.ALL -> Icons.Default.SportsBasketball
                        }
                        val sportLogoTint = when (selectedSportCategory) {
                            WomensSportCategory.BASKETBALL -> Color(0xFF76FF03)
                            WomensSportCategory.SOCCER -> Color(0xFF00E676)
                            WomensSportCategory.TENNIS -> Color(0xFFC6FF00)
                            WomensSportCategory.ICE_HOCKEY -> Color(0xFF00E5FF)
                            WomensSportCategory.VOLLEYBALL -> Color(0xFFFFD600)
                            WomensSportCategory.CRICKET -> Color(0xFFFF5252)
                            WomensSportCategory.ALL -> Color(0xFF76FF03)
                        }

                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.radialGradient(
                                        listOf(sportLogoTint.copy(alpha = 0.25f), Color(0xFF0F172A))
                                    )
                                )
                                .border(1.5.dp, sportLogoTint, CircleShape)
                                .clickable {
                                    val allCats = WomensSportCategory.entries
                                    val nextIndex = (allCats.indexOf(selectedSportCategory) + 1) % allCats.size
                                    selectedSportCategory = allCats[nextIndex]
                                }
                                .testTag("top_avatar_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = sportLogoVector,
                                contentDescription = "Women Sports: ${selectedSportCategory.displayName}",
                                tint = sportLogoTint,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Competition Selector pill immediately to the right-hand side of the left circle
                        Box(
                            modifier = Modifier
                                .height(32.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF1E293B).copy(alpha = 0.85f))
                                .border(1.dp, Color(0xFF76FF03).copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                                .clickable { isCompetitionsDialogOpen = true }
                                .padding(horizontal = 8.dp)
                                .testTag("header_games_selector_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = "International & National Games",
                                    tint = Color(0xFF76FF03),
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = "GAMES",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // Center of the blank space: WOMEN SPORTS title
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val titleText = if (currentLanguage == AppLanguage.EN) "WOMEN SPORTS" else localize("WOMEN SPORTS", currentLanguage)
                        Text(
                            text = titleText,
                            color = accentColor,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.SansSerif,
                            textAlign = TextAlign.Center,
                            letterSpacing = 0.5.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }

                    // Right side: Grid Options button
                    IconButton(
                        onClick = { isCompareDrawerOpen = !isCompareDrawerOpen },
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B))
                            .testTag("top_grid_menu_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.GridView,
                            contentDescription = "Options & Comparison",
                            tint = Color(0xFFF1F5F9),
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }

            if (favoriteTeams.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "⭐ FAVORITE TEAMS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = accentColor,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(favoriteTeams) { fav ->
                            val matchedTeam = TeamDataProvider.womensTeams
                                .find { it.abbreviation.lowercase() == fav.abbreviation.lowercase() }
                            
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF1E293B) else Color.White
                                    )
                                    .border(
                                        1.dp,
                                        if (selectedTeam?.abbreviation == fav.abbreviation) accentColor else textColor.copy(alpha = 0.1f),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable {
                                        if (matchedTeam != null) {
                                            selectedTeam = matchedTeam
                                            isWomensWorldCup = true
                                        }
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = fav.flag, fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = fav.abbreviation,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = textColor
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notifications active",
                                    tint = accentColor,
                                    modifier = Modifier.size(10.dp)
                                )
                            }
                        }
                    }
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
                if (!isCompareDrawerOpen) {
                    val filteredTeams = TeamDataProvider.womensTeams
                    InteractiveThreeJsGlobe(
                        selectedTeam = selectedTeam,
                        onTeamSelected = { selectedTeam = it },
                        theme = currentTheme,
                        stageLabel = getStageDisplayLabel(selectedStage, true),
                        zoomScale = zoomScale,
                        realTimeTeams = getWomensTeamsForStage(selectedStage, realTimeWomensAdvancedTeams),
                        activeTeams = filteredTeams,
                        selectedStadiumId = selectedStadiumId,
                        onStadiumSelected = { id ->
                            selectedStadiumId = id
                            selectedTeam = null
                            val activeList = HostStadiumDataProvider.womensHostStadiums
                            val matched = activeList.find { it.id == id } ?: HostStadiumDataProvider.hostStadiums.find { it.id == id }
                            if (matched != null) {
                                galleryStadium = matched
                                sidePanelStadium = matched
                                isSidePanelOpen = true
                            }
                        },
                        onRemindMatch = { stadiumId, matchTitle, dateStr, timeStr ->
                            val activeList = HostStadiumDataProvider.womensHostStadiums
                            val matched = activeList.find { it.id == stadiumId } ?: HostStadiumDataProvider.hostStadiums.find { it.id == stadiumId }
                            val venueName = matched?.name ?: "Venue"
                            activeReminders = activeReminders + stadiumId
                            com.example.service.MatchNotificationManager.scheduleStadiumReminder(
                                context = context,
                                stadiumId = stadiumId,
                                matchTitle = matchTitle,
                                stadiumName = venueName,
                                timeStr = timeStr
                            )
                            android.widget.Toast.makeText(
                                context,
                                "🔔 Reminder set for $venueName ($timeStr)",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        },
                        isWomensWorldCup = true,
                        highlightedStadiumId = highlightedStadiumId,
                        onTeamBadgeClicked = { team ->
                            selectedTeamForMetrics = team
                            isMetricsSheetOpen = true
                        },
                        targetRotationTarget = hudRotationTarget,
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("interactive_3d_globe")
                    )
                }
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
                            val liveStageCodes = if (isWomensWorldCup) {
                                getWomensTeamsForStage(selectedStage, realTimeWomensAdvancedTeams).toSet()
                            } else {
                                getRealTimeTeamsForStage(
                                    selectedStage,
                                    realTimeAdvancedTeams
                                )?.toSet().orEmpty()
                            }

                            val isMatchInSelectedStage =
                                selectedStage == TournamentStage.ALL ||
                                selectedStage == TournamentStage.QUALIFIED ||
                                team.abbreviation in liveStageCodes

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

            if (!isCompareDrawerOpen) {
                // GOLFX-INSPIRED ROTATING HUD (Country, City, Sport)
                WomenSportsRotatingHud(
                    onRotateToCoordinates = { lat, lon, zoom ->
                        hudRotationTarget = Pair(lat, lon)
                    },
                    onCountrySelected = { country ->
                        val matched = TeamDataProvider.womensTeams.find { it.abbreviation.equals(country.abbrev, ignoreCase = true) }
                        if (matched != null) {
                            selectedTeam = matched
                        }
                        hudRotationTarget = Pair(country.lat, country.lon)
                    },
                    onCitySelected = { city ->
                        selectedStadiumId = city.stadiumId
                        val matchedStadium = HostStadiumDataProvider.womensHostStadiums.find { it.id == city.stadiumId }
                        if (matchedStadium != null) {
                            galleryStadium = matchedStadium
                        }
                        hudRotationTarget = Pair(city.lat, city.lon)
                    },
                    onSportSelected = { sport ->
                        selectedStadiumId = sport.venueId
                        val matchedStadium = HostStadiumDataProvider.womensHostStadiums.find { it.id == sport.venueId }
                        if (matchedStadium != null) {
                            galleryStadium = matchedStadium
                        }
                        hudRotationTarget = Pair(sport.lat, sport.lon)
                        selectedSportCategory = when (sport.name.lowercase()) {
                            "basketball" -> WomensSportCategory.BASKETBALL
                            "soccer" -> WomensSportCategory.SOCCER
                            "tennis" -> WomensSportCategory.TENNIS
                            "ice hockey" -> WomensSportCategory.ICE_HOCKEY
                            "volleyball" -> WomensSportCategory.VOLLEYBALL
                            "cricket" -> WomensSportCategory.CRICKET
                            else -> WomensSportCategory.ALL
                        }
                    },
                    onStadiumSelected = { stadiumItem ->
                        selectedStadiumId = stadiumItem.id
                        val matchedStadium = HostStadiumDataProvider.womensHostStadiums.find { it.id == stadiumItem.id }
                            ?: HostStadiumDataProvider.hostStadiums.find { it.id == stadiumItem.id }
                        if (matchedStadium != null) {
                            sidePanelStadium = matchedStadium
                            isSidePanelOpen = true
                        }
                        hudRotationTarget = Pair(stadiumItem.lat, stadiumItem.lon)
                        selectedSportCategory = when (stadiumItem.sport.lowercase()) {
                            "basketball" -> WomensSportCategory.BASKETBALL
                            "soccer" -> WomensSportCategory.SOCCER
                            "tennis" -> WomensSportCategory.TENNIS
                            "ice hockey" -> WomensSportCategory.ICE_HOCKEY
                            "volleyball" -> WomensSportCategory.VOLLEYBALL
                            "cricket" -> WomensSportCategory.CRICKET
                            else -> WomensSportCategory.ALL
                        }
                    },
                    modifier = Modifier.testTag("women_sports_rotating_hud")
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
                    val displayNextMatch = remember(team, selectedStage) {
                        team.nextMatch
                    }
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

                                    val isCurrentTeamFav = favoriteTeams.any { it.abbreviation.lowercase() == team.abbreviation.lowercase() }
                                    IconButton(
                                        onClick = {
                                            ensureNotificationPermission {
                                                coroutineScope.launch {
                                                    if (isCurrentTeamFav) {
                                                        favoritesDao.deleteFavorite(team.abbreviation)
                                                        com.example.service.MatchNotificationManager.cancelMatchAlarm(context, team.abbreviation)
                                                        android.widget.Toast.makeText(context, "${team.name} removed from favorites", android.widget.Toast.LENGTH_SHORT).show()
                                                    } else {
                                                        favoritesDao.insertFavorite(
                                                            com.example.data.FavoriteTeam(
                                                                abbreviation = team.abbreviation,
                                                                name = team.name,
                                                                flag = team.flag,
                                                                isNotificationEnabled = true
                                                            )
                                                        )
                                                        val epochMillis = com.example.service.MatchNotificationManager.parseMatchTimeToEpoch(
                                                            displayNextMatch.date,
                                                            displayNextMatch.time
                                                        )
                                                        if (epochMillis > 0L) {
                                                            com.example.service.MatchNotificationManager.scheduleMatchAlarm(
                                                                context = context,
                                                                matchId = team.abbreviation,
                                                                teamName = team.name,
                                                                flag = team.flag,
                                                                opponent = displayNextMatch.opponent,
                                                                stadiumName = displayNextMatch.stadium.name,
                                                                timeStr = displayNextMatch.time,
                                                                epochMillis = epochMillis
                                                            )
                                                        }
                                                        android.widget.Toast.makeText(context, "${team.name} added to favorites! Notification scheduled.", android.widget.Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            }
                                        },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = if (isCurrentTeamFav) Icons.Default.Star else Icons.Default.StarBorder,
                                            contentDescription = "Favorite",
                                            tint = if (isCurrentTeamFav) Color(0xFFFBBF24) else Color.White,
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
                                        text = if (team.abbreviation == "USA") "United States Women's National Team (USWNT)" else "${team.name} National Team",
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
                                            text = "${localize("FIFA Ranking", currentLanguage)}: #${team.fifaRanking}",
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
                                            text = localize(tab.name, currentLanguage),
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
                                        val activeStageAbbrev = if (isWomensWorldCup) getWomensTeamsForStage(selectedStage, realTimeWomensAdvancedTeams) else getRealTimeTeamsForStage(selectedStage, realTimeAdvancedTeams)
                                        val isCurrentTeamActive = activeStageAbbrev == null || activeStageAbbrev.any { it.lowercase() == team.abbreviation.lowercase() }

                                        // A. MATCH Section
                                        item {
                                            Row(
                                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = if (isCurrentTeamActive) localize("NEXT MATCH", currentLanguage) else "LAST TOURNAMENT MATCH",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = if (isCurrentTeamActive) Color(0xFF0D9488) else Color(0xFFEF4444)
                                                )
                                                
                                                if (isCurrentTeamActive) {
                                                    Text(text = "Quarter-Final", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = textColor.copy(alpha = 0.5f))
                                                } else {
                                                    Box(
                                                        modifier = Modifier
                                                            .background(Color(0xFFEF4444).copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                                    ) {
                                                        Text(text = "ELIMINATED", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color(0xFFEF4444))
                                                    }
                                                }
                                            }

                                            Card(
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(16.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF1E293B) else Color.White
                                                ),
                                                border = BorderStroke(1.dp, if (isCurrentTeamActive) textColor.copy(alpha = 0.08f) else Color(0xFFEF4444).copy(alpha = 0.2f))
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

                                                        // Center Label: VS or Result
                                                        val lastPathItem = team.path.lastOrNull() ?: "Group Stage: Lost 1-2 vs Denmark"
                                                        val lastMatchStage = lastPathItem.substringBefore(":", "Completed")
                                                        val rest = lastPathItem.substringAfter(":", "")
                                                        val opponentName = if (isCurrentTeamActive) displayNextMatch.opponent else rest.substringAfter("vs ", "TBD").trim()
                                                        val matchResult = if (isCurrentTeamActive) "VS" else {
                                                            val r = rest.substringBefore(" vs", "Completed").trim()
                                                            if (r.isEmpty()) "Completed" else r
                                                        }

                                                        Column(
                                                            horizontalAlignment = Alignment.CenterHorizontally,
                                                            modifier = Modifier.padding(horizontal = 4.dp)
                                                        ) {
                                                            Text(
                                                                text = matchResult,
                                                                fontWeight = FontWeight.Black,
                                                                fontSize = if (isCurrentTeamActive) 14.sp else 12.sp,
                                                                color = if (isCurrentTeamActive) accentColor else if (matchResult.contains("Won")) Color(0xFF10B981) else Color(0xFFEF4444),
                                                                modifier = Modifier.padding(horizontal = 8.dp)
                                                            )
                                                            if (!isCurrentTeamActive) {
                                                                Spacer(modifier = Modifier.height(2.dp))
                                                                Text(
                                                                    text = lastMatchStage,
                                                                    fontWeight = FontWeight.Bold,
                                                                    fontSize = 8.sp,
                                                                    color = textColor.copy(alpha = 0.4f)
                                                                )
                                                            }
                                                        }

                                                        // Team 2 (Opponent)
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
                                                            Text(text = if (isCurrentTeamActive) displayNextMatch.date else "August 10, 2027", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = textColor.copy(alpha = 0.7f))
                                                        }
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            Icon(Icons.Default.AccessTime, contentDescription = null, tint = accentColor, modifier = Modifier.size(13.dp))
                                                            Spacer(modifier = Modifier.width(4.dp))
                                                            Text(text = if (isCurrentTeamActive) displayNextMatch.time else "18:00 Local", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = textColor.copy(alpha = 0.7f))
                                                        }
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = accentColor, modifier = Modifier.size(13.dp))
                                                            Spacer(modifier = Modifier.width(4.dp))
                                                            Text(
                                                                text = if (isCurrentTeamActive) {
                                                                    displayNextMatch.stadium.name.split(" ").firstOrNull() ?: displayNextMatch.stadium.name
                                                                } else {
                                                                    "Maracanã Stadium".split(" ").firstOrNull() ?: "Maracanã"
                                                                },
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
                                                                val defaultEmoji = when (displayNextMatch.stadium.weatherCondition) {
                                                                    "Sunny & Clear" -> "☀️"
                                                                    "Partly Cloudy" -> "⛅"
                                                                    "Humid & Showers" -> "🌧️"
                                                                    else -> "⛅"
                                                                }
                                                                val displayEmoji = realTimeWeather?.emoji ?: defaultEmoji
                                                                val displayTemp = realTimeWeather?.tempFahrenheit ?: displayNextMatch.stadium.weatherTemp
                                                                val displayCondition = realTimeWeather?.condition ?: displayNextMatch.stadium.weatherCondition

                                                                Text(text = displayEmoji, fontSize = 16.sp)
                                                                Spacer(modifier = Modifier.width(6.dp))
                                                                Column {
                                                                    Text(text = displayTemp, fontWeight = FontWeight.Black, fontSize = 10.sp, color = textColor)
                                                                    Text(text = displayCondition, fontSize = 7.sp, color = textColor.copy(alpha = 0.5f), fontWeight = FontWeight.Bold)
                                                                }
                                                            }

                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                Text(text = "💧", fontSize = 11.sp)
                                                                Spacer(modifier = Modifier.width(4.dp))
                                                                Column {
                                                                    Text(text = realTimeWeather?.humidity ?: "18%", fontWeight = FontWeight.Black, fontSize = 10.sp, color = textColor)
                                                                    Text(text = "Humidity", fontSize = 7.sp, color = textColor.copy(alpha = 0.5f))
                                                                }
                                                            }

                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                Text(text = "💨", fontSize = 11.sp)
                                                                Spacer(modifier = Modifier.width(4.dp))
                                                                Column {
                                                                    Text(text = realTimeWeather?.windSpeed ?: "14 km/h", fontWeight = FontWeight.Black, fontSize = 10.sp, color = textColor)
                                                                    Text(text = "Wind", fontSize = 7.sp, color = textColor.copy(alpha = 0.5f))
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            
                                            Spacer(modifier = Modifier.height(10.dp))
                                            MatchCountdownTimer(
                                                dateStr = if (isCurrentTeamActive) displayNextMatch.date else "August 10, 2027",
                                                timeStr = if (isCurrentTeamActive) displayNextMatch.time else "20:00 Local",
                                                textColor = textColor,
                                                accentColor = accentColor,
                                                currentTheme = currentTheme
                                            )
                                        }

                                        // B. TEAM OVERVIEW Grid Section
                                        item {
                                            Row(
                                                modifier = Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = localize("TEAM OVERVIEW", currentLanguage), fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFF0D9488))
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
                                                                text = localize(statItem.label, currentLanguage),
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
                                                Text(text = localize("STADIUM", currentLanguage), fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFF0D9488))
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
                                                            text = displayNextMatch.stadium.name,
                                                            fontWeight = FontWeight.ExtraBold,
                                                            fontSize = 13.sp,
                                                            color = textColor
                                                        )
                                                        Text(
                                                            text = displayNextMatch.stadium.city,
                                                            fontSize = 10.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = textColor.copy(alpha = 0.5f)
                                                        )
                                                        Text(
                                                            text = "${localize("Capacity", currentLanguage)}: ${displayNextMatch.stadium.capacity}",
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
                                                            Text(text = localize("VIEW ON MAP", currentLanguage), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
                                                Text(text = localize("HEAD COACH", currentLanguage), fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFF0D9488))
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
                                                Text(text = localize("KEY PLAYERS", currentLanguage), fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFF0D9488))
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
                                                                AsyncImage(
                                                                    model = com.example.util.PlayerPhotoHelper.getPlayerPhoto(player.name, team.abbreviation),
                                                                    contentDescription = player.name,
                                                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                                                    modifier = Modifier.fillMaxSize().clip(CircleShape)
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
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth().padding(10.dp),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Column(modifier = Modifier.weight(1f)) {
                                                            Text(text = "UPCOMING MATCH", fontSize = 8.5.sp, fontWeight = FontWeight.Black, color = accentColor)
                                                            Spacer(modifier = Modifier.height(3.dp))
                                                            Text(text = "vs ${displayNextMatch.opponent}", fontWeight = FontWeight.Black, fontSize = 13.sp, color = textColor)
                                                            Text(text = "🏟️ ${displayNextMatch.stadium.name} (${displayNextMatch.stadium.city})", fontSize = 10.sp, color = textColor.copy(alpha = 0.6f))
                                                            Text(text = "📅 ${displayNextMatch.date} • ⏰ ${displayNextMatch.time}", fontSize = 9.sp, color = textColor.copy(alpha = 0.5f), fontWeight = FontWeight.Bold)
                                                        }
                                                        
                                                        IconButton(
                                                            onClick = {
                                                                ensureNotificationPermission {
                                                                    coroutineScope.launch {
                                                                        val matchId = "${team.abbreviation}-${displayNextMatch.opponent}"
                                                                        val isNotifScheduled = matchNotifications.any { it.matchId == matchId }
                                                                        if (isNotifScheduled) {
                                                                            notificationsDao.deleteNotification(matchId)
                                                                            com.example.service.MatchNotificationManager.cancelMatchAlarm(context, matchId)
                                                                            android.widget.Toast.makeText(context, "Reminder cancelled for match vs ${displayNextMatch.opponent}", android.widget.Toast.LENGTH_SHORT).show()
                                                                        } else {
                                                                            val epochMillis = com.example.service.MatchNotificationManager.parseMatchTimeToEpoch(
                                                                                displayNextMatch.date,
                                                                                displayNextMatch.time
                                                                            )
                                                                            if (epochMillis > 0L) {
                                                                                notificationsDao.insertNotification(
                                                                                    com.example.data.MatchNotification(
                                                                                        matchId = matchId,
                                                                                        teamAbbreviation = team.abbreviation,
                                                                                        opponent = displayNextMatch.opponent,
                                                                                        dateStr = displayNextMatch.date,
                                                                                        timeStr = displayNextMatch.time,
                                                                                        kickoffEpoch = epochMillis,
                                                                                        isNotificationEnabled = true
                                                                                    )
                                                                                )
                                                                                com.example.service.MatchNotificationManager.scheduleMatchAlarm(
                                                                                    context = context,
                                                                                    matchId = matchId,
                                                                                    teamName = team.name,
                                                                                    flag = team.flag,
                                                                                    opponent = displayNextMatch.opponent,
                                                                                    stadiumName = displayNextMatch.stadium.name,
                                                                                    timeStr = displayNextMatch.time,
                                                                                    epochMillis = epochMillis
                                                                                )
                                                                                android.widget.Toast.makeText(context, "Reminder scheduled 30 min before kickoff!", android.widget.Toast.LENGTH_SHORT).show()
                                                                            } else {
                                                                                android.widget.Toast.makeText(context, "Could not parse kickoff time", android.widget.Toast.LENGTH_SHORT).show()
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            },
                                                            modifier = Modifier.size(40.dp)
                                                        ) {
                                                            val matchId = "${team.abbreviation}-${displayNextMatch.opponent}"
                                                            val isNotifScheduled = matchNotifications.any { it.matchId == matchId }
                                                            Icon(
                                                                imageVector = if (isNotifScheduled) Icons.Default.NotificationsActive else Icons.Default.NotificationsNone,
                                                                contentDescription = "Match Reminder",
                                                                tint = if (isNotifScheduled) accentColor else textColor.copy(alpha = 0.4f),
                                                                modifier = Modifier.size(22.dp)
                                                            )
                                                        }
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

            // WOMEN'S HOST CITIES & STADIUMS SLIDE-UP BOTTOM SHEET
            AnimatedVisibility(
                visible = isStadiumsSheetOpen,
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Bottom),
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 16.dp,
                            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                            clip = false,
                            ambientColor = Color(0xFF10B981).copy(alpha = 0.4f),
                            spotColor = Color.Black
                        )
                        .border(
                            BorderStroke(
                                1.2.dp,
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF10B981).copy(alpha = 0.5f),
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Title Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "🏟️",
                                    fontSize = 24.sp,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Column {
                                    Text(
                                        text = "Women's Sports Global & USA Stadiums",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 17.sp,
                                        color = textColor,
                                        letterSpacing = (-0.5).sp
                                    )
                                    Text(
                                        text = "15 premier venues across USA (WNBA, NWSL, PWHL) & Globally (FIFA, Grand Slam)",
                                        fontSize = 11.sp,
                                        color = textColor.copy(alpha = 0.6f)
                                    )
                                }
                            }
                            IconButton(
                                onClick = { isStadiumsSheetOpen = false },
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(textColor.copy(alpha = 0.08f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close Sheet",
                                    tint = textColor,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        // Scope filters (USA vs International)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(
                                    "ALL" to "🌟 All Venues (15)",
                                    "USA_NATIONAL" to "🇺🇸 USA National",
                                    "INTERNATIONAL" to "🌍 International"
                                ).forEach { (key, label) ->
                                    val isCur = stadiumScopeFilter == key
                                    FilterChip(
                                        selected = isCur,
                                        onClick = { stadiumScopeFilter = key },
                                        label = {
                                            Text(
                                                text = label,
                                                fontSize = 11.sp,
                                                fontWeight = if (isCur) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = Color(0xFF10B981).copy(alpha = 0.2f),
                                            selectedLabelColor = Color(0xFF10B981)
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            // Sport category filters
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(WomensSportCategory.values()) { cat ->
                                    val isCur = selectedSportCategory == cat
                                    FilterChip(
                                        selected = isCur,
                                        onClick = {
                                            selectedSportCategory = cat
                                            stadiumSportFilter = if (cat == WomensSportCategory.ALL) "ALL" else cat.displayName
                                        },
                                        label = {
                                            Text(
                                                text = "${cat.emoji} ${cat.displayName}",
                                                fontSize = 11.sp,
                                                fontWeight = if (isCur) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = Color(0xFF38BDF8).copy(alpha = 0.2f),
                                            selectedLabelColor = Color(0xFF38BDF8)
                                        )
                                    )
                                }
                            }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Horizontal list of stadiums
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 2.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            val activeStadiumsList = HostStadiumDataProvider.womensHostStadiums.filter { s ->
                                val matchesScope = when (stadiumScopeFilter) {
                                    "USA_NATIONAL" -> s.scope == "USA National"
                                    "INTERNATIONAL" -> s.scope == "International"
                                    else -> true
                                }
                                val matchesSport = when (selectedSportCategory) {
                                    WomensSportCategory.ALL -> true
                                    WomensSportCategory.BASKETBALL -> s.sport.equals("Basketball", ignoreCase = true)
                                    WomensSportCategory.SOCCER -> s.sport.equals("Soccer", ignoreCase = true)
                                    WomensSportCategory.TENNIS -> s.sport.equals("Tennis", ignoreCase = true)
                                    WomensSportCategory.ICE_HOCKEY -> s.sport.equals("Ice Hockey", ignoreCase = true)
                                    WomensSportCategory.VOLLEYBALL -> s.sport.equals("Volleyball", ignoreCase = true)
                                    WomensSportCategory.CRICKET -> s.sport.equals("Cricket", ignoreCase = true)
                                }
                                matchesScope && matchesSport
                            }
                            items(activeStadiumsList) { stadium ->
                                val isSelected = selectedStadiumId == stadium.id
                                val stadWeather = allStadiumsWeatherMap[stadium.id]
                                Card(
                                    modifier = Modifier
                                        .width(260.dp)
                                        .clickable {
                                            selectedStadiumId = stadium.id
                                        }
                                        .border(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) Color(0xFF10B981) else textColor.copy(alpha = 0.12f),
                                            shape = RoundedCornerShape(16.dp)
                                        ),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (currentTheme == GlobeTheme.COSMIC_DARK) {
                                            if (isSelected) Color(0xFF1E293B) else Color(0xFF1E293B).copy(alpha = 0.5f)
                                        } else {
                                            if (isSelected) Color(0xFFF1F5F9) else Color(0xFFF1F5F9).copy(alpha = 0.5f)
                                        }
                                    )
                                ) {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        // Stadium Photo
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(110.dp)
                                                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                                        ) {
                                            AsyncImage(
                                                model = stadium.image,
                                                contentDescription = stadium.name,
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                            // Real-Time Weather Overlay Badge
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.TopStart)
                                                    .padding(8.dp)
                                                    .background(Color(0xFF0F172A).copy(alpha = 0.88f), RoundedCornerShape(8.dp))
                                                    .border(0.5.dp, Color(0xFF38BDF8).copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                                    .padding(horizontal = 6.dp, vertical = 3.dp)
                                            ) {
                                                Text(
                                                    text = if (stadWeather != null) "${stadWeather.emoji} ${stadWeather.tempFahrenheit} · ${stadWeather.condition}" else "⛅ Fetching weather...",
                                                    color = Color(0xFF38BDF8),
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            // Capacity Badge
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.BottomEnd)
                                                    .padding(8.dp)
                                                    .background(Color.Black.copy(alpha = 0.75f), RoundedCornerShape(6.dp))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = "🎫 ${stadium.capacity}",
                                                    color = Color.White,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }

                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp)
                                        ) {
                                            Text(
                                                text = stadium.name,
                                                fontWeight = FontWeight.Black,
                                                fontSize = 14.sp,
                                                color = textColor,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = "📍 ${stadium.city}, ${stadium.country}",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color(0xFF10B981)
                                            )
                                            if (isWomensWorldCup && stadium.league.isNotEmpty()) {
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Row(
                                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Surface(
                                                        color = Color(0xFFF59E0B).copy(alpha = 0.18f),
                                                        shape = RoundedCornerShape(4.dp)
                                                    ) {
                                                        Text(
                                                            text = "🏆 ${stadium.league}",
                                                            color = Color(0xFFF59E0B),
                                                            fontSize = 9.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                        )
                                                    }
                                                    Surface(
                                                        color = Color(0xFF38BDF8).copy(alpha = 0.18f),
                                                        shape = RoundedCornerShape(4.dp)
                                                    ) {
                                                        Text(
                                                            text = stadium.sport,
                                                            color = Color(0xFF38BDF8),
                                                            fontSize = 9.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                        )
                                                    }
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = stadium.fact,
                                                fontSize = 10.sp,
                                                color = textColor.copy(alpha = 0.7f),
                                                lineHeight = 14.sp,
                                                minLines = 2,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )

                                            Spacer(modifier = Modifier.height(6.dp))
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(
                                                        if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF0F172A) else Color(0xFFE2E8F0),
                                                        RoundedCornerShape(8.dp)
                                                    )
                                                    .padding(horizontal = 8.dp, vertical = 5.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                    Text(text = stadWeather?.emoji ?: "⛅", fontSize = 11.sp)
                                                    Text(
                                                        text = if (stadWeather != null) "${stadWeather.tempFahrenheit} · ${stadWeather.condition}" else "Live Weather",
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF38BDF8) else Color(0xFF0284C7)
                                                    )
                                                }
                                                Text(
                                                    text = if (stadWeather != null) "💨 ${stadWeather.windSpeed}" else "Real-time",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = textColor.copy(alpha = 0.6f)
                                                )
                                            }
                                            
                                            if (stadium.id == "STAD_NEW_YORK") {
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .background(Color(0xFF8B5CF6).copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                                                        .border(1.dp, Color(0xFF8B5CF6).copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                                                        .padding(horizontal = 8.dp, vertical = 6.dp)
                                                ) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Text(text = "🎵", fontSize = 14.sp)
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Column {
                                                            Text(
                                                                text = "WOMEN'S SPORTS CHAMPIONSHIP HALFTIME SHOW",
                                                                fontSize = 8.5.sp,
                                                                fontWeight = FontWeight.Black,
                                                                color = Color(0xFF8B5CF6)
                                                            )
                                                            Text(
                                                                text = "Featuring legendary superstars, synchronized drone lights & spatial projection mapping!",
                                                                fontSize = 7.5.sp,
                                                                lineHeight = 10.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = textColor.copy(alpha = 0.8f)
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                            
                                            Spacer(modifier = Modifier.height(10.dp))
                                            
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                // Gallery Button
                                                Button(
                                                    onClick = {
                                                        galleryStadium = stadium
                                                    },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = Color(0xFF10B981),
                                                        contentColor = Color.White
                                                    ),
                                                    shape = RoundedCornerShape(10.dp),
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(34.dp),
                                                    contentPadding = PaddingValues(0.dp)
                                                ) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(
                                                            imageVector = Icons.Default.Collections,
                                                            contentDescription = "Gallery",
                                                            modifier = Modifier.size(12.dp)
                                                        )
                                                        Spacer(modifier = Modifier.width(3.dp))
                                                        Text("GALLERY", fontSize = 9.5.sp, fontWeight = FontWeight.Black)
                                                    }
                                                }

                                                // AR Camera Overlay Button
                                                Button(
                                                    onClick = {
                                                        arCameraStadium = stadium
                                                    },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = Color(0xFF38BDF8),
                                                        contentColor = Color.Black
                                                    ),
                                                    shape = RoundedCornerShape(10.dp),
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(34.dp),
                                                    contentPadding = PaddingValues(0.dp)
                                                ) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(
                                                            imageVector = Icons.Default.ViewInAr,
                                                            contentDescription = "AR Overlay",
                                                            modifier = Modifier.size(12.dp)
                                                        )
                                                        Spacer(modifier = Modifier.width(3.dp))
                                                        Text("AR VIEW", fontSize = 9.5.sp, fontWeight = FontWeight.Black)
                                                    }
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(6.dp))

                                            // Focus on 3D Globe Button
                                            Button(
                                                onClick = {
                                                    selectedStadiumId = stadium.id
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = if (isSelected) Color(0xFF10B981).copy(alpha = 0.2f) else textColor.copy(alpha = 0.08f),
                                                    contentColor = if (isSelected) Color(0xFF10B981) else textColor
                                                ),
                                                shape = RoundedCornerShape(10.dp),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(30.dp),
                                                contentPadding = PaddingValues(0.dp)
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.Center
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Explore,
                                                        contentDescription = "Fly to Stadium",
                                                        modifier = Modifier.size(12.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = if (isSelected) "SELECTED ON GLOBE" else "FLY TO STADIUM",
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold
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
        }

        // FLOATING VERTICAL UTILITY BUTTONS ON THE RIGHT-HAND SIDE (4 floating circular buttons without outer ellipse container)
        if (!isCompareDrawerOpen) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(y = 80.dp)
                    .padding(end = 12.dp)
                    .testTag("floating_right_utility_dock"),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. Playbook Button
                IconButton(
                    onClick = { isPlaybookOpen = true },
                    modifier = Modifier
                        .size(38.dp)
                        .shadow(elevation = 4.dp, shape = CircleShape)
                        .background(
                            brush = if (currentTheme == GlobeTheme.GLASS_LIGHT) {
                                Brush.verticalGradient(listOf(Color.White, Color(0xFFE2E8F0)))
                            } else {
                                Brush.verticalGradient(listOf(Color(0xFF334155), Color(0xFF1E293B)))
                            },
                            shape = CircleShape
                        )
                        .border(
                            width = 1.dp,
                            color = Color(0xFF60A5FA).copy(alpha = 0.5f),
                            shape = CircleShape
                        )
                        .testTag("user_playbook_toggle_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = "User Playbook & Guide",
                        tint = if (currentTheme == GlobeTheme.GLASS_LIGHT) Color(0xFF2563EB) else Color(0xFF60A5FA),
                        modifier = Modifier.size(19.dp)
                    )
                }

                // 2. Major Competitions Selector Button (FIFA, Olympics, National Leagues)
                IconButton(
                    onClick = { isCompetitionsDialogOpen = true },
                    modifier = Modifier
                        .size(38.dp)
                        .shadow(elevation = 4.dp, shape = CircleShape)
                        .background(
                            brush = if (currentTheme == GlobeTheme.GLASS_LIGHT) {
                                Brush.verticalGradient(listOf(Color.White, Color(0xFFE2E8F0)))
                            } else {
                                Brush.verticalGradient(listOf(Color(0xFF334155), Color(0xFF1E293B)))
                            },
                            shape = CircleShape
                        )
                        .border(
                            width = 1.dp,
                            color = Color(0xFF76FF03).copy(alpha = 0.7f),
                            shape = CircleShape
                        )
                        .testTag("competitions_dialog_trigger_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Major Competitions",
                        tint = Color(0xFF76FF03),
                        modifier = Modifier.size(19.dp)
                    )
                }

                // 3. Women's Sports Match Schedules (Whistle / Sports icon)
                IconButton(
                    onClick = {
                        isGamesSheetOpen = !isGamesSheetOpen
                        if (isGamesSheetOpen) {
                            selectedTeam = null
                        }
                    },
                    modifier = Modifier
                        .size(38.dp)
                        .shadow(elevation = 4.dp, shape = CircleShape)
                        .background(
                            brush = if (currentTheme == GlobeTheme.GLASS_LIGHT) {
                                Brush.verticalGradient(listOf(Color.White, Color(0xFFE2E8F0)))
                            } else {
                                Brush.verticalGradient(listOf(Color(0xFF334155), Color(0xFF1E293B)))
                            },
                            shape = CircleShape
                        )
                        .border(
                            width = 1.dp,
                            color = Color(0xFFFBBF24).copy(alpha = 0.5f),
                            shape = CircleShape
                        )
                        .testTag("womens_games_sheet_toggle_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Sports,
                        contentDescription = "Women's Sports Games",
                        tint = if (currentTheme == GlobeTheme.GLASS_LIGHT) Color(0xFFF59E0B) else Color(0xFFFBBF24),
                        modifier = Modifier.size(19.dp)
                    )
                }

                // 4. Language Selector ("EN" pill)
                Box(modifier = Modifier.wrapContentSize()) {
                    IconButton(
                        onClick = { isLanguageMenuExpanded = true },
                        modifier = Modifier
                            .size(38.dp)
                            .shadow(elevation = 4.dp, shape = CircleShape)
                            .background(
                                brush = if (currentTheme == GlobeTheme.GLASS_LIGHT) {
                                    Brush.verticalGradient(listOf(Color.White, Color(0xFFE2E8F0)))
                                } else {
                                    Brush.verticalGradient(listOf(Color(0xFF334155), Color(0xFF1E293B)))
                                },
                                shape = CircleShape
                            )
                            .border(
                                width = 1.dp,
                                color = Color(0xFF38BDF8).copy(alpha = 0.5f),
                                shape = CircleShape
                            )
                            .testTag("language_switch_button")
                    ) {
                        Text(
                            text = currentLanguage.code,
                            color = if (currentTheme == GlobeTheme.GLASS_LIGHT) Color(0xFF0D9488) else Color(0xFF38BDF8),
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp
                        )
                    }

                    DropdownMenu(
                        expanded = isLanguageMenuExpanded,
                        onDismissRequest = { isLanguageMenuExpanded = false },
                        modifier = Modifier.background(
                            if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF1E293B) else Color.White
                        )
                    ) {
                        AppLanguage.entries.forEach { lang ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = lang.displayName,
                                        fontWeight = if (currentLanguage == lang) FontWeight.Black else FontWeight.Medium,
                                        color = if (currentLanguage == lang) accentColor else textColor
                                    )
                                },
                                onClick = {
                                    currentLanguage = lang
                                    isLanguageMenuExpanded = false
                                }
                            )
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

        // ARCHITECTURAL GALLERY DIALOG
        galleryStadium?.let { stadium ->
            StadiumGalleryDialog(
                stadium = stadium,
                onDismiss = { galleryStadium = null },
                onOpenARCamera = {
                    arCameraStadium = stadium
                    galleryStadium = null
                }
            )
        }

        // VENUE TEAM SIDE-PANEL: ROSTER, PLAYER STATS, HISTORICAL PERFORMANCE & REMINDERS
        AnimatedVisibility(
            visible = isSidePanelOpen && sidePanelStadium != null,
            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            sidePanelStadium?.let { stadium ->
                StadiumTeamSidePanel(
                    stadium = stadium,
                    currentTheme = currentTheme,
                    onClose = { isSidePanelOpen = false },
                    onCompareTeam = { teamKey ->
                        val matched = teams.find {
                            it.abbreviation.equals(teamKey, ignoreCase = true) ||
                            it.name.contains(teamKey, ignoreCase = true)
                        } ?: teams.firstOrNull()
                        matched?.let { t ->
                            compareTeam1 = t
                            compareTeam2 = teams.find { it.abbreviation != t.abbreviation } ?: teams.lastOrNull()
                            isSidePanelOpen = false
                            isCompareDrawerOpen = true
                        }
                    },
                    onRemindMatch = { title, dateStr, timeStr ->
                        activeReminders = activeReminders + stadium.id
                        com.example.service.MatchNotificationManager.scheduleStadiumReminder(
                            context = context,
                            stadiumId = stadium.id,
                            matchTitle = title,
                            stadiumName = stadium.name,
                            timeStr = timeStr
                        )
                        android.widget.Toast.makeText(
                            context,
                            "🔔 Reminder set for ${stadium.name} ($timeStr)",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    },
                    isReminderActive = activeReminders.contains(stadium.id)
                )
            }
        }

        // AR CAMERA SEATING & PITCH OVERLAY
        arCameraStadium?.let { stadium ->
            StadiumARCameraOverlay(
                stadium = stadium,
                onClose = { arCameraStadium = null }
            )
        }

        // RECHARTS ANALYTICS DIALOG
        if (isMetricsSheetOpen) {
            RechartsAnalyticsDialog(
                team = selectedTeamForMetrics,
                allTeams = teams,
                onDismiss = { isMetricsSheetOpen = false },
                onSelectTeam = { team ->
                    selectedTeamForMetrics = team
                }
            )
        }

        // USER PLAYBOOK & GUIDE DIALOG OVERLAY
        UserPlaybookDialog(
            isOpen = isPlaybookOpen,
            onDismiss = { isPlaybookOpen = false },
            currentLanguage = currentLanguage,
            onLanguageChange = { currentLanguage = it },
            theme = currentTheme
        )

        // WOMEN'S SPORTS GAMES LOOK-UP BOTTOM SHEET
        WomensGamesLookupSheet(
            isOpen = isGamesSheetOpen,
            onDismiss = { isGamesSheetOpen = false },
            onFlyToStadium = { stadId ->
                selectedStadiumId = stadId
                isGamesSheetOpen = false
            },
            currentTheme = currentTheme,
            initialCategory = selectedSportCategory
        )

        // MAJOR WOMEN'S COMPETITIONS DIALOG (FIFA, Olympics, National Leagues in North America & LATAM)
        WomensCompetitionsDialog(
            isOpen = isCompetitionsDialogOpen,
            onDismiss = { isCompetitionsDialogOpen = false },
            onNavigateToLocation = { lat, lon, label, sportCategory ->
                hudRotationTarget = Pair(lat, lon)
                selectedSportCategory = when {
                    sportCategory.contains("soccer", ignoreCase = true) || sportCategory.contains("football", ignoreCase = true) -> WomensSportCategory.SOCCER
                    sportCategory.contains("basketball", ignoreCase = true) -> WomensSportCategory.BASKETBALL
                    sportCategory.contains("tennis", ignoreCase = true) -> WomensSportCategory.TENNIS
                    sportCategory.contains("ice hockey", ignoreCase = true) || sportCategory.contains("hockey", ignoreCase = true) -> WomensSportCategory.ICE_HOCKEY
                    sportCategory.contains("volleyball", ignoreCase = true) -> WomensSportCategory.VOLLEYBALL
                    sportCategory.contains("cricket", ignoreCase = true) -> WomensSportCategory.CRICKET
                    else -> WomensSportCategory.ALL
                }
                android.widget.Toast.makeText(
                    context,
                    "🌐 Navigating to $label",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        )
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
    stageLabel: String,
    zoomScale: Float,
    realTimeTeams: List<String>? = null,
    activeTeams: List<Team>,
    selectedStadiumId: String? = null,
    onStadiumSelected: ((String) -> Unit)? = null,
    isWomensWorldCup: Boolean = false,
    highlightedStadiumId: String? = null,
    onTeamBadgeClicked: ((Team) -> Unit)? = null,
    targetRotationTarget: Pair<Double, Double>? = null,
    onRemindMatch: ((String, String, String, String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var webViewRef by remember { mutableStateOf<WebView?>(null) }

    // Sync HUD direct rotation with 3D Globe camera
    LaunchedEffect(targetRotationTarget) {
        targetRotationTarget?.let { (lat, lon) ->
            webViewRef?.evaluateJavascript("javascript:if(window.rotateGlobeTo){window.rotateGlobeTo($lat, $lon, 2.4);}", null)
        }
    }

    // Sync selected stadium highlight with WebView
    LaunchedEffect(highlightedStadiumId) {
        if (highlightedStadiumId != null) {
            webViewRef?.evaluateJavascript("javascript:highlightStadiumFromAndroid('$highlightedStadiumId')", null)
        } else {
            webViewRef?.evaluateJavascript("javascript:highlightStadiumFromAndroid(null)", null)
        }
    }

    // Sync tournament type & stadiums with WebView
    LaunchedEffect(isWomensWorldCup) {
        webViewRef?.evaluateJavascript("javascript:setTournamentFromAndroid($isWomensWorldCup)", null)
        val stads = if (isWomensWorldCup) HostStadiumDataProvider.womensHostStadiums else HostStadiumDataProvider.hostStadiums
        val stadsArray = org.json.JSONArray().apply {
            stads.forEach { s ->
                put(org.json.JSONObject().apply {
                    put("id", s.id)
                    put("name", s.name)
                    put("city", s.city)
                    put("country", s.country)
                    put("capacity", s.capacity)
                    put("latitude", s.latitude)
                    put("longitude", s.longitude)
                    put("fact", s.fact)
                })
            }
        }
        val stadsJson = stadsArray.toString()
        webViewRef?.evaluateJavascript("javascript:if(window.setStadiumsFromAndroid){window.setStadiumsFromAndroid('$stadsJson');}", null)
    }

    // Sync selected stage with WebView
    LaunchedEffect(stageLabel) {
        webViewRef?.evaluateJavascript("javascript:setStageFromAndroid('$stageLabel')", null)
    }

    // Sync active teams list with WebView
    LaunchedEffect(activeTeams) {
        val simpleTeamsArray = org.json.JSONArray().apply {
            activeTeams.forEach { team ->
                put(org.json.JSONObject().apply {
                    put("name", team.name)
                    put("abbreviation", team.abbreviation)
                    put("flag", team.flag)
                    put("lat", team.latitude)
                    put("lon", team.longitude)
                })
            }
        }
        val simpleTeamsJson = simpleTeamsArray.toString()
        webViewRef?.evaluateJavascript("javascript:setTeamsFromAndroid('$simpleTeamsJson')", null)
        webViewRef?.evaluateJavascript("javascript:setStageFromAndroid('$stageLabel')", null)
    }

    // Sync real-time teams list with WebView when stage or real-time list changes
    LaunchedEffect(stageLabel, realTimeTeams) {
        val jsonArray = if (stageLabel == "All 48" || stageLabel == "Prequalified Teams") {
            org.json.JSONArray(activeTeams.map { it.abbreviation }).toString()
        } else if (realTimeTeams == null) {
            "null"
        } else {
            org.json.JSONArray(realTimeTeams).toString()
        }

        webViewRef?.evaluateJavascript(
            "javascript:updateActiveTeamsFromAndroid('$stageLabel', '$jsonArray')",
            null
        )
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

    // Sync stadium selection with WebView
    LaunchedEffect(selectedStadiumId) {
        selectedStadiumId?.let {
            webViewRef?.evaluateJavascript("javascript:selectStadium('$it')", null)
        }
    }

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    databaseEnabled = true
                    allowFileAccess = true
                    allowContentAccess = true
                    cacheMode = WebSettings.LOAD_DEFAULT
                }
                // Set opaque background matching dark cosmic slate theme (prevents Mesa DRI rendernode errors)
                setBackgroundColor(0xFF0F172A.toInt())
                
                // Set up the bridge interface
                addJavascriptInterface(object {
                    @JavascriptInterface
                    fun onCountryClick(abbreviation: String) {
                        // Crucial: run on main thread!
                        post {
                            val matched = activeTeams.find { it.abbreviation == abbreviation }
                            matched?.let { onTeamSelected(it) }
                        }
                    }

                    @JavascriptInterface
                    fun onStadiumClick(stadiumId: String) {
                        post {
                            onStadiumSelected?.invoke(stadiumId)
                        }
                    }

                    @JavascriptInterface
                    fun onTeamBadgeClick(abbreviation: String) {
                        post {
                            val matched = activeTeams.find { it.abbreviation == abbreviation }
                            matched?.let { onTeamBadgeClicked?.invoke(it) }
                        }
                    }

                    @JavascriptInterface
                    fun onRemindMatch(stadiumId: String, matchTitle: String, dateStr: String, timeStr: String) {
                        post {
                            onRemindMatch?.invoke(stadiumId, matchTitle, dateStr, timeStr)
                        }
                    }
                }, "Android")

                webViewClient = object : WebViewClient() {
                    override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean {
                        // Handle gracefully if WebView rendering process exits, avoiding application crashes
                        return true
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        // Initialize states once page loads
                        val themeStr = if (theme == GlobeTheme.GLASS_LIGHT) "light" else "dark"
                        view?.evaluateJavascript("javascript:setThemeFromAndroid('$themeStr')", null)
                        view?.evaluateJavascript("javascript:setTournamentFromAndroid($isWomensWorldCup)", null)
                        
                        val stads = if (isWomensWorldCup) HostStadiumDataProvider.womensHostStadiums else HostStadiumDataProvider.hostStadiums
                        val stadsArray = org.json.JSONArray().apply {
                            stads.forEach { s ->
                                put(org.json.JSONObject().apply {
                                    put("id", s.id)
                                    put("name", s.name)
                                    put("city", s.city)
                                    put("country", s.country)
                                    put("capacity", s.capacity)
                                    put("latitude", s.latitude)
                                    put("longitude", s.longitude)
                                    put("fact", s.fact)
                                })
                            }
                        }
                        val stadsJson = stadsArray.toString()
                        view?.evaluateJavascript("javascript:if(window.setStadiumsFromAndroid){window.setStadiumsFromAndroid('$stadsJson');}", null)
                        
                        val simpleTeamsArray = org.json.JSONArray().apply {
                            activeTeams.forEach { team ->
                                put(org.json.JSONObject().apply {
                                    put("name", team.name)
                                    put("abbreviation", team.abbreviation)
                                    put("flag", team.flag)
                                    put("lat", team.latitude)
                                    put("lon", team.longitude)
                                })
                            }
                        }
                        val simpleTeamsJson = simpleTeamsArray.toString()
                        view?.evaluateJavascript("javascript:setTeamsFromAndroid('$simpleTeamsJson')", null)
                        view?.evaluateJavascript("javascript:setStageFromAndroid('$stageLabel')", null)

                        val initialTeamsJson = if (stageLabel == "All 48" || stageLabel == "Prequalified Teams") {
                            org.json.JSONArray(activeTeams.map { it.abbreviation }).toString()
                        } else if (realTimeTeams == null) {
                            "null"
                        } else {
                            org.json.JSONArray(realTimeTeams).toString()
                        }
                        view?.evaluateJavascript(
                            "javascript:updateActiveTeamsFromAndroid('$stageLabel', '$initialTeamsJson')",
                            null
                        )

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

@Composable
fun MainSportsSwitcherBar(
    selectedCategory: WomensSportCategory,
    onCategorySelected: (WomensSportCategory) -> Unit,
    onOpenGamesLookup: () -> Unit,
    onFlyToSportStadium: (WomensSportCategory) -> Unit,
    currentTheme: GlobeTheme,
    modifier: Modifier = Modifier
) {
    val isDark = currentTheme != GlobeTheme.GLASS_LIGHT
    val surfaceColor = if (isDark) Color(0xFF0F172A).copy(alpha = 0.94f) else Color.White.copy(alpha = 0.96f)
    val textPrimary = if (isDark) Color.White else Color(0xFF0F172A)
    val textSecondary = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 2.dp)
    ) {
        // Horizontal Glassmorphic Sport Switcher Bar
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = surfaceColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            border = BorderStroke(
                width = 1.dp,
                color = if (isDark) Color.White.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.08f)
            )
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                // Header row with switcher title & quick action button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(0xFF38BDF8), CircleShape)
                        )
                        Text(
                            text = "SWITCH SPORT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            color = if (isDark) Color(0xFF38BDF8) else Color(0xFF0284C7)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onOpenGamesLookup() }
                            .background(
                                if (isDark) Color(0xFF1E293B) else Color(0xFFE0F2FE)
                            )
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Games",
                            tint = if (isDark) Color(0xFF38BDF8) else Color(0xFF0284C7),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "All Games & Events",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) Color(0xFF38BDF8) else Color(0xFF0284C7)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Scrollable Sport Category Pills
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 2.dp)
                ) {
                    items(WomensSportCategory.values()) { cat ->
                        val isSelected = selectedCategory == cat
                        val matchingGames = WomensSportsDataProvider.sampleGames.filter { g ->
                            cat == WomensSportCategory.ALL || g.sport == cat
                        }
                        val hasLive = matchingGames.any { it.status == "LIVE" }

                        Box(
                            modifier = Modifier
                                .height(46.dp)
                                .shadow(
                                    elevation = if (isSelected) 4.dp else 0.dp,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .background(
                                    brush = if (isSelected) {
                                        Brush.horizontalGradient(
                                            listOf(Color(0xFF2563EB), Color(0xFF7C3AED))
                                        )
                                    } else {
                                        Brush.horizontalGradient(
                                            listOf(
                                                if (isDark) Color(0xFF1E293B) else Color(0xFFF1F5F9),
                                                if (isDark) Color(0xFF334155) else Color(0xFFE2E8F0)
                                            )
                                        )
                                    },
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) Color(0xFF93C5FD) else textSecondary.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    onCategorySelected(cat)
                                    onFlyToSportStadium(cat)
                                }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                .testTag("main_sport_chip_${cat.name}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = cat.emoji,
                                    fontSize = 16.sp
                                )
                                Column(verticalArrangement = Arrangement.Center) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = cat.displayName,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                            color = if (isSelected) Color.White else textPrimary
                                        )
                                        if (hasLive) {
                                            Box(
                                                modifier = Modifier
                                                    .background(Color(0xFFEF4444), RoundedCornerShape(4.dp))
                                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                                            ) {
                                                Text(
                                                    text = "LIVE",
                                                    color = Color.White,
                                                    fontSize = 7.sp,
                                                    fontWeight = FontWeight.Black
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = when (cat) {
                                            WomensSportCategory.ALL -> "15 Venues • 6 Sports"
                                            WomensSportCategory.BASKETBALL -> "WNBA • Barclays"
                                            WomensSportCategory.SOCCER -> "NWSL • FIFA • 10 Venues"
                                            WomensSportCategory.TENNIS -> "WTA • Arthur Ashe"
                                            WomensSportCategory.ICE_HOCKEY -> "PWHL • Place Bell"
                                            WomensSportCategory.VOLLEYBALL -> "LOVB • Gas South"
                                            WomensSportCategory.CRICKET -> "ICC • Melbourne MCG"
                                        },
                                        fontSize = 9.sp,
                                        color = if (isSelected) Color.White.copy(alpha = 0.85f) else textSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Active Sport Quick Context Ribbon / Floating Info
        Spacer(modifier = Modifier.height(4.dp))
        val sportInfo = remember(selectedCategory) {
            when (selectedCategory) {
                WomensSportCategory.ALL -> Triple(
                    "🌟 All Women's Sports Hub",
                    "Exploring 15 premier stadiums across USA & globe",
                    "Barclays Center"
                )
                WomensSportCategory.BASKETBALL -> Triple(
                    "🏀 WNBA Pro Basketball",
                    "Active Venue: Barclays Center (Brooklyn, NY) • NY Liberty vs Indiana Fever (LIVE)",
                    "Barclays Center"
                )
                WomensSportCategory.SOCCER -> Triple(
                    "⚽ NWSL & FIFA Women's Soccer",
                    "Active: CPKC Stadium, Audi Field, Inter&Co, Snapdragon, Red Bull Arena & more",
                    "CPKC Stadium"
                )
                WomensSportCategory.TENNIS -> Triple(
                    "🎾 WTA Tour Grand Slam Tennis",
                    "Active: Arthur Ashe Stadium (NY) & Centre Court Wimbledon (London)",
                    "Arthur Ashe Stadium"
                )
                WomensSportCategory.ICE_HOCKEY -> Triple(
                    "🏒 PWHL Women's Ice Hockey",
                    "Active: Place Bell (Montreal, QC) • Montreal Victoire vs Toronto Sceptres",
                    "Place Bell"
                )
                WomensSportCategory.VOLLEYBALL -> Triple(
                    "🏐 LOVB Pro Volleyball League",
                    "Active: Gas South Arena (Duluth, GA) • LOVB Atlanta vs LOVB Omaha",
                    "Gas South Arena"
                )
                WomensSportCategory.CRICKET -> Triple(
                    "🏏 ICC Women's International Cricket",
                    "Active: Melbourne Cricket Ground (MCG, Australia) • Australia vs India",
                    "Melbourne Cricket Ground"
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) Color(0xFF1E293B).copy(alpha = 0.9f) else Color(0xFFEFF6FF).copy(alpha = 0.95f)
            ),
            border = BorderStroke(
                1.dp,
                if (isDark) Color(0xFF38BDF8).copy(alpha = 0.35f) else Color(0xFF3B82F6).copy(alpha = 0.3f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = sportInfo.first,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isDark) Color(0xFF38BDF8) else Color(0xFF1D4ED8)
                    )
                    Text(
                        text = sportInfo.second,
                        fontSize = 9.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = textSecondary
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Fly to arena button
                    OutlinedButton(
                        onClick = { onFlyToSportStadium(selectedCategory) },
                        modifier = Modifier.height(28.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (isDark) Color(0xFF34D399) else Color(0xFF059669)
                        ),
                        border = BorderStroke(1.dp, if (isDark) Color(0xFF34D399).copy(alpha = 0.5f) else Color(0xFF059669).copy(alpha = 0.5f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = "Fly to venue",
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(text = "Fly 📍", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }

                    // Look up games button
                    Button(
                        onClick = { onOpenGamesLookup() },
                        modifier = Modifier.height(28.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2563EB)
                        )
                    ) {
                        Text(text = "Games", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun TournamentGlobeSwitcher(
    isWomensWorldCup: Boolean = true,
    onToggle: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Elegant Women's Sports Hub Brand Badge (Pure Women's Sports)
    Row(
        modifier = modifier
            .height(42.dp)
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(22.dp))
            .background(Color(0xFF0F172A), RoundedCornerShape(22.dp))
            .border(
                width = 1.5.dp,
                brush = Brush.horizontalGradient(listOf(Color(0xFF2563EB), Color(0xFF7C3AED))),
                shape = RoundedCornerShape(22.dp)
            )
            .clip(RoundedCornerShape(22.dp))
            .padding(horizontal = 10.dp, vertical = 3.dp)
            .testTag("womens_world_cup_button"),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .border(1.dp, Color(0xFF93C5FD), CircleShape)
        ) {
            Image(
                painter = painterResource(id = com.example.R.drawable.img_women2027_1783841963682),
                contentDescription = "Women's Sports Hub",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = "WOMEN'S SPORTS",
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp,
                color = Color.White
            )
            Text(
                text = "3D Hub & Venues",
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF38BDF8)
            )
        }
    }
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

@Composable
fun MatchCountdownTimer(
    dateStr: String,
    timeStr: String,
    textColor: Color,
    accentColor: Color,
    currentTheme: GlobeTheme,
    modifier: Modifier = Modifier
) {
    var timeLeftMs by remember(dateStr, timeStr) {
        val target = com.example.service.MatchNotificationManager.parseMatchTimeToEpoch(dateStr, timeStr)
        mutableStateOf(target - System.currentTimeMillis())
    }

    LaunchedEffect(dateStr, timeStr) {
        val target = com.example.service.MatchNotificationManager.parseMatchTimeToEpoch(dateStr, timeStr)
        while (true) {
            timeLeftMs = target - System.currentTimeMillis()
            kotlinx.coroutines.delay(1000L)
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF1E293B) else Color.White
        ),
        border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(if (timeLeftMs > 0) Color(0xFF10B981) else Color(0xFFEF4444), CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (timeLeftMs > 0) "KICKOFF COUNTDOWN" else "MATCH TIME STATUS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = if (timeLeftMs > 0) Color(0xFF10B981) else Color(0xFFEF4444),
                    letterSpacing = 1.sp
                )
            }
            
            Spacer(modifier = Modifier.height(10.dp))

            if (timeLeftMs > 0) {
                val totalSeconds = timeLeftMs / 1000
                val seconds = totalSeconds % 60
                val minutes = (totalSeconds / 60) % 60
                val hours = (totalSeconds / 3600) % 24
                val days = totalSeconds / 86400

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CountdownUnitBox(value = days, label = "DAYS", textColor = textColor, currentTheme = currentTheme)
                    Text(":", fontSize = 16.sp, fontWeight = FontWeight.Black, color = textColor.copy(alpha = 0.5f))
                    CountdownUnitBox(value = hours, label = "HRS", textColor = textColor, currentTheme = currentTheme)
                    Text(":", fontSize = 16.sp, fontWeight = FontWeight.Black, color = textColor.copy(alpha = 0.5f))
                    CountdownUnitBox(value = minutes, label = "MINS", textColor = textColor, currentTheme = currentTheme)
                    Text(":", fontSize = 16.sp, fontWeight = FontWeight.Black, color = textColor.copy(alpha = 0.5f))
                    CountdownUnitBox(value = seconds, label = "SECS", textColor = textColor, currentTheme = currentTheme)
                }
            } else {
                val absSeconds = kotlin.math.abs(timeLeftMs) / 1000
                val hoursPassed = absSeconds / 3600
                if (hoursPassed < 4) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFEF4444).copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFFEF4444).copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "⚡ LIVE IN PROGRESS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFEF4444)
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .background(textColor.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                            .border(1.dp, textColor.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "✔️ MATCH COMPLETED",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = textColor.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CountdownUnitBox(
    value: Long,
    label: String,
    textColor: Color,
    currentTheme: GlobeTheme
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(54.dp)
            .background(
                if (currentTheme == GlobeTheme.COSMIC_DARK) Color(0xFF0F172A) else Color(0xFFF8FAFC),
                RoundedCornerShape(12.dp)
            )
            .border(1.dp, textColor.copy(alpha = 0.06f), RoundedCornerShape(12.dp))
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = String.format("%02d", value),
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = textColor
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            color = textColor.copy(alpha = 0.4f)
        )
    }
}

fun validateAndCorrectMatchSchedule(match: com.example.model.Match, stage: TournamentStage): com.example.model.Match {
    return when (stage) {
        TournamentStage.FINAL -> {
            if (match.stadium.name != "MetLife Stadium") {
                match.copy(
                    stadium = com.example.model.Stadium(
                        name = "MetLife Stadium",
                        city = "East Rutherford, USA",
                        capacity = "82,500",
                        latitude = 40.8128,
                        longitude = -74.0742,
                        weatherTemp = "78°F",
                        weatherCondition = "Sunny & Clear"
                    )
                )
            } else match
        }
        TournamentStage.BRONZE -> {
            if (match.stadium.name != "Hard Rock Stadium" && match.stadium.name != "Miami Gardens Stadium" && match.stadium.name != "Miami Stadium") {
                match.copy(
                    stadium = com.example.model.Stadium(
                        name = "Hard Rock Stadium",
                        city = "Miami Gardens, USA",
                        capacity = "64,767",
                        latitude = 25.9580,
                        longitude = -80.2389,
                        weatherTemp = "85°F",
                        weatherCondition = "Sunny & Clear"
                    )
                )
            } else match
        }
        else -> match
    }
}

fun getMatchesForStage(
    stage: TournamentStage,
    isWomens: Boolean = true,
    allTeams: List<com.example.model.Team> = TeamDataProvider.womensTeams,
    realTimeAdvancedTeams: Map<String, List<String>>? = null
): List<Pair<com.example.model.Team, com.example.model.Match>> {
    val result = mutableListOf<Pair<com.example.model.Team, com.example.model.Match>>()
    
    val womensTeamsStr = getWomensTeamsForStage(stage)
    val stageTeams = allTeams.filter { womensTeamsStr.contains(it.abbreviation) }
    stageTeams.forEach { team ->
        result.add(team to team.nextMatch)
    }
    
    val uniqueMatches = mutableListOf<Pair<com.example.model.Team, com.example.model.Match>>()
    val seenPairs = mutableSetOf<String>()
    
    result.forEach { (team, match) ->
        val sortedKey = listOf(team.abbreviation, match.opponent).sorted().joinToString("-")
        if (!seenPairs.contains(sortedKey)) {
            seenPairs.add(sortedKey)
            uniqueMatches.add(team to match)
        }
    }
    
    return uniqueMatches
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RechartsAnalyticsDialog(
    team: Team?,
    allTeams: List<Team>,
    onDismiss: () -> Unit,
    onSelectTeam: (Team) -> Unit
) {
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
                .background(Color.Black.copy(alpha = 0.85f))
                .padding(horizontal = 12.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.92f)
                    .shadow(24.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                border = BorderStroke(1.5.dp, Color(0xFF334155))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Header Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = team?.flag ?: "📊",
                                fontSize = 28.sp
                            )
                            Column {
                                Text(
                                    text = (team?.name ?: "Recharts Analytics").uppercase(),
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "📊 RECHARTS PERFORMANCE ANALYTICS",
                                    color = Color(0xFFF97316),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.8.sp
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0xFF1E293B), CircleShape)
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

                    // Team Quick Chips Selector
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(allTeams) { t ->
                            val isSelected = (t.abbreviation == team?.abbreviation)
                            FilterChip(
                                selected = isSelected,
                                onClick = { onSelectTeam(t) },
                                label = {
                                    Text(
                                        text = "${t.flag} ${t.abbreviation}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else Color(0xFF94A3B8)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFFEA580C),
                                    containerColor = Color(0xFF1E293B)
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) Color(0xFFF97316) else Color(0xFF334155)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // WebView with Recharts.js
                    var webViewRef by remember { mutableStateOf<WebView?>(null) }

                    LaunchedEffect(team) {
                        team?.let { t ->
                            webViewRef?.let { wv ->
                                val cleanName = t.name.replace("'", "\\'")
                                val js = "if (window.setTeamFromAndroid) { window.setTeamFromAndroid('${t.abbreviation}', '$cleanName', '${t.flag}', ${t.stats.goalsScored}, ${t.stats.shotsOnTarget}, ${t.stats.wins}, ${t.stats.possessionPercent}); }"
                                wv.evaluateJavascript(js, null)
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF0F172A))
                            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp))
                    ) {
                        AndroidView(
                            factory = { context ->
                                WebView(context).apply {
                                    layoutParams = ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT
                                    )
                                    setBackgroundColor(0xFF0F172A.toInt())
                                    settings.apply {
                                        javaScriptEnabled = true
                                        domStorageEnabled = true
                                        databaseEnabled = true
                                        allowFileAccess = true
                                        allowContentAccess = true
                                        cacheMode = WebSettings.LOAD_DEFAULT
                                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                                    }
                                    webViewClient = object : WebViewClient() {
                                        override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean {
                                            return true
                                        }

                                        override fun onPageFinished(view: WebView?, url: String?) {
                                            super.onPageFinished(view, url)
                                            team?.let { t ->
                                                val cleanName = t.name.replace("'", "\\'")
                                                val js = "if (window.setTeamFromAndroid) { window.setTeamFromAndroid('${t.abbreviation}', '$cleanName', '${t.flag}', ${t.stats.goalsScored}, ${t.stats.shotsOnTarget}, ${t.stats.wins}, ${t.stats.possessionPercent}); }"
                                                view?.evaluateJavascript(js, null)
                                            }
                                        }
                                    }
                                    loadUrl("file:///android_asset/metrics.html")
                                    webViewRef = this
                                }
                            },
                            update = { wv ->
                                webViewRef = wv
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

