package jbcu10.dev.medalert.config;

import org.json.JSONObject;

/**
 * Created by dev on 12/16/17.
 */

public class FirstAidConstant {


    public static String firstAidJson = "{\n" +
            "    \"data\": [\n" +
            "        {\n" +
            "            \"name\": \"Bleeding\",\n" +
            "            \"instruction\": [\n" +
            "                \" Keep victim lying down.\",\n" +
            "                \" Apply direct pressure using a clean cloth or sterile dressing directly on the wound.\",\n" +
            "                \" DO NOT take out any object that is lodged in a wound; see a doctor for help in removal.\",\n" +
            "                \" If there are no signs of a fracture in the injured area, carefully elevate the wound above the victim's heart.\",\n" +
            "                \" Once bleeding is controlled, keep victim warm by covering with a blanket, continuing to monitor for shock.\"\n" +
            "            ],\n" +
            "            \"link\": \" https://www.youtube.com/watch?v=y8CYSFgBJiI\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"Cleaning and Bandaging wounds\",\n" +
            "            \"instruction\": [\n" +
            "                \" Wash your hands and cleanse the injured area with clean soap and water, then blot dry.\",\n" +
            "                \" Apply antibiotic ointment to minor wound and cover with a sterile gauze dressing or bandage that is slightly larger than the actual wound.\"\n" +
            "            ],\n" +
            "            \"link\": \" https://www.youtube.com/watch?v=-47APzU3sXQ\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"Eye Injuries\",\n" +
            "            \"instruction\": [\n" +
            "                \" Cover both eyes with sterile dressings or eye cups to immobilize.\",\n" +
            "                \" Covering both eyes will minimize the movement of the injured eye\",\n" +
            "                \" DO NOT rub or apply pressure, ice, or raw meat to the injured eye.\",\n" +
            "                \"If the injury is a black eye, you may apply ice to cheek and area around eye, but not directly on the eyeball itself.\"\n" +
            "            ],\n" +
            "            \"link\": \" https://www.youtube.com/watch?v=PHrrxe3p8vw\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"Chemical Burns\",\n" +
            "            \"instruction\": [\n" +
            "                \" Flush the affected area with cool running water for at least 15 minutes.\",\n" +
            "                \"Remove all clothing and jewelry that has been contaminated.\",\n" +
            "                \"Monitor victim for shock and seek medical assistance.\",\n" +
            "                \"If chemical burn is in the eyes, flush continuously with water and seek medical attention immediately.\"\n" +
            "            ],\n" +
            "            \"link\": \" https://www.youtube.com/watch?v=CPTuZEqwo3k\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"SunBurn\",\n" +
            "            \"instruction\": [\n" +
            "                \" Avoid any further exposure to direct sunlight.\",\n" +
            "                \"Drink plenty of water to prevent dehydration.\",\n" +
            "                \"Do not apply cold water or ice to a severe burn.\",\n" +
            "                \"Use over-the-counter remedies to remove discomfort.\",\n" +
            "                \"If burn is severe and blisters develop, seek medical attention.\"\n" +
            "            ],\n" +
            "            \"link\": \" https://www.youtube.com/watch?v=ZeIN4ESgzu8\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"Unconsciousness\",\n" +
            "            \"instruction\": [\n" +
            "                \" Do not leave an unconscious victim alone.\",\n" +
            "                \"Assess victim’s state of awareness by asking if they are OK.\",\n" +
            "                \"Check the victim’s Airway, Breathing, and Circulation (ABC’s).\",\n" +
            "                \"If the victim’s ABC’s are not present, perform CPR. IMPORTANT: only a trained & qualified person should administer CPR.\",\n" +
            "                \"If ABC’s are present and spinal injury is not suspected, place victim on their side with their chin toward the ground to allow for secretion drainage.\",\n" +
            "                \"Cover the victim with blanket to keep warm and prevent shock. If victim communicates feeling warm, remove blanket.\"\n" +
            "            ],\n" +
            "            \"link\": \" https://www.youtube.com/watch?v=eTKo4DVVS58\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"Choking\",\n" +
            "            \"instruction\": [\n" +
            "                \" Ask the victim\",\n" +
            "                \"Are you OK?\",\n" +
            "                \"do not interfere or give first aid if the victim can speak, breathe, or cough.\",\n" +
            "                \"If the victim cannot speak, breathe, or cough, ask for someone to call 911 and then perform the Heimlich maneuver (abdominal thrust).\",\n" +
            "                \"How to perform the Heimlich maneuver: Position yourself behind the victim with your arms around victim’s stomach. Place the thumb-side of your fist above the victim’s navel and below the lower end of the breastbone. Take hold of your fist with your free hand and pull fist upward and in, quickly and firmly. Continue with thrusts until the object is dislodged or airway is clear.\"\n" +
            "            ],\n" +
            "            \"link\": \" https://www.youtube.com/watch?v=PeWQrTUt4JA\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"Poison\",\n" +
            "            \"instruction\": [\n" +
            "                \" Antidotes on labels may be wrong!! do not follow them unless instructed by a physician.never give anything by mouth (milk, water, Ipecac, etc.) until you have consulted with a medical professional.\",\n" +
            "                \"Keep a one ounce bottle of Ipecac on hand at all times in case of an emergency, and give only when instructed by a physician.\",\n" +
            "                \"If the poison is on the skin, flush skin with water for 15 minutes, then wash and rinse with soap and water.\",\n" +
            "                \"If poison is in the eye, flush with lukewarm water for 15 minutes. Adults can stand under the shower with eyes open. always consult medical professionals after any eye injury has occurred.\"\n" +
            "            ],\n" +
            "            \"link\": \" https://www.youtube.com/watch?v=RrPsfeij-bA\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"Strains and Sprains\",\n" +
            "            \"instruction\": [\n" +
            "                \" Rest: Rest the injured part until it's less painful.\",\n" +
            "                \"Ice: Wrap an icepack or cold compress in a towel and place over the injured part immediately. Continue for no more than 20 minutes at a time, four to eight times a day.\",\n" +
            "                \"Compression: Support the injured part with an elastic compression bandage for at least 2 days.\",\n" +
            "                \"Elevation: Raise the injured part above heart level to decrease swelling.\"\n" +
            "            ],\n" +
            "            \"link\": \"https://www.youtube.com/watch?v=9QihE9DqLTM\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"Animal Bites\",\n" +
            "            \"instruction\": [\n" +
            "                \" Control any bleeding by applying direct pressure or with elevation.To avoid risk of infection, do not close wound.\",\n" +
            "                \"Rinse the bite thoroughly, holding it under running water. Cleanse with soap and water and hold under water again for five minutes.\",\n" +
            "                \"do not put ointments or medicines on wound. Cover with dry sterile bandage or gauze.\",\n" +
            "                \"seek medical assistance immediately.\",\n" +
            "                \"note: report animal and human bites to local police and/or health authorities.\"\n" +
            "            ],\n" +
            "            \"link\": \"https://www.youtube.com/watch?v=r1iqunl4HSo\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"Vomiting\",\n" +
            "            \"instruction\": [\n" +
            "                \" Have the person drink small amounts of water, sports drinks, or clear liquids.\",\n" +
            "                \"Don't give the person solid food until vomiting has stopped.\",\n" +
            "                \"When the person can tolerate food, try small amounts of the BRAT diet: bananas, rice, applesauce, and toast.\"\n" +
            "            ],\n" +
            "            \"link\": \"https://www.youtube.com/watch?v=4qCN18xyxkw\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"Heart Stroke\",\n" +
            "            \"instruction\": [\n" +
            "                \" Put the person in a cool tub of water or a cool shower.\",\n" +
            "                \"Spray with a garden hose.\",\n" +
            "                \"Sponge with cool water.\",\n" +
            "                \"Fan while misting with cool water.\",\n" +
            "                \"Place ice packs or cool wet towels on the neck, armpits and groin.\",\n" +
            "                \"Cover with cool damp sheets.\"\n" +
            "            ],\n" +
            "            \"link\": \" https://m.youtube.com/watch?v=8ujHlU3F1GI\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"Heart Cramps\",\n" +
            "            \"instruction\": [\n" +
            "                \"Rest briefly and cool down\",\n" +
            "                \"Drink clear juice or an electrolyte-containing sports drink.\",\n" +
            "                \"Practice gentle, range-of-motion stretching and gentle massage of the affected muscle group\",\n" +
            "                \"Don't resume strenuous activity for several hours or longer after heat cramps go away.\",\n" +
            "                \"Call your doctor if your cramps don't go away within one hour or so \"\n" +
            "            ],\n" +
            "            \"link\": \" https://m.youtube.com/watch?v=8ujHlU3F1GI\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"Epilepsy\",\n" +
            "            \"instruction\": [\n" +
            "                \" Stay calm.\",\n" +
            "                \"Look around - is the person in a dangerous place?  If not, don't move them.\",\n" +
            "                \" Move objects like furniture away from them.\",\n" +
            "                \"Note the time the seizure starts.\",\n" +
            "                \"Stay with them. If they don't collapse but seem blank or confused, gently guide them away from any danger.\",\n" +
            "                \"Speak quietly and calmly.\",\n" +
            "                \"Cushion their head with something soft if they have collapsed to the ground.\",\n" +
            "                \"Don't hold them down.\",\n" +
            "                \"Don't put anything in their mouth.\",\n" +
            "                \"Check the time again. If a convulsive (shaking) seizure doesn't stop after 5 minutes\",\n" +
            "                \"After the seizure has stopped, put them into the recovery position and check that their breathing is returning to normal.  Gently check their mouth to see that nothing is blocking their airway such as food or false teeth. If their breathing sounds difficult after the seizure has stopped, call for an ambulance.\",\n" +
            "                \"Stay with them until they are fully recovered.\"\n" +
            "            ],\n" +
            "            \"link\": \" https://www.youtube.com/watch?v=jrqq7QUHOIM\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"Electric Shock\",\n" +
            "            \"instruction\": [\n" +
            "                \" Don't touch the injured person if he or she is still in contact with the electrical current.\",\n" +
            "                \"Call your local emergency number if the source of the burn is a high-voltage wire or lightning.\",\n" +
            "                \" Don't get near high-voltage wires until the power is turned off. Overhead power lines usually aren't insulated. Stay at least 20 feet (about 6 meters) away — farther if wires are jumping and sparking.\",\n" +
            "                \"Don't move a person with an electrical injury unless he or she is in immediate danger.\"\n" +
            "            ],\n" +
            "            \"link\": \" https://www.youtube.com/watch?v=jrqq7QUHOIM\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";



