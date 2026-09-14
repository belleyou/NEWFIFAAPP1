package com.example.model

data class GalleryPhoto(
    val url: String,
    val title: String,
    val caption: String,
    val category: PhotoCategory, // HISTORIC_MOMENTS, EXTERIOR, INTERIOR, ARCHITECTURE, SEATING
    val architecturalNote: String,
    val momentYear: String = ""
)

enum class PhotoCategory(val displayName: String) {
    ALL("All Views"),
    HISTORIC_MOMENTS("Historic Moments"),
    ARCHITECTURE("Architecture"),
    EXTERIOR("Exterior Facade"),
    INTERIOR("Interior & Pitch"),
    SEATING("Seating & Sightlines")
}

data class SeatingSection(
    val sectionId: String,
    val name: String,
    val tier: String,
    val capacity: String,
    val viewRating: String, // e.g., "5.0 ★ Prime Sideline"
    val colorHex: Long,
    val features: List<String>
)

data class StadiumArchitecturalDetails(
    val stadiumId: String,
    val architect: String,
    val yearOpened: String,
    val roofType: String,
    val facadeMaterial: String,
    val pitchType: String,
    val architecturalStyle: String,
    val seatingSections: List<SeatingSection>,
    val photos: List<GalleryPhoto>
)

object StadiumGalleryDataProvider {

    fun getGalleryForStadium(stadiumId: String, stadiumName: String): StadiumArchitecturalDetails {
        val photos = getPhotosForStadium(stadiumId)
        val sections = getSeatingSectionsForStadium(stadiumId)
        
        return when (stadiumId) {
            "STAD_NEW_YORK" -> StadiumArchitecturalDetails(
                stadiumId = stadiumId,
                architect = "HOK Sport (Populous), Skanska",
                yearOpened = "2010 (Renovated 2025)",
                roofType = "Open-Air Canopy with LED Louver System",
                facadeMaterial = "Aluminum Louvers & Dynamic Color LED Panels",
                pitchType = "Vacuum-Ventilated Natural Grass",
                architecturalStyle = "Modernist Dynamic Louver Architecture",
                seatingSections = sections,
                photos = photos
            )
            "STAD_MEXICO_CITY" -> StadiumArchitecturalDetails(
                stadiumId = stadiumId,
                architect = "Pedro Ramírez Vázquez & Rafael Mijares",
                yearOpened = "1966 (Modernization)",
                roofType = "Cantilevered Continuous Ring Canopy",
                facadeMaterial = "Concrete Pillars & Solar Glass Facade",
                pitchType = "Reinforced Hybrid Turf",
                architecturalStyle = "Colossal Aztec Temple Neo-Brutalism",
                seatingSections = sections,
                photos = photos
            )
            "STAD_LOS_ANGELES" -> StadiumArchitecturalDetails(
                stadiumId = stadiumId,
                architect = "HKS, Inc.",
                yearOpened = "2020",
                roofType = "Translucent ETFE Single-Layer Canopy",
                facadeMaterial = "Perforated Anodized Aluminum Panels",
                pitchType = "Matrix Turf System with Sub-Air Drainage",
                architecturalStyle = "Biomorphic Indoor-Outdoor Aerodynamic Bowl",
                seatingSections = sections,
                photos = photos
            )
            "STAD_ATLANTA" -> StadiumArchitecturalDetails(
                stadiumId = stadiumId,
                architect = "tvsdesign, HOK, Goode Van Slyke",
                yearOpened = "2017",
                roofType = "8-Petal Oculi Retractable Roof",
                facadeMaterial = "EFTE Facade & High-Translucent Polymer Wings",
                pitchType = "FieldTurf CORE Hybrid Precision System",
                architecturalStyle = "Kinetic Futuristic Geometry",
                seatingSections = sections,
                photos = photos
            )
            "STAD_DALLAS" -> StadiumArchitecturalDetails(
                stadiumId = stadiumId,
                architect = "HKS, Inc.",
                yearOpened = "2009",
                roofType = "Twin 290-meter Steel Arch Retractable Glass Roof",
                facadeMaterial = "Inclined Canted Glass Wall System",
                pitchType = "Modular FIFA Grade Natural Turf Trays",
                architecturalStyle = "Monumental High-Tech Glass & Steel Arch",
                seatingSections = sections,
                photos = photos
            )
            else -> StadiumArchitecturalDetails(
                stadiumId = stadiumId,
                architect = "Populous & HKS Architecture",
                yearOpened = "2015",
                roofType = "Cantilevered Weather Shield Roof Canopy",
                facadeMaterial = "Perforated Steel & Energy-Smart Glass",
                pitchType = "SISGrass Hybrid Pitch",
                architecturalStyle = "Contemporary World-Class Arena Design",
                seatingSections = sections,
                photos = photos
            )
        }
    }

