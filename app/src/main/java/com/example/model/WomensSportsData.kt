package com.example.model

enum class WomensSportCategory(val displayName: String, val emoji: String) {
    ALL("All Sports", "🌟"),
    BASKETBALL("Basketball", "🏀"),
    SOCCER("Soccer", "⚽"),
    TENNIS("Tennis", "🎾"),
    ICE_HOCKEY("Ice Hockey", "🏒"),
    VOLLEYBALL("Volleyball", "🏐"),
    CRICKET("Cricket", "🏏")
}

enum class WomensScope(val displayName: String, val emoji: String) {
    ALL("All Scopes", "🌐"),
    USA_NATIONAL("USA National", "🇺🇸"),
    INTERNATIONAL("International", "🌍")
}

data class WomensStar(
    val name: String,
    val teamOrCountry: String,
    val position: String,
    val number: Int = 0,
    val stats: String,
    val avatarUrl: String = "",
    val fact: String
)

data class WomensGame(
    val id: String,
    val sport: WomensSportCategory,
    val league: String, // e.g. "WNBA", "NWSL", "PWHL", "WTA", "FIFA WWC", "LOVB"
    val scope: WomensScope,
    val homeTeamOrAthlete: String,
    val awayTeamOrAthlete: String,
    val homeFlagOrLogo: String,
    val awayFlagOrLogo: String,
    val stadiumId: String,
    val stadiumName: String,
    val city: String,
    val date: String,
    val time: String,
    val roundOrStage: String,
    val status: String, // "UPCOMING", "LIVE", "FINAL"
    val homeScore: Int? = null,
    val awayScore: Int? = null,
    val periodOrSet: String? = null,
    val broadcast: String,
    val keyStars: List<WomensStar>,
    val aiPreview: String,
    val h2hRecord: String
)

object WomensSportsDataProvider {

