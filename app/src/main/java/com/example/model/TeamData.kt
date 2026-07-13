package com.example.model

data class Player(
    val name: String,
    val position: String,
    val number: Int,
    val description: String
)

data class Injury(
    val name: String,
    val injuryType: String,
    val returnEstimate: String
)

data class Stadium(
    val name: String,
    val city: String,
    val capacity: String,
    val latitude: Double,
    val longitude: Double,
    val weatherTemp: String,
    val weatherCondition: String
)

data class TournamentStats(
    val goalsScored: Int,
    val wins: Int,
    val possessionPercent: Int,
    val shotsOnTarget: Int,
    val cleanSheets: Int
)

data class Match(
    val opponent: String,
    val date: String,
    val time: String,
    val stadium: Stadium
)

data class Team(
    val name: String,
    val abbreviation: String,
    val flag: String,
    val fifaRanking: Int,
    val profile: String,
    val coach: String,
    val latitude: Double,
    val longitude: Double,
    val form: List<String>, // e.g. ["W", "W", "D", "L", "W"]
    val stats: TournamentStats,
    val keyPlayers: List<Player>,
    val injuries: List<Injury>,
    val nextMatch: Match,
    val path: List<String> // Road to final results: e.g. ["R32: Won 3-0 vs Poland", "R16: Won 2-1 vs Denmark"]
)

