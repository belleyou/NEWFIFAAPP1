package com.example.model

data class StadiumPlayer(
    val number: Int,
    val name: String,
    val position: String, // "GK", "DF", "MF", "FW", "PG", "SG", "SF", "PF", "C"
    val age: Int,
    val capsOrExp: String, // e.g. "74 caps" or "5 yrs exp"
    val clubOrCollege: String,
    val avatarUrl: String = "",
    val stats: StadiumPlayerStats,
    val signatureSkill: String
)

data class StadiumPlayerStats(
    val matchesPlayed: Int,
    val goalsOrPpg: String, // e.g. "18.2 PPG" or "14 Goals"
    val assistsOrApg: String, // e.g. "8.4 APG" or "9 Assists"
    val minutesPlayed: Int,
    val rating: Double, // e.g. 8.7
    val keyMetricLabel: String, // e.g. "Clean Sheets" or "Rebounds"
    val keyMetricValue: String, // e.g. "12" or "9.1 RPG"
    val shotAccuracyOrFg: String // e.g. "48.2% FG" or "64% On-Target"
)

data class StadiumTeamHistoricalPerformance(
    val championshipTitles: List<String>,
    val allTimeRecord: String, // e.g. "42W - 12D - 8L"
    val winPercentage: String, // e.g. "72.4%"
    val venueHomeRecord: String, // e.g. "18W - 2D - 1L at this venue"
    val recentForm: List<String>, // ["W", "W", "W", "D", "W"]
    val milestoneMoments: List<String>
)

data class StadiumTeamDetails(
    val teamId: String,
    val teamName: String,
    val abbreviation: String,
    val leagueOrCompetition: String,
    val sport: String,
    val flagOrLogo: String,
    val stadiumId: String,
    val stadiumName: String,
    val city: String,
    val headCoach: String,
    val foundedYear: String,
    val primaryColorHex: Long,
    val roster: List<StadiumPlayer>,
    val historicalPerformance: StadiumTeamHistoricalPerformance,
    val nextMatchSummary: String,
    val nextMatchDate: String,
    val nextMatchTime: String
)

object StadiumTeamRosterDataProvider {

    fun getTeamForStadium(stadiumId: String): StadiumTeamDetails {
        return when (stadiumId) {
            "STAD_CPKC" -> getKCCurrentDetails()
            "STAD_BARCLAYS" -> getNYLibertyDetails()
            "STAD_GAINBRIDGE" -> getIndianaFeverDetails()
            "STAD_MICHELOB" -> getLVAcesDetails()
            "STAD_SEATTLE", "STAD_CLIMATE" -> getSeattleStormDetails()
            "STAD_NEW_YORK", "STAD_METLIFE" -> getUSWNTDetails("STAD_NEW_YORK", "MetLife Stadium", "East Rutherford, NJ")
            "STAD_MEXICO_CITY", "STAD_AZTECA" -> getMexicoNationalTeamDetails()
            "STAD_LOS_ANGELES", "STAD_SOFI" -> getAngelCFDetails()
            "STAD_PORTLAND", "STAD_PROVIDENCE" -> getPortlandThornsDetails()
            "STAD_TORONTO", "STAD_BMO" -> getCanadaNationalTeamDetails()
            else -> getDefaultTeamDetails(stadiumId)
        }
    }