    val sampleGames: List<WomensGame> = listOf(
        // 1. WNBA Championship Game (USA National)
        WomensGame(
            id = "GAME_WNBA_FINALS_1",
            sport = WomensSportCategory.BASKETBALL,
            league = "WNBA",
            scope = WomensScope.USA_NATIONAL,
            homeTeamOrAthlete = "New York Liberty",
            awayTeamOrAthlete = "Indiana Fever",
            homeFlagOrLogo = "🗽",
            awayFlagOrLogo = "🔥",
            stadiumId = "STAD_BARCLAYS",
            stadiumName = "Barclays Center",
            city = "Brooklyn, NY",
            date = "Tonight",
            time = "7:30 PM EDT",
            roundOrStage = "WNBA Finals Game 1",
            status = "LIVE",
            homeScore = 78,
            awayScore = 74,
            periodOrSet = "Q4 3:45",
            broadcast = "ESPN / Disney+ / ABC",
            keyStars = listOf(
                WomensStar(
                    name = "Sabrina Ionescu",
                    teamOrCountry = "New York Liberty",
                    position = "PG",
                    number = 20,
                    stats = "18.2 PPG, 6.2 APG, 39.8% 3PT",
                    fact = "Record-breaking 3-point shootout champion with ice-cold clutch shooting."
                ),
                WomensStar(
                    name = "Caitlin Clark",
                    teamOrCountry = "Indiana Fever",
                    position = "PG",
                    number = 22,
                    stats = "19.5 PPG, 8.4 APG, Rookie of the Year",
                    fact = "All-time NCAA scoring queen and single-season WNBA assist record holder."
                ),
                WomensStar(
                    name = "Breanna Stewart",
                    teamOrCountry = "New York Liberty",
                    position = "PF",
                    number = 30,
                    stats = "20.4 PPG, 8.5 RPG, 2x WNBA MVP",
                    fact = "Elite two-way superstar and Olympic multi-gold medalist."
                )
            ),
            aiPreview = "High-octane duel between the Liberty's balanced perimeter motion and Clark's lethal transition playmaking. Watch out for pick-and-rolls targeting the short corner.",
            h2hRecord = "Liberty won 3 of 4 regular season encounters; Fever surging with high assist rates."
        ),

        // 2. WNBA Western Showdown (USA National)
        WomensGame(
            id = "GAME_WNBA_ACES_STORM",
            sport = WomensSportCategory.BASKETBALL,
            league = "WNBA",
            scope = WomensScope.USA_NATIONAL,
            homeTeamOrAthlete = "Las Vegas Aces",
            awayTeamOrAthlete = "Seattle Storm",
            homeFlagOrLogo = "♠️",
            awayFlagOrLogo = "⚡",
            stadiumId = "STAD_MICHELOB",
            stadiumName = "Michelob ULTRA Arena",
            city = "Las Vegas, NV",
            date = "Tomorrow",
            time = "9:00 PM EDT",
            roundOrStage = "Western Conference Semifinal",
            status = "UPCOMING",
            broadcast = "Prime Video / ION",
            keyStars = listOf(
                WomensStar(
                    name = "A'ja Wilson",
                    teamOrCountry = "Las Vegas Aces",
                    position = "C",
                    number = 22,
                    stats = "26.9 PPG, 11.9 RPG, 2.6 BPG (3x MVP)",
                    fact = "First player in WNBA history to surpass 1,000 points in a single season."
                ),
                WomensStar(
                    name = "Jewell Loyd",
                    teamOrCountry = "Seattle Storm",
                    position = "SG",
                    number = 24,
                    stats = "19.7 PPG, 4.5 RPG, 'Gold Mamba'",
                    fact = "Explosive shot creator known for unstoppable step-backs in clutch moments."
                )
            ),
            aiPreview = "The Aces rely heavily on A'ja Wilson's historic paint presence. Seattle must collapse defenders and control defensive rebounds to prevent second-chance putbacks.",
            h2hRecord = "Series tied 2-2 this season; average margin under 4.5 points."
        ),

        // 3. NWSL Historic Clash at CPKC Stadium (USA National)
        WomensGame(
            id = "GAME_NWSL_KC_ORLANDO",
            sport = WomensSportCategory.SOCCER,
            league = "NWSL",
            scope = WomensScope.USA_NATIONAL,
            homeTeamOrAthlete = "Kansas City Current",
            awayTeamOrAthlete = "Orlando Pride",
            homeFlagOrLogo = "🌊",
            awayFlagOrLogo = "🦁",
            stadiumId = "STAD_CPKC",
            stadiumName = "CPKC Stadium",
            city = "Kansas City, MO",
            date = "Friday",
            time = "8:00 PM EDT",
            roundOrStage = "NWSL Shield Decider",
            status = "UPCOMING",
            broadcast = "CBS / Paramount+ / NWSL+",
            keyStars = listOf(
                WomensStar(
                    name = "Temwa Chawinga",
                    teamOrCountry = "Kansas City Current",
                    position = "FW",
                    number = 11,
                    stats = "20 Goals (Single-Season All-Time NWSL Record)",
                    fact = "World's most prolific goalscorer in 2023-2024 with devastating pace."
                ),
                WomensStar(
                    name = "Marta",
                    teamOrCountry = "Orlando Pride",
                    position = "MF",
                    number = 10,
                    stats = "9 Goals, 6 Assists, 6x FIFA Player of the Year",
                    fact = "The Queen of Football leading Orlando's historic unbeaten run."
                ),
                WomensStar(
                    name = "Barbra Banda",
                    teamOrCountry = "Orlando Pride",
                    position = "FW",
                    number = 22,
                    stats = "13 Goals, 6 Assists in debut NWSL year",
                    fact = "Olympic hat-trick record holder with dynamic vertical power."
                )
            ),
            aiPreview = "A historic clash at the world's first purpose-built stadium for women's pro sports. Chawinga's line-breaking runs vs Marta's visionary tempo dictate the Shield.",
            h2hRecord = "Orlando won 2-1 in Florida; KC Current unbeaten at home in CPKC Stadium."
        ),

        // 4. NWSL West Coast Derby (USA National)
        WomensGame(
            id = "GAME_NWSL_ANGELCITY_THORNS",
            sport = WomensSportCategory.SOCCER,
            league = "NWSL",
            scope = WomensScope.USA_NATIONAL,
            homeTeamOrAthlete = "Angel City FC",
            awayTeamOrAthlete = "Portland Thorns",
            homeFlagOrLogo = "👼",
            awayFlagOrLogo = "🌹",
            stadiumId = "STAD_BMO_LA",
            stadiumName = "BMO Stadium",
            city = "Los Angeles, CA",
            date = "Saturday",
            time = "10:00 PM EDT",
            roundOrStage = "NWSL Regular Season",
            status = "UPCOMING",
            broadcast = "ION Television / Prime",
            keyStars = listOf(
                WomensStar(
                    name = "Alyssa Thompson",
                    teamOrCountry = "Angel City FC",
                    position = "FW",
                    number = 21,
                    stats = "5 Goals, 4 Assists, USWNT Olympic Alternate",
                    fact = "High school phenom drafted #1 overall, lethal when cutting inside."
                ),
                WomensStar(
                    name = "Sophia Smith",
                    teamOrCountry = "Portland Thorns",
                    position = "FW",
                    number = 9,
                    stats = "12 Goals, 6 Assists, 2022 NWSL MVP",
                    fact = "Triple Espresso Olympic champion with clinical bilateral finishing."
                )
            ),
            aiPreview = "Sold-out crowd expected at BMO Stadium in downtown LA. Angel City presses high, while Portland executes ruthless counter-attacks spearheaded by Sophia Smith.",
            h2hRecord = "Thorns hold 4 wins, Angel City 2 wins, 2 draws in series history."
        ),

        // 5. PWHL Ice Hockey Rivalry (USA National / North America)
        WomensGame(
            id = "GAME_PWHL_MINN_BOSTON",
            sport = WomensSportCategory.ICE_HOCKEY,
            league = "PWHL",
            scope = WomensScope.USA_NATIONAL,
            homeTeamOrAthlete = "Minnesota Frost",
            awayTeamOrAthlete = "Boston Fleet",
            homeFlagOrLogo = "❄️",
            awayFlagOrLogo = "⚓",
            stadiumId = "STAD_XCEL",
            stadiumName = "Xcel Energy Center",
            city = "Saint Paul, MN",
            date = "Sunday",
            time = "2:00 PM EDT",
            roundOrStage = "Walter Cup Rematch",
            status = "UPCOMING",
            broadcast = "Bally Sports / YouTube / NESN",
            keyStars = listOf(
                WomensStar(
                    name = "Taylor Heise",
                    teamOrCountry = "Minnesota Frost",
                    position = "C",
                    number = 9,
                    stats = "1st Overall Pick, Playoff MVP, 5 Playoff Goals",
                    fact = "Dynamic center with rapid release and extraordinary stickhandling."
                ),
                WomensStar(
                    name = "Hilary Knight",
                    teamOrCountry = "Boston Fleet",
                    position = "FW",
                    number = 21,
                    stats = "Captain, All-Time Women's World Championship Scorer",
                    fact = "Icon of women's hockey with 9 World Championship gold medals."
                )
            ),
            aiPreview = "A physical clash of the inaugural Walter Cup finalists. Minnesota looks to capitalize on home ice speed, while Boston's defensive discipline will test the Frost power play.",
            h2hRecord = "Minnesota won the inaugural Walter Cup 3 games to 2."
        ),

        // 6. WTA Tour Grand Slam Final (USA National)
        WomensGame(
            id = "GAME_WTA_USOPEN_FINAL",
            sport = WomensSportCategory.TENNIS,
            league = "WTA Tour",
            scope = WomensScope.USA_NATIONAL,
            homeTeamOrAthlete = "Coco Gauff (USA)",
            awayTeamOrAthlete = "Aryna Sabalenka",
            homeFlagOrLogo = "🇺🇸",
            awayFlagOrLogo = "🎾",
            stadiumId = "STAD_ARTHUR_ASHE",
            stadiumName = "Arthur Ashe Stadium",
            city = "Flushing Meadows, NY",
            date = "Saturday",
            time = "4:00 PM EDT",
            roundOrStage = "US Open Women's Championship",
            status = "UPCOMING",
            broadcast = "ESPN / ESPN+ / Tennis Channel",
            keyStars = listOf(
                WomensStar(
                    name = "Coco Gauff",
                    teamOrCountry = "United States",
                    position = "Singles #3",
                    stats = "US Open Champion, WTA Finals Champion",
                    fact = "Youngest American woman to win US Open since Serena Williams."
                ),
                WomensStar(
                    name = "Aryna Sabalenka",
                    teamOrCountry = "International",
                    position = "Singles #1",
                    stats = "3x Grand Slam Champion (US Open, 2x Aussie Open)",
                    fact = "Possesses the heaviest average forehand speed across all tennis tours."
                )
            ),
            aiPreview = "Arthur Ashe Stadium's 23,771 crowd will be roaring. Gauff's world-class backhand and court coverage will contest Sabalenka's overwhelming baseline baseline firepower.",
            h2hRecord = "Head-to-head tied at 4-4 in tour classics."
        ),

        // 7. FIFA Women's World Cup 2027 Grand Final (International)
        WomensGame(
            id = "GAME_WWC_FINAL_2027",
            sport = WomensSportCategory.SOCCER,
            league = "FIFA Women's World Cup",
            scope = WomensScope.INTERNATIONAL,
            homeTeamOrAthlete = "United States (USWNT)",
            awayTeamOrAthlete = "Brazil Women",
            homeFlagOrLogo = "🇺🇸",
            awayFlagOrLogo = "🇧🇷",
            stadiumId = "STAD_MARACANA",
            stadiumName = "Maracanã Stadium",
            city = "Rio de Janeiro, Brazil",
            date = "July 25, 2027",
            time = "8:00 PM Local",
            roundOrStage = "FIFA WWC Grand Final",
            status = "UPCOMING",
            broadcast = "FOX / Telemundo / TV Globo",
            keyStars = listOf(
                WomensStar(
                    name = "Trinity Rodman",
                    teamOrCountry = "United States",
                    position = "FW",
                    number = 5,
                    stats = "Olympic Gold Champion, Electrifying Winger",
                    fact = "World-class work rate, defensive tracking, and clinical game-winners."
                ),
                WomensStar(
                    name = "Sophia Smith",
                    teamOrCountry = "United States",
                    position = "FW",
                    number = 11,
                    stats = "Olympic Gold Champion, 2024 Golden Boot Contender",
                    fact = "Master of combination play and deadly inside 18 yards."
                ),
                WomensStar(
                    name = "Debinha",
                    teamOrCountry = "Brazil",
                    position = "FW",
                    number = 9,
                    stats = "Copa América Champion, Seleção Maestro",
                    fact = "Brazilian flair and clinical finishing in front of 78,000 home fans."
                )
            ),
            aiPreview = "The dream final at the cathedral of world football. Emma Hayes' USWNT faces Arthur Elias' Brazil in an atmosphere of 78,000 passionate spectators.",
            h2hRecord = "USWNT won the 2024 Olympic Gold Medal Match 1-0 in Paris."
        ),

        // 8. UEFA Women's Champions League Final (International)
        WomensGame(
            id = "GAME_UWCL_BARCA_LYON",
            sport = WomensSportCategory.SOCCER,
            league = "UEFA Women's Champions League",
            scope = WomensScope.INTERNATIONAL,
            homeTeamOrAthlete = "Barcelona Femení",
            awayTeamOrAthlete = "Lyon Féminin",
            homeFlagOrLogo = "🔵🔴",
            awayFlagOrLogo = "⚪🔵",
            stadiumId = "STAD_JOHAN_CRUYFF",
            stadiumName = "Camp Nou / Johan Cruyff",
            city = "Barcelona, Spain",
            date = "May 24, 2025",
            time = "6:00 PM CET",
            roundOrStage = "UWCL European Final",
            status = "UPCOMING",
            broadcast = "DAZN / YouTube (Worldwide Free Stream)",
            keyStars = listOf(
                WomensStar(
                    name = "Aitana Bonmatí",
                    teamOrCountry = "Barcelona Femení",
                    position = "MF",
                    number = 14,
                    stats = "2x Ballon d'Or Winner, The Architect",
                    fact = "Unmatched game vision, close control, and tournament dominance."
                ),
                WomensStar(
                    name = "Alexia Putellas",
                    teamOrCountry = "Barcelona Femení",
                    position = "MF",
                    number = 11,
                    stats = "2x Ballon d'Or Winner, Captain",
                    fact = "Symbol of modern women's soccer and Blaugrana leader."
                ),
                WomensStar(
                    name = "Ada Hegerberg",
                    teamOrCountry = "Lyon Féminin",
                    position = "FW",
                    number = 14,
                    stats = "All-Time UWCL Top Scorer (64 Goals)",
                    fact = "First ever Women's Ballon d'Or winner and lethal aerial threat."
                )
            ),
            aiPreview = "European football royalty clash. Barcelona's tiki-taka dominance meets Lyon's athletic power and unmatched 8-time Champions League heritage.",
            h2hRecord = "Barcelona won the 2024 Bilbao Final 2-0; Lyon won 2019 and 2022 finals."
        ),

        // 9. Wimbledon Women's Championship Final (International)
        WomensGame(
            id = "GAME_WTA_WIMBLEDON",
            sport = WomensSportCategory.TENNIS,
            league = "WTA Tour",
            scope = WomensScope.INTERNATIONAL,
            homeTeamOrAthlete = "Iga Świątek (POL)",
            awayTeamOrAthlete = "Aryna Sabalenka",
            homeFlagOrLogo = "🇵🇱",
            awayFlagOrLogo = "🎾",
            stadiumId = "STAD_WIMBLEDON",
            stadiumName = "Centre Court, Wimbledon",
            city = "London, UK",
            date = "July 12, 2025",
            time = "2:00 PM BST",
            roundOrStage = "Wimbledon Ladies' Singles Final",
            status = "UPCOMING",
            broadcast = "BBC / ESPN / Eurosport",
            keyStars = listOf(
                WomensStar(
                    name = "Iga Świątek",
                    teamOrCountry = "Poland",
                    position = "Singles World #2",
                    stats = "5x Grand Slam Champion, Olympic Bronze Medalist",
                    fact = "Exceptional heavy topspin forehand and supreme tactical mental strength."
                ),
                WomensStar(
                    name = "Aryna Sabalenka",
                    teamOrCountry = "International",
                    position = "Singles World #1",
                    stats = "3x Grand Slam Champion, Year-End World #1",
                    fact = "Fierce baseline hitter with high-velocity first serves."
                )
            ),
            aiPreview = "Grass court showdown at tennis' most prestigious venue. Świątek's footwork vs Sabalenka's thunderous serves on the pristine Centre Court turf.",
            h2hRecord = "Świątek leads overall 8-4; first Grand Slam final meeting on grass."
        ),

        // 10. Pro Volleyball USA Championship (USA National)
        WomensGame(
            id = "GAME_VOLLEY_ATLANTA_OMAHA",
            sport = WomensSportCategory.VOLLEYBALL,
            league = "LOVB / PVF",
            scope = WomensScope.USA_NATIONAL,
            homeTeamOrAthlete = "Omaha Supernovas",
            awayTeamOrAthlete = "Atlanta Vibe",
            homeFlagOrLogo = "🏐",
            awayFlagOrLogo = "⚡",
            stadiumId = "STAD_CHI_HEALTH",
            stadiumName = "CHI Health Center",
            city = "Omaha, NE",
            date = "Sunday",
            time = "7:00 PM CDT",
            roundOrStage = "Pro Volleyball Championship",
            status = "UPCOMING",
            broadcast = "CBS Sports Network / LOVB Stream",
            keyStars = listOf(
                WomensStar(
                    name = "Jordan Larson",
                    teamOrCountry = "LOVB Omaha / USA",
                    position = "Outside Hitter",
                    number = 10,
                    stats = "Olympic Gold, Silver & Bronze Medalist, MVP",
                    fact = "Legendary USA Volleyball captain and quintessential leader."
                ),
                WomensStar(
                    name = "Leah Edmond",
                    teamOrCountry = "Atlanta Vibe",
                    position = "Outside Hitter",
                    number = 13,
                    stats = "PVF MVP, 4.3 Kills per Set",
                    fact = "Electric high-flier with thunderous spike velocity."
                )
            ),
            aiPreview = "Nebraska's passionate volleyball capital hosts the championship. Omaha's disciplined reception meets Atlanta's towering block in front of 15,000+ fans.",
            h2hRecord = "Split 2-2 in inaugural season."
        ),

        // 11. Women's Premier League Cricket Final (International)
        WomensGame(
            id = "GAME_WPL_CRICKET_FINAL",
            sport = WomensSportCategory.CRICKET,
            league = "WPL",
            scope = WomensScope.INTERNATIONAL,
            homeTeamOrAthlete = "Royal Challengers Bangalore Women",
            awayTeamOrAthlete = "Delhi Capitals Women",
            homeFlagOrLogo = "🔴",
            awayFlagOrLogo = "🔵",
            stadiumId = "STAD_DY_PATIL",
            stadiumName = "DY Patil Stadium",
            city = "Navi Mumbai, India",
            date = "March 15, 2025",
            time = "7:30 PM IST",
            roundOrStage = "WPL Championship Final",
            status = "FINAL",
            homeScore = 115,
            awayScore = 113,
            periodOrSet = "RCB won by 8 wickets",
            broadcast = "JioCinema / Sky Sports Cricket",
            keyStars = listOf(
                WomensStar(
                    name = "Smriti Mandhana",
                    teamOrCountry = "RCB Women / India",
                    position = "Opening Batter & Captain",
                    number = 18,
                    stats = "ICC Women's Cricketer of the Year, Elegant Left-Hander",
                    fact = "Led RCB to their historic first championship title in cricket history."
                ),
                WomensStar(
                    name = "Ellyse Perry",
                    teamOrCountry = "RCB Women / Australia",
                    position = "All-Rounder",
                    number = 8,
                    stats = "Orange Cap Winner, 341 Runs, 6/15 Bowling Record",
                    fact = "Dual international in both cricket and soccer World Cups."
                )
            ),
            aiPreview = "RCB's spin attack dismantled Delhi's top order to claim their historic title in front of 40,000 cheering fans in Navi Mumbai.",
            h2hRecord = "RCB won the final after thrilling group stage battles."
        )
    )

    fun filterGames(
        scope: WomensScope,
        category: WomensSportCategory,
        searchQuery: String = ""
    ): List<WomensGame> {
        return sampleGames.filter { game ->
            val scopeMatch = (scope == WomensScope.ALL || game.scope == scope)
            val categoryMatch = (category == WomensSportCategory.ALL || game.sport == category)
            val queryMatch = if (searchQuery.isBlank()) true else {
                val q = searchQuery.trim().lowercase()
                game.homeTeamOrAthlete.lowercase().contains(q) ||
                game.awayTeamOrAthlete.lowercase().contains(q) ||
                game.league.lowercase().contains(q) ||
                game.city.lowercase().contains(q) ||
                game.stadiumName.lowercase().contains(q) ||
                game.keyStars.any { it.name.lowercase().contains(q) }
            }
            scopeMatch && categoryMatch && queryMatch
        }
    }
}