    public static String[][] firstaid = {
            {"Poisoning and Overdose",
                    "Determine what substance is involved and how taken.",
                    "Stay with victim and assist as necessary.",
                    "If choking, lower victims head.",
                    "Collect remainder of substance."},

            {
                    "Fainting, Unconsciousness and Shock",
                    "Have victim lie or sit down and rest.",
                    "Keep victim comfortable, not hot or cold.",
                    "Ask or look for emergency medical I.D.",
                    "Treat other injuries as necessary."
            },

            {
                    "Burns, Thermal and Chemical",
                    "Remove all affected clothing.",
                    "Flood chemical burn with cool water.",
                    "Cover burn with dry bandage.",
                    "Keep victim quiet and comfortable."
            },

            {
                    "Basic instructions on how to stop bleeding.",
                    "Stop the bleeding by applying direct pressure over the wound with fingers or with hand.",
                    "Lay the severely bleeding casualty immediately down.",
                    "If bandages are at hand, apply a pressure bandage over the wound.",
                    "Severe bleeding may lead to shock, a critical condition, caused by disturbaces in the circulation."
            },

            {
                    "Choking",
                    "Check victims mouth and clear of foreign matter.",
                    "Use abdominal thrusts (only if victim is unconscious).",
                    "",
                    ""
            },

            {
                    "Heart Attack",
                    "Place victim lying down on back.",
                    "Give CPR as necessary.",
                    "Keep victim comfortable, not hot or cold.",
                    "Ask or look for emergency medical I.D."
            },

            {
                    "Fractures and Sprains",
                    "Keep victim still.",
                    "Keep injured area immobile.",
                    "",
                    ""
            },

            {
                    "Hypovolemic Shock",
                    "Check victim’s airway, breathing and circulation.",
                    "Lay victim down on his /her back.",
                    "Raise the victim’s legs 8-12 inches.",
                    "Prevent body heat loss by wrapping blankets, coats, etc. around victim."
            }
    };


}
