recipe_template = """
    {{
        "type": "minecraft:crafting_shaped",
        "pattern": [
            "ED"
        ],
        "key": {{
            "E": {{
            "tag": "tabithas_elevators:elevator"
            }},
            "D": {{
            "item": "minecraft:{0}_dye"
            }}
        }},
        "result": {{
            "item": "tabithas_elevators:elevator_{0}",
            "count": 1
        }}
    }}
"""

recipe_template_slab = """
    {{
        "type": "minecraft:crafting_shaped",
        "pattern": [
            "SSS"
        ],
        "key": {{
            "S": {{
            "item": "tabithas_elevators:elevator_{0}"
            }}
        }},
        "result": {{
            "item": "tabithas_elevators:elevator_{0}_slab",
            "count": 6
        }}
    }}
"""


color_array = [
    "white",
    "black",
    "brown",
    "blue",
    "yellow",
    "green",
    "red",
    "orange",
    "magenta",
    "light_blue",
    "lime",
    "pink",
    "gray",
    "light_gray",
    "cyan",
    "purple"
]

for color in color_array:
    print("\"tabithas_elevators:elevator_" + color + "\",")

for color in color_array:
    file = open("elevator_" + color + ".json", "w")
    file.write(recipe_template.format(color))
    file.close()

    file = open("elevator_" + color + "_slab.json", "w")
    file.write(recipe_template_slab.format(color))
    file.close()
