package com.example.model

data class HostStadium(
    val id: String,
    val name: String,
    val fifaName: String,
    val city: String,
    val country: String,
    val capacity: String,
    val latitude: Double,
    val longitude: Double,
    val fact: String,
    val image: String
)

object HostStadiumDataProvider {
    val hostStadiums = listOf(
        HostStadium(
            id = "STAD_VANCOUVER",
            name = "BC Place",
            fifaName = "Vancouver Stadium",
            city = "Vancouver",
            country = "Canada",
            capacity = "54,500",
            latitude = 49.2767,
            longitude = -123.1120,
            fact = "Features a massive retractable roof and a beautiful harbor view.",
            image = "https://images.unsplash.com/photo-1517649763962-0c623066013b?auto=format&fit=crop&w=800&q=80"
        ),
        HostStadium(
            id = "STAD_TORONTO",
            name = "BMO Field",
            fifaName = "Toronto Stadium",
            city = "Toronto",
            country = "Canada",
            capacity = "45,736",
            latitude = 43.6328,
            longitude = -79.4186,
            fact = "Canada's premier soccer-specific stadium, situated right near Lake Ontario.",
            image = "https://images.unsplash.com/photo-1568194157720-8eae79a3761a?auto=format&fit=crop&w=800&q=80"
        ),
        HostStadium(
            id = "STAD_MEXICO_CITY",
            name = "Estadio Azteca",
            fifaName = "Azteca Stadium",
            city = "Mexico City",
            country = "Mexico",
            capacity = "87,523",
            latitude = 19.3029,
            longitude = -99.1505,
            fact = "The legendary temple of football, hosting its third historic World Cup.",
            image = "https://images.unsplash.com/photo-1510563800743-aed236490d08?auto=format&fit=crop&w=800&q=80"
        ),
        HostStadium(
            id = "STAD_MONTERREY",
            name = "Estadio BBVA",
            fifaName = "Monterrey Stadium",
            city = "Monterrey",
            country = "Mexico",
            capacity = "53,500",
            latitude = 25.6692,
            longitude = -100.2443,
            fact = "Famous for its breathtaking view of the Cerro de la Silla mountain.",
            image = "https://images.unsplash.com/photo-1522778119026-d647f0596c20?auto=format&fit=crop&w=800&q=80"
        ),
        HostStadium(
            id = "STAD_GUADALAJARA",
            name = "Estadio Akron",
            fifaName = "Guadalajara Stadium",
            city = "Guadalajara",
            country = "Mexico",
            capacity = "48,071",
            latitude = 20.6819,
            longitude = -103.4627,
            fact = "Designed to resemble a volcano, with a roof shaped like a cloud.",
            image = "https://images.unsplash.com/photo-1541252260730-0412e8e2108e?auto=format&fit=crop&w=800&q=80"
        ),
        HostStadium(
            id = "STAD_SEATTLE",
            name = "Lumen Field",
            fifaName = "Seattle Stadium",
            city = "Seattle",
            country = "United States",
            capacity = "69,000",
            latitude = 47.5952,
            longitude = -122.3316,
            fact = "Renowned for its unique design and deafening acoustics.",
            image = "https://images.unsplash.com/photo-1431324155629-1a6edd1dec1d?auto=format&fit=crop&w=800&q=80"
        ),
        HostStadium(
            id = "STAD_SAN_FRANCISCO",
            name = "Levi's Stadium",
            fifaName = "San Francisco Bay Area Stadium",
            city = "Santa Clara",
            country = "United States",
            capacity = "68,500",
            latitude = 37.4030,
            longitude = -121.9698,
            fact = "One of the most technologically advanced and sustainable stadiums in the world.",
            image = "https://images.unsplash.com/photo-1556486284-a15d2fef87f0?auto=format&fit=crop&w=800&q=80"
        ),
        HostStadium(
            id = "STAD_LOS_ANGELES",
            name = "SoFi Stadium",
            fifaName = "Los Angeles Stadium",
            city = "Inglewood",
            country = "United States",
            capacity = "70,240",
            latitude = 33.9535,
            longitude = -118.3390,
            fact = "State-of-the-art indoor-outdoor architectural marvel in Inglewood.",
            image = "https://images.unsplash.com/photo-1505373877841-8d25f7d46678?auto=format&fit=crop&w=800&q=80"
        ),
        HostStadium(
            id = "STAD_HOUSTON",
            name = "NRG Stadium",
            fifaName = "Houston Stadium",
            city = "Houston",
            country = "United States",
            capacity = "72,220",
            latitude = 29.6847,
            longitude = -95.4082,
            fact = "The world's first retractable-roof NFL facility, known for its electric atmosphere.",
            image = "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?auto=format&fit=crop&w=800&q=80"
        ),
        HostStadium(
            id = "STAD_DALLAS",
            name = "AT&T Stadium",
            fifaName = "Dallas Stadium",
            city = "Arlington",
            country = "United States",
            capacity = "80,000",
            latitude = 32.7473,
            longitude = -97.0928,
            fact = "Nicknamed 'Jerry World', boasts one of the world's largest HD video screens.",
            image = "https://images.unsplash.com/photo-1504156806559-3736b512044a?auto=format&fit=crop&w=800&q=80"
        ),
        HostStadium(
            id = "STAD_KANSAS_CITY",
            name = "Arrowhead Stadium",
            fifaName = "Kansas City Stadium",
            city = "Kansas City",
            country = "United States",
            capacity = "76,416",
            latitude = 39.0489,
            longitude = -94.4839,
            fact = "Holds the Guinness World Record for the loudest stadium crowd.",
            image = "https://images.unsplash.com/photo-1551958219-acbc608c6377?auto=format&fit=crop&w=800&q=80"
        ),
        HostStadium(
            id = "STAD_ATLANTA",
            name = "Mercedes-Benz Stadium",
            fifaName = "Atlanta Stadium",
            city = "Atlanta",
            country = "United States",
            capacity = "71,000",
            latitude = 33.7573,
            longitude = -84.4010,
            fact = "Features a signature pinwheel retractable roof and a 360-degree halo screen.",
            image = "https://images.unsplash.com/photo-1486286701208-1d58e9338013?auto=format&fit=crop&w=800&q=80"
        ),
        HostStadium(
            id = "STAD_MIAMI",
            name = "Hard Rock Stadium",
            fifaName = "Miami Stadium",
            city = "Miami Gardens",
            country = "United States",
            capacity = "64,767",
            latitude = 25.9580,
            longitude = -80.2389,
            fact = "A vibrant multi-purpose venue surrounded by tropical scenery.",
            image = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?auto=format&fit=crop&w=800&q=80"
        ),
        HostStadium(
            id = "STAD_BOSTON",
            name = "Gillette Stadium",
            fifaName = "Boston Stadium",
            city = "Foxborough",
            country = "United States",
            capacity = "65,878",
            latitude = 42.0909,
            longitude = -71.2643,
            fact = "Features a giant lighthouse landmark and a brand-new high-tech video board.",
            image = "https://images.unsplash.com/photo-1606925797300-0b35e9d17d0e?auto=format&fit=crop&w=800&q=80"
        ),
        HostStadium(
            id = "STAD_PHILADELPHIA",
            name = "Lincoln Financial Field",
            fifaName = "Philadelphia Stadium",
            city = "Philadelphia",
            country = "United States",
            capacity = "67,594",
            latitude = 39.9008,
            longitude = -75.1675,
            fact = "Powered by solar panels and wind turbines, a highly eco-friendly stadium.",
            image = "https://images.unsplash.com/photo-1524646889995-3277ab3efecf?auto=format&fit=crop&w=800&q=80"
        ),
        HostStadium(
            id = "STAD_NEW_YORK",
            name = "MetLife Stadium",
            fifaName = "New York New Jersey Stadium",
            city = "East Rutherford",
            country = "United States",
            capacity = "82,500",
            latitude = 40.8128,
            longitude = -74.0742,
            fact = "Chosen as the grand venue for the highly-anticipated FIFA 2026 Final match.",
            image = "https://images.unsplash.com/photo-1518063319789-7217e6706b04?auto=format&fit=crop&w=800&q=80"
        )
    )

