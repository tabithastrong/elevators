loot_table_template = """
{{
  "type": "minecraft:block",
  "pools": [
    {{
      "rolls": 1,
      "entries": [
        {{
          "type": "minecraft:item",
          "name": "tabithas_elevators:elevator_{0}"
        }}
      ],
      "conditions": [
        {{
          "condition": "minecraft:survives_explosion"
        }}
      ]
    }}
  ]
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
    file = open("elevator_" + color + ".json", "w")
    file.write(loot_table_template.format(color))
    file.close()

    file = open("elevator_" + color + "_slab.json", "w")
    file.write(loot_table_template.format(color + "_slab"))
    file.close()
