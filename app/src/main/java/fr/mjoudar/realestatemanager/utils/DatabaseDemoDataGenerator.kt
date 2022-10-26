package fr.mjoudar.realestatemanager.utils

import fr.mjoudar.realestatemanager.domain.models.*
import java.util.*

class DatabaseDemoDataGenerator {

    /**********************************************************************************************
     ** Agents
     **********************************************************************************************/

    val agent1 = Agent(
        UUID.randomUUID().toString(),
        "Richard Branson",
        "file:///android_asset/Agents/agent_avatar_1.png",
        "richard.branson@rem.com",
        "+1 354-607-2343"
    )
    val agent2 = Agent(
        UUID.randomUUID().toString(),
        "Sara Blakely",
        "file:///android_asset/Agents/agent_avatar_2.png",
        "sara.blakely@rem.com",
        "+1 354-607-3245"
    )
    val agent3 = Agent(
        UUID.randomUUID().toString(),
        "Robert Kiyosaki",
        "file:///android_asset/Agents/agent_avatar_3.png",
        "robert.kiyosaki@rem.com",
        "+1 354-607-2343"
    )

    /**********************************************************************************************
     ** Strings
     **********************************************************************************************/

    val description1 = "This magnificent 7 bedroom / 7 bathroom gorgeous family home with over 7500 ft.² of interior living space is located in West Vancouver‘s lower British Properties with easy & convenient access to excellent schools, Park Royal shopping centre as well as downtown Vancouver. Situated on a very quiet cul-de-sac, one enters this sunny south facing estate sized property via a private gated driveway which takes you up to a courtyard area with ample parking for 6 cars and nearby 3 car garage. This elegant residence with its grand high entry foyer leads to all the principal main floor spacious rooms and onto the expansive sunlit patios overlooking the lovely pool, hot tub and manicured gardens. Features of the home include: quality wood windows, extensive millwork throughout, radiant in floor heat, air conditioning, 5 fireplaces, state of the art kitchen plus butler pantry and bar, as well as the stunning outdoor pool and hot tub adjacent to the main floor kitchen and family room. The upper floor with access from two staircases has 5 exquisite bedrooms with vaulted ceilings and ensuite bathrooms. The lower level entertaining area contains a media room, games room, rec room, wine room plus bar and gym/storage plus 2 extra bedrooms."
    val description2 = "Gorgeous 2 bedroom 2 bathroom in the Westroyal! Recently upgraded in 2022 with all new premium Bosch stainless steel appliances in the kitchen including: fridge, wall oven, microwave, hood fan, cooktop plus Miele dishwasher. Cabinova Kitchen has renovated & resurfaced all kitchen cabinets and added new under cabinet lighting. For those who work at home or need extra living space, the 2nd bedroom has been redesigned to function as a den or an office. This spacious suite on the quiet cool side of the building overlooks the Capilano river and gardens below. One feels they are in the country as all you see is the lovely greenery of the trees, sprawling lawns and beautiful grounds. The suite offers lots of space at 1315 sq ft with a magnificent oversized covered balcony to enjoy. Comes with 2 of the best easy access side by side parking stall's adjacent to the elevators. A storage locker is conveniently located nearby. Small dog or cat welcome."
    val description3 = "LOCATION! LOCATION! LOCATION! Excellent opportunity to own this gorgeous semi-waterfront home with dramatic close in ocean views looking out to Point Grey, UBC and beyond to Vancouver Island. This lovely custom built 4 bedroom/6 bathroom (2 level) Altamont home was completed in 2007 and features large principal rooms with soaring ceilings on the main floor. Expansive glass doors lead out to covered patio terraces overlooking the sparkling outdoor pool and beautiful gardens. Upstairs, all 4 bedrooms have their own ensuite bathrooms. For those who need storage, the lower level massive crawl space offers plenty of space. This amazing semi-waterfront property is conveniently located just steps away to the waterfront, transit, parks, beaches, great schools (West Bay IB elementary school) as well as wonderful shopping and restaurants in nearby Dundarave Village only a short, easy 5 block stroll away."
    val description4 = "Beautifully appointed 3 bedroom/3 bath end unit townhome, completely renovated by one of West Van's luxury home builders in the rarely available, quiet, gated and well maintained Winchester. Pride of ownership is evident from the moment you enter. The main floor features engineered hardwood floors with radiant floor heat, light filled rooms with vaulted ceilings, 2 gas fireplaces, a custom designed chef's kitchen with large stone island & a spacious outdoor sundeck. The large master bedroom has a lovely ensuite bathroom with Nuheat in the floors. The double garage with level access into the home is an added bonus. The Lower floor with radiant floor heat features oversized bedrooms, full bath, laundry and 2 extra large storage rooms. Enjoy 1 or 2 level living in spacious comfort! The Winchester is a 19 yr +, friendly 47 unit community close to walking/biking trails within walking distance of the Parkgate shopping centre, library, rec centre & Deep Cove Village."
    val description5 = "Situated on a very large and very quiet 24,393 sq ft private lot boasting stunning ocean and marina views, sits this 6 bedroom, 6 bathroom family home.This residence is part of Sea Breeze Estates, a group of 6 homes within walking distance to Thunderbird Marina and West Vancouver Yacht Club below. Easy access to Sea View Walk, Trans Canada and Baden Powell Trails, Gleneagles Golf Course, Community Centre and Gleneagles elementary school nearby. Access to all 3 floors with an amazing outdoor glass elevator overlooking the marina! The main floor has a spacious entrance lobby with the kitchen easily accessible from the 3 car tiled garage, a casual dining area, a cozy living room with fireplace, a larger more formal dining area and a TV or family room. The kitchen has cherry wood cabinets, lots of easy slide drawers, a recycling centre, grey granite counters, built in oven and lovely wet bar. Enjoy the spectacular views from a terraced large deck and garden area with rock walls down below the ozonated pool and relaxing hot tub, with easy care bushes that bloom throughout the year. Entertaining... no problem, in addition to the 3 car garage, there is up to an additional 7 parking spots for friends and family when visiting this amazing home."
    val description6 = "Amazingly beautiful .9 acre estate sized property centrally located in West Vancouver's lower British Properties and situated on a very quiet cul-de-sac. With expansive lawns and gorgeous manicured gardens this enormous and rare estate with a lovely home could be ideally suited for a growing family who love spending time outdoors. The sunny and bright south facing residence of 4 bedrooms with extra dens and flex space has had many updates over the years and the current owners of 33 years have meticulously maintained it. Nearby is the Hollyburn Country Club, Capilano Golf Club, Sentinel and Westcot public schools as well as Collingwood and Mulgrave private schools. Speedy access is very close at hand to downtown Vancouver and wonderful shopping at nearby Park Royal."
    val description7 = "Gorgeous estate sized (29,185 sq ft) sunny south facing very large property in Caulfeild with lovely ocean and city views. Very quiet street and neighbourhood, only a few minutes to Caulfeild Elementary, Rockridge Secondary and Caulfeild Shopping Center. This well maintained stylish 4 bedroom 3300 sq ft home with bright open concept plan has a gourmet kitchen, magnificent home office and a gorgeous 310 ft sundeck from which to enjoy the bright southerly vistas."
    val description8 = "Lovely bright south facing garden level 1 bedroom, 1 bathroom suite in immaculate condition in \"The Bristol\" a very well maintained concrete building located on a quiet cut-de-sac and within walking distance to Ambleside shops, restaurants, Fresh Street Market, Library, Seniors Center and sea-wall. A fantastic bonus is the separate, private entrance to your ground level suite with your parking stall very close by. No elevators or stairs required!"
    val description9 = "The only words to describe the amazing views from this newly upgraded, very spacious 1288 sq ft 2 bedroom/2 bathroom suite in The Westroyal would be: PANORAMIC, BREATHTAKING, UNOBSTRUCTED, AWESOME, SWEEPING 180 VIEWS. Recent improvements include: complete suite beautifully and professionally painted, updated fireplace surround, all new appliances, new blinds, upgraded mirrored doors. Other features include a linen closet, insuite laundry, gas fireplace, expansive covered outdoor balcony to enjoy the magnificent views. The Westroyal offers a large indoor pool and spa, fitness centre and resident manager/caretaker and is pet friendly. Enjoy convenient access across the street to Park Royal, with its many shops and restaurants and great walking along the Seawall to nearby Ambleside!"
    val description10 = "Spectacular 6 bedroom, 8 bathroom luxury Ambleside Estate property with ocean views steps away to West Van Secondary school. Situated on a large 12,461 sq ft lot this 6472 sq ft recently constructed residence is built to the very highest standards. Features include infloor radiant heat, air conditioning, wine cellar, Theater room and gym. Enjoy a gourmet chefs kitchen with top of the line WOLF and SUBZERO appliances and additional WOK kitchen. Beautifully landscaped with a very large expansive FLAT LEVEL YARD-excellent for families with children. Lower level has a large rec-room, media room and gym in addition to a very spacious AUTHORIZED 1 bedroom suite with self-contained kitchen and laundry."
    val description11 = "Stunning ocean, sunset an island views are to be had from this beautiful and spacious 4 bedroom 5 bathroom west coast contemporary home nestled on a quiet street within easy walking distance to Rockridge secondary and Caulfeild Village shopping mall. This bright, sunny open plan 2 level stylish family home has recently had many updates including all new windows and sliding doors, upgraded Duradeck outdoor decking, updated kitchen with stainless steel appliances, new LED lighting with dimmers and upgraded light fixtures and extensive landscaping with new planting in the gardens. Magnificent ocean views are enjoyed from the many rooms and decks within the home as well the terraced backyard is great for entertaining with custom built patios from which to enjoy the westerly views of the ocean and sunsets. This home comes with a double car garage and heated driveway."
    val description12 = "This spectacular West Vancouver waterfront home extensively renovated, is sitting on a large 20,128 sq ft peninsula like property and has the most incredible unobstructed ocean views imaginable. The 3 bedroom, 3 bathroom residence offers approx 3100 sq ft of open plan living space leading out to huge wrap around entertainment size decks overlooking the amazing 180 degree water views of Howe Sound. This is exceptional waterfront value in West Vancouver and only 5 minutes from Caulfeild Village shopping as well as great schools. This home comes with a level 2 car garage in addition to having ample street parking for visitors and guests."