    val womensHostStadiums = listOf(
        HostStadium(
            id = "STAD_CORINTHIANS",
            name = "Arena Corinthians",
            fifaName = "São Paulo Stadium",
            city = "São Paulo",
            country = "Brazil",
            capacity = "49,205",
            latitude = -23.5453,
            longitude = -46.4742,
            fact = "Opened in 2014, famous for hosting the opening match of the 2014 FIFA World Cup.",
            image = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?auto=format&fit=crop&w=640&q=80"
        ),
        HostStadium(
            id = "STAD_AMAZONIA",
            name = "Arena da Amazônia",
            fifaName = "Manaus Stadium",
            city = "Manaus",
            country = "Brazil",
            capacity = "40,549",
            latitude = -3.0833,
            longitude = -60.0281,
            fact = "Located in the heart of the Amazon rainforest, designed to resemble a straw basket.",
            image = "https://images.unsplash.com/photo-1522778119026-d647f0596c20?auto=format&fit=crop&w=640&q=80"
        ),
        HostStadium(
            id = "STAD_FONTE_NOVA",
            name = "Arena Fonte Nova",
            fifaName = "Salvador Stadium",
            city = "Salvador",
            country = "Brazil",
            capacity = "50,025",
            latitude = -12.9786,
            longitude = -38.5042,
            fact = "A beautiful stadium built next to the Dique do Tororó lake with historical significance.",
            image = "https://images.unsplash.com/photo-1577223625856-745b14c5520a?auto=format&fit=crop&w=640&q=80"
        ),
        HostStadium(
            id = "STAD_BEIRA_RIO",
            name = "Beira-Rio Stadium",
            fifaName = "Porto Alegre Stadium",
            city = "Porto Alegre",
            country = "Brazil",
            capacity = "50,842",
            latitude = -30.0654,
            longitude = -51.2359,
            fact = "Located beautifully on the banks of the Guaíba River, home to Sport Club Internacional.",
            image = "https://images.unsplash.com/photo-1568194157720-8eae79a3761a?auto=format&fit=crop&w=640&q=80"
        ),
        HostStadium(
            id = "STAD_NACIONAL",
            name = "Estádio Nacional Mané Garrincha",
            fifaName = "Brasília Stadium",
            city = "Brasília",
            country = "Brazil",
            capacity = "72,788",
            latitude = -15.7835,
            longitude = -47.8992,
            fact = "One of the most architecturally striking stadiums in the world with 288 monumental pillars.",
            image = "https://images.unsplash.com/photo-1486286701208-1d58e9338013?auto=format&fit=crop&w=640&q=80"
        ),
        HostStadium(
            id = "STAD_MARACANA",
            name = "Maracanã Stadium",
            fifaName = "Rio de Janeiro Stadium",
            city = "Rio de Janeiro",
            country = "Brazil",
            capacity = "78,838",
            latitude = -22.9122,
            longitude = -43.2302,
            fact = "The iconic spiritual home of football, which hosted two World Cup finals (1950 and 2014).",
            image = "https://images.unsplash.com/photo-1518063319789-7217e6706b04?auto=format&fit=crop&w=640&q=80"
        ),
        HostStadium(
            id = "STAD_MINEIRAO",
            name = "Mineirão Stadium",
            fifaName = "Belo Horizonte Stadium",
            city = "Belo Horizonte",
            country = "Brazil",
            capacity = "61,846",
            latitude = -19.8659,
            longitude = -43.9713,
            fact = "An legendary arena in Minas Gerais with a rich footballing heritage.",
            image = "https://images.unsplash.com/photo-1524646889995-3277ab3efecf?auto=format&fit=crop&w=640&q=80"
        )
    )
}
