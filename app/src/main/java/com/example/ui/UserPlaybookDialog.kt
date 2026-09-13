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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.BrandOrangeRed

data class PlaybookSection(
    val id: String,
    val icon: ImageVector,
    val title: Map<AppLanguage, String>,
    val subtitle: Map<AppLanguage, String>,
    val details: Map<AppLanguage, List<String>>
)

object PlaybookDataProvider {
    val sections = listOf(
        PlaybookSection(
            id = "overview",
            icon = Icons.Default.Public,
            title = mapOf(
                AppLanguage.EN to "1. Women's Sports 3D Globe & Hub Overview",
                AppLanguage.CN to "1. 3D 女子体育地球仪与平台概览",
                AppLanguage.ES to "1. Globo 3D de Deportes Femeninos y Plataforma",
                AppLanguage.JP to "1. 女子スポーツ 3D地球儀 & ハブ概要",
                AppLanguage.TH to "1. ลูกโลก 3D กีฬาสตรีและภาพรวมแพลตฟอร์ม"
            ),
            subtitle = mapOf(
                AppLanguage.EN to "Explore global & USA national women's sports venues, leagues, and tournaments in 3D.",
                AppLanguage.CN to "在 3D 地球上探索美国国内与全球女子体育场馆、职业联赛与国际大赛。",
                AppLanguage.ES to "Explora sedes, ligas y torneos de deportes femeninos en EE.UU. y a nivel global en 3D.",
                AppLanguage.JP to "全米および世界中の女子スポーツスタジアム、リーグ、国際大会を3Dで探索。",
                AppLanguage.TH to "สำรวจสนาม ลีก และทัวร์นาเมนต์กีฬาสตรีทั้งในสหรัฐอเมริกาและทั่วโลกในรูปแบบ 3D"
            ),
            details = mapOf(
                AppLanguage.EN to listOf(
                    "• Global & USA Scope: Look up women's sports across USA National leagues (WNBA, NWSL, PWHL, WTA Tennis, PVF/LOVB Volleyball) and International competitions (FIFA Women's World Cup, UEFA Women's Champions League, Grand Slam Tennis, Cricket WPL).",
                    "• Interactive 3D Earth: Drag anywhere on the globe with one finger to rotate and explore venues and participating nations across North America, South America, Europe, Asia, and Oceania.",
                    "• Zoom Slider: Adjust the zoom slider (0.6x to 4.0x) or use pinch gestures to inspect host cities and stadium campus locations in high definition.",
                    "• Day / Night Modes: Switch between modern Glass Light Mode and Cosmic Dark Space Mode using the sun/moon button in the top bar.",
                    "• Glowing 3D Pins: Tap on any glowing venue or national team pin on the 3D globe to bring up in-depth statistics, venue capacities, and games schedules."
                ),
                AppLanguage.CN to listOf(
                    "• 全球与美国双重视野：全面涵盖美国本土女子职业联赛（WNBA 篮球、NWSL 足球、PWHL 冰球、WTA 网球公开赛、PVF/LOVB 排球）及国际顶级女子赛事（FIFA 女足世界杯、女足欧冠、大满贯网球赛、女子板球超级联赛 WPL）。",
                    "• 3D 互动地球仪：单指平滑拖拽球体，即可自由旋转探索北美、南美、欧洲、亚洲以及大洋洲的女子体育重镇与场馆。",
                    "• 精度缩放滑块：通过底部的缩放滑块（0.6x 至 4.0x）或双指捏合，高清缩放观察举办城市与体育场园区。",
                    "• 日间 / 深空主题：随时点击右上角日月按钮，在清新明亮模式与深空宇宙模式间自由切换。",
                    "• 发光 3D 标识：点击球体上任何发光的场馆或代表队标识，即可即时弹出详细统计、场馆容纳人数与赛程安排。"
                ),
                AppLanguage.ES to listOf(
                    "• Alcance Global y Nacional de EE.UU.: Consulta deportes femeninos en ligas nacionales de EE.UU. (WNBA, NWSL, PWHL, WTA, PVF/LOVB Voleibol) y torneos mundiales (Copa Mundial Femenina FIFA, UWCL, Grand Slam Tenis, WPL Críquet).",
                    "• Tierra 3D Interactiva: Arrastra con un dedo para girar el globo y explorar sedes y naciones en Norteamérica, Sudamérica, Europa, Asia y Oceanía.",
                    "• Control de Zoom: Usa el deslizador de zoom (0.6x a 4.0x) o pellizca la pantalla para inspeccionar las ciudades sede en alta definición.",
                    "• Modos Día y Noche: Cambia entre modo Claro de Cristal y Oscuro Cósmico con el botón de sol y luna en la barra superior.",
                    "• Pines 3D Luminosos: Toca cualquier pin en el globo para abrir estadísticas detalladas, aforos de estadios y partidos programados."
                ),
                AppLanguage.JP to listOf(
                    "• 全米 & グローバル対応: 米国国内リーグ（WNBA、NWSL、PWHL、WTAテニス、バレーボールPVF/LOVB）と世界的大会（FIFA女子W杯、女子欧州CL、グランドスラム、WPLクリケット）を完全網羅。",
                    "• インタラクティブ3D地球儀: ドラッグ操作で地球を自在に回転させ、北米、南米、欧州、アジア、オセアニアの女子スポーツ開催地を探索可能。",
                    "• ズームスライダー: 0.6倍〜4.0倍の精密ズームバーまたはピンチ操作で、開催都市やスタジアム周辺を高解像度で観察。",
                    "• 昼夜モード切替: 画面右上の太陽/月アイコンで、爽快なライトモードと漆黒のコズミックダークモードを即時切替。",
                    "• 発光3Dピン: 地球儀上に輝くスタジアムや代表チームのピンをタップすると、収容人数や詳細データシートが立ち上がります。"
                ),
                AppLanguage.TH to listOf(
                    "• ครอบคลุมทั้งสหรัฐฯ และระดับสากล: ค้นหาข้อมูลกีฬาสตรีในลีกระดับชาติของสหรัฐฯ (WNBA, NWSL, PWHL, WTA, PVF/LOVB) และทัวร์นาเมนต์ระดับโลก (ฟุตบอลโลกหญิง FIFA, UWCL, แกรนด์สแลม, WPL คริกเก็ต)",
                    "• ลูกโลก 3D แบบอินเทอร์แอคทีฟ: ลากนิ้วเพื่อหมุนลูกโลกและสำรวจสถานที่และประเทศที่เข้าร่วมทั้งในอเมริกาเหนือ อเมริกาใต้ ยุโรป เอเชีย และโอเชียเนีย",
                    "• ตัวควบคุมการซูม: ปรับสไลเดอร์ซูม (0.6x ถึง 4.0x) หรือใช้การบีบขยายหน้าจอเพื่อส่องดูพิกัดสนามและเมืองเจ้าภาพอย่างคมชัด",
                    "• โหมดกลางวัน/กลางคืน: สลับระหว่างโหมดสว่างสดใส (Glass Light) และโหมดอวกาศ (Cosmic Dark) ด้วยปุ่มดวงอาทิตย์/ดวงจันทร์",
                    "• หมุด 3D เรืองแสง: แตะหมุดเรืองแสงบนลูกโลก 3D เพื่อดูสถิติเชิงลึก ความจุสนาม และตารางการแข่งขันได้ทันที"
                )
            )
        ),
        PlaybookSection(
            id = "games_lookup",
            icon = Icons.Default.Sports,
            title = mapOf(
                AppLanguage.EN to "2. Looking Up Games, Scores & Schedules",
                AppLanguage.CN to "2. 查阅赛事对决、实时比分与赛程日历",
                AppLanguage.ES to "2. Consulta de Partidos, Marcadores y Calendarios",
                AppLanguage.JP to "2. 試合情報・リアルタイム速報 & スケジュール検索",
                AppLanguage.TH to "2. การค้นหาแมตช์ ผลคะแนนสด และตารางแข่ง"
            ),
            subtitle = mapOf(
                AppLanguage.EN to "Instant lookup of games, live scores, broadcast info, and star players across USA and globally.",
                AppLanguage.CN to "即刻查阅美国本土与国际女子赛事实时比分、转播平台与超级球星。",
                AppLanguage.ES to "Búsqueda instantánea de partidos, resultados en vivo, canales de transmisión y estrellas en EE.UU. y el mundo.",
                AppLanguage.JP to "全米および世界の女子スポーツの試合日程、速報スコア、放送局、注目スター選手を即座にチェック。",
                AppLanguage.TH to "ค้นหาข้อมูลการแข่งขัน คะแนนสด ช่องทางถ่ายทอดสด และนักกีฬาดาวดังได้ทันที"
            ),
            details = mapOf(
                AppLanguage.EN to listOf(
                    "• Games Look-Up Sheet: Tap the '⚡ Games' or sports icon button in the header deck to open the comprehensive Women's Sports Games sheet.",
                    "• Scope Filters: Switch between '🌟 All Scopes', '🇺🇸 USA National' (WNBA Finals, NWSL Championship, PWHL Playoffs), and '🌍 International' (FIFA WWC, UWCL, Wimbledon, Australian Open).",
                    "• Sport Category Filters: Filter games by Basketball 🏀, Soccer ⚽, Tennis 🎾, Ice Hockey 🏒, Volleyball 🏐, and Cricket 🏏.",
                    "• Live Scores & Broadcasts: View real-time scores for ongoing matches, kickoff/tip-off times in your local timezone, and official broadcast channels (ESPN, Prime Video, CBS Sports, DAZN, Sky Sports).",
                    "• Instant 3D Fly-To: Each game card features a 'Fly to Stadium' button that rotates the 3D globe and zooms directly into the venue where the game is played!"
                ),
                AppLanguage.CN to listOf(
                    "• 赛事查阅抽屉：点击顶部控制栏的“⚡ Games”或运动图标按钮，即可展开女子体育赛事全面查询面板。",
                    "• 范围智能筛选：在“🌟 全部赛事”、“🇺🇸 美国本土联赛”（WNBA 总决赛、NWSL 冠军赛、PWHL 季后赛）与“🌍 国际赛事”（FIFA 女足世界杯、女足欧冠、温网、澳网）之间快速切换。",
                    "• 运动分类筛选：支持按篮球 🏀、足球 ⚽、网球 🎾、冰球 🏒、排球 🏐 与板球 🏏 类别一键分类检索。",
                    "• 实时比分与官方转播：查看进行中比赛的即时比分、转换为本地时区的开赛时间，以及官方电视/流媒体转播平台（ESPN、Prime Video、CBS Sports、DAZN 等）。",
                    "• 3D 球场一键飞跃：每场比赛卡片均配有“Fly to Stadium”按钮，点击后 3D 地球仪将平滑旋转并直接聚焦至该比赛的举办场馆！"
                ),
                AppLanguage.ES to listOf(
                    "• Panel de Partidos: Toca el botón '⚡ Games' o el icono de deportes en la barra superior para abrir el directorio de encuentros.",
                    "• Filtros de Alcance: Alterna entre '🌟 Todos', '🇺🇸 Nacional EE.UU.' (WNBA, NWSL, PWHL) y '🌍 Internacional' (Copa Mundial Femenina, UWCL, Wimbledon).",
                    "• Filtros por Deporte: Filtra partidos de Baloncesto 🏀, Fútbol ⚽, Tenis 🎾, Hockey sobre Hielo 🏒, Voleibol 🏐 y Críquet 🏏.",
                    "• Resultados en Vivo y Transmisión: Consulta marcadores en tiempo real, horas de inicio en tu zona horaria local y cadenas oficiales (ESPN, Prime Video, CBS, DAZN).",
                    "• Vuelo 3D Instantáneo al Estadio: Cada partido incluye el botón 'Fly to Stadium' para girar y enfocar el globo 3D exactamente sobre el estadio."
                ),
                AppLanguage.JP to listOf(
                    "• 試合検索シート: ヘッダーバーの「⚡ Games」またはスポーツアイコンをタップすると、総合女子スポーツ試合一覧が開きます。",
                    "• 開催スコープ絞り込み: 「🌟 全スコープ」「🇺🇸 米国国内（WNBA、NWSL、PWHL）」「🌍 国際大会（女子W杯、女子欧州CL、四大大会テニス）」をワンタップ切替。",
                    "• 競技カテゴリー別表示: バスケットボール🏀、サッカー⚽、テニス🎾、アイスホッケー🏒、バレーボール🏐、クリケット🏏ごとに分類閲覧可能。",
                    "• ライブスコア & 放送局情報: リアルタイム試合速報、端末の現地時間に自動変換された開始時間、公式中継局（ESPN、Prime Video、CBS、DAZN）を表示。",
                    "• 3Dスタジアム飛躍ボタン: 各試合カードの「Fly to Stadium」を押すと、3D地球儀がスムーズに旋回し、その試合が行われるスタジアムへ直接ズームイン！"
                ),
                AppLanguage.TH to listOf(
                    "• หน้าต่างค้นหาแมตช์: แตะปุ่ม '⚡ Games' หรือไอคอนกีฬาในแถบด้านบนเพื่อเปิดตารางแข่งขันกีฬาสตรีที่ครอบคลุม",
                    "• ตัวกรองขอบเขต: สลับระหว่าง '🌟 ทั้งหมด', '🇺🇸 ระดับชาติสหรัฐฯ' (รอบชิง WNBA, แชมป์ NWSL, เพลย์ออฟ PWHL) และ '🌍 ระดับสากล' (ฟุตบอลโลกหญิง, UWCL, วิมเบิลดัน)",
                    "• ตัวกรองประเภทกีฬา: กรองแมตช์ตาม บาสเกตบอล 🏀, ฟุตบอล ⚽, เทนนิส 🎾, ฮอกกี้น้ำแข็ง 🏒, วอลเลย์บอล 🏐, และ คริกเก็ต 🏏",
                    "• ผลคะแนนสดและช่องถ่ายทอด: ดูคะแนนสดแบบเรียลไทม์ เวลาเริ่มแข่งที่ปรับตามเวลาท้องถิ่น และช่องทางถ่ายทอดสดทางการ (ESPN, Prime Video, CBS Sports, DAZN)",
                    "• วาร์ปกล้อง 3D ไปยังสนาม: การ์ดแต่ละแมตช์มีปุ่ม 'Fly to Stadium' ที่จะหมุนและซูมลูกโลก 3D ไปยังสนามที่แข่งทันที!"
                )
            )
        ),
        PlaybookSection(
            id = "stadiums_lookup",
            icon = Icons.Default.Place,
            title = mapOf(
                AppLanguage.EN to "3. Looking Up Stadiums & Arenas Globally & in USA",
                AppLanguage.CN to "3. 查阅全球与美国女子体育场馆与球场",
                AppLanguage.ES to "3. Consulta de Estadios y Arenas en EE.UU. y el Mundo",
                AppLanguage.JP to "3. 全米 & 世界のスタジアム・アリーナ詳細検索",
                AppLanguage.TH to "3. การค้นหาสนามกีฬาและอารีน่าทั่วโลกและในสหรัฐฯ"
            ),
            subtitle = mapOf(
                AppLanguage.EN to "Explore world-record venues like CPKC Stadium, Barclays Center, Maracanã, and Wembley.",
                AppLanguage.CN to "探索全球首座女子专业球场 CPKC 体育场、巴克莱中心、马拉卡纳与温布利等传奇场馆。",
                AppLanguage.ES to "Explora recintos históricos como el CPKC Stadium, Barclays Center, Maracaná y Wembley.",
                AppLanguage.JP to "世界初の女子プロ専用球技場CPKCスタジアム、バークレイズ・センター、マラカナン等の名会場を網羅。",
                AppLanguage.TH to "สำรวจสนามระดับโลก เช่น สนาม CPKC (สนามกีฬาสตรีแห่งแรกของโลก), บาร์เคลย์ส เซ็นเตอร์, มารากาน่า และเวมบลีย์"
            ),
            details = mapOf(
                AppLanguage.EN to listOf(
                    "• Stadiums Directory: Click the green Location Pin icon in the top header deck to open the full stadium directory.",
                    "• USA National Venues: Discover CPKC Stadium in Kansas City (the world's 1st stadium built exclusively for a women's professional sports team), Barclays Center (New York Liberty), Gainbridge Fieldhouse (Indiana Fever), Michelob ULTRA Arena (Las Vegas Aces), Arthur Ashe Stadium (US Open Tennis), Providence Park (Portland Thorns), BMO Stadium (Angel City FC), and Xcel Energy Center (PWHL Minnesota).",
                    "• Global Iconic Venues: Explore Maracanã & Neo Química Arena (FIFA Women's World Cup 2027 Brazil), Wembley Stadium (Women's Finalissima / Lionesses), Centre Court Wimbledon (London), Court Philippe-Chatrier (Roland-Garros Paris), Rod Laver Arena (Australian Open Melbourne), Groupama Stadium (Lyon, France), Camp Nou / Johan Cruyff (FC Barcelona Femení), Place Bell (PWHL Montreal), and DY Patil Stadium (WPL Cricket India).",
                    "• Venue Details & Trivia: View seating capacities, playing surface (Hardwood, Natural Grass, Hardcourt, Ice), home tenant teams, historical milestones, and real-time live local weather.",
                    "• 3D Camera Jump: Tap 'View on Map' to animate the 3D Earth camera directly to the stadium coordinates with pin focus."
                ),
                AppLanguage.CN to listOf(
                    "• 体育场指南面板：点击顶部浮动栏中的绿色定位大头针图标按钮，即可打开场馆全景目录。",
                    "• 美国本土标杆场馆：包括堪萨斯城 CPKC 体育场（全球首座专为女子职业运动队量身打造的专业足球场）、巴克莱中心（纽约自由人队）、甘布里吉球馆（印第安纳狂热队）、米其洛超级球馆（拉斯维加斯王牌队）、阿瑟·阿什球场（美网）、普罗维登斯公园（波特兰荆棘队）、BMO 体育场（天使城 FC）及明尼苏达 Xcel 能量中心（PWHL 冰球）。",
                    "• 国际标志性殿堂：涵盖巴西马拉卡纳体育场与新哥林多体育场（2027 FIFA 女足世界杯）、伦敦温布利大球场（女足欧洲杯破纪录之夜）、温网中央球场、巴黎罗兰·加洛斯夏蒂埃球场、墨尔本罗德·拉沃尔球场、法国里昂奥林匹克公园球场、巴塞罗那克鲁伊夫球场、蒙特利尔贝尔广场球场及印度孟买 DY 帕蒂尔板球场。",
                    "• 详细技术参数：查看准确坐席容量、比赛地面（枫木地板、天然草坪、硬地球场、专业冰面）、常驻主队、历史里程碑与当地实时天气。",
                    "• 3D 镜头精准定位：点击“View on Map”，3D 地球镜头将顺滑飞跃至该场馆的地理坐标并高亮展示。"
                ),
                AppLanguage.ES to listOf(
                    "• Directorio de Estadios: Toca el icono verde de Pin de Ubicación en el panel superior para acceder al catálogo completo.",
                    "• Sedes Nacionales de EE.UU.: Descubre el CPKC Stadium (el 1er estadio del mundo construido exclusivamente para un equipo profesional femenino), Barclays Center (NY Liberty), Gainbridge Fieldhouse (Indiana Fever), Michelob ULTRA Arena (Aces), Estadio Arthur Ashe (US Open), Providence Park (Thorns) y BMO Stadium.",
                    "• Sedes Icónicas Globales: Explora Maracaná y Neo Química Arena (Mundial 2027 Brasil), Estadio Wembley (récord de aforo femenino), Pista Central de Wimbledon, Philippe-Chatrier en París, Rod Laver Arena en Melbourne, Groupama Stadium en Lyon, Estadio Johan Cruyff en Barcelona y Estadio DY Patil en India.",
                    "• Detalles y Trivia: Capacidad exacta de público, superficie de juego (madera, césped, pista dura, hielo), equipos inquilinos, hitos y clima en vivo.",
                    "• Salto de Cámara 3D: Toca 'View on Map' para animar el globo 3D directamente a las coordenadas del estadio."
                ),
                AppLanguage.JP to listOf(
                    "• スタジアムディレクトリ: ヘッダーの緑色ピンアイコンをタップすると、全スタジアムガイドが開きます。",
                    "• 米国国内の主要会場: カンザスシティのCPKCスタジアム（世界初の女子プロスポーツ専用設計スタジアム）、バークレイズ・センター（NYリバティ）、ゲインブリッジ・フィールドハウス（インディアナ・フィーバー）、ミケロブ・ウルトラ・アリーナ（ラスベガス・エーシズ）、アーサー・アッシュ・スタジアム（全米オープン）、プロビデンス・パーク（ポートランド・ソーンズ）等。",
                    "• 世界の伝説的会場: ブラジルのマラカナン＆ネオ・キミカ（2027女子W杯）、英ウェンブリー（観客動員世界記録）、ウィンブルドン・センターコート、全仏フィリップ・シャトリエ、豪ロッド・レーバー・アリーナ、リヨン・グルパマ、バルセロナ・ヨハン・クライフ、インド・DYパティール等を網羅。",
                    "• スペック & 建築トリビア: 収容人数、フロア素材（ハードウッド、天然芝、ハードコート、アイスリンク）、所属チーム、歴史的快挙、現地ライブ気象情報。",
                    "• 3Dワンタップ飛躍: 「View on Map」を押すと、3D地球カメラがスタジアム位置へダイナミックにズームインします。"
                ),
                AppLanguage.TH to listOf(
                    "• สมุดรายชื่อสนาม: คลิกที่ปุ่มหมุดสถานที่สีเขียวในแถบด้านบนเพื่อเปิดดูรายชื่อสนามและอารีน่าทั้งหมด",
                    "• สนามระดับชาติในสหรัฐฯ: ค้นพบสนาม CPKC ในแคนซัสซิตี้ (สนามแห่งแรกในโลกที่สร้างขึ้นเพื่อทีมกีฬาสตรีอาชีพโดยเฉพาะ), บาร์เคลย์ส เซ็นเตอร์ (นิวยอร์ก ลิเบอร์ตี้), เกนบริดจ์ ฟิลด์เฮาส์ (อินเดียนา ฟีเวอร์), มิเชลอบ อัลตรา อารีน่า (ลาสเวกัส เอซ), สนามอาเธอร์ แอช (ยูเอส โอเพ่น) และ โพรวิเดนซ์ พาร์ค",
                    "• สนามระดับตำนานทั่วโลก: สำรวจสนามมารากาน่าและนีโอ กีมิก้า (ฟุตบอลโลกหญิง 2027 บราซิล), สนามเวมบลีย์ (สถิติผู้ชมกีฬาสตรี), คอร์ตกลางวิมเบิลดัน, ฟิลิปป์-ชาทริเยร์ ในปารีส, ร็อด เลเวอร์ อารีน่า เมลเบิร์น, กรูปามา สเตเดียม ลียง และ ดีวาย ปาติล สเตเดียม ในอินเดีย",
                    "• รายละเอียดและความจุ: ความจุที่นั่งที่แน่นอน ประเภทพื้นสนาม (ไม้ปาร์เกต์, หญ้าจริง, ฮาร์ดคอร์ต, ลานน้ำแข็ง), ทีมเจ้าถิ่น, เหตุการณ์ประวัติศาสตร์ และสภาพอากาศสด",
                    "• การวาร์ปกล้อง 3D: แตะ 'View on Map' เพื่อหมุนและขยายกล้อง 3D บนลูกโลกไปยังพิกัดของสนามนั้นทันที"
                )
            )
        ),
        PlaybookSection(
            id = "athletes_stars",
            icon = Icons.Default.Star,
            title = mapOf(
                AppLanguage.EN to "4. Star Athletes, Rosters & Milestones",
                AppLanguage.CN to "4. 传奇球星档案、阵容名单与里程碑",
                AppLanguage.ES to "4. Atletas Estrella, Plantillas e Hitos",
                AppLanguage.JP to "4. スター選手名鑑・ロスター & 歴史的記録",
                AppLanguage.TH to "4. นักกีฬาดาวดัง รายชื่อผู้เล่น และสถิติสำคัญ"
            ),
            subtitle = mapOf(
                AppLanguage.EN to "Learn about Caitlin Clark, A'ja Wilson, Coco Gauff, Sophia Smith, Marta, and global icons.",
                AppLanguage.CN to "了解凯特琳·克拉克、阿贾·威尔逊、高芙、索菲娅·史密斯、玛塔等体坛超级巨星。",
                AppLanguage.ES to "Conoce a Caitlin Clark, A'ja Wilson, Coco Gauff, Sophia Smith, Marta y figuras mundiales.",
                AppLanguage.JP to "ケイトリン・クラーク、エイジャ・ウィルソン、ココ・ガウフ、ソフィア・スミス、マルタらのプロフィールを閲覧。",
                AppLanguage.TH to "เรียนรู้เกี่ยวกับ เคทลิน คลาร์ก, เอจา วิลสัน, โคโค กอฟฟ์, โซเฟีย สมิธ, มาร์ตา และดาวดังระดับโลก"
            ),
            details = mapOf(
                AppLanguage.EN to listOf(
                    "• Athlete Highlights: Key game matchups feature prominent cards spotlighting top athletes with their jersey numbers, positions, and current scoring/assists/ranking statistics.",
                    "• WNBA Icons: Read detailed insights on Caitlin Clark (NCAA all-time leading scorer, Indiana Fever point guard sensation), A'ja Wilson (2x WNBA Champion & MVP, Las Vegas Aces), and Sabrina Ionescu (NY Liberty sharp-shooter).",
                    "• NWSL & Soccer Legends: Track Sophia Smith (Portland Thorns & USWNT Olympic gold medalist), Marta (6x World Player of the Year, Brazil & Orlando Pride), and Trinity Rodman (Washington Spirit).",
                    "• Tennis, Hockey & Beyond: Access milestones for Coco Gauff (US Open champion), Marie-Philip Poulin (Canada PWHL Olympic legend), and Smriti Mandhana (India Cricket superstar)."
                ),
                AppLanguage.CN to listOf(
                    "• 球星特写面板：赛事卡片中置顶展示对决双方的核心球星，附带球衣号码、场上位置与得分/助攻/世界排名等关键数据。",
                    "• WNBA 超级巨星：深度了解凯特琳·克拉克（Caitlin Clark，NCAA 历史得分王、印第安纳狂热核心）、阿贾·威尔逊（A'ja Wilson，两届 WNBA 总冠军与 MVP）、萨布丽娜·约内斯库（Sabrina Ionescu，三分球记录保持者）。",
                    "• NWSL 与足球传奇：追踪索菲娅·史密斯（Sophia Smith，波特兰荆棘队与美国女足奥运金牌前锋）、玛塔（Marta，六届世界足球小姐、巴西队与奥兰多荣耀队灵魂）、特里尼蒂·罗德曼（Trinity Rodman）。",
                    "• 网球、冰球与板球巨星：查阅高芙（Coco Gauff，美网大满贯冠军）、玛丽-菲利普·普兰（Marie-Philip Poulin，加拿大女子冰球四届奥运传奇）与斯姆里蒂·曼达纳（Smriti Mandhana，印度板球超级巨星）的生平战绩。"
                ),
                AppLanguage.ES to listOf(
                    "• Fichas de Atletas: Las tarjetas de partido destacan a las principales deportistas con su dorsal, posición y estadísticas clave de anotación y asistencias.",
                    "• Íconos de la WNBA: Descubre a Caitlin Clark (máxima anotadora histórica de la NCAA, Indiana Fever), A'ja Wilson (doble campeona y MVP de la WNBA, Las Vegas Aces) y Sabrina Ionescu.",
                    "• Leyendas del Fútbol Femenino: Sigue a Sophia Smith (Portland Thorns y oro olímpico con EE.UU.), Marta (6 veces Mejor Jugadora del Mundo, Brasil) y Trinity Rodman.",
                    "• Tenis, Hockey y Más: Consulta los hitos de Coco Gauff (campeona del US Open), Marie-Philip Poulin (estrella olímpica del PWHL) y Smriti Mandhana (críquet de la India)."
                ),
                AppLanguage.JP to listOf(
                    "• 注目スター特集: 試合カード内に注目アスリートの背番号、ポジション、得点・アシスト・世界ランキングを分かりやすく掲載。",
                    "• WNBAのスーパースター: ケイトリン・クラーク（NCAA歴代最多得点王、インディアナ・フィーバー）、エイジャ・ウィルソン（WNBA制覇＆MVP、ラスベガス・エーシズ）、サブリナ・イオネスクらの活躍を詳説。",
                    "• NWSL & 女子サッカーの至宝: ソフィア・スミス（ポートランド・ソーンズ＆米国代表五輪金メダリスト）、マルタ（FIFA世界年間最優秀選手6度受賞、ブラジル代表）、トリニティ・ロドマン。",
                    "• テニス・アイスホッケー他: ココ・ガウフ（全米オープン覇者）、マリー＝フィリップ・プラン（五輪カナダ代表PWHL主将）、スリティ・マンダナ（インド女子クリケットのエース）。"
                ),
                AppLanguage.TH to listOf(
                    "• ไฮไลท์นักกีฬา: การ์ดแข่งขันจะแสดงนักกีฬาตัวท็อป พร้อมเบอร์เสื้อ ตำแหน่ง สถิติคะแนน การทำแอสซิสต์ และอันดับโลก",
                    "• ดาวดัง WNBA: ศึกษาข้อมูลของ เคทลิน คลาร์ก (ผู้ทำคะแนนสูงสุดตลอดกาล NCAA ของอินเดียนา ฟีเวอร์), เอจา วิลสัน (แชมป์และ MVP 2 สมัยของลาสเวกัส เอซ) และ ซาบรินา ไอโอเนสคู",
                    "• ตำนานฟุตบอลสตรี NWSL: ติดตาม โซเฟีย สมิธ (พอร์ตแลนด์ ธอร์นส์ และเหรียญทองโอลิมปิก), มาร์ตา (นักเตะยอดเยี่ยมแห่งปี 6 สมัยของบราซิล) และ ทรินิตี้ ร็อดแมน",
                    "• เทนนิส ฮอกกี้ และอื่นๆ: บันทึกความสำเร็จของ โคโค กอฟฟ์ (แชมป์ยูเอส โอเพ่น), มารี-ฟิลิป ปูแลง (ตำนานโอลิมปิก PWHL แคนาดา) และ สมีริติ มันธานา (ซูเปอร์สตาร์คริกเก็ตอินเดีย)"
                )
            )
        ),
        PlaybookSection(
            id = "ai_tactics",
            icon = Icons.Default.Psychology,
            title = mapOf(
                AppLanguage.EN to "5. Gemini AI Match Analysis & 30-Min Alerts",
                AppLanguage.CN to "5. Gemini AI 深度赛前分析与 30 分钟智能提醒",
                AppLanguage.ES to "5. Análisis Táctico con Gemini AI y Alertas de 30 Min",
                AppLanguage.JP to "5. Gemini AI 試合分析 & 30分前スマートアラート",
                AppLanguage.TH to "5. การวิเคราะห์แมตช์ด้วย Gemini AI และการแจ้งเตือนล่วงหน้า 30 นาที"
            ),
            subtitle = mapOf(
                AppLanguage.EN to "AI tactical keys to the game, head-to-head records, and automated kickoff reminders.",
                AppLanguage.CN to "AI 提炼比赛关键战术、历史交锋战绩与开赛前 30 分钟本地通知。",
                AppLanguage.ES to "Claves tácticas generadas por IA, historial cara a cara y alarmas 30 minutos antes del partido.",
                AppLanguage.JP to "AIによる戦術キーポイント解説、対戦成績、試合開始30分前の自動プッシュ通知機能。",
                AppLanguage.TH to "กลยุทธ์สำคัญจาก AI สถิติการพบกัน และการแจ้งเตือนอัตโนมัติก่อนแข่ง 30 นาที"
            ),
            details = mapOf(
                AppLanguage.EN to listOf(
                    "• Gemini AI Tactical Preview: Tap the brain icon (🧠) on any game card to generate an AI breakdown of key matchups, offensive pace, defensive schemes, and head-to-head records.",
                    "• Head-to-Head History: Review recent meetings and score margins between rival franchises (e.g., Las Vegas Aces vs New York Liberty, USA vs Brazil).",
                    "• 30-Minute Kickoff Alert: Tap the Bell icon (🔔) next to any upcoming fixture to schedule an automated local notification 30 minutes before tip-off/kickoff.",
                    "• Offline-Safe Scheduling: Alerts run via Android AlarmManager and will wake your device even if the app is minimized or backgrounded."
                ),
                AppLanguage.CN to listOf(
                    "• Gemini AI 战术解析：点击任意比赛卡片上的大脑图标（🧠），即刻唤醒 AI 解读关键对位、攻防节奏转换与两队交手战绩。",
                    "• 历史对决数据：快速查看豪强对阵历史（如拉斯维加斯王牌对决纽约自由人、美国女足交锋巴西女足等宿敌记录）。",
                    "• 30 分钟智能开赛提醒：点击赛程右侧的铃铛图标（🔔），即可预订开球前 30 分钟的本地推送闹钟。",
                    "• 后台持久可靠：基于 Android 系统级 AlarmManager 运行，即使应用处于后台或锁屏状态，也能准时唤醒提醒您收看比赛。"
                ),
                AppLanguage.ES to listOf(
                    "• Análisis Táctico Gemini AI: Toca el icono de cerebro (🧠) en cualquier partido para generar un informe sobre ritmo ofensivo, defensas y duelos directos.",
                    "• Historial Cara a Cara: Revisa los últimos enfrentamientos y márgenes de puntos entre rivales históricos.",
                    "• Alarma 30 Minutos Antes: Haz clic en la campana (🔔) para programar una notificación automática 30 minutos antes del inicio.",
                    "• Funcionamiento en Segundo Plano: Funciona con AlarmManager de Android, avisándote puntualmente aunque la aplicación esté cerrada."
                ),
                AppLanguage.JP to listOf(
                    "• Gemini AI 戦術プレビュー: 各試合カードの脳アイコン（🧠）をタップすると、マッチアップの焦点、攻守のペース配分、対戦履歴をAIが即時解説。",
                    "• 直接対決ヒストリー: エーシズ対リバティ、米国代表対ブラジル代表など、宿敵同士の過去対戦結果を即時照会。",
                    "• 試合開始30分前アラート: ベルアイコン（🔔）をタップすると、キックオフ/ティップオフの30分前に自動プッシュ通知を予約。",
                    "• バックグラウンド確実通知: AndroidのAlarmManagerにより、アプリが閉じられていても正確なタイミングでお知らせします。"
                ),
                AppLanguage.TH to listOf(
                    "• บทวิเคราะห์แทคติกจาก Gemini AI: แตะไอคอนรูปสมอง (🧠) ในการ์ดการแข่งขันเพื่อดูการเจาะลึกแทคติก การประกบตัว และสถิติ H2H",
                    "• ประวัติการพบกัน: ตรวจสอบผลการพบกันล่าสุดและผลต่างคะแนนระหว่างทีมคู่ปรับ (เช่น ลาสเวกัส เอซ พบ นิวยอร์ก ลิเบอร์ตี้)",
                    "• แจ้งเตือนล่วงหน้า 30 นาที: แตะไอคอนกระดิ่ง (🔔) เพื่อตั้งเวลาการแจ้งเตือนล่วงหน้า 30 นาทีก่อนเริ่มแข่งขันโดยอัตโนมัติ",
                    "• ทำงานได้แม้อยู่เบื้องหลัง: ใช้ระบบ Android AlarmManager แจ้งเตือนตรงเวลาแม้จะพับแอปไว้ก็ตาม"
                )
            )
        ),
        PlaybookSection(
            id = "language_support",
            icon = Icons.Default.Language,
            title = mapOf(
                AppLanguage.EN to "6. 5-Language Instant Translation (EN, 中文, ES, JP, TH)",
                AppLanguage.CN to "6. 全球 5 大语言即时无缝切换 (英语, 中文, 西班牙语, 日语, 泰语)",
                AppLanguage.ES to "6. Traducción Instantánea en 5 Idiomas (EN, 中文, ES, JP, TH)",
                AppLanguage.JP to "6. 5言語インスタント翻訳 (英語, 中国語, スペイン語, 日本語, タイ語)",
                AppLanguage.TH to "6. การแปลภาษาทันที 5 ภาษา (อังกฤษ, จีน, สเปน, ญี่ปุ่น, ไทย)"
            ),
            subtitle = mapOf(
                AppLanguage.EN to "Seamless one-tap language switching across English, Chinese, Spanish, Japanese, and Thai.",
                AppLanguage.CN to "一键无缝切换英语、中文、西班牙语、日语与泰语 5 大国际语言。",
                AppLanguage.ES to "Cambio de idioma con un solo toque entre inglés, chino, español, japonés y tailandés.",
                AppLanguage.JP to "英語、中国語、スペイン語、日本語、タイ語の全5言語をワンタップでシームレス切替。",
                AppLanguage.TH to "สลับภาษาด้วยการแตะเพียงครั้งเดียวระหว่าง อังกฤษ, จีน, สเปน, ญี่ปุ่น และ ไทย"
            ),
            details = mapOf(
                AppLanguage.EN to listOf(
                    "• Language Button Placement: Tap the language pill button (showing 'EN', 'CN', 'ES', 'JP', or 'TH') located at the right side of the location icon in the header deck.",
                    "• Deep Multi-Language Coverage: All sections of the User Playbook, games schedules, tournament stages, stadium fact sheets, weather conditions, and AI prompts are translated natively.",
                    "• Dynamic Localization: Switching languages immediately updates the UI with zero lag and retains your current search filters and active stadium on the 3D globe."
                ),
                AppLanguage.CN to listOf(
                    "• 语言按钮位置：点击顶部控制栏中定位图标右侧的语言药丸按钮（显示 'EN'、'CN'、'ES'、'JP' 或 'TH'）。",
                    "• 深度多语言覆盖：本用户指南（Playbook）的所有章节、赛事赛程、场馆事实表、天气信息与 AI 提示词均已完成深度原生化翻译。",
                    "• 毫秒级动态切换：切换语言瞬间完成全界面刷新，且完整保留您当前的搜索关键词与 3D 地球仪上选中的场馆定位。"
                ),
                AppLanguage.ES to listOf(
                    "• Ubicación del Botón: Toca el botón de idioma ('EN', 'CN', 'ES', 'JP' o 'TH') a la derecha del icono de ubicación en la barra superior.",
                    "• Cobertura Lingüística Profunda: Todas las secciones del Playbook, calendarios de partidos, fichas de estadios, clima y avisos de IA están traducidos de manera nativa.",
                    "• Actualización Instantánea: El cambio de idioma es inmediato sin perder tus filtros ni la posición de la cámara 3D."
                ),
                AppLanguage.JP to listOf(
                    "• 言語ボタンの配置場所: ヘッダーバーのロケーションピンアイコンの右隣にある言語ピルボタン（'EN', 'CN', 'ES', 'JP', 'TH'）をタップ。",
                    "• 深い多言語カバー率: ユーザープレイブックの全セクション、試合日程、スタジアム詳細データ、天気情報、AIプロンプトが母国語レベルでローカライズされています。",
                    "• 瞬時のダイナミック更新: 言語変更はミリ秒単位で反映され、現在選択中の検索フィルターや3D地球儀のスタジアム位置を維持します。"
                ),
                AppLanguage.TH to listOf(
                    "• ตำแหน่งปุ่มภาษา: แตะที่ปุ่มเม็ดยาแสดงภาษา ('EN', 'CN', 'ES', 'JP' หรือ 'TH') ที่อยู่ทางด้านขวาของไอคอนหมุดสถานที่ในแถบด้านบน",
                    "• ครอบคลุมหลายภาษาอย่างลึกซึ้ง: ทุกส่วนของคู่มือการใช้งาน (Playbook), ตารางแข่ง, ข้อมูลสนาม, สภาพอากาศ และระบบวิเคราะห์ AI ได้รับการแปลภาษาอย่างสมบูรณ์",
                    "• อัปเดตทันทีแบบไดนามิก: สลับภาษาได้ทันทีโดยไม่เสียตัวกรองที่เลือกไว้ และกล้อง 3D จะยังคงโฟกัสที่สนามเดิมอย่างราบรื่น"
                )
            )
        )
    )
}