    private fun getSeatingSectionsForStadium(stadiumId: String): List<SeatingSection> {
        return listOf(
            SeatingSection(
                sectionId = "SEC_VIP",
                name = "VIP Presidential Skybox & Club Row",
                tier = "Exclusive Level 200",
                capacity = "2,400 Seats",
                viewRating = "5.0 ★ Panoramic Midfield",
                colorHex = 0xFFFFD700, // Gold
                features = listOf("Private Lounge Access", "Gourmet Catering", "Luxury Padded Seats", "In-seat Service")
            ),
            SeatingSection(
                sectionId = "SEC_LOWER_MID",
                name = "Lower Bowl Sideline (100 Level)",
                tier = "Lower Tier",
                capacity = "22,500 Seats",
                viewRating = "4.9 ★ Pitch-Side Proximity",
                colorHex = 0xFF10B981, // Emerald
                features = listOf("Player Tunnel Proximity", "Sub-Air Climate Control", "Optimal Tactical View")
            ),
            SeatingSection(
                sectionId = "SEC_GOAL_CURVE",
                name = "Supporters End & Goal Curve",
                tier = "Lower / Middle Tier",
                capacity = "18,000 Seats",
                viewRating = "4.7 ★ Electric Atmosphere",
                colorHex = 0xFF38BDF8, // Cyan
                features = listOf("Atmosphere Safe Standing", "Direct Behind-Goal Penalty View", "Ultra-wide LED View")
            ),
            SeatingSection(
                sectionId = "SEC_CLUB_UPPER",
                name = "Club Mezzanine & Premium Suites",
                tier = "Middle Tier 200/300",
                capacity = "14,200 Seats",
                viewRating = "4.8 ★ Elevated Angle",
                colorHex = 0xFF8B5CF6, // Purple
                features = listOf("Climate Controlled Concours", "Craft Beverage Bars", "Dedicated Fast-Track Gates")
            ),
            SeatingSection(
                sectionId = "SEC_UPPER_BOWL",
                name = "Upper Skyline Deck (400 Level)",
                tier = "Upper Deck",
                capacity = "28,000 Seats",
                viewRating = "4.5 ★ Full Stadium Panorama",
                colorHex = 0xFFF59E0B, // Amber
                features = listOf("Unobstructed Aerial View", "Direct Access to Concessions", "Shaded Roof Canopy")
            ),
            SeatingSection(
                sectionId = "SEC_PRESS_BOX",
                name = "FIFA International Media Press Box",
                tier = "Press Level",
                capacity = "1,200 Workstations",
                viewRating = "5.0 ★ Broadcast View",
                colorHex = 0xFFEC4899, // Pink
                features = listOf("High-Speed Fiber Optics", "Live Tactical Analytics Monitor", "Acoustic Shielding")
            )
        )
    }