object TeamDataProvider {
    private val detailedTeams = listOf(
        Team(
            name = "Argentina",
            abbreviation = "ARG",
            flag = "🇦🇷",
            fifaRanking = 1,
            profile = "Defending world champions boasting an elite squad that seamlessly blends legendary experience with high-pressing youth. Known for their tactical mastery and fluid transitions.",
            coach = "Lionel Scaloni",
            latitude = -34.6037,
            longitude = -58.3816,
            form = listOf("W", "W", "W", "D", "W"),
            stats = TournamentStats(goalsScored = 14, wins = 4, possessionPercent = 62, shotsOnTarget = 18, cleanSheets = 3),
            keyPlayers = listOf(
                Player("Lionel Messi", "FW", 10, "The GOAT. Legendary playmaker and captain with unmatched vision and set-piece lethalness."),
                Player("Alexis Mac Allister", "MF", 20, "Midfield engine. Controls tempo, recovers balls, and makes late runs into the box."),
                Player("Emiliano Martínez", "GK", 23, "Elite shot-stopper and penalty shootout specialist with immense presence.")
            ),
            injuries = listOf(
                Injury("Lisandro Martínez", "DF - Knee Sprain", "Return: 2 weeks")
            ),
            nextMatch = Match(
                opponent = "France",
                date = "July 4, 2026",
                time = "18:00 Local",
                stadium = Stadium(
                    name = "SoFi Stadium",
                    city = "Los Angeles, USA",
                    capacity = "70,240",
                    latitude = 33.9535,
                    longitude = -118.3390,
                    weatherTemp = "74°F",
                    weatherCondition = "Sunny & Clear"
                )
            ),
            path = listOf(
                "Round of 32: Won 3-0 vs Poland",
                "Round of 16: Won 2-1 vs Denmark",
                "Quarter-Final: vs France"
            )
        ),
        Team(
            name = "France",
            abbreviation = "FRA",
            flag = "🇫🇷",
            fifaRanking = 2,
            profile = "Les Bleus are a physical powerhouse possessing blinding speed on the wings, robust central control, and a rock-solid defense designed for devastating counter-attacks.",
            coach = "Didier Deschamps",
            latitude = 48.8566,
            longitude = 2.3522,
            form = listOf("W", "W", "D", "W", "W"),
            stats = TournamentStats(goalsScored = 13, wins = 4, possessionPercent = 58, shotsOnTarget = 16, cleanSheets = 2),
            keyPlayers = listOf(
                Player("Kylian Mbappé", "FW", 10, "Captain and superstar. Blinding pace, lethal cutting-in from the left, and clinical finishing."),
                Player("Antoine Griezmann", "MF", 7, "Tactical mastermind. Orchestrates plays, links defense to attack, and is highly versatile."),
                Player("William Saliba", "DF", 4, "Calm, physically dominant defender. Elite reading of the game and tackles with precision.")
            ),
            injuries = listOf(
                Injury("Aurelien Tchouameni", "MF - Calf Strain", "Return: 5 days")
            ),
            nextMatch = Match(
                opponent = "Argentina",
                date = "July 4, 2026",
                time = "18:00 Local",
                stadium = Stadium(
                    name = "SoFi Stadium",
                    city = "Los Angeles, USA",
                    capacity = "70,240",
                    latitude = 33.9535,
                    longitude = -118.3390,
                    weatherTemp = "74°F",
                    weatherCondition = "Sunny & Clear"
                )
            ),
            path = listOf(
                "Round of 32: Won 2-0 vs Ukraine",
                "Round of 16: Won 3-1 vs Ecuador",
                "Quarter-Final: vs Argentina"
            )
        ),
        Team(
            name = "Spain",
            abbreviation = "ESP",
            flag = "🇪🇸",
            fifaRanking = 3,
            profile = "Tiki-Taka evolved. Spain couples relentless vertical winger speed with standard majestic midfield ball retention and high counter-pressing.",
            coach = "Luis de la Fuente",
            latitude = 40.4168,
            longitude = -3.7038,
            form = listOf("W", "W", "W", "W", "D"),
            stats = TournamentStats(goalsScored = 15, wins = 4, possessionPercent = 65, shotsOnTarget = 19, cleanSheets = 4),
            keyPlayers = listOf(
                Player("Lamine Yamal", "FW", 19, "Winger prodigy. Gifted dribbler, explosive acceleration, and creative crossing."),
                Player("Rodri", "MF", 16, "Ballon d'Or winner. Anchor of the midfield, absolute elite passing, and physical dominance."),
                Player("Pedri", "MF", 8, "Creative maestro. Finds space in tight pockets and delivers incisive line-breaking passes.")
            ),
            injuries = listOf(
                Injury("Gavi", "MF - ACL Rehab", "Return: Late July")
            ),
            nextMatch = Match(
                opponent = "Brazil",
                date = "July 5, 2026",
                time = "20:00 Local",
                stadium = Stadium(
                    name = "MetLife Stadium",
                    city = "East Rutherford, USA",
                    capacity = "82,500",
                    latitude = 40.8135,
                    longitude = -74.0743,
                    weatherTemp = "78°F",
                    weatherCondition = "Partly Cloudy"
                )
            ),
            path = listOf(
                "Round of 32: Won 4-1 vs Switzerland",
                "Round of 16: Won 1-0 vs Japan",
                "Quarter-Final: vs Brazil"
            )
        ),
        Team(
            name = "Brazil",
            abbreviation = "BRA",
            flag = "🇧🇷",
            fifaRanking = 5,
            profile = "O Jogo Bonito with a modern high-speed spin. Brazil plays with intense offensive flair and high creative freedom, supported by aggressive fullbacks.",
            coach = "Dorival Júnior",
            latitude = -15.7938,
            longitude = -47.8827,
            form = listOf("W", "W", "L", "W", "W"),
            stats = TournamentStats(goalsScored = 12, wins = 4, possessionPercent = 60, shotsOnTarget = 17, cleanSheets = 2),
            keyPlayers = listOf(
                Player("Vinícius Júnior", "FW", 7, "Flamboyant and electrifying. Decisive in 1v1 situations with incredible acceleration."),
                Player("Rodrygo", "FW", 10, "Intelligent forward with fantastic positional flexibility and ice-cold composure."),
                Player("Bruno Guimarães", "MF", 5, "Relentless mid-block presser and ball winner with highly direct passing style.")
            ),
            injuries = listOf(
                Injury("Neymar Jr", "FW - Recovery Training", "Return: Mid-July")
            ),
            nextMatch = Match(
                opponent = "Spain",
                date = "July 5, 2026",
                time = "20:00 Local",
                stadium = Stadium(
                    name = "MetLife Stadium",
                    city = "East Rutherford, USA",
                    capacity = "82,500",
                    latitude = 40.8135,
                    longitude = -74.0743,
                    weatherTemp = "78°F",
                    weatherCondition = "Partly Cloudy"
                )
            ),
            path = listOf(
                "Round of 32: Won 3-1 vs Chile",
                "Round of 16: Won 4-2 vs South Korea",
                "Quarter-Final: vs Spain"
            )
        ),
        Team(
            name = "England",
            abbreviation = "ENG",
            flag = "🏴󠁧󠁢󠁥󠁮󠁧󠁿",
            fifaRanking = 4,
            profile = "A highly organized and balanced roster of global superstars. England plays structured, set-piece heavy, defensively secure tournament football.",
            coach = "Thomas Tuchel",
            latitude = 51.5074,
            longitude = -0.1278,
            form = listOf("W", "D", "W", "W", "W"),
            stats = TournamentStats(goalsScored = 11, wins = 3, possessionPercent = 56, shotsOnTarget = 14, cleanSheets = 3),
            keyPlayers = listOf(
                Player("Jude Bellingham", "MF", 10, "Dynamic box-to-box powerhouse. Scores clutch goals and dominates physical duels."),
                Player("Harry Kane", "FW", 9, "All-time legendary forward. Perfect link play and elite clinical finishing."),
                Player("Bukayo Saka", "FW", 7, "Brilliant right-wing winger. Direct dribbling, high creativity, and work rate.")
            ),
            injuries = listOf(
                Injury("Luke Shaw", "DF - Hamstring Tightness", "Return: 3 days")
            ),
            nextMatch = Match(
                opponent = "USA",
                date = "July 5, 2026",
                time = "17:00 Local",
                stadium = Stadium(
                    name = "Hard Rock Stadium",
                    city = "Miami, USA",
                    capacity = "64,767",
                    latitude = 25.9580,
                    longitude = -80.2389,
                    weatherTemp = "82°F",
                    weatherCondition = "Humid & Showers"
                )
            ),
            path = listOf(
                "Round of 32: Won 2-1 vs Austria",
                "Round of 16: Won 2-0 vs Senegal",
                "Quarter-Final: vs USA"
            )
        ),
        Team(
            name = "USA",
            abbreviation = "USA",
            flag = "🇺🇸",
            fifaRanking = 16,
            profile = "The host golden generation. Fueled by passionate home stadium support, Pochettino's squad utilizes extreme high-energy pressing and quick vertical transitions.",
            coach = "Mauricio Pochettino",
            latitude = 38.9072,
            longitude = -77.0369,
            form = listOf("W", "W", "D", "D", "W"),
            stats = TournamentStats(goalsScored = 9, wins = 3, possessionPercent = 54, shotsOnTarget = 12, cleanSheets = 2),
            keyPlayers = listOf(
                Player("Christian Pulisic", "FW", 10, "Captain America. Elite dynamic winger, clinical finisher, and national leader."),
                Player("Weston McKennie", "MF", 8, "High-energy midfielder. Superb aerial threat and robust defensive recovery."),
                Player("Antonee Robinson", "DF", 3, "Jedi. Speedster left-back with relentless overlapping runs and crosses.")
            ),
            injuries = listOf(
                Injury("Tyler Adams", "MF - Hamstring Care", "Return: Match day")
            ),
            nextMatch = Match(
                opponent = "England",
                date = "July 5, 2026",
                time = "17:00 Local",
                stadium = Stadium(
                    name = "Hard Rock Stadium",
                    city = "Miami, USA",
                    capacity = "64,767",
                    latitude = 25.9580,
                    longitude = -80.2389,
                    weatherTemp = "82°F",
                    weatherCondition = "Humid & Showers"
                )
            ),
            path = listOf(
                "Round of 32: Won 1-0 vs Sweden",
                "Round of 16: Won 2-1 vs Italy",
                "Quarter-Final: vs England"
            )
        ),
        Team(
            name = "Mexico",
            abbreviation = "MEX",
            flag = "🇲🇽",
            fifaRanking = 15,
            profile = "Co-hosts with incredible passion. Known for intense collective pressing, fierce aerial combat, and utilizing legendary stadium atmospheres to suffocate opponents.",
            coach = "Javier Aguirre",
            latitude = 19.4326,
            longitude = -99.1332,
            form = listOf("W", "D", "W", "L", "W"),
            stats = TournamentStats(goalsScored = 8, wins = 3, possessionPercent = 52, shotsOnTarget = 11, cleanSheets = 2),
            keyPlayers = listOf(
                Player("Santiago Giménez", "FW", 9, "El Bebote. Powerful, in-form clinical center forward who thrives in the penalty box."),
                Player("Edson Álvarez", "MF", 4, "The defensive rock and captain. Dominant aerial winner and midfield destroyer."),
                Player("Luis Chávez", "MF", 14, "Free-kick maestro. Delivers wonderful diagonal long-passes and long-range rockets.")
            ),
            injuries = listOf(
                Injury("Cesar Montes", "DF - Groin Strain", "Return: 1 week")
            ),
            nextMatch = Match(
                opponent = "Canada",
                date = "July 6, 2026",
                time = "19:00 Local",
                stadium = Stadium(
                    name = "Estadio Azteca",
                    city = "Mexico City, Mexico",
                    capacity = "87,523",
                    latitude = 19.3029,
                    longitude = -99.1505,
                    weatherTemp = "70°F",
                    weatherCondition = "Rainy Evening"
                )
            ),
            path = listOf(
                "Round of 32: Won 2-1 vs Algeria",
                "Round of 16: Won 1-0 vs Uruguay",
                "Quarter-Final: vs Canada"
            )
        ),
        Team(
            name = "Canada",
            abbreviation = "CAN",
            flag = "🇨🇦",
            fifaRanking = 35,
            profile = "A historic co-host quarter-final run. Jesse Marsch has transformed Canada into a high-octane physical force using vertical counter-attacks and rapid transitions.",
            coach = "Jesse Marsch",
            latitude = 45.4215,
            longitude = -75.6972,
            form = listOf("W", "D", "W", "D", "W"),
            stats = TournamentStats(goalsScored = 7, wins = 2, possessionPercent = 49, shotsOnTarget = 10, cleanSheets = 1),
            keyPlayers = listOf(
                Player("Alphonso Davies", "DF", 19, "Phonzie. World-class speedster who switches from overlapping fullback to forward wing."),
                Player("Jonathan David", "FW", 20, "Highly clever striker with fantastic off-the-ball movement and elite composure."),
                Player("Stephen Eustáquio", "MF", 7, "Midfield orchestrator. Controls tempo, coordinates defensive blocks, and takes set-pieces.")
            ),
            injuries = listOf(
                Injury("Tajon Buchanan", "FW - Shin Fracture", "Out of Tournament")
            ),
            nextMatch = Match(
                opponent = "Mexico",
                date = "July 6, 2026",
                time = "19:00 Local",
                stadium = Stadium(
                    name = "Estadio Azteca",
                    city = "Mexico City, Mexico",
                    capacity = "87,523",
                    latitude = 19.3029,
                    longitude = -99.1505,
                    weatherTemp = "70°F",
                    weatherCondition = "Rainy Evening"
                )
            ),
            path = listOf(
                "Round of 32: Won 1-0 vs Belgium",
                "Round of 16: Won 1-0 vs Switzerland",
                "Quarter-Final: vs Mexico"
            )
        )
    )