@Composable
fun UserPlaybookDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    currentLanguage: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    theme: GlobeTheme
) {
    if (!isOpen) return

    var activeLanguage by remember(currentLanguage) { mutableStateOf(currentLanguage) }
    var selectedCategory by remember { mutableStateOf("overview") }

    val dialogBg = if (theme == GlobeTheme.GLASS_LIGHT) {
        Color(0xFFF8FAFC)
    } else {
        Color(0xFF0F172A)
    }

    val cardBg = if (theme == GlobeTheme.GLASS_LIGHT) {
        Color.White
    } else {
        Color(0xFF1E293B)
    }

    val textColor = if (theme == GlobeTheme.GLASS_LIGHT) {
        Color(0xFF0F172A)
    } else {
        Color(0xFFF8FAFC)
    }

    val subTextColor = if (theme == GlobeTheme.GLASS_LIGHT) {
        Color(0xFF475569)
    } else {
        Color(0xFF94A3B8)
    }

    val accent = BrandOrangeRed

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.88f)
                .clip(RoundedCornerShape(24.dp))
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            accent,
                            if (theme == GlobeTheme.GLASS_LIGHT) Color(0xFF0D9488) else Color(0xFF38BDF8)
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .testTag("user_playbook_dialog"),
            color = dialogBg
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // DIALOG HEADER: Title + Language Switcher + Close Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(accent, accent.copy(alpha = 0.7f))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column {
                            val headerTitle = when (activeLanguage) {
                                AppLanguage.EN -> "USER PLAYBOOK & GUIDE"
                                AppLanguage.CN -> "用户指南与功能手册"
                                AppLanguage.ES -> "GUÍA Y MANUAL DEL USUARIO"
                                AppLanguage.JP -> "ユーザープレイブック & 攻略ガイド"
                                AppLanguage.TH -> "คู่มือการใช้งานและฟังก์ชันแอป"
                            }
                            Text(
                                text = headerTitle,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = textColor,
                                letterSpacing = 0.5.sp
                            )
                            val headerSub = when (activeLanguage) {
                                AppLanguage.EN -> "Full Feature Manual • FIFA World Cup 3D"
                                AppLanguage.CN -> "全功能操作指南 • FIFA 3D 世界杯"
                                AppLanguage.ES -> "Manual de Funciones • Copa Mundial FIFA 3D"
                                AppLanguage.JP -> "全機能操作ガイド • FIFA 3D ワールドカップ"
                                AppLanguage.TH -> "คู่มือฟังก์ชันทั้งหมด • FIFA 3D World Cup"
                            }
                            Text(
                                text = headerSub,
                                fontSize = 11.sp,
                                color = subTextColor,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                if (theme == GlobeTheme.GLASS_LIGHT) Color(0xFFE2E8F0) else Color(0xFF334155)
                            )
                            .testTag("close_playbook_dialog_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = textColor
                        )
                    }
                }

                // LANGUAGE QUICK SWITCHER TABS INSIDE PLAYBOOK
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(AppLanguage.entries) { lang ->
                        val isSelected = activeLanguage == lang
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) {
                                        Brush.horizontalGradient(listOf(accent, Color(0xFFEA580C)))
                                    } else {
                                        Brush.horizontalGradient(
                                            listOf(
                                                if (theme == GlobeTheme.GLASS_LIGHT) Color(0xFFE2E8F0) else Color(0xFF334155),
                                                if (theme == GlobeTheme.GLASS_LIGHT) Color(0xFFCBD5E1) else Color(0xFF1E293B)
                                            )
                                        )
                                    }
                                )
                                .clickable {
                                    activeLanguage = lang
                                    onLanguageChange(lang)
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${lang.code} • ${lang.displayName}",
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                color = if (isSelected) Color.White else textColor
                            )
                        }
                    }
                }

                Divider(
                    color = if (theme == GlobeTheme.GLASS_LIGHT) Color(0xFFE2E8F0) else Color(0xFF334155),
                    thickness = 1.dp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // CATEGORY SELECTOR TABS
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(PlaybookDataProvider.sections) { sec ->
                        val isCatSelected = selectedCategory == sec.id
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    if (isCatSelected) {
                                        if (theme == GlobeTheme.GLASS_LIGHT) Color(0xFF0D9488) else Color(0xFF0284C7)
                                    } else {
                                        if (theme == GlobeTheme.GLASS_LIGHT) Color(0xFFF1F5F9) else Color(0xFF1E293B)
                                    }
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isCatSelected) Color.Transparent else (if (theme == GlobeTheme.GLASS_LIGHT) Color(0xFFCBD5E1) else Color(0xFF334155)),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable { selectedCategory = sec.id }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = sec.icon,
                                contentDescription = null,
                                tint = if (isCatSelected) Color.White else (if (theme == GlobeTheme.GLASS_LIGHT) Color(0xFF0D9488) else Color(0xFF38BDF8)),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = sec.title[activeLanguage]?.take(15) ?: sec.id,
                                fontSize = 11.sp,
                                fontWeight = if (isCatSelected) FontWeight.Black else FontWeight.SemiBold,
                                color = if (isCatSelected) Color.White else textColor
                            )
                        }
                    }
                }

                // MAIN CONTENT LIST
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(PlaybookDataProvider.sections) { sec ->
                        val isHighlighted = selectedCategory == sec.id

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = if (isHighlighted) 2.dp else 1.dp,
                                    color = if (isHighlighted) accent else (if (theme == GlobeTheme.GLASS_LIGHT) Color(0xFFE2E8F0) else Color(0xFF334155)),
                                    shape = RoundedCornerShape(18.dp)
                                ),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isHighlighted) {
                                    if (theme == GlobeTheme.GLASS_LIGHT) Color(0xFFFFF7ED) else Color(0xFF1A1D2E)
                                } else cardBg
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = if (isHighlighted) 6.dp else 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.padding(bottom = 6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isHighlighted) accent.copy(alpha = 0.15f)
                                                else (if (theme == GlobeTheme.GLASS_LIGHT) Color(0xFFF1F5F9) else Color(0xFF334155))
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = sec.icon,
                                            contentDescription = null,
                                            tint = if (isHighlighted) accent else (if (theme == GlobeTheme.GLASS_LIGHT) Color(0xFF0D9488) else Color(0xFF38BDF8)),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = sec.title[activeLanguage] ?: sec.title[AppLanguage.EN] ?: "",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Black,
                                            color = if (isHighlighted) accent else textColor
                                        )
                                        Text(
                                            text = sec.subtitle[activeLanguage] ?: sec.subtitle[AppLanguage.EN] ?: "",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = subTextColor
                                        )
                                    }
                                }

                                Divider(
                                    color = if (theme == GlobeTheme.GLASS_LIGHT) Color(0xFFF1F5F9) else Color(0xFF334155),
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )

                                val detailLines = sec.details[activeLanguage] ?: sec.details[AppLanguage.EN] ?: emptyList()
                                detailLines.forEach { line ->
                                    Text(
                                        text = line,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = textColor,
                                        lineHeight = 17.sp,
                                        modifier = Modifier.padding(vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // BOTTOM ACTION BAR
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(24.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accent
                    )
                ) {
                    val gotItText = when (activeLanguage) {
                        AppLanguage.EN -> "Got it, back to tournament! 🚀"
                        AppLanguage.CN -> "明白了，返回 3D 地球仪！🚀"
                        AppLanguage.ES -> "¡Entendido, volver al torneo! 🚀"
                        AppLanguage.JP -> "了解！大会画面に戻る 🚀"
                        AppLanguage.TH -> "เข้าใจแล้ว กลับสู่การแข่งขัน! 🚀"
                    }
                    Text(
                        text = gotItText,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}
