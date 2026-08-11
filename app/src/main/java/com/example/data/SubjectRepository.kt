package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SubjectRepository {

    const val OFFICIAL_YOUTUBE_CHANNEL_URL = "https://youtube.com/@skmissionboard?si=wckj0D5alOeUnVW8"
    const val OFFICIAL_CHANNEL_NAME = "SK MISSION BOARD"
    const val ACADEMY_TAGLINE = "by SK ACADEMY"

    private val _subjects = MutableStateFlow<List<Subject>>(emptyList())
    val subjects: StateFlow<List<Subject>> = _subjects.asStateFlow()

    private val _chapters = MutableStateFlow<List<Chapter>>(emptyList())
    val chapters: StateFlow<List<Chapter>> = _chapters.asStateFlow()

    private val _pdfs = MutableStateFlow<List<PdfResource>>(emptyList())
    val pdfs: StateFlow<List<PdfResource>> = _pdfs.asStateFlow()

    private val _videos = MutableStateFlow<List<VideoResource>>(emptyList())
    val videos: StateFlow<List<VideoResource>> = _videos.asStateFlow()

    private val _mcqs = MutableStateFlow<List<McqItem>>(emptyList())
    val mcqs: StateFlow<List<McqItem>> = _mcqs.asStateFlow()

    private val _questions = MutableStateFlow<List<QuestionItem>>(emptyList())
    val questions: StateFlow<List<QuestionItem>> = _questions.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        val subjectList = listOf(
            Subject(
                id = "science",
                name = "Science",
                nameHindi = "विज्ञान",
                code = "SCI-10",
                description = "Physics, Chemistry & Biology complete NCERT/Bihar Board Class 10 Syllabus.",
                colorHex = 0xFF8B5CF6,
                accentHex = 0xFFA78BFA
            ),
            Subject(
                id = "mathematics",
                name = "Mathematics",
                nameHindi = "गणित",
                code = "MATH-10",
                description = "Algebra, Geometry, Trigonometry, Statistics & Probability Class 10.",
                colorHex = 0xFF06B6D4,
                accentHex = 0xFF38BDF8
            ),
            Subject(
                id = "social_science",
                name = "Social Science",
                nameHindi = "सामाजिक विज्ञान",
                code = "SST-10",
                description = "History, Geography, Political Science, Economics & Disaster Management.",
                colorHex = 0xFFF59E0B,
                accentHex = 0xFFFBBF24
            ),
            Subject(
                id = "hindi",
                name = "Hindi",
                nameHindi = "हिंदी",
                code = "HIN-10",
                description = "Godhuli Gadya/Kavya, Varnika and Vyakaran Class 10 complete syllabus.",
                colorHex = 0xFFEC4899,
                accentHex = 0xFFF472B6
            ),
            Subject(
                id = "english",
                name = "English",
                nameHindi = "अंग्रेजी",
                code = "ENG-10",
                description = "First Flight, Footprints Without Feet, Poetry and Grammar Class 10.",
                colorHex = 0xFF10B981,
                accentHex = 0xFF34D399
            ),
            Subject(
                id = "sanskrit",
                name = "Sanskrit",
                nameHindi = "संस्कृत",
                code = "SAN-10",
                description = "Piyusham (पीयूषम्) Part 2 and Vyakaran Class 10 complete NCERT/Bihar Board syllabus.",
                colorHex = 0xFF6366F1,
                accentHex = 0xFF818CF8
            )
        )

        val chapterList = mutableListOf<Chapter>()

        // ==========================================
        // 1. SCIENCE - ALL 16 CHAPTERS (NCERT/Bihar Board)
        // ==========================================
        val scienceChapters = listOf(
            Chapter("sci_ch_1", "science", 1, "Chemical Reactions and Equations", "रासायनिक अभिक्रियाएं एवं समीकरण", "Types of chemical reactions, balancing chemical equations, oxidation and reduction."),
            Chapter("sci_ch_2", "science", 2, "Acids, Bases and Salts", "अम्ल, क्षारक एवं लवण", "Properties of acids and bases, pH scale, preparation and uses of important salts."),
            Chapter("sci_ch_3", "science", 3, "Metals and Non-metals", "धातु एवं अधातु", "Physical and chemical properties, reactivity series, metallurgy, corrosion and prevention."),
            Chapter("sci_ch_4", "science", 4, "Carbon and its Compounds", "कार्बन एवं उसके यौगिक", "Covalent bonding in carbon, homologous series, functional groups, soaps and detergents."),
            Chapter("sci_ch_5", "science", 5, "Periodic Classification of Elements", "तत्वों का आवर्त वर्गीकरण", "Dobereiner's Triads, Newlands' Law of Octaves, Mendeleev and Modern Periodic Table."),
            Chapter("sci_ch_6", "science", 6, "Life Processes", "जैव प्रक्रम", "Nutrition, respiration, transportation, and excretion in plants and human beings."),
            Chapter("sci_ch_7", "science", 7, "Control and Coordination", "नियंत्रण एवं समन्वय", "Nervous system, reflex arc, human brain, plant hormones and endocrine system."),
            Chapter("sci_ch_8", "science", 8, "How do Organisms Reproduce?", "जीव जनन कैसे करते हैं?", "Asexual and sexual reproduction, reproductive health, plant and human reproduction."),
            Chapter("sci_ch_9", "science", 9, "Heredity and Evolution", "आनुवंशिकता एवं जैव विकास", "Mendel's experiments, sex determination, inheritance and evolution theories."),
            Chapter("sci_ch_10", "science", 10, "Light – Reflection and Refraction", "प्रकाश – परावर्तन तथा अपवर्तन", "Laws of reflection, spherical mirrors, mirror formula, refraction, lenses and power."),
            Chapter("sci_ch_11", "science", 11, "Human Eye and Colourful World", "मानव नेत्र तथा रंगबिरंगा संसार", "Structure of human eye, defects of vision, atmospheric refraction, dispersion of light."),
            Chapter("sci_ch_12", "science", 12, "Electricity", "विद्युत", "Electric current, Ohm's law, resistance, series & parallel combinations, heating effect."),
            Chapter("sci_ch_13", "science", 13, "Magnetic Effects of Electric Current", "विद्युत धारा के चुंबकीय प्रभाव", "Magnetic field lines, Fleming's rules, electromagnetic induction, electric motor & generator."),
            Chapter("sci_ch_14", "science", 14, "Sources of Energy", "ऊर्जा के स्रोत", "Conventional and non-conventional sources of energy, solar energy, nuclear energy."),
            Chapter("sci_ch_15", "science", 15, "Our Environment", "हमारा पर्यावरण", "Ecosystem, food chains, food webs, ozone layer depletion and waste management."),
            Chapter("sci_ch_16", "science", 16, "Management of Natural Resources", "प्राकृतिक संसाधनों का प्रबंधन", "Sustainable management of forests, wildlife, water harvesting, coal and petroleum.")
        )
        chapterList.addAll(scienceChapters)

        // ==========================================
        // 2. MATHEMATICS - ALL 15 CHAPTERS
        // ==========================================
        val mathChapters = listOf(
            Chapter("math_ch_1", "mathematics", 1, "Real Numbers", "वास्तविक संख्याएं", "Euclid's division lemma, Fundamental Theorem of Arithmetic, irrationality proof."),
            Chapter("math_ch_2", "mathematics", 2, "Polynomials", "बहुपद", "Zeros of a polynomial, relationship between zeros and coefficients, division algorithm."),
            Chapter("math_ch_3", "mathematics", 3, "Pair of Linear Equations in Two Variables", "दो चरों वाले रैखिक समीकरण युग्म", "Graphical method, substitution, elimination, cross-multiplication method."),
            Chapter("math_ch_4", "mathematics", 4, "Quadratic Equations", "द्विघात समीकरण", "Standard form, factorization method, quadratic formula, nature of roots."),
            Chapter("math_ch_5", "mathematics", 5, "Arithmetic Progressions", "समांतर श्रेढ़ी", "n-th term of an AP, sum of first n terms of an AP, practical word problems."),
            Chapter("math_ch_6", "mathematics", 6, "Triangles", "त्रिभुज", "Similar triangles, Thales theorem (BPT), Pythagoras theorem and proofs."),
            Chapter("math_ch_7", "mathematics", 7, "Coordinate Geometry", "निर्देशांक ज्यामिति", "Distance formula, section formula, area of a triangle formula."),
            Chapter("math_ch_8", "mathematics", 8, "Introduction to Trigonometry", "त्रिकोणमिति का परिचय", "Trigonometric ratios, values at specific angles, trigonometric identities."),
            Chapter("math_ch_9", "mathematics", 9, "Some Applications of Trigonometry", "त्रिकोणमिति के कुछ अनुप्रयोग", "Heights and distances, angle of elevation, angle of depression."),
            Chapter("math_ch_10", "mathematics", 10, "Circles", "वृत्त", "Tangent to a circle, length of tangents from an external point."),
            Chapter("math_ch_11", "mathematics", 11, "Constructions", "रचनाएँ", "Division of a line segment, construction of tangents to a circle."),
            Chapter("math_ch_12", "mathematics", 12, "Areas Related to Circles", "वृत्तों से संबंधित क्षेत्रफल", "Perimeter and area of circle, area of sector and segment of a circle."),
            Chapter("math_ch_13", "mathematics", 13, "Surface Areas and Volumes", "पृष्ठीy क्षेत्रफल और आयतन", "Surface areas and volumes of combinations of solids, frustum of a cone."),
            Chapter("math_ch_14", "mathematics", 14, "Statistics", "सांख्यिकी", "Mean, median, and mode of grouped data, cumulative frequency graph (ogive)."),
            Chapter("math_ch_15", "mathematics", 15, "Probability", "प्रायिकता", "Classical approach to probability, elementary events, complementary events.")
        )
        chapterList.addAll(mathChapters)

        // ==========================================
        // 3. SOCIAL SCIENCE - ALL 27 CHAPTERS
        // ==========================================
        val sstChapters = listOf(
            // History
            Chapter("sst_ch_1", "social_science", 1, "Nationalism in Europe", "यूरोप में राष्ट्रवाद", "History: Rise of nationalism, French revolution, Unification of Italy and Germany."),
            Chapter("sst_ch_2", "social_science", 2, "Socialism and Communism", "समाजवाद एवं साम्यवाद", "History: Russian revolution, Karl Marx, Bolshevik revolution."),
            Chapter("sst_ch_3", "social_science", 3, "Nationalist Movement in Indo-China", "हिंद-चीन में राष्ट्रवादी आंदोलन", "History: French colonization, anti-colonial struggle, Vietnam war."),
            Chapter("sst_ch_4", "social_science", 4, "Nationalism in India", "भारत में राष्ट्रवाद", "History: Non-cooperation movement, Civil disobedience, Quit India, Rowlatt Act."),
            Chapter("sst_ch_5", "social_science", 5, "Economy and Livelihood", "अर्थव्यवस्था और आजीविका", "History: Industrial revolution, factory system, labor movements."),
            Chapter("sst_ch_6", "social_science", 6, "Urbanization and Urban Life", "शहरीकरण एवं शहरी जीवन", "History: Development of modern cities, London and Bombay growth."),
            Chapter("sst_ch_7", "social_science", 7, "Trade and Globalization", "व्यापार और भूमंडलीकरण", "History: Silk route, pre-modern world, Great Depression of 1929."),
            Chapter("sst_ch_8", "social_science", 8, "Press Culture and Nationalism", "प्रेस संस्कृति एवं राष्ट्रवाद", "History: Print revolution, vernacular press act, freedom struggle."),
            // Geography
            Chapter("sst_ch_9", "social_science", 9, "India: Resources and Utilization", "भारत: संसाधन एवं उपयोग", "Geography: Classification of resources, land & soil degradation, conservation."),
            Chapter("sst_ch_10", "social_science", 10, "Agriculture in India", "कृषि", "Geography: Types of farming, cropping patterns, major food crops."),
            Chapter("sst_ch_11", "social_science", 11, "Manufacturing Industries", "निर्माण उद्योग", "Geography: Agro-based and mineral-based industries, industrial pollution."),
            Chapter("sst_ch_12", "social_science", 12, "Transport, Communication & Trade", "परिवहन, संचार एवं व्यापार", "Geography: Railways, roadways, waterways, international trade."),
            Chapter("sst_ch_13", "social_science", 13, "Bihar: Agriculture & Forest Resources", "बिहार: कृषि एवं वन संसाधन", "Geography: Bihar state resource mapping, forest cover, minerals."),
            Chapter("sst_ch_14", "social_science", 14, "Map Reading & Analysis", "मानचित्र अध्ययन", "Geography: Topographical maps, contour lines, symbols."),
            // Political Science
            Chapter("sst_ch_15", "social_science", 15, "Power Sharing in Democracy", "लोकतंत्र में सत्ता की साझेदारी", "Civics: Ethnic composition, Belgium and Sri Lanka power sharing models."),
            Chapter("sst_ch_16", "social_science", 16, "Working of Power Sharing", "सत्ता में साझेदारी की कार्यप्रणाली", "Civics: Federalism, Union list, State list, Panchayati Raj system."),
            Chapter("sst_ch_17", "social_science", 17, "Competition and Struggle in Democracy", "लोकतंत्र में प्रतिस्पर्धा एवं संघर्ष", "Civics: Political parties, popular struggles, pressure groups."),
            Chapter("sst_ch_18", "social_science", 18, "Outcomes of Democracy", "लोकतंत्र के परिणाम", "Civics: Accountable, responsive and legitimate government."),
            Chapter("sst_ch_19", "social_science", 19, "Challenges to Democracy", "लोकतंत्र की चुनौतियाँ", "Civics: Deepening democracy, political reforms, casteism."),
            // Economics
            Chapter("sst_ch_20", "social_science", 20, "History of Development", "विकास का इतिहास एवं उसकी स्थिति", "Economics: Per capita income, National income, Human Development Index (HDI)."),
            Chapter("sst_ch_21", "social_science", 21, "State and National Income", "राज्य एवं राष्ट्र की आय", "Economics: Gross Domestic Product (GDP), Net National Product."),
            Chapter("sst_ch_22", "social_science", 22, "Money, Savings and Credit", "मुद्रा, बचत एवं साख", "Economics: Evolution of money, commercial banks, self-help groups."),
            Chapter("sst_ch_23", "social_science", 23, "Our Financial Institutions", "हमारी वित्तीय संस्थाएं", "Economics: RBI, NABARD, microfinance institutions in Bihar."),
            Chapter("sst_ch_24", "social_science", 24, "Employment and Services", "रोजगार एवं सेवाएं", "Economics: Primary, secondary, tertiary sectors, outsourcing."),
            Chapter("sst_ch_25", "social_science", 25, "Globalization", "वैश्वीकरण", "Economics: Multinational corporations (MNCs), WTO, foreign trade."),
            Chapter("sst_ch_26", "social_science", 26, "Consumer Rights", "उपभोक्ता अधिकार", "Economics: COPRA 1986, Consumer courts, ISI and Agmark certification."),
            // Disaster Management
            Chapter("sst_ch_27", "social_science", 27, "Disaster Management Intro", "आपदा प्रबंधन परिचय", "Disaster Management: Floods in Bihar, earthquakes, droughts, safety measures.")
        )
        chapterList.addAll(sstChapters)

        // ==========================================
        // 4. HINDI - ALL 29 CHAPTERS
        // ==========================================
        val hindiChapters = listOf(
            Chapter("hin_ch_1", "hindi", 1, "श्रम विभाजन और जाति प्रथा", "Shram Vibhajan Aur Jati Pratha", "लेखक: डॉ. भीमराव अंबेडकर - निबंध।"),
            Chapter("hin_ch_2", "hindi", 2, "विष के दांत", "Vish Ke Dant", "लेखक: नलिन विलोचन शर्मा - कहानी।"),
            Chapter("hin_ch_3", "hindi", 3, "भारत से हम क्या सीखें", "Bharat Se Ham Kya Sikhen", "लेखक: मैक्समूलर - भाषण।"),
            Chapter("hin_ch_4", "hindi", 4, "नाखून क्यों बढ़ते हैं", "Nakhun Kyon Barhte Hain", "लेखक: हजारी प्रसाद द्विवेदी - ललित निबंध।"),
            Chapter("hin_ch_5", "hindi", 5, "नागरी लिपि", "Nagari Lipi", "लेखक: गुणाकर मुले - निबंध।"),
            Chapter("hin_ch_6", "hindi", 6, "बहादुर", "Bahadur", "लेखक: अमरकांत - कहानी।"),
            Chapter("hin_ch_7", "hindi", 7, "परंपरा का मूल्यांकन", "Parampara Ka Mulyankan", "लेखक: रामविलास शर्मा - निबंध।"),
            Chapter("hin_ch_8", "hindi", 8, "जीत जीत मैं निरखत हूँ", "Jit Jit Main Nirakhat Hun", "लेखक: पंडित बिरजू महाराज - साक्षात्कार।"),
            Chapter("hin_ch_9", "hindi", 9, "आविन्यों", "Avinyon", "लेखक: अशोक वाजपेयी - यात्रा वृत्तांत।"),
            Chapter("hin_ch_10", "hindi", 10, "मछली", "Machhli", "लेखक: विनोद कुमार शुक्ल - कहानी।"),
            Chapter("hin_ch_11", "hindi", 11, "नौबतखाने में इबादत", "Naubat Khane Me Ibadat", "लेखक: यतीन्द्र मिश्र - व्यक्तिचित्र (उस्ताद बिस्मिल्ला खाँ)।"),
            Chapter("hin_ch_12", "hindi", 12, "शिक्षा और संस्कृति", "Shiksha Aur Sanskriti", "लेखक: महात्मा गांधी - शिक्षाशास्त्र।"),
            // Kavya Khand
            Chapter("hin_ch_13", "hindi", 13, "राम नाम बिनु बिरथे जगि जन्मा", "Ram Naam Binu Birthe", "कवि: गुरु नानक - पद।"),
            Chapter("hin_ch_14", "hindi", 14, "प्रेम अयनि श्री राधिका", "Prem Ayni Shri Radhika", "कवि: रसखान - सवैया।"),
            Chapter("hin_ch_15", "hindi", 15, "अति सूधो सनेह को मारग है", "Ati Sudho Saneh Ko Marg", "कवि: घनानंद - कवित्त।"),
            Chapter("hin_ch_16", "hindi", 16, "स्वदेशी", "Swadeshi", "कवि: बद्रीनारायण चौधरी 'प्रेमघन'।"),
            Chapter("hin_ch_17", "hindi", 17, "भारतमाता", "Bharatmata", "कवि: सुमित्रानंदन पंत - कविता।"),
            Chapter("hin_ch_18", "hindi", 18, "जनतंत्र का जन्म", "Janatantra Ka Janma", "कवि: रामधारी सिंह 'दिनकर' - राष्ट्रकवि।"),
            Chapter("hin_ch_19", "hindi", 19, "हिरोशिमा", "Hiroshima", "कवि: सचिदानंद हीरानंद वात्स्यायन 'अज्ञेय'।"),
            Chapter("hin_ch_20", "hindi", 20, "एक वृक्ष की हत्या", "Ek Vriksh Ki Hatya", "कवि: कुँवर नारायण - कविता।"),
            Chapter("hin_ch_21", "hindi", 21, "हमारी नींद", "Hamari Neend", "कवि: वीरेन डंगवाल - समकालीन कविता।"),
            Chapter("hin_ch_22", "hindi", 22, "अक्षर ज्ञान", "Akshar Gyan", "कवि: अनामिका - बाल मनोविज्ञान कविता।"),
            Chapter("hin_ch_23", "hindi", 23, "लौटकर आऊँगा फिर", "Lautkar Aaunga Phir", "कवि: जीवनानंद दास - प्रकृति प्रेम।"),
            Chapter("hin_ch_24", "hindi", 24, "मेरे बिना तुम प्रभु", "Mere Bina Tum Prabhu", "कवि: रेनर मारिया रिल्के - भक्ति कविता।"),
            // Varnika
            Chapter("hin_ch_25", "hindi", 25, "दही वाली मगम्मा", "Dahi Wali Magamma", "लेखक: श्रीनिवास - कन्नड़ कहानी (वर्णिका)।"),
            Chapter("hin_ch_26", "hindi", 26, "ढहते विश्वास", "Dhahate Vishwas", "लेखक: सातकौड़ी होता - उड़िया कहानी।"),
            Chapter("hin_ch_27", "hindi", 27, "माँ", "Maa", "लेखक: ईश्वर पेटलीकर - गुजराती कहानी।"),
            Chapter("hin_ch_28", "hindi", 28, "नगर", "Nagar", "लेखक: सुजाता - तमिल कहानी।"),
            Chapter("hin_ch_29", "hindi", 29, "धरती कब तक घूमेगी", "Dharti Kab Tak Ghumegi", "लेखक: सांवर दइया - राजस्थानी कहानी।")
        )
        chapterList.addAll(hindiChapters)

        // ==========================================
        // 5. ENGLISH - ALL 28 CHAPTERS
        // ==========================================
        val englishChapters = listOf(
            Chapter("eng_ch_1", "english", 1, "A Letter to God", "Prose", "Author: G.L. Fuentes - Story of Lencho's unwavering faith in God."),
            Chapter("eng_ch_2", "english", 2, "Nelson Mandela: Long Walk to Freedom", "Prose", "Author: Nelson Rolihlahla Mandela - Autobiography excerpt."),
            Chapter("eng_ch_3", "english", 3, "Two Stories about Flying", "Prose", "His First Flight & Black Aeroplane - Overcoming fear."),
            Chapter("eng_ch_4", "english", 4, "From the Diary of Anne Frank", "Prose", "Author: Anne Frank - Diary entries during WWII."),
            Chapter("eng_ch_5", "english", 5, "Glimpses of India", "Prose", "A Baker from Goa, Coorg, Tea from Assam."),
            Chapter("eng_ch_6", "english", 6, "Mijbil the Otter", "Prose", "Author: Gavin Maxwell - Story of an otter pet."),
            Chapter("eng_ch_7", "english", 7, "Madam Rides the Bus", "Prose", "Author: Vallikkannan - Valli's first bus journey."),
            Chapter("eng_ch_8", "english", 8, "The Sermon at Benares", "Prose", "Gautama Buddha's sermon to Kisa Gotami."),
            Chapter("eng_ch_9", "english", 9, "The Proposal", "Prose", "Author: Anton Chekhov - One act play."),
            // Poetry
            Chapter("eng_ch_10", "english", 10, "Dust of Snow", "Poetry", "Poet: Robert Frost - Short uplifting poem."),
            Chapter("eng_ch_11", "english", 11, "Fire and Ice", "Poetry", "Poet: Robert Frost - Destruction of the world."),
            Chapter("eng_ch_12", "english", 12, "A Tiger in the Zoo", "Poetry", "Poet: Leslie Norris - Contrast between cage & wild."),
            Chapter("eng_ch_13", "english", 13, "How to Tell Wild Animals", "Poetry", "Poet: Carolyn Wells - Humorous poem on wild beasts."),
            Chapter("eng_ch_14", "english", 14, "The Ball Poem", "Poetry", "Poet: John Berryman - Loss and responsibility."),
            Chapter("eng_ch_15", "english", 15, "Amanda!", "Poetry", "Poet: Robin Klein - Teenager seeking freedom."),
            Chapter("eng_ch_16", "english", 16, "The Trees", "Poetry", "Poet: Adrienne Rich - Trees moving into forest."),
            Chapter("eng_ch_17", "english", 17, "Fog", "Poetry", "Poet: Carl Sandburg - Metaphor of cat."),
            Chapter("eng_ch_18", "english", 18, "The Tale of Custard the Dragon", "Poetry", "Poet: Ogden Nash - Ballad of a cowardly dragon."),
            Chapter("eng_ch_19", "english", 19, "For Anne Gregory", "Poetry", "Poet: W.B. Yeats - External vs internal beauty."),
            // Footprints Without Feet
            Chapter("eng_ch_20", "english", 20, "A Triumph of Surgery", "Supplementary", "Author: James Herriot - Tricki the dog."),
            Chapter("eng_ch_21", "english", 21, "The Thief's Story", "Supplementary", "Author: Ruskin Bond - Hari Singh & Anil."),
            Chapter("eng_ch_22", "english", 22, "The Midnight Visitor", "Supplementary", "Author: Robert Arthur - Secret agent Ausable."),
            Chapter("eng_ch_23", "english", 23, "A Question of Trust", "Supplementary", "Author: Victor Canning - Horace Danby."),
            Chapter("eng_ch_24", "english", 24, "Footprints Without Feet", "Supplementary", "Author: H.G. Wells - Scientist Griffin."),
            Chapter("eng_ch_25", "english", 25, "The Making of a Scientist", "Supplementary", "Author: Robert W. Peterson - Richard Ebright."),
            Chapter("eng_ch_26", "english", 26, "The Necklace", "Supplementary", "Author: Guy de Maupassant - Matilda's story."),
            Chapter("eng_ch_27", "english", 27, "Bholi", "Supplementary", "Author: K.A. Abbas - Education transforming Bholi."),
            Chapter("eng_ch_28", "english", 28, "The Book That Saved the Earth", "Supplementary", "Author: Claire Boiko - Martian invasion comedy.")
        )
        chapterList.addAll(englishChapters)

        // ==========================================
        // 6. SANSKRIT - ALL 14 CHAPTERS (Piyusham Part 2)
        // ==========================================
        val sanskritChapters = listOf(
            Chapter("san_ch_1", "sanskrit", 1, "मंगलम्", "Mangalam", "उपनिषद् के पद्यत्मक मंत्र और ईश्वर भक्ति।"),
            Chapter("san_ch_2", "sanskrit", 2, "पाटलिपुत्रवैभवम्", "Pataliputra Vaibhavam", "पटना (पाटलिपुत्र) का ऐतिहासिक एवं सांस्कृतिक वैभव।"),
            Chapter("san_ch_3", "sanskrit", 3, "अलसकथा", "Alas Katha", "विद्यापति द्वारा रचित पुरुषपरीक्षा से संकलित आलसियों की कथा।"),
            Chapter("san_ch_4", "sanskrit", 4, "संस्कृतसाहित्ये लेखिकाः", "Sanskrit Sahitye Lekhikah", "संस्कृत साहित्य में विदुषी महिलाओं का योगदान।"),
            Chapter("san_ch_5", "sanskrit", 5, "भारतमहिमा", "Bharat Mahima", "पौराणिक तथा आधुनिक पद्य में भारत देश की महिमा।"),
            Chapter("san_ch_6", "sanskrit", 6, "भारतीयसंस्काराः", "Bharatiya Samskarah", "जीवन के 16 मुख्य भारतीय संस्कारों का विस्तृत वर्णन।"),
            Chapter("san_ch_7", "sanskrit", 7, "नीतिश्लोकाः", "Niti Shlokah", "महर्षि वेदव्यास रचित विदुर नीति के सदाचार श्लोक।"),
            Chapter("san_ch_8", "sanskrit", 8, "कर्मवीर कथा", "Karmaveer Katha", "भीखनटोला के दलित बालक रामप्रवेश राम की सफलता की कहानी।"),
            Chapter("san_ch_9", "sanskrit", 9, "स्वामी दयानन्दः", "Swami Dayanand", "आर्य समाज के संस्थापक एवं समाज सुधारक स्वामी दयानन्द।"),
            Chapter("san_ch_10", "sanskrit", 10, "मन्दाकिनीवर्णनम्", "Mandakini Varnanam", "वाल्मीकि रामायण के अयोध्या काण्ड से मन्दाकिनी नदी वर्णन।"),
            Chapter("san_ch_11", "sanskrit", 11, "व्याघ्रपथिककथा", "Vyaghra Pathika Katha", "हितोपदेश के मित्रलाभ भाग से संकलित लोभ की कथा।"),
            Chapter("san_ch_12", "sanskrit", 12, "कर्णस्य दानवीरता", "Karnasya Danaveerata", "महाकवि भास रचित रूपक कर्णभारम् से दानवीर कर्ण।"),
            Chapter("san_ch_13", "sanskrit", 13, "विश्वशांतिः", "Vishwa Shantih", "संसार में असहिष्णुता निवारण एवं विश्वशांति का सन्देश।"),
            Chapter("san_ch_14", "sanskrit", 14, "शास्त्रकाराः", "Shastrakarah", "भारतीय शास्त्रों, वेदांगों एवं ऋषियों-महर्षियों का परिचय।")
        )
        chapterList.addAll(sanskritChapters)

        // Count chapters per subject and update subjectList
        val updatedSubjects = subjectList.map { subj ->
            val count = chapterList.count { it.subjectId == subj.id }
            subj.copy(totalChaptersCount = count)
        }

        _subjects.value = updatedSubjects
        _chapters.value = chapterList

        // Populate sample PDFs & Videos to verify functional features
        populateInitialResources()
    }

    private fun populateInitialResources() {
        val samplePdfs = listOf(
            PdfResource(
                id = "pdf_sci_1_notes",
                chapterId = "sci_ch_1",
                subjectId = "science",
                title = "Chemical Reactions - Complete Chapter Hand Written Notes",
                description = "Class 10 Science Chapter 1 detailed notes by SK ACADEMY faculty with reaction formulas.",
                pdfUrl = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf",
                fileSize = "2.4 MB",
                pageCount = 12,
                category = ResourceType.NOTES
            ),
            PdfResource(
                id = "pdf_sci_1_imp_q",
                chapterId = "sci_ch_1",
                subjectId = "science",
                title = "Top 25 Most Important Board Exam Questions 2026",
                description = "Guaranteed 2-mark, 3-mark & 5-mark short and long answer questions with answers.",
                pdfUrl = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf",
                fileSize = "1.8 MB",
                pageCount = 8,
                category = ResourceType.IMPORTANT_QUESTIONS
            ),
            PdfResource(
                id = "pdf_math_1_formula",
                chapterId = "math_ch_1",
                subjectId = "mathematics",
                title = "Real Numbers - Complete Formula Sheet & Quick Mind Map",
                description = "Euclid's division lemma, HCF & LCM formulas, irrationality theorems summary.",
                pdfUrl = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf",
                fileSize = "1.1 MB",
                pageCount = 4,
                category = ResourceType.QUICK_REVISION
            ),
            PdfResource(
                id = "pdf_sst_4_pyq",
                chapterId = "sst_ch_4",
                subjectId = "social_science",
                title = "Nationalism in India - Last 10 Years Bihar Board PYQs",
                description = "Solved Previous Year Questions (2015-2025) with marking scheme.",
                pdfUrl = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf",
                fileSize = "3.2 MB",
                pageCount = 15,
                category = ResourceType.PREVIOUS_YEAR_QUESTIONS
            )
        )

        // Sample Provided Videos (Category A)
        val sampleVideos = listOf(
            VideoResource(
                id = "vid_sci_1_l1",
                chapterId = "sci_ch_1",
                subjectId = "science",
                title = "Chemical Reactions and Equations | Lecture 1 | Full Chapter Revision in One Shot",
                youtubeUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                youtubeVideoId = "dQw4w9WgXcQ",
                duration = "45:12"
            ),
            VideoResource(
                id = "vid_sci_1_l2",
                chapterId = "sci_ch_1",
                subjectId = "science",
                title = "Chemical Reactions & Balancing Equations | Top 15 VVI Questions for Board Exam",
                youtubeUrl = "https://www.youtube.com/watch?v=3JZ_D3ELwOQ",
                youtubeVideoId = "3JZ_D3ELwOQ",
                duration = "32:40"
            ),
            VideoResource(
                id = "vid_math_1_l1",
                chapterId = "math_ch_1",
                subjectId = "mathematics",
                title = "Real Numbers (वास्तविक संख्याएं) Complete Chapter Zero to Hero | SK MISSION BOARD",
                youtubeUrl = "https://www.youtube.com/watch?v=L_LUpnjgPso",
                youtubeVideoId = "L_LUpnjgPso",
                duration = "58:20"
            )
        )

        val sampleMcqs = listOf(
            McqItem(
                id = "mcq_sci_1_1",
                chapterId = "sci_ch_1",
                question = "किस प्रकार की अभिक्रिया में दो या दो से अधिक पदार्थ मिलकर एक नया पदार्थ बनाते हैं?",
                options = listOf("संयोजन अभिक्रिया (Combination)", "वियोजन अभिक्रिया (Decomposition)", "विस्थापन अभिक्रिया (Displacement)", "द्विविस्थापन अभिक्रिया (Double Displacement)"),
                correctOptionIndex = 0,
                explanation = "संयोजन अभिक्रिया में दो या अधिक अभिकारक मिलकर केवल एक उत्पाद का निर्माण करते हैं।"
            ),
            McqItem(
                id = "mcq_sci_1_2",
                chapterId = "sci_ch_1",
                question = "लोहे पर जंग लगना किस प्रकार की प्रक्रिया है?",
                options = listOf("अपचयन", "ऑक्सीकरण एवं संक्षारण", "अपघटन", "उदासीनीकरण"),
                correctOptionIndex = 1,
                explanation = "लोहे का नमी एवं ऑक्सीजन की उपस्थिति में संक्षारित होना ऑक्सीकरण (Corrosion) प्रक्रिया है।"
            )
        )

        _pdfs.value = samplePdfs
        _videos.value = sampleVideos
        _mcqs.value = sampleMcqs
    }

    // Query Methods
    fun getSubjectById(subjectId: String): Subject? {
        return _subjects.value.find { it.id == subjectId }
    }

    fun getChaptersForSubject(subjectId: String): List<Chapter> {
        return _chapters.value.filter { it.subjectId == subjectId }.sortedBy { it.number }
    }

    fun getChapterById(chapterId: String): Chapter? {
        return _chapters.value.find { it.id == chapterId }
    }

    fun getPdfsForChapter(chapterId: String): List<PdfResource> {
        return _pdfs.value.filter { it.chapterId == chapterId }
    }

    fun getVideosForChapter(chapterId: String): List<VideoResource> {
        return _videos.value.filter { it.chapterId == chapterId }
    }

    fun getMcqsForChapter(chapterId: String): List<McqItem> {
        return _mcqs.value.filter { it.chapterId == chapterId }
    }

    fun searchAll(query: String): List<SearchResult> {
        if (query.trim().isEmpty()) return emptyList()
        val q = query.lowercase().trim()
        val results = mutableListOf<SearchResult>()

        // Match subjects
        _subjects.value.filter {
            it.name.lowercase().contains(q) || it.nameHindi.contains(q) || it.description.lowercase().contains(q)
        }.forEach {
            results.add(
                SearchResult(
                    id = "subj_${it.id}",
                    title = "${it.name} (${it.nameHindi})",
                    subtitle = "Subject • ${it.totalChaptersCount} Chapters",
                    typeName = "Subject",
                    subjectId = it.id
                )
            )
        }

        // Match chapters
        _chapters.value.filter {
            it.title.lowercase().contains(q) || (it.titleHindi?.contains(q) == true) || "chapter ${it.number}".contains(q)
        }.forEach { ch ->
            val subjName = getSubjectById(ch.subjectId)?.name ?: ""
            results.add(
                SearchResult(
                    id = "ch_${ch.id}",
                    title = "Ch ${ch.number}: ${ch.title}",
                    subtitle = "$subjName • ${ch.titleHindi ?: ""}",
                    typeName = "Chapter",
                    subjectId = ch.subjectId,
                    chapterId = ch.id
                )
            )
        }

        // Match PDFs
        _pdfs.value.filter {
            it.title.lowercase().contains(q) || (it.description?.lowercase()?.contains(q) == true)
        }.forEach { pdf ->
            val ch = getChapterById(pdf.chapterId)
            results.add(
                SearchResult(
                    id = "pdf_${pdf.id}",
                    title = pdf.title,
                    subtitle = "PDF • ${ch?.title ?: "Class 10 Note"}",
                    typeName = "PDF Note",
                    subjectId = pdf.subjectId,
                    chapterId = pdf.chapterId,
                    pdfUrl = pdf.pdfUrl
                )
            )
        }

        // Match Videos
        _videos.value.filter {
            it.title.lowercase().contains(q)
        }.forEach { vid ->
            val ch = getChapterById(vid.chapterId)
            results.add(
                SearchResult(
                    id = "vid_${vid.id}",
                    title = vid.title,
                    subtitle = "Video Lecture • ${ch?.title ?: "Class 10 Video"}",
                    typeName = "Video Lecture",
                    subjectId = vid.subjectId,
                    chapterId = vid.chapterId,
                    youtubeUrl = vid.youtubeUrl
                )
            )
        }

        return results
    }
}
