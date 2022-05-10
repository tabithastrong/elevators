from os import listdir
from os.path import isfile, join

onlyfiles = [f for f in listdir("./") if isfile(join("./", f))]

item_model = """{{
  "parent": "tabithas_elevators:block/{0}"
}}
"""

block_model = """{{
  "parent": "minecraft:block/slab",
  "textures": {{
    "bottom": "tabithas_elevators:blocks/{0}",
    "top": "tabithas_elevators:blocks/{0}",
    "side": "tabithas_elevators:blocks/{1}"
  }}
}}
"""

block_model_top = """{{
  "parent": "minecraft:block/slab_top",
  "textures": {{
    "bottom": "tabithas_elevators:blocks/{0}",
    "top": "tabithas_elevators:blocks/{0}",
    "side": "tabithas_elevators:blocks/{1}"
  }}
}}
"""

block_state = """{{
  "variants": {{
    "type=bottom": {{
      "model": "tabithas_elevators:block/{0}"
    }},
    "type=double": {{
      "model": "tabithas_elevators:block/{1}"
    }},
    "type=top": {{
      "model": "tabithas_elevators:block/{2}"
    }}
  }}
}}"""

print(block_state.format("Penis", "penis", "penis"))

for x in onlyfiles:
  if not x.endswith("_slab.png"):
      continue

  # file = elevator_black_slab
  file, extension = x.split(".")

  # slab_type = elevator_black  
  slab_type = file.replace("_slab", "")

  if extension == "png":
      item_model_slab = item_model.format(file)
      block_model_slab = block_model.format(slab_type, file)
      block_model_slab_top = block_model_top.format(slab_type, file)

      block_state_slab = block_state.format(file, slab_type, file + "_top")

      imf = open("../../models/item/{}.json".format(file), "w")
      imf.write(item_model_slab)
      imf.close()

      bmf = open("../../models/block/{}.json".format(file), "w")
      bmf.write(block_model_slab)
      bmf.close()

      bmft = open("../../models/block/{}.json".format(file + "_top"), "w")
      bmft.write(block_model_slab_top)
      bmft.close()

      bsf = open("../../blockstates/{}.json".format(file), "w")
      bsf.write(block_state_slab)
      bsf.close()