    val teams: List<Team> = run {
        val list = detailedTeams.toMutableList()
        val extraInfo = listOf(
            Triple("Germany", "GER", "🇩🇪"),
            Triple("Italy", "ITA", "🇮🇹"),
            Triple("Portugal", "POR", "🇵🇹"),
            Triple("Netherlands", "NED", "🇳🇱"),
            Triple("Belgium", "BEL", "🇧🇪"),
            Triple("Croatia", "CRO", "🇭🇷"),
            Triple("Uruguay", "URU", "🇺🇾"),
            Triple("Colombia", "COL", "🇨🇴"),
            Triple("Morocco", "MAR", "🇲🇦"),
            Triple("Senegal", "SEN", "🇸🇳"),
            Triple("Japan", "JPN", "🇯🇵"),
            Triple("South Korea", "KOR", "🇰🇷"),
            Triple("Australia", "AUS", "🇦🇺"),
            Triple("Iran", "IRN", "🇮🇷"),
            Triple("Saudi Arabia", "KSA", "🇸🇦"),
            Triple("Switzerland", "SUI", "🇨🇭"),
            Triple("Denmark", "DEN", "🇩🇰"),
            Triple("Ukraine", "UKR", "🇺🇦"),
            Triple("Poland", "POL", "🇵🇱"),
            Triple("Sweden", "SWE", "🇸🇪"),
            Triple("Austria", "AUT", "🇦🇹"),
            Triple("Turkey", "TUR", "🇹🇷"),
            Triple("Nigeria", "NGA", "🇳🇬"),
            Triple("Egypt", "EGY", "🇪🇬"),
            Triple("Cameroon", "CMR", "🇨🇲"),
            Triple("Algeria", "ALG", "🇩🇿"),
            Triple("Tunisia", "TUN", "🇹🇳"),
            Triple("Ecuador", "ECU", "🇪🇨"),
            Triple("Peru", "PER", "🇵🇪"),
            Triple("Chile", "CHI", "🇨🇱"),
            Triple("Costa Rica", "CRC", "🇨🇷"),
            Triple("Panama", "PAN", "🇵🇦"),
            Triple("Jamaica", "JAM", "🇯🇲"),
            Triple("New Zealand", "NZL", "🇳🇿"),
            Triple("South Africa", "RSA", "🇿🇦"),
            Triple("Ghana", "GHA", "🇬🇭"),
            Triple("Ivory Coast", "CIV", "🇨🇮"),
            Triple("Iraq", "IRQ", "🇮🇶"),
            Triple("Qatar", "QAT", "🇶🇦"),
            Triple("United Arab Emirates", "UAE", "🇦🇪")
        )

        val latLons = mapOf(
            "GER" to Pair(52.5200, 13.4050),
            "ITA" to Pair(41.9028, 12.4964),
            "POR" to Pair(38.7223, -9.1393),
            "NED" to Pair(52.3676, 4.9041),
            "BEL" to Pair(50.8503, 4.3517),
            "CRO" to Pair(45.8150, 15.9819),
            "URU" to Pair(-34.9011, -56.1645),
            "COL" to Pair(4.7110, -74.0721),
            "MAR" to Pair(34.0209, -6.8416),
            "SEN" to Pair(14.7167, -17.4677),
            "JPN" to Pair(35.6762, 139.6503),
            "KOR" to Pair(37.5665, 126.9780),
            "AUS" to Pair(-35.2809, 149.1300),
            "IRN" to Pair(35.6892, 51.3890),
            "KSA" to Pair(24.7136, 46.6753),
            "SUI" to Pair(46.9480, 7.4474),
            "DEN" to Pair(55.6761, 12.5683),
            "UKR" to Pair(50.4501, 30.5234),
            "POL" to Pair(52.2297, 21.0122),
            "SWE" to Pair(59.3293, 18.0686),
            "AUT" to Pair(48.2082, 16.3738),
            "TUR" to Pair(39.9334, 32.8597),
            "NGA" to Pair(9.0765, 7.3986),
            "EGY" to Pair(30.0444, 31.2357),
            "CMR" to Pair(3.8480, 11.5021),
            "ALG" to Pair(36.7538, 3.0588),
            "TUN" to Pair(36.8065, 10.1815),
            "ECU" to Pair(-0.1807, -78.4678),
            "PER" to Pair(-12.0464, -77.0428),
            "CHI" to Pair(-33.4489, -70.6693),
            "CRC" to Pair(9.9281, -84.0907),
            "PAN" to Pair(8.9824, -79.5199),
            "JAM" to Pair(18.0179, -76.8099),
            "NZL" to Pair(-41.2865, 174.7762),
            "RSA" to Pair(-25.7479, 28.1878),
            "GHA" to Pair(5.6037, -0.1870),
            "CIV" to Pair(6.8276, -5.2793),
            "IRQ" to Pair(33.3152, 44.3661),
            "QAT" to Pair(25.2854, 51.5310),
            "UAE" to Pair(24.4539, 54.3773)
        )

        extraInfo.forEachIndexed { index, (name, abbreviation, flag) ->
            val latLon = latLons[abbreviation] ?: Pair(0.0, 0.0)
            list.add(
                Team(
                    name = name,
                    abbreviation = abbreviation,
                    flag = flag,
                    fifaRanking = 10 + index,
                    profile = "A proud competitor representing $name in the FIFA 2026 World Cup, bringing dynamic tactics and world-class spirit to the international stage.",
                    coach = "Coach of $name",
                    latitude = latLon.first,
                    longitude = latLon.second,
                    form = listOf("W", "D", "W", "L", "W"),
                    stats = TournamentStats(goalsScored = 8, wins = 2, possessionPercent = 52, shotsOnTarget = 11, cleanSheets = 1),
                    keyPlayers = listOf(
                        Player("Captain Star", "MF", 10, "A legendary captain and veteran presence in the squad."),
                        Player("Speedy Star", "FW", 7, "Electric winger with lightning pace and dynamic crossing."),
                        Player("Defense Anchor", "DF", 4, "Strong, dominant center-back keeping clean sheets.")
                    ),
                    injuries = emptyList(),
                    nextMatch = Match(
                        opponent = "Challenger",
                        date = "July 12, 2026",
                        time = "20:00 Local",
                        stadium = Stadium(
                            name = "Azteca Stadium",
                            city = "Mexico City, Mexico",
                            capacity = "87,500",
                            latitude = 19.3029,
                            longitude = -99.1505,
                            weatherTemp = "72°F",
                            weatherCondition = "Partly Cloudy"
                        )
                    ),
                    path = listOf(
                        "Group Stage: Qualified",
                        "Round of 32: Competitive Performance"
                    )
                )
            )
        }
        list.toList()
    }

