# MechanicalSystemGame

## Classes

### Grid

The topmost container that contains all `Block`'s in a map and arses all `Block`'s and `BlockFeature`'s to multiple `SystemFeature`'s

### Block
Individual Block, contains multiple `Feature`

### BlockFeature
A specific feature of a block, belongs to a feature type

### SystemFeature
A container that holds an interconnected group of `BlockFeature`

### FeatureEntry
A registered entry that connects `BlockFeature` to `SystemFeature`

### Registrar
A place to register all `FeatureEntry`'s