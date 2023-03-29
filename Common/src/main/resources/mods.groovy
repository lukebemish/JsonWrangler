ModsDotGroovy.make {
    modLoader = 'gml'
    loaderVersion = '[1,)'

    license = 'LGPL-3.0-or-later'
    issueTrackerUrl = 'https://github.com/lukebemish/JsonWrangler/issues'

    mod {
        modId = this.buildProperties['mod_id']
        displayName = this.buildProperties['mod_name']
        version = this.version
        group = this.group
        intermediate_mappings = 'net.fabricmc:intermediary'
        displayUrl = 'https://github.com/lukebemish/JsonWrangler'

        description = "Adds modularly defined metals"
        authors = [this.buildProperties['mod_author'] as String]

        dependencies {
            minecraft = this.minecraftVersionRange

            forge {
                versionRange = ">=${this.forgeVersion}"
            }
            onForge {
                mod('gml') {
                    versionRange = ">=${this.libs.versions.gml}"
                }
            }

            onQuilt {
                mod('groovyduvet') {
                    versionRange = ">=${this.libs.versions.groovyduvet}"
                }
            }

            quiltLoader {
                versionRange = ">=${this.quiltLoaderVersion}"
            }
        }

        entrypoints {
            init = [
                    adapted {
                        adapter = 'groovyduvet'
                        value = 'dev.lukebemish.jsonwrangler.quilt.JsonWranglerQuilt'
                    }
            ]
        }
    }
    onQuilt {
        mixin = [
            'mixin.jsonwrangler.json'
        ]
    }
}