    private fun getKCCurrentDetails(): StadiumTeamDetails {
        return StadiumTeamDetails(
            teamId = "TEAM_KC_CURRENT",
            teamName = "Kansas City Current",
            abbreviation = "KCC",
            leagueOrCompetition = "NWSL (National Women's Soccer League)",
            sport = "Soccer ⚽",
            flagOrLogo = "🌊",
            stadiumId = "STAD_CPKC",
            stadiumName = "CPKC Stadium",
            city = "Kansas City, MO",
            headCoach = "Vlatko Andonovski",
            foundedYear = "2020",
            primaryColorHex = 0xFF0D9488, // Teal
            roster = listOf(
                StadiumPlayer(
                    number = 9,
                    name = "Temwa Chawinga",
                    position = "FW",
                    age = 26,
                    capsOrExp = "52 caps",
                    clubOrCollege = "Malawi / KC Current",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 25,
                        goalsOrPpg = "20 Goals (NWSL Record)",
                        assistsOrApg = "6 Assists",
                        minutesPlayed = 2190,
                        rating = 9.2,
                        keyMetricLabel = "Golden Boot",
                        keyMetricValue = "1st in NWSL",
                        shotAccuracyOrFg = "68.4% On-Target"
                    ),
                    signatureSkill = "Explosive counter-attack acceleration and pinpoint clinical finishing."
                ),
                StadiumPlayer(
                    number = 10,
                    name = "Lo'eau LaBonta",
                    position = "MF",
                    age = 31,
                    capsOrExp = "140 pro apps",
                    clubOrCollege = "Stanford University",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 26,
                        goalsOrPpg = "7 Goals",
                        assistsOrApg = "5 Assists",
                        minutesPlayed = 2240,
                        rating = 8.6,
                        keyMetricLabel = "Pass Accuracy",
                        keyMetricValue = "84.2%",
                        shotAccuracyOrFg = "52% On-Target"
                    ),
                    signatureSkill = "Ice-in-the-veins penalty conversion and charismatic midfield engine."
                ),
                StadiumPlayer(
                    number = 14,
                    name = "Claire Hutton",
                    position = "MF",
                    age = 18,
                    capsOrExp = "USYNT U-20 Captain",
                    clubOrCollege = "Bethlehem, NY",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 24,
                        goalsOrPpg = "1 Goal",
                        assistsOrApg = "4 Assists",
                        minutesPlayed = 1980,
                        rating = 8.4,
                        keyMetricLabel = "Tackles Won",
                        keyMetricValue = "68 Tackles",
                        shotAccuracyOrFg = "78% Pass Direct"
                    ),
                    signatureSkill = "Prodigious defensive vision, elite positional tackling, and spatial awareness."
                ),
                StadiumPlayer(
                    number = 7,
                    name = "Debinha",
                    position = "MF",
                    age = 33,
                    capsOrExp = "134 Brazil caps",
                    clubOrCollege = "Brazil National Team",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 20,
                        goalsOrPpg = "5 Goals",
                        assistsOrApg = "7 Assists",
                        minutesPlayed = 1620,
                        rating = 8.8,
                        keyMetricLabel = "Key Passes",
                        keyMetricValue = "42",
                        shotAccuracyOrFg = "61% On-Target"
                    ),
                    signatureSkill = "Samba dribbling maestro and creative through-ball distribution."
                ),
                StadiumPlayer(
                    number = 16,
                    name = "Vanessa DiBernardo",
                    position = "MF",
                    age = 32,
                    capsOrExp = "200+ NWSL apps",
                    clubOrCollege = "Illinois",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 25,
                        goalsOrPpg = "5 Goals",
                        assistsOrApg = "6 Assists",
                        minutesPlayed = 1910,
                        rating = 8.3,
                        keyMetricLabel = "Distance Covered",
                        keyMetricValue = "11.2 km/match",
                        shotAccuracyOrFg = "49% On-Target"
                    ),
                    signatureSkill = "Scored the historic inaugural goal at CPKC Stadium in the 5-4 home opener."
                ),
                StadiumPlayer(
                    number = 21,
                    name = "AD Franch",
                    position = "GK",
                    age = 34,
                    capsOrExp = "10 USWNT caps",
                    clubOrCollege = "Oklahoma State",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 24,
                        goalsOrPpg = "1.04 GAA",
                        assistsOrApg = "1 Assist",
                        minutesPlayed = 2160,
                        rating = 8.5,
                        keyMetricLabel = "Clean Sheets",
                        keyMetricValue = "9 Clean Sheets",
                        shotAccuracyOrFg = "77.5% Save Pct"
                    ),
                    signatureSkill = "Commanding aerial presence and world-class reflexes on penalty stops."
                )
            ),
            historicalPerformance = StadiumTeamHistoricalPerformance(
                championshipTitles = listOf("2024 NWSL Shield Runner-up", "2024 NWSL x Liga MX Femenil Summer Cup Champions"),
                allTimeRecord = "46W - 20D - 24L",
                winPercentage = "63.8%",
                venueHomeRecord = "11W - 2D - 1L at CPKC Stadium (92.8% Point Capture)",
                recentForm = listOf("W", "W", "D", "W", "W"),
                milestoneMoments = listOf(
                    "March 16, 2024: Opened CPKC Stadium as the first purpose-built stadium in world women's sports history with a 5-4 win.",
                    "October 2024: Temwa Chawinga broke the NWSL single-season scoring record with 20 goals.",
                    "Summer 2024: Lifted the international NWSL x Liga MX Summer Cup trophy at home."
                )
            ),
            nextMatchSummary = "vs Orlando Pride (NWSL Championship Semifinal)",
            nextMatchDate = "Saturday, Nov 16",
            nextMatchTime = "3:00 PM EST"
        )
    }

    private fun getNYLibertyDetails(): StadiumTeamDetails {
        return StadiumTeamDetails(
            teamId = "TEAM_NY_LIBERTY",
            teamName = "New York Liberty",
            abbreviation = "NYL",
            leagueOrCompetition = "WNBA (Women's National Basketball Association)",
            sport = "Basketball 🏀",
            flagOrLogo = "🗽",
            stadiumId = "STAD_BARCLAYS",
            stadiumName = "Barclays Center",
            city = "Brooklyn, NY",
            headCoach = "Sandy Brondello",
            foundedYear = "1997",
            primaryColorHex = 0xFF14B8A6, // Seafoam
            roster = listOf(
                StadiumPlayer(
                    number = 20,
                    name = "Sabrina Ionescu",
                    position = "PG",
                    age = 26,
                    capsOrExp = "5 yrs pro",
                    clubOrCollege = "Oregon",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 38,
                        goalsOrPpg = "18.2 PPG",
                        assistsOrApg = "6.2 APG",
                        minutesPlayed = 1240,
                        rating = 9.4,
                        keyMetricLabel = "3-Pointers Made",
                        keyMetricValue = "107 3PM",
                        shotAccuracyOrFg = "39.8% 3PT"
                    ),
                    signatureSkill = "Logo range 3-point sniping and clutch buzzer-beater heroics."
                ),
                StadiumPlayer(
                    number = 30,
                    name = "Breanna Stewart",
                    position = "PF",
                    age = 30,
                    capsOrExp = "8 yrs pro (2x MVP)",
                    clubOrCollege = "UConn",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 38,
                        goalsOrPpg = "20.4 PPG",
                        assistsOrApg = "3.5 APG",
                        minutesPlayed = 1310,
                        rating = 9.6,
                        keyMetricLabel = "Rebounds",
                        keyMetricValue = "8.5 RPG",
                        shotAccuracyOrFg = "46.8% FG"
                    ),
                    signatureSkill = "Unstoppable high-post turnaround jumper and lockdown rim protection."
                ),
                StadiumPlayer(
                    number = 35,
                    name = "Jonquel Jones",
                    position = "C",
                    age = 30,
                    capsOrExp = "7 yrs pro (Finals MVP)",
                    clubOrCollege = "George Washington",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 39,
                        goalsOrPpg = "14.2 PPG",
                        assistsOrApg = "3.2 APG",
                        minutesPlayed = 1190,
                        rating = 9.1,
                        keyMetricLabel = "Double-Doubles",
                        keyMetricValue = "18",
                        shotAccuracyOrFg = "54.2% FG"
                    ),
                    signatureSkill = "Physical paint dominance, rim protection, and 2024 WNBA Finals MVP."
                ),
                StadiumPlayer(
                    number = 44,
                    name = "Betnijah Laney-Hamilton",
                    position = "SF",
                    age = 30,
                    capsOrExp = "9 yrs pro",
                    clubOrCollege = "Rutgers",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 32,
                        goalsOrPpg = "11.8 PPG",
                        assistsOrApg = "3.4 APG",
                        minutesPlayed = 980,
                        rating = 8.5,
                        keyMetricLabel = "Defensive Stops",
                        keyMetricValue = "1.4 SPG",
                        shotAccuracyOrFg = "45.0% FG"
                    ),
                    signatureSkill = "Tenacious point-of-attack perimeter perimeter defense."
                )
            ),
            historicalPerformance = StadiumTeamHistoricalPerformance(
                championshipTitles = listOf("2024 WNBA Champions 🏆", "2023 WNBA Commissioner's Cup Champions", "5x Eastern Conference Champions"),
                allTimeRecord = "460W - 440L",
                winPercentage = "68.2% (2024 Season)",
                venueHomeRecord = "19W - 2L at Barclays Center (2024 Season)",
                recentForm = listOf("W", "W", "W", "W", "W"),
                milestoneMoments = listOf(
                    "October 20, 2024: Defeated Minnesota Lynx in Game 5 OT to win first-ever WNBA franchise championship.",
                    "August 2023: Lifted Commissioner's Cup trophy in front of electric Brooklyn home crowd.",
                    "July 2023: Sabrina Ionescu set all-time 3-point contest record (37 of 40 points) at Barclays Center."
                )
            ),
            nextMatchSummary = "vs Las Vegas Aces (Championship Rematch)",
            nextMatchDate = "Tonight",
            nextMatchTime = "7:30 PM EDT"
        )
    }

    private fun getIndianaFeverDetails(): StadiumTeamDetails {
        return StadiumTeamDetails(
            teamId = "TEAM_IND_FEVER",
            teamName = "Indiana Fever",
            abbreviation = "IND",
            leagueOrCompetition = "WNBA (Women's National Basketball Association)",
            sport = "Basketball 🏀",
            flagOrLogo = "🔥",
            stadiumId = "STAD_GAINBRIDGE",
            stadiumName = "Gainbridge Fieldhouse",
            city = "Indianapolis, IN",
            headCoach = "Stephanie White",
            foundedYear = "2000",
            primaryColorHex = 0xFFEF4444, // Red / Gold
            roster = listOf(
                StadiumPlayer(
                    number = 22,
                    name = "Caitlin Clark",
                    position = "PG",
                    age = 22,
                    capsOrExp = "Rookie of the Year",
                    clubOrCollege = "Iowa",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 40,
                        goalsOrPpg = "19.5 PPG",
                        assistsOrApg = "8.4 APG (Record)",
                        minutesPlayed = 1410,
                        rating = 9.7,
                        keyMetricLabel = "Triple-Doubles",
                        keyMetricValue = "2 (First Rookie)",
                        shotAccuracyOrFg = "34.4% 3PT"
                    ),
                    signatureSkill = "Generational passing vision and ultra-deep transition stepback triples."
                ),
                StadiumPlayer(
                    number = 7,
                    name = "Aliyah Boston",
                    position = "C",
                    age = 22,
                    capsOrExp = "2023 Rookie of the Year",
                    clubOrCollege = "South Carolina",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 40,
                        goalsOrPpg = "14.0 PPG",
                        assistsOrApg = "3.2 APG",
                        minutesPlayed = 1260,
                        rating = 9.0,
                        keyMetricLabel = "Rebounds",
                        keyMetricValue = "8.9 RPG",
                        shotAccuracyOrFg = "52.9% FG"
                    ),
                    signatureSkill = "Elite pick-and-roll finishing, offensive rebounding, and interior defense."
                ),
                StadiumPlayer(
                    number = 0,
                    name = "Kelsey Mitchell",
                    position = "SG",
                    age = 28,
                    capsOrExp = "7 yrs pro",
                    clubOrCollege = "Ohio State",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 40,
                        goalsOrPpg = "19.2 PPG",
                        assistsOrApg = "2.5 APG",
                        minutesPlayed = 1280,
                        rating = 8.9,
                        keyMetricLabel = "Speed Rating",
                        keyMetricValue = "98 Top Speed",
                        shotAccuracyOrFg = "46.8% FG"
                    ),
                    signatureSkill = "Blistering downhill rim attacks and clutch fourth-quarter shot making."
                )
            ),
            historicalPerformance = StadiumTeamHistoricalPerformance(
                championshipTitles = listOf("2012 WNBA Champions 🏆", "3x Eastern Conference Champions", "2024 Playoff Appearance"),
                allTimeRecord = "380W - 470L",
                winPercentage = "55.0% (2024 Season)",
                venueHomeRecord = "12W - 8L at Gainbridge Fieldhouse (2024 Season)",
                recentForm = listOf("W", "L", "W", "W", "L"),
                milestoneMoments = listOf(
                    "July 17, 2024: Caitlin Clark dished out 19 assists against Dallas, setting all-time WNBA single-game record.",
                    "September 2024: Set all-time WNBA single-season home attendance record at Gainbridge Fieldhouse.",
                    "October 2012: Tamika Catchings led Fever to franchise's first WNBA Championship."
                )
            ),
            nextMatchSummary = "vs New York Liberty (Semifinal Clashes)",
            nextMatchDate = "Tomorrow",
            nextMatchTime = "8:00 PM EDT"
        )
    }

    private fun getLVAcesDetails(): StadiumTeamDetails {
        return StadiumTeamDetails(
            teamId = "TEAM_LV_ACES",
            teamName = "Las Vegas Aces",
            abbreviation = "LVA",
            leagueOrCompetition = "WNBA (Women's National Basketball Association)",
            sport = "Basketball 🏀",
            flagOrLogo = "♠️",
            stadiumId = "STAD_MICHELOB",
            stadiumName = "Michelob ULTRA Arena",
            city = "Las Vegas, NV",
            headCoach = "Becky Hammon",
            foundedYear = "1997",
            primaryColorHex = 0xFFE11D48, // Crimson
            roster = listOf(
                StadiumPlayer(
                    number = 22,
                    name = "A'ja Wilson",
                    position = "PF",
                    age = 28,
                    capsOrExp = "3x WNBA MVP",
                    clubOrCollege = "South Carolina",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 40,
                        goalsOrPpg = "26.9 PPG (Record)",
                        assistsOrApg = "2.3 APG",
                        minutesPlayed = 1370,
                        rating = 9.9,
                        keyMetricLabel = "Blocks & Rebounds",
                        keyMetricValue = "11.9 RPG, 2.6 BPG",
                        shotAccuracyOrFg = "51.8% FG"
                    ),
                    signatureSkill = "Historically dominant scoring inside, mid-range touch, and Defensive Player of the Year rim denial."
                ),
                StadiumPlayer(
                    number = 12,
                    name = "Chelsea Gray",
                    position = "PG",
                    age = 32,
                    capsOrExp = "10 yrs pro (Point Gawd)",
                    clubOrCollege = "Duke",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 30,
                        goalsOrPpg = "9.8 PPG",
                        assistsOrApg = "5.0 APG",
                        minutesPlayed = 890,
                        rating = 8.9,
                        keyMetricLabel = "Clutch FG%",
                        keyMetricValue = "56.2%",
                        shotAccuracyOrFg = "42.0% FG"
                    ),
                    signatureSkill = "No-look wizard passes and legendary mid-range pull-up fadeaways under pressure."
                ),
                StadiumPlayer(
                    number = 0,
                    name = "Jackie Young",
                    position = "SG",
                    age = 27,
                    capsOrExp = "6 yrs pro",
                    clubOrCollege = "Notre Dame",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 39,
                        goalsOrPpg = "15.8 PPG",
                        assistsOrApg = "5.3 APG",
                        minutesPlayed = 1300,
                        rating = 9.0,
                        keyMetricLabel = "Physicality Rating",
                        keyMetricValue = "95 / 100",
                        shotAccuracyOrFg = "43.0% FG"
                    ),
                    signatureSkill = "Muscular bully-ball driving through contact and high-accuracy 3-point spot-ups."
                )
            ),
            historicalPerformance = StadiumTeamHistoricalPerformance(
                championshipTitles = listOf("2022 WNBA Champions 🏆", "2023 WNBA Champions 🏆", "2022 Commissioner's Cup Champions"),
                allTimeRecord = "440W - 420L",
                winPercentage = "74.5% (Past 3 seasons)",
                venueHomeRecord = "48W - 8L at Michelob ULTRA Arena (2022-2024)",
                recentForm = listOf("W", "W", "W", "L", "W"),
                milestoneMoments = listOf(
                    "September 2024: A'ja Wilson became the first player in WNBA history to score 1,000 points in a single season.",
                    "October 2023: Won back-to-back WNBA Championships, the first team to repeat in 21 years.",
                    "September 2022: Becky Hammon won championship in her debut season as head coach."
                )
            ),
            nextMatchSummary = "vs Minnesota Lynx (Western Showdown)",
            nextMatchDate = "Friday, Nov 15",
            nextMatchTime = "9:00 PM PDT"
        )
    }

    private fun getSeattleStormDetails(): StadiumTeamDetails {
        return StadiumTeamDetails(
            teamId = "TEAM_SEA_STORM",
            teamName = "Seattle Storm",
            abbreviation = "SEA",
            leagueOrCompetition = "WNBA (Women's National Basketball Association)",
            sport = "Basketball 🏀",
            flagOrLogo = "⚡",
            stadiumId = "STAD_SEATTLE",
            stadiumName = "Climate Pledge Arena",
            city = "Seattle, WA",
            headCoach = "Noelle Quinn",
            foundedYear = "2000",
            primaryColorHex = 0xFF10B981, // Green
            roster = listOf(
                StadiumPlayer(
                    number = 24,
                    name = "Jewell Loyd",
                    position = "SG",
                    age = 31,
                    capsOrExp = "9 yrs pro",
                    clubOrCollege = "Notre Dame",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 37,
                        goalsOrPpg = "19.7 PPG",
                        assistsOrApg = "3.6 APG",
                        minutesPlayed = 1250,
                        rating = 8.9,
                        keyMetricLabel = "Free Throw %",
                        keyMetricValue = "88.1%",
                        shotAccuracyOrFg = "38.0% FG"
                    ),
                    signatureSkill = "The 'Gold Mamba' fearless shot-creation and microwave scoring bursts."
                ),
                StadiumPlayer(
                    number = 3,
                    name = "Nneka Ogwumike",
                    position = "PF",
                    age = 34,
                    capsOrExp = "12 yrs pro (2016 MVP)",
                    clubOrCollege = "Stanford",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 37,
                        goalsOrPpg = "16.7 PPG",
                        assistsOrApg = "2.3 APG",
                        minutesPlayed = 1170,
                        rating = 9.1,
                        keyMetricLabel = "Rebounds",
                        keyMetricValue = "7.6 RPG",
                        shotAccuracyOrFg = "51.1% FG"
                    ),
                    signatureSkill = "Ultra-efficient footwork, smooth turnaround floaters, and leadership."
                ),
                StadiumPlayer(
                    number = 4,
                    name = "Skylar Diggins-Smith",
                    position = "PG",
                    age = 34,
                    capsOrExp = "10 yrs pro",
                    clubOrCollege = "Notre Dame",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 40,
                        goalsOrPpg = "15.1 PPG",
                        assistsOrApg = "6.4 APG",
                        minutesPlayed = 1270,
                        rating = 8.8,
                        keyMetricLabel = "Steals",
                        keyMetricValue = "1.5 SPG",
                        shotAccuracyOrFg = "42.5% FG"
                    ),
                    signatureSkill = "Relentless full-court ball pressure and quick dish assists."
                )
            ),
            historicalPerformance = StadiumTeamHistoricalPerformance(
                championshipTitles = listOf("4x WNBA Champions (2004, 2010, 2018, 2020) 🏆", "2021 Commissioner's Cup Champions"),
                allTimeRecord = "465W - 390L",
                winPercentage = "62.5%",
                venueHomeRecord = "28W - 12L at Climate Pledge Arena",
                recentForm = listOf("W", "L", "W", "L", "W"),
                milestoneMoments = listOf(
                    "October 2020: Swept Las Vegas Aces in WNBA Finals behind Sue Bird and Breanna Stewart.",
                    "October 2021: Re-opened the transformed zero-carbon Climate Pledge Arena.",
                    "September 2022: Sue Bird played historic final game before 18,104 standing fans."
                )
            ),
            nextMatchSummary = "vs Phoenix Mercury (Pacific Clash)",
            nextMatchDate = "Sunday, Nov 17",
            nextMatchTime = "6:00 PM PDT"
        )
    }

    private fun getUSWNTDetails(stadiumId: String, stadiumName: String, city: String): StadiumTeamDetails {
        return StadiumTeamDetails(
            teamId = "TEAM_USA_WOMEN",
            teamName = "United States Women's National Team",
            abbreviation = "USA",
            leagueOrCompetition = "FIFA Women's World Cup / Olympic Champions",
            sport = "Soccer ⚽",
            flagOrLogo = "🇺🇸",
            stadiumId = stadiumId,
            stadiumName = stadiumName,
            city = city,
            headCoach = "Emma Hayes",
            foundedYear = "1985",
            primaryColorHex = 0xFF2563EB, // Blue
            roster = listOf(
                StadiumPlayer(
                    number = 9,
                    name = "Mallory Swanson",
                    position = "FW",
                    age = 26,
                    capsOrExp = "95 caps",
                    clubOrCollege = "Chicago Red Stars",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 22,
                        goalsOrPpg = "14 Goals",
                        assistsOrApg = "8 Assists",
                        minutesPlayed = 1840,
                        rating = 9.5,
                        keyMetricLabel = "Olympic Gold Match-Winner",
                        keyMetricValue = "Gold Medal Goal vs Brazil",
                        shotAccuracyOrFg = "64.2% On-Target"
                    ),
                    signatureSkill = "Lethal diagonal cutting and ice-cold finishing in major tournament finals."
                ),
                StadiumPlayer(
                    number = 11,
                    name = "Sophia Smith",
                    position = "FW",
                    age = 24,
                    capsOrExp = "52 caps",
                    clubOrCollege = "Portland Thorns",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 20,
                        goalsOrPpg = "12 Goals",
                        assistsOrApg = "6 Assists",
                        minutesPlayed = 1710,
                        rating = 9.3,
                        keyMetricLabel = "Shot Creation",
                        keyMetricValue = "4.8 per 90",
                        shotAccuracyOrFg = "58.0% On-Target"
                    ),
                    signatureSkill = "1v1 isolation dribbling and thunderous strike power from distance."
                ),
                StadiumPlayer(
                    number = 5,
                    name = "Trinity Rodman",
                    position = "FW",
                    age = 22,
                    capsOrExp = "44 caps",
                    clubOrCollege = "Washington Spirit",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 21,
                        goalsOrPpg = "9 Goals",
                        assistsOrApg = "7 Assists",
                        minutesPlayed = 1760,
                        rating = 9.1,
                        keyMetricLabel = "Pressing Intensity",
                        keyMetricValue = "18 Pressures/game",
                        shotAccuracyOrFg = "55% On-Target"
                    ),
                    signatureSkill = "Electrifying speed down the flank and dynamic defensive tracking."
                ),
                StadiumPlayer(
                    number = 1,
                    name = "Alyssa Naeher",
                    position = "GK",
                    age = 36,
                    capsOrExp = "108 caps",
                    clubOrCollege = "Chicago Red Stars",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 18,
                        goalsOrPpg = "0.55 GAA",
                        assistsOrApg = "0 Assists",
                        minutesPlayed = 1680,
                        rating = 9.2,
                        keyMetricLabel = "Clean Sheets",
                        keyMetricValue = "11 Clean Sheets",
                        shotAccuracyOrFg = "84.5% Save Pct"
                    ),
                    signatureSkill = "Heroic penalty shootout saves and stone-cold leadership."
                )
            ),
            historicalPerformance = StadiumTeamHistoricalPerformance(
                championshipTitles = listOf("4x FIFA Women's World Cup Champions (1991, 1999, 2015, 2019) 🏆", "5x Olympic Gold Medals (1996, 2004, 2008, 2012, 2024) 🥇", "9x CONCACAF W Championships"),
                allTimeRecord = "570W - 85D - 72L",
                winPercentage = "78.4%",
                venueHomeRecord = "12W - 0D - 0L at this venue",
                recentForm = listOf("W", "W", "W", "W", "W"),
                milestoneMoments = listOf(
                    "August 2024: Won Olympic Gold in Paris under manager Emma Hayes defeating Brazil 1-0.",
                    "July 2019: Defeated Netherlands 2-0 to repeat as FIFA Women's World Cup champions.",
                    "July 1999: Historic Rose Bowl final victory before 90,185 fans defined modern women's soccer."
                )
            ),
            nextMatchSummary = "vs England Lionesses (Wembley Showcase)",
            nextMatchDate = "Saturday, Nov 30",
            nextMatchTime = "12:30 PM EST"
        )
    }

    private fun getAngelCFDetails(): StadiumTeamDetails {
        return StadiumTeamDetails(
            teamId = "TEAM_ANGEL_CITY",
            teamName = "Angel City FC",
            abbreviation = "ACFC",
            leagueOrCompetition = "NWSL (National Women's Soccer League)",
            sport = "Soccer ⚽",
            flagOrLogo = "👼",
            stadiumId = "STAD_LOS_ANGELES",
            stadiumName = "BMO Stadium / SoFi Stadium",
            city = "Los Angeles, CA",
            headCoach = "Becki Tweed",
            foundedYear = "2020",
            primaryColorHex = 0xFFF43F5E, // Coral / Black
            roster = listOf(
                StadiumPlayer(
                    number = 2,
                    name = "Sydney Leroux",
                    position = "FW",
                    age = 34,
                    capsOrExp = "77 USWNT caps",
                    clubOrCollege = "UCLA",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 24,
                        goalsOrPpg = "7 Goals",
                        assistsOrApg = "2 Assists",
                        minutesPlayed = 1680,
                        rating = 8.5,
                        keyMetricLabel = "Aerial Duels Won",
                        keyMetricValue = "48",
                        shotAccuracyOrFg = "54% On-Target"
                    ),
                    signatureSkill = "Bicycle kick acrobatics and ferocious physical hold-up play."
                ),
                StadiumPlayer(
                    number = 99,
                    name = "Claire Emslie",
                    position = "FW",
                    age = 30,
                    capsOrExp = "60 Scotland caps",
                    clubOrCollege = "Florida Atlantic",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 23,
                        goalsOrPpg = "7 Goals",
                        assistsOrApg = "4 Assists",
                        minutesPlayed = 1750,
                        rating = 8.6,
                        keyMetricLabel = "Crosses Completed",
                        keyMetricValue = "64",
                        shotAccuracyOrFg = "57% On-Target"
                    ),
                    signatureSkill = "Pinpoint bending crosses from set pieces and dead-ball wizardry."
                )
            ),
            historicalPerformance = StadiumTeamHistoricalPerformance(
                championshipTitles = listOf("2023 NWSL Playoff Quarterfinalists", "Community Impact Excellence Award"),
                allTimeRecord = "28W - 19D - 25L",
                winPercentage = "52.0%",
                venueHomeRecord = "16W - 8D - 8L in Los Angeles",
                recentForm = listOf("W", "D", "L", "W", "W"),
                milestoneMoments = listOf(
                    "April 2022: Played inaugural NWSL home game before 22,000 sellout crowd in Los Angeles.",
                    "October 2023: Clinched historic first postseason playoff berth with thrilling Decision Day victory."
                )
            ),
            nextMatchSummary = "vs San Diego Wave (California Clásico)",
            nextMatchDate = "Saturday, Nov 23",
            nextMatchTime = "7:00 PM PST"
        )
    }

    private fun getPortlandThornsDetails(): StadiumTeamDetails {
        return StadiumTeamDetails(
            teamId = "TEAM_POR_THORNS",
            teamName = "Portland Thorns FC",
            abbreviation = "POR",
            leagueOrCompetition = "NWSL (National Women's Soccer League)",
            sport = "Soccer ⚽",
            flagOrLogo = "🌹",
            stadiumId = "STAD_PORTLAND",
            stadiumName = "Providence Park",
            city = "Portland, OR",
            headCoach = "Rob Gale",
            foundedYear = "2012",
            primaryColorHex = 0xFFDC2626, // Red
            roster = listOf(
                StadiumPlayer(
                    number = 9,
                    name = "Sophia Smith",
                    position = "FW",
                    age = 24,
                    capsOrExp = "2022 NWSL MVP",
                    clubOrCollege = "Stanford",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 20,
                        goalsOrPpg = "12 Goals",
                        assistsOrApg = "6 Assists",
                        minutesPlayed = 1680,
                        rating = 9.4,
                        keyMetricLabel = "Golden Boot",
                        keyMetricValue = "2nd in NWSL",
                        shotAccuracyOrFg = "61% On-Target"
                    ),
                    signatureSkill = "Unstoppable turn of speed and curling finishes inside the far post."
                ),
                StadiumPlayer(
                    number = 12,
                    name = "Christine Sinclair",
                    position = "FW",
                    age = 41,
                    capsOrExp = "331 Canada caps (World Record 190 Int'l Goals)",
                    clubOrCollege = "Portland",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 19,
                        goalsOrPpg = "4 Goals",
                        assistsOrApg = "2 Assists",
                        minutesPlayed = 920,
                        rating = 8.8,
                        keyMetricLabel = "Legend Status",
                        keyMetricValue = "190 Int'l Goals",
                        shotAccuracyOrFg = "68% On-Target"
                    ),
                    signatureSkill = "All-time international football goalscoring queen and legendary leadership."
                )
            ),
            historicalPerformance = StadiumTeamHistoricalPerformance(
                championshipTitles = listOf("3x NWSL Champions (2013, 2017, 2022) 🏆", "2x NWSL Shield Winners (2016, 2021)", "2021 Challenge Cup Champions"),
                allTimeRecord = "125W - 60D - 65L",
                winPercentage = "62.0%",
                venueHomeRecord = "78W - 25D - 20L at Providence Park",
                recentForm = listOf("W", "L", "W", "W", "D"),
                milestoneMoments = listOf(
                    "October 2022: Sophia Smith named youngest MVP in NWSL history leading Thorns to 3rd championship.",
                    "October 2024: Christine Sinclair celebrated final home game before 25,218 roaring fans in the Rose City."
                )
            ),
            nextMatchSummary = "vs Gotham FC (NWSL Semifinal)",
            nextMatchDate = "Sunday, Nov 17",
            nextMatchTime = "3:00 PM EST"
        )
    }

    private fun getMexicoNationalTeamDetails(): StadiumTeamDetails {
        return StadiumTeamDetails(
            teamId = "TEAM_MEX_WOMEN",
            teamName = "Mexico Women's National Team",
            abbreviation = "MEX",
            leagueOrCompetition = "CONCACAF W / International",
            sport = "Soccer ⚽",
            flagOrLogo = "🇲🇽",
            stadiumId = "STAD_MEXICO_CITY",
            stadiumName = "Estadio Azteca",
            city = "Mexico City, Mexico",
            headCoach = "Pedro López",
            foundedYear = "1998",
            primaryColorHex = 0xFF15803D, // Green
            roster = listOf(
                StadiumPlayer(
                    number = 7,
                    name = "Kiana Palacios",
                    position = "FW",
                    age = 28,
                    capsOrExp = "45 caps",
                    clubOrCollege = "Club América",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 18,
                        goalsOrPpg = "8 Goals",
                        assistsOrApg = "4 Assists",
                        minutesPlayed = 1450,
                        rating = 8.7,
                        keyMetricLabel = "Conversion Rate",
                        keyMetricValue = "24.5%",
                        shotAccuracyOrFg = "58% On-Target"
                    ),
                    signatureSkill = "Deadly first touch in the box and aerial flick headers."
                ),
                StadiumPlayer(
                    number = 10,
                    name = "Jacqueline Ovalle",
                    position = "MF",
                    age = 25,
                    capsOrExp = "58 caps",
                    clubOrCollege = "Tigres UANL",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 20,
                        goalsOrPpg = "9 Goals",
                        assistsOrApg = "7 Assists",
                        minutesPlayed = 1680,
                        rating = 9.2,
                        keyMetricLabel = "Take-On Success",
                        keyMetricValue = "72.4%",
                        shotAccuracyOrFg = "62% On-Target"
                    ),
                    signatureSkill = "'La Maga' breathtaking chip goals and sensational solo slalom runs."
                )
            ),
            historicalPerformance = StadiumTeamHistoricalPerformance(
                championshipTitles = listOf("2023 Pan American Games Gold Medal 🥇", "2023 Central American Games Gold Medal", "2024 W Gold Cup Semifinalists"),
                allTimeRecord = "165W - 42D - 95L",
                winPercentage = "62.4%",
                venueHomeRecord = "24W - 6D - 4L at Estadio Azteca",
                recentForm = listOf("W", "W", "L", "W", "W"),
                milestoneMoments = listOf(
                    "February 2024: Defeated USWNT 2-0 in historic W Gold Cup group stage match behind Ovalle's wonder-goal.",
                    "November 2023: Won first-ever Pan American Games Gold Medal in Santiago, Chile."
                )
            ),
            nextMatchSummary = "vs Colombia (International Friendly)",
            nextMatchDate = "Saturday, Nov 30",
            nextMatchTime = "6:00 PM CST"
        )
    }

    private fun getCanadaNationalTeamDetails(): StadiumTeamDetails {
        return StadiumTeamDetails(
            teamId = "TEAM_CAN_WOMEN",
            teamName = "Canada Women's National Team",
            abbreviation = "CAN",
            leagueOrCompetition = "FIFA / Olympic Champions",
            sport = "Soccer ⚽",
            flagOrLogo = "🇨🇦",
            stadiumId = "STAD_TORONTO",
            stadiumName = "BMO Field",
            city = "Toronto, ON",
            headCoach = "Bev Priestman",
            foundedYear = "1986",
            primaryColorHex = 0xFFDC2626, // Red
            roster = listOf(
                StadiumPlayer(
                    number = 10,
                    name = "Jessie Fleming",
                    position = "MF",
                    age = 26,
                    capsOrExp = "132 caps (Captain)",
                    clubOrCollege = "Portland Thorns",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 22,
                        goalsOrPpg = "6 Goals",
                        assistsOrApg = "8 Assists",
                        minutesPlayed = 1910,
                        rating = 9.3,
                        keyMetricLabel = "Pass Accuracy",
                        keyMetricValue = "88.2%",
                        shotAccuracyOrFg = "56% On-Target"
                    ),
                    signatureSkill = "Impeccable passing tempo, penalty precision, and captain's composure."
                ),
                StadiumPlayer(
                    number = 1,
                    name = "Kailen Sheridan",
                    position = "GK",
                    age = 29,
                    capsOrExp = "54 caps",
                    clubOrCollege = "San Diego Wave",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 20,
                        goalsOrPpg = "0.72 GAA",
                        assistsOrApg = "1 Assist",
                        minutesPlayed = 1800,
                        rating = 9.0,
                        keyMetricLabel = "Save Pct",
                        keyMetricValue = "81.4%",
                        shotAccuracyOrFg = "9 Clean Sheets"
                    ),
                    signatureSkill = "Spectacular fingertip saves and 70-yard distribution kicks."
                )
            ),
            historicalPerformance = StadiumTeamHistoricalPerformance(
                championshipTitles = listOf("2021 Olympic Gold Medallists (Tokyo) 🥇", "2x Olympic Bronze Medallists (2012, 2016) 🥉", "2x CONCACAF W Champions"),
                allTimeRecord = "215W - 62D - 110L",
                winPercentage = "64.8%",
                venueHomeRecord = "18W - 4D - 3L at BMO Field",
                recentForm = listOf("W", "W", "W", "D", "W"),
                milestoneMoments = listOf(
                    "August 2021: Won Olympic Gold in Tokyo defeating Sweden in dramatic penalty shootout.",
                    "Summer 2024: Overcame 6-point Olympic deduction to win all three group stage matches in Paris."
                )
            ),
            nextMatchSummary = "vs Germany (International Showcase)",
            nextMatchDate = "Friday, Nov 29",
            nextMatchTime = "7:30 PM EST"
        )
    }

    private fun getDefaultTeamDetails(stadiumId: String): StadiumTeamDetails {
        return StadiumTeamDetails(
            teamId = "TEAM_HOST_$stadiumId",
            teamName = "Host Venue Showcase XI",
            abbreviation = "HST",
            leagueOrCompetition = "Elite Tournament Championship",
            sport = "Multi-Sport 🌟",
            flagOrLogo = "🏟️",
            stadiumId = stadiumId,
            stadiumName = "Championship Venue",
            city = "Host City",
            headCoach = "Elite Technical Staff",
            foundedYear = "2024",
            primaryColorHex = 0xFF6366F1, // Indigo
            roster = listOf(
                StadiumPlayer(
                    number = 10,
                    name = "Tournament MVP",
                    position = "FW",
                    age = 25,
                    capsOrExp = "45 caps",
                    clubOrCollege = "Championship Squad",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 18,
                        goalsOrPpg = "12 Goals",
                        assistsOrApg = "8 Assists",
                        minutesPlayed = 1580,
                        rating = 9.1,
                        keyMetricLabel = "Top Scorer",
                        keyMetricValue = "1st Place",
                        shotAccuracyOrFg = "62% On-Target"
                    ),
                    signatureSkill = "Game-changing finishing and visionary playmaker leadership."
                ),
                StadiumPlayer(
                    number = 1,
                    name = "Star Goalkeeper",
                    position = "GK",
                    age = 28,
                    capsOrExp = "60 caps",
                    clubOrCollege = "Elite League",
                    stats = StadiumPlayerStats(
                        matchesPlayed = 18,
                        goalsOrPpg = "0.78 GAA",
                        assistsOrApg = "0 Assists",
                        minutesPlayed = 1620,
                        rating = 8.9,
                        keyMetricLabel = "Clean Sheets",
                        keyMetricValue = "8 Clean Sheets",
                        shotAccuracyOrFg = "82% Save Pct"
                    ),
                    signatureSkill = "Commanding box presence and acrobatic penalty stops."
                )
            ),
            historicalPerformance = StadiumTeamHistoricalPerformance(
                championshipTitles = listOf("Championship Host Venue Honors", "Premier International Venue"),
                allTimeRecord = "34W - 12D - 8L",
                winPercentage = "70.8%",
                venueHomeRecord = "14W - 2D - 1L at this venue",
                recentForm = listOf("W", "W", "W", "D", "W"),
                milestoneMoments = listOf(
                    "Historic inaugural tournament fixture before a capacity crowd.",
                    "Record attendance milestone set during championship weekend."
                )
            ),
            nextMatchSummary = "Championship Quarter-Final",
            nextMatchDate = "Upcoming Weekend",
            nextMatchTime = "8:00 PM Local"
        )
    }
}
