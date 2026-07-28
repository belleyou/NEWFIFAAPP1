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
                AppLanguage.EN to "1. 3D Globe & Tournament Overview",
                AppLanguage.CN to "1. 3D 地球仪与赛事概览",
                AppLanguage.ES to "1. Globo 3D y Visión General del Torneo",
                AppLanguage.JP to "1. 3D 地球儀 & 大会概要",
                AppLanguage.TH to "1. ลูกโลก 3D และภาพรวมการแข่งขัน"
            ),
            subtitle = mapOf(
                AppLanguage.EN to "Master rotating, zooming, and switching between Men's & Women's World Cups.",
                AppLanguage.CN to "掌握旋转、缩放以及男女足世界杯之间的快速切换。",
                AppLanguage.ES to "Aprende a girar, hacer zoom y alternar entre los Mundiales Masculino y Femenino.",
                AppLanguage.JP to "3D地球儀の操作、拡大縮小、男女ワールドカップの切り替え方法。",
                AppLanguage.TH to "เรียนรู้การหมุน ขยาย และสลับระหว่างฟุตบอลโลกชายและหญิง"
            ),
            details = mapOf(
                AppLanguage.EN to listOf(
                    "• Interactive 3D Earth: Drag anywhere on the globe with one finger to rotate and explore participating nations and host continents.",
                    "• Zoom Control: Use the bottom FIFA Zoom Slider (0.6x to 4.0x) or pinch-to-zoom to closely inspect venue cities and team bases.",
                    "• Tournament Switcher: Toggle between the FIFA 2026 Men's World Cup (USA, Canada, Mexico - 48 Teams) and FIFA 2027 Women's World Cup (Brazil - 32 Teams).",
                    "• Day / Night Themes: Tap the Sun/Moon pill button in the top right header to switch between Glass Light Mode and Cosmic Dark Space Mode.",
                    "• Country Pin Selection: Click any glowing national flag pin on the globe to open that team's full profile, FIFA ranking, and match history."
                ),
                AppLanguage.CN to listOf(
                    "• 3D 互动地球仪：用单指平滑拖拽球体，自由旋转探索各参赛国与举办大洲。",
                    "• 动态缩放控制：使用底部 FIFA 缩放滑块（0.6x 至 4.0x）或双指捏合，精确查看举办城市与球队基地。",
                    "• 世界杯赛程切换：在 2026 美加墨男子世界杯（48 支球队）与 2027 巴西女子世界杯（32 支球队）之间一键切换。",
                    "• 日间 / 夜间主题：点击右上角的日月胶囊按钮，在清新明亮模式与深空宇宙模式之间切换。",
                    "• 国旗大头针点选：点击地图上发光的国家旗帜大头针，即可打开该队的详细档案、FIFA 排名与战绩。"
                ),
                AppLanguage.ES to listOf(
                    "• Tierra 3D Interactiva: Arrastra en cualquier lugar del globo para girar y explorar los países participantes.",
                    "• Control de Zoom: Utiliza el deslizador de zoom de FIFA (0.6x a 4.0x) para inspeccionar de cerca las sedes y ciudades.",
                    "• Alternador de Torneo: Cambia entre la Copa Mundial Masculina FIFA 2026 (48 equipos) y la Copa Mundial Femenina FIFA 2027 (32 equipos).",
                    "• Temas Día / Noche: Toca el botón de Sol/Luna en la esquina superior derecha para cambiar entre modo Claro de Cristal y Oscuro Cósmico.",
                    "• Selección de Pines Nacionales: Haz clic en el pin de la bandera para abrir el perfil completo del equipo, su ranking FIFA y estadísticas."
                ),
                AppLanguage.JP to listOf(
                    "• インタラクティブ3D地球儀: ドラッグ操作で地球を回転させ、出場国や開催大陸を自由に探索できます。",
                    "• ズームコントロール: 下部のFIFAズームスライダー（0.6倍〜4.0倍）またはピンチイン/アウトで開催都市を拡大観察。",
                    "• 大会切り替え: 2026年男子ワールドカップ（48カ国）と2027年女子ワールドカップ（32カ国）をワンタップで切り替え。",
                    "• 昼/夜テーマ切替: 右上の太陽/月ボタンで、爽やかなグラスライトモードとコズミックダークモードを切り替えられます。",
                    "• 国旗ピンタップ: 地球儀上の発光する国旗ピンをタップすると、FIFAランキングや試合履歴などの詳細を開きます。"
                ),
                AppLanguage.TH to listOf(
                    "• โลก 3D แบบอินเทอร์แอคทีฟ: ลากนิ้วเพื่อหมุนลูกโลกและสำรวจประเทศที่เข้าร่วมการแข่งขันและทวีปเจ้าภาพ",
                    "• การควบคุมซูม: ใช้สไลเดอร์ซูม FIFA ด้านล่าง (0.6x ถึง 4.0x) เพื่อส่องดูเมืองเจ้าภาพและที่ตั้งสนามอย่างใกล้ชิด",
                    "• สลับการแข่งขัน: สลับระหว่าง ฟุตบอลโลกชาย 2026 (48 ทีม) และ ฟุตบอลโลกหญิง 2027 (32 ทีม) ได้ทันที",
                    "• ธีมกลางวัน/กลางคืน: แตะปุ่มดวงอาทิตย์/ดวงจันทร์ ที่มุมขวาบนเพื่อสลับระหว่างโหมดสว่าง Glass Light และโหมดอวกาศ Cosmic Dark",
                    "• หมุดธงชาติ: คลิกที่หมุดธงชาติบนลูกโลกเพื่อดูข้อมูลทีมอย่างละเอียด อันดับ FIFA และสถิติการแข่งขัน"
                )
            )
        ),
        PlaybookSection(
            id = "stages",
            icon = Icons.Default.EmojiEvents,
            title = mapOf(
                AppLanguage.EN to "2. Tournament Stages & Live FIFA Sync",
                AppLanguage.CN to "2. 赛事阶段与 FIFA 实时数据同步",
                AppLanguage.ES to "2. Etapas del Torneo y Sincronización FIFA en Vivo",
                AppLanguage.JP to "2. 大会ステージ & リアルタイムFIFA同期",
                AppLanguage.TH to "2. รอบการแข่งขันและการอัปเดตสดจาก FIFA"
            ),
            subtitle = mapOf(
                AppLanguage.EN to "Spin the 3D rotating stage drum and access auto-updated qualified teams.",
                AppLanguage.CN to "旋转 3D 轮盘切换阶段，实时获取官方最新晋级球队数据。",
                AppLanguage.ES to "Gira el tambor 3D de etapas y accede a los equipos clasificados actualizados automáticamente.",
                AppLanguage.JP to "3D回転ステージドラムを回して、自動更新される出場決定チームにアクセス。",
                AppLanguage.TH to "หมุนวงล้อ 3D เพื่อเปลี่ยนรอบการแข่งขัน พร้อมอัปเดตทีมที่ผ่านเข้ารอบโดยอัตโนมัติ"
            ),
            details = mapOf(
                AppLanguage.EN to listOf(
                    "• 3D Stage Drum: Tap left/right chevrons or swipe horizontally across the middle drum bar to rotate between tournament rounds.",
                    "• Supported Rounds: Access Qualified / All 32 / All 48, Round of 16, Quarter Finals, Semi Finals, Final, and Bronze Medal playoff.",
                    "• Real-Time FIFA Live Updates: Powered by Gemini AI background integration, qualified teams and tournament rosters are automatically synchronized live.",
                    "• 2027 Women's World Cup Qualified List: For the 2027 Women's World Cup in Brazil, 'Qualified' & 'All 32' show all 32 participating national teams, while future knockout rounds populate dynamically as real matches progress.",
                    "• Dynamic Globe Filtering: Selecting any stage automatically filters the pins on the 3D map to highlight active teams and venues."
                ),
                AppLanguage.CN to listOf(
                    "• 3D 旋转鼓转盘：点击左右箭头或在中间滚筒上左右滑动，轻松切换不同比赛阶段。",
                    "• 支持的比赛阶段：包括已出线/32强/48强、16强赛、四分之一决赛（8强）、半决赛（4强）、决赛及三四名决赛。",
                    "• 实时 FIFA 数据同步：由 Gemini AI 后台驱动，根据官方最新动态自动更新出线名单与最新对阵。",
                    "• 2027 女足世界杯实时数据：“Qualified”（已出线）与“All 32”（32强）全自动更新 32 支参赛球队，后续淘汰赛阶段随着真实赛事推进自动更新。",
                    "• 动态地图联动：选择任意比赛阶段后，3D 地球仪将自动过滤大头针，仅高亮展示该阶段参赛球队与场馆。"
                ),
                AppLanguage.ES to listOf(
                    "• Tambor 3D de Etapas: Toca los flechas o desliza horizontalmente en el tambor central para rotar entre las rondas del torneo.",
                    "• Rondas Soportadas: Clasificados / Todos 32 / Todos 48, Octavos de Final, Cuartos de Final, Semifinales, Final y Tercer Puesto.",
                    "• Sincronización FIFA en Tiempo Real: Impulsado por Gemini AI, los equipos clasificados se actualizan automáticamente en tiempo real.",
                    "• Lista de Clasificados Mundial Femenino 2027: Para el Mundial Femenino 2027 en Brasil, 'Clasificados' y 'Todos 32' muestran las 32 selecciones, mientras las rondas eliminatorias se actualizan según los resultados.",
                    "• Filtrado Dinámico en el Globo: Seleccionar una etapa filtra automáticamente los pines en el mapa 3D para destacar los equipos activos."
                ),
                AppLanguage.JP to listOf(
                    "• 3Dステージドラム: 左右の矢印をタップするか、中央ドラムを左右スワイプして大会ラウンドを回せます。",
                    "• 対応ラウンド: 出場決定 / 全32国 / 全48国、ベスト16、準々決勝（ベスト8）、準決勝（ベスト4）、決勝、3位決定戦。",
                    "• リアルタイムFIFA同期: Gemini AIバックエンド連携により、出場決定チームや対戦表が公式発表に合わせて自動ライブ更新されます。",
                    "• 2027女子W杯ライブ更新: ブラジル開催の2027女子W杯では、「Qualified」と「All 32」で全32チームが自動反映され、決勝トーナメントは実際の試合結果に応じて更新されます。",
                    "• ダイナミックマップフィルタ: ステージを選択すると、3D地球儀上のピンが自動フィルタリングされ、該当チームとスタジアムのみがハイライト表示されます。"
                ),
                AppLanguage.TH to listOf(
                    "• วงล้อรอบการแข่งขัน 3D: แตะลูกศรซ้าย/ขวา หรือปัดแนวนอนที่แท่งวงล้อกลางเพื่อสลับรอบการแข่งขัน",
                    "• รอบที่รองรับ: ทีมเข้ารอบ / ทั้งหมด 32 ทีม / 48 ทีม, รอบ 16 ทีม, รอบ 8 ทีม, รอบรองชนะเลิศ, รอบชิงชนะเลิศ และนัดชิงอันดับสาม",
                    "• การอัปเดตสดจาก FIFA: ขับเคลื่อนด้วย Gemini AI ในพื้นหลัง อัปเดตรายชื่อทีมที่ผ่านเข้ารอบตามการประกาศอย่างเป็นทางการของ FIFA แบบเรียลไทม์",
                    "• ฟุตบอลโลกหญิง 2027: แสดงรายชื่อ 32 ทีมชาติที่เข้าร่วมใน 'Qualified' และ 'All 32' ส่วนรอบน็อคเอาท์จะอัปเดตโดยอัตโนมัติตามผลการแข่งขันจริง",
                    "• การกรองหมุดบนลูกโลก: เมื่อเลือกโบว์ลรอบการแข่งขัน หมุดบนลูกโลก 3D จะกรองเน้นเฉพาะทีมและสนามที่แข่งขันในรอบนั้น"
                )
            )
        ),
        PlaybookSection(
            id = "compare",
            icon = Icons.Default.CompareArrows,
            title = mapOf(
                AppLanguage.EN to "3. VS Tactical Compare & AI Decides For Me",
                AppLanguage.CN to "3. VS 战术对比与 AI 智能预测引擎",
                AppLanguage.ES to "3. Comparador Táctico VS y Predicción por AI",
                AppLanguage.JP to "3. VS 戰術比較 & AI 判定エンジン",
                AppLanguage.TH to "3. การเปรียบเทียบเชิงแทคติก VS และ AI วิเคราะห์"
            ),
            subtitle = mapOf(
                AppLanguage.EN to "Compare national teams side-by-side and run Gemini AI win predictions.",
                AppLanguage.CN to "双队并排深度战术对比，一键运行 Gemini AI 胜率预测与战术拆解。",
                AppLanguage.ES to "Compara selecciones nacionales frente a frente y ejecuta predicciones de victoria con Gemini AI.",
                AppLanguage.JP to "2つの代表チームを横一列で詳細比較し、Gemini AIによる勝率予測を実行できます。",
                AppLanguage.TH to "เปรียบเทียบทีมชาติแบบเคียงข้างกัน และใช้ Gemini AI ในการทำนายผลและวิเคราะห์เชิงลึก"
            ),
            details = mapOf(
                AppLanguage.EN to listOf(
                    "• Opening Compare Drawer: Tap the top-left 'VS' logo button to open the side-by-side Comparison Drawer.",
                    "• Select Teams: Pick any two qualified nations from the dropdown list to compare FIFA ranking, win count, goals scored, and clean sheets.",
                    "• 'AI Decides For Me': Tap the gradient AI button to activate Gemini AI analysis.",
                    "• Real-Time Match Simulation: AI analyzes head-to-head history, squad strength, player form, and key matchups to calculate win percentages and score predictions.",
                    "• Detailed Tactical Breakdown: View expected formations, tactical strengths, weakness exploits, and key player duels."
                ),
                AppLanguage.CN to listOf(
                    "• 打开对比抽屉：点击顶部左侧的“VS”Logo按钮，即可展开双队战术对比面板。",
                    "• 选择球队：从下拉菜单中挑选任意两支参赛国家队，并排对比 FIFA 排名、胜率、进球数、零封场次与控球率。",
                    "• “AI 帮我决定”功能：点击 AI 渐变按钮，触发 Google Gemini 深度智能分析。",
                    "• 实时比赛模拟：AI 结合两队历史对决、近期状态、球星阵型与关键位置较量，计算出获胜概率与预测比分。",
                    "• 深度战术拆解：获取详细的战术阵型建议、防线破绽分析与核心球员对位亮点。"
                ),
                AppLanguage.ES to listOf(
                    "• Abrir el Panel de Comparación: Toca el botón de logo 'VS' en la esquina superior izquierda para desplegar el comparador.",
                    "• Seleccionar Equipos: Elige dos selecciones de la lista desplegable para comparar ranking FIFA, victorias, goles anotados y arcos en cero.",
                    "• 'AI Decides For Me': Haz clic en el botón con degradado de AI para activar el análisis inteligente de Gemini AI.",
                    "• Simulación en Tiempo Real: La IA analiza el historial, la forma de los jugadores y enfrentamientos clave para calcular probabilidades y marcadores.",
                    "• Desglose Táctico Detallado: Consulta las alineaciones previstas, puntos fuertes, debilidades y duelos individuales clave."
                ),
                AppLanguage.JP to listOf(
                    "• 比較ドロワーを開く: トップ左上の「VS」ロゴボタンをタップすると、2チーム比較ドロワーが開きます。",
                    "• チーム選択: ドロップダウンから任意の2カ国を選択し、FIFAランキング、勝利数、総得点、クリーンシート、平均支配率を比較できます。",
                    "• 「AI Decides For Me」: グラデーション状のAIボタンをタップすると、Gemini AIによるリアルタイム勝敗分析がスタートします。",
                    "• リアルタイム試合シミュレーション: 対戦履歴、選手フォーム、キーマッチアップをAIが総合分析し、勝率パーセンテージと予測スコアを算出。",
                    "• 詳細戦術アナリシス: 予想フォーメーション、戦術的強み、弱点攻略法、注目選手の対决ポイントを確認できます。"
                ),
                AppLanguage.TH to listOf(
                    "• เปิดลิ้นชักการเปรียบเทียบ: แตะปุ่มโลโก้ 'VS' ที่มุมซ้ายบนเพื่อเปิดแผงเปรียบเทียบทีมแบบเคียงข้างกัน",
                    "• เลือกทีม: เลือกสองทีมชาติจากรายการเพื่อเปรียบเทียบอันดับ FIFA, จำนวนนัดที่ชนะ, ประตูที่ทำได้, คลีนชีต และสถิติการครองบอล",
                    "• ปุ่ม 'AI Decides For Me': คลิกปุ่ม AI เพื่อเปิดใช้งานการวิเคราะห์อัจฉริยะจาก Google Gemini AI",
                    "• การจำลองผลการแข่งขันแบบเรียลไทม์: AI วิเคราะห์ประวัติการพบกัน ฟอร์มผู้เล่น และการประกบตัวสำคัญเพื่อคำนวณโอกาสชนะและสกอร์คาดการณ์",
                    "• บทวิเคราะห์เชิงแทคติกแบบเจาะลึก: ดูแผนการเล่นที่คาดการณ์ จุดแข็งเชิงยุทธศาสตร์ และจุดอ่อนของคู่แข่ง"
                )
            )
        ),
        PlaybookSection(
            id = "stadiums",
            icon = Icons.Default.Place,
            title = mapOf(
                AppLanguage.EN to "4. Host Stadiums & City Directory",
                AppLanguage.CN to "4. 主办体育场与举办城市指南",
                AppLanguage.ES to "4. Estadios Sede y Directorio de Ciudades",
                AppLanguage.JP to "4. 開催スタジアム & 都市ガイド",
                AppLanguage.TH to "4. สนามแข่งขันและเมืองเจ้าภาพ"
            ),
            subtitle = mapOf(
                AppLanguage.EN to "Explore all host venues, capacities, architectural trivia, and 3D map camera jump.",
                AppLanguage.CN to "探索所有比赛场馆、容纳人数、建筑特色，并支持 3D 地球镜头一键定位。",
                AppLanguage.ES to "Explora todas las sedes, capacidades, datos arquitectónicos y salto de cámara 3D en el mapa.",
                AppLanguage.JP to "全開催スタジアム、収容人数、建築トリビアの確認、3D地球カメラのワンタップジャンプ機能。",
                AppLanguage.TH to "สำรวจสนามแข่งขันทั้งหมด ความจุข้อมูลสถาปัตยกรรม และการวาร์ปกล้อง 3D ไปยังสนามนั้นๆ"
            ),
            details = mapOf(
                AppLanguage.EN to listOf(
                    "• Opening Stadiums Sheet: Click the green Location Pin icon button in the header deck (placed right next to the Playbook button).",
                    "• Complete Venue Roster: View all 16 host venues for the 2026 Men's World Cup (e.g., MetLife Stadium, Estadio Azteca, SoFi Stadium) and 2027 Women's World Cup venues in Brazil (Maracanã, Mané Garrincha, Neo Química Arena, Mineirão, etc.).",
                    "• Stadium Stats & Trivia: Access exact seating capacities, host city weather badges, historical facts, and scheduled key matches.",
                    "• 'View on Map' Camera Fly-To: Tap 'View on Map' on any stadium card to automatically rotate and zoom the 3D Earth directly to that stadium's coordinates."
                ),
                AppLanguage.CN to listOf(
                    "• 打开场馆面板：点击顶部浮动控制栏中的绿色定位大头针图标按钮（紧邻本 Playbook 按钮左侧）。",
                    "• 完整场馆列表：浏览 2026 美加墨男足世界杯所有 16 座主办场馆（如大都会体育场、阿兹特克体育场、SoFi 体育场等）以及 2027 巴西女足世界杯主办场馆（马拉卡纳体育场、加林查体育场、新哥林多体育场等）。",
                    "• 场馆参数与建筑故事：查看精确容纳人数、当地天气指示、历史趣闻与承办的关键对决。",
                    "• “在地图上查看”一键飞跃：点击场馆卡片上的“View on Map”，3D 地球仪镜头将自动流畅旋转并放大至该体育场的精确地理坐标。"
                ),
                AppLanguage.ES to listOf(
                    "• Abrir el Panel de Estadios: Haz clic en el botón verde con el icono de Pin de Ubicación en el panel superior.",
                    "• Directorio Completo de Sedes: Consulta los 16 estadios del Mundial 2026 y los estadios del Mundial Femenino 2027 en Brasil (Maracaná, Mané Garrincha, Neo Química Arena, etc.).",
                    "• Estadísticas y Trivia de Estadios: Accede a la capacidad exacta de asientos, clima local, datos históricos y partidos programados.",
                    "• Volar al Mapa: Toca 'View on Map' en cualquier tarjeta de estadio para girar y acercar la cámara 3D directamente a sus coordenadas."
                ),
                AppLanguage.JP to listOf(
                    "• スタジアムシートを開く: ヘッダー右側の緑色ロケーションピンアイコン（本Playbookボタンの右隣）をタップ。",
                    "• 全開催スタジアム一覧: 2026男子W杯の全16会場（メットライフ、アステカ、SoFi等）および2027女子W杯ブラジル会場（マラカナン、マネ・ガリンシャ、ネオ・キミカ・アレーナ等）を一覧閲覧できます。",
                    "• キャパシティ & 建築トリビア: 正確な収容人数、現地リアルタイム天候バッジ、歴史的エピソード、開催予定試合をチェック。",
                    "• 「View on Map」カメラ飛躍: スタジアムカードの「View on Map」をタップすると、3D地球儀カメラが滑らかに回転し、該当スタジアムの位置へ即座にズームインします。"
                ),
                AppLanguage.TH to listOf(
                    "• เปิดแผงข้อมูลสนาม: คลิกที่ปุ่มไอคอนหมุดสถานที่สีเขียว ที่อยู่ข้างปุ่ม Playbook นี้",
                    "• รายชื่อสนามทั้งหมด: ดูข้อมูลสนามแข่งขันทั้ง 16 แห่งของฟุตบอลโลกชาย 2026 และสนามแข่งขันฟุตบอลโลกหญิง 2027 ในบราซิล (มารากาน่า, มาเน่ การินชา, นีโอ กีมิก้า อารีนา ฯลฯ)",
                    "• สถิติและความจุ: ดูความจุที่นั่งที่แม่นยำ สภาพอากาศในพื้นที่ สภาพสถาปัตยกรรม และแมตช์สำคัญที่จัดขึ้น",
                    "• ปุ่ม 'View on Map': แตะปุ่มดูบนแผนที่ในการ์ดสนามใดก็ได้ กล้อง 3D บนลูกโลกจะหมุนและขยายไปยังพิกัดสนามนั้นทันที"
                )
            )
        ),
        PlaybookSection(
            id = "schedules",
            icon = Icons.Default.Event,
            title = mapOf(
                AppLanguage.EN to "5. Match Schedules, Lineups & Smart Alerts",
                AppLanguage.CN to "5. 赛程日历、首发阵容与 30 分钟智能提醒",
                AppLanguage.ES to "5. Calendario de Partidos, Alineaciones y Alertas",
                AppLanguage.JP to "5. 試合日程・スタメン & 30分前リマインダー通知",
                AppLanguage.TH to "5. ตารางการแข่งขัน รายชื่อผู้เล่น และการแจ้งเตือน"
            ),
            subtitle = mapOf(
                AppLanguage.EN to "Local time kickoff schedules, player lists, and automated 30-minute pre-match alarms.",
                AppLanguage.CN to "自动匹配当地时区开球时间，查看球星阵容并开启开赛前 30 分钟系统提醒。",
                AppLanguage.ES to "Horarios adaptados a tu zona horaria local, alineaciones y alarmas automáticas 30 minutos antes del partido.",
                AppLanguage.JP to "現地タイムゾーン自動調整のキックオフ日程、選手リスト、試合開始30分前の自動アラーム機能。",
                AppLanguage.TH to "ตารางเวลาแข่งที่ปรับตามเวลาท้องถิ่น รายชื่อนักเตะ และระบบการแจ้งเตือนล่วงหน้า 30 นาที"
            ),
            details = mapOf(
                AppLanguage.EN to listOf(
                    "• Team Overview & Profiles: Click any nation to access 4 primary tabs: Overview, Squad & Key Players, Stats Gauges, and Match Schedule.",
                    "• Timezone Auto-Localization: Match kickoff times automatically parse and display in your device's local timezone.",
                    "• Smart Notification Bell: Tap the bell icon next to any upcoming fixture to set a background alarm.",
                    "• 30-Minute Kickoff Alert: System posts an automated push notification exactly 30 minutes before match kickoff so you never miss a kickoff.",
                    "• Key Player Breakdown: View market values, club teams, goals, and injury updates for star players."
                ),
                AppLanguage.CN to listOf(
                    "• 球队概览面板：点击任意国家队即可打开 4 个核心标签页：Overview（概览）、Squad（阵容）、Stats（数据）与 Schedule（赛程日历）。",
                    "• 时区自动转换：所有开球时间均根据您设备的本地时区自动解析并精准显示。",
                    "• 智能开赛提醒铃铛：点击赛程列表右侧的铃铛图标，即可一键预约后台闹钟提醒。",
                    "• 开赛前 30 分钟推播：系统将在比赛开球前 30 分钟准时发送本地通知，确保您不错过任何一场精彩角逐。",
                    "• 核心球星情报：查看球星身价、所属俱乐部、进球表现与最新伤病状态。"
                ),
                AppLanguage.ES to listOf(
                    "• Perfiles de Equipo: Haz clic en cualquier selección para acceder a 4 pestañas: Resumen, Plantilla, Estadísticas y Calendario.",
                    "• Conversión Horaria Automática: Las horas de inicio se adaptan automáticamente a la hora local de tu dispositivo.",
                    "• Campana de Notificación Inteligente: Toca la campana junto a cualquier partido para activar una alarma en segundo plano.",
                    "• Alerta 30 Minutos Antes: El sistema envía una notificación 30 minutos antes del saque inicial.",
                    "• Información de Jugadores Estrella: Consulta valores de mercado, clubes, goles y partes de lesiones."
                ),
                AppLanguage.JP to listOf(
                    "• チーム詳細プロファイル: 国旗をタップすると「Overview（概要）」「Squad（選手）」「Stats（統計）」「Schedule（日程）」の4タブを切り替えられます。",
                    "• タイムゾーン自動調整: 全試合のキックオフ時刻は、お使いの端末のローカルタイムゾーンに自動変換して表示されます。",
                    "• 智能リマインダーベル: 日程リストのベルアイコンをタップすると、試合リマインダー通知をワンタップ登録。",
                    "• 試合開始30分前アラート: キックオフ30分前にプッシュ通知を自動送信。注目のキックオフを見逃しません。",
                    "• スター選手アナリシス: 注目選手の市場価値、所属クラブ、得点数、怪我情報をリアルタイムチェック。"
                ),
                AppLanguage.TH to listOf(
                    "• ข้อมูลทีมอย่างละเอียด: คลิกที่ประเทศใดก็ได้เพื่อเข้าถึง 4 แท็บหลัก: ภาพรวม (Overview), รายชื่อนักเตะ (Squad), สถิติ (Stats) และตารางแข่ง (Schedule)",
                    "• ปรับเวลาตามเวลาท้องถิ่น: เวลาคิกออฟจะปรับแสดงผลตามเขตเวลาในเครื่องของคุณโดยอัตโนมัติ",
                    "• ปุ่มกระดิ่งแจ้งเตือน: แตะไอคอนกระดิ่งข้างแมตช์ที่กำลังจะมาถึงเพื่อตั้งเวลาการแจ้งเตือน",
                    "• แจ้งเตือนล่วงหน้า 30 นาที: ระบบจะส่งการแจ้งเตือนล่วงหน้า 30 นาทีก่อนเริ่มแข่งขันทันที",
                    "• ข้อมูลนักเตะดาวดัง: ดูมูลค่าการตลาด สโมสร ประตูที่ทำได้ และสถานะการบาดเจ็บของนักเตะ"
                )
            )
        ),
        PlaybookSection(
            id = "favorites",
            icon = Icons.Default.Star,
            title = mapOf(
                AppLanguage.EN to "6. Favorite Teams Deck & Quick Access Shortcuts",
                AppLanguage.CN to "6. 收藏球队栏目与快捷访问置顶",
                AppLanguage.ES to "6. Equipos Favoritos y Accesos Rápidos",
                AppLanguage.JP to "6. お気に入りチームデッキ & クイックショートカット",
                AppLanguage.TH to "6. แผงทีมโปรดและการเข้าถึงด่วน"
            ),
            subtitle = mapOf(
                AppLanguage.EN to "Pin your favorite nations to the top deck for quick camera jump and status updates.",
                AppLanguage.CN to "将心仪的国家队置顶至顶部栏目，实现镜头一键定位与实时动态接收。",
                AppLanguage.ES to "Fija tus selecciones favoritas en la barra superior para saltar rápido con la cámara y ver sus novedades.",
                AppLanguage.JP to "お気に入りの代表国をトップデッキに固定し、ワンタップでカメラ移動 & 最新情報をチェック。",
                AppLanguage.TH to "ปักหมุดทีมชาติโปรดไว้ที่แผงด้านบนเพื่อการวาร์ปกล้องและรับข่าวสารอย่างรวดเร็ว"
            ),
            details = mapOf(
                AppLanguage.EN to listOf(
                    "• Star Pinning: Open any team's card and click the star icon in the header to mark them as a Favorite Team.",
                    "• Top Deck Access: All pinned teams appear as high-resolution quick-access badges right below the main header console.",
                    "• Instant Camera Fly-To: Tap any favorite team badge in the top deck to instantly fly the 3D Earth camera directly to that country on the globe.",
                    "• Priority Match Feeds: Favorited teams automatically prioritize their match schedules and notify you about upcoming fixtures."
                ),
                AppLanguage.CN to listOf(
                    "• 星号一键收藏：打开任意球队卡片，点击右上角的星号图标，即可将该队设为“收藏球队”。",
                    "• 顶部快捷栏目：所有已收藏球队将以高精国旗徽章的形式，集中展示在顶部主控制台下方。",
                    "• 镜头瞬间飞跃：直接点击顶部栏中的收藏球队徽章，3D 地球仪镜头将瞬间旋转并缩放至该国的地理位置。",
                    "• 优先赛程推送：收藏球队将享受优先赛程展示，并在比赛即将到来时及时提醒您。"
                ),
                AppLanguage.ES to listOf(
                    "• Marcado con Estrella: Abre la tarjeta de cualquier equipo y toca la estrella para añadirlo a Favoritos.",
                    "• Acceso en la Barra Superior: Los equipos favoritos aparecen como insignias de acceso rápido debajo del panel principal.",
                    "• Vuelo de Cámara Instantáneo: Toca la insignia de un equipo favorito para volar la cámara 3D directamente a ese país.",
                    "• Notificaciones Prioritarias: Recibe avisos prioritarios sobre los partidos y novedades de tus equipos favoritos."
                ),
                AppLanguage.JP to listOf(
                    "• 星アイコンでお気に入り登録: チームカードを開き、ヘッダーの星アイコンをタップしてお気に入りに追加。",
                    "• トップデッキ表示: 固定されたチームは、メインコンソール直下にアイコンバッジとしてクイック表示されます。",
                    "• カメラ移動の即時飛躍: トップデッキのお気に入り国旗バッジをタップすると、3D地球儀カメラがその国へ直接ジャンプします。",
                    "• 優先日程フィーダー: お気に入りチームの試合日程や結果が優先表示され、通知もスムーズに届きます。"
                ),
                AppLanguage.TH to listOf(
                    "• การปักหมุดดาว: เปิดการ์ดข้อมูลทีมใดก็ได้ แล้วคลิกที่ไอคอนดาวเพื่อเพิ่มเป็นทีมโปรด",
                    "• การเข้าถึงที่แผงด้านบน: ทีมโปรดทั้งหมดจะปรากฏเป็นตราสัญลักษณ์ด่วนใต้คอนโซลหลัก",
                    "• การวาร์ปกล้องทันที: แตะที่ตราสัญลักษณ์ทีมโปรดเพื่อหมุนกล้อง 3D บนลูกโลกไปยังประเทศนั้นทันที",
                    "• การเตือนแมตช์ความสำคัญสูง: ทีมโปรดจะได้รับความสำคัญในการแสดงตารางแข่งและการแจ้งเตือนล่วงหน้า"
                )
            )
        ),
        PlaybookSection(
            id = "language",
            icon = Icons.Default.Language,
            title = mapOf(
                AppLanguage.EN to "7. Multilingual Support (EN, 中文, ES, JP, TH)",
                AppLanguage.CN to "7. 全球多语言即时切换 (英文, 中文, 西班牙文, 日文, 泰文)",
                AppLanguage.ES to "7. Soporte Multilingüe (EN, 中文, ES, JP, TH)",
                AppLanguage.JP to "7. 多言語サポート (英語, 中国語, スペイン語, 日本語, タイ語)",
                AppLanguage.TH to "7. การรองรับหลายภาษา (อังกฤษ, จีน, สเปน, ญี่ปุ่น, ไทย)"
            ),
            subtitle = mapOf(
                AppLanguage.EN to "Seamlessly switch interface language across 5 global languages.",
                AppLanguage.CN to "可在英语、中文、西班牙语、日语与泰语 5 种语言之间实现无缝实时切换。",
                AppLanguage.ES to "Cambia el idioma de la interfaz al instante entre 5 idiomas globales.",
                AppLanguage.JP to "5つのグローバル言語間でUIや解説文を即座にシームレス切り替え。",
                AppLanguage.TH to "สลับภาษาของแอปพลิเคชันได้ทันที 5 ภาษาทั่วโลก"
            ),
            details = mapOf(
                AppLanguage.EN to listOf(
                    "• Language Button Location: Tap the language code button ('EN', 'CN', 'ES', 'JP', 'TH') located at the right side of the location icon button.",
                    "• 5 Full Global Languages: Select between English (EN), Chinese 中文 (CN), Spanish Español (ES), Japanese 日本語 (JP), and Thai ไทย (TH).",
                    "• Complete Interface Translation: Instantly translates all stage labels, team stats, stadium guides, tactical AI predictions, match dates, and this Playbook itself."
                ),
                AppLanguage.CN to listOf(
                    "• 语言按钮位置：点击位于定位图标按钮右侧的语言代码按钮（'EN'、'CN'、'ES'、'JP'、'TH'）。",
                    "• 支持 5 大全球语言：可在 English (EN)、中文 (CN)、Español (ES)、日本語 (JP) 和 ไทย (TH) 之间随心切换。",
                    "• 界面全方位翻译：即时翻译所有比赛阶段标签、球队统计数据、场馆指南、AI 战术预测、开球日期以及本 Playbook 指南本身。"
                ),
                AppLanguage.ES to listOf(
                    "• Ubicación del Botón de Idioma: Toca el botón de código de idioma ('EN', 'CN', 'ES', 'JP', 'TH') ubicado a la derecha del icono de ubicación.",
                    "• 5 Idiomas Globales: Elige entre Inglés (EN), Chino 中文 (CN), Español (ES), Japonés 日本語 (JP) y Tailandés ไทย (TH).",
                    "• Traducción Completa de la Interfaz: Traduce al instante todas las etiquetas, estadísticas, guías de estadios, predicciones por IA y este Playbook."
                ),
                AppLanguage.JP to listOf(
                    "• 言語ボタンの位置: ロケーションアイコンの右隣にある言語コードボタン（'EN', 'CN', 'ES', 'JP', 'TH'）をタップ。",
                    "• 全5言語対応: 英語 (EN)、中国語 (CN)、スペイン語 (ES)、日本語 (JP)、タイ語 (TH) から自在に選択できます。",
                    "• 全画面インスタント翻訳: ステージ名、チーム統計、スタジアム情報、AI対戦予測、試合日時、そして本Playbook解説文まで全翻訳されます。"
                ),
                AppLanguage.TH to listOf(
                    "• ตำแหน่งปุ่มเลือกภาษา: แตะที่ปุ่มรหัสภาษา ('EN', 'CN', 'ES', 'JP', 'TH') ที่อยู่ด้านขวาของไอคอนปุ่มสถานที่",
                    "• รองรับ 5 ภาษาหลัก: เลือกได้ระหว่าง อังกฤษ (EN), จีน (CN), สเปน (ES), ญี่ปุ่น (JP) และ ไทย (TH)",
                    "• แปลภาษาครอบคลุมทั้งแอป: แปลฉลากรอบการแข่งขัน สถิติติข้อมูลสนาม การวิเคราะห์ AI วันเวลาแข่ง และคู่มือ Playbook นี้โดยทันที"
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