    val womensTeams: List<Team> = listOf(
        Team(
            name = "Brazil", abbreviation = "BRA", flag = "🇧🇷", fifaRanking = 9,
            profile = "Host nation of the 2027 Women's World Cup. Possessing elite individual skill and a passionate home crowd behind them, the Seleção Feminina is primed for glory.",
            coach = "Arthur Elias", latitude = -15.7938, longitude = -47.8827,
            form = listOf("W", "W", "W", "D", "W"),
            stats = TournamentStats(goalsScored = 12, wins = 4, possessionPercent = 58, shotsOnTarget = 15, cleanSheets = 2),
            keyPlayers = listOf(
                Player("Marta", "FW", 10, "The legendary Queen of Football, bringing unmatched experience and clinical vision."),
                Player("Debinha", "FW", 9, "Electric forward with creative flair, clinical finishing, and high press work rate."),
                Player("Rafaelle", "DF", 4, "Strong, calm center-back and leader stabilizing the defensive line.")
            ),
            injuries = emptyList(),
            nextMatch = Match(
                opponent = "United States", date = "June 18, 2027", time = "20:00 Local",
                stadium = Stadium("Maracanã Stadium", "Rio de Janeiro, Brazil", "78,838", -22.9122, -43.2302, "72°F", "Clear Evening")
            ),
            path = listOf("Locked In: Host Nation", "Prequalified")
        ),
        Team(
            name = "United States", abbreviation = "USA", flag = "🇺🇸", fifaRanking = 1,
            profile = "Four-time World Champions and current Olympic gold medalists, the USWNT combines explosive youth and world-class athleticism to dominate their opponents.",
            coach = "Emma Hayes", latitude = 38.9072, longitude = -77.0369,
            form = listOf("W", "W", "W", "W", "W"),
            stats = TournamentStats(goalsScored = 16, wins = 5, possessionPercent = 64, shotsOnTarget = 20, cleanSheets = 4),
            keyPlayers = listOf(
                Player("Sophia Smith", "FW", 11, "Dynamic and explosive forward with incredible dribbling and clinical finishing."),
                Player("Trinity Rodman", "FW", 5, "Electrifying winger with world-class work rate, creativity, and direct threat."),
                Player("Naomi Girma", "DF", 4, "Calm, physically dominant defender. The best reading of the game in women's football.")
            ),
            injuries = emptyList(),
            nextMatch = Match(
                opponent = "Brazil", date = "June 18, 2027", time = "20:00 Local",
                stadium = Stadium("Maracanã Stadium", "Rio de Janeiro, Brazil", "78,838", -22.9122, -43.2302, "72°F", "Clear Evening")
            ),
            path = listOf("CONCACAF W Gold Cup: Winners", "Prequalified")
        ),
        Team(
            name = "England", abbreviation = "ENG", flag = "🏴󠁧󠁢󠁥󠁮󠁧󠁿", fifaRanking = 2,
            profile = "The Lionesses are European champions with a highly organized squad. Known for tactical discipline and clinical wing-play.",
            coach = "Sarina Wiegman", latitude = 51.5074, longitude = -0.1278,
            form = listOf("W", "W", "D", "W", "W"),
            stats = TournamentStats(goalsScored = 14, wins = 4, possessionPercent = 60, shotsOnTarget = 18, cleanSheets = 3),
            keyPlayers = listOf(
                Player("Lauren James", "MF", 7, "Flamboyant, technically gifted playmaker capable of absolute magic."),
                Player("Alessia Russo", "FW", 9, "Hard-working modern center forward with elite movement and finishing."),
                Player("Alex Greenwood", "DF", 5, "Superb passing range and ball-playing center-back stabilizing the backline.")
            ),
            injuries = emptyList(),
            nextMatch = Match(
                opponent = "Spain", date = "June 20, 2027", time = "17:00 Local",
                stadium = Stadium("Mineirão Stadium", "Belo Horizonte, Brazil", "61,846", -19.8659, -43.9713, "70°F", "Partly Cloudy")
            ),
            path = listOf("UEFA Nations League: Prequalified", "Prequalified")
        ),
        Team(
            name = "Spain", abbreviation = "ESP", flag = "🇪🇸", fifaRanking = 3,
            profile = "Reigning Women's World Cup champions. Masterful possession style coupled with lightning counter-press transitions.",
            coach = "Montse Tomé", latitude = 40.4168, longitude = -3.7038,
            form = listOf("W", "W", "W", "W", "D"),
            stats = TournamentStats(goalsScored = 18, wins = 4, possessionPercent = 68, shotsOnTarget = 22, cleanSheets = 3),
            keyPlayers = listOf(
                Player("Aitana Bonmatí", "MF", 14, "Ballon d'Or winner. Unmatched footballing IQ, passing range, and tactical control."),
                Player("Alexia Putellas", "MF", 11, "Two-time Ballon d'Or winner. Legendary creative maestro of world football."),
                Player("Salma Paralluelo", "FW", 7, "Speedster forward with explosive vertical acceleration and clinical threat.")
            ),
            injuries = emptyList(),
            nextMatch = Match(
                opponent = "England", date = "June 20, 2027", time = "17:00 Local",
                stadium = Stadium("Mineirão Stadium", "Belo Horizonte, Brazil", "61,846", -19.8659, -43.9713, "70°F", "Partly Cloudy")
            ),
            path = listOf("Defending Champions: Prequalified", "Prequalified")
        ),
        Team(
            name = "Germany", abbreviation = "GER", flag = "🇩🇪", fifaRanking = 4,
            profile = "Physically dominant and highly organized squad, combining clinical vertical attacks with standard solid defensive blocks.",
            coach = "Christian Wück", latitude = 52.5200, longitude = 13.4050,
            form = listOf("W", "W", "L", "W", "W"),
            stats = TournamentStats(goalsScored = 11, wins = 3, possessionPercent = 55, shotsOnTarget = 14, cleanSheets = 2),
            keyPlayers = listOf(
                Player("Alexandra Popp", "FW", 11, "Powerhouse captain and lethal aerial threat with massive presence in the box."),
                Player("Lena Oberdorf", "MF", 6, "Elite defensive midfielder. Relentless tackler and temp controller."),
                Player("Giulia Gwinn", "DF", 15, "Superb wingback with outstanding cross delivery and direct threat.")
            ),
            injuries = emptyList(),
            nextMatch = Match(
                opponent = "France", date = "June 22, 2027", time = "19:00 Local",
                stadium = Stadium("Estádio Nacional", "Brasília, Brazil", "72,788", -15.7835, -47.8992, "75°F", "Dry & Clear")
            ),
            path = listOf("UEFA Women's Championship: Prequalified", "Prequalified")
        ),
        Team(
            name = "France", abbreviation = "FRA", flag = "🇫🇷", fifaRanking = 5,
            profile = "Les Bleues bring raw athletic speed, dynamic midfield transitions, and a physical defense ready to contest for the crown.",
            coach = "Laurent Bonadei", latitude = 48.8566, longitude = 2.3522,
            form = listOf("W", "D", "W", "W", "D"),
            stats = TournamentStats(goalsScored = 12, wins = 3, possessionPercent = 57, shotsOnTarget = 15, cleanSheets = 2),
            keyPlayers = listOf(
                Player("Kadidiatou Diani", "FW", 11, "Direct and powerful right-winger with exceptional clinical finishing."),
                Player("Grace Geyoro", "MF", 8, "Dynamic box-to-box midfielder with intelligent movement into the box."),
                Player("Wendie Renard", "DF", 3, "Legendary captain. Physically dominant aerial winner and defensive anchor.")
            ),
            injuries = emptyList(),
            nextMatch = Match(
                opponent = "Germany", date = "June 22, 2027", time = "19:00 Local",
                stadium = Stadium("Estádio Nacional", "Brasília, Brazil", "72,788", -15.7835, -47.8992, "75°F", "Dry & Clear")
            ),
            path = listOf("UEFA Nations League: Prequalified", "Prequalified")
        ),
        Team(
            name = "Japan", abbreviation = "JPN", flag = "🇯🇵", fifaRanking = 7,
            profile = "Nadeshiko Japan plays with majestic collective passing, high footballing IQ, and quick positional changes that unlock defenses.",
            coach = "Futoshi Ikeda", latitude = 35.6762, longitude = 139.6503,
            form = listOf("W", "W", "D", "W", "W"),
            stats = TournamentStats(goalsScored = 13, wins = 4, possessionPercent = 61, shotsOnTarget = 17, cleanSheets = 3),
            keyPlayers = listOf(
                Player("Yui Hasegawa", "MF", 14, "Elite ball-playing deep playmaker. Orchestrates attacks with absolute composure."),
                Player("Hinata Miyazawa", "FW", 7, "Golden Boot winner. Blinding speed behind defenses and clinical finishing."),
                Player("Saki Kumagai", "DF", 4, "Legendary captain. Unrivaled experience stabilizing the defensive line.")
            ),
            injuries = emptyList(),
            nextMatch = Match(
                opponent = "Australia", date = "June 25, 2027", time = "18:00 Local",
                stadium = Stadium("Arena Corinthians", "São Paulo, Brazil", "49,205", -23.5453, -46.4742, "68°F", "Soft Evening Breeze")
            ),
            path = listOf("AFC Women's Asian Cup: Qualified", "Prequalified")
        ),
        Team(
            name = "Australia", abbreviation = "AUS", flag = "🇦🇺", fifaRanking = 12,
            profile = "The Matildas combine relentless physical grit with quick direct counter-attacks that excite fans across the globe.",
            coach = "Tom Sermanni", latitude = -35.2809, longitude = 149.1300,
            form = listOf("W", "D", "L", "W", "W"),
            stats = TournamentStats(goalsScored = 10, wins = 3, possessionPercent = 53, shotsOnTarget = 12, cleanSheets = 2),
            keyPlayers = listOf(
                Player("Sam Kerr", "FW", 20, "Legendary captain. World-class striker, aerial threat, and clinical finisher."),
                Player("Caitlin Foord", "FW", 9, "Creative, direct winger who terrorizes fullbacks with direct dribbling."),
                Player("Steph Catley", "DF", 7, "Consistent, ball-playing left-back with elite cross delivery.")
            ),
            injuries = emptyList(),
            nextMatch = Match(
                opponent = "Japan", date = "June 25, 2027", time = "18:00 Local",
                stadium = Stadium("Arena Corinthians", "São Paulo, Brazil", "49,205", -23.5453, -46.4742, "68°F", "Soft Evening Breeze")
            ),
            path = listOf("AFC Women's Asian Cup: Qualified", "Prequalified")
        ),
        Team(
            name = "Canada", abbreviation = "CAN", flag = "🇨🇦", fifaRanking = 6,
            profile = "Highly athletic and defensively secure, Canada combines experienced structure with quick attacking verticality.",
            coach = "Andy Spence", latitude = 45.4215, longitude = -75.6972,
            form = listOf("W", "D", "W", "W", "L"),
            stats = TournamentStats(goalsScored = 9, wins = 3, possessionPercent = 54, shotsOnTarget = 11, cleanSheets = 3),
            keyPlayers = listOf(
                Player("Jessie Fleming", "MF", 17, "Intelligent captain and midfield general who coordinates attacks and takes penalties."),
                Player("Adriana Leon", "FW", 19, "Fiery, direct forward with great aerial timing and direct clinical threat."),
                Player("Kadeisha Buchanan", "DF", 3, "Physically dominant center-back keeping clean sheets with robust tackles.")
            ),
            injuries = emptyList(),
            nextMatch = Match(
                opponent = "Sweden", date = "June 26, 2027", time = "16:00 Local",
                stadium = Stadium("Beira-Rio Stadium", "Porto Alegre, Brazil", "50,842", -30.0654, -51.2359, "62°F", "Cool Afternoon")
            ),
            path = listOf("CONCACAF Championship: Qualified", "Prequalified")
        ),
        Team(
            name = "Sweden", abbreviation = "SWE", flag = "🇸🇪", fifaRanking = 8,
            profile = "Superb team organization combined with elite set-piece execution and robust defensive solidity.",
            coach = "Peter Gerhardsson", latitude = 59.3293, longitude = 18.0686,
            form = listOf("W", "W", "D", "L", "W"),
            stats = TournamentStats(goalsScored = 11, wins = 3, possessionPercent = 56, shotsOnTarget = 13, cleanSheets = 3),
            keyPlayers = listOf(
                Player("Fridolina Rolfö", "FW", 18, "World-class versatile winger with direct overlapping pace and power."),
                Player("Stina Blackstenius", "FW", 11, "Clever, direct center forward with fantastic off-the-ball movement."),
                Player("Amanda Ilestedt", "DF", 13, "Set-piece threat and rock-solid defender dominating both penalty boxes.")
            ),
            injuries = emptyList(),
            nextMatch = Match(
                opponent = "Canada", date = "June 26, 2027", time = "16:00 Local",
                stadium = Stadium("Beira-Rio Stadium", "Porto Alegre, Brazil", "50,842", -30.0654, -51.2359, "62°F", "Cool Afternoon")
            ),
            path = listOf("UEFA Women's Euro: Qualified", "Prequalified")
        ),
        Team(
            name = "Netherlands", abbreviation = "NED", flag = "🇳🇱", fifaRanking = 11,
            profile = "Total Football in women's style. Elegant ball retention, fluid transitions, and creative playmaking.",
            coach = "Andries Jonker", latitude = 52.3676, longitude = 4.9041,
            form = listOf("W", "D", "W", "D", "W"),
            stats = TournamentStats(goalsScored = 10, wins = 2, possessionPercent = 59, shotsOnTarget = 14, cleanSheets = 2),
            keyPlayers = listOf(
                Player("Jill Roord", "MF", 10, "Lethal midfield engine who makes late runs into the box to score clutch goals."),
                Player("Lieke Martens", "FW", 11, "Legendary playmaker on the wing with clinical passing and dribbling."),
                Player("Sherida Spitse", "DF", 8, "Veteran captain. Elite reading of the game and set-piece specialist.")
            ),
            injuries = emptyList(),
            nextMatch = Match(
                opponent = "Colombia", date = "June 28, 2027", time = "20:00 Local",
                stadium = Stadium("Arena Fonte Nova", "Salvador, Brazil", "50,025", -12.9786, -38.5042, "78°F", "Warm Coastal breeze")
            ),
            path = listOf("UEFA Nations League: Qualified", "Prequalified")
        ),
        Team(
            name = "Colombia", abbreviation = "COL", flag = "🇨🇴", fifaRanking = 21,
            profile = "Las Chicas Superpoderosas bring fierce individual flair, aggressive direct wing play, and a proud spirit.",
            coach = "Ángelo Marsiglia", latitude = 4.7110, longitude = -74.0721,
            form = listOf("W", "W", "D", "W", "L"),
            stats = TournamentStats(goalsScored = 13, wins = 3, possessionPercent = 51, shotsOnTarget = 12, cleanSheets = 1),
            keyPlayers = listOf(
                Player("Linda Caicedo", "FW", 18, "Global teen prodigy. World-class speed, dazzling dribbling, and clinical finishes."),
                Player("Mayra Ramírez", "FW", 9, "Physically dominant center forward with exceptional link play and power."),
                Player("Catalina Usme", "MF", 10, "Legendary creative general. Playmaker and set-piece specialist.")
            ),
            injuries = emptyList(),
            nextMatch = Match(
                opponent = "Netherlands", date = "June 28, 2027", time = "20:00 Local",
                stadium = Stadium("Arena Fonte Nova", "Salvador, Brazil", "50,025", -12.9786, -38.5042, "78°F", "Warm Coastal breeze")
            ),
            path = listOf("Copa América Femenina: Runners-Up", "Prequalified")
        ),
        Team(
            name = "Morocco", abbreviation = "MAR", flag = "🇲🇦", fifaRanking = 59,
            profile = "The history makers of Africa, combining tight defensive shape with fast counter-attacking play.",
            coach = "Jorge Vilda", latitude = 34.0209, longitude = -6.8416,
            form = listOf("W", "L", "W", "D", "W"),
            stats = TournamentStats(goalsScored = 7, wins = 2, possessionPercent = 46, shotsOnTarget = 8, cleanSheets = 2),
            keyPlayers = listOf(
                Player("Ghizlane Chebbak", "MF", 7, "Inspirational captain and playmaking core distributing pinpoint balls."),
                Player("Anissa Lahmari", "FW", 16, "Slick, direct attacking midfielder with clinical finishing skills."),
                Player("Elodie Nakkach", "MF", 6, "Hard-working defensive midfielder with excellent positioning.")
            ),
            injuries = emptyList(),
            nextMatch = Match(
                opponent = "Nigeria", date = "June 29, 2027", time = "19:00 Local",
                stadium = Stadium("Arena da Amazônia", "Manaus, Brazil", "40,549", -3.0833, -60.0281, "82°F", "Humid & Cloudy")
            ),
            path = listOf("WAFCON: Semifinalists", "Prequalified")
        ),
        Team(
            name = "Nigeria", abbreviation = "NGA", flag = "🇳🇬", fifaRanking = 36,
            profile = "Super Falcons are African powerhouses, utilizing rapid transitions, physical presence, and world-class pace.",
            coach = "Justin Madugu", latitude = 9.0765, longitude = 7.3986,
            form = listOf("W", "D", "W", "L", "W"),
            stats = TournamentStats(goalsScored = 9, wins = 3, possessionPercent = 48, shotsOnTarget = 11, cleanSheets = 2),
            keyPlayers = listOf(
                Player("Asisat Oshoala", "FW", 8, "Six-time African Player of the Year. Blinding speed and clinical composure."),
                Player("Rasheedat Ajibade", "FW", 15, "The girl with the blue hair. Direct, electric winger with superb dribbling."),
                Player("Chiamaka Nnadozie", "GK", 16, "One of the best shot-stoppers in women's football. Incredible presence.")
            ),
            injuries = emptyList(),
            nextMatch = Match(
                opponent = "Morocco", date = "June 29, 2027", time = "19:00 Local",
                stadium = Stadium("Arena da Amazônia", "Manaus, Brazil", "40,549", -3.0833, -60.0281, "82°F", "Humid & Cloudy")
            ),
            path = listOf("WAFCON: Champions", "Prequalified")
        ),
        Team(
            name = "Denmark", abbreviation = "DEN", flag = "🇩🇰", fifaRanking = 12,
            profile = "Known for tactical awareness and strong team defense, Denmark is ready to compete at the highest level in 2027.",
            coach = "Andrée Jeglertz", latitude = 55.6761, longitude = 12.5683,
            form = listOf("W", "W", "L", "W", "D"),
            stats = TournamentStats(goalsScored = 9, wins = 3, possessionPercent = 54, shotsOnTarget = 12, cleanSheets = 3),
            keyPlayers = listOf(
                Player("Pernille Harder", "FW", 10, "World-class attacking midfielder and captain, famous for creative playmaking."),
                Player("Amalie Vangsgaard", "FW", 9, "Clinical striker with great aerial strength and box presence."),
                Player("Katrine Veje", "DF", 11, "Experienced left-back providing immense defensive stability and crossing threat.")
            ),
            injuries = emptyList(),
            nextMatch = Match(
                opponent = "Colombia", date = "June 19, 2027", time = "15:00 Local",
                stadium = Stadium("Mineirão Stadium", "Belo Horizonte, Brazil", "61,846", -19.8659, -43.9713, "70°F", "Sunny")
            ),
            path = listOf("UEFA Women's Championship: Qualified", "Locked In")
        ),
        Team(
            name = "Argentina", abbreviation = "ARG", flag = "🇦🇷", fifaRanking = 33,
            profile = "La Albiceleste plays with traditional South American passion and high work-rate, aiming to make a historical run.",
            coach = "Germán Portanova", latitude = -34.6037, longitude = -58.3816,
            form = listOf("W", "L", "W", "D", "L"),
            stats = TournamentStats(goalsScored = 6, wins = 2, possessionPercent = 48, shotsOnTarget = 9, cleanSheets = 1),
            keyPlayers = listOf(
                Player("Banini Estefanía", "MF", 22, "Technically superb playmaker with great vision and dribbling skills."),
                Player("Yamila Rodríguez", "FW", 11, "Electric winger with direct run-ins and fierce long-range shooting."),
                Player("Aldana Cometti", "DF", 6, "Commanding center-back with passionate leadership and physical dominance.")
            ),
            injuries = emptyList(),
            nextMatch = Match(
                opponent = "Spain", date = "June 20, 2027", time = "19:00 Local",
                stadium = Stadium("Arena Fonte Nova", "Salvador, Brazil", "50,025", -12.9786, -38.5042, "75°F", "Humid")
            ),
            path = listOf("Copa América Femenina: Third Place", "Locked In")
        ),
        Team(
            name = "Philippines", abbreviation = "PHI", flag = "🇵🇭", fifaRanking = 39,
            profile = "The Filipinas have shown incredible rising strength in Asian football, powered by dynamic team spirit and clinical organization.",
            coach = "Mark Torcaso", latitude = 14.5995, longitude = 120.9842,
            form = listOf("W", "D", "W", "L", "W"),
            stats = TournamentStats(goalsScored = 8, wins = 3, possessionPercent = 45, shotsOnTarget = 10, cleanSheets = 2),
            keyPlayers = listOf(
                Player("Sarina Bolden", "FW", 18, "Lethal clinical forward. Scored historical goals for the nation in major tournaments."),
                Player("Olivia McDaniel", "GK", 1, "Clutch, brave goalkeeper renowned for heroic penalty saves and shot-stopping."),
                Player("Hali Long", "DF", 5, "Inspirational captain and defensive anchor with massive tackle counts.")
            ),
            injuries = emptyList(),
            nextMatch = Match(
                opponent = "New Zealand", date = "June 21, 2027", time = "16:00 Local",
                stadium = Stadium("Arena da Amazônia", "Manaus, Brazil", "40,549", -3.0833, -60.0281, "84°F", "Very Humid")
            ),
            path = listOf("AFC Women's Asian Cup: Semifinalists", "Locked In")
        ),
        Team(
            name = "China PR", abbreviation = "CHN", flag = "🇨🇳", fifaRanking = 19,
            profile = "The Steel Roses are multiple-time Asian champions, characterized by disciplined defensive play and cohesive teamwork.",
            coach = "Ante Milicic", latitude = 39.9042, longitude = 116.4074,
            form = listOf("W", "W", "D", "W", "L"),
            stats = TournamentStats(goalsScored = 10, wins = 3, possessionPercent = 52, shotsOnTarget = 13, cleanSheets = 2),
            keyPlayers = listOf(
                Player("Wang Shuang", "FW", 7, "Elite creative forward with magical left foot, clinical passes, and set-piece goals."),
                Player("Zhang Linyan", "FW", 19, "Extremely agile and speedy winger with low center of gravity unlocking defenses."),
                Player("Wang Shanshan", "DF", 11, "Versatile, experienced leader playing both as striker and center-back.")
            ),
            injuries = emptyList(),
            nextMatch = Match(
                opponent = "Japan", date = "June 22, 2027", time = "17:00 Local",
                stadium = Stadium("Arena Corinthians", "São Paulo, Brazil", "49,205", -23.5453, -46.4742, "69°F", "Breezy")
            ),
            path = listOf("AFC Women's Asian Cup: Winners", "Locked In")
        ),
        Team(
            name = "Korea DPR", abbreviation = "PRK", flag = "🇰🇵", fifaRanking = 9,
            profile = "Known for their intense physical fitness, rapid vertical transition, and high defensive work rate.",
            coach = "Ri Yu-il", latitude = 39.0392, longitude = 125.7625,
            form = listOf("W", "W", "W", "W", "D"),
            stats = TournamentStats(goalsScored = 14, wins = 4, possessionPercent = 53, shotsOnTarget = 18, cleanSheets = 3),
            keyPlayers = listOf(
                Player("Sung Hyang-sim", "FW", 10, "Rapid attacker with dazzling dribbles and clinical vision."),
                Player("Kim Kyong-yong", "FW", 9, "Powerful modern center forward scoring magnificent vertical headers."),
                Player("Ri Myong-gum", "DF", 3, "Resilient, commanding center-back stabilizing the backline.")
            ),
            injuries = emptyList(),
            nextMatch = Match(
                opponent = "Korea Republic", date = "June 23, 2027", time = "20:00 Local",
                stadium = Stadium("Estádio Nacional", "Brasília, Brazil", "72,788", -15.7835, -47.8992, "74°F", "Clear")
            ),
            path = listOf("AFC Women's Asian Cup: Qualified", "Locked In")
        ),
        Team(
            name = "Korea Republic", abbreviation = "KOR", flag = "🇰🇷", fifaRanking = 20,
            profile = "Taegeuk Nangja brings dynamic movement, disciplined modern tactics, and superb individual technical ball-control.",
            coach = "Shin Sang-woo", latitude = 37.5665, longitude = 126.9780,
            form = listOf("W", "D", "W", "D", "W"),
            stats = TournamentStats(goalsScored = 9, wins = 3, possessionPercent = 56, shotsOnTarget = 12, cleanSheets = 2),
            keyPlayers = listOf(
                Player("Ji So-yun", "MF", 10, "All-time legendary playmaker. Vision, passing control, and free-kick goals."),
                Player("Casey Phair", "FW", 19, "Young explosive forward with massive physical acceleration and clinical threat."),
                Player("Cho So-hyun", "MF", 8, "Tenacious midfield engine winning high percentages of second balls.")
            ),
            injuries = emptyList(),
            nextMatch = Match(
                opponent = "Korea DPR", date = "June 23, 2027", time = "20:00 Local",
                stadium = Stadium("Estádio Nacional", "Brasília, Brazil", "72,788", -15.7835, -47.8992, "74°F", "Clear")
            ),
            path = listOf("AFC Women's Asian Cup: Runners-Up", "Locked In")
        ),
        Team(
            name = "New Zealand", abbreviation = "NZL", flag = "🇳🇿", fifaRanking = 28,
            profile = "The Football Ferns play with high physical strength, elite organization on set pieces, and a proud athletic heritage.",
            coach = "Michael Mayne", latitude = -40.9006, longitude = 174.8860,
            form = listOf("W", "L", "D", "W", "L"),
            stats = TournamentStats(goalsScored = 7, wins = 2, possessionPercent = 50, shotsOnTarget = 10, cleanSheets = 1),
            keyPlayers = listOf(
                Player("Ali Riley", "DF", 7, "Veteran captain and fullback bringing passion, speed, and wing overlaps."),
                Player("Hannah Wilkinson", "FW", 17, "Lethal, physically strong striker scoring historic match-winners."),
                Player("Katie Bowen", "DF", 14, "Consistent, ball-playing center-back organizing defensive lines.")
            ),
            injuries = emptyList(),
            nextMatch = Match(
                opponent = "Philippines", date = "June 21, 2027", time = "16:00 Local",
                stadium = Stadium("Arena da Amazônia", "Manaus, Brazil", "40,549", -3.0833, -60.0281, "84°F", "Very Humid")
            ),
            path = listOf("OFC Women's Nations Cup: Winners", "Locked In")
        )
    )
}