    /**********************************************************************************************
     ** Photos offer_1
     **********************************************************************************************/

    val de = "file:///android_asset/Offer_1/645_Holmbury_Place_West_Vancouver_1.jpeg"

    val photo1_1 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_1/645_Holmbury_Place_West_Vancouver_1.jpeg",
        description = "main"
    )
    val photo1_2 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_1/645_Holmbury_Place_West_Vancouver_2.jpeg",
        description = "front"
    )
    val photo1_3 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_1/645_Holmbury_Place_West_Vancouver_5.jpeg",
        description = "living room"
    )
    val photo1_4 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_1/645_Holmbury_Place_West_Vancouver_9.jpeg",
        description = "living room"
    )
    val photo1_5 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_1/645_Holmbury_Place_West_Vancouver_12.jpeg",
        description = "kitchen"
    )
    val photo1_6 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_1/645_Holmbury_Place_West_Vancouver_29.jpeg",
        description = "bedroom"
    )
    val photo1_7 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_1/645_Holmbury_Place_West_Vancouver_30.jpeg",
        description = "bathroom"
    )
    val photo1_8 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_1/645_Holmbury_Place_West_Vancouver_31.jpeg",
        description = "bathroom"
    )
    val photo1_9 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_1/645_Holmbury_Place_West_Vancouver_36.jpeg",
        description = "bedroom"
    )
    val photo1_10 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_1/645_Holmbury_Place_West_Vancouver_45.jpeg",
        description = "bedroom"
    )
    val photo1_11 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_1/645_Holmbury_Place_West_Vancouver_56.jpeg",
        description = "living room"
    )
    val photo1_12 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_1/645_Holmbury_Place_West_Vancouver_78.jpeg",
        description = "aerial view"
    )

    /**********************************************************************************************
     ** Photos offer_2
     **********************************************************************************************/
    val photo2_1 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_2/Unit_4A_338_Taylor_Way_West_Vancouver_3.jpeg",
        description = "main"
    )
    val photo2_2 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_2/Unit_4A_338_Taylor_Way_West_Vancouver_5.jpeg",
        description = "hallway"
    )
    val photo2_3 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_2/Unit_4A_338_Taylor_Way_West_Vancouver_6.jpeg",
        description = "dining room"
    )
    val photo2_4 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_2/Unit_4A_338_Taylor_Way_West_Vancouver_7.jpeg",
        description = "dining room"
    )
    val photo2_5 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_2/Unit_4A_338_Taylor_Way_West_Vancouver_10.jpeg",
        description = "living room"
    )
    val photo2_6 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_2/Unit_4A_338_Taylor_Way_West_Vancouver_11.jpeg",
        description = "living room"
    )
    val photo2_7 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_2/Unit_4A_338_Taylor_Way_West_Vancouver_14.jpeg",
        description = "kitchen"
    )
    val photo2_8 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_2/Unit_4A_338_Taylor_Way_West_Vancouver_19.jpeg",
        description = "kitchen"
    )
    val photo2_9 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_2/Unit_4A_338_Taylor_Way_West_Vancouver_20.jpeg",
        description = "window bay"
    )
    val photo2_10 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_2/Unit_4A_338_Taylor_Way_West_Vancouver_24.jpeg",
        description = "guest room"
    )
    val photo2_11 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_2/Unit_4A_338_Taylor_Way_West_Vancouver_26.jpeg",
        description = "bedroom"
    )
    val photo2_12 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_2/Unit_4A_338_Taylor_Way_West_Vancouver_27.jpeg",
        description = "bedroom"
    )
    val photo2_13 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_2/Unit_4A_338_Taylor_Way_West_Vancouver_29.jpeg",
        description = "bathroom"
    )

    /**********************************************************************************************
     ** Photos offer_3
     **********************************************************************************************/
    val photo3_1 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_3/2974_Marine_Drive_West_Vancouver_1.jpeg",
        description = "main"
    )
    val photo3_2 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_3/2974_Marine_Drive_West_Vancouver_5.jpeg",
        description = "living room"
    )
    val photo3_3 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_3/2974_Marine_Drive_West_Vancouver_11.jpeg",
        description = "dining room"
    )
    val photo3_4 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_3/2974_Marine_Drive_West_Vancouver_13.jpeg",
        description = "kitchen"
    )
    val photo3_5 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_3/2974_Marine_Drive_West_Vancouver_16.jpeg",
        description = "living room"
    )
    val photo3_6 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_3/2974_Marine_Drive_West_Vancouver_18.jpeg",
        description = "bathroom"
    )
    val photo3_7 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_3/2974_Marine_Drive_West_Vancouver_22.jpeg",
        description = "home office"
    )
    val photo3_8 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_3/2974_Marine_Drive_West_Vancouver_25.jpeg",
        description = "bedroom"
    )
    val photo3_9 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_3/2974_Marine_Drive_West_Vancouver_31.jpeg",
        description = "balcony"
    )
    val photo3_10 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_3/2974_Marine_Drive_West_Vancouver_34.jpeg",
        description = "bathroom"
    )
    val photo3_11 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_3/2974_Marine_Drive_West_Vancouver_36.jpeg",
        description = "bedroom"
    )
    val photo3_12 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_3/2974_Marine_Drive_West_Vancouver_43.jpeg",
        description = "porch"
    )
    val photo3_13 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_3/2974_Marine_Drive_West_Vancouver_49.jpeg",
        description = "jacuzzi"
    )
    val photo3_14 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_3/2974_Marine_Drive_West_Vancouver_55.jpeg",
        description = "swimming pool"
    )

    /**********************************************************************************************
     ** Photos offer_4
     **********************************************************************************************/
    val photo4_1 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_4/Unit_22_4055_Indian_River_Drive_North_Vancouver_4.jpeg",
        description = "main"
    )
    val photo4_2 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_4/Unit_22_4055_Indian_River_Drive_North_Vancouver_6.jpeg",
        description = "living room"
    )
    val photo4_3 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_4/Unit_22_4055_Indian_River_Drive_North_Vancouver_7.jpeg",
        description = "living room"
    )
    val photo4_4 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_4/Unit_22_4055_Indian_River_Drive_North_Vancouver_10.jpeg",
        description = "living room"
    )
    val photo4_5 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_4/Unit_22_4055_Indian_River_Drive_North_Vancouver_11.jpeg",
        description = "dining room"
    )
    val photo4_6 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_4/Unit_22_4055_Indian_River_Drive_North_Vancouver_15.jpeg",
        description = "kitchen"
    )
    val photo4_7 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_4/Unit_22_4055_Indian_River_Drive_North_Vancouver_17.jpeg",
        description = "kitchen"
    )
    val photo4_8 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_4/Unit_22_4055_Indian_River_Drive_North_Vancouver_18.jpeg",
        description = "kitchen"
    )
    val photo4_9 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_4/Unit_22_4055_Indian_River_Drive_North_Vancouver_22.jpeg",
        description = "bedroom"
    )
    val photo4_10 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_4/Unit_22_4055_Indian_River_Drive_North_Vancouver_25.jpeg",
        description = "bathroom"
    )
    val photo4_11 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_4/Unit_22_4055_Indian_River_Drive_North_Vancouver_28.jpeg",
        description = "bedroom"
    )
    val photo4_12 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_4/Unit_22_4055_Indian_River_Drive_North_Vancouver_38.jpeg",
        description = "balcony"
    )
    /**********************************************************************************************
     ** Photos offer_5
     **********************************************************************************************/
    val photo5_1 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_5/5835_Marine_Drive_West_Vancouver_1.jpeg",
        description = "main"
    )
    val photo5_2 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_5/5835_Marine_Drive_West_Vancouver_7.jpeg",
        description = "living room"
    )
    val photo5_3 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_5/5835_Marine_Drive_West_Vancouver_9.jpeg",
        description = "kitchen"
    )
    val photo5_4 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_5/5835_Marine_Drive_West_Vancouver_11.jpeg",
        description = "kitchen"
    )
    val photo5_5 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_5/5835_Marine_Drive_West_Vancouver_13.jpeg",
        description = "living room"
    )
    val photo5_6 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_5/5835_Marine_Drive_West_Vancouver_15.jpeg",
        description = "dining room"
    )
    val photo5_7 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_5/5835_Marine_Drive_West_Vancouver_20.jpeg",
        description = "hallway"
    )
    val photo5_8 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_5/5835_Marine_Drive_West_Vancouver_22.jpeg",
        description = "bedroom"
    )
    val photo5_9 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_5/5835_Marine_Drive_West_Vancouver_23.jpeg",
        description = "bedroom"
    )
    val photo5_10 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_5/5835_Marine_Drive_West_Vancouver_25.jpeg",
        description = "bathroom"
    )
    val photo5_11 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_5/5835_Marine_Drive_West_Vancouver_34.jpeg",
        description = "bedroom"
    )
    val photo5_12 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_5/5835_Marine_Drive_West_Vancouver_52.jpeg",
        description = "swimming pool"
    )
    val photo5_13 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_5/5835_Marine_Drive_West_Vancouver_54.jpeg",
        description = "jacuzzi"
    )
    /**********************************************************************************************
     ** Photos offer_6
     **********************************************************************************************/
    val photo6_1 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_6/630_Holmbury_Place_West_Vancouver_6.jpeg",
        description = "main"
    )
    val photo6_2 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_6/630_Holmbury_Place_West_Vancouver_8.jpeg",
        description = "living room"
    )
    val photo6_3 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_6/630_Holmbury_Place_West_Vancouver_11.jpeg",
        description = "dining room"
    )
    val photo6_4 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_6/630_Holmbury_Place_West_Vancouver_12.jpeg",
        description = "kitchen"
    )
    val photo6_5 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_6/630_Holmbury_Place_West_Vancouver_14.jpeg",
        description = "kitchen"
    )
    val photo6_6 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_6/630_Holmbury_Place_West_Vancouver_15.jpeg",
        description = "kitchen"
    )
    val photo6_7 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_6/630_Holmbury_Place_West_Vancouver_23.jpeg",
        description = "living room"
    )
    val photo6_8 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_6/630_Holmbury_Place_West_Vancouver_26.jpeg",
        description = "bedroom"
    )
    val photo6_9 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_6/630_Holmbury_Place_West_Vancouver_27.jpeg",
        description = "bedroom"
    )
    val photo6_10 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_6/630_Holmbury_Place_West_Vancouver_28.jpeg",
        description = "bathroom"
    )
    val photo6_11 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_6/630_Holmbury_Place_West_Vancouver_31.jpeg",
        description = "bedroom"
    )
    val photo6_12 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_6/630_Holmbury_Place_West_Vancouver_44.jpeg",
        description = "backyard"
    )
    val photo6_13 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_6/630_Holmbury_Place_West_Vancouver_45.jpeg",
        description = "aerial view"
    )

    /**********************************************************************************************
     ** Photos offer_7
     **********************************************************************************************/
    val photo7_1 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_7/4669_Rutland_Road_West_Vancouver_3.jpeg",
        description = "main"
    )
    val photo7_2 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_7/4669_Rutland_Road_West_Vancouver_5.jpeg",
        description = "hallway"
    )
    val photo7_3 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_7/4669_Rutland_Road_West_Vancouver_9.jpeg",
        description = "living room"
    )
    val photo7_4 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_7/4669_Rutland_Road_West_Vancouver_17.jpeg",
        description = "kitchen"
    )
    val photo7_5 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_7/4669_Rutland_Road_West_Vancouver_18.jpeg",
        description = "kitchen"
    )
    val photo7_6 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_7/4669_Rutland_Road_West_Vancouver_21.jpeg",
        description = "dining room"
    )
    val photo7_7 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_7/4669_Rutland_Road_West_Vancouver_22.jpeg",
        description = "bedroom"
    )
    val photo7_8 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_7/4669_Rutland_Road_West_Vancouver_23.jpeg",
        description = "bedroom"
    )
    val photo7_9 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_7/4669_Rutland_Road_West_Vancouver_33.jpeg",
        description = "bathroom"
    )
    val photo7_10 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_7/4669_Rutland_Road_West_Vancouver_40.jpeg",
        description = "backyard"
    )

    /**********************************************************************************************
     ** Photos offer_8
     **********************************************************************************************/
    val photo8_1 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_8/Unit_101_1737_Duchess_Avenue_West_Vancouver_7.jpeg",
        description = "main"
    )
    val photo8_2 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_8/Unit_101_1737_Duchess_Avenue_West_Vancouver_14.jpeg",
        description = "kitchen"
    )
    val photo8_3 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_8/Unit_101_1737_Duchess_Avenue_West_Vancouver_16.jpeg",
        description = "living room"
    )
    val photo8_4 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_8/Unit_101_1737_Duchess_Avenue_West_Vancouver_19.jpeg",
        description = "hallway"
    )
    val photo8_5 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_8/Unit_101_1737_Duchess_Avenue_West_Vancouver_21.jpeg",
        description = "dinning room"
    )
    val photo8_6 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_8/Unit_101_1737_Duchess_Avenue_West_Vancouver_22.jpeg",
        description = "living room"
    )
    val photo8_7 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_8/Unit_101_1737_Duchess_Avenue_West_Vancouver_23.jpeg",
        description = "balcony"
    )
    val photo8_8 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_8/Unit_101_1737_Duchess_Avenue_West_Vancouver_24.jpeg",
        description = "window bay"
    )
    val photo8_9 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_8/Unit_101_1737_Duchess_Avenue_West_Vancouver_25.jpeg",
        description = "bedroom"
    )
    val photo8_10 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_8/Unit_101_1737_Duchess_Avenue_West_Vancouver_26.jpeg",
        description = "bedroom"
    )
    val photo8_11 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_8/Unit_101_1737_Duchess_Avenue_West_Vancouver_27.jpeg",
        description = "bathroom"
    )

    /**********************************************************************************************
     ** Photos offer_9
     **********************************************************************************************/
    val photo9_1 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_9/Unit_16A_338_Taylor_Way_West_Vancouver_8.jpeg",
        description = "main"
    )
    val photo9_2 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_9/Unit_16A_338_Taylor_Way_West_Vancouver_10.jpeg",
        description = "living room"
    )
    val photo9_3 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_9/Unit_16A_338_Taylor_Way_West_Vancouver_11.jpeg",
        description = "living room"
    )
    val photo9_4 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_9/Unit_16A_338_Taylor_Way_West_Vancouver_16.jpeg",
        description = "kitchen"
    )
    val photo9_5 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_9/Unit_16A_338_Taylor_Way_West_Vancouver_18.jpeg",
        description = "kitchen"
    )
    val photo9_6 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_9/Unit_16A_338_Taylor_Way_West_Vancouver_19.jpeg",
        description = "window bay"
    )
    val photo9_7 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_9/Unit_16A_338_Taylor_Way_West_Vancouver_20.jpeg",
        description = "bedroom"
    )
    val photo9_8 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_9/Unit_16A_338_Taylor_Way_West_Vancouver_21.jpeg",
        description = "bedroom"
    )
    val photo9_9 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_9/Unit_16A_338_Taylor_Way_West_Vancouver_24.jpeg",
        description = "bedroom"
    )
    val photo9_10 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_9/Unit_16A_338_Taylor_Way_West_Vancouver_27.jpeg",
        description = "balcony"
    )
    val photo9_11 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_9/Unit_16A_338_Taylor_Way_West_Vancouver_29.jpeg",
        description = "balcony"
    )
    /**********************************************************************************************
     ** Photos offer_10
     **********************************************************************************************/
    val photo10_1 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_10/1885_St_Denis_360hometours_1.jpeg",
        description = "main"
    )
    val photo10_2 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_10/1885_St_Denis_360hometours_2.jpeg",
        description = "living room"
    )
    val photo10_3 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_10/1885_St_Denis_360hometours_3.jpeg",
        description = "kitchen"
    )
    val photo10_4 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_10/1885_St_Denis_360hometours_4.jpeg",
        description = "kitchen"
    )
    val photo10_5 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_10/1885_St_Denis_360hometours_5.jpeg",
        description = "living room"
    )
    val photo10_6 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_10/1885_St_Denis_360hometours_6.jpeg",
        description = "porch"
    )
    val photo10_7 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_10/1885_St_Denis_360hometours_7.jpeg",
        description = "bedroom"
    )
    val photo10_8 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_10/1885_St_Denis_360hometours_8.jpeg",
        description = "bathroom"
    )
    val photo10_9 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_10/1885_St_Denis_360hometours_9.jpeg",
        description = "bathroom"
    )
    val photo10_10 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_10/1885_St_Denis_360hometours_10.jpeg",
        description = "bedroom"
    )
    val photo10_11 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_10/1885_St_Denis_360hometours_11.jpeg",
        description = "home cinema"
    )
    val photo10_12 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_10/1885_St_Denis_360hometours_12.jpeg",
        description = "gym room"
    )
    val photo10_13 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_10/1885_St_Denis_360hometours_13.jpeg",
        description = "basement"
    )
    /**********************************************************************************************
     ** Photos offer_11
     **********************************************************************************************/
    val photo11_1 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_11/4765_Westwood_Drive_West_Vancouver_1.jpeg",
        description = "main"
    )
    val photo11_2 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_11/4765_Westwood_Drive_West_Vancouver_2.jpeg",
        description = "living room"
    )
    val photo11_3 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_11/4765_Westwood_Drive_West_Vancouver_3.jpeg",
        description = "dining room"
    )
    val photo11_4 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_11/4765_Westwood_Drive_West_Vancouver_4.jpeg",
        description = "kitchen"
    )
    val photo11_5 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_11/4765_Westwood_Drive_West_Vancouver_5.jpeg",
        description = "kitchen"
    )
    val photo11_6 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_11/4765_Westwood_Drive_West_Vancouver_6.jpeg",
        description = "kitchen"
    )
    val photo11_7 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_11/4765_Westwood_Drive_West_Vancouver_7.jpeg",
        description = "bedroom"
    )
    val photo11_8 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_11/4765_Westwood_Drive_West_Vancouver_8.jpeg",
        description = "laundry room"
    )
    val photo11_9 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_11/4765_Westwood_Drive_West_Vancouver_9.jpeg",
        description = "bedroom"
    )
    val photo11_10 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_11/4765_Westwood_Drive_West_Vancouver_10.jpeg",
        description = "bedroom"
    )
    val photo11_11 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_11/4765_Westwood_Drive_West_Vancouver_11.jpeg",
        description = "bathroom"
    )
    val photo11_12 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_11/4765_Westwood_Drive_West_Vancouver_12.jpeg",
        description = "balcony"
    )
    /**********************************************************************************************
     ** Photos offer_12
     **********************************************************************************************/
    val photo12_1 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_12/8235_Pasco_Road_West_Vancouver_1.jpeg",
        description = "main"
    )
    val photo12_2 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_12/8235_Pasco_Road_West_Vancouver_2.jpeg",
        description = "bedroom"
    )
    val photo12_3 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_12/8235_Pasco_Road_West_Vancouver_3.jpeg",
        description = "bedroom"
    )
    val photo12_4 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_12/8235_Pasco_Road_West_Vancouver_4.jpeg",
        description = "bathroom"
    )
    val photo12_5 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_12/8235_Pasco_Road_West_Vancouver_5.jpeg",
        description = "living room"
    )
    val photo12_6 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_12/8235_Pasco_Road_West_Vancouver_6.jpeg",
        description = "dining room"
    )
    val photo12_7 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_12/8235_Pasco_Road_West_Vancouver_7.jpeg",
        description = "balcony"
    )
    val photo12_8 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_12/8235_Pasco_Road_West_Vancouver_8.jpeg",
        description = "bedroom"
    )
    val photo12_9 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_12/8235_Pasco_Road_West_Vancouver_9.jpeg",
        description = "jacuzzi"
    )
    val photo12_10 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_12/8235_Pasco_Road_West_Vancouver_10.jpeg",
        description = "sauna"
    )
    val photo12_11 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_12/8235_Pasco_Road_West_Vancouver_11.jpeg",
        description = "living room"
    )
    val photo12_12 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_12/8235_Pasco_Road_West_Vancouver_12.jpeg",
        description = "living room"
    )
    val photo12_13 = Photo(
        UUID.randomUUID().toString(),
        uri = "file:///android_asset/Offer_12/8235_Pasco_Road_West_Vancouver_13.jpeg",
        description = "kitchen"
    )

    /**********************************************************************************************
     ** Address
     **********************************************************************************************/
    val address1 = Address(
        "645 Holmbury Place",
        "",
        "V7S 1P8",
        "Vancouver",
        "BC",
        "Canada"
    )
    val address2 = Address(
        "338 Taylor Way",
        "Unit 4A",
        "V7T 1A6",
        "Vancouver",
        "BC",
        "Canada"
    )
    val address3 = Address(
        "2974 Marine Drive",
        "",
        "V7V 1M2",
        "Vancouver",
        "BC",
        "Canada"
    )
    val address4 = Address(
        "4055 Indian River Drive",
        "Unit 22",
        "V7G 2R1",
        "Vancouver",
        "BC",
        "Canada"
    )
    val address5 = Address(
        "5835 Marine Drive",
        "",
        "V7W 2S1",
        "Vancouver",
        "BC",
        "Canada"
    )
    val address6 = Address(
        "630 Holmbury Place",
        "",
        "V7S 1P7",
        "Vancouver",
        "BC",
        "Canada"
    )
    val address7 = Address(
        "4669 Rutland Road",
        "",
        "V7W 1G6",
        "Vancouver",
        "BC",
        "Canada"
    )
    val address8 = Address(
        "1737 Duchess Avenue",
        "Unit 101",
        "V7V 1P8",
        "Vancouver",
        "BC",
        "Canada"
    )
    val address9 = Address(
        "338 Taylor Way",
        "Unit 16A",
        "V7T 1A6",
        "Vancouver",
        "BC",
        "Canada"
    )
    val address10 = Address(
        "1885 St Denis Road",
        "",
        "V7V 3W4",
        "Vancouver",
        "BC",
        "Canada"
    )
    val address11 = Address(
        "4765 Westwood Drive",
        "",
        "V7S 3B5",
        "Vancouver",
        "BC",
        "Canada"
    )
    val address12 = Address(
        "8235 Pasco Road",
        "",
        "V7W 2T5",
        "Vancouver",
        "BC",
        "Canada"
    )

    /**********************************************************************************************
     ** Offers
     **********************************************************************************************/

    val offer1 = Offer(
        UUID.randomUUID().toString(),
        PropertyType.HOUSE,
        OfferType.SALE,
        true,
        6598000,
        703,
        7,
        7,
        mutableListOf(Particularities.SWIMMING_POOL, Particularities.BACKYARD, Particularities.GARDEN, Particularities.BALCONY, Particularities.GARAGE),
        description1,
        mutableListOf(photo1_1, photo1_2, photo1_3, photo1_4, photo1_5, photo1_6, photo1_7, photo1_8, photo1_9, photo1_10, photo1_11, photo1_12),
        photo1_1.id,
        address1,
        mutableListOf(POI.PARK, POI.CULTURAL_CENTER),
        agent1.id,
        System.currentTimeMillis().toLong()
    )

    val offer2 = Offer(
        UUID.randomUUID().toString(),
        PropertyType.APARTMENT,
        OfferType.RENT,
        true,
        2300,
        120,
        2,
        2,
        mutableListOf(Particularities.PARKING_LOT, Particularities.BALCONY),
        description2,
        mutableListOf(photo2_1, photo2_2, photo2_3, photo2_4, photo2_5, photo2_6, photo2_7, photo2_8, photo2_9, photo2_10, photo2_11, photo2_12, photo2_13),
        photo2_1.id,
        address2,
        mutableListOf(POI.PARK, POI.SCHOOL, POI.MARKET_MALL),
        agent3.id,
        System.currentTimeMillis().toLong()
    )

    val offer3 = Offer(
        UUID.randomUUID().toString(),
        PropertyType.HOUSE,
        OfferType.SALE,
        true,
        12598000,
        857,
        4,
        6,
        mutableListOf(Particularities.SWIMMING_POOL, Particularities.BACKYARD, Particularities.GARDEN, Particularities.BALCONY, Particularities.GARAGE, Particularities.JACUZZI),
        description3,
        mutableListOf(photo3_1, photo3_2, photo3_3, photo3_4, photo3_5, photo3_6, photo3_7, photo3_8, photo3_9, photo3_10, photo3_11, photo3_12, photo3_13, photo3_14),
        photo3_1.id,
        address3,
        mutableListOf(POI.PARK, POI.SPORT_CENTER),
        agent3.id,
        System.currentTimeMillis().toLong()
    )

    val offer4 = Offer(
        UUID.randomUUID().toString(),
        PropertyType.HOUSE,
        OfferType.SALE,
        true,
        2487000,
        234,
        3,
        3,
        mutableListOf(Particularities.BACKYARD, Particularities.BALCONY, Particularities.GARAGE),
        description4,
        mutableListOf(photo4_1, photo4_2, photo4_3, photo4_4, photo4_5, photo4_6, photo4_7, photo4_8, photo4_9, photo4_10, photo4_11, photo4_12),
        photo4_1.id,
        address4,
        mutableListOf(POI.PARK, POI.CULTURAL_CENTER),
        agent2.id,
        System.currentTimeMillis().toLong()
    )

    val offer5 = Offer(
        UUID.randomUUID().toString(),
        PropertyType.HOUSE,
        OfferType.SALE,
        false,
        4356000,
        467,
        6,
        6,
        mutableListOf(Particularities.SWIMMING_POOL, Particularities.JACUZZI, Particularities.BACKYARD, Particularities.GARDEN, Particularities.BALCONY, Particularities.GARAGE),
        description5,
        mutableListOf(photo5_1, photo5_2, photo5_3, photo5_4, photo5_5, photo5_6, photo5_7, photo5_8, photo5_9, photo5_10, photo5_11, photo5_12, photo5_13),
        photo5_1.id,
        address5,
        mutableListOf(POI.SCHOOL, POI.MEDICAL_CENTER),
        agent1.id,
        System.currentTimeMillis().toLong()
    )

    val offer6 = Offer(
        UUID.randomUUID().toString(),
        PropertyType.HOUSE,
        OfferType.RENT,
        true,
        3500,
        331,
        4,
        2,
        mutableListOf(Particularities.BACKYARD, Particularities.BASEMENT, Particularities.BALCONY, Particularities.GARAGE),
        description6,
        mutableListOf(photo6_1, photo6_2, photo6_3, photo6_4, photo6_5, photo6_6, photo6_7, photo6_8, photo6_9, photo6_10, photo6_11, photo6_12, photo6_13),
        photo6_1.id,
        address6,
        mutableListOf(POI.PARK, POI.CULTURAL_CENTER, POI.SCHOOL, POI.MARKET_MALL),
        agent2.id,
        System.currentTimeMillis().toLong()
    )

    val offer7 = Offer(
        UUID.randomUUID().toString(),
        PropertyType.HOUSE,
        OfferType.SALE,
        false,
        4356000,
        308,
        4,
        3,
        mutableListOf(Particularities.BACKYARD, Particularities.BASEMENT, Particularities.BALCONY, Particularities.GARAGE),
        description7,
        mutableListOf(photo7_1, photo7_2, photo7_3, photo7_4, photo7_5, photo7_6, photo7_7, photo7_8, photo7_9, photo7_10),
        photo7_1.id,
        address7,
        mutableListOf(POI.PARK, POI.BUS_STATION),
        agent3.id,
        System.currentTimeMillis().toLong()
    )

    val offer8 = Offer(
        UUID.randomUUID().toString(),
        PropertyType.APARTMENT,
        OfferType.RENT,
        true,
        2200,
        84,
        2,
        1,
        mutableListOf(Particularities.BALCONY),
        description8,
        mutableListOf(photo8_1, photo8_2, photo8_3, photo8_4, photo8_5, photo8_6, photo8_7, photo8_8, photo8_9, photo8_10, photo8_11),
        photo8_1.id,
        address8,
        mutableListOf(POI.SCHOOL, POI.CULTURAL_CENTER, POI.SPORT_CENTER, POI.MARKET_MALL, POI.BAR_COFFEESHOP),
        agent1.id,
        System.currentTimeMillis().toLong()
    )

    val offer9 = Offer(
        UUID.randomUUID().toString(),
        PropertyType.APARTMENT,
        OfferType.RENT,
        false,
        3200,
        120,
        2,
        2,
        mutableListOf(Particularities.BALCONY, Particularities.GARAGE),
        description9,
        mutableListOf(photo9_1, photo9_2, photo9_3, photo9_4, photo9_5, photo9_6, photo9_7, photo9_8, photo9_9, photo9_10, photo9_11),
        photo9_1.id,
        address9,
        mutableListOf(POI.PARK, POI.MARKET_MALL, POI.SCHOOL, POI.BAR_COFFEESHOP, POI.RESTAURANT),
        agent3.id,
        System.currentTimeMillis().toLong()
    )

    val offer10 = Offer(
        UUID.randomUUID().toString(),
        PropertyType.HOUSE,
        OfferType.SALE,
        true,
        12650000,
        868,
        6,
        8,
        mutableListOf(Particularities.SWIMMING_POOL, Particularities.BACKYARD, Particularities.GARDEN, Particularities.BALCONY, Particularities.GARAGE, Particularities.GYM_ROOM),
        description10,
        mutableListOf(photo10_1, photo10_2, photo10_3, photo10_4, photo10_5, photo10_6, photo10_7, photo10_8, photo10_9, photo10_10, photo10_11, photo10_12, photo10_13),
        photo10_1.id,
        address10,
        mutableListOf(POI.PARK, POI.RESTAURANT),
        agent2.id,
        System.currentTimeMillis().toLong()
    )

    val offer11 = Offer(
        UUID.randomUUID().toString(),
        PropertyType.HOUSE,
        OfferType.RENT,
        true,
        4200,
        386,
        4,
        5,
        mutableListOf(Particularities.BACKYARD, Particularities.BALCONY, Particularities.GARAGE),
        description11,
        mutableListOf(photo11_1, photo11_2, photo11_3, photo11_4, photo11_5, photo11_6, photo11_7, photo11_8, photo11_9, photo11_10, photo11_11, photo11_12),
        photo11_1.id,
        address11,
        mutableListOf(POI.PARK, POI.SCHOOL, POI.BAR_COFFEESHOP),
        agent1.id,
        System.currentTimeMillis().toLong()
    )
    val offer12 = Offer(
        UUID.randomUUID().toString(),
        PropertyType.HOUSE,
        OfferType.SALE,
        false,
        6398000,
        348,
        3,
        3,
        mutableListOf(Particularities.JACUZZI, Particularities.SAUNA, Particularities.GARDEN, Particularities.BALCONY, Particularities.GARAGE),
        description12,
        mutableListOf(photo12_1, photo12_2, photo12_3, photo12_4, photo12_5, photo12_6, photo12_7, photo12_8, photo12_9, photo12_10, photo12_11, photo12_12, photo12_13),
        photo12_1.id,
        address12,
        mutableListOf(POI.PARK),
        agent2.id,
        System.currentTimeMillis().toLong()
    )

}