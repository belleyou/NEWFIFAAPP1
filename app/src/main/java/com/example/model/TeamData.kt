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
}
