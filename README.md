# JsonWrangler

*A tool for all your JSON-modifying needs*

JsonWrangler is a Minecraft mod which allows for the modification of game data and resources, as stored in JSON files,
through groovy scripts which can be packaged in resource or data packs.

## Usage

JsonWrangler should allow modification of basically any JSON file with a "winner takes all" behavior - for instance,
loot tables, models, worldgen data, or animation metadata files. If JsonWrangler fails to wrangle a file that you think
it should, please submit a bug report. If you wish to modify a file at a given path, say,
`assets/minecraft/textures/block/magma.png.mcmeta` or `data/minecraft/loot_tables/chests/simple_dungeon.json`, you
should create a groovy script at same path, but with a `.groovy` at the end - in this case,
`assets/minecraft/textures/block/magma.png.mcmeta.groovy` or `data/minecraft/loot_tables/chests/simple_dungeon.json.groovy`.
Inside of your groovy script, you are given several arguments, and must return a value which will replace the original
JSON file. Valid values to return include maps, where map keys must be strings and values may be
lists, maps, numbers, booleans, strings, or dates. In addition to normal groovy features, You have the following
variables at your disposal inside your script:

* `json` - the original JSON file, parsed into a map
* `platform` - either `"quilt"` or `"forge"`, depending on which platform you are running on
* `override` - a method which, if called, will cause any further scripts attempting to modify the same file to not run

Multiple scripts in different datapacks or resource packs can target the same file - they are ran in order from highest
priority (the "top" pack) to lowest priority (the "bottom" pack). If a script calls `override()`, no further scripts
will run.

## Servers

**Note:** As the groovy scripts that JsonWrangler loads can execute arbitrary code, JsonWrangler will not load scripts
from server resource packs. If you wish to load scripts from a server resource pack, you will have to copy it as a local
resource pack first.

## Examples

### Changing the texture of an item
Placing the following file at `assets/minecraft/models/item/apple.json.groovy` will change the texture of the apple to
use the potato texture:
```groovy
json?.textures?.layer0 = 'minecraft:item/potato'

return json
```
