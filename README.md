# DFU Data Fixing Notes
Note: this assumes a basic understanding of optics, type systems, and Codecs

## Prior Reading
- https://docs.fabricmc.net/develop/codecs
- https://docs.neoforged.net/docs/datastorage/codecs/

A basic understanding of Codecs and DFU's type system is required if you want to understand this document.
These codecs are the other half to this data fixing system basically, this system would not work without their 
underlying logic.

## Type System
### Dynamic
Represents any serialized value in any format (NBT, JSON, etc.)
This is the format used by Codecs between serialization and deserialization stages.
Dynamics have the following type system:
- All java numeric types (byte, short, int, longs, floats, doubles)
- Maps (represented as `Stream<Pair<T>>`)
- Lists (represented as `Stream<T>`)

## Data-fixing Core

### DataFixer
A DataFixer is an individual fix applied to a schema and value.

### DataFixerBuilder
A builder for a series of DataFixer transformations.
- `addSchema` adds a schema to the rules
- `addFixer` adds a data fixer to the rules

`addFixer` should be called *after* the `addSchema`.
`addSchema` calls should also generally be nested.
```
var dataFixerBuilder = new DataFixerBuilder(2);
var v0 = dataFixerBuilder.addSchema(0, foobar.RootSchema::new);
var v1 = dataFixerBuilder.addSchema(1, foobar.FooDoubledSchema::new);
dataFixerBuilder.addFixer(new foobar.FooDoublingFixer(v1, true));
var v2 = dataFixerBuilder.addSchema(2, foobar.BarFieldMigrationSchema::new);
dataFixerBuilder.addFixer(new foobar.BarMigrationFixer(v2, true));
```

## Schema
Holds a record of all the types that exist, and their current TypeTemplates.
A root schema must exist registering at least one recursive type. (Issue #45 on DFU Repo)

Schemas have parents that define whatever comes before them in previous versions.
To create a parentless root schema, you must create a new Schema that overrides all
methods and creates a recursive-marked type (even if the type itself isn't recursive).

Schemas use integer versions to set apart what version they're in, in ascending order.
Version 1 comes after version 0.

### TypeReference
A reference to a specific type in the code (e.g worlds, chunks, blocks, etc.)
This is used by Schemas to track changes to a type across versions.

### TypeRewriteRule
A rule for changing the format of a type.

#### Most methods
Most methods are simple:
- `nop` does no rewriting
- `seq` applies multiple type rewrite rules in a row
- `orElse` applies the first rewrite rule if it succeeds, otherwise applies the second

There are more methods, but I'm not sure how exactly they work:
- `all` applies all the type rewrite rule, and only returns a result if all of them returns.
- `checkOnce` currently does literally nothing ????
- `everywhere` appears to recursively apply a type rewrite rule as much as possible
- `ifSame` returns a successful rewrite result if the type supplied to it, 
and the type the rewrite result worked with, are the same type

A few are responsible for applying the primary logic behind rewrite rules.

#### fixTypeEverywhereTyped
This uses optics to find all instances of a type and apply a modifier to them.
In an ideal situation, you should be using the optics in this function.
If you get "No such children" errors when using the optics, you should fall back to other methods.

#### writeFixAndRead
This uses a fallback of the Dynamic instances to allow you to make any changes.
Note that this does not seem

#### Patterns
Methods ending in `Typed` operate on a `Typed` instance, which appears to be a `Dynamic` + a `Type`.
Methods not ending in `Typed` operate on a `Dynamic` instance.
## Error Handling
If you get an error, you most likely used one of the methods wrong or violated the rules of a schema.
If you get a "No more children" error, an optic tried to traverse into something that doesn't exist.