    private fun getPhotosForStadium(stadiumId: String): List<GalleryPhoto> {
        return when (stadiumId) {
            "STAD_CPKC" -> listOf(
                GalleryPhoto(
                    url = "https://images.unsplash.com/photo-1510563800743-aed236490d08?auto=format&fit=crop&w=1200&q=80",
                    title = "Inaugural Opening Match Historic Sellout",
                    caption = "March 16, 2024: The historic first match at CPKC Stadium, the world's first purpose-built arena for a women's pro team, ending in a dramatic 5-4 victory.",
                    category = PhotoCategory.HISTORIC_MOMENTS,
                    architecturalNote = "11,500 roaring fans along the scenic Missouri River front celebrated this historic milestone for women's athletics.",
                    momentYear = "2024"
                ),
                GalleryPhoto(
                    url = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?auto=format&fit=crop&w=1200&q=80",
                    title = "Riverfront Canopy Architecture",
                    caption = "Open-air cantilevered roof framing the picturesque Missouri River skyline with natural river breezes.",
                    category = PhotoCategory.ARCHITECTURE,
                    architecturalNote = "Custom-engineered lightweight steel truss canopy designed to maximize pitch ventilation while shading fans.",
                    momentYear = "2024"
                ),
                GalleryPhoto(
                    url = "https://images.unsplash.com/photo-1522778119026-d647f0596c20?auto=format&fit=crop&w=1200&q=80",
                    title = "Pristine Kentucky Bluegrass Pitch",
                    caption = "State-of-the-art natural hybrid pitch surface engineered with sub-air thermal regulation.",
                    category = PhotoCategory.INTERIOR,
                    architecturalNote = "Root-zone monitoring sensors and rapid-drainage gravel sub-layers maintain perfect playing conditions year-round."
                ),
                GalleryPhoto(
                    url = "https://images.unsplash.com/photo-1541252260730-0412e8e2108e?auto=format&fit=crop&w=1200&q=80",
                    title = "Steep Pitch-Side Supporters Section",
                    caption = "Safe-standing supporters section designed with a 32-degree rake angle placing fans just yards from the touchline.",
                    category = PhotoCategory.SEATING,
                    architecturalNote = "Direct proximity sightlines create an electric, high-decibel home pitch advantage."
                )
            )
            "STAD_BARCLAYS" -> listOf(
                GalleryPhoto(
                    url = "https://images.unsplash.com/photo-1504450758481-7338eba7524a?auto=format&fit=crop&w=1200&q=80",
                    title = "2024 WNBA Championship Trophy Celebration",
                    caption = "October 20, 2024: New York Liberty lift their first WNBA Championship trophy before a deafening sellout crowd of 18,090.",
                    category = PhotoCategory.HISTORIC_MOMENTS,
                    architecturalNote = "Confetti erupted across the weathered steel arena bowl as Sabrina Ionescu and Breanna Stewart hoisted the title.",
                    momentYear = "2024"
                ),
                GalleryPhoto(
                    url = "https://images.unsplash.com/photo-1518063319789-7217e6706b04?auto=format&fit=crop&w=1200&q=80",
                    title = "Weathered Corten Steel Oculus Facade",
                    caption = "SHoP Architects' iconic weathered steel lattice canopy and 360-degree digital oculus ring.",
                    category = PhotoCategory.ARCHITECTURE,
                    architecturalNote = "Composed of 12,000 uniquely fabricated pre-weathered steel panels that naturally oxidize over time."
                ),
                GalleryPhoto(
                    url = "https://images.unsplash.com/photo-1519766304817-4f37bda74a29?auto=format&fit=crop&w=1200&q=80",
                    title = "Seafoam Court & Arena Bowl",
                    caption = "Center court illuminated with the Liberty's iconic seafoam green hardwood and high-output LED spotlight rig.",
                    category = PhotoCategory.INTERIOR,
                    architecturalNote = "Acoustically treated ceiling baffle system channels crowd decibels straight down to court level."
                )
            )
            "STAD_GAINBRIDGE" -> listOf(
                GalleryPhoto(
                    url = "https://images.unsplash.com/photo-1519766304817-4f37bda74a29?auto=format&fit=crop&w=1200&q=80",
                    title = "Record-Breaking Rookie Assist Record Night",
                    caption = "Historic 19-assist performance by Caitlin Clark shattering the all-time WNBA single-game record before a sellout arena.",
                    category = PhotoCategory.HISTORIC_MOMENTS,
                    architecturalNote = "Gainbridge set modern attendance milestones as basketball fans gathered from all 50 states.",
                    momentYear = "2024"
                ),
                GalleryPhoto(
                    url = "https://images.unsplash.com/photo-1486286701208-1d58e9338013?auto=format&fit=crop&w=1200&q=80",
                    title = "Fieldhouse Retro Brick & Glass Facade",
                    caption = "Architectural homage to historic Indiana high school gymnasiums blended with glass modernism.",
                    category = PhotoCategory.ARCHITECTURE,
                    architecturalNote = "Features exposed steel trusses, warm brick pillars, and expansive street-facing glass atriums."
                ),
                GalleryPhoto(
                    url = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?auto=format&fit=crop&w=1200&q=80",
                    title = "Center-Hung Mega LED Scoreboard",
                    caption = "Huge high-definition curved video board offering 360-degree game replays and telemetry stats.",
                    category = PhotoCategory.INTERIOR,
                    architecturalNote = "Installed during the recent $360M Fieldhouse of the Future comprehensive modernization."
                )
            )
            "STAD_MICHELOB" -> listOf(
                GalleryPhoto(
                    url = "https://images.unsplash.com/photo-1518609878373-06d740f60d8b?auto=format&fit=crop&w=1200&q=80",
                    title = "Back-to-Back Championship Banner Unfurling",
                    caption = "Las Vegas Aces celebrate back-to-back WNBA Championships under the leadership of MVP A'ja Wilson and Becky Hammon.",
                    category = PhotoCategory.HISTORIC_MOMENTS,
                    architecturalNote = "The arena transformed into an electric fortress with full championship ceremonies and pyro.",
                    momentYear = "2023"
                ),
                GalleryPhoto(
                    url = "https://images.unsplash.com/photo-1518063319789-7217e6706b04?auto=format&fit=crop&w=1200&q=80",
                    title = "Mandalay Bay Entertainment Strip Architecture",
                    caption = "Sleek contemporary venue structure embedded directly into the Las Vegas resort corridor.",
                    category = PhotoCategory.EXTERIOR,
                    architecturalNote = "Designed for rapid transition between high-stakes basketball and premier global combat sports."
                )
            )
            "STAD_NEW_YORK" -> listOf(
                GalleryPhoto(
                    url = "https://images.unsplash.com/photo-1518063319789-7217e6706b04?auto=format&fit=crop&w=1200&q=80",
                    title = "Historic World Cup Opening Ceremony",
                    caption = "Grand illumination of MetLife Stadium hosting over 82,500 global fans in dynamic colors.",
                    category = PhotoCategory.HISTORIC_MOMENTS,
                    architecturalNote = "Programmed dynamic LED louver facade displaying colors of all competing federations.",
                    momentYear = "2026"
                ),
                GalleryPhoto(
                    url = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?auto=format&fit=crop&w=1200&q=80",
                    title = "Aluminum Louver Architecture",
                    caption = "Over 1.2 miles of curved aluminum louvers allowing natural ventilation while creating an iconic silhouette.",
                    category = PhotoCategory.ARCHITECTURE,
                    architecturalNote = "Designed by Populous and Skanska to withstand severe Atlantic coastal weather patterns."
                ),
                GalleryPhoto(
                    url = "https://images.unsplash.com/photo-1522778119026-d647f0596c20?auto=format&fit=crop&w=1200&q=80",
                    title = "FIFA Grade Hybrid Pitch & Grandstand",
                    caption = "Deep-tier lower bowl framing the pristine natural turf pitch with vacuum air regulation.",
                    category = PhotoCategory.INTERIOR,
                    architecturalNote = "Sub-surface root warming coils maintain optimum 65°F turf root temperature during matches."
                )
            )
            "STAD_MEXICO_CITY" -> listOf(
                GalleryPhoto(
                    url = "https://images.unsplash.com/photo-1518063319789-7217e6706b04?auto=format&fit=crop&w=1200&q=80",
                    title = "Pelé & Maradona World Cup Glory",
                    caption = "The iconic venue where Pelé hoisted the 1970 trophy and Maradona scored the 'Goal of the Century' in 1986.",
                    category = PhotoCategory.HISTORIC_MOMENTS,
                    architecturalNote = "The only stadium on earth to host three different FIFA World Cup tournament finals (1970, 1986, 2026).",
                    momentYear = "1970 / 1986"
                ),
                GalleryPhoto(
                    url = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?auto=format&fit=crop&w=1200&q=80",
                    title = "Monumental Brutalist Aztec Pillars",
                    caption = "Pedro Ramírez Vázquez's awe-inspiring concrete ring and monumental structural cantilevers.",
                    category = PhotoCategory.ARCHITECTURE,
                    architecturalNote = "Built with reinforced volcanic rock concrete inspired by pre-Columbian pyramids."
                ),
                GalleryPhoto(
                    url = "https://images.unsplash.com/photo-1522778119026-d647f0596c20?auto=format&fit=crop&w=1200&q=80",
                    title = "Colossal 87,000 Spectator Cauldron",
                    caption = "Panoramic view across the steep tiers echoing with the legendary Mexican roar.",
                    category = PhotoCategory.INTERIOR,
                    architecturalNote = "High-altitude bowl (2,240m above sea level) creating unique ball flight aerodynamics."
                )
            )
            else -> listOf(
                GalleryPhoto(
                    url = "https://images.unsplash.com/photo-1518063319789-7217e6706b04?auto=format&fit=crop&w=1200&q=80",
                    title = "Championship Match Historic Celebration",
                    caption = "Historic championship showdown before a capacity crowd with synchronized pyrotechnics.",
                    category = PhotoCategory.HISTORIC_MOMENTS,
                    architecturalNote = "A landmark sporting triumph etched in tournament history before roaring fans.",
                    momentYear = "2025"
                ),
                GalleryPhoto(
                    url = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?auto=format&fit=crop&w=1200&q=80",
                    title = "Cantilevered Roof Canopy Structure",
                    caption = "Precision steel truss canopy structure protecting spectators from weather while optimizing turf sunlight.",
                    category = PhotoCategory.ARCHITECTURE,
                    architecturalNote = "Aerodynamically tuned to channel breeze through the upper deck while resisting extreme wind loads."
                ),
                GalleryPhoto(
                    url = "https://images.unsplash.com/photo-1522778119026-d647f0596c20?auto=format&fit=crop&w=1200&q=80",
                    title = "Panoramic Pitch Bowl & Turf",
                    caption = "Panoramic wide-angle view from the center circle towards the massive grandstand and canopy roof.",
                    category = PhotoCategory.INTERIOR,
                    architecturalNote = "Engineered with 360-degree acoustics to amplify crowd roar down onto the pitch surface."
                ),
                GalleryPhoto(
                    url = "https://images.unsplash.com/photo-1541252260730-0412e8e2108e?auto=format&fit=crop&w=1200&q=80",
                    title = "Lower Tier Pitch-Side Perspective",
                    caption = "View from the player bench looking out across the pristine hybrid grass pitch toward goal stands.",
                    category = PhotoCategory.SEATING,
                    architecturalNote = "Seating pitch gradient set at 34 degrees for maximum sightline efficiency without obstruction."
                )
            )
        }
    }
}
